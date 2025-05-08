package com.abdallah.sarrawi.mymsgs.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdallah.sarrawi.mymsgs.R
import com.abdallah.sarrawi.mymsgs.SharedPref
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsTypesViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.MyViewModelFactory
import com.abdallah.sarrawi.mymsgs.ViewModel.ViewModelFactory
import com.abdallah.sarrawi.mymsgs.adapter.MsgsTypes_Adapter
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.databinding.FragmentFirstBinding
import com.abdallah.sarrawi.mymsgs.db.LocaleSource
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.paging.MsgsTypesAdapterPaging
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.repository.MsgsTypesRepo
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import com.abdallah.sarrawi.mymsgs.vm.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.launch

class FirstFragment : Fragment() {
    private lateinit var _binding : FragmentFirstBinding
    private val binding get() = _binding
    var isDark = true
    lateinit var rootLayout: ConstraintLayout
    var mprogressdaialog: Dialog? = null

    var clickCount = 0
    var mInterstitialAd: InterstitialAd?=null


//    private val msgstypesAdapter by lazy {  MsgsTypes_Adapter(/*isDark*/) }
//    private val retrofitService = ApiService.provideRetrofitInstance()
//    private val mainRepository2 by lazy {  MsgsRepo(retrofitService, LocaleSource(requireContext())) }
//
//    private val mainRepository by lazy {  MsgsTypesRepo(retrofitService, LocaleSource(requireContext())) }
//
//    private val viewModel: MsgsTypesViewModel by viewModels{
//        MyViewModelFactory(mainRepository,mainRepository2,requireActivity() as MainActivity)
//    }
//
//
//    private val viewModel2: MsgsViewModel by viewModels{
//        ViewModelFactory(mainRepository2)
//    }

    private val msgsTypesAdapterPaging by lazy {  MsgsTypesAdapterPaging(requireContext(),this/*isDark*/) }
    private val ID_Type_id=0
    private val retrofitService = ApiService.provideRetrofitInstance()
    private val mainRepository3 by lazy { Repo_Type(retrofitService, LocaleSource(requireContext()),
        PostDatabase.getInstance(requireContext())) }
    private val vm_types: VM_Type by viewModels {
        MyVMFactoryTypes(mainRepository3, requireContext(), PostDatabase.getInstance(requireContext()))
    }

