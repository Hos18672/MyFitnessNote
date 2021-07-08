package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.R.drawable.*
import com.example.myfitneesnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.chat_to_row.view.*


class myProfileActivity : BaseActivity() {

    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_my_profile)
        fullscreen()
        setupActionBar()
        userProfileData()
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
    private fun setupActionBar() {
        setSupportActionBar(toolBar_my_profile_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(ic_navigate_before_black_24dp)
        }
        toolBar_my_profile_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }


    private fun userProfileData() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java)
                val fullname = snapshot.child("name").getValue(String::class.java)
                login_username_input.setText(username)
                profile_fullName_input.setText(fullname)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}

/*
class profileImage(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        // load our user image into the picture
        val uri = user.image
        val targetImageView = viewHolder.itemView.imageView_to_row
        Picasso.get().load(uri).into(targetImageView)
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}*/
