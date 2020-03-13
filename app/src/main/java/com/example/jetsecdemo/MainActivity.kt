package com.example.jetsecdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_shared_prefs.setOnClickListener {
            startActivity(Intent(this, SharedPrefsActivity::class.java))
        }

        bt_files.setOnClickListener {
            startActivity(Intent(this, FilesActivity::class.java))
        }
    }
}
