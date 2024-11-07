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

import com.etas.e2e.tools.client.FoldingServiceGrpc
import com.etas.e2e.tools.client.FoldingServiceOuterClass
import com.etas.e2e.tools.client.FoldingServiceOuterClass.FoldMirrorsResponse
import com.etas.e2e.tools.client.FoldingServiceOuterClass.GetMirrorFoldStatusResponse
import com.etas.e2e.tools.client.FoldingServiceOuterClass.GetMirrorFoldStatusResponse.GetMirrorFoldStatus
import com.etas.e2e.tools.client.FoldingServiceOuterClass.UnfoldMirrorsResponse
import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.eclipse.kuksa.proto.v2.Types.Datapoint
import org.eclipse.kuksa.proto.v2.Types.SignalID
import org.eclipse.kuksa.proto.v2.Types.Value
import org.eclipse.velocitas.sdk.grpc.AsyncBrokerGrpcFacade

class FoldingServiceImpl(val brokerGrpcFacade: AsyncBrokerGrpcFacade) : FoldingServiceGrpc.FoldingServiceImplBase() {

    override fun unfoldMirrors(
        request: FoldingServiceOuterClass.UnfoldMirrorsRequest,
        responseObserver: StreamObserver<UnfoldMirrorsResponse>,
    ) {
        val signalId = SignalID.newBuilder()
            .setPath("Vehicle.Custom.Media.Media0x4c1.Voicectrlextmirrorfold4c1")
            .build()

        val value = Value.newBuilder()
            .setUint32(GetMirrorFoldStatus.STATUS_EXPANDED_VALUE)
            .build()
        val datapoint = Datapoint.newBuilder()
            .setValue(value)
            .build()

        try {
            val brokerResponse = brokerGrpcFacade.publishValue(signalId, datapoint).get()
        } catch (e: Exception) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.message).asRuntimeException())
            return
        }

        val response = UnfoldMirrorsResponse.newBuilder().build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun foldMirrors(
        request: FoldingServiceOuterClass.FoldMirrorsRequest,
        responseObserver: StreamObserver<FoldMirrorsResponse>,
    ) {
        val signalId = SignalID.newBuilder()
            .setPath("Vehicle.Custom.Media.Media0x4c1.Voicectrlextmirrorfold4c1")
            .build()

        val value = Value.newBuilder()
            .setUint32(GetMirrorFoldStatus.STATUS_FOLDED_VALUE)
            .build()
        val datapoint = Datapoint.newBuilder()
            .setValue(value)
            .build()

        val brokerResponse = try {
            brokerGrpcFacade.publishValue(signalId, datapoint).get()
        } catch (e: Exception) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.message).asRuntimeException())
            return
        }

        val response = FoldMirrorsResponse.newBuilder().build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun getMirrorFoldStatus(
        request: FoldingServiceOuterClass.GetMirrorFoldStatusRequest,
        responseObserver: StreamObserver<GetMirrorFoldStatusResponse>,
    ) {
        val signalId = SignalID.newBuilder()
            .setPath("Vehicle.Custom.Media.Media0x4c1.Voicectrlextmirrorfold4c1")
            .build()

        val brokerResponse = try {
            brokerGrpcFacade.getValue(signalId).get()
        } catch (e: Exception) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.message).asRuntimeException())
            return
        }

        val value = brokerResponse.dataPoint.value.uint32

        val responseBuilder = GetMirrorFoldStatusResponse.newBuilder()
        when (value) {
            GetMirrorFoldStatus.STATUS_INVALID_VALUE -> {
                responseObserver.onError(Status.INVALID_ARGUMENT.asRuntimeException())
                return
            }

            GetMirrorFoldStatus.STATUS_EXPANDED_VALUE -> {
                responseBuilder.setStatus(GetMirrorFoldStatus.STATUS_EXPANDED_VALUE)
            }

            GetMirrorFoldStatus.STATUS_FOLDED_VALUE -> {
                responseBuilder.setStatus(GetMirrorFoldStatus.STATUS_FOLDED_VALUE)
            }

            GetMirrorFoldStatus.STATUS_RESERVED_VALUE -> {
                responseBuilder.setStatus(GetMirrorFoldStatus.STATUS_RESERVED_VALUE)
            }
        }

        responseObserver.onNext(responseBuilder.build())
        responseObserver.onCompleted()
    }
}
