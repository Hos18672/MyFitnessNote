package com.example.myfitneesnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.myfitneesnote.BaseActivity
import com.example.myfitneesnote.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_security.*
import kotlinx.android.synthetic.main.activity_security.btnSave

class SecurityActivity : BaseActivity() {
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        mFirebaseInstance = FirebaseDatabase.getInstance()

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")

        val user = FirebaseAuth.getInstance().getCurrentUser()

        // add it only if it is not saved to database
        userId = user?.getUid()


        btnSave.setOnClickListener {
            onUpdateClicked()
        }
        btnback.setOnClickListener {
            onBackPressed()
        }
    }

    private fun updateUser(password1: String,password2: String, email: String) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2)&& !TextUtils.isEmpty(email) && password1 == password2) {
            mFirebaseDatabase!!.child(userId!!).child("password").setValue(password1)
            mFirebaseDatabase!!.child(userId!!).child("email").setValue(email)
            Toast.makeText(applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(applicationContext, "Unable to update user", Toast.LENGTH_SHORT).show()

    }

    fun onUpdateClicked() {
        val pass1 = editTextTextPassword.text.toString()
        val pass2 = editTextTextPasswordAgain.text.toString()
        val email = editTextTextEmailAddress.text.toString()

        //Calling updateUser function
        updateUser(pass1, pass2,email)
    }


}
