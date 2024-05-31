package com.abdallah.sarrawi.mymsgs.db.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.models.MsgsModel

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add_fav(fav: FavoriteModel)

    //    @Query("Select * from Favorite_table")
    @Query("select e.*, c.MsgTypes as typeTitle from  Fav_table " +
            "e left join msgs_types_table c  on " +
            " c.id = e.ID_Type_id where " +
            "e.ID_Type_id order by e.createdAt DESC")
    fun getAllFav(): LiveData<List<FavoriteModel>>

    // delete favorite item from db
    @Delete
    suspend fun deletefav(item:FavoriteModel)

    //@Query("SELECT ID_Type_id AS idType FROM Favorite_table ORDER BY ID_Type_id DESC")
    //fun getAllFavMsgTypeIds(): LiveData<List<FavoriteModel>>


}