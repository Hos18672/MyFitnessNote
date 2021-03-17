package com.example.myfitneesnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_uebung.*
import kotlinx.android.synthetic.main.activity_muskel_group.*

class AddWorkoutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_uebung)
        fullscreen()
        save_btn.setOnClickListener {
            onBackPressed()
        }
        back_btn3.setOnClickListener {
            onBackPressed()
        }
    }
}
