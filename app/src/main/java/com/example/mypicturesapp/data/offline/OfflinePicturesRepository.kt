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

package com.example.mypicturesapp.data.offline

import com.example.mypicturesapp.data.PicturesRepository
import kotlinx.coroutines.flow.Flow

class OfflinePicturesRepository(private val pictureDao: PictureDao): PicturesRepository {
    fun getAllPicturesStream(): Flow<List<PictureEntity>> = pictureDao.getAllPictures()
    fun getPictureStream(id: Int): Flow<PictureEntity?> = pictureDao.getPicture(id)
    suspend fun insert(item: PictureEntity) = pictureDao.insert(item)
    suspend fun delete(item: PictureEntity) = pictureDao.delete(item)

}