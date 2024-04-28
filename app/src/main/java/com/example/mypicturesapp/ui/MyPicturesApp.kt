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

@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mypicturesapp.ui

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mypicturesapp.R
import com.example.mypicturesapp.ui.navigation.PictureNavHost
import com.example.mypicturesapp.ui.home.HomeViewModel
import com.example.mypicturesapp.ui.utils.ReplyContentType
import com.example.mypicturesapp.ui.utils.ReplyNavigationType

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MyPicturesApp(
    windowSize: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController(),
    ) {
        PictureNavHost(navController = navController)
//
//        val navigationType: ReplyNavigationType
//        val contentType: ReplyContentType
//
//        when (windowSize) {
//            WindowWidthSizeClass.Compact -> {
//                navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
//                contentType = ReplyContentType.LIST_ONLY
//            }
//            WindowWidthSizeClass.Medium -> {
//                navigationType = ReplyNavigationType.NAVIGATION_RAIL
//                contentType = ReplyContentType.LIST_ONLY
//            }
//            WindowWidthSizeClass.Expanded -> {
//                navigationType = ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER
//                contentType = ReplyContentType.LIST_AND_DETAIL
//            }
//            else -> {
//                navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
//                contentType = ReplyContentType.LIST_ONLY
//            }
//        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PicturesTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}
