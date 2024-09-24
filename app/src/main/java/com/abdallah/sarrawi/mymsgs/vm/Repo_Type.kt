package com.abdallah.sarrawi.mymsgs.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.db.LocaleSource
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.models.*
import com.abdallah.sarrawi.mymsgs.paging.MsgsPaging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class Repo_Type constructor(val apiService: ApiService, private val localeSource: LocaleSource, val database: PostDatabase){

    suspend fun update_fav(id: Int,state:Boolean) {

        localeSource.update_favMsg(id,state)
    }

    suspend fun add_fav(fav: FavoriteModel) {

        localeSource.insertFavoriteMsgs(fav)
    }


    // delete favorite item from db
    suspend fun deleteFav(fav: FavoriteModel) {

        localeSource.deleteFavoriteMsgs(fav)
    }


    fun getAllFav(): LiveData<PagingData<FavoriteModel>> {
        return Pager(
            config = PagingConfig(pageSize = 12, enablePlaceholders = false),
            pagingSourceFactory = { database.favoriteDao().getAllFavpa() }
        ).liveData
    }


    ///////////////////////////////
    fun getAllMsgs(ID_Type_id:Int): LiveData<PagingData<MsgModelWithTitle>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders =  false
            ),
            pagingSourceFactory = { database.msgsDao().getAllMsgsDaoWithTitle2(ID_Type_id) }
        ).liveData
    }

    fun getAllMsgsTypes(): LiveData<PagingData<MsgsTypeWithCount>> {
        return Pager(
            config = PagingConfig(
                pageSize = 12,
                enablePlaceholders =  false
            ),
            pagingSourceFactory = { database.typesDao().getAllMsgTypesWithCountspa() }
        ).liveData
    }


    private val ID_Type_id:Int=0
    fun getAllMsgsSerPag():LiveData<PagingData<MsgsModel>>{

        return  Pager(
            config = PagingConfig(pageSize = 12,
                enablePlaceholders =  false
            ),

            pagingSourceFactory = { MsgsPaging(apiService,ID_Type_id) }
        ).liveData
    }
}