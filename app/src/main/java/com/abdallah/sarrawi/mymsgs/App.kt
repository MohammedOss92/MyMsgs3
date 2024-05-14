package com.abdallah.sarrawi.mymsgs

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.abdallah.sarrawi.mymsgs.broadcastReceiver.RefreshPostsService
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