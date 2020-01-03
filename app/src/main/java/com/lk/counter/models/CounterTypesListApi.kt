package com.lk.counter.models

import com.google.gson.annotations.SerializedName

data class CounterTypesListApi(@SerializedName("types") val counterTypesList:List<CounterTypeApi>)