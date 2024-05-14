package com.abdallah.sarrawi.mymsgs

import android.content.Context
import android.content.Intent
import android.util.Log

class Utils {

    companion object {
        fun IntenteShare(con: Context, dialog_Heder_text: String, heder: String, Msg: String) {
            try {

                var sharingIntent = Intent("android.intent.action.SEND")
                sharingIntent.type = "text/plain"
//                sharingIntent.putExtra("android.intent.extra.TEXT", out9.getText().toString())
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, heder)
                sharingIntent.putExtra(Intent.EXTRA_TEXT, Msg)
                con.startActivity(
                    Intent.createChooser(
                        sharingIntent,dialog_Heder_text
                    )
                )



            } catch (e: Exception) {
                Log.d("error in share", e.toString())
            }
        }

        fun shareApp(con: Context){
            val sendIntent = Intent()
            sendIntent.action=Intent.ACTION_SEND
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT," مشاركة التطبيق\n\n https://play.google.com/store/apps/details?id=com.abdallah.sarrawi.mymsgs")
            con.startActivity(
                Intent.createChooser(
                    sendIntent,"choose one"
                )
            )
        }
    }







}

