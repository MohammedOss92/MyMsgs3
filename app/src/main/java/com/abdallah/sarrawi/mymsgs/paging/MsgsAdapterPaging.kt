package com.abdallah.sarrawi.mymsgs.paging

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ClipData
import android.content.Context
import android.os.Build
import android.text.ClipboardManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdallah.sarrawi.mymsgs.R
import com.abdallah.sarrawi.mymsgs.Utils
import com.abdallah.sarrawi.mymsgs.databinding.MsgsDesignBinding
import com.abdallah.sarrawi.mymsgs.models.MsgModelWithTitle
import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import com.abdallah.sarrawi.mymsgs.ui.fragments.NewMsgsFragmentDirections
import com.abdallah.sarrawi.mymsgs.ui.fragments.SecondFragmentDirections

class MsgsAdapterPaging (val con: Context,val frag: Fragment): PagingDataAdapter<MsgModelWithTitle, MsgsAdapterPaging.ViewHolder>(COMPARATOR) {

        var onItemClick2: ((item:MsgsModel,position:Int) -> Unit)? = null
        var onItemClick: ((Int, MsgModelWithTitle, Int) -> Unit)? = null



        inner class ViewHolder(private val binding: MsgsDesignBinding) : RecyclerView.ViewHolder(binding.root) {
            init {

                binding.moreBtn.setOnClickListener {
                    popupMenus(it)
                }
            }

            fun bind(msgsModel: MsgModelWithTitle) {
                binding.apply {
//                tvTitle.text = nokatModel.NokatTypes
                    tvMsgM.text = msgsModel.msgModel!!.MessageName
                    newMsgM.setImageResource(R.drawable.new_msg)
                    newMsgM.visibility = if (msgsModel!!.msgModel!!.new_msgs == 0) View.INVISIBLE else View.VISIBLE

                    if (msgsModel!!.msgModel!!.is_fav) {
                        favBtn.setImageResource(R.drawable.baseline_favorite_true)
                    } else {
                        favBtn.setImageResource(R.drawable.baseline_favorite_border_false)
                    }

                    if (msgsModel!!.msgModel!!.new_msgs == 0) {
                        binding.newMsgM.setVisibility(View.INVISIBLE)
                    } else {
                        binding.newMsgM.setVisibility(View.VISIBLE)
                    }
                    binding.favBtn.setOnClickListener {

                        val position = bindingAdapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            getItem(position)?.let { item ->
                                onItemClick?.invoke(item.msgModel!!.id ?: 0, item, position)
                            }
                        }
                    }

                }

            }

            fun popupMenus(view: View) {

                val popupMenu = PopupMenu(con,view)
                popupMenu.inflate(R.menu.menu_msg)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.share ->{


                            Utils.IntenteShare(con, "مسجاتي", "مسجاتي",binding.tvMsgM.text.toString() )

                            true
                        }
                        R.id.copy ->{

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
                            true
                        }
                        R.id.edit ->{
                            Toast.makeText(con, "edit", Toast.LENGTH_SHORT).show()

                            val direction = SecondFragmentDirections.actionSecondFragmentToEditFragment(
                                binding.tvMsgM.text.toString()
                            )

                            NavHostFragment.findNavController(frag).navigate(direction)


                            true
                        }

                        R.id.edit ->{
                            Toast.makeText(con, "edit", Toast.LENGTH_SHORT).show()

                            val direction = NewMsgsFragmentDirections.actionSecondFragmentToEditFragment(
                                binding.tvMsgM.text.toString()
                            )

                            NavHostFragment.findNavController(frag).navigate(direction)


                            true
                        }

                        else -> true
                    }
                }
                popupMenu.show()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = MsgsDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val msgsModel = getItem(position)
            msgsModel?.let {
                holder.bind(it)
            }
        }

        companion object {
            private val COMPARATOR = object : DiffUtil.ItemCallback<MsgModelWithTitle>() {
                override fun areItemsTheSame(oldItem: MsgModelWithTitle, newItem: MsgModelWithTitle): Boolean {
                    return oldItem.msgModel!!.id == newItem.msgModel!!.id
                }

                override fun areContentsTheSame(oldItem: MsgModelWithTitle, newItem: MsgModelWithTitle): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

