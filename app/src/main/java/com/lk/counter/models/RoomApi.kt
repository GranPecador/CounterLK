package com.lk.counter.models

import com.google.gson.annotations.SerializedName

data class RoomApi(@SerializedName("id") val roomId:Int,
                   @SerializedName("name") val name:String)