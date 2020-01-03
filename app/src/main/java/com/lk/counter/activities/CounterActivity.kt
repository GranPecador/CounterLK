package com.lk.counter.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.lk.counter.R
import com.lk.counter.api.RetrofitClient
import com.lk.counter.models.CounterGetApi
import com.lk.counter.models.CountersListApi
import com.lk.counter.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounterActivity : AppCompatActivity() {

    private var counterId = -1
    private var addressId = -1

    private lateinit var counter:CounterGetApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        val bundle = intent.extras
        bundle?.let {
            //counterId = intent.getIntExtra("IDCOUNTER", -1)
            //addressId = intent.getIntExtra("IDADDRESS", -1)
            counter = intent.getParcelableExtra("COUNTER")
            setInfo(counter)
        }
    }

    private fun getCounterFromServer() {
        RetrofitClient.instance.getCounter(SharedPrefManager.getToken(), addressId, counterId)
            .enqueue(object : Callback<CounterGetApi> {
                override fun onFailure(call: Call<CounterGetApi>, t: Throwable) {
                    Log.e("Retfit", "Error occurred while getting request!")
                    //t.printStackTrace()
                    Toast.makeText(applicationContext, "Не могу подключиться к серверу", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onResponse(call: Call<CounterGetApi>,
                    response: Response<CounterGetApi>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            setInfo(it)
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

    fun setInfo(counter:CounterGetApi){
        findViewById<TextView>(R.id.counter_serial_number_text_activity).text = counter.serialNumber
        findViewById<TextView>(R.id.counter_data_text_activity).text = counter.data.toString()
        if(counter.open==0)
            findViewById<TextView>(R.id.counter_open_text_activity).text = "Нет"
        else
            findViewById<TextView>(R.id.counter_open_text_activity).text = "Да"
        findViewById<TextView>(R.id.counter_batt_text_activity).text = "${counter.battery}%"
        findViewById<TextView>(R.id.counter_room_text_activity).text = counter.roomName
        findViewById<TextView>(R.id.counter_type_text_activity).text = counter.counterTypeName
    }
}
