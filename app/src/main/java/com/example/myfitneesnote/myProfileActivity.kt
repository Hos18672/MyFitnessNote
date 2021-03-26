package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_my_profile.*

class myProfileActivity : BaseActivity() {

    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        fullscreen()
        setupActionBar()


        mFirebaseInstance = FirebaseDatabase.getInstance()

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")

        val user = FirebaseAuth.getInstance().getCurrentUser()

        // add it only if it is not saved to database
        userId = user?.getUid()

        btnSave.setOnClickListener {
            onUpdateClicked()
        }
        btnChangeEmailOrPassword.setOnClickListener {
            startActivity(Intent(this, SecurityActivity::class.java))
        }
    }


    private fun updateUser(username: String, name: String) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(name)) {
            mFirebaseDatabase!!.child(userId!!).child("username").setValue(username)
            mFirebaseDatabase!!.child(userId!!).child("name").setValue(name)
            Toast.makeText(applicationContext, "Successfully updated user", Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(applicationContext, "Unable to update user", Toast.LENGTH_SHORT).show()

    }

    fun onUpdateClicked() {
        val username = login_username_input.text.toString()
        val name = profile_fullName_input.getText().toString()

        //Calling updateUser function
        updateUser(username, name)
    }

/*
    fun onDeleteClicked(view: View) {
        //Remove value from child
        mFirebaseDatabase!!.child(userId!!).removeValue()
        Toast.makeText(applicationContext, "Successfully deleted user", Toast.LENGTH_SHORT).show()

        // clear information
        txt_user.setText("")
        email.setText("")
        name.setText("")
    }


    companion object {
        private val TAG = upd_del::class.java.getSimpleName()
    }
    */


    private fun setupActionBar() {
        setSupportActionBar(toolBar_my_profile_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_my_profile_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }

}
