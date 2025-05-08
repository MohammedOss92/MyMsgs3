package com.abdallah.sarrawi.mymsgs.vm

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.db.PostDatabase
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.models.MsgModelWithTitle
import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel
import com.abdallah.sarrawi.mymsgs.paging.MsgsPaging
import com.abdallah.sarrawi.mymsgs.utils.NetworkConnection
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import java.io.IOException


class VM_Msgs(private val repo_type: Repo_Type, val context: Context, val database: PostDatabase): ViewModel()  {

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected


    private val _pagingSourceFactory = MutableLiveData<PagingSource<Int, MsgsModel>>()


    fun invalidatePagingSourceTypes() {
        _pagingSourceFactory.value = _pagingSourceFactory.value // قم بإعادة تعيين القيمة لتنشيط PagingSource
    }

    fun checkNetworkConnection(applicationContext: Context) {
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observeForever { isConnected ->
            _isConnected.value = isConnected
        }
    }
    fun internetCheck(c: Context): Boolean {
        val cmg = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+
            cmg.getNetworkCapabilities(cmg.activeNetwork)?.let { networkCapabilities ->
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        } else {
            return cmg.activeNetworkInfo?.isConnectedOrConnecting == true
        }

        return false
    }

    private val retrofitService = ApiService.provideRetrofitInstance()



    suspend fun refreshMsgsType(apiService: ApiService, database: PostDatabase, view: View) {
        if (internetCheck(context)) {
            var page = 1
            var msgsTypesList: List<MsgsTypesModel>

            try {
                do {
                    Log.d("API Debug", "Fetching msgs types for page: $page")
                    val response = apiService.getMsgsTypes_Ser2(page)
                    if (response.isSuccessful) {
                        msgsTypesList = response.body()?.results?.MsgsTypesModel ?: emptyList()

                        if (msgsTypesList.isNotEmpty()) {
                            // حفظ الأنواع في قاعدة البيانات
                            withContext(Dispatchers.IO) {
                                database.typesDao().insertPosts(msgsTypesList)
                            }

                            // جلب كل نوع بشكل متوازي
                            coroutineScope {
                                val jobs = msgsTypesList.map { type ->
                                    async {
                                        refreshMsgswithID(apiService, database, type.id)
                                    }
                                }
                                jobs.awaitAll() // انتظار كل العمليات أن تكتمل
                            }

                            page++
                        } else {
                            break
                        }
                    } else {
                        Log.e("API Error", response.errorBody()?.string() ?: "Unknown error")
                        msgsTypesList = emptyList()
                    }
                } while (msgsTypesList.isNotEmpty())
            } catch (e: IOException) {
                Log.e("Network Error", "Network error occurred: ${e.message}")
                throw e
            } catch (e: HttpException) {
                Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
            }
        } else {
            Snackbar.make(view, "يرجى التحقق من اتصالك بالإنترنت..", Snackbar.LENGTH_SHORT).show()
        }
    }

    suspend fun refreshMsgswithID(apiService: ApiService, database: PostDatabase, ID_Type_id: Int) {
        var page = 1
        val allMsgs = mutableListOf<MsgsModel>()
        var isLastPage = false

        try {
            do {
                val response = apiService.getMsgs_Ser2(ID_Type_id, page)
                if (response.isSuccessful) {
                    val msgList = response.body()?.results?.MsgsModel ?: emptyList()
                    if (msgList.isNotEmpty()) {
                        allMsgs.addAll(msgList)
                        page++
                    } else {
                        isLastPage = true
                    }
                } else {
                    isLastPage = true
                }
            } while (!isLastPage)

            if (allMsgs.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    database.msgsDao().insert_msgs(allMsgs)
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to fetch msgs for type $ID_Type_id: ${e.message}")
        }
    }


    fun getAllMsgsSerPag(ID_Type_id:Int): LiveData<PagingData<MsgsModel>> {
        // تحقق من أن ID_Type_id ليس صفرًا
        Log.d("MsgsPaging", "Fetching messages for ID_Type_id: $ID_Type_id")

        return Pager(
            config = PagingConfig(pageSize = 12, enablePlaceholders = false),
            pagingSourceFactory = { MsgsPaging(ApiService.provideRetrofitInstance(), ID_Type_id) }
        ).liveData
    }


    ///////////////////////////////////////
    //fav
    val favMsgs: LiveData<PagingData<FavoriteModel>> = repo_type.getAllFav().cachedIn(viewModelScope)

    fun update_favs(id: Int, state: Boolean) = viewModelScope.launch {
        repo_type.update_fav(id, state)
    }

    fun add_favs(fav: FavoriteModel) = viewModelScope.launch {
        repo_type.add_fav(fav)
    }

    fun delete_favs(fav: FavoriteModel) = viewModelScope.launch {
        repo_type.deleteFav(fav)
    }
    val favMsg: LiveData<PagingData<FavoriteModel>> = repo_type.getAllFav().cachedIn(viewModelScope)

    fun update_fav(id:Int,state:Boolean)=viewModelScope.launch {
        repo_type.update_fav(id,state)
    }

    //////////////////////////////
    var ID_Type_id =0
    val itemss: LiveData<PagingData<MsgModelWithTitle>> = repo_type.getAllMsgs(ID_Type_id).cachedIn(viewModelScope)
    fun itemsswhereID (ID_Type_id:Int):LiveData<PagingData<MsgModelWithTitle>>{
        return repo_type.getAllMsgs(ID_Type_id)

    }

    private val _itemss = MutableStateFlow<PagingData<MsgsModel>>(PagingData.empty())
    val itemsss: StateFlow<PagingData<MsgsModel>> = _itemss.asStateFlow()

    //////////////////////////
    //new
    fun getAllMsgsNew():LiveData<PagingData<MsgModelWithTitle>>{
        return repo_type.getAllMsgsNew()
    }

    ////////////////////////
    //bookmark
    fun setBookmarkForItem(item: MsgsModel) {
        viewModelScope.launch {
            repo_type.setBookmarkForItem(item)
        }
    }

    suspend fun deletemsgtypes(){
        repo_type.deletemsgtypes()
    }

    suspend fun deletemsg(){
        repo_type.deletemsgs()
    }

}