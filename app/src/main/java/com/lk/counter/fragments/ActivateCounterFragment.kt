package com.lk.counter.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.lk.counter.R
import com.lk.counter.api.RetrofitClient
import com.lk.counter.models.*
import com.lk.counter.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActivateCounterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActivateCounterFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var addressesDropdownMenu:AutoCompleteTextView
    private lateinit var roomsDropdownMenu:AutoCompleteTextView
    private lateinit var counterTypesDropdownMenu:AutoCompleteTextView
    private lateinit var loginEdt:TextInputEditText
    private lateinit var serialNumberEdt:TextInputEditText
    private lateinit var activateBtn: MaterialButton
    private var selectedCurrentIdAddress : Int = 0
    private var selectedCurrentIdRoom : Int = 0
    private var selectedCurrentCounterType : Int = 0

    private lateinit var adapterAddress:ArrayAdapter<String?>
    private lateinit var adapterRoom:ArrayAdapter<String?>
    private lateinit var adapterCounterType:ArrayAdapter<String?>
    private var addressesId:ArrayList<Int> = ArrayList()
    private var roomsId:ArrayList<Int> = ArrayList()
    private var typesId:ArrayList<Int> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activate_counter, container, false)
        initializeWidgets(view)
        setInfoInDropdownMenus()
        activateBtn.setOnClickListener{
            verifyFields()


        }
        return view
    }

    private fun initializeWidgets(view:View){
        addressesDropdownMenu = view.findViewById(R.id.address_filled_exposed_dropdown)
        roomsDropdownMenu = view.findViewById(R.id.room_filled_exposed_dropdown)
        counterTypesDropdownMenu = view.findViewById(R.id.counter_type_filled_exposed_dropdown)
        loginEdt = view.findViewById(R.id.activate_counter_login_edt)
        serialNumberEdt = view.findViewById(R.id.activate_counter_serial_number_edt)
        activateBtn = view.findViewById(R.id.activate_counter_btn)
    }

    private fun setInfoInDropdownMenus(){
        context?.let {
            adapterAddress = ArrayAdapter(it, R.layout.dropdown_menu_popup_item, ArrayList())
            addressesDropdownMenu.setAdapter(adapterAddress)
            getAddressesFromServer()
            addressesDropdownMenu.setOnItemClickListener { parent, view, position, id ->
                selectedCurrentIdAddress = addressesId[position]
                Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
            }

            adapterRoom = ArrayAdapter(it, R.layout.dropdown_menu_popup_item, ArrayList())
            roomsDropdownMenu.setAdapter(adapterRoom)
            getRoomsFromServer()
            roomsDropdownMenu.setOnItemClickListener { parent, view, position, id ->
                selectedCurrentIdRoom = roomsId[position]
            }

            adapterCounterType =
                ArrayAdapter(it, R.layout.dropdown_menu_popup_item, ArrayList())
            counterTypesDropdownMenu.setAdapter(adapterCounterType)
            getCounterTypeFromServer()
            counterTypesDropdownMenu.setOnItemClickListener { parent, view, position, id ->
                selectedCurrentCounterType = typesId[position]
            }
        }
    }

    private fun getAddressesFromServer(){
        RetrofitClient.instance.getAddresses(SharedPrefManager.getToken())
            .enqueue(object : Callback<AddressesListApi> {
                override fun onFailure(call: Call<AddressesListApi>, t: Throwable) {
                    Log.e("Retfit", "Error occurred while getting request!")
                    //t.printStackTrace()
                    Toast.makeText(context, "Не могу подключиться к серверу", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<AddressesListApi>,
                    response: Response<AddressesListApi>
                ) {
                    if (response.isSuccessful) {
                        Log.e(
                            "Retfit", "request sent successful\n Code: ${response.code()}"
                        )
                        val tempAddresses: AddressesListApi? = response.body()
                        tempAddresses?.let {
                            if (it.addressesList!=null) {
                                Log.e("Retrofit", "receive addresses listuuuuuuuuuuuuuuuuuuuuuuuuuu\n")
                                var addressesList = it.addressesList
                                adapterAddress.clear()
                                addressesId.clear()
                                var addressesString = ArrayList<String>()
                                addressesList.forEach { addr ->
                                    addressesString.add("${addr.country}, ${addr.city}, ${addr.street}, ${addr.homeNumber}, ${addr.flat}")
                                    addressesId.add(addr.addressId)
                                    Log.e("Activate", "${addr.country}, ${addr.city}, ${addr.street}, ${addr.homeNumber}, ${addr.flat}")
                                }
                                if(addressesString.isEmpty())
                                    addressesString.add("Нет адресов доступных")
                                adapterAddress.addAll(addressesString)
                                adapterAddress.notifyDataSetChanged()
                                addressesDropdownMenu.setSelection(0)
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
                                adapterRoom.clear()
                                roomsId.clear()
                                val roomsNameString = ArrayList<String>()
                                roomsList.forEach {room->
                                    roomsNameString.add(room.name)
                                    roomsId.add(room.roomId)
                                }
                                adapterRoom.addAll(roomsNameString)
                                adapterRoom.notifyDataSetChanged()
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

    private fun getCounterTypeFromServer(){
        RetrofitClient.instance.getCounterTypes(SharedPrefManager.getToken())
            .enqueue(object : Callback<CounterTypesListApi>{
                override fun onFailure(call: Call<CounterTypesListApi>, t: Throwable) {
                    Log.e("Retfit", "Error occurred while getting request!")
                    //t.printStackTrace()
                    Toast.makeText(context, "Не могу подключиться к серверу", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<CounterTypesListApi>,
                    response: Response<CounterTypesListApi>
                ) {
                    if (response.isSuccessful) {
                        Log.e(
                            "Retfit", "request sent successful\n Code: ${response.code()}"
                        )
                        val temp: CounterTypesListApi? = response.body()
                        temp?.let {
                            if (it.counterTypesList!=null) {
                                var typesList = it.counterTypesList
                                Log.e("Retrofit", "receive rooms list\n")
                                adapterCounterType.clear()
                                typesId.clear()
                                val counterTypesString = ArrayList<String>()
                                typesList.forEach {type->
                                    counterTypesString.add(type.name)
                                    typesId.add(type.typeId)
                                }
                                adapterCounterType.addAll(counterTypesString)
                                adapterCounterType.notifyDataSetChanged()
                            }
                        }
                    } else {
                        Log.e(
                            "Retfit",
                            "request sent unsuccessful\nCode: ${response.code()}"
                        )
                        Toast.makeText(context, "Возможно что-то случилось\n" +
                                "Code: ${response.code()}", Toast.LENGTH_LONG).show()
                    }                }

            })
    }

    private fun verifyFields():Boolean{
//        if(addressesDropdownMenu.isSelected){
//
//        }
        loginEdt.text?.let {
            if (it.isEmpty()) {
                loginEdt.error = "Заполните поле"
                loginEdt.requestFocus()
                return@verifyFields false
            }
        }
        serialNumberEdt.text?.let {
            if (it.isEmpty()) {
                serialNumberEdt.error = "Заполните поле"
                serialNumberEdt.requestFocus()
                return@verifyFields false
            }
        }
        val addrPos = addressesDropdownMenu.listSelection
        val roomPos = roomsDropdownMenu.listSelection
        val typePos = counterTypesDropdownMenu.listSelection
        val counter = CounterActivatePostApi(loginEdt.text.toString(), selectedCurrentIdRoom,
            serialNumberEdt.text.toString(), selectedCurrentIdAddress, selectedCurrentCounterType)
        sendActivateCounterToServer(counter)
        return true
    }

    private fun sendActivateCounterToServer(counter:CounterActivatePostApi){
        RetrofitClient.instance.postActivateCounter(SharedPrefManager.getToken(), counter)
            .enqueue(object :Callback<MessageApi>{
                override fun onFailure(call: Call<MessageApi>, t: Throwable) {
                    Log.e("Retfit", "Error occurred while getting request!")
                    //t.printStackTrace()
                    Toast.makeText(context, "Не могу подключиться к серверу", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<MessageApi>, response: Response<MessageApi>) {
                    if (response.isSuccessful) {
                        Log.e(
                            "Retfit", "request sent successful\n Code: ${response.code()}"
                        )
                        val temp: MessageApi? = response.body()
                        Toast.makeText(context, temp?.message, Toast.LENGTH_LONG).show()
                    } else {
                        Log.e(
                            "Retfit",
                            "request sent unsuccessful\nCode: ${response.code()}"
                        )
                        val temp: MessageApi? = response.body()
                        Toast.makeText(context, "Что-то случилось\n" +
                                "Code: ${response.code()} \n${temp?.message}", Toast.LENGTH_LONG).show()
                    }                }

            })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ActivateCounterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ActivateCounterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
