package com.example.mypicturesapp.data.offline

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pictureEntity: PictureEntity)

    @Delete
    suspend fun delete(pictureEntity: PictureEntity)

    @Query("select * from picture")
    fun getAllPictures(): Flow<List<PictureEntity>>

    @Query("select * from picture where id = :id")
    fun getPicture(id: Int): Flow<PictureEntity>
}