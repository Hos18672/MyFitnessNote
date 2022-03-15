package com.example.myfitneesnote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_muskel_workouts.*

class MuskelWorkoutsActivity : BaseActivity() {
    private var gymType: String? = ""
    private var muskleName: String? = ""
    private lateinit var listWorkouts: ArrayList<String>
    private var listChestWorkoutsImage: ArrayList<Any> = ArrayList()
    private var listBicepsWorkoutsImage: ArrayList<Any> = ArrayList()
    private var listTricepsWorkoutsImage: ArrayList<Any> = ArrayList()
    private var listForearmsWorkoutsImage: ArrayList<Any> = ArrayList()
    private var listBackWorkoutsImage: ArrayList<Any> = ArrayList()
    private var listShoulderWorkoutsImage: ArrayList<Any> = ArrayList()
    private var listLegWorkoutsImage: ArrayList<Any> = ArrayList()
    private var listAbsWorkoutsImage: ArrayList<Any> = ArrayList()

    private var listOfAllWorkouts: HashMap<String,ArrayList<Any>> = HashMap()

    private var list: ArrayList<Any> = ArrayList()

    private lateinit var listView :ListView
    var adapter: MyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muskel_workouts)
        setupActionBar()


        gymType = intent.getStringExtra("GymName").toString()
        muskleName = intent.getStringExtra("MuskelName").toString()
        toolBar_textView.text = muskleName
        listWorkouts = intent.getStringArrayListExtra("listOfWorkouts") as ArrayList<String>
        listView = findViewById<ListView>(R.id.recipe_list_view)
        setLists()

        setAdapter()
    }

    private fun setLists(){
        // list of Chest Workouts
        listChestWorkoutsImage.add(R.drawable.bench_press_barbell)
        listChestWorkoutsImage.add(R.drawable.schraegbankdruecken)
        listChestWorkoutsImage.add(R.drawable.parallels_stangenbeugen)
        listChestWorkoutsImage.add(R.drawable.kabelbruecken_fliegen)
        listChestWorkoutsImage.add(R.drawable.hantelfliegen)
        listChestWorkoutsImage.add(R.drawable.liegenstuetzen)

        // list of Biceps Workouts
        listBicepsWorkoutsImage.add(R.drawable.hantelausfallschritte)
        listBicepsWorkoutsImage.add(R.drawable.langhantel_bacnkdruecken)
        listBicepsWorkoutsImage.add(R.drawable.hammer_exercise)
        listBicepsWorkoutsImage.add(R.drawable.bizeps_kurzhantel)
        listBicepsWorkoutsImage.add(R.drawable.kabelbiezepsuebung)


        // list of Triceps Workouts
        listTricepsWorkoutsImage.add(R.drawable.bankdruecken_mit_enger_griff)
        listTricepsWorkoutsImage.add(R.drawable.sitzendes_kurzhanteldruecken)
        listTricepsWorkoutsImage.add(R.drawable.trizepsverlaengerungen)
        listTricepsWorkoutsImage.add(R.drawable.einarmige_tirzepshantelverlaengerungen)
        listTricepsWorkoutsImage.add(R.drawable.plank_up_down)

        // list of Forearms Workouts
        listForearmsWorkoutsImage.add(R.drawable.handgelenkuebungen)
        listForearmsWorkoutsImage.add(R.drawable.handgelenkuebungen_2)
        listForearmsWorkoutsImage.add(R.drawable.handgelenkuebung_im_stehen)
        listForearmsWorkoutsImage.add(R.drawable.kurzhantel_handgelenkdrehung)


        // list of Back Workouts
        listBackWorkoutsImage.add(R.drawable.barbell_pronation_bent_over)
        listBackWorkoutsImage.add(R.drawable.latziehen)
        listBackWorkoutsImage.add(R.drawable.senkrechtes_rudern)
        listBackWorkoutsImage.add(R.drawable.kurzhantel_handgelenkdrehung)

        // list of Shoulder Workouts
        listShoulderWorkoutsImage.add(R.drawable.rueckendruecken)
        listShoulderWorkoutsImage.add(R.drawable.sitzendes_kurzhanteldruecken)
        listShoulderWorkoutsImage.add(R.drawable.seitenheben)
        listShoulderWorkoutsImage.add(R.drawable.frontheben)
        listShoulderWorkoutsImage.add(R.drawable.barbel_front_lift)
        listShoulderWorkoutsImage.add(R.drawable.militaerpresse_hinter_dem_nacken)

        // list of Abs Workouts
        listAbsWorkoutsImage.add(R.drawable.dumbell_side_tilt)
        listAbsWorkoutsImage.add(R.drawable.flachebank_liegendes_beinheben)
        listAbsWorkoutsImage.add(R.drawable.seitenbruecke)
        listAbsWorkoutsImage.add(R.drawable.superman)
        listAbsWorkoutsImage.add(R.drawable.beinheben)
        listAbsWorkoutsImage.add(R.drawable.drehendes_hueftheben)

        // list of Leg Workouts
        listLegWorkoutsImage.add(R.drawable.kniebeugen)
        listLegWorkoutsImage.add(R.drawable.dumbbel_drop_steps)
        listLegWorkoutsImage.add(R.drawable.squat_dumbbell)
        listLegWorkoutsImage.add(R.drawable.hackkniebeugen)
        listLegWorkoutsImage.add(R.drawable.barbel_step)
        listLegWorkoutsImage.add(R.drawable.good_morning)

        listOfAllWorkouts["Chest"] = (listChestWorkoutsImage)
        listOfAllWorkouts["Biceps"] = (listBicepsWorkoutsImage)
        listOfAllWorkouts["Triceps"] = (listTricepsWorkoutsImage)
        listOfAllWorkouts["Forearms"] = (listForearmsWorkoutsImage)
        listOfAllWorkouts["Back"] = (listBackWorkoutsImage)
        listOfAllWorkouts["Shoulder"] = (listShoulderWorkoutsImage)
        listOfAllWorkouts["Leg"] = (listLegWorkoutsImage)
        listOfAllWorkouts["Abs"] = (listAbsWorkoutsImage)

    }


    private fun setAdapter() {

        for (i in listOfAllWorkouts.keys) {
            if (i == "Chest" && muskleName == "CHEST") {
                list = listChestWorkoutsImage
            }
            if (i == "Biceps" && muskleName == "BICEPS") {
                list = listBicepsWorkoutsImage
            }
            if (i == "Triceps" && muskleName == "TRICEPS") {
                list = listTricepsWorkoutsImage
            }
            if (i == "Forearms" && muskleName == "FOREARMS") {
                list = listForearmsWorkoutsImage
            }
            if (i == "Back" && muskleName == "BACK") {
                list = listBackWorkoutsImage
            }
            if (i == "Shoulder" && muskleName == "SHOULDER") {
                list = listShoulderWorkoutsImage
            }
            if (i == "Abs" && muskleName == "ABS") {
                list = listAbsWorkoutsImage
            }
            if (i == "Leg" && muskleName == "LEG") {
                list = listLegWorkoutsImage
            }
        }

            adapter = MyAdapter(this, listWorkouts, list)
            listView.adapter = adapter
            listView.setOnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, AddWorkoutActivity::class.java)
                intent.putExtra("GymName", gymType)
                intent.putExtra("MuskelName", muskleName)
                intent.putExtra("WorkoutName", adapter!!.getItemString(position))
                startActivity(intent)
            }
    }


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
class MyAdapter(private val context: Context, private val arrayList: java.util.ArrayList<String>,private val list: ArrayList<Any>) : BaseAdapter() {
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
        var imageView : ImageView = convertView.findViewById(R.id.imageView)
        workoutName.text = arrayList[position]
        imageView.setImageResource(list[position] as Int)
        return convertView
    }
}