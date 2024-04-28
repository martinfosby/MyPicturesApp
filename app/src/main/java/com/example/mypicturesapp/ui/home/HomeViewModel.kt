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
package com.example.mypicturesapp.ui.home

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypicturesapp.data.network.NetworkPicturesRepository
import com.example.mypicturesapp.data.offline.OfflinePicturesRepository
import com.example.mypicturesapp.data.offline.PictureEntity
import com.example.mypicturesapp.data.network.PictureRemote
import com.example.mypicturesapp.model.PictureInterface
import com.example.mypicturesapp.ui.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface HomeNetworkUiState: UiState {
    data class Success(
        val photos: List<PictureRemote>,
    ) : HomeNetworkUiState

    object Error : HomeNetworkUiState
    object Loading : HomeNetworkUiState
}

data class HomeOfflineUiState(val pictureList: List<PictureEntity> = listOf())

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class HomeViewModel(
    private val networkPicturesRepository: NetworkPicturesRepository,
    private val offlinePicturesRepository: OfflinePicturesRepository,
) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var homeNetworkUiState: HomeNetworkUiState by mutableStateOf(HomeNetworkUiState.Loading)
        private set

    var homeOfflineUiState: StateFlow<HomeOfflineUiState> =
        offlinePicturesRepository.getAllPicturesStream().map { HomeOfflineUiState(it) }
            .filterNotNull()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeOfflineUiState()
            )

    var pictureUiState by mutableStateOf(PictureUiState())
        private set

    init {
        getPhotosFromNetwork()
    }

    fun getPhotosFromNetwork() {
        viewModelScope.launch {
            homeNetworkUiState = HomeNetworkUiState.Loading
            homeNetworkUiState = try {
                HomeNetworkUiState.Success(networkPicturesRepository.getAllPicturesStream())
            } catch (e: IOException) {
                HomeNetworkUiState.Error
            } catch (e: HttpException) {
                HomeNetworkUiState.Error
            }
        }
    }

    suspend fun savePicture(pictureInterface: PictureInterface) {
        offlinePicturesRepository.insert(pictureInterface.toPictureEntity())
    }

    suspend fun deletePicture(pictureInterface: PictureInterface) {
        offlinePicturesRepository.delete(pictureInterface.toPictureEntity())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class PictureUiState(
    val pictureDetails: PictureDetails = PictureDetails(),
)

data class PictureDetails(
    val id: Int = 0,
    val albumId: Int = 0,
    val title: String = "",
    val url: String = "",
    val thumbnailUrl: String = "",
)

fun PictureDetails.toPictureEntity(): PictureEntity = PictureEntity(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)


fun PictureRemote.toPictureEntity(): PictureEntity = PictureEntity(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)

fun PictureInterface.toPictureEntity(): PictureEntity = PictureEntity(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)