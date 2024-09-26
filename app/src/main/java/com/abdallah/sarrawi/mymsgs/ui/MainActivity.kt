package com.abdallah.sarrawi.mymsgs.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.abdallah.sarrawi.mymsgs.R
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsTypesViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.MyViewModelFactory
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.databinding.ActivityMainBinding
import com.abdallah.sarrawi.mymsgs.db.LocaleSource
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.repository.MsgsTypesRepo
import com.abdallah.sarrawi.mymsgs.vm.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var binding: ActivityMainBinding
//    lateinit var viewModel: MsgsTypesViewModel
//    lateinit var viewModel2: MsgsViewModel
    var mInterstitialAd: InterstitialAd? = null
    private lateinit var navController: NavController
    var mprogressdaialog: Dialog? = null
    var fragment = 1

    private val ID_Type_id=0
    private val retrofitService = ApiService.provideRetrofitInstance()
    private val mainRepository3 by lazy { Repo_Type(retrofitService, LocaleSource(this),
        PostDatabase.getInstance(this)) }
    private val vm_types: VM_Type by viewModels {
        MyVMFactoryTypes(mainRepository3, this, PostDatabase.getInstance(this))
    }

    // أخذ أو إنشاء VM_Msgs
    private val vm_msgs: VM_Msgs by viewModels {
        MyVMFactory(mainRepository3, this, PostDatabase.getInstance(this), ID_Type_id)
    }
//س
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        bottomNav = findViewById(R.id.bottomNav)

        navController =
            findNavController(R.id.nav_host_fragment_activity_main)
//s
        appBarConfiguration = AppBarConfiguration(setOf(R.id.firsFragment,R.id.favoriteFragment,R.id.newMsgsFragment,R.id.settingsFrag))
//        setupActionBarWithNavController(navController,appBarConfiguration)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.secondFragment || destination.id == R.id.splashFragment || destination.id == R.id.editFragment ) {

                bottomNav.visibility = View.GONE
            } else {

                bottomNav.visibility = View.VISIBLE
            }
        }

//        val retrofitService = ApiService.provideRetrofitInstance()
//        val mainRepository = MsgsTypesRepo(retrofitService, LocaleSource(this))
//        val mainRepository2 = MsgsRepo(retrofitService, LocaleSource(this))
//        //  supportActionBar?.hide()
//
//        viewModel =
//            ViewModelProvider(this, MyViewModelFactory(mainRepository, mainRepository2, this)).get(
//                MsgsTypesViewModel::class.java
//            )
//        viewModel2 =
//            ViewModelProvider(this, MyViewModelFactory(mainRepository, mainRepository2, this)).get(
//                MsgsViewModel::class.java
//            )


    FirebaseMessaging.getInstance().subscribeToTopic("alert")
    FirebaseMessaging.getInstance().token
        .addOnSuccessListener { token ->
            // قم بتنفيذ العمليات المطلوبة هنا على الـ token

        }
        .addOnFailureListener { exception ->
            // قم بتنفيذ العمليات المطلوبة هنا في حالة فشل العملية
        }

    loadInterstitialAd()





        // val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        //  val navController = navHostFragment.navController
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                val handler = Handler(Looper.getMainLooper())

                lifecycleScope.launch {
                    try {
                        // استخدم this@MainActivity للحصول على السياق
                        vm_msgs.refreshMsgsType(
                            ApiService.provideRetrofitInstance(),
                            PostDatabase.getInstance(this@MainActivity), // استخدام this@MainActivity للحصول على السياق
                            findViewById(android.R.id.content) // استخدم findViewById إذا كنت تحتاج إلى View
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                viewModel.refreshPosts(this)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun loadInterstitialAd() {
        MobileAds.initialize(this) { initializationStatus ->
            // do nothing on initialization complete
        }

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-1895204889916566/9391166409",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until an ad is loaded.
                    mInterstitialAd = interstitialAd
                    Log.i("onAdLoadedL", "onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.d("onAdLoadedF", loadAdError.toString())
                    mInterstitialAd = null
                }
            }
        )
    }

//    fun showprogressdialog() {
//
//        binding.progressBar.visibility = View.VISIBLE
//        binding.tvLoad?.visibility  = View.VISIBLE
//
//
//        //  mprogressdaialog = Dialog(this)
//        //  mprogressdaialog!!.setCancelable(false)
//        //  mprogressdaialog!!.setContentView(R.layout.progress_dialog)
//
//        //  mprogressdaialog!!.show()
//    }
//
//    fun hideprogressdialog() {
//        Log.e("tesssst","entred")
//        //  recreate()
//        // mprogressdaialog!!.dismiss()
////        binding.progressBar.visibility = View.GONE
////        binding.tvLoad?.visibility = View.GONE
//
//        if (mInterstitialAd != null) {
//            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
//                override fun onAdDismissedFullScreenContent() {
//                    Log.d(TAG, "Ad was dismissed.")
//                    // Load the next interstitial ad.
//                    loadInterstitialAd()
//                }
//
//                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                    Log.d(TAG, "Ad failed to show.")
//                    // Load the next interstitial ad.
//                    loadInterstitialAd()
//                }
//
//                override fun onAdShowedFullScreenContent() {
//                    Log.d(TAG, "Ad showed fullscreen content.")
//                    mInterstitialAd = null
//                }
//            }
//            mInterstitialAd?.show(this)
//        } else {
//            Log.d(TAG, "Ad wasn't loaded.")
//            // Load the next interstitial ad.
//            loadInterstitialAd()
//        }
//
//        mInterstitialAd?.show(this)
//
//
//        recreate()
//
//    }
//
//
//    override fun onDestroy() {
//        if (mprogressdaialog != null && mprogressdaialog!!.isShowing) mprogressdaialog!!.dismiss()
//        super.onDestroy()
//    }
//
//    override fun onStop() {
//        //  if (mprogressdaialog != null && mprogressdaialog!!.isShowing) mprogressdaialog!!.dismiss()
//        super.onStop()
//    }
//
//    override fun onDetachedFromWindow() {
//        //  if (mprogressdaialog != null && mprogressdaialog!!.isShowing) mprogressdaialog!!.dismiss()
//
//        super.onDetachedFromWindow()
//    }



}