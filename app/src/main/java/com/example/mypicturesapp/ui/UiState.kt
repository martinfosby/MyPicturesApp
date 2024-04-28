package com.example.mypicturesapp.ui

import com.example.mypicturesapp.model.PictureInterface

interface UiState {
    data class Success(
        val photos: List<PictureInterface>,
    ) : UiState
    object Error : UiState
    object Loading : UiState
}