package com.abdallah.sarrawi.mymsgs.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.vm.Repo_Type
import com.abdallah.sarrawi.mymsgs.vm.VM_Type


class MyVMFactoryTypes constructor(private val repository: Repo_Type, val context: Context, val database: PostDatabase): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
         if (modelClass.isAssignableFrom(VM_Type::class.java)) {
             return  VM_Type(this.repository,context,database) as T
        }else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}