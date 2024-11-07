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

package com.example.app

import com.etas.e2e.tools.client.FoldingServiceGrpc
import com.etas.e2e.tools.client.FoldingServiceOuterClass.FoldMirrorsRequest
import com.etas.e2e.tools.client.FoldingServiceOuterClass.GetMirrorFoldStatusRequest
import com.etas.e2e.tools.client.FoldingServiceOuterClass.GetMirrorFoldStatusResponse.GetMirrorFoldStatus
import com.etas.e2e.tools.client.FoldingServiceOuterClass.UnfoldMirrorsRequest
import com.etas.e2e.tools.client.ServiceClient1
import com.example.logger.LogcatLoggingStrategy
import io.grpc.Grpc
import io.grpc.InsecureChannelCredentials
import org.eclipse.velocitas.sdk.VehicleApplication
import org.eclipse.velocitas.sdk.logging.Logger

class VehicleApp : VehicleApplication() {
    init {
        Logger.loggingStrategy = LogcatLoggingStrategy
    }

    val serviceClient = ServiceClient1("10.0.2.2", 12345)

    // replace with generated client
    val serviceChannel = Grpc.newChannelBuilderForAddress(
        "10.0.2.2",
        12345,
        InsecureChannelCredentials.create()
    ).build()
    val foldingService = FoldingServiceGrpc.newFutureStub(serviceChannel)

    override fun onStart() {
        serviceClient.connect()
    }

    override fun onStop() {
        serviceClient.shutdown()
    }

    // TODO replace with generated client
    fun foldMirrors() {
        val request = FoldMirrorsRequest.getDefaultInstance()
        val response = foldingService.foldMirrors(request).get()
    }

    fun unfoldMirrors() {
        val request = UnfoldMirrorsRequest.getDefaultInstance()
        val response = foldingService.unfoldMirrors(request).get()
    }

    fun getMirrorFoldStatus(): GetMirrorFoldStatus {
        val request = GetMirrorFoldStatusRequest.getDefaultInstance()
        val response = foldingService.getMirrorFoldStatus(request).get()
        return GetMirrorFoldStatus.forNumber(response.status)
    }
}
