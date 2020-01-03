package com.lk.counter.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lk.counter.R


import com.lk.counter.models.RoomApi

import kotlinx.android.synthetic.main.fragment_room.view.*

/**
 * [RecyclerView.Adapter] that can display a [RoomApi] and makes a call to the
 */
class MyRoomRecyclerViewAdapter: RecyclerView.Adapter<MyRoomRecyclerViewAdapter.ViewHolder>() {

    private val mValues: MutableList<RoomApi> = ArrayList()

    fun setData(newRooms:List<RoomApi>){
        mValues.clear()
        mValues.addAll(newRooms)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_room, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mRoomIdView.text = item.roomId.toString()
        holder.mNameView.text = item.name
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mRoomIdView: TextView = mView.room_id_item
        val mNameView: TextView = mView.room_name_item
    }
}
