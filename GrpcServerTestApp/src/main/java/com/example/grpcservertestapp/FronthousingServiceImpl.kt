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

import com.etas.e2e.tools.client.FronthousingServiceGrpc.FronthousingServiceImplBase
import com.etas.e2e.tools.client.FronthousingServiceOuterClass
import com.etas.e2e.tools.client.FronthousingServiceOuterClass.GetAirConditioningStatusResponse
import com.etas.e2e.tools.client.FronthousingServiceOuterClass.GetAirConditioningStatusResponse.AirConditioningStatus
import com.etas.e2e.tools.client.FronthousingServiceOuterClass.SwitchOffAirConditioningResponse
import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.eclipse.kuksa.proto.v2.Types.Datapoint
import org.eclipse.kuksa.proto.v2.Types.SignalID
import org.eclipse.kuksa.proto.v2.Types.Value
import org.eclipse.velocitas.sdk.grpc.AsyncBrokerGrpcFacade

class FronthousingServiceImpl(val brokerGrpcFacade: AsyncBrokerGrpcFacade) : FronthousingServiceImplBase() {
    override fun getAirConditioningStatus(
        request: FronthousingServiceOuterClass.GetAirConditioningStatusRequest,
        responseObserver: StreamObserver<GetAirConditioningStatusResponse>,
    ) {
        val signalId = SignalID.newBuilder()
            .setPath("Vehicle.Cabin.HVAC.Fronthousing.AchmcAcOnStatusS")
            .build()

        val brokerResponse = try {
            brokerGrpcFacade.getValue(signalId).get()
        } catch (e: Exception) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.message).asRuntimeException())
            return
        }

        val value = brokerResponse.dataPoint.value.uint32

        val responseBuilder = GetAirConditioningStatusResponse.newBuilder()
        when (value) {
            AirConditioningStatus.STATUS_INVALID_VALUE -> {
                responseBuilder.setStatus(AirConditioningStatus.STATUS_INVALID_VALUE)
            }

            AirConditioningStatus.STATUS_TURN_ON_VALUE -> {
                responseBuilder.setStatus(AirConditioningStatus.STATUS_TURN_ON_VALUE)
            }

            AirConditioningStatus.STATUS_TURN_OFF_VALUE -> {
                responseBuilder.setStatus(AirConditioningStatus.STATUS_TURN_OFF_VALUE)
            }

            AirConditioningStatus.STATUS_RESERVED_VALUE -> {
                responseBuilder.setStatus(AirConditioningStatus.STATUS_RESERVED_VALUE)
            }

            else -> {
                responseObserver.onError(Status.INVALID_ARGUMENT.asRuntimeException())
            }
        }

        responseObserver.onNext(responseBuilder.build())
    }

    override fun switchOffAirConditioning(
        request: FronthousingServiceOuterClass.SwitchOffAirConditioningRequest,
        responseObserver: StreamObserver<SwitchOffAirConditioningResponse>,
    ) {
        val requestedValue = request.acswitch
        when (requestedValue) {
            AirConditioningStatus.STATUS_INVALID_VALUE -> {
                responseObserver.onError(Status.INVALID_ARGUMENT.asRuntimeException())
                return
            }

            AirConditioningStatus.STATUS_TURN_ON_VALUE -> {
                val signalId = SignalID.newBuilder()
                    .setPath("Vehicle.Cabin.HVAC.Fronthousing.AchmcAcOnStatusS")
                    .build()

                val value = Value.newBuilder()
                    .setUint32(AirConditioningStatus.STATUS_TURN_ON_VALUE)
                    .build()

                val datapoint = Datapoint.newBuilder()
                    .setValue(value)
                    .build()
                brokerGrpcFacade.publishValue(signalId, datapoint)
            }

            AirConditioningStatus.STATUS_TURN_OFF_VALUE -> {
                val signalId = SignalID.newBuilder()
                    .setPath("Vehicle.Cabin.HVAC.Fronthousing.AchmcAcOnStatusS")
                    .build()

                val value = Value.newBuilder()
                    .setUint32(AirConditioningStatus.STATUS_TURN_OFF_VALUE)
                    .build()

                val datapoint = Datapoint.newBuilder()
                    .setValue(value)
                    .build()
                brokerGrpcFacade.publishValue(signalId, datapoint)
            }

            AirConditioningStatus.STATUS_RESERVED_VALUE -> {
                val signalId = SignalID.newBuilder()
                    .setPath("Vehicle.Cabin.HVAC.Fronthousing.AchmcAcOnStatusS")
                    .build()

                val value = Value.newBuilder()
                    .setUint32(AirConditioningStatus.STATUS_RESERVED_VALUE)
                    .build()

                val datapoint = Datapoint.newBuilder()
                    .setValue(value)
                    .build()
                brokerGrpcFacade.publishValue(signalId, datapoint)
            }
        }

        val response = SwitchOffAirConditioningResponse.getDefaultInstance()
        responseObserver.onNext(response)
    }

    override fun getCycleMode(
        request: FronthousingServiceOuterClass.GetCycleModeRequest?,
        responseObserver: StreamObserver<FronthousingServiceOuterClass.GetCycleModeResponse>?,
    ) {
        super.getCycleMode(request, responseObserver)
    }

    override fun isFrontDefrostRunning(
        request: FronthousingServiceOuterClass.IsFrontDefrostRunningRequest?,
        responseObserver: StreamObserver<FronthousingServiceOuterClass.IsFrontDefrostRunningResponse>?,
    ) {
        super.isFrontDefrostRunning(request, responseObserver)
    }

    override fun setCircularKey(
        request: FronthousingServiceOuterClass.SetCircularKeyRequest?,
        responseObserver: StreamObserver<FronthousingServiceOuterClass.SetCircularKeyResponse>?,
    ) {
        super.setCircularKey(request, responseObserver)
    }

    override fun startFrontDefrost(
        request: FronthousingServiceOuterClass.StartFrontDefrostRequest?,
        responseObserver: StreamObserver<FronthousingServiceOuterClass.StartFrontDefrostResponse>?,
    ) {
        super.startFrontDefrost(request, responseObserver)
    }

}
