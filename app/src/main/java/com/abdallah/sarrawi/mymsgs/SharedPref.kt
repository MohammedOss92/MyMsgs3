package com.abdallah.sarrawi.mymsgs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharedPref(var con:Context) {
     var isDark: Boolean= false

    fun saveThemeStatePref(isDark: Boolean) {
        val pref = con.getSharedPreferences("myPref", AppCompatActivity.MODE_PRIVATE)

        val editor = pref.edit()
        editor.putBoolean("isDark", isDark)
        editor.commit()
    }

    fun getThemeStatePref(): Boolean {
        val pref =
            con.getSharedPreferences("myPref", AppCompatActivity.MODE_PRIVATE)
        return pref.getBoolean("isDark", false)
    }
}