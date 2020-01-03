package com.lk.counter.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lk.counter.R
import com.lk.counter.adapters.MyRoomRecyclerViewAdapter
import com.lk.counter.api.RetrofitClient

import com.lk.counter.models.RoomApi
import com.lk.counter.models.RoomsListApi
import com.lk.counter.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class RoomFragment : Fragment() {

    private var columnCount = 1

    private val adapterRooms=MyRoomRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = adapterRooms
            }
        }
        getRoomsFromServer()
        return view
    }

    private fun getRoomsFromServer(){
        RetrofitClient.instance.getRooms(SharedPrefManager.getToken())
            .enqueue(object: Callback<RoomsListApi>{
                override fun onFailure(call: Call<RoomsListApi>, t: Throwable) {
                    Log.e("Retfit", "Error occurred while getting request!")
                    //t.printStackTrace()
                    Toast.makeText(context, "Не могу подключиться к серверу", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<RoomsListApi>,
                    response: Response<RoomsListApi>
                ) {
                    if (response.isSuccessful) {
                        Log.e(
                            "Retfit", "request sent successful\n Code: ${response.code()}"
                        )
                        val tempRooms: RoomsListApi? = response.body()
                        tempRooms?.let {
                            if (it.roomsList!=null) {
                                var roomsList = it.roomsList
                                Log.e("Retrofit", "receive rooms list\n")
                                setTestersList(roomsList)
                            }
                        }
                    } else {
                        Log.e(
                            "Retfit",
                            "request sent unsuccessful\nCode: ${response.code()}"
                        )
                        Toast.makeText(context, "Возможно что-то случилось\n" +
                                "Code: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    fun setTestersList(list: List<RoomApi>) {
        adapterRooms.setData(list)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            RoomFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
