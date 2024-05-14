package com.abdallah.sarrawi.mymsgs.models

import androidx.room.Embedded

data class MsgsTypeWithCount(
    @Embedded
    var msgTypes: MsgsTypesModel? = null,
    val subCount: Int = 0,
    val newMsgsCount: Int = 0
)
