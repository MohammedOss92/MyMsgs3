package com.abdallah.sarrawi.mymsgs.vm

//suspend fun refreshMsgswithID3(apiService: ApiService, database: PostDatabase, ID_Type_id: Int) {
//        var page = 1 // البدء بالصفحة الأولى
//        var msgList: List<MsgsModel>
//        var isLastPage = false // متغير للتحقق مما إذا كانت الصفحة الأخيرة
//
//        try {
//            do {
//                Log.d("API Debug", "Fetching messages for ID_Type_id: $ID_Type_id, page: $page")
//                val response = apiService.getMsgs_Ser2(ID_Type_id, page)
//
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    msgList = responseBody?.results?.MsgsModel ?: emptyList()
//
//                    if (msgList.isNotEmpty()) {
//                        Log.d("API Debug", "Inserting ${msgList.size} messages into database for ID_Type_id: $ID_Type_id, page: $page")
//                        database.msgsDao().insert_msgs(msgList)
//                        page++ // الانتقال إلى الصفحة التالية فقط إذا كانت البيانات موجودة
//                    } else {
//                        Log.d("API Info", "No more messages found for ID_Type_id: $ID_Type_id on page: $page")
//                        isLastPage = true // إذا كانت القائمة فارغة، اعتبر أنها الصفحة الأخيرة
//                    }
//                } else {
//                    // التحقق من حالة الخطأ
//                    when (response.code()) {
//                        404 -> {
//                            Log.e("API Error", "Invalid page: $page for ID_Type_id: $ID_Type_id, stopping fetch.")
//                            isLastPage = true // توقف إذا كانت الصفحة غير موجودة
//                        }
//                        else -> {
//                            Log.e("API Error", "Failed to fetch messages: ${response.errorBody()?.string() ?: "Unknown error"} for ID_Type_id: $ID_Type_id, page: $page")
//                            isLastPage = true // يمكنك اختيار إيقاف التكرار في حالة الخطأ
//                        }
//                    }
//                }
//            } while (!isLastPage) // استمر في التكرار حتى يتم كسر الحلقة عند الصفحة الأخيرة
//        } catch (e: IOException) {
//            Log.e("Network Error", "Network error occurred: ${e.message}")
//            throw IOException("Network error", e)
//        } catch (e: HttpException) {
//            Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
//        } catch (e: Exception) {
//            Log.e("General Error", "An unexpected error occurred: ${e.message}")
//        }
//    }
//
//
//
//    suspend fun refreshMsgswithID2(apiService: ApiService, database: PostDatabase, ID_Type_id: Int) {
//        var page = 1 // البدء بالصفحة الأولى
//
//        try {
//            do {
//                Log.d("API Debug", "Fetching messages for ID_Type_id: $ID_Type_id, page: $page")
//                val response = apiService.getMsgs_Ser2(ID_Type_id, page)
//
//                if (response.isSuccessful) {
//                    val msgList = response.body()?.results?.MsgsModel ?: emptyList()
//
//                    if (msgList.isNotEmpty()) {
//                        Log.d("API Debug", "Inserting ${msgList.size} messages into database for ID_Type_id: $ID_Type_id, page: $page")
//                        database.msgsDao().insert_msgs(msgList)
//                        page++ // الانتقال إلى الصفحة التالية فقط إذا كانت البيانات موجودة
//                    } else {
//                        Log.d("API Info", "No more messages found for ID_Type_id: $ID_Type_id on page: $page")
//                        break // إذا كانت القائمة فارغة، أوقف التكرار
//                    }
//                } else {
//                    // التحقق من حالة الخطأ
//                    when (response.code()) {
//                        404 -> {
//                            Log.e("API Error", "Invalid page: $page for ID_Type_id: $ID_Type_id, stopping fetch.")
//                            break // إيقاف التكرار إذا كانت الصفحة غير صحيحة
//                        }
//                        else -> {
//                            Log.e("API Error", "Failed to fetch messages: ${response.errorBody()?.string() ?: "Unknown error"} for ID_Type_id: $ID_Type_id, page: $page")
//                            break // يمكنك اختيار إيقاف التكرار في حالة الخطأ
//                        }
//                    }
//                }
//            } while (true) // استمر في التكرار حتى يتم كسر الحلقة
//        } catch (e: IOException) {
//            Log.e("Network Error", "Network error occurred: ${e.message}")
//            throw IOException("Network error", e)
//        } catch (e: HttpException) {
//            Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
//        } catch (e: Exception) {
//            Log.e("General Error", "An unexpected error occurred: ${e.message}")
//        }
//    }
//
//
//    fun getAllMsgsSerPag(ID_Type_id:Int): LiveData<PagingData<MsgsModel>> {
//        // تحقق من أن ID_Type_id ليس صفرًا
//        Log.d("MsgsPaging", "Fetching messages for ID_Type_id: $ID_Type_id")
//
//        return Pager(
//            config = PagingConfig(pageSize = 12, enablePlaceholders = false),
//            pagingSourceFactory = { MsgsPaging(ApiService.provideRetrofitInstance(), ID_Type_id) }
//        ).liveData
//    }

//suspend fun refreshMsgsType2(apiService: ApiService, database: PostDatabase, view: View) {
//        if (internetCheck(context)) {
//            CoroutineScope(Dispatchers.IO).launch {
//                var page = 1
//                var msgsTypesList: List<MsgsTypesModel>
//                var retryCount = 0
//
//                try {
//                    do {
//                        Log.d("API Debug", "Fetching msgs types for page: $page")
//                        val response = apiService.getMsgsTypes_Ser2(page)
//                        if (response.isSuccessful) {
//                            msgsTypesList = response.body()?.results?.MsgsTypesModel ?: emptyList()
//                            if (msgsTypesList.isNotEmpty()) {
//                                withContext(Dispatchers.IO) {
//                                    database.typesDao().insertPosts(msgsTypesList)
//                                }
//                                page++
//
//                                for (nokatType in msgsTypesList) {
//                                    refreshMsgswithID(apiService, database, nokatType.id)
//                                }
//
//                                retryCount = 0
//                            } else {
//                                break
//                            }
//                        } else {
//                            Log.e("API Error", response.errorBody()?.string() ?: "Unknown error")
//                            msgsTypesList = emptyList()
//                        }
//
//                        delay(500)
//
//                    } while (msgsTypesList.isNotEmpty() && retryCount < 3)
//                } catch (e: IOException) {
//                    Log.e("Network Error", "Network error occurred: ${e.message}")
//                    throw e
//                } catch (e: HttpException) {
//                    Log.e("HTTP Error", "HTTP error occurred: ${e.message}")
//                }
//            }
//        } else {
//            Snackbar.make(view, "يرجى التحقق من اتصالك بالإنترنت..", Snackbar.LENGTH_SHORT).show()
//        }
//    }