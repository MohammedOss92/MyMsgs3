package com.abdallah.sarrawi.mymsgs.broadcastReceiver

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsTypesViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.MyViewModelFactory
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.db.LocaleSource
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.repository.MsgsTypesRepo
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RefreshPostsService : JobIntentService() {

    lateinit var viewModel: MsgsTypesViewModel



    companion object {
        private const val JOB_ID = 1001

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, RefreshPostsService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.d("RefreshPostsService", "Handling work")

        val context = applicationContext as MainActivity
        val serviceScope = CoroutineScope(Dispatchers.Default)
        serviceScope.launch {
            val retrofitService = ApiService.provideRetrofitInstance()
            val mainRepository = MsgsTypesRepo(retrofitService, LocaleSource(context))
            val mainRepository2 = MsgsRepo(retrofitService, LocaleSource(context))
            val viewModel = ViewModelProvider(context).get(MsgsTypesViewModel::class.java)
            viewModel.refreshPosts(context)
        }
        Log.d("RefreshPostsService", "Handling work")
    }


}

/*class RefreshPostsJobService : JobService() {

    private lateinit var viewModel: MsgsTypesViewModel

    override fun onStartJob(params: JobParameters?): Boolean {
        val retrofitService = ApiService.provideRetrofitInstance()
        val mainRepository = MsgsTypesRepo(retrofitService, LocaleSource(this))
        val mainRepository2 = MsgsRepo(retrofitService, LocaleSource(this))
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(
            MsgsTypesViewModel::class.java
        )
        viewModel.viewModelScope.launch {
            viewModel.refreshPosts(applicationContext as MainActivity)
            jobFinished(params, false)
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}
*/

/*class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // تنفيذ الوظيفة المؤجلة
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(1, ComponentName(this, RefreshPostsJobService::class.java))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .build()
        jobScheduler.schedule(jobInfo)
    }
}
*/
