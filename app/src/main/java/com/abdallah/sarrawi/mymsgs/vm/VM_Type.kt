package com.abdallah.sarrawi.mymsgs.vm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.models.MsgsTypeWithCount
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel
import com.abdallah.sarrawi.mymsgs.paging.MsgsTypePaging

class VM_Type constructor(private val repoType:Repo_Type, val context: Context, val database: PostDatabase): ViewModel(){

    private val _pagingSourceFactory = MutableLiveData<PagingSource<Int, MsgsTypesModel>>()

    val msgType: LiveData<PagingData<MsgsTypeWithCount>> = repoType.getAllMsgsTypes().cachedIn(viewModelScope)

    fun invalidatePagingSourceTypes() {
        _pagingSourceFactory.value = _pagingSourceFactory.value // قم بإعادة تعيين القيمة لتنشيط PagingSource
    }

    fun getAllMsgsTypesSerPag():LiveData<PagingData<MsgsTypesModel>>{

        return  Pager(
            config = PagingConfig(pageSize = 12,
                enablePlaceholders =  false
            ),

            pagingSourceFactory = { MsgsTypePaging(ApiService.provideRetrofitInstance()) }
        ).liveData
    }

}