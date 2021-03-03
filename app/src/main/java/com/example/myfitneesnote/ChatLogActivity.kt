package com.example.myfitneesnote


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myfitneesnote.model.ChatMessage
import com.example.myfitneesnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.toolBar_Chat_activity
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*


class ChatLogActivity : AppCompatActivity() {
    companion object{ val TAG = "ChatLog" }
    var toUser: User?= null
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        toUser =  intent.getParcelableExtra<User>(ChatActivity.USER_KEY)
        recyclerView_chat_log.adapter= adapter
        setupActionBar()
        listenForMessages()

        SendeBtn.setOnClickListener{
            Log.d(TAG, "To send Message")
            performSendMessage()
        }
    }


    private  fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
         ref.addChildEventListener(object : ChildEventListener {
             override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                 val chatMessage = p0.getValue(ChatMessage::class.java)

                 if (chatMessage != null) {
                     Log.d(TAG, chatMessage!!.text)
                     if (chatMessage.formId == FirebaseAuth.getInstance().uid) {
                         val currentUser = ChatActivity.currentUser ?: return
                         adapter.add(ChatFromItem(chatMessage.text, currentUser!!))
                     } else {
                         adapter.add(ChatToItem(chatMessage.text, toUser!!))
                     }
                 }
             }

             override fun onCancelled(der: DatabaseError) {
                 TODO("Not yet implemented")
             }

             override fun onChildChanged(snap: DataSnapshot, previousChildName: String?) {
                 TODO("Not yet implemented")
             }

             override fun onChildMoved(snap: DataSnapshot, previousChildName: String?) {
                 TODO("Not yet implemented")
             }

             override fun onChildRemoved(snapshot: DataSnapshot) {
                 TODO("Not yet implemented")
             }

         })
    }


    private fun performSendMessage(){
      //How do we actually send a message to firebase
        val text =editTextChatLog.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        // get ID of the Reciever

        val toId = toUser?.user_id


        val refrence = FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage= ChatMessage(
            refrence.key!!,
            text,
            fromId!!,
            toId!!,
            System.currentTimeMillis() / 1000
        )
        refrence.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG, "Saved our Chat message : ${refrence.key}")
            editTextChatLog.setText("")

        }
        recyclerView_chat_log.scrollToPosition((recyclerView_chat_log.adapter?.itemCount ?: -1) -2)

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

}

// --------------- Outer Class -------------------
class ChatFromItem(val text: String, val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = text
        // load our user image into the picture
        val uri = user.image
        val targetImageView = viewHolder.itemView.imageView_from_row
        Picasso.get().load(uri).into(targetImageView)
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String, val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text

        // load our user image into the picture
        val uri = user.image
        val targetImageView = viewHolder.itemView.imageView_to_row
        Picasso.get().load(uri).into(targetImageView)
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}


