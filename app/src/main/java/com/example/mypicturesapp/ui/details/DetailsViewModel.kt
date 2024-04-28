package com.example.mypicturesapp.ui.details

import com.example.mypicturesapp.data.network.AlbumRemote
import com.example.mypicturesapp.ui.UiState
import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypicturesapp.data.network.NetworkPicturesRepository
import com.example.mypicturesapp.data.offline.OfflinePicturesRepository
import com.example.mypicturesapp.data.network.PictureRemote
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface DetailsUiState: UiState {
    data class Success(
        val photo: PictureRemote,
        val album: AlbumRemote,
    ) : DetailsUiState

    object Error : DetailsUiState
    object Loading : DetailsUiState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val networkPicturesRepository: NetworkPicturesRepository,
    private val offlinePicturesRepository: OfflinePicturesRepository,
) : ViewModel() {

    private val pictureId: Int = checkNotNull(savedStateHandle[DetailsDestination.pictureIdArg])

    /** The mutable State that stores the status of the most recent request */
    var detailsUiState: DetailsUiState by mutableStateOf(DetailsUiState.Loading)
        private set

    init {
        getPhoto()
    }

    fun getPhoto() {
        viewModelScope.launch {
            val albumId: Int = networkPicturesRepository.getPictureStream(pictureId).albumId
            detailsUiState = DetailsUiState.Loading
            detailsUiState = try {
                DetailsUiState.Success(
                    networkPicturesRepository.getPictureStream(pictureId),
                    networkPicturesRepository.getAlbumById(albumId)
                )
            } catch (e: IOException) {
                DetailsUiState.Error
            } catch (e: HttpException) {
                DetailsUiState.Error
            }
        }
    }
}
