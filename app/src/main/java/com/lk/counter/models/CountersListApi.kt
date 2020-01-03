package com.lk.counter.models

import com.google.gson.annotations.SerializedName

data class CountersListApi(@SerializedName("counters") val counters:List<CounterGetApi>?,
                           @SerializedName("message") val message:String?)