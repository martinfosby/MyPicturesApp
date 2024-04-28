/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mypicturesapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mypicturesapp.ui.details.DetailsDestination
import com.example.mypicturesapp.ui.home.HomeDestination
import com.example.mypicturesapp.ui.home.HomeScreen
import com.example.mypicturesapp.ui.details.DetailsScreen
import com.example.mypicturesapp.ui.details.DetailsViewModel
import com.example.mypicturesapp.ui.home.HomeViewModel
import kotlinx.coroutines.launch

/**
 * Provides Navigation graph for the application.
 */
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PictureNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                onShowClicked = {
                    navController.navigate("${DetailsDestination.route}/${it}")
                },
            )
        }
        composable(
            route = DetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailsDestination.pictureIdArg) {
                type = NavType.IntType
            })
        ) {
            DetailsScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}
