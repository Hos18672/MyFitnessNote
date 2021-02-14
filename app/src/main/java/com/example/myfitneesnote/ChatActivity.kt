package com.example.myfitneesnote

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import com.example.myfitneesnote.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.user_row.*
import kotlinx.android.synthetic.main.user_row.view.*

class ChatActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        fullscreen()
        setupActionBar()
        fetchUsers()
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_Chat_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_Chat_activity.setNavigationOnClickListener{
            onBackPressed()
        }
}
    companion object{
        val USER_KEY = "USER_KEY"
    }
   fun fetchUsers(){
      val ref = FirebaseDatabase.getInstance().getReference("/users")
       ref.addListenerForSingleValueEvent(object : ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               val adapter = GroupAdapter<ViewHolder>()
               snapshot.children.forEach{
                   Log.d("new massage", it.toString())
                   val user = it.getValue(User::class.java)
                   if(user != null) {
                       if (user.username == "Trainer") {
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
       viewHolder.itemView.User_name.text= user.username
    }
    override fun getLayout(): Int {
        return R.layout.user_row
    }
}

