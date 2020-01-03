package com.lk.counter.models

import com.google.gson.annotations.SerializedName

class AddressesListApi (@SerializedName("addresses") val addressesList:ArrayList<AddressApi>)