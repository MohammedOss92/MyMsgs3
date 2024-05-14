package com.abdallah.sarrawi.mymsgs.adapter

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.text.ClipboardManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdallah.sarrawi.mymsgs.R
import com.abdallah.sarrawi.mymsgs.Utils
import com.abdallah.sarrawi.mymsgs.databinding.MsgsDesignBinding
import com.abdallah.sarrawi.mymsgs.databinding.MsgsFavDeBinding
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.models.MsgModelWithTitle
import com.abdallah.sarrawi.mymsgs.ui.fragments.FavoriteFragmentDirections
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class Msgs_Fav_Adapter(val con:Context,var frag:Fragment) : RecyclerView.Adapter<Msgs_Fav_Adapter.MyViewHolder>() {
    var onItemClick: ((fav:FavoriteModel) -> Unit)? = null // pass favorite item on click
    private var adCount = 4
    inner class MyViewHolder(val binding: MsgsFavDeBinding) : RecyclerView.ViewHolder(binding.root) {
        var adView: AdView?=null
        init {
                binding.favBtn.setOnClickListener {
                onItemClick?.invoke(msgs_fav_list[adapterPosition])
                }
//            adView= itemView.findViewById(R.id.adView)
            binding.moreBtnFav.setOnClickListener{popupMenus(it)}
        }

        private fun popupMenus(view: View) {

            val current_msgsModel = msgs_fav_list[position]
            val popupMenu = PopupMenu(con,view)
            popupMenu.inflate(R.menu.menu_msg)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.share ->{
                        val v = LayoutInflater.from(con).inflate(R.layout.msgs_design,null)
                        val tvMsg = v.findViewById<TextView>(R.id.tvMsg_m)

                        Utils.IntenteShare(con, "مسجاتي", "مسجاتي",binding.tvMsgM.text.toString() )
                        Toast.makeText(con, "share", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.copy ->{
                        val v = LayoutInflater.from(con).inflate(R.layout.msgs_design,null)
                        val tvMsg = v.findViewById<TextView>(R.id.tvMsg_m)

//                        val stringYouExtracted: String = tvMsg.getText().toString()
                        val stringYouExtracted: String = binding.tvMsgM.text.toString()

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                            val clipboard =
                                con.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.text = stringYouExtracted
                        } else {
                            val clipboard =
                                con.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = ClipData
                                .newPlainText(stringYouExtracted, stringYouExtracted)
                            clipboard.setPrimaryClip(clip)
                        }
                        Toast.makeText(con, "تم نسخ النص", Toast.LENGTH_SHORT).show()
                        Toast.makeText(con, "copy", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.edit ->{
                        val dir = FavoriteFragmentDirections.actionFavoriteFragmentToEditFragment(
                            binding.tvMsgM.text.toString()
                        )
                        NavHostFragment.findNavController(frag).navigate(dir)
                        true
                    }

                    else -> true
                }
            }
            popupMenu.show()
        }

    }
    private val diffCallback = object : DiffUtil.ItemCallback<FavoriteModel>(){
        override fun areItemsTheSame(oldItem: FavoriteModel, newItem: FavoriteModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteModel, newItem: FavoriteModel): Boolean {
            return newItem == oldItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var msgs_fav_list: List<FavoriteModel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MsgsFavDeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Msgs_Fav_Adapter.MyViewHolder, position: Int) {
        val current_msgsModel = msgs_fav_list[position]
        holder.binding.apply {
            tvTitleM.text=current_msgsModel.TypeTitle
            tvMsgM.text=current_msgsModel.MessageName

            newMsgM.setImageResource(R.drawable.new_msg)

            if (current_msgsModel.new_msgs == 0){

                newMsgM.setVisibility(View.INVISIBLE)
            }
            else {
                newMsgM.setVisibility(View.VISIBLE)
            }
        }

//        if (position % adCount == 0) {  // تحقق مما إذا كانت هذه العنصر هي عنصر الإعلان
//            Log.d("AD_TAG", "Loading Ad at position $position")
//            holder.adView?.loadAd(AdRequest.Builder().build())  // تحميل الإعلان
//
//        }

//        if (position % adCount == 0) {
//            Log.d("AD_TAG", "Loading Ad at position $position")
//            holder.adView?.loadAd(AdRequest.Builder().build())
//        } else {
//            holder.adView!!.visibility = View.GONE
//        }

//        if (recyclerView.adapter?.itemCount == 0) {
//            adView.visibility = View.GONE
//        } else {
//            adView.visibility = View.VISIBLE
//            // Load AdMob banner ad here
//        }
    }

    override fun getItemCount(): Int {
        return msgs_fav_list.size
    }
}