    // أخذ أو إنشاء VM_Msgs
    private val vm_msgs: VM_Msgs by viewModels {
        MyVMFactory(mainRepository3, requireContext(), PostDatabase.getInstance(requireContext()), ID_Type_id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
       Log.e("tessst","entred")
        (activity as MainActivity).fragment = 1


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        setUpRv()
//        adapterOnClick()
        setup()
        adapterOnClick()
        InterstitialAd_fun()
        loadInterstitialAd()
        binding.swipeRefreshLayout.setOnRefreshListener {
            // بدء عملية التحديث
            startRefreshing()
        }
        menu_item()
    }



    private fun adapterOnClick(){
        //لاحظ الفانكشن انها بترمي الid
//        msgstypesAdapter.onItemClick = {id, MsgTypes ->
        msgsTypesAdapterPaging.onItemClick = {id ->
//            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
//            val direction = FirstFragmentDirections.actionFirsFragmentToSecondFragment(id, MsgTypes)
//            InterstitialAd_fun()
            clickCount++
            if (clickCount >= 2) {
// بمجرد أن يصل clickCount إلى 4، اعرض الإعلان
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
                clickCount = 0 // اعيد قيمة المتغير clickCount إلى الصفر بعد عرض الإعلان

            }

            val direction = FirstFragmentDirections.actionFirsFragmentToSecondFragment(id)
            findNavController().navigate(direction)
        }

    }



//    private fun setUpRv() = viewModel.viewModelScope.launch {
//
//
//
//        viewModel.getPostsFromRoomWithCounts(requireContext() as MainActivity).observe(requireActivity()) { listTvShows ->
//       //     Log.e("tessst",listTvShows.size.toString()+"  adapter")
//            msgstypesAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
//            msgstypesAdapter.msgsTypesModel = listTvShows
//            if(binding.rcMsgTypes.adapter == null){
//                msgstypesAdapter.msgsTypesModel = listTvShows
//                binding.rcMsgTypes.layoutManager = LinearLayoutManager(requireContext())
//                binding.rcMsgTypes.adapter = msgstypesAdapter
//            }
//            msgstypesAdapter.notifyDataSetChanged()
//
//        }
//
//    }



    private fun setup() = vm_types.viewModelScope.launch {
        if (isAdded) {
            binding.rcMsgTypes.layoutManager = LinearLayoutManager(requireContext())

            val pagingAdapter = MsgsTypesAdapterPaging(requireContext(), this@FirstFragment)
            binding.rcMsgTypes.adapter = pagingAdapter

            // مراقبة بيانات Paging والتحميل
            lifecycleScope.launch {
                vm_types.msgType.observe(viewLifecycleOwner) { pagingData ->
                    pagingAdapter.submitData(lifecycle, pagingData)
                }
            }

            // تهيئة سياسة استعادة الحالة
            pagingAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }


    private fun setup1() {
        if (isAdded) {
            binding.rcMsgTypes.layoutManager = LinearLayoutManager(requireContext())

            val pagingAdapter = MsgsTypesAdapterPaging(requireContext(), this)
            binding.rcMsgTypes.adapter = pagingAdapter
            lifecycleScope.launch {
//                nokatViewModel.invalidatePagingSourceTypes()
//                nokatViewModel.nokatTypesFlow.collectLatest { pagingData ->
//                    Log.d("NokatTypeFlow", "Received new paging data: $pagingData")
//                    pagingAdapter.submitData(pagingData)
//                }

                vm_types.msgType.observe(viewLifecycleOwner) { pagingData ->
                    pagingAdapter.submitData(lifecycle, pagingData)

                }
                vm_types.invalidatePagingSourceTypes()
            }

            pagingAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }



    private fun menu_item() {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.first_frag_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when(menuItem.itemId){
                    R.id.action_refresh ->{

                        startRefreshing()
                    }

                    R.id.action_theme -> {


                        val prefs = SharedPref(requireContext())
                        val isDark = prefs.getThemeStatePref()
                        prefs.saveThemeStatePref(!isDark)

                        if(isDark){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                        else{
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                    }

                }
                return true
            }

        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }




   fun InterstitialAd_fun (){


       MobileAds.initialize(requireActivity()) { initializationStatus ->
           // do nothing on initialization complete
       }

       val adRequest = AdRequest.Builder().build()
       InterstitialAd.load(
           requireActivity(),
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
    fun loadInterstitialAd() {
        MobileAds.initialize(requireActivity()) { initializationStatus ->
            // do nothing on initialization complete
        }

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireActivity(),
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

    private fun startRefreshing() {
        // بدء عملية التحديث
        binding.swipeRefreshLayout.isRefreshing = true // بدء عملية التحديث



        lifecycleScope.launch {
            try {
//                vm_msgs.deletemsg()
//                vm_msgs.deletemsgtypes()

                // هنا يمكنك استدعاء عملية التحديث الفعلي إذا لزم الأمر
                vm_msgs.refreshMsgsType(
                    ApiService.provideRetrofitInstance(),
                    PostDatabase.getInstance(requireContext()),
                    requireView()
                )
                vm_types.invalidatePagingSourceTypes() // إعادة تحميل البيانات من Room بعد التحديث

            } catch (e: Exception) {
                // التعامل مع الأخطاء
                e.printStackTrace()
            } finally {
                // تأخير إيقاف التحديث لمدة 5 ثوانٍ بعد بدء التحديث
                    binding.swipeRefreshLayout.isRefreshing = false

            }
        }
    }

    fun showInterstitial(){
        clickCount++
        if (clickCount >= 2) {
// بمجرد أن يصل clickCount إلى 4، اعرض الإعلان
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
                loadInterstitialAd()
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
            clickCount = 0 // اعيد قيمة المتغير clickCount إلى الصفر بعد عرض الإعلان

        }
    }

}