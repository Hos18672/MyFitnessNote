package com.example.myfitneesnote.activities


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.myfitneesnote.R
import com.example.myfitneesnote.utils.showCustomToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@Suppress("NAME_SHADOWING")
class SettingsActivity : BaseActivity() {
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private var userId: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var email2 : String
    // Initialize Firebase Auth
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //fullscreen()
        setupActionBar()
        userProfileData()

        mFirebaseInstance = FirebaseDatabase.getInstance()
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")
        val user = FirebaseAuth.getInstance().currentUser
        // add it only if it is not saved to database
        userId = user?.uid
        btnSave.setOnClickListener {
            if (checkForInternet(this)) {
                onUpdatePasswordClicked()
            } else {
                showErrorSnackBar("No internet connection!")
            }
        }
        btnSave2.setOnClickListener {
            if (checkForInternet(this)) {
                onUpdateEmailClicked()
            } else {
                showErrorSnackBar("No internet connection!")
            }
        }
        auth = Firebase.auth
    }

    private  fun updateUserPassword(currentPassword :String, password1: String,password2: String, email: String){
        if(validatePasswordForm(currentPassword, password1, password2)) {
            if (isValidPassword(password1)){
                if (password1 == password2) {
                    if (password1.length >= 8 && password2.length >= 8){
                        val user = FirebaseAuth.getInstance().currentUser
                        auth.signInWithEmailAndPassword(email, currentPassword)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // updating the user via child nodes
                                    user!!.updatePassword(password1).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            finish()
                                            startActivity(intent)
                                        } else {
                                            showErrorSnackBar("Update failed")
                                        }
                                    }
                                    Toast.makeText(applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
                                } else {
                                    // If sign in fails, display a message to the user.
                                    showErrorSnackBar("Current Password is wrong")
                                }
                            }
                    }else{
                        showErrorSnackBar("Password lengths must be greater than 8!")
                    }

                }else{
                    showErrorSnackBar("Passwords not match")
                }
            }else{
                showErrorSnackBar("Your password must contain at least one uppercase, one lowercase letter, and one special character!")
            }

        }
    }

    private  fun updateUserEmail( email: String,currentPassword :String) {
        if(validateEmailForm(email, currentPassword)) {
                val user = FirebaseAuth.getInstance().currentUser
                auth.signInWithEmailAndPassword(email2, currentPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // updating the user via child nodes
                            user!!.updateEmail(email).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    updateUser(this,email)
                                    finish()
                                    startActivity(intent)
                                } else {
                                    showErrorSnackBar("Update failed")
                                }
                            }
                            Toast(this).showCustomToast ("Successfully updated", this)

                        } else {
                            // If sign in fails, display a message to the user.
                            showErrorSnackBar("Current Password is wrong")
                        }
                    }

            }
    }

    private fun updateUser(settingsActivity: SettingsActivity, email: String, ) {
        // updating the user via child nodes
        val fs  : FirebaseFirestore = FirebaseFirestore.getInstance()
        if (!TextUtils.isEmpty(email)) {
            mFirebaseDatabase!!.child(userId!!).child("email").setValue(email)
            fs.collection("users").document(userId!!).update("email", email.trim())
        }
        else
            showErrorSnackBar("Email update Failed!")
    }
    private fun onUpdateEmailClicked() {
        val email = editTextTextEmail.text.toString()
        val currentPassword= CurrentPasswordEmail.text.toString()
        //Calling updateUser function
       updateUserEmail(email,currentPassword)

    }
    private fun onUpdatePasswordClicked() {
        val currentPassword= CurrentPassword.text.toString()
        val pass1 = editTextTextPassword.text.toString()
        val pass2 = editTextTextPasswordAgain.text.toString()
        val email = editTextTextEmail.text.toString()
        //Calling updateUser function
        updateUserPassword(currentPassword,pass1, pass2,email)
    }
    private fun userProfileData() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            editTextTextEmail.setText(user.email)
            email2 = user.email.toString()

        }
    }
    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
    private  fun validatePasswordForm(currentPassword :String, password1: String,password2: String) : Boolean{
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
            }else ->{
                true
            }
        }
    }
    private  fun validateEmailForm(email: String,currentPassword :String) : Boolean{
        return when{
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Email is empty")
                false
            }
            TextUtils.isEmpty(currentPassword)->{
                showErrorSnackBar("Current password is empty")
                false
            }
            else ->{
                true
            }
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_security_activity)
        val actionBar = supportActionBar
        if(actionBar!=null)
        {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_security_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
