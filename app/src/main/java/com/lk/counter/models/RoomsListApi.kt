package com.lk.counter.models

import com.google.gson.annotations.SerializedName

data class RoomsListApi(@SerializedName("rooms") val roomsList:List<RoomApi>)