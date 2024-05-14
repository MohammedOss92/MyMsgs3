package com.abdallah.sarrawi.mymsgs.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "msg_types_table")
data class MsgsTypesModel(
    // // sweilem edit

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @NonNull
    var id: Int = 0,

    @ColumnInfo(name = "MsgTypes")
    @SerializedName("MsgTypes")
    var MsgTypes: String?,

    @ColumnInfo(name = "new_msg")
    @SerializedName("new_msg")
    var new_msg: Int
)
