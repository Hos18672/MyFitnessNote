package com.example.myfitneesnote

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.model.User
import com.example.myfitneesnote.utils.Constant
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main_layout.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LoginActivity : BaseActivity() {
    val database = FirebaseDatabase.getInstance()
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        fullscreen()
        setupActionBar()
        login_signUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        sing_in_button.setOnClickListener {
                loginUser()
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_login_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_login_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    /**
     * A function to login the user.
     */
    private fun loginUser() {
        val email: String = login_email_input.text.toString().trim { it <= ' ' }
        val password: String = login_password_input.text.toString().trim { it <= ' ' }
        val pb = findViewById(R.id.progressBar_login) as ProgressBar

        if (validateForm(email, password)) {
                //showProgressDialog(resources.getString(R.string.please_wait))
                // create an instance and create a register a user with email and password
                pb.visibility = View.VISIBLE
                //sign_in_btn_text.setText("Please wait ...")
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        //hideProgressDialog()
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this@LoginActivity,
                                "Your are logged in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()

                        } else {
                            pb.visibility = View.GONE
                            Toast.makeText(
                                this@LoginActivity, task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
        } else {
            //pb.visibility = View.GONE
            sign_in_btn_text.setText("Sign in")
        }
    }
    fun logInSuccess(user: User) {
        //hideProgressDialog1()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter a  email address")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a  password")
                false
            }
            else -> {
                true
            }
        }
    }



    override fun onBackPressed() {
        finish()
    }
}



