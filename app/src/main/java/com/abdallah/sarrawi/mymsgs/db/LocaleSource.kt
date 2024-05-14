package com.abdallah.sarrawi.mymsgs.db

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.abdallah.sarrawi.mymsgs.db.Dao.FavoriteDao
import com.abdallah.sarrawi.mymsgs.db.Dao.MsgsDao
import com.abdallah.sarrawi.mymsgs.db.Dao.MsgsTypesDao
import com.abdallah.sarrawi.mymsgs.models.*

class LocaleSource(context: Context) {

    private var TypesDao: MsgsTypesDao?
    private var Msgs_Dao: MsgsDao?
    private var FavoriteDao: FavoriteDao?

    var ID_Type_id: Int?=null


    init {
        val dataBase = PostDatabase.getInstance(context.applicationContext)
        TypesDao = dataBase.typesDao()
        Msgs_Dao = dataBase.msgsDao()
        FavoriteDao = dataBase.favoriteDao()

    }

    companion object {
        private var sourceConcreteClass: LocaleSource? = null
        fun getInstance(context: Context): LocaleSource {
            if (sourceConcreteClass == null)
                sourceConcreteClass = LocaleSource(context)
            return sourceConcreteClass as LocaleSource
        }
    }

    suspend fun getMsgsTypes_Dao(): List<MsgsTypesModel> {
        return TypesDao?.getMsgsTypes_Dao()!!
    }

    suspend fun getMsgsTypesWithCounts(): List<MsgsTypeWithCount>?{
        return TypesDao?.getAllMsgTypesWithCounts()
    }

    suspend fun getMsgs_Dao(id:Int): List<MsgsModel> {
        return Msgs_Dao?.getAllMsgsDao(id)!!
    }

    suspend fun getMsgsWithTitle(id: Int): List<MsgModelWithTitle>{
        return Msgs_Dao?.getAllMsgsDaoWithTitle(id)!!
    }

    fun getAllNewMsg(): LiveData<List<MsgModelWithTitle>>{
        return Msgs_Dao?.getAllNewMsg()!!
    }


    suspend fun insertPosts(posts: List<MsgsTypesModel>) {
        TypesDao?.insertPosts(posts)!!
    }

    suspend fun insert_msgs(msgs: List<MsgsModel>) {
        Msgs_Dao?.insert_msgs(msgs)!!
    }

    suspend fun deletePosts() {
        TypesDao?.deleteALlPosts()
    }

    /************************/
    suspend fun add_fav(fav: FavoriteModel){
        FavoriteDao?.add_fav(fav)
    }

    fun getAllFav(): LiveData<List<FavoriteModel>>{
        Log.e("tessst","entred666")
        return FavoriteDao?.getAllFav()!!
    }

    // delete favorite item from db
    suspend fun delete_fav(fav:FavoriteModel) {
        FavoriteDao?.deletefav(fav)!!
    }

    // update msg_table items favorite state
    suspend fun update_fav(id: Int,state:Boolean) {
        Msgs_Dao?.update_fav(id,state)!!
    }

}