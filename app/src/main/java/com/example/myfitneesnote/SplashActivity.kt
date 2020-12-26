package com.example.myfitneesnote


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.myfitneesnote.firebase.FirestoreClass

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        fullscreen()

        Handler().postDelayed({

            var currentUserID = FirestoreClass().getCurrentUserId()
            if (currentUserID.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish() }, 1500)

    }
}
