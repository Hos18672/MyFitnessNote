package com.example.myfitneesnote.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.myfitneesnote.R
import com.example.myfitneesnote.firebase.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash.*

@Suppress("DEPRECATION")
class SplashActivity : BaseActivity() {

    lateinit var mAuth: FirebaseAuth
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //fullscreen()
        animat()
        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.currentUser
        Handler().postDelayed({
            val currentUserID = FirestoreClass().getCurrentUserId()
            if (currentUserID.isNotEmpty() && user != null) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
                overridePendingTransition(R.anim.slidetoright, R.anim.slide_out_left)
            }
            finish()
        }, 900)

    }
    private fun animat(){
        val btt = AnimationUtils.loadAnimation(this, R.anim.btt)
        splash_textView.startAnimation(btt)
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        splash_imageView.startAnimation(ttb)
    }
}
