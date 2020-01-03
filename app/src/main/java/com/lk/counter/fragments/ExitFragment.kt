package com.lk.counter.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lk.counter.R
import com.lk.counter.storage.SharedPrefManager

/**
 * A simple [ExitFragment] subclass.
 */
class ExitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SharedPrefManager.deleteUserData()
        activity?.finish()
        return null
    }
}
