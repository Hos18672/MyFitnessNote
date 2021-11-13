package com.example.myfitneesnote

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
    var p :String? = ""
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
        window.navigationBarColor = android.R.color.white
      setupActionBar()
        var Topic = findViewById<TextView>(R.id.Topic)
        var Picture = findViewById<ImageView>(R.id.Picture) as ImageView
        var Description = findViewById<TextView>(R.id.Description)

         t =  intent.getStringExtra("TopicName")
         d =  intent.getStringExtra("DescriptionName")
         p = intent.getStringExtra("ImageSrc")

        Topic.text = t
        Description.text  = d
        if (t == "EZ Bar Curl"){
            Picture.setImageResource(R.drawable.ez_bar_curl)
        }
        if (t == "Dumbbell Curl"){
            Picture.setImageResource(R.drawable.curl_dumbell)
        }
        if (t == "One arm dumbbell Curl"){
            Picture.setImageResource(R.drawable.one_arm_dumbell)
        }
        if (t == "Seated biceps curls"){
            Picture.setImageResource(R.drawable.biceps_curl)
        }


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


