package com.example.myfitneesnote


import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        fullscreen()

        intro_login_button.setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))
        }
        intro_sign_up_button.setOnClickListener {

            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
