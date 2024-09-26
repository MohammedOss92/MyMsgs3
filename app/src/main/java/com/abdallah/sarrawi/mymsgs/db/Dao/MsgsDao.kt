package com.abdallah.sarrawi.mymsgs.db.Dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*

import com.abdallah.sarrawi.mymsgs.models.MsgModelWithTitle
import com.abdallah.sarrawi.mymsgs.models.MsgsModel

@Dao
interface MsgsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert_msgs(msgs: List<MsgsModel>)



    @Query("Select * from msgs_table where ID_Type_id =:ID_Type_id order by id desc")
    suspend fun getAllMsgsDao(ID_Type_id: Int): List<MsgsModel>

    @Query("Select * from msgs_table where ID_Type_id =:ID_Type_id order by id desc")
    fun getAllMsgsDaopa(ID_Type_id: Int): PagingSource<Int, MsgsModel>

    @Query("select e.*, c.MsgTypes as typeTitle from  msgs_table e left join msgs_types_table c  on  c.id = e.ID_Type_id where e.ID_Type_id=:ID_Type_id order by id DESC")
    suspend fun getAllMsgsDaoWithTitle(ID_Type_id: Int): List<MsgModelWithTitle>

    @Query("select e.*, c.MsgTypes as typeTitle from  msgs_table e left join msgs_types_table c  on  c.id = e.ID_Type_id where e.ID_Type_id=:ID_Type_id order by id DESC")
    fun getAllMsgsDaoWithTitle2(ID_Type_id: Int): PagingSource<Int,MsgModelWithTitle>

    @Query("SELECT e.*, c.MsgTypes AS typeTitle " +
            "FROM msgs_table e " +
            "LEFT JOIN msgs_types_table c ON c.id = e.ID_Type_id " +
            "WHERE e.new_msgs = 1 " +
            "ORDER BY e.id DESC")
    fun getAllNewMsg(): LiveData<List<MsgModelWithTitle>>

    @Query("SELECT e.*, c.MsgTypes AS typeTitle " +
            "FROM msgs_table e " +
            "LEFT JOIN msgs_types_table c ON c.id = e.ID_Type_id " +
            "WHERE e.new_msgs = 1 " +
            "ORDER BY e.id DESC")
    fun getAllNewMsg2(): PagingSource<Int,MsgModelWithTitle>


    @Query("delete from msgs_table")
    fun deleteAllmessage()

    // update msg_table items favorite state
    @Query("Update msgs_table SET is_fav = :state where id =:ID")
    suspend fun update_fav(ID:Int,state:Boolean)
    //////////////////////////////

//    @Query("UPDATE msgs_table SET isBookmark = 0 WHERE isBookmark = 1")
    @Query("UPDATE msgs_table SET isBookmark = 0 WHERE isBookmark = 1 AND ID_Type_id = :typeId")
    suspend fun resetBookmarksInMsgs(typeId: Int)

    @Update
    suspend fun updateMsg(msg: MsgsModel)

    @Transaction
    suspend fun setBookmarkForMsg(newBookmark: MsgsModel) {
        // إعادة تعيين جميع العناصر المؤشرة المرتبطة بنفس ID_Type_id
        resetBookmarksInMsgs(newBookmark.ID_Type_id)
        // تحديث العنصر الجديد ليكون مؤشراً عليه
        updateMsg(newBookmark.copy(isBookmark = 1))
    }
}