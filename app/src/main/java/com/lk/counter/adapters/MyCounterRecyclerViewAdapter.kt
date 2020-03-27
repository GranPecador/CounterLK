package com.lk.counter.adapters

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lk.counter.OnListCountersInteractionListener
import com.lk.counter.R


import com.lk.counter.models.CounterGetApi

import kotlinx.android.synthetic.main.fragment_counter.view.*

/**
 * [RecyclerView.Adapter] that can display a [Counter] and makes a call to the
 * specified [CounterOnListFragmentInteractionListener].
 */
class MyCounterRecyclerViewAdapter: RecyclerView.Adapter<MyCounterRecyclerViewAdapter.ViewHolder>() {
    private val mValues: MutableList<CounterGetApi> = ArrayList()
    private var mListenerCounters: OnListCountersInteractionListener? = null
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as CounterGetApi
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListenerCounters?.onListCountersInteraction(item)
        }
    }

    fun setListener(listenerCounters: OnListCountersInteractionListener?){
        mListenerCounters = listenerCounters
    }

    fun setData(newCounters:List<CounterGetApi>){
        mValues.clear()
        mValues.addAll(newCounters)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_counter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mSerialNumberView.text = item.serialNumber
        holder.mDataView.text = "${item.data}"
        if (item.counterTypeName == "Горячая вода")
            holder.mCircul.background = holder.blue
        else
            holder.mCircul.background =holder.red
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mSerialNumberView: TextView = mView.counter_serial_number_text_info
        val mDataView: TextView = mView.counter_data_text_info
        val mCircul:ImageView = mView.counter_circuit_type
        val blue:Drawable=mView.resources.getDrawable(R.drawable.layout_blue_circuit)
        val red:Drawable=mView.resources.getDrawable(R.drawable.layout_red_circuit)
    }
}
