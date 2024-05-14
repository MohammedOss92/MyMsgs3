package com.abdallah.sarrawi.mymsgs.models

import androidx.room.Embedded

data class FavoriteModelWithTitle(
    @Embedded
    var favoriteModel: FavoriteModel? = null,
    val typeTitle: String = ""
)
