package com.abdallah.sarrawi.mymsgs.db.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert

import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abdallah.sarrawi.mymsgs.models.MsgModelWithTitle
import com.abdallah.sarrawi.mymsgs.models.MsgsModel

@Dao
interface MsgsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert_msgs(msgs: List<MsgsModel>)



    @Query("Select * from msg_table where ID_Type_id =:ID_Type_id")
    suspend fun getAllMsgsDao(ID_Type_id: Int): List<MsgsModel>

    @Query("select e.*, c.MsgTypes as typeTitle from  msg_table e left join msg_types_table c  on  c.id = e.ID_Type_id where e.ID_Type_id=:ID_Type_id order by id DESC")
    suspend fun getAllMsgsDaoWithTitle(ID_Type_id: Int): List<MsgModelWithTitle>

    @Query("SELECT e.*, c.MsgTypes AS typeTitle " +
            "FROM msg_table e " +
            "LEFT JOIN msg_types_table c ON c.id = e.ID_Type_id " +
            "WHERE e.new_msgs = 1 " +
            "ORDER BY e.id DESC")
    fun getAllNewMsg(): LiveData<List<MsgModelWithTitle>>


    @Query("delete from msg_table")
    fun deleteAllmessage()

    // update msg_table items favorite state
    @Query("Update msg_table SET is_fav = :state where id =:ID")
    suspend fun update_fav(ID:Int,state:Boolean)
}