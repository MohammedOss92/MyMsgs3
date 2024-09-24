package com.abdallah.sarrawi.mymsgs.models.mod

import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel
import com.google.gson.annotations.SerializedName

data class Results(
                   @SerializedName("MsgsTypesModel")
                   val MsgsTypesModel:List<MsgsTypesModel>,
                   @SerializedName("MsgsModel")
                   val MsgsModel: List<MsgsModel>)
