package com.lk.counter.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.lk.counter.OnListCountersInteractionListener
import com.lk.counter.R
import com.lk.counter.fragments.AddAddressFragment
import com.lk.counter.fragments.AddressFragment
import com.lk.counter.models.CounterGetApi
import com.lk.counter.storage.SharedPrefManager

class PersonalAdminActivity : AppCompatActivity(), AddAddressFragment.OnFragmentInteractionListener, OnListCountersInteractionListener{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var addressId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!SharedPrefManager.isLoggedIn()){
            val intent =
                Intent(this@PersonalAdminActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_admin)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.personal_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.nav_add_address,
                R.id.nav_add_room,
                R.id.nav_activate_counter,
                R.id.nav_addresses,
                R.id.nav_rooms,
                R.id.nav_exit
            ), drawerLayout = drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerView = navView.getHeaderView(0)
        val usernameTxt = headerView.findViewById<TextView>(R.id.nav_personal_username_txt)
        usernameTxt.text = SharedPrefManager.getLogin()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onFragmentInteraction(uri: Uri) {
    }

    override fun onListCountersInteraction(item: CounterGetApi) {
        val intent = Intent(this, CounterActivity::class.java)
        intent.putExtra("COUNTER", item)
        //intent.putExtra("IDCOUNTER", item.counterId)
        //intent.putExtra("IDADDRESS", addressId)
        startActivity(intent)
    }
}
