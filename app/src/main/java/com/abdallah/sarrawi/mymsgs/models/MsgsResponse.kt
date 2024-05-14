package com.abdallah.sarrawi.mymsgs.models

import com.google.gson.annotations.SerializedName

data class MsgsResponse(
    @SerializedName("MsgsModel")
    val results:List<MsgsModel>
)
