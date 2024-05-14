package com.abdallah.sarrawi.mymsgs.ui.fragments

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.repository.MsgsTypesRepo
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
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


    private val msgstypesAdapter by lazy {  MsgsTypes_Adapter(/*isDark*/) }
    private val retrofitService = ApiService.provideRetrofitInstance()
    private val mainRepository2 by lazy {  MsgsRepo(retrofitService, LocaleSource(requireContext())) }

    private val mainRepository by lazy {  MsgsTypesRepo(retrofitService, LocaleSource(requireContext())) }

    private val viewModel: MsgsTypesViewModel by viewModels{
        MyViewModelFactory(mainRepository,mainRepository2,requireActivity() as MainActivity)
    }


    private val viewModel2: MsgsViewModel by viewModels{
        ViewModelFactory(mainRepository2)
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

        InterstitialAd_fun()
        setUpRv()
        adapterOnClick()
        menu_item()
    }



    private fun adapterOnClick(){
        //لاحظ الفانكشن انها بترمي الid
//        msgstypesAdapter.onItemClick = {id, MsgTypes ->
        msgstypesAdapter.onItemClick = {id ->
//            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
//            val direction = FirstFragmentDirections.actionFirsFragmentToSecondFragment(id, MsgTypes)
//            InterstitialAd_fun()
            clickCount++
            if (clickCount >= 4) {
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



    private fun setUpRv() = viewModel.viewModelScope.launch {



        viewModel.getPostsFromRoomWithCounts(requireContext() as MainActivity).observe(requireActivity()) { listTvShows ->
       //     Log.e("tessst",listTvShows.size.toString()+"  adapter")
            msgstypesAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            msgstypesAdapter.msgsTypesModel = listTvShows
            if(binding.rcMsgTypes.adapter == null){
                msgstypesAdapter.msgsTypesModel = listTvShows
                binding.rcMsgTypes.layoutManager = LinearLayoutManager(requireContext())
                binding.rcMsgTypes.adapter = msgstypesAdapter
            }
            msgstypesAdapter.notifyDataSetChanged()

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
                    R.id.action_refresh -> {
                        viewModel.refreshPosts(requireActivity() as MainActivity)
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

    fun showprogressdialog() {

        binding.progressBar.visibility = View.VISIBLE
        binding.textView.visibility = View.VISIBLE
        //  mprogressdaialog = Dialog(this)
        //  mprogressdaialog!!.setCancelable(false)
        //  mprogressdaialog!!.setContentView(R.layout.progress_dialog)

        //  mprogressdaialog!!.show()
    }

    fun hideprogressdialog() {
        Log.e("tesssst","entred")
        //  recreate()
        // mprogressdaialog!!.dismiss()
        binding.progressBar.visibility = View.GONE
        binding.textView.visibility = View.GONE
//        recreate()

    }

    override fun onDestroy() {
        if (mprogressdaialog != null && mprogressdaialog!!.isShowing) mprogressdaialog!!.dismiss()
        super.onDestroy()
    }

    override fun onStop() {
        //  if (mprogressdaialog != null && mprogressdaialog!!.isShowing) mprogressdaialog!!.dismiss()
        super.onStop()
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


}