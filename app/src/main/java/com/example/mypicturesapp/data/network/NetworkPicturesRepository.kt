package com.example.mypicturesapp.data.network

import com.example.mypicturesapp.data.PicturesRepository


class NetworkPicturesRepository(
    private val picturesApiService: PicturesApiService,
): PicturesRepository {
    suspend fun getAllPicturesStream(): List<PictureRemote> = picturesApiService.getPhotos()
    suspend fun getPictureStream(id: Int): PictureRemote = picturesApiService.getPhotoById(id)
    suspend fun getAlbumById(id: Int): AlbumRemote = picturesApiService.getAlbumById(id)
}