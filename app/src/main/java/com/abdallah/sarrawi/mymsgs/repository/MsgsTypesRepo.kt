package com.abdallah.sarrawi.mymsgs.repository

import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.db.LocaleSource
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel

class MsgsTypesRepo constructor(private val apiService: ApiService,private val localeSource: LocaleSource) {

    suspend fun getMsgsTypes_Ser() = apiService.getMsgsTypes_Ser()

    suspend fun getMsgsTypes_Dao() = localeSource.getMsgsTypes_Dao()
    suspend fun getMsgsTypesWithCount()= localeSource.getMsgsTypesWithCounts()


    suspend fun insertPosts (posts:List<MsgsTypesModel>?){
        if(!posts.isNullOrEmpty()){
            localeSource.insertPosts(posts)
        }
    }

}