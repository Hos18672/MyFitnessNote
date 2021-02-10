package com.example.myfitneesnote

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.model.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        nav_view.setNavigationItemSelectedListener(this)
        FirestoreClass().loginUser(this)
        fullscreen()
        onClick()
    }
    fun onClick(){
        var main_menu : ImageView = findViewById(id.main_menu)
        var add_main : ImageView   = findViewById(id.Add_main)
        var chat_main : ImageView   = findViewById(id.chat_main)
        var diagram_main : ImageView   = findViewById(id.main_diagramm)

        main_menu.setOnClickListener {
            if(drawer_layout.isDrawerOpen(GravityCompat.START)){
                drawer_layout.closeDrawer(GravityCompat.START)
            }else{
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        add_main.setOnClickListener {
            startActivity(Intent(this, workout::class.java))
        }
        chat_main.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
        diagram_main.setOnClickListener {
            startActivity(Intent(this, myProfileActivity::class.java))
        }
    }
    companion object {
        //A unique code for starting the activity for result
        const val MY_PROFILE_REQUEST_CODE: Int = 11
        const val CREATE_BOARD_REQUEST_CODE: Int = 12
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            id.nav_my_profile ->{
                startActivity(Intent(this, myProfileActivity::class.java))
            }
            id.nav_sign_out ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }
fun updateNavigationUserDetails(user: User) {
        Glide.with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(drawable.ic_user_place_holder)
            .into(main_drawer_profile_photo)

        tv_username.text = user.username
    }
}

