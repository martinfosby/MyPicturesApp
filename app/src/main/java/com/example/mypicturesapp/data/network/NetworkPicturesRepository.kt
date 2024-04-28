package com.example.mypicturesapp.data.network

import android.content.Context
import androidx.work.WorkManager
import com.example.mypicturesapp.data.PicturesRepository


class NetworkPicturesRepository(
    private val picturesApiService: PicturesApiService,
//    context: Context,
): PicturesRepository {
//    private val workManager = WorkManager.getInstance(context)

    suspend fun getAllPicturesStream(): List<PictureRemote> = picturesApiService.getPhotos()
    suspend fun getPictureStream(id: Int): PictureRemote = picturesApiService.getPhotoById(id)
    suspend fun getAlbumById(id: Int): AlbumRemote = picturesApiService.getAlbumById(id)
}