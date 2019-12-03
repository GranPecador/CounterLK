package com.lk.counter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lk.counter.R
import com.lk.counter.models.CounterModel
import java.util.zip.Inflater

class CountersAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val counterList:MutableList<CounterModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_counter, parent,false)
        return CounterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return counterList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    inner class CounterViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }
}