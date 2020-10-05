package com.h.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.h.users.ui.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, mainFragment)
                .commit()
        }
    }
}