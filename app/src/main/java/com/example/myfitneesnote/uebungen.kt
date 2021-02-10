package com.example.myfitneesnote

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_uebungen.*

class uebungen : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uebungen)
        fullscreen()
        setupActionBar()
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_uebungenactivity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_uebungenactivity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
