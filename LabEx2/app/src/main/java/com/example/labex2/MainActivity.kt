package com.example.labex2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var count = 0   // current output value
    private var step = 1    // 1 = default behaviour, 2 = step behaviour

    private lateinit var tvOutput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvOutput = findViewById(R.id.tvOutput)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnSubtract = findViewById<Button>(R.id.btnSubtract)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val btnStep = findViewById<Button>(R.id.btnStep)

        updateOutput()

        // Add: increase by current step
        btnAdd.setOnClickListener {
            count += step
            updateOutput()
        }

        // Subtract: decrease by current step
        btnSubtract.setOnClickListener {
            count -= step
            updateOutput()
        }

        // Reset: back to 0 and back to default behaviour
        btnReset.setOnClickListener {
            count = 0
            step = 1
            updateOutput()
        }

        // Step: change to increase/decrease by two
        btnStep.setOnClickListener {
            step = 2
        }
    }

    private fun updateOutput() {
        tvOutput.text = count.toString()
    }
}