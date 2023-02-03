package com.example.guru2.Main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.guru2.R

class firstPage : AppCompatActivity() {
    lateinit var btn_start:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        btn_start=findViewById(R.id.btn_start)

        btn_start.setOnClickListener {

            val intent = Intent(this, Choice::class.java)
            startActivity(intent)
            finish()

        }


    }


}