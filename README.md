# vehicle-app-kotlin-template

Vehicle App template for Kotlin

This project is in incubation status. Not all required functionality might be migrated yet.

## Overview

This Velocitas template has a native Android Gradle project setup. It does not use Visual Studio Code Dev Containers
like the other template repositories. Every dependency needed is provided by the Gradle build
system.

## gRPC Client Generation

The vehicle-app-kotlin-template can automatically generate a gRPC Client based on a proto service
definition (*.proto) as well as the corresponding client logic in .json format.

The grpc-client module is used for the generation of the gRPC client. The app project automatically
includes the sources generated inside the grpc-client project.

Required Software:

- Python 3.12.0

Other versions might work, however are untested.

Some requirements must be met:

- Proto files should be named as ...Service.proto. If the file cannot be named ...Service.proto,
  e.g. door.proto, the proto file must use the protobuf generator option `java_outer_classname`and
  define it like this: `option java_outer_classname = "DoorServiceOuterClass";`
- Proto files and client logic must have a matching package, e.g.:
  DoorService.json
  ```
  {
  "VehicleSdk": {
    "name": "DoorServiceClient",
    "package": "com.etas.e2e.service.door.client",
    "host": {
      "hostname": "",
      "port": 55555,
      "tls": false,
      "auth": null
    },
    [...]
  ```
  DoorService.proto
  ```
  syntax = "proto3";

  package com.etas.e2e.service.door.client;

  service DoorService {
    rpc OpenDoor(OpenDoorRequest) returns (OpenDoorResponse);
  }
  
  [...]
  ```

Some manual steps are mandatory:

1) Copy Protobuf Service files to `grpc-client/src/main/proto`.
2) Copy Client Logic JSON files to `grpc-client/src/main/resources`.

Generated code can be found in `grpc-client/build/generated/source/proto/main/java`

If the Service Client should not be generated automatically, remove the following lines from
grpc-client/build.gradle:

```
    tasks.getByName("compileJava") {
        dependsOn("generateGrpcServiceClientFor$name")
    }
```

The gRPC Client can then be generated manually using the corresponding gradle tasks, e.g.
DoorService.json:

- generateGrpcServiceClientForDoorService

A jar can be build using `./gradlew clean dist`.
Optionally a fat jar can be generated using `./gradlew clean distFatJar`.
The generated artifacts can be found in `grpc-client/build/libs`.

## Quickstart

The Android Auto Desktop Head Unit Emulator is required for testing. The minimum supported version is Android 12.

1) Use the SDK Manager -> SDK tools in Android Studio to install the Android Auto Desktop Head Unit
   Emulator.
2) Use the SDK Manager -> SDK Platforms to install the Android Automotive System Image for your
   architecture
3) Create a new Android Automotive Virtual Device with the above installed system image
4) Deploy the app to the emulator

