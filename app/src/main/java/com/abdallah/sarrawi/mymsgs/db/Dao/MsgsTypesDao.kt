package com.abdallah.sarrawi.mymsgs.db.Dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.abdallah.sarrawi.mymsgs.models.MsgsTypeWithCount
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel

@Dao
interface MsgsTypesDao {



    /************************/

    @Query("SELECT * FROM msgs_types_table")
    suspend fun getMsgsTypes_Dao():  List<MsgsTypesModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<MsgsTypesModel>)

    @Query("DELETE FROM msgs_types_table")
    fun deleteALlPosts()



    @Query(
        "SELECT c.*, " +
                "COUNT(e.ID_Type_id) AS subCount, " +
                "SUM(CASE WHEN e.new_msgs = 1 THEN 1 ELSE 0 END) AS newMsgsCount " +
                "FROM msgs_types_table c " +
                "LEFT JOIN msgs_table e ON c.id = e.ID_Type_id " +
                "GROUP BY c.id"
    )
    suspend fun getAllMsgTypesWithCounts(): List<MsgsTypeWithCount>?


    @Query(
        "SELECT c.*, " +
                "COUNT(e.ID_Type_id) AS subCount, " +
                "SUM(CASE WHEN e.new_msgs = 1 THEN 1 ELSE 0 END) AS newMsgsCount " +
                "FROM msgs_types_table c " +
                "LEFT JOIN msgs_table e ON c.id = e.ID_Type_id " +
                "GROUP BY c.id"
    )
    fun getAllMsgTypesWithCountspa(): PagingSource<Int,MsgsTypeWithCount>

}