package com.abdallah.sarrawi.mymsgs.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsTypesViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.MyViewModelFactory
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.db.LocaleSource
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.repository.MsgsTypesRepo
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import kotlinx.coroutines.launch

class InstallReceiver : BroadcastReceiver() {

    lateinit var viewModel: MsgsTypesViewModel

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("InstallReceiver", "Received broadcast")
        if (intent.action == Intent.ACTION_PACKAGE_ADDED) {
            val packageName = intent.data?.schemeSpecificPart
            if (packageName == context.packageName) {
                // تنفيذ الوظيفة refreshPosts
                val retrofitService = ApiService.provideRetrofitInstance()
                val mainRepository = MsgsTypesRepo(retrofitService, LocaleSource(context))
                val mainRepository2 = MsgsRepo(retrofitService, LocaleSource(context))
                val viewModel = ViewModelProvider(context as ViewModelStoreOwner).get(MsgsTypesViewModel::class.java)
                viewModel.viewModelScope.launch {
                    viewModel.refreshPostswithout(context)
                }
                Log.d("InstallReceiver", "Received broadcast")
            }
        }
    }


}

