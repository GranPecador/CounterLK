package com.lk.counter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.lk.counter.R
import com.lk.counter.api.RetrofitClient
import com.lk.counter.models.LoginResponse
import com.lk.counter.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val loginEdit =  findViewById<TextInputEditText>(R.id.registration_login_edt)
        val passwordEdit = findViewById<TextInputEditText>(R.id.registration_password_edt)
        val emailEdit =  findViewById<TextInputEditText>(R.id.registration_email_edt)
        val phoneEdit = findViewById<TextInputEditText>(R.id.registration_telephone_edt)
        val nameEdit =  findViewById<TextInputEditText>(R.id.registration_name_edt)
        val surnameEdit = findViewById<TextInputEditText>(R.id.registration_surname_edt)
        val createUserBtn = findViewById<MaterialButton>(R.id.registration_create_btn)
        createUserBtn.setOnClickListener{

            val login = loginEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()
            val email = emailEdit.text.toString().trim()
            val phone = phoneEdit.text.toString().trim()
            val name = nameEdit.text.toString().trim()
            val surname = surnameEdit.text.toString().trim()

            if (login.isEmpty()){
                loginEdit.error = "Login required!"
                loginEdit.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()){
                passwordEdit.error = "Password required!"
                passwordEdit.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                emailEdit.error = "Email required!"
                emailEdit.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()){
                phoneEdit.error = "Phone required!"
                phoneEdit.requestFocus()
                return@setOnClickListener
            }
            if (name.isEmpty()){
                nameEdit.error = "Name required!"
                nameEdit.requestFocus()
                return@setOnClickListener
            }
            if (surname.isEmpty()){
                surnameEdit.error = "Surname required!"
                surnameEdit.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.postRegister(login,password, email, phone, name, surname)
                .enqueue(object: Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Trabli", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.isSuccessful) {
                            response.body()?.let {

                                SharedPrefManager.setUser(it.token, login, it.role)

                                val intent =
                                    Intent(this@RegistrationActivity, PersonalAdminActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        } else
                            Toast.makeText(applicationContext, "${response.code()}", Toast.LENGTH_LONG).show()

                    }

                })
        }
    }
}
