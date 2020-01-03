package com.lk.counter.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

import com.lk.counter.R
import com.lk.counter.api.RetrofitClient
import com.lk.counter.models.AddressPostApi
import com.lk.counter.models.MessageApi
import com.lk.counter.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddAddressFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddAddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAddressFragment : Fragment() {
    private var param1: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var loginEdt:TextInputEditText
    private lateinit var countryEdt:TextInputEditText
    private lateinit var cityEdt:TextInputEditText
    private lateinit var streetEdt:TextInputEditText
    private lateinit var homeNumberEdt:TextInputEditText
    private lateinit var flatEdt:TextInputEditText
    private lateinit var addAddressBtn:MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_address, container, false)
        initializeWidgets(view)
        addAddressBtn.setOnClickListener{
            verifyEdits()
        }
        return view
    }

    private fun initializeWidgets(view: View){
        loginEdt = view.findViewById(R.id.add_address_login_edt)
        countryEdt = view.findViewById(R.id.add_address_country_edt)
        cityEdt = view.findViewById(R.id.add_address_city_edt)
        streetEdt = view.findViewById(R.id.add_address_street_edt)
        homeNumberEdt = view.findViewById(R.id.add_address_home_number_edt)
        flatEdt = view.findViewById(R.id.add_address_apartment_edt)
        addAddressBtn = view.findViewById(R.id.add_address_create_btn)
    }

    private fun verifyEdits(){
        loginEdt.text?.let {
            if (it.isEmpty()) {
                loginEdt.error = "Заполните поле"
                loginEdt.requestFocus()
                return@verifyEdits
            }
        }
        countryEdt.text?.let {
            if (it.isEmpty()) {
                countryEdt.error = "Заполните поле"
                countryEdt.requestFocus()
                return@verifyEdits
            }
        }
        cityEdt.text?.let {
            if (it.isEmpty()) {
                cityEdt.error = "Заполните поле"
                cityEdt.requestFocus()
                return@verifyEdits
            }
        }
        streetEdt.text?.let {
            if (it.isEmpty()) {
                streetEdt.error = "Заполните поле"
                streetEdt.requestFocus()
                return@verifyEdits
            }
        }
        homeNumberEdt.text?.let {
            if (it.isEmpty()) {
                homeNumberEdt.error = "Заполните поле"
                homeNumberEdt.requestFocus()
                return@verifyEdits
            }
        }
        flatEdt.text?.let {
            if (it.isEmpty()) {
                flatEdt.error = "Заполните поле"
                flatEdt.requestFocus()
                return@verifyEdits
            }
        }
        val address = AddressPostApi(loginEdt.text.toString(), countryEdt.text.toString(), cityEdt.text.toString(),
            streetEdt.text.toString(), homeNumberEdt.text.toString().toInt(), flatEdt.text.toString().toInt())
        sendAddressToServer(address)
    }

    private fun sendAddressToServer(address:AddressPostApi){
        RetrofitClient.instance.postAddress(SharedPrefManager.getToken(), address).enqueue(object: Callback<MessageApi>{
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
                    Toast.makeText(context, "Адрес успешно добавлен", Toast.LENGTH_LONG).show()

                } else {
                    Log.e(
                        "Retfit",
                        "request sent unsuccessful\nCode: ${response.code()}\nMessage: ${response.body()?.message}"
                    )
                    Toast.makeText(context, "Возможно этот адрес уже существует в базе," +
                            "\n${response.code()}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment AddAddressFragment.
         */
        @JvmStatic
        fun newInstance(param1: String) =
            AddAddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
