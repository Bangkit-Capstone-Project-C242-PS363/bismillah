package com.adira.signmaster.ui.translate

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adira.signmaster.R

class TranslateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        supportActionBar?.hide()

        val ivSend = findViewById<ImageView>(R.id.ivSend)
        ivSend.setOnClickListener {
            // Logika untuk menangani klik
            Toast.makeText(this, "Send button clicked!", Toast.LENGTH_SHORT).show()
        }
    }


}