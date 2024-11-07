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

package com.example.grpcservertestapp

import io.grpc.Grpc
import io.grpc.InsecureChannelCredentials
import io.grpc.ServerBuilder
import org.eclipse.velocitas.sdk.grpc.BrokerGrpcFacade

fun main(args: Array<String>) {
    val host = "localhost"
    val port = 55556
    val credentials = InsecureChannelCredentials.create()
    val channel = Grpc.newChannelBuilderForAddress(host, port, credentials).build()
    val brokerGrpcFacade = BrokerGrpcFacade(channel)

    val foldingServiceImpl = FoldingServiceImpl(brokerGrpcFacade)
    val fronthousingServiceImpl = FronthousingServiceImpl(brokerGrpcFacade)

    val server = ServerBuilder.forPort(12345)
        .addService(foldingServiceImpl)
        .addService(fronthousingServiceImpl)
        .build()

    server.start()

    while (true) {
        // accept requests
    }
}
