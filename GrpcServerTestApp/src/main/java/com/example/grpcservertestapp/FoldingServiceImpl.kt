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
import io.grpc.stub.StreamObserver
import org.eclipse.velocitas.sdk.grpc.BrokerGrpcFacade

class FoldingServiceImpl(brokerGrpcFacade: BrokerGrpcFacade) : FoldingServiceGrpc.FoldingServiceImplBase() {
    private var mirrorFoldingStatus: Int = 0

    override fun unfoldMirrors(
        request: FoldingServiceOuterClass.UnfoldMirrorsRequest?,
        responseObserver: StreamObserver<UnfoldMirrorsResponse>?,
    ) {
        mirrorFoldingStatus = GetMirrorFoldStatus.STATUS_EXPANDED_VALUE

        val response = UnfoldMirrorsResponse.newBuilder().build()
        responseObserver?.onNext(response)
    }

    override fun foldMirrors(
        request: FoldingServiceOuterClass.FoldMirrorsRequest?,
        responseObserver: StreamObserver<FoldMirrorsResponse>?,
    ) {
        mirrorFoldingStatus = GetMirrorFoldStatus.STATUS_FOLDED_VALUE

        val response = FoldMirrorsResponse.newBuilder().build()
        responseObserver?.onNext(response)
    }

    override fun getMirrorFoldStatus(
        request: FoldingServiceOuterClass.GetMirrorFoldStatusRequest?,
        responseObserver: StreamObserver<GetMirrorFoldStatusResponse>?,
    ) {
        val response = GetMirrorFoldStatusResponse.newBuilder()
            .setStatus(mirrorFoldingStatus)
            .build()
        responseObserver?.onNext(response)
    }
}
