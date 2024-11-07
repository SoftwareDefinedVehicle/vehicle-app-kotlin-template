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

import android.widget.Toast
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarIcon
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import org.eclipse.velocitas.sdk.logging.Logger

/**
 * The VehicleAppScreen to show information on the car infotainment system.
 *
 * @see Screen
 */
class VehicleAppScreen(carContext: CarContext, val vehicleApp: VehicleApp) : Screen(carContext) {
    override fun onGetTemplate(): Template {
        val unfoldMirrorAction = Action.Builder()
            .setTitle("Unfold Mirrors")
            .setIcon(CarIcon.APP_ICON)
            .setOnClickListener {
                vehicleApp.unfoldMirrors()
                Toast.makeText(carContext, "Unfolding Mirror", Toast.LENGTH_SHORT).show()
            }.build()

        val row1 = Row.Builder()
            .setTitle("Unfold Mirrors")
            .addAction(unfoldMirrorAction)
            .build()

        val foldMirrorAction = Action.Builder()
            .setTitle("Fold Mirrors")
            .setIcon(CarIcon.APP_ICON)
            .setOnClickListener {
                vehicleApp.foldMirrors()
                Toast.makeText(carContext, "Folding Mirror", Toast.LENGTH_SHORT).show()
            }.build()
        val row2 = Row.Builder()
            .setTitle("Fold Mirrors")
            .addAction(foldMirrorAction)
            .build()

        val getMirrorFoldStatusAction = Action.Builder()
            .setTitle("Get Mirror Fold Status")
            .setIcon(CarIcon.APP_ICON)
            .setOnClickListener {
                val mirrorFoldStatus = vehicleApp.getMirrorFoldStatus()
                Logger.info("", "mirrorFoldStatus: $mirrorFoldStatus")
                Toast.makeText(carContext, "Mirror Folding Status: $mirrorFoldStatus", Toast.LENGTH_SHORT).show()
            }.build()

        val row3 = Row.Builder()
            .setTitle("Get Mirror Fold Status")
            .addAction(getMirrorFoldStatusAction)
            .build()

        val pane = Pane.Builder()
            .addRow(row1)
            .addRow(row2)
            .addRow(row3)
            .build()

        return PaneTemplate.Builder(pane)
            .setHeaderAction(Action.APP_ICON)
            .build()
    }
}
