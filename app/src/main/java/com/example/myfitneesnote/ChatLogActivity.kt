package com.example.myfitneesnote

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.myfitneesnote.model.ChatMessage
import com.example.myfitneesnote.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.toolBar_Chat_activity
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.util.*


class ChatLogActivity : BaseActivity() {
    companion object{ const val TAG = "ChatLog" }
    var toUser: User?= null
    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        toUser =  intent.getParcelableExtra(ChatActivity.USER_KEY)
        recyclerView_chat_log.adapter= adapter
        setupActionBar()
        listenForMessages()
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        SendeBtn.setOnClickListener{
            SendeBtn.animate().apply {
                duration = 100
                scaleYBy(.3f)
                scaleXBy(.3f)
            }.withEndAction {
                SendeBtn.animate().apply {
                    duration = 100
                    scaleYBy(-.3f)
                    scaleXBy(-.3f)
                }
            }.start()
            Log.d(TAG, "To send Message")
            if(editTextChatLog.text.toString() != ""){
                performSendMessage()
            }
        }
    }
    private  fun listenForMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.user_id
        val ref   = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
         ref.addChildEventListener(object : ChildEventListener {
             override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                 val chatMessage = p0.getValue(ChatMessage::class.java)
                 if (chatMessage != null) {
                     Log.d(TAG, chatMessage.text)
                     if (chatMessage.formId == FirebaseAuth.getInstance().uid) {
                         val currentUser = ChatActivity.currentUser ?: return
                         adapter.add(ChatFromItem(chatMessage.text, currentUser, chatMessage.timestamp))
                         recyclerView_chat_log.scrollToPosition(adapter.itemCount -1)
                     } else {
                         adapter.add(ChatToItem(chatMessage.text, toUser!!,chatMessage.timestamp,intent.getStringExtra("name").toString()))
                         recyclerView_chat_log.scrollToPosition(adapter.itemCount -1)
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
        val c= Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val time = "${hour}:${minute}"
      //How do we actually send a message to firebase
        val text =editTextChatLog.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        // get ID of the Reciever
        val toId = toUser?.user_id
       // val refrence = FirebaseDatabase.getInstance().getReference("/messages").push()
        val refrence = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toRefrence = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage= ChatMessage(
            refrence.key!!,
            text,
            fromId!!,
            toId!!,
            time
        )
        refrence.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG, "Saved our Chat message : ${refrence.key}")
            editTextChatLog.setText("")
            recyclerView_chat_log.scrollToPosition(adapter.itemCount -1)
        }
        toRefrence.setValue(chatMessage)
    }
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
        }
    }
}
// --------------- Outer Class -------------------
class ChatFromItem(val text: String, val user: User, val time: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = text
        viewHolder.itemView.textview_to_row_date_from.text = time
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChatToItem(val text: String, val user: User, val time: String, val name: String): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
        viewHolder.itemView.textview_to_row_date_to.text= time
        viewHolder.itemView.textview_to_row__name.text = name
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

}


