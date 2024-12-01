package com.adira.signmaster.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.adira.signmaster.R
import com.adira.signmaster.ui.login.LoginActivity


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        // Find the button in the layout
        val buttonLogin = rootView.findViewById<Button>(R.id.buttonLogin)

        // Set an OnClickListener to navigate to LoginActivity when button is clicked
        buttonLogin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }
}
