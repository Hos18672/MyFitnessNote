package com.example.myfitneesnote.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.myfitneesnote.R
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
        setAdapter()
    }

    private fun setListHome(){
        // list of Chest Workouts
        listChestWorkoutsImage.addAll(arrayOf(R.drawable.decline_push_ups,R.drawable.incline_push_ups,R.drawable.plyometric_pushups,R.drawable.slightly_easier_push_ups,R.drawable.walking_plank))
        // list of Biceps Workouts
        listBicepsWorkoutsImage.addAll(arrayOf(R.drawable.chin_ups,R.drawable.plank_taps,R.drawable.pull_ups,R.drawable.reverse_hand_push_ups))
        // list of Triceps Workouts
        listTricepsWorkoutsImage.addAll(arrayOf(R.drawable.diamond_pushups,R.drawable.power_triceps_extension,R.drawable.triceps_bow,R.drawable.triceps_dips))
        // list of Back Workouts
        listBackWorkoutsImage.addAll(arrayOf(R.drawable.bridge,R.drawable.high_blank,R.drawable.low_blank,R.drawable.quadruped_limb_raises,R.drawable.superman_back_extension))
        // list of Shoulder Workouts
        listShoulderWorkoutsImage.addAll(arrayOf(R.drawable.band_pull_aparts,R.drawable.decline_push_ups,R.drawable.front_schoulder_raise_with_band,R.drawable.handstand_push_ups,R.drawable.plank_raise_tap_crunch))
        // list of Abs Workouts
        listLegWorkoutsImage.addAll(arrayOf(R.drawable.lunges,R.drawable.pistol_squats,R.drawable.squats,R.drawable.squats_jumps))
        // list of Leg Workouts
        listAbsWorkoutsImage.addAll(arrayOf(R.drawable.flachebank_liegendes_beinheben,R.drawable.seitenbruecke,R.drawable.superman,R.drawable.beinheben,R.drawable.drehendes_hueftheben))

        listOfAllWorkouts["Chest"] = listChestWorkoutsImage
        listOfAllWorkouts["Biceps"] = listBicepsWorkoutsImage
        listOfAllWorkouts["Triceps"] = listTricepsWorkoutsImage
        listOfAllWorkouts["Back"] = listBackWorkoutsImage
        listOfAllWorkouts["Shoulder"] = listShoulderWorkoutsImage
        listOfAllWorkouts["Leg"] = listLegWorkoutsImage
        listOfAllWorkouts["Abs"] = listAbsWorkoutsImage
    }


    private fun setListGym(){
        // list of Chest Workouts
        listChestWorkoutsImage.addAll(arrayOf(R.drawable.bench_press_barbell,R.drawable.schraegbankdruecken,R.drawable.parallels_stangenbeugen,R.drawable.kabelbruecken_fliegen,R.drawable.hantelfliegen,R.drawable.liegenstuetzen))
        listChestWorkoutsImage.addAll(arrayOf(R.drawable.decline_push_ups,R.drawable.incline_push_ups,R.drawable.plyometric_pushups,R.drawable.slightly_easier_push_ups,R.drawable.walking_plank))
        // list of Biceps Workouts
        listBicepsWorkoutsImage.addAll(arrayOf(R.drawable.hantelausfallschritte,R.drawable.langhantel_bacnkdruecken,R.drawable.hammer_exercise,R.drawable.bizeps_kurzhantel,R.drawable.kabelbiezepsuebung))
        listBicepsWorkoutsImage.addAll(arrayOf(R.drawable.chin_ups,R.drawable.plank_taps,R.drawable.pull_ups,R.drawable.reverse_hand_push_ups))
        // list of Triceps Workouts
        listTricepsWorkoutsImage.addAll(arrayOf(R.drawable.bankdruecken_mit_enger_griff,R.drawable.sitzendes_kurzhanteldruecken,R.drawable.trizepsverlaengerungen,R.drawable.einarmige_tirzepshantelverlaengerungen,R.drawable.plank_up_down))
        listTricepsWorkoutsImage.addAll(arrayOf(R.drawable.diamond_pushups,R.drawable.power_triceps_extension,R.drawable.triceps_bow,R.drawable.triceps_dips))
        // list of Forearms Workouts
        listForearmsWorkoutsImage.addAll(arrayOf(R.drawable.handgelenkuebungen,R.drawable.handgelenkuebungen_2,R.drawable.handgelenkuebung_im_stehen,R.drawable.kurzhantel_handgelenkdrehung))
        // list of Back Workouts
        listBackWorkoutsImage.addAll(arrayOf(R.drawable.barbell_pronation_bent_over,R.drawable.latziehen,R.drawable.senkrechtes_rudern,R.drawable.kurzhantel_handgelenkdrehung))
        listBackWorkoutsImage.addAll(arrayOf(R.drawable.bridge,R.drawable.high_blank,R.drawable.low_blank,R.drawable.quadruped_limb_raises,R.drawable.superman_back_extension))
        // list of Shoulder Workouts
        listShoulderWorkoutsImage.addAll(arrayOf(R.drawable.rueckendruecken,R.drawable.sitzendes_kurzhanteldruecken,R.drawable.seitenheben,R.drawable.frontheben,R.drawable.barbel_front_lift,R.drawable.militaerpresse_hinter_dem_nacken))
        listShoulderWorkoutsImage.addAll(arrayOf(R.drawable.band_pull_aparts,R.drawable.decline_push_ups,R.drawable.front_schoulder_raise_with_band,R.drawable.handstand_push_ups,R.drawable.plank_raise_tap_crunch))
        // list of Abs Workouts
        listAbsWorkoutsImage.addAll(arrayOf(R.drawable.dumbell_side_tilt,R.drawable.flachebank_liegendes_beinheben,R.drawable.seitenbruecke,R.drawable.superman,R.drawable.beinheben,R.drawable.drehendes_hueftheben))
        // list of Leg Workouts
        listLegWorkoutsImage.addAll(arrayOf(R.drawable.kniebeugen,R.drawable.dumbbel_drop_steps,R.drawable.squat_dumbbell,R.drawable.hackkniebeugen,R.drawable.barbel_step,R.drawable.good_morning))
        listLegWorkoutsImage.addAll(arrayOf(R.drawable.lunges,R.drawable.pistol_squats,R.drawable.squats_jumps))

        listOfAllWorkouts["Chest"] = listChestWorkoutsImage
        listOfAllWorkouts["Biceps"] = listBicepsWorkoutsImage
        listOfAllWorkouts["Triceps"] = listTricepsWorkoutsImage
        listOfAllWorkouts["Forearms"] = listForearmsWorkoutsImage
        listOfAllWorkouts["Back"] = listBackWorkoutsImage
        listOfAllWorkouts["Shoulder"] = listShoulderWorkoutsImage
        listOfAllWorkouts["Leg"] = listLegWorkoutsImage
        listOfAllWorkouts["Abs"] = listAbsWorkoutsImage
    }

    private fun setAdapter() {
        if (gymType == "HOME"){
            setListHome()
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

        }else{
            setListGym()
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
        val convertView: View?
        convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false)
        workoutName = convertView.findViewById(R.id.workoutName)
        val imageView : ImageView = convertView.findViewById(R.id.imageView)
        workoutName.text = arrayList[position]
        imageView.setImageResource(list[position] as Int)
        return convertView
    }
}