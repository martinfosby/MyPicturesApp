package com.example.mypicturesapp.data.offline

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [PictureEntity::class], version = 1, exportSchema = false)
abstract class PictureDatabase : RoomDatabase() {

    abstract fun pictureDao(): PictureDao

    companion object {
        @Volatile
        private var Instance: PictureDatabase? = null

        fun getDatabase(context: Context): PictureDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PictureDatabase::class.java, "picture_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}