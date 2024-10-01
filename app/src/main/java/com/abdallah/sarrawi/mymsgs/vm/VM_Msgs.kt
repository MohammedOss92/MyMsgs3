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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext



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



    suspend fun refreshMsgsType2(apiService: ApiService, database: PostDatabase, view: View) {
        if (internetCheck(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                var page = 1
                var msgsTypesList: List<MsgsTypesModel>
                var retryCount = 0

                try {
                    do {
                        Log.d("API Debug", "Fetching msgs types for page: $page")
                        val response = apiService.getMsgsTypes_Ser2(page)
                        if (response.isSuccessful) {
                            msgsTypesList = response.body()?.results?.MsgsTypesModel ?: emptyList()
                            if (msgsTypesList.isNotEmpty()) {
                                withContext(Dispatchers.IO) {
                                    database.typesDao().insertPosts(msgsTypesList)
                                }
                                page++

                                for (nokatType in msgsTypesList) {
                                    refreshMsgswithID(apiService, database, nokatType.id)
                                }

                                retryCount = 0
                            } else {
                                break
                            }
                        } else {
                            Log.e("API Error", response.errorBody()?.string() ?: "Unknown error")
                            msgsTypesList = emptyList()
                        }

                        delay(500)

                    } while (msgsTypesList.isNotEmpty() && retryCount < 3)
                } catch (e: IOException) {
                    Log.e("Network Error", "Network error occurred: ${e.message}")
                    throw e
                } catch (e: HttpException) {
                    Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
                }
            }
        } else {
            Snackbar.make(view, "يرجى التحقق من اتصالك بالإنترنت..", Snackbar.LENGTH_SHORT).show()
        }
    }



    suspend fun refreshMsgsType(apiService: ApiService, database: PostDatabase, view: View) {
        if (internetCheck(context)) {
            var page = 1 // البدء بالصفحة الأولى
            var msgsTypesList: List<MsgsTypesModel>

            try {
                do {
                    Log.d("API Debug", "Fetching msgs types for page: $page")
                    val response = apiService.getMsgsTypes_Ser2(page)
                    if (response.isSuccessful) {
                        msgsTypesList = response.body()?.results?.MsgsTypesModel ?: emptyList()
                        if (msgsTypesList.isNotEmpty()) {


                            withContext(Dispatchers.IO) {
                                try {
                                    Log.d("DB Debug", "Attempting to delete all posts")
                                    database.typesDao().deleteALlPosts()  // حذف البيانات القديمة
                                    Log.d("DB Debug", "Attempting to delete all posts")
                                    database.msgsDao().deleteAllmessage()  // حذف البيانات القديمة
                                    database.typesDao().insertPosts(msgsTypesList)
                                } catch (e: Exception) {
                                    Log.e("DB Error", "Error deleting all posts: ${e.message}")
                                }
                            }


                            page++
                            for (nokatType in msgsTypesList) {
                                refreshMsgswithID(apiService, database, nokatType.id)
                            }
                        } else {
                            break // أوقف التكرار إذا لم يكن هناك بيانات
                        }
                    } else {
                        Log.e("API Error", response.errorBody()?.string() ?: "Unknown error")
                        msgsTypesList = emptyList() // تعيين القائمة كفارغة لإيقاف التكرار
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
        val allMsgs = mutableListOf<MsgsModel>() // قائمة لتجميع جميع الرسائل من كل الصفحات
        var isLastPage = false

        try {
            do {
                Log.d("API Debug", "Fetching messages for ID_Type_id: $ID_Type_id, page: $page")
                val response = apiService.getMsgs_Ser2(ID_Type_id, page)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val msgList = responseBody?.results?.MsgsModel ?: emptyList()

                    if (msgList.isNotEmpty()) {
                        Log.d("API Debug", "Fetched ${msgList.size} messages for ID_Type_id: $ID_Type_id, page: $page")
                        allMsgs.addAll(msgList) // أضف جميع الرسائل إلى القائمة
                        page++ // الانتقال إلى الصفحة التالية
                    } else {
                        Log.d("API Info", "No more messages found for ID_Type_id: $ID_Type_id on page: $page")
                        isLastPage = true // الصفحة فارغة، اعتبرها الأخيرة
                    }
                } else {
                    // التعامل مع الأخطاء المحتملة في الرد من الـ API
                    when (response.code()) {
                        404 -> {
                            Log.e("API Error", "Page $page not found for ID_Type_id: $ID_Type_id, stopping fetch.")
                            isLastPage = true // إذا كانت الصفحة غير موجودة، أوقف التكرار
                        }
                        else -> {
                            Log.e("API Error", "Failed to fetch messages: ${response.errorBody()?.string() ?: "Unknown error"} for ID_Type_id: $ID_Type_id, page: $page")
                            isLastPage = true // توقف التكرار عند حدوث خطأ
                        }
                    }
                }
            } while (!isLastPage)

            if (allMsgs.isNotEmpty()) {
                // إدخال جميع الرسائل في قاعدة البيانات دفعة واحدة بعد الجلب
                Log.d("API Debug", "Inserting all ${allMsgs.size} messages into the database for ID_Type_id: $ID_Type_id")
                database.msgsDao().insert_msgs(allMsgs)
            } else {
                Log.d("API Info", "No messages to insert into the database for ID_Type_id: $ID_Type_id")
            }
        } catch (e: IOException) {
            Log.e("Network Error", "Network error occurred: ${e.message}")
            // عرض رسالة للمستخدم توضح أن هناك مشكلة في الشبكة
            throw IOException("Network error", e)
        } catch (e: HttpException) {
            Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
            // عرض رسالة للمستخدم توضح أن هناك مشكلة في الرد من الخادم
        } catch (e: Exception) {
            Log.e("General Error", "An unexpected error occurred: ${e.message}")
            // معالجة الأخطاء العامة لمنع تعطل التطبيق
        }
    }


    suspend fun refreshMsgswithID3(apiService: ApiService, database: PostDatabase, ID_Type_id: Int) {
        var page = 1 // البدء بالصفحة الأولى
        var msgList: List<MsgsModel>
        var isLastPage = false // متغير للتحقق مما إذا كانت الصفحة الأخيرة

        try {
            do {
                Log.d("API Debug", "Fetching messages for ID_Type_id: $ID_Type_id, page: $page")
                val response = apiService.getMsgs_Ser2(ID_Type_id, page)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    msgList = responseBody?.results?.MsgsModel ?: emptyList()

                    if (msgList.isNotEmpty()) {
                        Log.d("API Debug", "Inserting ${msgList.size} messages into database for ID_Type_id: $ID_Type_id, page: $page")
                        database.msgsDao().insert_msgs(msgList)
                        page++ // الانتقال إلى الصفحة التالية فقط إذا كانت البيانات موجودة
                    } else {
                        Log.d("API Info", "No more messages found for ID_Type_id: $ID_Type_id on page: $page")
                        isLastPage = true // إذا كانت القائمة فارغة، اعتبر أنها الصفحة الأخيرة
                    }
                } else {
                    // التحقق من حالة الخطأ
                    when (response.code()) {
                        404 -> {
                            Log.e("API Error", "Invalid page: $page for ID_Type_id: $ID_Type_id, stopping fetch.")
                            isLastPage = true // توقف إذا كانت الصفحة غير موجودة
                        }
                        else -> {
                            Log.e("API Error", "Failed to fetch messages: ${response.errorBody()?.string() ?: "Unknown error"} for ID_Type_id: $ID_Type_id, page: $page")
                            isLastPage = true // يمكنك اختيار إيقاف التكرار في حالة الخطأ
                        }
                    }
                }
            } while (!isLastPage) // استمر في التكرار حتى يتم كسر الحلقة عند الصفحة الأخيرة
        } catch (e: IOException) {
            Log.e("Network Error", "Network error occurred: ${e.message}")
            throw IOException("Network error", e)
        } catch (e: HttpException) {
            Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
        } catch (e: Exception) {
            Log.e("General Error", "An unexpected error occurred: ${e.message}")
        }
    }



    suspend fun refreshMsgswithID2(apiService: ApiService, database: PostDatabase, ID_Type_id: Int) {
        var page = 1 // البدء بالصفحة الأولى

        try {
            do {
                Log.d("API Debug", "Fetching messages for ID_Type_id: $ID_Type_id, page: $page")
                val response = apiService.getMsgs_Ser2(ID_Type_id, page)

                if (response.isSuccessful) {
                    val msgList = response.body()?.results?.MsgsModel ?: emptyList()

                    if (msgList.isNotEmpty()) {
                        Log.d("API Debug", "Inserting ${msgList.size} messages into database for ID_Type_id: $ID_Type_id, page: $page")
                        database.msgsDao().insert_msgs(msgList)
                        page++ // الانتقال إلى الصفحة التالية فقط إذا كانت البيانات موجودة
                    } else {
                        Log.d("API Info", "No more messages found for ID_Type_id: $ID_Type_id on page: $page")
                        break // إذا كانت القائمة فارغة، أوقف التكرار
                    }
                } else {
                    // التحقق من حالة الخطأ
                    when (response.code()) {
                        404 -> {
                            Log.e("API Error", "Invalid page: $page for ID_Type_id: $ID_Type_id, stopping fetch.")
                            break // إيقاف التكرار إذا كانت الصفحة غير صحيحة
                        }
                        else -> {
                            Log.e("API Error", "Failed to fetch messages: ${response.errorBody()?.string() ?: "Unknown error"} for ID_Type_id: $ID_Type_id, page: $page")
                            break // يمكنك اختيار إيقاف التكرار في حالة الخطأ
                        }
                    }
                }
            } while (true) // استمر في التكرار حتى يتم كسر الحلقة
        } catch (e: IOException) {
            Log.e("Network Error", "Network error occurred: ${e.message}")
            throw IOException("Network error", e)
        } catch (e: HttpException) {
            Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
        } catch (e: Exception) {
            Log.e("General Error", "An unexpected error occurred: ${e.message}")
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

}