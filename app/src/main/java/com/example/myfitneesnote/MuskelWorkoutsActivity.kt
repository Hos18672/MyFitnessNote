package com.example.myfitneesnote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_muskel_workouts.*
import kotlinx.android.synthetic.main.activity_tips.view.*


class MuskelWorkoutsActivity : BaseActivity() {
    private var gymType: String? = ""
    private var muskleName: String? = ""
    private lateinit var listWorkouts: ArrayList<String>
    private lateinit var listView :ListView
    var adapter: MyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muskel_workouts)
        setupActionBar()
        gymType = intent.getStringExtra("GymName").toString()
        muskleName = intent.getStringExtra("MuskelName").toString()
        listWorkouts = intent.getStringArrayListExtra("listOfWorkouts") as ArrayList<String>
        listView = findViewById<ListView>(R.id.recipe_list_view)

        adapter = MyAdapter(this, listWorkouts)
        listView.adapter = adapter


        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, AddWorkoutActivity::class.java)
            intent.putExtra("MuskelName", adapter!!.getItemString(position))
            startActivity(intent)
        }
    }

//     fun  onClick(view : View){
//         val intent = Intent( this,  AddWorkoutActivity::class.java)
//         intent.putExtra("WorkoutName", view.contentDescription)
//         intent.putExtra("MuskelName", muskleName)
//         intent.putExtra("GymName", gymType)
//
//         startActivity(intent)
//
//    }

    private fun setupActionBar() {
        setSupportActionBar(toolBar_muskelWorkouts)
        val actionBar = supportActionBar
        if (actionBar != null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_muskelWorkouts.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}


//Class MyAdapter
class MyAdapter(private val context: Context, private val arrayList: java.util.ArrayList<String>) : BaseAdapter() {
    private lateinit var workoutName: TextView

    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
     fun getItemString(position: Int): String {
        return arrayList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false)
        workoutName = convertView.findViewById(R.id.workoutName)
        workoutName.text = arrayList[position]

        return convertView
    }
}