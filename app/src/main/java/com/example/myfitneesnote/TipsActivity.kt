package com.example.myfitneesnote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_add_workout.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_tips.*

class TipsActivity : BaseActivity() {
    var t :String? = ""
    var d :String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
      setupActionBar()
        var Topic = findViewById<TextView>(R.id.Topic)
        var Picture = findViewById<ImageView>(R.id.Picture) as ImageView
        var Description = findViewById<TextView>(R.id.Description)

         t =  intent.getStringExtra("TopicName")
         d =  intent.getStringExtra("DescriptionName")

        Topic.text = t
        Description.text  = d

    }



    private fun setupActionBar() {
        setSupportActionBar(toolBar_Tipps)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_Tipps.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}