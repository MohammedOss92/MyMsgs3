package com.abdallah.sarrawi.mymsgs.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.abdallah.sarrawi.mymsgs.db.Dao.FavoriteDao
import com.abdallah.sarrawi.mymsgs.db.Dao.MsgsDao
import com.abdallah.sarrawi.mymsgs.db.Dao.MsgsTypesDao
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.models.LocalDateTimeConverter
import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel
/*في كوتلن، الـ companion object هو كائن ثابت مرتبط بالكلاس نفسه، ويمكنك الوصول إليه من خلال الكلاس دون الحاجة لإنشاء كائن جديد من الكلاس. أما بالنسبة لـ Singleton فهو نمط تصميم يهدف إلى أن يكون هناك كائن واحد فقط من الكلاس طوال فترة تشغيل البرنامج.*/
@Database(
    entities = [MsgsTypesModel::class, MsgsModel::class, FavoriteModel::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
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
                "MsgsDb.db"
            )
                .fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2) // إضافة الهجرة هنا

                .build()
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE msgs_table ADD COLUMN isBookmark INTEGER NOT NULL DEFAULT 0") // 0 هي القيمة الافتراضية
            }
        }

    }
}
