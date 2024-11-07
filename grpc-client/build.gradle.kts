/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java-library")
    alias(libs.plugins.protobuf)
    alias(libs.plugins.shadow)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

protobuf {
    protoc {
        artifact = libs.protoc.asProvider().get().toString()
    }
    plugins {
        create("java") {
            artifact = libs.protoc.gen.grpc.java.get().toString()
        }
        create("grpc") {
            artifact = libs.protoc.gen.grpc.java.get().toString()
        }
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                named("java") {
                    option("lite")
                }
            }
            it.plugins {
                create("grpc") {
                    option("lite")
                }
            }
        }
    }
}

tasks.register<Javadoc>("generateApiDocs") {
    group = "grpc"

    val mainSrcSet = sourceSets.main.get()
    classpath = mainSrcSet.runtimeClasspath
    source = mainSrcSet.allJava
    options {
        encoding("UTF-8")
        charset("UTF-8")
    }
}

tasks.register<Exec>("installDependencies") {
    group = "grpc"

    workingDir("$projectDir/tools")
    commandLine("python3", "-m", "pip", "install", "-r", "./requirements.txt")
}

fileTree("$projectDir/src/main/resources").forEach { file ->
    val name = file.nameWithoutExtension
    tasks.register<Exec>("generateGrpcServiceClientFor$name") {
        group = "grpc"
        dependsOn("installDependencies", "generateProto")

        workingDir("$projectDir/tools")
        val inputFile = file.path
        commandLine("python3", "generate_code.py", inputFile)
    }

    tasks.getByName("compileJava") {
        dependsOn("generateGrpcServiceClientFor$name")
    }
}

tasks.register<Jar>("javadocJar") {
    group = "grpc"
    dependsOn("generateApiDocs")

    archiveClassifier.set("javadoc")
    from("$projectDir/build/docs/javadoc")
}

tasks.register("dist") {
    group = "grpc"
    dependsOn("jar", "javadocJar")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("fat")
    mergeServiceFiles()
}

tasks.register("distFatJar") {
    group = "grpc"
    dependsOn("shadowJar", "javadocJar")
}

dependencies {
    implementation(libs.grpc.netty.shaded)
    api(libs.grpc.protobuf.lite)
    api(libs.grpc.stub)
    implementation(libs.javax.annotation.api)
}
