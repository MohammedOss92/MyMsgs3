package com.abdallah.sarrawi.mymsgs.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "msgs_table",
    foreignKeys =[ForeignKey(entity = MsgsTypesModel::class, childColumns = ["ID_Type_id"], parentColumns = ["id"])])
data class MsgsModel(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @NonNull
    var id : Int = 0,

    @ColumnInfo("MessageName")
    @SerializedName("MessageName")
    var MessageName : String,

    @ColumnInfo("new_msgs")
    @SerializedName("new_msgs")
    var new_msgs: Int,

    @ColumnInfo("ID_Type_id", index = true)
    @SerializedName("ID_Type")
    var ID_Type_id: Int,

    @ColumnInfo("is_fav")
    @SerializedName("is_fav")
    var is_fav: Boolean = false,

    @ColumnInfo(name = "createdAt")
    var createdAt: String? = null, // قيمة افتراضية تكون null

    @ColumnInfo("isBookmark") // إضافة اسم العمود هنا
    var isBookmark: Int = 0, // القيمة الافتراضية ستكون 0

)
