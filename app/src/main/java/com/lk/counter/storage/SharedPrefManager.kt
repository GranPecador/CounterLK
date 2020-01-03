package com.lk.counter.storage

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager() {


    val mFilePref = "com.example.tester"

    fun initPref(context: Context){
         mSharedPref = context.getSharedPreferences(mFilePref, Context.MODE_PRIVATE)
     }



    companion object {

        private val TOKENPREF = "token_pref"
        private val LOGINPREF = "login_pref"

        lateinit var mSharedPref : SharedPreferences

        fun newInstance(): SharedPrefManager {
            val utils = SharedPrefManager()
            return utils
        }

        fun isLoggedIn():Boolean{
            return getToken()
                .isNotEmpty()
        }

        fun setUser(token:String, login:String){
            val prefEditor = mSharedPref.edit()
            prefEditor.putString(TOKENPREF, token)
            prefEditor.putString(LOGINPREF, login)
            prefEditor.apply()
        }

        fun getToken():String{
            mSharedPref.getString(
                TOKENPREF,"")?.let{return it}
            return ""
        }
        fun getLogin():String{
            mSharedPref.getString(
                LOGINPREF, "")?.let{return it}
            return ""
        }

        fun deleteUserData(){
            val prefEditor = mSharedPref.edit()
            prefEditor.remove(TOKENPREF)
            prefEditor.remove(LOGINPREF)
            prefEditor.apply()
        }

    }

}