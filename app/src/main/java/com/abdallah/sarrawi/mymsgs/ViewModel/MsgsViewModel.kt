package com.abdallah.sarrawi.mymsgs.ViewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.abdallah.sarrawi.mymsgs.api.ApiService
import com.abdallah.sarrawi.mymsgs.models.FavoriteModel
import com.abdallah.sarrawi.mymsgs.models.MsgModelWithTitle
import com.abdallah.sarrawi.mymsgs.models.MsgsModel
import com.abdallah.sarrawi.mymsgs.models.MsgsTypesModel
import com.abdallah.sarrawi.mymsgs.repository.MsgsRepo
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MsgsViewModel constructor(private val msgsRepo:MsgsRepo):ViewModel() {

    private var __response = MutableLiveData<List<FavoriteModel>>()
    val responseMsgsFav: MutableLiveData<List<FavoriteModel>>
        get() = __response

    private val retrofitService = ApiService.provideRetrofitInstance()

    private val _response = MutableLiveData<List<MsgsModel>>()

    private val _responseWithTitle= MutableLiveData<List<MsgModelWithTitle>>()

    private val _responseWithTitlea= MutableLiveData<List<MsgModelWithTitle>>()

    suspend fun getAllMsgs(ID_Type_id:Int) :MutableLiveData<List<MsgsModel>> {

        msgsRepo.getMsgs_Ser(ID_Type_id).let { response ->

            if (response.isSuccessful) {
                _response.postValue(response.body()?.results)
                Log.i("TestRoom", "getAllMsgs: posts ${response.body()?.results}")
                msgsRepo.insert_msgs(response.body()?.results)
            } else {
                Log.i("TestRoom", "getAllMsgs: data corrupted")
                Log.d("tag", "getAll Error: ${response.code()}")
                Log.d("tag", "getAll: ${response.body()}")
            }
        }
        return _response
    }

    suspend fun getMsgsFromRoom_by_id(ID_Type_id:Int,context: Context) :MutableLiveData<List<MsgModelWithTitle>>{
        viewModelScope.launch {
            val response = msgsRepo.getMsgWithTitle(ID_Type_id)
            withContext(Dispatchers.Main) {
                if (response.isEmpty()) {
                    Log.i("TestRoom", "getPostsFromRoom: will cal api")
                    //no data in database so will call data from API
                    if (internetCheck(context)) {
                        getAllMsgs(ID_Type_id)
                    }else{
               Toast.makeText(context,"please check your internet connection..",Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.i("TestRoom", "getPostsFromRoom: get from Room")
                    _responseWithTitle.postValue(response)
                }
            }
        }
        return _responseWithTitle
    }



    /****************/
     fun add_fav(fav: FavoriteModel)= viewModelScope.launch {
        msgsRepo.add_fav(fav)
    }

    // update msg_table items favorite state
    fun update_fav(id: Int,state:Boolean) = viewModelScope.launch {
        msgsRepo.update_fav(id,state)
    }

    fun getAllNewMsg(): LiveData<List<MsgModelWithTitle>> {
        Log.e("tessst","entred22")
        return msgsRepo.getAllNewMsg()

    }

    fun getFav(): LiveData<List<FavoriteModel>> {
        Log.e("tessst","entred22")
//        viewModelScope.launch {
//          __response.postValue(msgsRepo.getAllFav())
//        }
        return msgsRepo.getAllFav()
    }

    // delete favorite item from db
    fun delete_fav(fav: FavoriteModel)= viewModelScope.launch {
        msgsRepo.deleteFav(fav)
    }
    /*********/


    suspend fun refreshMsgs(ID_Type_id:Int) {
        getAllMsgs(ID_Type_id)
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
}
//}




