package com.example.myfitneesnote.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.myfitneesnote.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_body_info.*

class BodyInfoActivity : BaseActivity() {
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    var fs  : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var userId: String? = null
    private  var getGender : Boolean = true

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_info)
        setupActionBar()
        mFirebaseInstance = FirebaseDatabase.getInstance()
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")
        val user = FirebaseAuth.getInstance().currentUser
        // add it only if it is not saved to database
        userId = user?.uid
        finish_button.setOnClickListener {
                onUpdateClicked()
        }

    }
    private fun updateUser(age: String, height :String, weight :String, gender: String) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(age) && !TextUtils.isEmpty(height)&& !TextUtils.isEmpty(weight)&& !TextUtils.isEmpty(gender)) {
            mFirebaseDatabase!!.child(userId!!).child("age").setValue(age)
            mFirebaseDatabase!!.child(userId!!).child("height").setValue(height)
            mFirebaseDatabase!!.child(userId!!).child("weight").setValue(weight)
            mFirebaseDatabase!!.child(userId!!).child("gender").setValue(gender)
            Toast.makeText(applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
            fs.collection("users").document(userId!!).update("age", age,
                "height", height,
                "weight", weight,
                "gender", gender)
        }
        else
            Toast.makeText(applicationContext, "Unable to update user", Toast.LENGTH_SHORT).show()


    }
    private fun onUpdateClicked() {
        val age = age_input.text.toString()
        val height = height_input.text.toString()
        val weight = weight_input.text.toString()
        val gender : String
        if(getGender){
            gender = "Male"
        }else{
            gender = "Female"
        }
        if (age.toInt() in 16..120 ){
            if(height.toInt() in 50 ..300){
                if(weight.toInt() in 20 ..300) {
                    updateUser(age, height, weight, gender)
                    startActivity(Intent(this, LoginActivity::class.java))
                }else{
                    showErrorSnackBar("Your weight must be between 20 kg and 300 kg!")
                }
            }else{
                showErrorSnackBar("Your height must be between 50 cm and 300 cm!")
            }
        }else{
            showErrorSnackBar("Your age must be between 16 and 120 years Old!")
        }

    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_info_activity)
        val actionBar = supportActionBar
        if(actionBar!=null)
        {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_info_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_male ->
                    if (checked) {
                        getGender = true
                    }
                R.id.radio_female ->
                    if (checked) {
                        getGender = false
                    }
            }
        }
    }

}