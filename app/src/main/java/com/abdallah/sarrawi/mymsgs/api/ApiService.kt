package com.abdallah.sarrawi.mymsgs.api

import com.abdallah.sarrawi.mymsgs.models.MsgsResponse
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesResponse
import com.abdallah.sarrawi.mymsgs.models.mod.MyMsgsResponse
import com.abdallah.sarrawi.mymsgs.models.mod.MyMsgsTypesResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("msgtypes_api_show")
    //sweilem edit
    suspend fun getMsgsTypes_Ser(): Response<MsgsTypesResponse>

    @GET("msgsapishow/{ID_Type_id}")
    suspend fun getMsgs_Ser
        (@Path("ID_Type_id") ID_Type_id:Int
    ):Response<MsgsResponse>
////////////////////////////
    @GET("msgstypespa")
    //sweilem edit
    suspend fun getMsgsTypes_Ser2(
    @Query("page") page: Int
    ): Response<MyMsgsTypesResponse>

    @GET("msgsapiidspa/{ID_Type_id}")
    suspend fun getMsgs_Ser2(
     @Path("ID_Type_id") ID_Type_id:Int,
     @Query("page") page: Int
    ):Response<MyMsgsResponse>





    companion object {
        var retrofitService: ApiService? = null
        fun provideRetrofitInstance(): ApiService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://159.223.123.196/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(ApiService::class.java)
            }
        return retrofitService!!
    }

    }

    }

