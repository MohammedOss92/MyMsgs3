package com.abdallah.sarrawi.mymsgs.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.vm.Repo_Type
import com.abdallah.sarrawi.mymsgs.vm.VM_Msgs


class MyVMFactory constructor(private val repository: Repo_Type, val context: Context, val database: PostDatabase, private val ID_Type_id:Int): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VM_Msgs::class.java)) {
            return  VM_Msgs(this.repository,context,database) as T
        }else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}