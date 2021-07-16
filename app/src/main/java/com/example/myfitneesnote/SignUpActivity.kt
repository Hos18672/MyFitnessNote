package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.myfitneesnote.firebase.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        fullscreen()
        setupActionBar()
        signUp_loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        sigUp_button.setOnClickListener {
                 signUpUser()
        }
    }
/*  var selectedImageUri : Uri?= null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("Upload Image", "Image was selected")
            val selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            signUp_image.setBackgroundDrawable(bitmapDrawable)
        }
    }*/
    /**
     * A function to register the user  and save data on  firestore database.
     */
   private  fun signUpUser(){
        val name     : String = signUpNameInput.text.toString().trim{ it <= ' '}
        val username : String = signUpUsernameInput.text.toString().trim{ it <= ' '}
        val email    : String = signUp_email_input.text.toString().trim{ it <= ' '}
        val password : String = signUp_password_input.text.toString().trim{ it <= ' '}
        val password2: String = signUp_password_input2.text.toString().trim{ it <= ' '}
        //val Image : String = signUp_image.toString().trim{ it <= ' '}
        if(validateForm(name, username, email, password) ) {
            if (password.equals(password2)) {
                // Toast.makeText(this, "Now we register User",Toast.LENGTH_SHORT).show()
                //password encryption
                val hashPass = BCrypt.withDefaults().hashToString(12, password.toCharArray())
                //showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val signUpedEmail = firebaseUser.email!!
                            val user = com.example.myfitneesnote.model.User(
                                firebaseUser.uid,
                                name,
                                username,
                                email,
                                hashPass
                               // Image
                            )
                            FirestoreClass().registerUser(this, user)
                            val uid = FirebaseAuth.getInstance().uid ?: ""
                            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
                            ref.setValue(user)
                                .addOnSuccessListener {
                                    Log.d("User", "Finally we saved the user to Firebase Database")
                                }
                                .addOnFailureListener {
                                    Log.d("User", "Failed to set value to database: ${it.message}")
                                }
                            //  FirebaseAuth.getInstance().signOut()
                            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("name", signUpNameInput.text.toString())
                            intent.putExtra("userName", signUpUsernameInput.text.toString())
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()
                            //hideProgressDialog1()
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }else{
                showErrorSnackBar("Passwords not match")
            }
        }
    }
/*    private fun uploadUserImageToFirebase(){
        if (selectedImageUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                Log.d("UserUploadImage", "Successfully uploaded${it.metadata?.path}")
            }
    }*/
    private  fun validateForm(name: String, username: String, email: String, password: String) : Boolean{
        return when{
            TextUtils.isEmpty(name)->{ showErrorSnackBar("Please enter a  name")
                false
            }
            TextUtils.isEmpty(username)->{
                showErrorSnackBar("Please enter a  username")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a  email address")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a  password")
                false
            }else ->{
                true
            }
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_sign_up_activity)
        var actionBar = supportActionBar
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_sign_up_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    /**
     * A function to be called the user is registered successfully and entry is made in the firestore database.
     */
    fun userRegisteredSuccess() {
        Toast.makeText(
            this@SignUpActivity,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()
        // Hide the progress dialog
       // hideProgressDialog()
        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
    override fun onBackPressed() { finish() }
}
