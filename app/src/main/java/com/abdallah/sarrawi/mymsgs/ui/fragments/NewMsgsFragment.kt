package com.abdallah.sarrawi.mymsgs.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.abdallah.sarrawi.mymsgs.adapter.CallBack
import com.abdallah.sarrawi.mymsgs.adapter.Msgs_Adapter
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.databinding.FragmentNewMsgsBinding
import com.abdallah.sarrawi.mymsgs.databinding.FragmentSecondBinding
import com.abdallah.sarrawi.mymsgs.db.LocaleSource
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.models.MsgModelWithTitle
import com.abdallah.sarrawi.mymsgs.paging.MsgsAdapterPaging
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import com.abdallah.sarrawi.mymsgs.vm.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class NewMsgsFragment : Fragment(), CallBack {
    private lateinit var _binding : FragmentNewMsgsBinding
    private val binding get() = _binding

//    private val msgsAdapter by lazy { Msgs_Adapter(requireContext(),this) }
//    private val retrofitService = ApiService.provideRetrofitInstance()
//
//    private val mainRepository by lazy {  MsgsRepo(retrofitService, LocaleSource(requireContext())) }
//
//    private val viewModel: MsgsViewModel by viewModels{
//        ViewModelFactory(mainRepository)
//    }

    var clickCount = 0

    private val msgsAdapterPaging by lazy {  MsgsAdapterPaging(requireContext(),this/*isDark*/) }
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
        (activity as MainActivity).fragment = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewMsgsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRv()
//        adapterOnClick()
        menu_item()

    }



    fun setUpRv() {
        // إعداد الـ RecyclerView مع LinearLayoutManager
        binding.rcMsgsNew.layoutManager = LinearLayoutManager(requireContext())

        // إنشاء الـ PagingAdapter
        val pagingAdapter = MsgsAdapterPaging(requireContext(), this)
        binding.rcMsgsNew.adapter = pagingAdapter

        // مراقبة بيانات الـ ViewModel وتحديث الـ RecyclerView
        vm_msgs.getAllMsgsNew().observe(viewLifecycleOwner) { pagingData ->
            // تحديث القائمة بعد تغيير حالة العنصر المفضل
            pagingAdapter.submitData(lifecycle, pagingData)
        }

        pagingAdapter.onItemClick = { id, item, position ->
            clickCount++
            if (clickCount >= 2) {
// بمجرد أن يصل clickCount إلى 4، اعرض الإعلان
//                    if (mInterstitialAd != null) {
//                        mInterstitialAd?.show(requireActivity())
//                        loadInterstitialAd()
//                    } else {
//                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
//                    }
                clickCount = 0 // اعيد قيمة المتغير clickCount إلى الصفر بعد عرض الإعلان

            }

            val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            val fav = FavoriteModel(item.msgModel!!.id,item.msgModel!!.MessageName,item.msgModel!!.MessageName,item.msgModel!!.ID_Type_id,item.msgModel!!.ID_Type_id).apply {
                createdAt = currentTime
            }

            if (item.msgModel!!.is_fav) {
                vm_msgs.update_favs(item.msgModel!!.id, false)
                vm_msgs.delete_favs(fav)
                lifecycleScope.launch {
                    val snackbar = Snackbar.make(requireView(), "تم الحذف من المفضلة", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                    pagingAdapter.notifyItemChanged(position) // Update UI after operation
                }
            } else {
                vm_msgs.update_favs(item.msgModel!!.id, true)
                vm_msgs.add_favs(fav)
                lifecycleScope.launch {
                    val snackbar = Snackbar.make(requireView(), "تم الاضافة الى المفضلة", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                    pagingAdapter.notifyItemChanged(position) // Update UI after operation
                }
            }
        }
    }

    fun adapterOnClick(){
//        msgsAdapterPaging.onItemClick = { id,item, i: Int ->
//            val fav = FavoriteModel(
//                item.msgModel!!.id,
//                item.msgModel!!.MessageName,
//                item.typeTitle,
//                item.msgModel!!.new_msgs,
//                item.msgModel!!.ID_Type_id
//            )
//            // check if item is favorite or not
//            if (item.msgModel!!.is_fav) {
//                vm_msgs.update_fav(item.msgModel!!.id, false) // update favorite item state
//                vm_msgs.delete_favs(fav) //delete item from db
//                Toast.makeText(requireContext(), "تم الحذف من المفضلة", Toast.LENGTH_SHORT).show()
//            } else {
//                vm_msgs.update_fav(item.msgModel!!.id, true)
//                vm_msgs.add_favs(fav) // add item to db
//                Toast.makeText(requireContext(), "تم الاضافة الى المفضلة", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
        val pagingAdapter = MsgsAdapterPaging(requireContext(),this)
        pagingAdapter.onItemClick = { id, item, position ->
            clickCount++
            if (clickCount >= 2) {
// بمجرد أن يصل clickCount إلى 4، اعرض الإعلان
//                    if (mInterstitialAd != null) {
//                        mInterstitialAd?.show(requireActivity())
//                        loadInterstitialAd()
//                    } else {
//                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
//                    }
                clickCount = 0 // اعيد قيمة المتغير clickCount إلى الصفر بعد عرض الإعلان

            }

            val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            val fav = FavoriteModel(item.msgModel!!.id,item.msgModel!!.MessageName,item.msgModel!!.MessageName,item.msgModel!!.ID_Type_id,item.msgModel!!.ID_Type_id).apply {
                createdAt = currentTime
            }

            if (item.msgModel!!.is_fav) {
                vm_msgs.update_favs(item.msgModel!!.id, false)
                vm_msgs.delete_favs(fav)
                lifecycleScope.launch {
                    val snackbar = Snackbar.make(requireView(), "تم الحذف من المفضلة", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                    pagingAdapter.notifyItemChanged(position) // Update UI after operation
                }
            } else {
                vm_msgs.update_favs(item.msgModel!!.id, true)
                vm_msgs.add_favs(fav)
                lifecycleScope.launch {
                    val snackbar = Snackbar.make(requireView(), "تم الاضافة الى المفضلة", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                    pagingAdapter.notifyItemChanged(position) // Update UI after operation
                }
            }
        }

    }

//    private fun adapterOnClick() {
//        msgsAdapter.onItemClick = { it: MsgModelWithTitle, i: Int ->
//            val fav = FavoriteModel(it.msgModel!!.id, it.msgModel!!.MessageName, it.typeTitle, it.msgModel!!.new_msgs, it.msgModel!!.ID_Type_id)
//            // check if item is favorite or not
//            if (it.msgModel!!.is_fav) {
//                viewModel.update_fav(it.msgModel!!.id, false) // update favorite item state
//                viewModel.delete_fav(fav) //delete item from db
//                Toast.makeText(requireContext(), "تم الحذف من المفضلة", Toast.LENGTH_SHORT).show()
//            } else {
//                viewModel.update_fav(it.msgModel!!.id, true)
//                viewModel.add_fav(fav) // add item to db
//                Toast.makeText(requireContext(), "تم الاضافة الى المفضلة", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    private fun setUpRv() {
//        viewModel.getAllNewMsg().observe(viewLifecycleOwner) { listTvShows ->
//            // تحديث القائمة بعد تغيير حالة العنصر المفضل
//            msgsAdapter.msgsModel = listTvShows
//            msgsAdapter.notifyDataSetChanged()
//        }
//
//        binding.rcMsgsNew.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = msgsAdapter
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
                        val dir = NewMsgsFragmentDirections.actionNewMsgsFragmentToEditFragment("")
                        NavHostFragment.findNavController(this@NewMsgsFragment).navigate(dir)
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
}