package com.example.myfitneesnote.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.myfitneesnote.R
import com.example.myfitneesnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.user_row.view.*

class UsersActivity : BaseActivity() {
    companion object{
        var currentUser: User?= null
        const val USER_KEY = "USER_KEY"
    }
    var senderId = ""
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        setupActionBar()
        updateUser("0")
        senderId = intent.getStringExtra("user_from_id").toString()
        fetchUsers()
        fetchCurrentUser()
        window.enterTransition = null
        window.exitTransition = null
        btnBack_ChatList.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
    private fun updateUser(inChat :String) {
        val uid = FirebaseAuth.getInstance().uid
        var mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("/users/$uid")
        mFirebaseDatabase!!.child("inChat").setValue(inChat)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupActionBar() {
        setSupportActionBar(toolBar_Chat_activity)
        val actionBar = supportActionBar
        if(actionBar!=null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_Chat_activity.setNavigationOnClickListener{
            onBackPressed()
            finish()
        }
    }
    private  fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private  fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach{
                    Log.d("new massage", it.toString())
                    val user = it.getValue(User::class.java)
                    if(user != null) {
                        var text = user.lastMessage
                        if (user.user_id != FirebaseAuth.getInstance().uid) {
                            adapter.add(UserItemViewHolder(user,text,senderId))
                        }
                    }
                }
                adapter.setOnItemClickListener{ item, view ->
                    val userItem= item as UserItemViewHolder
                    val intent = Intent( view.context, ChatActivity:: class.java)
                    intent.putExtra("name",userItem.user.name)
                    // intent.putExtra(USER_KEY, userItem.user.username)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)

                }
                adapter.notifyDataSetChanged()
                recyclerView_add.adapter = adapter

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBackPressed() {
        super.onBackPressed()
        updateUser("0")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
        window.exitTransition = null;
    }
}
class UserItemViewHolder(val user: User, val message : String, var sender_id: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.User_name.text= user.username
        if  (message !="" && user.user_id == sender_id){
                viewHolder.itemView.received_message_icon.visibility = View.VISIBLE
                viewHolder.itemView.last_message.text = message
        }
        if (user.image.isNotEmpty()){
            Picasso.get().load(user.image).into(viewHolder.itemView.UserImage)
        }
        else{
            System.out.println("empty")
        }
    }
    override fun getLayout(): Int {
        return R.layout.user_row
    }
}
