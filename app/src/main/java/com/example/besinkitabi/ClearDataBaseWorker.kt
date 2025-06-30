package com.example.besinkitabi
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ClearDataBaseWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            // Veritabanı referansını al
            val foodsDB = Room.databaseBuilder(
                applicationContext,
                FoodsDatabase::class.java, "foods"
            ).build()
            val foodsDAO = foodsDB.foodDAO()

            // Veritabanındaki tüm verileri sil
            withContext(Dispatchers.IO) {
                foodsDAO.deleteAllFoods()
            }
            Result.success()
        }catch (e: Exception) {
            Result.failure()
        }
    }
}