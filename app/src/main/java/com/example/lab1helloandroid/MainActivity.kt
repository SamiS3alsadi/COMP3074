package com.example.lab1helloandroid

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textViewHello)
        val button = findViewById<Button>(R.id.buttonChangeText)

        button.setOnClickListener {
            textView.text = "hi s3 was here!"
            button.text = "clicked!"

        }
    }
}
