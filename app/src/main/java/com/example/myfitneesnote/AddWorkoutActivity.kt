package com.example.myfitneesnote


import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.myfitneesnote.model.Workout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_add_workout.*

class AddWorkoutActivity : BaseActivity() {
    var  counter  = 0
    lateinit var workout : Workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)
        onClick()
    }
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun onClick(){
        btnBackNewWorkout.setOnClickListener {onBackPressed()}

        btn_set_minus.setOnClickListener{minusButton(SetNum)}
        btn_set_plus.setOnClickListener{plusButton(SetNum)}

        btn_weight_minus.setOnClickListener{minusButton(weightNum)}
        btn_weight_plus.setOnClickListener{plusButton(weightNum)}

        btn_break_minus.setOnClickListener{minusButton(breakNum)}
        btn_break_plus.setOnClickListener{plusButton(breakNum)}

        btn_repeat_minus.setOnClickListener{minusButton(repeatNum)}
        btn_repeat_plus.setOnClickListener{plusButton(repeatNum)}

        save_btn.setOnClickListener { save() }
    }
    private fun minusButton(et: EditText){
        var num = et.text.toString().toInt()
        num--
        et.text= "${num}".toEditable()
    }
    private fun plusButton(et: EditText){
        var num = et.text.toString().toInt()
        num++
        et.text= "${num}".toEditable()
    }
    private fun save(){
        val user = FirebaseAuth.getInstance().currentUser
        var userId = user?.uid.toString()
        var gymType : String? = intent.getStringExtra("GymName")
        var muskelName : String? = intent.getStringExtra("MuskelName")
        workout= Workout(userId,gymType,muskelName,SetNum.toString(),weightNum.toString(),breakNum.toString(),repeatNum.toString())

        Toast.makeText(
            this,
            "User: ${userId}\n"+ "Gym: ${gymType}\n" + "Set: ${SetNum.text}\n" + "Weight: ${weightNum.text}\n" + "Break: ${breakNum.text}\n" + "Repeat: ${repeatNum.text}",
            Toast.LENGTH_LONG
        ).show()
    }
}
