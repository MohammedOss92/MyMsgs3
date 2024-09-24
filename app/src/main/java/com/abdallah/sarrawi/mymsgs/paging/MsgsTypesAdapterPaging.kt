package com.abdallah.sarrawi.mymsgs.paging

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdallah.sarrawi.mymsgs.databinding.MsgstypeslayoutBinding
import com.abdallah.sarrawi.mymsgs.models.MsgsTypeWithCount
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel
import com.abdallah.sarrawi.mymsgs.ui.fragments.FirstFragmentDirections

class MsgsTypesAdapterPaging (val con: Context, val frag: Fragment): PagingDataAdapter<MsgsTypeWithCount, MsgsTypesAdapterPaging.ViewHolder>(COMPARATOR) {


    var onItemClick: ((Int) -> Unit)? = null

    inner class ViewHolder(private val binding: MsgstypeslayoutBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { item ->
                        // تمرير المعرف إلى الاتجاه
//                        Log.d("MsgAdapter", "Item clicked ID: ${item.msgTypes?.id}")
                        Log.d("MsgAdapter", "Item clicked ID: ${item.msgTypes!!.id}")

                        val direction =
                            FirstFragmentDirections.actionFirsFragmentToSecondFragment(
//                                item.msgTypes!!.id
                                item.msgTypes!!.id
                            )
                        NavHostFragment.findNavController(frag).navigate(direction)

                        // يمكنك استدعاء onItemClick إذا كنت بحاجة لذلك
//                        onItemClick?.invoke(item.msgTypes!!.id)
                        onItemClick?.invoke(item.msgTypes!!.id)
                    }
                }


            }
        }


        fun bind(msgsTypeModel: MsgsTypeWithCount) {
            binding.apply {
                tvTitle.text = msgsTypeModel.msgTypes!!.MsgTypes
                tvCounter.text = msgsTypeModel.subCount.toString()
                tvnew.text = msgsTypeModel.newMsgsCount.toString()
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msgsTypeModel = getItem(position)
        msgsTypeModel?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MsgstypeslayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<MsgsTypeWithCount>() {
            override fun areItemsTheSame(oldItem: MsgsTypeWithCount, newItem: MsgsTypeWithCount): Boolean {
                return oldItem.msgTypes!!.id == newItem.msgTypes!!.id
            }

            override fun areContentsTheSame(oldItem: MsgsTypeWithCount, newItem: MsgsTypeWithCount): Boolean {
                return oldItem == newItem
            }
        }
    }
}