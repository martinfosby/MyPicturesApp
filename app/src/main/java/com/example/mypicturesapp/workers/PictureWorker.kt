package com.example.mypicturesapp.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mypicturesapp.PicturesApplication
import com.example.mypicturesapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "PictureWorker"

class PictureWorker(
    val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val app = context as PicturesApplication

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val repos = app.container.networkPicturesRepository
                Result.success()
            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_getting_repository),
                    throwable
                )
                Result.failure()
            }
        }
    }
}