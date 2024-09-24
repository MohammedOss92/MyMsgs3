package com.abdallah.sarrawi.mymsgs.paging

import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel

import retrofit2.HttpException
import java.io.IOException

class MsgsTypePaging(private val apiService: ApiService

) : PagingSource<Int, MsgsTypesModel>() {

    @RequiresApi(34)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MsgsTypesModel> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getMsgsTypes_Ser2(page)
            if (response.isSuccessful) {
                val msgstypesList = response.body()?.results?.MsgsTypesModel ?: emptyList()



                LoadResult.Page(
                    data = msgstypesList,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (msgstypesList.isEmpty()) null else page + 1
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



    override fun getRefreshKey(state: PagingState<Int, MsgsTypesModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}