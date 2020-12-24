package com.example.myfitneesnote

import activities.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        intro_login_button.setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))
        }
        intro_sign_up_button.setOnClickListener {

            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
