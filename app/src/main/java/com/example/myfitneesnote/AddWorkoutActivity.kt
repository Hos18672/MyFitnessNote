package com.example.myfitneesnote


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.adapters.TrainingItemAdapteradd
import com.example.myfitneesnote.firebase.FirestoreClass
import com.example.myfitneesnote.model.Workout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.vivekkaushik.datepicker.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_add_workout.*
import java.util.*


class AddWorkoutActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener  {
    var uuid : UUID= UUID.randomUUID()
    private lateinit var currentDate :String
    private val db = FirebaseFirestore.getInstance()
    private lateinit var trainingItemAdapter : TrainingItemAdapteradd
    private lateinit var recyclerView: RecyclerView
    private var trainingsName: String? = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_add_workout)
        onClick()
       // animat()
        setupActionBar()

  /*      recyclerView = findViewById(id.rv_trainings_list_Add_Training)
        getTrainingsFromFireStore() */
       // trainingItemAdapter.notifyDataSetChanged()
        trainingsName= intent.getStringExtra("MuskelName")
        TrainingName.text = trainingsName
        //Set a Start date (Default, 1 Jan 1970)
        val c= Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        datePickerTimeline.setInitialDate(year,month,day)
        currentDate = "${year}/${month+1}/${day}"
        // datePickerTimeline.setActiveDate(Calendar.getInstance())
        // Set a date Selected Listener
        val trainingsFragment = trainings_fragment()

        supportFragmentManager.beginTransaction().apply {
            replace(id.root_container, trainingsFragment).commit()
        }


        datePickerTimeline.setOnDateSelectedListener(object : OnDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
                // Do Something
                 currentDate = "${year}/${month+1}/${day}"
            }
            override fun onDisabledDateSelected(
                year: Int,
                month: Int,
                day: Int,
                dayOfWeek: Int,
                isDisabled: Boolean
            ) {
               // datePickerTimeline.setInitialDate(Calendar.YEAR, Calendar.MONTH,Calendar.DATE)
              //  currentDate = "${year}/${month+1}/${day}"
            }
        })
        // Disable date
        datePickerTimeline.setDisabledDateColor(Color.BLUE)
        val dates = arrayOf(Calendar.getInstance().time)
        datePickerTimeline.deactivateDates(dates)
    }



    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun onClick(){
        btnBackNewWorkout.setOnClickListener {
            val intent = Intent( this,  MuskelGroupActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btnBackNewWorkout, "addTrainingBtn")
            startActivity(intent, options.toBundle())

        }
        btn_set_minus.setOnClickListener{minusButton(SetNum)}
        btn_set_plus.setOnClickListener{plusButton(SetNum)}
        btn_weight_minus.setOnClickListener{minusButton(weightNum)}
        btn_weight_plus.setOnClickListener{plusButton(weightNum)}
        btn_break_minus.setOnClickListener{minusButton(breakNum)}
        btn_break_plus.setOnClickListener{plusButton(breakNum)}
        btn_repeat_minus.setOnClickListener{minusButton(repeatNum)}
        btn_repeat_plus.setOnClickListener{plusButton(repeatNum)}
        val trainingsFragment =   trainings_fragment()


        save_btn.setOnClickListener {
            //this might be helpful
            // layoutManager.reverseLayout = true
            /*         val gymType    : String = intent.getStringExtra("GymName").toString()
                     val muskelName : String = intent.getStringExtra("MuskelName").toString()
                     val intent = Intent(this, AddWorkoutActivity::class.java)
                     intent.putExtra("MuskelName", muskelName)
                     intent.putExtra("GymName", gymType)
                     startActivity(intent)
                     overridePendingTransition(0, 0)
                     finish()  }*/
            createTraining()
            supportFragmentManager.beginTransaction().apply {
                replace(id.root_container, trainingsFragment).commit()

            }



        }

    }


    private fun refreshFragment(context : Context){
        context?.let {
            val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
            fragmentManager?.let {
                val currentFragment = fragmentManager.findFragmentById(id.root_container)
                currentFragment?.let {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.detach(it)
                    fragmentTransaction.attach(it)
                    fragmentTransaction.commit()
                }
            }
        }
    }

    private fun minusButton(et: EditText){
        var num = et.text.toString().toInt()
        if(num  > 0 ) {
            num--
            et.text = "$num".toEditable()
        }
    }
    private fun plusButton(et: EditText){
        var num = et.text.toString().toInt()
        num++
        et.text = "$num".toEditable()
    }
    private fun createTraining(){
        val userWeight = 70
        val currentDateAndTime = Timestamp.now()
        val workout : Workout
        val gymType    : String = intent.getStringExtra("GymName").toString()
        val muskelName : String = intent.getStringExtra("MuskelName").toString()
        var set = SetNum.text.toString()
        var rep = repeatNum.text.toString()
        val Calorie = set.toInt()*rep.toInt()*(2*3.5*userWeight)/200
        workout= Workout(
            uuid.toString(),
            gymType,
            muskelName,
            set,
            weightNum.text.toString(),
            breakNum.text.toString(),
            rep,
            currentDate,
            Calorie,
            currentDateAndTime)
        FirestoreClass().createNewTraining(this, workout)

    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_add_workout_activity)
        val actionBar = supportActionBar
        if(actionBar!=null)
        {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(drawable.ic_navigate_before_black_24dp)
        }
        toolBar_add_workout_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }

/*    private fun getTrainingsFromFireStore(){
        val query : Query = db.collection(Constant.USERS)
            .document(getCurrentUserID())
            .collection(Constant.TRAININGS)
            .orderBy("date", Query.Direction.DESCENDING)
        val fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
            .setQuery(query, Workout::class.java)
            .build()
        trainingItemAdapter = TrainingItemAdapteradd(fireStoreRecyclerOption)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter= trainingItemAdapter
        val item = object : SwipeToDelete(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                trainingItemAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper= ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        trainingItemAdapter.notifyDataSetChanged()
        recyclerView.startLayoutAnimation()
    }
    override fun onStart() {
        super.onStart()
        trainingItemAdapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        trainingItemAdapter.stopListening()
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}


