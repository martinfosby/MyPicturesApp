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
package com.example.mypicturesapp.data

import android.content.Context
import com.example.mypicturesapp.data.network.NetworkPicturesRepository
import com.example.mypicturesapp.data.offline.OfflinePicturesRepository
import com.example.mypicturesapp.data.offline.PictureDatabase
import com.example.mypicturesapp.data.network.PicturesApiService
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

interface AppContainer {
    val networkPicturesRepository: NetworkPicturesRepository
    val offlinePicturesRepository: OfflinePicturesRepository
}


/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppDataContainer(private val context: Context) : AppContainer {
    private val baseUrl = "https://jsonplaceholder.typicode.com/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: PicturesApiService by lazy {
        retrofit.create(PicturesApiService::class.java)
    }


    override val networkPicturesRepository: NetworkPicturesRepository by lazy {
        NetworkPicturesRepository(retrofitService)
    }
    override val offlinePicturesRepository: OfflinePicturesRepository by lazy {
        OfflinePicturesRepository(PictureDatabase.getDatabase(context).pictureDao())
    }
}
