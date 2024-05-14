package com.abdallah.sarrawi.mymsgs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdallah.sarrawi.mymsgs.R
import com.abdallah.sarrawi.mymsgs.databinding.MsgstypeslayoutBinding
import com.abdallah.sarrawi.mymsgs.models.MsgsTypeWithCount

class MsgsTypes_Adapter() : RecyclerView.Adapter<MsgsTypes_Adapter.MyViewHolder>() {
//    var isdark = false
//    constructor(isdark:Boolean) : this() {
//        this.isdark=isdark
//    }
//    var onItemClick: ((Int,String) -> Unit)? = null
    var onItemClick: ((Int) -> Unit)? = null

    inner class MyViewHolder(val binding : MsgstypeslayoutBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {


//                onItemClick?.invoke(msgsTypesModel[layoutPosition].id,msgsTypesModel[layoutPosition].MsgTypes!!)
                onItemClick?.invoke(msgsTypesModel[layoutPosition].msgTypes?.id?:0)

            }
        }

    }


    private val diffCallback = object : DiffUtil.ItemCallback<MsgsTypeWithCount>(){
        override fun areItemsTheSame(oldItem: MsgsTypeWithCount, newItem: MsgsTypeWithCount): Boolean {
            return oldItem.msgTypes?.id == newItem.msgTypes?.id

        }

        override fun areContentsTheSame(oldItem: MsgsTypeWithCount, newItem: MsgsTypeWithCount): Boolean {
            return newItem == oldItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var msgsTypesModel: List<MsgsTypeWithCount>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MsgstypeslayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current_msgsTypesModel = msgsTypesModel[position]
        holder.binding.apply {
            tvTitle.text = current_msgsTypesModel.msgTypes?.MsgTypes
//            newMsg.setImageResource(R.drawable.new_msg)
            tvnew.text = current_msgsTypesModel.newMsgsCount.toString()
            tvCounter.text = (current_msgsTypesModel.subCount.toString())

            if (current_msgsTypesModel.msgTypes?.new_msg == 0) {

//                newMsg.setVisibility(View.INVISIBLE)
                tvnew.setVisibility(View.INVISIBLE)
            } else {
//                newMsg.setVisibility(View.VISIBLE)
                tvnew.setVisibility(View.VISIBLE)
            }


        }

    }

    override fun getItemCount(): Int {
        return msgsTypesModel.size
    }
}