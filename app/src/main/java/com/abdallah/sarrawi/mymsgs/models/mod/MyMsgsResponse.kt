package com.abdallah.sarrawi.mymsgs.models.mod

import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel
import com.google.gson.annotations.SerializedName

data class MyMsgsResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("total_pages")
    val total_pages: Int,
    @SerializedName("current_page")
    val current_page: Int,
    @SerializedName("results")
    val results: Results,
    @SerializedName("MsgsModel")
    val MsgsModel: List<MsgsModel>

)
