package com.example.mypicturesapp.data.network

import kotlinx.serialization.Serializable


@Serializable
data class AlbumRemote(
    val userId: Int,
    val id: Int,
    val title: String,
)