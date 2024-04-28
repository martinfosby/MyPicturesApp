package com.example.mypicturesapp.model

interface PictureInterface {
    val id: Int
    val albumId: Int
    val title: String
    val url: String
    val thumbnailUrl: String
}