package com.abdallah.sarrawi.mymsgs

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.abdallah.sarrawi.mymsgs.workmanager.FetchDataWorker

import com.jakewharton.threetenabp.AndroidThreeTen

class App : Application() {

     companion object {
         var ctx: Context? = null
     }
    var IsDark :Boolean = true
     override fun onCreate() {
         super.onCreate()
         ctx = applicationContext
         AndroidThreeTen.init(this)

         var sharedPref: SharedPref = SharedPref(this)
         IsDark=sharedPref.getThemeStatePref()


         if(IsDark){
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
         }
         else{
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
         }
         scheduleDataFetching()
//         //يمكنك إنشاؤه في مستوى التطبيق لتشارك البيانات بين الأنشطة:
//        val sharedViewModel: SharedViewModel by lazy {
//            ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(SharedViewModel::class.java)
//        }

     }

    private fun scheduleDataFetching() {
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val hasFetchedData = sharedPref.getBoolean("hasFetchedData", false)

        if (!hasFetchedData) {
            val workRequest = OneTimeWorkRequestBuilder<FetchDataWorker>().build()

            WorkManager.getInstance(this).enqueue(workRequest)

            // تحديث SharedPreferences بعد جلب البيانات
            sharedPref.edit().putBoolean("hasFetchedData", true).apply()
        }
    }




//    private fun registerRefreshPostsService() {
//        val serviceIntent = Intent(this, RefreshPostsService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(serviceIntent)
//        } else {
//            startService(serviceIntent)
//        }
//    }


}