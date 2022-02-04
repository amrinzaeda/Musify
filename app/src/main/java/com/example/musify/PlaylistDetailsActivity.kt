package com.example.musify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class PlaylistDetailsActivity : AppCompatActivity() {
    private lateinit var buttonToHome : Button
    private lateinit var NameInputEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_details)


        val position = intent.extras?.getInt("position")
        NameInputEditText = findViewById(R.id.editTextTitle)

        buttonToHome = findViewById(R.id.buttonToHome)

        buttonToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("name", NameInputEditText.text.toString())
                putExtra("position", position)
            }
            startActivity(intent)
        }
    }
}