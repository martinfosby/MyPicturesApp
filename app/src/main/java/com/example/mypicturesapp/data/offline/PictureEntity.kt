package com.example.mypicturesapp.data.offline

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mypicturesapp.model.PictureInterface

@Entity("picture")
data class PictureEntity(
    @PrimaryKey
    override val id: Int,
    override val albumId: Int,
    override val title: String,
    override val url: String,
    override val thumbnailUrl: String,
) : PictureInterface
