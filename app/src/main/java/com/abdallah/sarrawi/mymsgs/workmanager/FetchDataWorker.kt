package com.abdallah.sarrawi.mymsgs.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel
import retrofit2.HttpException
import java.io.IOException


class FetchDataWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val apiService = ApiService.provideRetrofitInstance()
        val database = PostDatabase.getInstance(applicationContext)

        return try {
            refreshMsgsType(apiService, database)
            Result.success()
        } catch (e: Exception) {
            Log.e("Worker Error", "Error in fetching messages: ${e.message}")
            Result.retry()
        }
    }

    private suspend fun refreshMsgsType(apiService: ApiService, database: PostDatabase) {
            var page = 1
            var msgsTypesList: List<MsgsTypesModel>

            try {
                do {
                    Log.d("API Debug", "Fetching msgs types for page: $page")
                    val response = apiService.getMsgsTypes_Ser2(page)
                    if (response.isSuccessful) {
                        msgsTypesList = response.body()?.results?.MsgsTypesModel ?: emptyList()
                        if (msgsTypesList.isNotEmpty()) {
                            database.typesDao().insertPosts(msgsTypesList)
                            page++
                            for (nokatType in msgsTypesList) {
                                refreshMsgswithID(apiService, database, nokatType.id)
                            }
                        } else {
                            break // أوقف التكرار إذا لم يكن هناك بيانات
                        }
                    } else {
                        Log.e("API Error", response.errorBody()?.string() ?: "Unknown error")
                        msgsTypesList = emptyList()
                    }
                } while (msgsTypesList.isNotEmpty())
            } catch (e: IOException) {
                Log.e("Network Error", "Network error occurred: ${e.message}")
                throw e
            } catch (e: HttpException) {
                Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
            }

    }

    private suspend fun refreshMsgswithID(apiService: ApiService, database: PostDatabase, ID_Type_id: Int) {
        var page = 1
        val allMsgs = mutableListOf<MsgsModel>()
        var isLastPage = false

        try {
            do {
                Log.d("API Debug", "Fetching messages for ID_Type_id: $ID_Type_id, page: $page")
                val response = apiService.getMsgs_Ser2(ID_Type_id, page)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val msgList = responseBody?.results?.MsgsModel ?: emptyList()

                    if (msgList.isNotEmpty()) {
                        allMsgs.addAll(msgList)
                        page++
                    } else {
                        isLastPage = true
                    }
                } else {
                    when (response.code()) {
                        404 -> {
                            Log.e("API Error", "Page $page not found for ID_Type_id: $ID_Type_id, stopping fetch.")
                            isLastPage = true
                        }
                        else -> {
                            Log.e("API Error", "Failed to fetch messages: ${response.errorBody()?.string() ?: "Unknown error"}")
                            isLastPage = true
                        }
                    }
                }
            } while (!isLastPage)

            if (allMsgs.isNotEmpty()) {
                database.msgsDao().insert_msgs(allMsgs)
            } else {
                Log.d("API Info", "No messages to insert into the database for ID_Type_id: $ID_Type_id")
            }
        } catch (e: IOException) {
            Log.e("Network Error", "Network error occurred: ${e.message}")
            throw IOException("Network error", e)
        } catch (e: HttpException) {
            Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
        } catch (e: Exception) {
            Log.e("General Error", "An unexpected error occurred: ${e.message}")
        }
    }
}


/*class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // جدولة FetchDataWorker عند تثبيت التطبيق
        val fetchDataRequest = OneTimeWorkRequest.Builder(FetchDataWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(fetchDataRequest)

        // جدولة FetchDataWorker بشكل دوري
        val periodicFetchDataRequest = PeriodicWorkRequest.Builder(FetchDataWorker::class.java, 1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "FetchDataWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicFetchDataRequest
        )
    }
}
*/
