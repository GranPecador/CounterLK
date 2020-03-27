package com.lk.counter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lk.counter.OnListCountersInteractionListener
import com.lk.counter.R
import com.lk.counter.adapters.MyCounterRecyclerViewAdapter
import com.lk.counter.api.RetrofitClient
import com.lk.counter.models.*
import com.lk.counter.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalUserActivity : AppCompatActivity(),  OnListCountersInteractionListener{

    private lateinit var dropDownMenu : AutoCompleteTextView

    private lateinit var addressesAdapter: ArrayAdapter<AddressApi?>
    private val adapterCounters = MyCounterRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!SharedPrefManager.isLoggedIn()){
            val intent =
                Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_user)

        dropDownMenu = findViewById(R.id.personal_user_dropdown_menu)
        addressesAdapter = ArrayAdapter(applicationContext, R.layout.dropdown_menu_popup_item, ArrayList())
        dropDownMenu.setAdapter(addressesAdapter)
        dropDownMenu.threshold = 0
        dropDownMenu.setOnItemClickListener { parent, view, position, id ->
            addressesAdapter.getItem(position)?.let {
                getCountersByAddress(it.addressId)
                Toast.makeText(applicationContext, position.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        getAddressesFromServer()

        val recyclerViewCounters: RecyclerView = findViewById(R.id.personal_user_recycler_counters)
        adapterCounters.setListener(this)
        with(recyclerViewCounters){
            adapter = adapterCounters
            layoutManager = LinearLayoutManager(applicationContext)
        }
    }

    private fun getAddressesFromServer() {
        RetrofitClient.instance.getAddresses(SharedPrefManager.getToken())
            .enqueue(object : Callback<AddressesListApi> {
                override fun onFailure(call: Call<AddressesListApi>, t: Throwable) {
                    Log.e("Retfit", "Error occurred while getting request!")
                    //t.printStackTrace()
                    Toast.makeText(applicationContext, "Не могу подключиться к серверу", Toast.LENGTH_LONG)
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
                            applicationContext, "Возможно что-то случилось\n" +
                                    "Code: ${response.code()}", Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }

    fun setAddressesList(list: List<AddressApi>) {
        addressesAdapter.clear()
        addressesAdapter.addAll(list)
        addressesAdapter.notifyDataSetChanged()
        dropDownMenu.listSelection = 0
        getCountersByAddress(list[0].addressId)
    }

    fun getCountersByAddress(addressId: Int){
        RetrofitClient.instance.getCounters(SharedPrefManager.getToken(),addressId)
            .enqueue(object: Callback<CountersListApi>{
                override fun onFailure(call: Call<CountersListApi>, t: Throwable) {
                    Log.e("Retfit", "Error occurred while getting request!")
                    //t.printStackTrace()
                    Toast.makeText(applicationContext, "Не могу подключиться к серверу", Toast.LENGTH_LONG).show()                }

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
                        Toast.makeText(applicationContext, "Возможно что-то случилось\n" +
                                "Code: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater : MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.user_menu_exit_item -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout(){
        SharedPrefManager.deleteUserData()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onListCountersInteraction(item: CounterGetApi) {
        val intent = Intent(this, CounterActivity::class.java)
        intent.putExtra("COUNTER", item)
        startActivity(intent)
    }
}
