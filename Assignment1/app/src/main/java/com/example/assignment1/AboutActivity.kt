package com.example.assignment1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Shows the back arrow on the left of the ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Called when that back arrow is tapped.
    // finish() closes only this activity, so the MainActivity that is already
    // sitting underneath it on the back stack is shown again. A new instance
    // of MainActivity is never created.
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
