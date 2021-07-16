package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.myfitneesnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.user_row.view.*

class ChatActivity : BaseActivity() {

    companion object{
        var currentUser: User?= null
        val USER_KEY = "USER_KEY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        fullscreen()
        setupActionBar()
        fetchUsers()
        fetchCurrentUser()
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_Chat_activity)
        var actionBar = supportActionBar
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_Chat_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    private  fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d("LatesMessages", "Current User ${currentUser?.image}")
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
                        if (user.user_id!= FirebaseAuth.getInstance().uid) {
                            adapter.add(UserItem(user))
                        }
                    }
                }
                adapter.setOnItemClickListener{ item, view ->
                    val userItem= item as UserItem
                    val intent = Intent( view.context, ChatLogActivity:: class.java)
                    // intent.putExtra(USER_KEY, userItem.user.username)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
class UserItem(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val uri = user.image
        viewHolder.itemView.User_name.text= user.username
    }
    override fun getLayout(): Int {
        return R.layout.user_row
    }
}
