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

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        SharedPrefManager.newInstance().initPref(applicationContext)
        if (SharedPrefManager.isLoggedIn()) {
            openPersonalCabinet()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginEdit = findViewById<TextInputEditText>(R.id.login_login_edt)
        val passwordEdit = findViewById<TextInputEditText>(R.id.login_password_edt)
        val loginButton = findViewById<MaterialButton>(R.id.login_enter_btn)
        loginButton.setOnClickListener {
            val login = loginEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()

            if (login.isEmpty()) {
                loginEdit.error = "Login required!"
                loginEdit.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordEdit.error = "Password required!"
                passwordEdit.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.postLogin(login, password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Не удалось подключиться",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                SharedPrefManager.setUser(it.token, login, it.role)
                                openPersonalCabinet()
                            }
                        } else
                            Toast.makeText(
                                applicationContext,
                                "Ошибка: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()

                    }

                })
        }
        val registrationButton = findViewById<MaterialButton>(R.id.login_registration_btn)
        registrationButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openPersonalCabinet(){
        when (SharedPrefManager.getRole()) {
            "admin" -> {
                val intent =
                    Intent(this@LoginActivity, PersonalAdminActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            "worker" -> {
                val intent =
                    Intent(this@LoginActivity, PersonalWorkerActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            "user" -> {
                val intent = Intent(this@LoginActivity, PersonalUserActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}
