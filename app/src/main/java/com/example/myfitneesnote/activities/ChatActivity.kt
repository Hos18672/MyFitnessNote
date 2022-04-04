package com.example.myfitneesnote.activities

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.myfitneesnote.R
import com.example.myfitneesnote.RetrofitInstance
import com.example.myfitneesnote.firebase.FirebaseService
import com.example.myfitneesnote.model.ChatMessage
import com.example.myfitneesnote.model.NotificationData
import com.example.myfitneesnote.model.PushNotification
import com.example.myfitneesnote.model.User
import com.example.myfitneesnote.utils.showCustomToast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_users.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class ChatActivity : BaseActivity() {
    companion object{ const val TAG = "ChatLog" }
    var toUser: User?= null
    val adapter = GroupAdapter<ViewHolder>()
    var TOPIC = "/topics/myTopic"
    var username = ""
    var active = false;
    var isInChat = ""
    lateinit var sharedpref : SharedPreferences
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_users)
        window.enterTransition = null;
        window.exitTransition = null;
        userData()
        active = true
        sharedpref = getSharedPreferences("chat", MODE_PRIVATE)
        sharedpref.edit().putBoolean("active",active).apply()
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        updateUser("1")
        toUser =  intent.getParcelableExtra(UsersActivity.USER_KEY)
        updateUser("1")
        deleteUserMessage(toUser!!.user_id)
        getFirebaseMessagingToken()
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        recyclerView_chat_users.adapter= adapter
        setupActionBar()
        listenForMessages()
        var noti = NotificationManager.EXTRA_NOTIFICATION_CHANNEL_ID
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            notificationManager.areNotificationsPaused()
        }

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        SendeBtn.setOnClickListener{
            Log.d(TAG, "To send Message")
            if(editTextChatLog.text.toString() != ""){
                performSendMessage()
            }
        }
        userProfileData2()
    }
    fun getFirebaseMessagingToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    //Could not get FirebaseMessagingToken
                    return@addOnCompleteListener
                }
                if (null != task.result) {
                    //Got FirebaseMessagingToken
                    val firebaseMessagingToken = Objects.requireNonNull(task.result)
                    FirebaseService.token = firebaseMessagingToken
                    //Use firebaseMessagingToken further
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
                    chatMessage.text.let { Log.d(TAG, it) }
                    if (chatMessage.formId == FirebaseAuth.getInstance().uid) {
                        val currentUser = UsersActivity.currentUser ?: return
                        ChatFromItem(
                            chatMessage.text, currentUser,
                            chatMessage.timestamp
                        ).let {
                            adapter.add(it)
                        }
                        recyclerView_chat_users.scrollToPosition(adapter.itemCount -1)
                    } else {
                        ChatToItem(
                            chatMessage.text, toUser!!,
                            chatMessage.timestamp,intent.getStringExtra("name").toString())
                            .let { adapter.add(it) }
                        recyclerView_chat_users.scrollToPosition(adapter.itemCount -1)
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
        val refrence = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toRefrence = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage= ChatMessage(refrence.key!!, text, fromId!!, toId!!, time)
        refrence.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG, "Saved our Chat message : ${refrence.key}")
            editTextChatLog.setText("")
            recyclerView_chat_users.scrollToPosition(adapter.itemCount -1)
        }
        updateUserMessage(chatMessage)
        userProfileData2()
        toRefrence.setValue(chatMessage)
        if( userProfileData2() == "0"){
            FirebaseService.token?.let {
                PushNotification(
                    NotificationData(username,chatMessage.text,fromId),
                    toUser!!.token
                ).also {
                    sendNotification(it)
                    userProfileData2()
                }
            }
        }
        userProfileData2()
    }
    private fun updateUserMessage(text: ChatMessage) {
         val mFirebaseDatabase: DatabaseReference? = FirebaseDatabase.getInstance()!!.getReference("/users/${text.toId}")
        val fs  : FirebaseFirestore = FirebaseFirestore.getInstance()
        if (!TextUtils.isEmpty(text.text)) {
           mFirebaseDatabase!!.child("lastMessage").setValue(text.text)
            fs.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).update(
                "lastMessage",text.text)
                }
        val mFirebaseDatabase2: DatabaseReference? = FirebaseDatabase.getInstance()!!.getReference("/users/${FirebaseAuth.getInstance()!!.currentUser!!.uid}")
        val fs2  : FirebaseFirestore = FirebaseFirestore.getInstance()
        if (!TextUtils.isEmpty(text.text)) {
            mFirebaseDatabase2!!.child("lastMessage").setValue(text.text)
            fs2.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).update(
                "lastMessage",text.text)
        }
    }
    private fun deleteUserMessage(text: String) {
        val mFirebaseDatabase: DatabaseReference? = FirebaseDatabase.getInstance()!!.getReference("/users/${text}")
        val fs  : FirebaseFirestore = FirebaseFirestore.getInstance()
        if (!TextUtils.isEmpty(text)) {
            mFirebaseDatabase!!.child("lastMessage").setValue("")
            fs.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).update(
                "lastMessage","")
        }
    }

    private fun userProfileData2():String {
        val toId = toUser?.user_id
        val ref   = FirebaseDatabase.getInstance().getReference("/users/$toId")
        ref.child("inChat").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                isInChat = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("Hey", "Failed to read app title value.", error.toException())
            }
        })
        return isInChat
    }
    private fun userData() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                username = snapshot.child("username").getValue(String::class.java).toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Toast(this@ChatActivity).showCustomToast("Notification is not sended", this@ChatActivity)
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())


        }
    }
    private fun updateUser(inChat :String) {
        val uid = FirebaseAuth.getInstance().uid
        val mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val fs  : FirebaseFirestore = FirebaseFirestore.getInstance()
        mFirebaseDatabase!!.child("inChat").setValue(inChat)
    }
    override fun onStart() {
        super.onStart()
        updateUser("1")
    }
    override fun onResume() {
        super.onResume()
        updateUser("1")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBackPressed() {
        super.onBackPressed()
        updateUser("0")
        val intent = Intent(this, UsersActivity::class.java)
        startActivity(intent)
        finishAffinity()
        window.enterTransition = null;
        window.exitTransition = null;
        deleteUserMessage(toUser!!.user_id)
    }

    override fun onDestroy() {
        super.onDestroy()
        updateUser("0")
    }

    override fun onStop() {
        super.onStop()
        updateUser("0")
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
            window.enterTransition = null;
            window.exitTransition = null;
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
class ChatToItem(val text: String, val user: User, private val time: String, val name: String): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
        viewHolder.itemView.textview_to_row_date_to.text= time
        viewHolder.itemView.textview_to_row__name.text = name
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

}