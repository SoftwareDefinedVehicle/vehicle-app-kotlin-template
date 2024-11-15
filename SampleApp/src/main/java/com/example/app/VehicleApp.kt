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

import com.example.logger.LogcatLoggingStrategy
import org.eclipse.velocitas.sdk.VehicleApplication
import org.eclipse.velocitas.sdk.logging.Logger

class VehicleApp : VehicleApplication() {
    init {
        Logger.loggingStrategy = LogcatLoggingStrategy
    }

    override fun onStart() {
        // unused
    }

    override fun onStop() {
        // unused
    }
}