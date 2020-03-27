package com.lk.counter

import com.lk.counter.models.CounterGetApi

interface OnListCountersInteractionListener {
    fun onListCountersInteraction(item: CounterGetApi)

}