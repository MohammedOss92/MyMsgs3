package com.abdallah.sarrawi.mymsgs.models

import androidx.room.Embedded

data class MsgModelWithTitle(
    @Embedded
    var msgModel: MsgsModel? = null,
    val typeTitle: String = ""

)
