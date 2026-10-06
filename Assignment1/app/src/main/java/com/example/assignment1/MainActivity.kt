package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etHours: EditText
    private lateinit var etRate: EditText
    private lateinit var etTaxRate: EditText

    private lateinit var tvPay: TextView
    private lateinit var tvOvertimePay: TextView
    private lateinit var tvTotalPay: TextView
    private lateinit var tvTax: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etHours = findViewById(R.id.etHours)
        etRate = findViewById(R.id.etRate)
        etTaxRate = findViewById(R.id.etTaxRate)

        tvPay = findViewById(R.id.tvPay)
        tvOvertimePay = findViewById(R.id.tvOvertimePay)
        tvTotalPay = findViewById(R.id.tvTotalPay)
        tvTax = findViewById(R.id.tvTax)

        findViewById<Button>(R.id.btnCalculate).setOnClickListener {
            calculate()
        }
    }

    // Puts the items from main_menu.xml into the ActionBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Runs when a menu item is tapped
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_about) {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun calculate() {
        // readNumber returns null if the field is empty or not a valid number,
        // and marks the field with an error. return stops here if that happens.
        val hours = readNumber(etHours) ?: return
        val hourlyRate = readNumber(etRate) ?: return
        val taxRate = readNumber(etTaxRate) ?: return

        val pay: Double
        val overtimePay: Double

        if (hours <= 40) {
            pay = hours * hourlyRate
            overtimePay = 0.0
        } else {
            pay = 40 * hourlyRate
            overtimePay = (hours - 40) * hourlyRate * 1.5
        }

        val totalPay = pay + overtimePay
        val tax = pay * taxRate

        tvPay.text = getString(R.string.money, pay)
        tvOvertimePay.text = getString(R.string.money, overtimePay)
        tvTotalPay.text = getString(R.string.money, totalPay)
        tvTax.text = getString(R.string.money, tax)
    }

    private fun readNumber(field: EditText): Double? {
        val value = field.text.toString().trim().toDoubleOrNull()

        if (value == null || value < 0) {
            field.error = getString(R.string.error_invalid)
            return null
        }
        return value
    }
}
