package com.abdallah.sarrawi.mymsgs.paging

import android.util.Log
import retrofit2.HttpException
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import java.io.IOException

class MsgsPaging(
    private val apiService: ApiService,
    private val ID_Type_id:Int,
) : PagingSource<Int, MsgsModel>() {
    @RequiresApi(34)
    override suspend fun load(params: LoadParams<Int> ): LoadResult<Int, MsgsModel> {
        val page = params.key ?: 1

        return try {
            val response = apiService.getMsgs_Ser2(ID_Type_id,page)
            if (response.isSuccessful) {
                val msgsList = response.body()?.results?.MsgsModel?: emptyList()


                LoadResult.Page(
                    data = msgsList,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (msgsList.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error loading data. Response: ${response.code()}, ${response.message()}"))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }



    override fun getRefreshKey(state: PagingState<Int, MsgsModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}