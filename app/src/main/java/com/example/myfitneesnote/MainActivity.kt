package com.example.myfitneesnote


import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R.id
import com.example.myfitneesnote.R.layout
import com.example.myfitneesnote.adapters.TrainingItemAdapterMain
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_add_workout.constraintLayout3
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_layout.*
import kotlinx.android.synthetic.main.activity_main_layout.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.firestore.QuerySnapshot
import kotlin.collections.HashMap
import java.time.LocalDate
import java.time.LocalDate.parse
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var trainingItemAdapterMain: TrainingItemAdapterMain
    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("ResourceAsColor", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        nav_view.setNavigationItemSelectedListener(this)
        recyclerView = findViewById(id.rv_trainings_list_main)
        constraintLayout3.bringToFront()
        onClick()
        userData()
        animat()
        getTrainingsFromFireStore()
        updateNavigationUserDetails()
        setupLineChartData(7)
        val trainingsFragment = workout_list_main_fragment()
        supportFragmentManager.beginTransaction().apply {
            replace(id.root_container_main, trainingsFragment).commit()
        }

        if (!checkForInternet(this)) {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()
        }
        if (!scrollView_main.canScrollVertically(1)) {
            constraintLayout3.elevation = 10f
        }
        if (!scrollView_main.canScrollVertically(-1)) {
            constraintLayout3.elevation = 5f
        }

        scrollView_main.viewTreeObserver
            .addOnScrollChangedListener {
                if (!scrollView_main.canScrollVertically(-1)) {
                    constraintLayout3.elevation = 0f
                }
                else{
                    constraintLayout3.elevation = 50f
                }
            }
    }

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    private fun userData() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        with(ref) {
            addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = snapshot.child("username").getValue(String::class.java)
                    tv_username.text = username
                }

                override fun onCancelled(error: DatabaseError): Unit = TODO("Not yet implemented")
            })
        }
    }

    @SuppressLint("NewApi")
    private fun onClick() {
        val mainMenu: ImageButton = findViewById(id.main_menu)
        val addMenu: ImageView = findViewById(id.add_main)
        val chatMain: ImageButton = findViewById(id.chat_main)
        val mainImage: ImageButton = findViewById(id.mainImage)
        val diagramMain: ImageButton = findViewById(id.main_diagramm)
        val tip1: CardView = findViewById(id.EZ_bar_curl)
        val tip2: CardView = findViewById(id.One_arm_dumbbell)
        val tip3: CardView = findViewById(id.Dumbbell)
        val tip4: CardView = findViewById(id.Seated_biceps_curls)


        tipsItemsClick(tip1)
        tipsItemsClick(tip2)
        tipsItemsClick(tip3)
        tipsItemsClick(tip4)

        mainImage.setOnClickListener {
            //animate(mainImage)
            val intent = Intent(this, MyProfileActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                mainImage,
                "profileImage"
            )
            startActivity(intent, options.toBundle())
        }

        mainMenu.setOnClickListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        addMenu.setOnClickListener {
            addMenu.animate().apply {
                duration = 500
                scaleYBy(.3f)
                scaleXBy(.3f)
            }.withEndAction {
                addMenu.animate().apply {
                    duration = 500
                    scaleYBy(-.3f)
                    scaleXBy(-.3f)
                }
            }.start()
            val intent = Intent(this, WorkoutsChoiceActivity::class.java)
            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, add_main, "addBtn")
            startActivity(intent, options.toBundle())
        }
        chatMain.setOnClickListener {
            chat_main
            animate(chatMain)
            val intent = Intent(this, ChatActivity::class.java)
            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, chatMain, "chatBtn")
            startActivity(intent, options.toBundle())
        }
        diagramMain.setOnClickListener {
            animate(diagramMain)
            val intent = Intent(this, WorkoutsActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                diagramMain,
                "trainingsBtn"
            )
            startActivity(intent, options.toBundle())
        }

        btn_sing_out_draw_layout.setOnClickListener {
            btn_sing_out_draw_layout.animate().apply {
                duration = 100
                scaleYBy(.3f)
                scaleXBy(.3f)
            }.withEndAction {
                btn_sing_out_draw_layout.animate().apply {
                    duration = 100
                    scaleYBy(-.3f)
                    scaleXBy(-.3f)
                }
            }.start()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        toggleButtonsGroup.checkedButtonId
        toggleButtonsGroup.isSingleSelection = true
        toggleButtonsGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    id.LastWeek -> setupLineChartData(7)
                    id.TwoWeeks -> setupLineChartData(14)
                    id.ThreeWeeks -> setupLineChartData(21)
                    id.OneMonth -> setupLineChartData(30)
                    id.All -> setupLineChartData(365)
                }
            }
        }
    }

    private fun animat() {
        val rtl = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        cvLineChart.startAnimation(rtl)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animate(btn: ImageButton) {
        val stateList = ColorStateList.valueOf(resources.getColor(R.color.holo_blue_light))
        val stateList2 = ColorStateList.valueOf(resources.getColor(R.color.background_dark))
        btn.animate().apply {
            duration = 500
            scaleYBy(.0f)
            scaleXBy(.0f)
            btn.backgroundTintList = stateList
        }.withEndAction {
            btn.animate().apply {
                duration = 500
                scaleYBy(-.0f)
                scaleXBy(-.0f)
                btn.backgroundTintList = stateList2
            }
        }.start()
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.nav_my_profile -> {
                startActivity(Intent(this, MyProfileActivity::class.java))
            }

            id.nav_Settings -> {
                startActivity(Intent(this, SecurityActivity::class.java))
            }

            id.btn_sing_out_draw_layout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                )
                startActivity(intent)
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun tipsItemsClick(tip: CardView) {
        tip.setOnClickListener { id ->
            when (id) {
                id.EZ_bar_curl -> {
                    val topic = "EZ Bar Curl"
                    val dic: String =
                        "You can use a narrow, medium, or wide grip to emphasize a specific part of the biceps or to increase the difficulty of the exercise.\n" + "\n" +
                                "A narrow grip will emphasize the outer head and a wide grip will emphasize the inner head.\n" + "\n" +
                                "Avoid bringing the elbows too far forward although you can move them a little bit forward as it may help to get a better contraction in the biceps. \n" + "\n" +
                                "Choose a weight that you can curl comfortably without needing to rock back, but it should be heavier enough to cause a good effort during each set. \n" + "\n" +
                                "Cheat reps can be effective for overloading the muscles and causing a greater stimulus but this technique, should, ideally, only be done by more experienced exercisers. \n" + "\n" +
                                "Do not move the elbows too far forward and rest at the top of the curl as this may relieve tension from the biceps. \n" + "\n" +
                                "We recommend to not lock out your elbows during the negative portion of the exercise to keep tension on your biceps."

                    val intent = Intent(this, TipsActivity::class.java)
                    intent.putExtra("TopicName", topic)
                    intent.putExtra("DescriptionName", dic)
                    startActivity(intent)
                }

                id.Dumbbell -> {

                    val topic = "Dumbbell Curl"
                    val dic =
                        "Once you've selected your weights, it's time to get your form down:\n" +
                                "\n" + "\n" +
                                "Stand with your feet hip-width apart with a dumbbell in each hand. Bend your knees slightly, engage your core and maintain good upright posture.\n" + "\n" +
                                "Position your arms so that your palms are facing forward. Hold onto the dumbbells, but don't grip them so tightly that you feel strain in your forearms.\n" + "\n" +
                                "Bending at the elbow, lift both dumbbells up toward your shoulders by flexing your bicep muscles. Lower the dumbbells the same way you raised them until your arms are fully extended in the same position you started in.\n" + "\n" +
                                "Repeat eight to 12 repetitions without swinging your weights. In other words, rely on your muscles rather than momentum. If you find yourself needing to add momentum to lift, try using a slightly lighter dumbbell instead, as swinging can lead to injury."
                    val intent = Intent(this, TipsActivity::class.java)
                    intent.putExtra("TopicName", topic)
                    intent.putExtra("DescriptionName", dic)
                    startActivity(intent)

                }
                id.One_arm_dumbbell -> {
                    val topic = "One arm dumbbell Curl"
                    val dic =
                        "Experiment with head position and see which option (looking forward vs. packing the neck) works better for you.\n" + "\n" +
                                "Fight the urge to use your opposing arm to brace against your leg or any other implement.\n" + "\n" +
                                "Keep some tone through your abdominals as you pull the dumbbell into your body to ensure you don’t arch excessively through your spine.\n" + "\n" +
                                "Don’t allow momentum to dictate the movement, control the dumbbells throughout the entirety of each rep.\n" +
                                "If you feel your biceps being overused and your back remaining under active, consider utilizing a false grip (i.e. don’t wrap the thumb around the dumbbell).\n" + "\n" +
                                "Don’t allow the head to jut forward as you pull.\n" +
                                "Similarly, ensure the shoulder blade moves on the rib cage. Don’t lock the shoulder blade down and just move through the glenohumeral joint.\n" + "\n" +
                                " "
                    val intent = Intent(this, TipsActivity::class.java)
                    intent.putExtra("TopicName", topic)
                    intent.putExtra("DescriptionName", dic)
                    startActivity(intent)
                }
                id.Seated_biceps_curls -> {

                    val topic = "Seated biceps curls"
                    val dic = "Step 1\n" +
                            "Starting Position: Sit in the machine, placing your arms over the incline pad. Adjust the seat height until the middle of your elbows aligns with the axis of rotation (fulcrum) of the moving lever (part) of the machine.  Grasp the handles firmly with a full grip (thumbs clasped around the handles) and maintain a neutral wrist position (wrists aligned with your forearms).  Your elbows should be extended, but not fully locked. Stiffen (“brace”) your abdominal muscles to stabilize your spine, and attempt to avoid movement in your low back throughout the exercise.  Align your head with your spine, and depress and retract your scapulae (pull shoulders back and down) and attempt to hold this position throughout the exercise.\n" +
                            "\n" +
                            "\n" +
                            "Step 2\n" +
                            "Gently exhale and slowly curl the bar upwards towards your chest by bending your elbows.  Maintain a neutral wrist position and avoid any movement in your torso during the exercise.\n" +
                            "\n" +
                            "\n" +
                            "Step 3\n" +
                            "Continue curling the bar upwards until your elbows can flex (bend) no further.  Pause momentarily then slowly return to your starting position, allowing your elbows to extend in a slow, controlled manner, moving the handles back towards the floor, stopping when your arms are extended, but not locked and the backs of your forearms make light contact with the incline pad.  Repeat the movement.\n" +
                            "\n" +
                            "\n" +
                            "Step 4\n" +
                            "Exercise Variation: This exercise can be performed unilaterally (one arm at a time)\n" +
                            "\n" +
                            "\n" +
                            "While this exercise targets the biceps effectively, proper technique is important to prevent unnecessary stresses placed in low back by swinging your torso backwards during the movement. Follow the instructions provided to avoid potential injury."
                    val intent = Intent(this, TipsActivity::class.java)
                    intent.putExtra("TopicName", topic)
                    intent.putExtra("DescriptionName", dic)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()

        }
    }

    private fun updateNavigationUserDetails() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //val username = snapshot.child("username").getValue(String::class.java)
                //val imageUri = snapshot.child("image").getValue(String::class.java)
                //Picasso.get().load(imageUri!!).into(main_drawer_profile_photo)
                //tv_username.text = username
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun dateFormatter(date: String): ChronoLocalDate? {
        val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"))

        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return currentDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupLineChartData(size: Int) {
        val currentDate =  getCurrentDate()
        val listWorkouts: ArrayList<Workout> = arrayListOf()
        val mapListWorkouts = HashMap<LocalDate, String>()
        val listCalories = arrayListOf<Double>()
        val listDates = arrayListOf<Int>()
        var sumCal = 0.0

        db.collection(Constant.USERS)
            .document(getCurrentUserId()).collection("Workouts")
            .orderBy("currentDateTime", Query.Direction.DESCENDING).get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    val document: QuerySnapshot? = task.result
                    if (document != null) {
                        for (doc in document) {
                            listWorkouts.add(doc.toObject(Workout::class.java))
                        }
                        listWorkouts.sortBy { it.date }
                        for (i in listWorkouts) {
                            for (j in listWorkouts) {
                                if (i.currentDateTime == j.currentDateTime) {
                                    sumCal += j.calorie
                                }
                            }
                            val date = dateFormatter(i.currentDateTime) as LocalDate
                            mapListWorkouts[date] = sumCal.toString()
                            sumCal = 0.0
                        }
                    }
                    val sorted = mapListWorkouts.toSortedMap()
                    var j = 0
                    for (i in sorted.keys.toList().indices) {

                        val sortedDate = sorted.keys.toList()[i] as LocalDate
                        val subtractedCurrentDate = currentDate.minusDays(size.toLong())

                        if (sortedDate > subtractedCurrentDate && currentDate > sortedDate) {
                            listDates.add(j)
                            listCalories.add(sorted.values.toList()[i].toDouble())
                            j++
                        }
                    }
                    chartSetConfig(listDates, listCalories)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): LocalDate {
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        val month: String = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        return dateFormatter("${year}-${month}-${day}") as LocalDate
    }
    private fun chartSetConfig(listDates: List<Int>, listCalories: ArrayList<Double>) {
        var i = 0
        val yVals = ArrayList<Entry>()
        while (i < listDates.size) {
            yVals.add(Entry(listDates[i].toFloat(), listCalories[i].toFloat(), i))
            i++
        }
        val set1 = LineDataSet(yVals, "DataSet 1")
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(dataSets)
        // set data
        lineChart.data = data

        set1.color = Color.BLUE
        set1.lineWidth = 0.5f
        set1.setDrawCircleHole(false)
        set1.setDrawCircles(false)
        lineChart.setScaleEnabled(false)
        set1.valueTextSize = 0f
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.setDrawFilled(true)
        set1.gradientColor
        set1.setCircleColor(Color.BLACK)
        lineChart.description.isEnabled = true
        lineChart.legend.isEnabled = true
        lineChart.background.transparentRegion
        //lineChart.setDrawGridBackground()
        //lineChart.xAxis.labelCount = listXDate2.size+2
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        //----------------------------------------------------------------------
        lineChart.axisLeft.setDrawLabels(false)
        lineChart.axisRight.setDrawLabels(false)
        lineChart.xAxis.setDrawLabels(true)
        // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.data.isHighlightEnabled = false
        val xAxis: XAxis = lineChart.xAxis
        xAxis.isEnabled = true
        xAxis.setDrawLabels(false)
        xAxis.labelCount = 7

        val yAxis: YAxis = lineChart.axisLeft
        yAxis.isEnabled = true
        yAxis.setDrawLabels(true)
        yAxis.labelCount = 10
        lineChart.axisRight
        /*       val vf: ValueFormatter = object : ValueFormatter() {
                   //value format here, here is the overridden method
                   override fun getFormattedValue(value: Float): String {
                       return "" + value.toInt()
                   }
               }
               val vf2: ValueFormatter = object : ValueFormatter() {
                   //value format here, here is the overridden method
                   override fun getFormattedValue(value: Float): String {
                       var m = ""
                       var n = 0.3
                       if (value.toString().contains(".30")) {
                           value - n
                           m = value.toString()
                       }

                       return m
                   }
               }*/
        xAxis.isGranularityEnabled = true
        xAxis.labelCount = listDates.size + 1
        lineChart.setDrawBorders(false)
        lineChart.setDrawGridBackground(false)
        lineChart.legend.isEnabled = false
        // no description text
        lineChart.description.isEnabled = false
        // enable touch gestures
        lineChart.setTouchEnabled(true)
        // enable scaling and dragging
        lineChart.isDragEnabled = false
        lineChart.setScaleEnabled(false)
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false)
        lineChart.isAutoScaleMinMaxEnabled = false
        lineChart.invalidate()
        lineChart.setNoDataTextColor(Color.BLUE)

        // hide legend
        val legend: Legend = lineChart.legend
        legend.isEnabled = false
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTrainingsFromFireStore() {
        val currentDate = getCurrentDate().toString()
        val query: Query = db.collection(Constant.USERS)
            .document(getCurrentUserID())
            .collection("Workouts")
            .whereEqualTo("currentDateTime", currentDate)
            .orderBy("date", Query.Direction.DESCENDING)
        val fireStoreRecyclerOption: FirestoreRecyclerOptions<Workout> =
            FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(query, Workout::class.java)
                .build()
        trainingItemAdapterMain = TrainingItemAdapterMain(fireStoreRecyclerOption)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = trainingItemAdapterMain
    }


    override fun onStart() {
        super.onStart()
        trainingItemAdapterMain.startListening()
    }

    override fun onStop() {
        super.onStop()
        trainingItemAdapterMain.stopListening()
    }
}

/*class UserLoged(val user: User): Item<ViewHolder>(){
   override fun bind(viewHolder: ViewHolder, position: Int) {
       // load our user image into the picture
       val uri = user.image
       val targetImageView = viewHolder.itemView.main_drawer_profile_photo
       Picasso.get().load(uri).into(targetImageView)
   }
   override fun getLayout(): Int {
       return R.layout.chat_from_row
   }
}*/







