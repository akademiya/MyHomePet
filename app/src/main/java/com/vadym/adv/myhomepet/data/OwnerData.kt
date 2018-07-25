package com.vadym.adv.myhomepet.data

import com.google.gson.annotations.SerializedName

data class OwnerData(
        @SerializedName("userId") val userId: Int,
        @SerializedName("id") val id: Int
)