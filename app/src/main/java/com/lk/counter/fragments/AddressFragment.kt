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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.lk.counter.OnListCountersInteractionListener
import com.lk.counter.R
import com.lk.counter.adapters.MyCounterRecyclerViewAdapter
import com.lk.counter.api.RetrofitClient

import com.lk.counter.models.AddressApi
import com.lk.counter.models.AddressesListApi
import com.lk.counter.models.CountersListApi
import com.lk.counter.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 */
class AddressFragment : Fragment() {

    private var columnCount = 1

    private lateinit var addressesAdapter: ArrayAdapter<String?>
    private var addressesId:ArrayList<Int?> = ArrayList()
    private lateinit var dropdownMenu:AutoCompleteTextView

    private var listenerCounters: OnListCountersInteractionListener? = null
    private val adapterCounters = MyCounterRecyclerViewAdapter()

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
        val view = inflater.inflate(R.layout.fragment_counter_list, container, false)
        dropdownMenu =
            view.findViewById(R.id.fragment_addresses_dropdown_menu)
        addressesAdapter = ArrayAdapter(context, R.layout.dropdown_menu_popup_item, ArrayList())
        dropdownMenu.setAdapter(addressesAdapter)
        dropdownMenu.threshold = 0
        dropdownMenu.setOnItemClickListener { parent, view, position, id ->
            addressesId[position]?.let {
                getCountersFromServer(it)
                Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        getAddressesFromServer()

        val countersRecycler = view.findViewById<RecyclerView>(R.id.counters_list)
        adapterCounters.setListener(listenerCounters)
        // Set the adapter
        with(countersRecycler) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = adapterCounters
        }


        return view
    }

    private fun getAddressesFromServer() {
        RetrofitClient.instance.getAddresses(SharedPrefManager.getToken())
            .enqueue(object : Callback<AddressesListApi> {
                override fun onFailure(call: Call<AddressesListApi>, t: Throwable) {
                    Log.e("Retfit", "Error occurred while getting request!")
                    //t.printStackTrace()
                    Toast.makeText(context, "Не могу подключиться к серверу", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onResponse(
                    call: Call<AddressesListApi>,
                    response: Response<AddressesListApi>
                ) {
                    if (response.isSuccessful) {
                        Log.e(
                            "Retfit", "request sent successful\n Code: ${response.code()}"
                        )
                        response.body()?.let {
                            val tempAddresses = it.addressesList
                            if (tempAddresses != null) {
                                Log.e("Retrofit", "receive addresses list\n")
                                setAddressesList(tempAddresses)
                            }
                        }
                    } else {
                        Log.e(
                            "Retfit",
                            "request sent unsuccessful\nCode: ${response.code()}"
                        )
                        Toast.makeText(
                            context, "Возможно что-то случилось\n" +
                                    "Code: ${response.code()}", Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }

    fun setAddressesList(list: ArrayList<AddressApi>) {
        addressesAdapter.clear()
        addressesId.clear()
        list.forEach { addr ->
            addressesAdapter.add("${addr.country}, ${addr.city}, ${addr.street}, ${addr.homeNumber}, ${addr.flat}")
            addressesId.add(addr.addressId)
        }
        addressesAdapter.notifyDataSetChanged()
        dropdownMenu.listSelection = 0
        getCountersFromServer(list[0].addressId)
    }

    private fun getCountersFromServer(addressId: Int){
        RetrofitClient.instance.getCounters(SharedPrefManager.getToken(),addressId)
            .enqueue(object: Callback<CountersListApi>{
                override fun onFailure(call: Call<CountersListApi>, t: Throwable) {
                    Log.e("Retfit", "Error occurred while getting request!")
                    //t.printStackTrace()
                    Toast.makeText(context, "Не могу подключиться к серверу", Toast.LENGTH_LONG).show()                }

                override fun onResponse(
                    call: Call<CountersListApi>,
                    response: Response<CountersListApi>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            it.counters?.let{counters ->
                                adapterCounters.setData(counters)
                            }
                            if (!it.message.isNullOrBlank()){

                            }
                        }
                    }else{
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListCountersInteractionListener) {
            listenerCounters = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerCounters = null
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
