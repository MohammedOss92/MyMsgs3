package com.abdallah.sarrawi.mymsgs.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdallah.sarrawi.mymsgs.R
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsTypesViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.MyViewModelFactory
import com.abdallah.sarrawi.mymsgs.ViewModel.ViewModelFactory
import com.abdallah.sarrawi.mymsgs.adapter.Msgs_Adapter
import com.abdallah.sarrawi.mymsgs.adapter.Msgs_Fav_Adapter
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.databinding.FragmentFavoriteBinding
import com.abdallah.sarrawi.mymsgs.db.LocaleSource
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.paging.MsgsAdapterFavPaging
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.repository.MsgsTypesRepo
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import com.abdallah.sarrawi.mymsgs.vm.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var _binding: FragmentFavoriteBinding
    private val binding get() = _binding!!
    var clickCount = 0
    var mInterstitialAd: InterstitialAd?=null
//    lateinit var msgfavadapter :Msgs_Fav_Adapter
//
//    private val retrofitService = ApiService.provideRetrofitInstance()
//
//    private val mainRepository by lazy { MsgsRepo(retrofitService, LocaleSource(requireContext())) }
//
//    private val viewModel: MsgsViewModel by viewModels {
//        ViewModelFactory(mainRepository)
//    }
//
    lateinit var msgsAdapterFavPaging: MsgsAdapterFavPaging
    private val retrofitService = ApiService.provideRetrofitInstance()
    private val mainRepository3 by lazy { Repo_Type(retrofitService, LocaleSource(requireContext()),
        PostDatabase.getInstance(requireContext())) }
    private val vm_types: VM_Type by viewModels {
        MyVMFactoryTypes(mainRepository3, requireContext(), PostDatabase.getInstance(requireContext()))
    }
    private var ID_Type_id=0
    // أخذ أو إنشاء VM_Msgs
    private val vm_msgs: VM_Msgs by viewModels {
        MyVMFactory(mainRepository3, requireContext(), PostDatabase.getInstance(requireContext()), ID_Type_id)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        (activity as MainActivity).fragment = 1
//        msgfavadapter = Msgs_Fav_Adapter(requireContext(),this /*,this*/ )
        msgsAdapterFavPaging = MsgsAdapterFavPaging(requireContext(),this /*,this*/ )

        menu_item()
        setUpRv()
        adapterOnClick()
        InterstitialAd_fun()
        loadInterstitialAd()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun adapterOnClick() {

//        msgfavadapter.onItemClick = {
//            viewModel.viewModelScope.launch {
//                viewModel.update_fav(it.id,false) // update item state
//                val result = mainRepository.deleteFav(it)   // delete favorite item from db
//                Toast.makeText(requireContext(),"تم الحذف من المفضلة",Toast.LENGTH_SHORT).show()
//                setUpRv()
//            }
//
//        }
        msgsAdapterFavPaging.onItemClick = {
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
            vm_msgs.viewModelScope.launch {
                vm_msgs.update_fav(it.id,false) // update item state
                val result = mainRepository3.deleteFav(it)   // delete favorite item from db
                val snackbar = Snackbar.make(requireView(), "تم الحذف من المفضلة", Snackbar.LENGTH_SHORT)
                snackbar.show()
                setUpRv()
            }

        }
    }

//    @SuppressLint("SuspiciousIndentation")
//    private fun setUpRv() = viewModel.viewModelScope.launch {
//
//        viewModel.getFav()
//            .observe(viewLifecycleOwner) { listTvShows ->
//                //     Log.e("tessst",listTvShows.size.toString()+"  adapter")
//                // msgfavadapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
////                msgfavadapter.msgs_fav_list = listTvShows
////                binding.rcMsgFav.adapter = msgfavadapter
//                msgfavadapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
////            msgsAdapter.msgsModel = listShows
////            binding.rcMsgs.adapter = msgsAdapter
//                msgfavadapter.notifyDataSetChanged()
//                if(binding.rcMsgFav.adapter == null){
//                    msgfavadapter.msgs_fav_list = listTvShows
//                    binding.rcMsgFav.layoutManager = LinearLayoutManager(requireContext())
//                    binding.rcMsgFav.adapter = msgfavadapter
//                    msgfavadapter.notifyDataSetChanged()
//                }
//            }
//
//    }


    @SuppressLint("SuspiciousIndentation")
    private fun setUpRv() = vm_msgs.viewModelScope.launch {
        // مراقبة البيانات القادمة من الـ ViewModel
        vm_msgs.favMsgs.observe(viewLifecycleOwner) { pagingData ->

//            // تحقق إذا كانت القائمة الحالية فارغة، إذا كانت كذلك، أضف عنصرًا جديدًا
//            val updatedListShows = if (listShows.isEmpty()) {
//                // إنشاء قائمة جديدة تحتوي على عنصر معين في حالة أن القائمة فارغة
//                listShows + FavoriteModel(0, "مرحبا", "مسجات اسلامية", 0, 1)
//            } else {
//                // إذا كانت القائمة غير فارغة، قم بتصفية العناصر التي لا تحتوي على "مرحبا"
//                listShows.filter { it.MessageName != "مرحبا" }
//            }
            val updatedPagingData = pagingData.map { favoriteModel ->
                // تحقق إذا كانت البيانات تحتوي على "مرحبا" واستبدلها أو قم بتعديل البيانات
                if (favoriteModel.MessageName != "مرحبا") {
                    favoriteModel
                } else {
                    // استبدال العنصر عند وجود "مرحبا"
                    FavoriteModel(0, "مرحبا", "مسجات اسلامية", 0, 1)
                }
            }

            // تعيين سياسة استعادة حالة RecyclerView
            msgsAdapterFavPaging.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW

            // تمرير البيانات إلى محول RecyclerView
            msgsAdapterFavPaging.submitData(lifecycle, pagingData)

            // إذا كان المحول غير معين مسبقًا، قم بتعيينه وإعداد التخطيط
            if (binding.rcMsgFav.adapter == null) {
                binding.rcMsgFav.layoutManager = LinearLayoutManager(requireContext())
                binding.rcMsgFav.adapter = msgsAdapterFavPaging
            } else {
                // إذا كان المحول معينًا بالفعل، قم بتحديث البيانات
                msgsAdapterFavPaging.notifyDataSetChanged()
            }

            // تسجيل الدخول لتتبع العملية
            Log.e("tessst", "enter111")
        }
    }


    private fun menu_item() {
        // The usage of an interface lets you inject your own implementation

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.favorite_frag_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when(menuItem.itemId){

                    R.id.action_zakrafah_fav ->{
                        val dir = FavoriteFragmentDirections.actionFavoriteFragmentToEditFragment2("")
                        NavHostFragment.findNavController(this@FavoriteFragment).navigate(dir)
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
}