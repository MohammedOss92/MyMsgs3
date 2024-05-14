package com.abdallah.sarrawi.mymsgs.ui.fragments

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
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.models.MsgModelWithTitle
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class SecondFragment : Fragment() , CallBack {

    private lateinit var _binding : FragmentSecondBinding
    private val binding get() = _binding
    private var argsId = -1
    private var MsgTypes_name = ""

//    lateinit var  msgsAdapter :Msgs_Adapter
    private val msgsAdapter by lazy { Msgs_Adapter(requireContext(),this) }
    private val retrofitService = ApiService.provideRetrofitInstance()

    private val mainRepository by lazy {  MsgsRepo(retrofitService, LocaleSource(requireContext())) }

    private val viewModel: MsgsViewModel by viewModels{
        ViewModelFactory(mainRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argsId = SecondFragmentArgs.fromBundle(requireArguments()).id
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



        setUpRv()
        adapterOnClick()
        menu_item()

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun adapterOnClick(){
//        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        msgsAdapter.onItemClick = { it: MsgModelWithTitle, i: Int ->
            val fav= FavoriteModel(it.msgModel!!.id,it.msgModel!!.MessageName,it.typeTitle,it.msgModel!!.new_msgs,it.msgModel!!.ID_Type_id)
//            fav.createdAt=currentTime
            // check if item is favorite or not
            if (it.msgModel!!.is_fav){
                viewModel.update_fav(it.msgModel!!.id,false) // update favorite item state
                viewModel.delete_fav(fav) //delete item from db
                Toast.makeText(requireContext(),"تم الحذف من المفضلة",Toast.LENGTH_SHORT).show()
                setUpRv()
                msgsAdapter.notifyDataSetChanged()

            }else{
                viewModel.update_fav(it.msgModel!!.id,true)
                viewModel.add_fav(fav) // add item to db
                Toast.makeText(requireContext(),"تم الاضافة الى المفضلة",Toast.LENGTH_SHORT).show()
                setUpRv()
                msgsAdapter.notifyDataSetChanged()
            }

        }

//        msgsAdapter.onClick={popupMenus(requireView())}

    }
    private  fun setUpRv() = viewModel.viewModelScope.launch {

//        binding.rcMsgTypes.apply {
//            adapter = msgstypesAdapter
//            setHasFixedSize(true)
//        }


        viewModel.getMsgsFromRoom_by_id(argsId,requireContext()).observe(viewLifecycleOwner) { listShows ->
            //  msgsAdapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            msgsAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
//            msgsAdapter.msgsModel = listShows
//            binding.rcMsgs.adapter = msgsAdapter
            msgsAdapter.msgsModel = listShows
            if(binding.rcMsgs.adapter == null){
                binding.rcMsgs.layoutManager = LinearLayoutManager(requireContext())
                binding.rcMsgs.adapter = msgsAdapter
                msgsAdapter.notifyDataSetChanged()
            }else{
                msgsAdapter.notifyDataSetChanged()
            }
            Log.e("tessst","enter111")

        }
    }



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