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
import io.grpc.stub.StreamObserver
import org.eclipse.kuksa.proto.v2.Types.SignalID
import org.eclipse.velocitas.sdk.grpc.BrokerGrpcFacade

class FronthousingServiceImpl(val brokerGrpcFacade: BrokerGrpcFacade) : FronthousingServiceImplBase() {
    override fun getAirConditioningStatus(
        request: FronthousingServiceOuterClass.GetAirConditioningStatusRequest?,
        responseObserver: StreamObserver<FronthousingServiceOuterClass.GetAirConditioningStatusResponse>?,
    ) {
        val signalId = SignalID.newBuilder()
            .setPath("Vehicle.Cabin.HVAC.Fronthousing.AchmcAcOnStatusS")
            .build()

        brokerGrpcFacade.getValue(signalId)

        super.getAirConditioningStatus(request, responseObserver)
    }

    override fun switchOffAirConditioning(
        request: FronthousingServiceOuterClass.SwitchOffAirConditioningRequest?,
        responseObserver: StreamObserver<FronthousingServiceOuterClass.SwitchOffAirConditioningResponse>?,
    ) {
        super.switchOffAirConditioning(request, responseObserver)
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
