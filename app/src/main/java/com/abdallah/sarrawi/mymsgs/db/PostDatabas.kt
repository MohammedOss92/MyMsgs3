package com.abdallah.sarrawi.mymsgs.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abdallah.sarrawi.mymsgs.db.Dao.FavoriteDao
import com.abdallah.sarrawi.mymsgs.db.Dao.MsgsDao
import com.abdallah.sarrawi.mymsgs.db.Dao.MsgsTypesDao
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.models.LocalDateTimeConverter
import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel

@Database(entities = [MsgsTypesModel::class, MsgsModel::class, FavoriteModel::class], version = 6, exportSchema = false)
//@TypeConverters(LocalDateTimeConverter::class)  // تأكد من إزالة التعليق إذا كنت بحاجة إلى محولات الأنواع
abstract class PostDatabase : RoomDatabase() {

    abstract fun typesDao(): MsgsTypesDao
    abstract fun msgsDao(): MsgsDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {

        @Volatile
        private var instance: PostDatabase? = null

        fun getInstance(context: Context): PostDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PostDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                PostDatabase::class.java,
                "PostDatabase.db"
            )
                .fallbackToDestructiveMigrationFrom(6)
                .build()
        }
    }
}
