package com.abdallah.sarrawi.mymsgs.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "Fav_table")
data class FavoriteModel(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @NonNull
    var id : Int = 0,

    @ColumnInfo("MessageName")
    @SerializedName("MessageName")
    var MessageName : String,

    @ColumnInfo("TypeTitle")
    @SerializedName("TypeTitle")
    var TypeTitle: String,

    @ColumnInfo("new_msgs")
    @SerializedName("new_msgs")
    var new_msgs: Int,

    @ColumnInfo("ID_Type_id", index = true)
    @SerializedName("ID_Type_id")
    var ID_Type_id: Int,
    @ColumnInfo(name = "createdAt")
    var createdAt: String? = null

)
