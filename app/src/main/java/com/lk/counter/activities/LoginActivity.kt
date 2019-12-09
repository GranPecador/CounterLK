package com.lk.counter.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.lk.counter.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<MaterialButton>(R.id.login_enter_btn)
        loginButton.setOnClickListener{
            Toast.makeText(this, "pfq", Toast.LENGTH_LONG).show()
        }
    }
}
