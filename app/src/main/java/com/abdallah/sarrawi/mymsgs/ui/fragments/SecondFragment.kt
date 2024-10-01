package com.abdallah.sarrawi.mymsgs.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdallah.sarrawi.mymsgs.R
import com.abdallah.sarrawi.mymsgs.ViewModel.MsgsViewModel
import com.abdallah.sarrawi.mymsgs.ViewModel.ViewModelFactory
import com.abdallah.sarrawi.mymsgs.adapter.Msgs_Adapter
import com.abdallah.sarrawi.mymsgs.adapter.CallBack
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.databinding.FragmentSecondBinding
import com.abdallah.sarrawi.mymsgs.db.LocaleSource
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.models.MsgModelWithTitle
import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import com.abdallah.sarrawi.mymsgs.paging.MsgsAdapterPaging
import com.abdallah.sarrawi.mymsgs.paging.MsgsTypesAdapterPaging
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import com.abdallah.sarrawi.mymsgs.vm.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class SecondFragment : Fragment() , CallBack {

    private lateinit var _binding : FragmentSecondBinding
    private val binding get() = _binding
    private var argsId = -1
    private var MsgTypes_name = ""
    var clickCount = 0
    var mInterstitialAd: InterstitialAd?=null
//    lateinit var  msgsAdapter :Msgs_Adapter
//    private val msgsAdapter by lazy { Msgs_Adapter(requireContext(),this) }
//    private val retrofitService = ApiService.provideRetrofitInstance()
//
//    private val mainRepository by lazy {  MsgsRepo(retrofitService, LocaleSource(requireContext())) }
//
//    private val viewModel: MsgsViewModel by viewModels{
//        ViewModelFactory(mainRepository)
//    }

    var onBookmarkClick: ((MsgsModel) -> Unit)? = null
    lateinit var msgsModel: MsgsModel
    private val msgsAdapterPaging by lazy {  MsgsAdapterPaging(requireContext(),this, showElements = true/*isDark*/) }
    private var ID_Type_id=0
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argsId = SecondFragmentArgs.fromBundle(requireArguments()).id
        Log.d("SecondFragment", "Received ID: $argsId")
//        MsgTypes_name = SecondFragmentArgs.fromBundle(requireArguments()).msgType
        (activity as MainActivity).fragment = 2
//        msgsAdapter = Msgs_Adapter(requireContext(),this /*,this*/ )
        // (activity as MainActivity).id = argsId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Toast.makeText(requireContext(), argsId.toString(), Toast.LENGTH_LONG).show()
        //Toast.makeText(requireContext(), MsgTypes_name, Toast.LENGTH_LONG).show()



        setup()
        InterstitialAd_fun()
        loadInterstitialAd()

        menu_item()

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setup() {
        if (isAdded) {
            binding.rcMsgs.layoutManager = LinearLayoutManager(requireContext())

//            val pagingAdapter = MsgsAdapterPaging(requireContext(),this)
            binding.rcMsgs.adapter = msgsAdapterPaging
//            lifecycleScope.launch {
//                nokatViewModel.invalidatePagingSource()
//                nokatViewModel.nokatFlow.collectLatest { pagingData ->
//                    pagingAdapter.submitData(pagingData)
//                }}

            lifecycleScope.launch {

//                vm_msgs.itemsss.collectLatest { pagingData ->
//                    pagingAdapter.submitData(lifecycle, pagingData)
                vm_msgs.itemsswhereID(argsId).observe(viewLifecycleOwner) { pagingData ->
                    Log.d("PagingData", "Received paging data: ${pagingData}")
                    msgsAdapterPaging.submitData(lifecycle, pagingData)
                }
                vm_msgs.invalidatePagingSourceTypes()
            }

            msgsAdapterPaging.onItemClick = { id, item, position ->
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

                val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                val fav = FavoriteModel(item.msgModel!!.id,item.msgModel!!.MessageName,item.typeTitle,item.msgModel!!.ID_Type_id,item.msgModel!!.ID_Type_id).apply {
                    createdAt = currentTime
                }

                if (item.msgModel!!.is_fav) {
                    vm_msgs.update_favs(item.msgModel!!.id, false)
                    vm_msgs.delete_favs(fav)
                    lifecycleScope.launch {
                        val snackbar = Snackbar.make(requireView(), "تم الحذف من المفضلة", Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                } else {
                    vm_msgs.update_favs(item.msgModel!!.id, true)
                    vm_msgs.add_favs(fav)
                    lifecycleScope.launch {
                        val snackbar = Snackbar.make(requireView(), "تم الاضافة الى المفضلة", Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                }
            }
            msgsAdapterPaging.onItemClick2 = { bookmarkState, item, position ->
                val newBookmarkState = if (item.msgModel!!.isBookmark == 0) 1 else 0
                item.msgModel!!.isBookmark = newBookmarkState // تحديث الحالة محلياً
                vm_msgs.setBookmarkForItem(item.msgModel!!) // تمرير العنصر إلى ViewModel لتحديثه في قاعدة البيانات
            }



            msgsAdapterPaging.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }


//    private fun adapterOnClick(){
////        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
//        msgsAdapter.onItemClick = { it: MsgModelWithTitle, i: Int ->
//            val fav= FavoriteModel(it.msgModel!!.id,it.msgModel!!.MessageName,it.typeTitle,it.msgModel!!.new_msgs,it.msgModel!!.ID_Type_id)
////            fav.createdAt=currentTime
//            // check if item is favorite or not
//            if (it.msgModel!!.is_fav){
//                viewModel.update_fav(it.msgModel!!.id,false) // update favorite item state
//                viewModel.delete_fav(fav) //delete item from db
//                Toast.makeText(requireContext(),"تم الحذف من المفضلة",Toast.LENGTH_SHORT).show()
//                setUpRv()
//                msgsAdapter.notifyDataSetChanged()
//
//            }else{
//                viewModel.update_fav(it.msgModel!!.id,true)
//                viewModel.add_fav(fav) // add item to db
//                Toast.makeText(requireContext(),"تم الاضافة الى المفضلة",Toast.LENGTH_SHORT).show()
//                setUpRv()
//                msgsAdapter.notifyDataSetChanged()
//            }
//
//        }
//
////        msgsAdapter.onClick={popupMenus(requireView())}
//
//    }
//    private  fun setUpRv() = viewModel.viewModelScope.launch {
//
////        binding.rcMsgTypes.apply {
////            adapter = msgstypesAdapter
////            setHasFixedSize(true)
////        }
//
//
//        viewModel.getMsgsFromRoom_by_id(argsId,requireContext()).observe(viewLifecycleOwner) { listShows ->
//            //  msgsAdapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.ALLOW
//            msgsAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
////            msgsAdapter.msgsModel = listShows
////            binding.rcMsgs.adapter = msgsAdapter
//            msgsAdapter.msgsModel = listShows
//            if(binding.rcMsgs.adapter == null){
//                binding.rcMsgs.layoutManager = LinearLayoutManager(requireContext())
//                binding.rcMsgs.adapter = msgsAdapter
//                msgsAdapter.notifyDataSetChanged()
//            }else{
//                msgsAdapter.notifyDataSetChanged()
//            }
//            Log.e("tessst","enter111")
//
//        }
//    }



//    private  fun setUpRv() = viewModel.viewModelScope.launch {
//
////        binding.rcMsgTypes.apply {
////            adapter = msgstypesAdapter
////            setHasFixedSize(true)
////        }
//
//
//        viewModel.getMsgsFromRoom_by_id(argsId,requireContext()).observe(viewLifecycleOwner) { listShows ->
//            //  msgsAdapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.ALLOW
//            msgsAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
////            msgsAdapter.msgsModel = listShows
////            binding.rcMsgs.adapter = msgsAdapter
//            msgsAdapter.notifyDataSetChanged()
//            if(binding.rcMsgs.adapter == null){
//                msgsAdapter.msgsModel = listShows
//                binding.rcMsgs.layoutManager = LinearLayoutManager(requireContext())
//                binding.rcMsgs.adapter = msgsAdapter
//                msgsAdapter.notifyDataSetChanged()
//            }
//            Log.e("tessst","enter111")
//
//        }
//    }

    private fun menu_item() {
        // The usage of an interface lets you inject your own implementation

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.second_frag_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when(menuItem.itemId){

                    R.id.action_zakrafah ->{
                        val dir = SecondFragmentDirections.actionSecondFragmentToEditFragment("")
                        NavHostFragment.findNavController(this@SecondFragment).navigate(dir)
                    }

                    R.id.action_bookmark -> {


                        lifecycleScope.launch {

                            vm_msgs.itemsswhereID(argsId).observe(viewLifecycleOwner) { pagingData ->
                                Log.d("PagingData", "Received paging data: ${pagingData}")
                                msgsAdapterPaging.submitData(lifecycle, pagingData)
                                scrollToBookmark()
                                showInterstitial()
                            }

//                            vm_msgs.itemsss.collectLatest { pagingData ->
//                                msgsAdapterPaging.submitData(pagingData) // تأكد من تحميل البيانات أولاً
//                                scrollToBookmark() // التمرير بعد تحميل البيانات
//                            }
                        }
                    }



                }
                return true
            }

        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun OnClickListener() {
        val popupMenu = PopupMenu(requireContext(),view)
        popupMenu.inflate(R.menu.menu_msg)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){



                R.id.edit ->{
//                    Toast.makeText(requireContext(), "edit", Toast.LENGTH_SHORT).show()
//                    val direction = SecondFragmentDirections.actionSecondFragmentToEditFragment()
//
//                    NavHostFragment.findNavController(this).navigate(direction)
                    true
                }

                else -> true
            }
        }
        popupMenu.show()
    }



    fun scrollToBookamark() {
        val snapshot = msgsAdapterPaging.snapshot() // الحصول على البيانات الحالية
        for (i in snapshot.indices) {
            val item = snapshot[i]

            // التحقق أن العنصر غير فارغ وأن isBookmark يساوي 1
            if (item?.msgModel?.isBookmark == 1) {
                // التمرير إلى الموضع المحدد
                binding.rcMsgs.scrollToPosition(i)
                break // نوقف البحث بمجرد العثور على العنصر
            }
        }
    }
    fun scrollToBookmark() {
        val layoutManager = binding.rcMsgs.layoutManager as LinearLayoutManager
        val snapshot = msgsAdapterPaging.snapshot() // الحصول على البيانات الحالية
        for (i in snapshot.indices) {
            val item = snapshot[i]

            // التحقق أن العنصر غير فارغ وأن isBookmark يساوي 1
            if (item?.msgModel?.isBookmark == 1) {
                // استخدام scrollToPositionWithOffset للتمرير إلى الموضع بعد تحميل العناصر
                layoutManager.scrollToPositionWithOffset(i, 0)
                break // نوقف البحث بمجرد العثور على العنصر
            }
        }
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