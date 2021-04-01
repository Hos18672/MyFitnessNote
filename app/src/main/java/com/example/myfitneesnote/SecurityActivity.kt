package com.example.myfitneesnote


import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_security.*
import kotlinx.android.synthetic.main.activity_security.btnSave

class SecurityActivity : BaseActivity() {
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private var userId: String? = null
    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        userProfileData()
        mFirebaseInstance = FirebaseDatabase.getInstance()
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")
        val user = FirebaseAuth.getInstance().getCurrentUser()
        // add it only if it is not saved to database
        userId = user?.getUid()
        btnSave.setOnClickListener { onUpdateClicked() }
        btnback.setOnClickListener { onBackPressed() }
        auth = Firebase.auth
    }

    private fun updateUser2(currentPassword :String, password1: String,password2: String, email: String) {
        if (validateForm(currentPassword, password1, password2, email) && password1.equals(password2)) {
            val user = FirebaseAuth.getInstance().currentUser
            val currentPW = CurrentPassword.text.toString()
            auth.signInWithEmailAndPassword(email, currentPW)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // updating the user via child nodes
                        user!!.updatePassword(password1).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                println("Update Success")
                            } else {
                                println("Error Update")
                            }
                        }
                        user!!.updateEmail(email).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                println("Update Success")
                            } else {
                                println("Error Update")
                            }
                        }
                        Toast.makeText(applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext, "Current Password is wrong", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    private  fun updateUser(currentPassword :String, password1: String,password2: String, email: String){
        if(validateForm(currentPassword, password1, password2, email)) {
            if (password1.equals(password2)) {
                val user = FirebaseAuth.getInstance().currentUser
                val currentPW = CurrentPassword.text.toString()
                auth.signInWithEmailAndPassword(email, currentPW)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // updating the user via child nodes
                            user!!.updatePassword(password1).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    println("Update Success")
                                } else {
                                    println("Error Update")
                                }
                            }
                            user!!.updateEmail(email).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    println("Update Success")
                                } else {
                                    println("Error Update")
                                }
                            }
                            Toast.makeText(applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(applicationContext, "Current Password is wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                showErrorSnackBar("Passwords not match")
            }
        }
    }



    fun onUpdateClicked() {
        val currentPassword= CurrentPassword.text.toString()
        val pass1 = editTextTextPassword.text.toString()
        val pass2 = editTextTextPasswordAgain.text.toString()
        val email = editTextTextEmailAddress.text.toString()
        //Calling updateUser function
        updateUser(currentPassword,pass1, pass2,email)
    }
    private fun userProfileData() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.child("email").getValue(String::class.java)
                editTextTextEmailAddress.setText(email)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private  fun validateForm(currentPassword :String, password1: String,password2: String, email: String) : Boolean{
        return when{
            TextUtils.isEmpty(currentPassword)->{
                showErrorSnackBar("Current password is empty")
                false
            }
            TextUtils.isEmpty(password1)->{
                showErrorSnackBar("Password is empty")
                false
            }
            TextUtils.isEmpty(password2)->{
                showErrorSnackBar("Password is empty")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Email is empty")
                false
            }else ->{
                true
            }
        }
    }
}



