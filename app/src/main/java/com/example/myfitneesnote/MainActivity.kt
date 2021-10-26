package com.example.myfitneesnote


import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.ChatLogActivity.Companion.TAG
import com.example.myfitneesnote.R.id
import com.example.myfitneesnote.R.layout
import com.example.myfitneesnote.adapters.TrainingItemAdapterMain
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
import kotlinx.android.synthetic.main.nav_header_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "IMPLICIT_CAST_TO_ANY")
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var trainingItemAdapterMain : TrainingItemAdapterMain
    private lateinit var recyclerView: RecyclerView
    lateinit var main : MainActivity
    var list= arrayListOf<String>()
    var Id = ""
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
        setupLineChartData(365)
      //  setupLineChartData2()
    }


    private fun getLastWrokoutId(id : ArrayList<String>) : String {
        var getId = ""
        getId = id.get(id.size-1)
        Id = getId
        return getId
    }
    private fun userData() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java)
                tv_username.text = username
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun onClick() {
        val mainMenu: ImageButton = findViewById(id.main_menu)
        val addMenu: ImageView = findViewById(id.Add_main)
        val chatMain: ImageButton = findViewById(id.chat_main)
        val mainImage: ImageButton = findViewById(id.tv_main_profile_image)
        val diagramMain: ImageButton = findViewById(id.main_diagramm)
        //val cvLinechart: LineChart = findViewById(id.lineChart)
        val tip1 : CardView = findViewById(id.tip1)
        val tip2 : CardView = findViewById(id.tip2)
        val tip3 : CardView = findViewById(id.tip3)
        val tip4 : CardView = findViewById(id.tip4)
        tip1.setOnClickListener {
            animateConst(tip1)
            startActivity(Intent(this,TipsActivity::class.java))
        }
        tip2.setOnClickListener {
            animateConst(tip2)
            startActivity(Intent(this,TipsActivity::class.java))
        }
        tip3.setOnClickListener {
            animateConst(tip3)
            startActivity(Intent(this,TipsActivity::class.java))
        }
        tip4.setOnClickListener {
            animateConst(tip4)
            startActivity(Intent(this,TipsActivity::class.java))
        }

        mainImage.setOnClickListener {
            animate(mainImage)
            val intent = Intent(this, MyProfileActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, tv_main_profile_image, "profileImage")
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
                val intent = Intent(this, WorkoutActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Add_main, "addBtn")
                startActivity(intent, options.toBundle())
        }
        chatMain.setOnClickListener {
            chat_main
            animate(chatMain)
            val intent = Intent(this, ChatActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, chatMain, "chatBtn")
            startActivity(intent, options.toBundle())
        }
        diagramMain.setOnClickListener {
            animate(diagramMain)
            val intent = Intent(this, TrainingActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, diagramMain, "trainingsBtn")
            startActivity(intent, options.toBundle())
        }

        btn_sing_out_draw_layout.setOnClickListener {
            btn_sing_out_draw_layout.animate().apply {
                duration =100
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
                    id.LastWeek ->   setupLineChartData(7)
                    id.TwoWeeks ->   setupLineChartData(14)
                    id.ThreeWeeks -> setupLineChartData(21)
                    id.OneMonth ->   setupLineChartData(30)
                    id.All ->        setupLineChartData(365)
                }
            }
        }
    }
    private fun animat(){
        val rtl = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        cvLineChart.startAnimation(rtl)
    }
    private fun animate(btn: ImageButton){
        btn.animate().apply {
            duration =100
            scaleYBy(.3f)
            scaleXBy(.3f)
        }.withEndAction {
            btn.animate().apply {
                duration = 100
                scaleYBy(-.3f)
                scaleXBy(-.3f)
            }
        }.start()
    }
    private fun animateConst(btn: CardView){
        btn.animate().apply {
            duration =100
            scaleYBy(.1f)
            scaleXBy(.1f)
        }.withEndAction {
            btn.animate().apply {
                duration = 100
                scaleYBy(-.1f)
                scaleXBy(-.1f)
            }
        }.start()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            id.nav_my_profile ->{
                startActivity(Intent(this, MyProfileActivity::class.java))
            }

            id.nav_Settings ->{
                startActivity(Intent(this, SecurityActivity::class.java))
            }
            id.btn_sing_out_draw_layout ->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(
                       Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK)
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
    private fun updateNavigationUserDetails() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              //  val username = snapshot.child("username").getValue(String::class.java)
                // val imageUri = snapshot.child("image").getValue(String::class.java)
                // Picasso.get().load(imageUri!!).into(main_drawer_profile_photo)
                //tv_username.text = username
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupLineChartData(size : Int) {
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        val month: String = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datelocal = "${year}-${month}-${day - size}"
        val datelocal2 = "${year}-${month}-${day}"


        val listCalories = arrayListOf<Double>()
        val listSumCalories = arrayListOf<Double>()
        val listDates = arrayListOf<String>()

        var listDuplicate = arrayListOf<Double>()
        var listD = arrayListOf<String>()
        var DuplicationsList = arrayListOf<Double>()

        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener { task ->
                val any = if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val caloriesDate = document.get("currentDateTime").toString()
                        val calorie = document.get("calorie").toString()
                        val wid = document.get("workout_id").toString()
                        val inputFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val caloriedate: Date = inputFormatter.parse(caloriesDate)
                        val currentDay : Date = inputFormatter.parse(datelocal)
                        val currentDate : Date = inputFormatter.parse(datelocal2)
                        val outputFormatter: DateFormat = SimpleDateFormat("MM-dd")
                        val output: String = outputFormatter.format(caloriedate) // Output : 01/20/2012
                         list.add(wid)
                        if ( currentDay  < caloriedate) {
                            listCalories.add(calorie.toDouble())
                            listDates.add(wid)
                      }
                    }
                    // find duplication Date and sum Calories
                    var sum = 0.0
                    for ( i  in 0 until listDates.size){
                        for(j in 0 until listDates.size){
                            if( i !=j && listDates[i] == listDates[j]){
                                if(sum == 0.0){
                                    sum += listCalories[i]+listCalories[j]
                                }else{
                                    sum += listCalories[j]
                                }
                            }
                        }
                        listDuplicate.add(sum)
                        if(sum == 0.0 ){
                            listDuplicate.add(listCalories[i])
                        }
                        sum = 0.0
                    }

                    for(i in 0 until listDuplicate.size){
                        listDuplicate.remove(0.0)
                    }

                    print(" \n Test Calories = " + listDuplicate.toString()+"\n")
                    print(" \n Test  Dates  = " + listDates.toString()+"\n")

                    val listSumCalories =  listDuplicate.distinct()
                    val listDates3 = listDates.distinct()

                    print(" \n Test Calories = " + listSumCalories.toString()+"\n")
                    print(" \n Test  Dates  = " + listDates3.toString()+"\n")
                    // add Lists to the SetChart
                    chartSetConfig(listDates3, listSumCalories)


                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }

    private fun chartSetConfig(listDates: List<String>, listCalories: List<Double>){
        var i = 0
        val yVals = ArrayList<Entry>()
        while (i < listDates.size) {
            yVals.add(Entry(listDates[i].toFloat(), listCalories[i].toFloat(), i))
            i++
        }
        val set1 = LineDataSet(yVals, "DataSet 1")
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
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(dataSets)
        // set data
        lineChart.data = data
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = true
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
        xAxis.isEnabled = false

        val yAxis: YAxis = lineChart.axisLeft
        yAxis.isEnabled = true
        yAxis.setDrawLabels(true)
        yAxis.labelCount = 10
        lineChart.axisRight
        val vf: ValueFormatter = object : ValueFormatter() {
            //value format here, here is the overridden method
            override fun getFormattedValue(value: Float): String {
                return "" + value.toInt()
                //${month}/
            }
        }
        val vf2: ValueFormatter = object : ValueFormatter() {
            //value format here, here is the overridden method
            override fun getFormattedValue(value: Float): String {
                var m = ""
                var n = 0.3
                if(value.toString().contains(".30")){
                    value -n
                    m = value.toString()
                }

                return  m
            }
        }
        //xAxis.axisMinimum = 7f
        xAxis.valueFormatter = vf2

        xAxis.isGranularityEnabled =  true

        xAxis.labelCount = listDates.size +1
        yAxis.setDrawLabels(true)
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

    private fun getTrainingsFromFireStore(){
        val c= Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val currentDate = "${year}-${month +1}-${day}"
        val query : Query = db.collection(Constant.USERS)
            .document(getCurrentUserID())
            .collection(Constant.TRAININGS)
            .whereEqualTo("currentDateTime", currentDate)
            .orderBy("date",Query.Direction.DESCENDING)
        val fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
            .setQuery(query, Workout::class.java)
            .build()
        trainingItemAdapterMain = TrainingItemAdapterMain(fireStoreRecyclerOption)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter= trainingItemAdapterMain
    }
    override fun onStart() {
        super.onStart()
        trainingItemAdapterMain.startListening()
    }
    override fun onStop() {
        super.onStop()
        trainingItemAdapterMain.stopListening()
    }

    private fun setupLineChartData2() {

        var listCalories = arrayListOf<Int>()
        var listDuplicate = arrayListOf<Int>()
        var listDates = arrayListOf<String>()
        var listD = arrayListOf<String>()
        var DuplicationsList = arrayListOf<Int>()



        listCalories = arrayListOf<Int>(10,25,20,35,30)
        listDates = arrayListOf<String>("10","10","10","13","14")

       // find duplication Date and sum Calories
        var sum = 0
        for ( i  in 0 until listDates.size){
            for(j in 0 until listDates.size){
                if( i !=j && listDates[i] == listDates[j]){
                    if(sum == 0){
                        sum += listCalories[i]+listCalories[j]
                    }else{
                        sum += listCalories[j]
                    }
                }
            }
            listDuplicate.add(sum)
            if(sum == 0 ){
                listDuplicate.add(listCalories[i])
            }
            sum = 0
        }
        listDuplicate.remove(0)

        for(i in 0 until listDuplicate.size){
            listDuplicate.remove(0)
        }


        print(listD.toString()+"\n")
        print("sum = " + listDuplicate.toString()+"\n")

        // Add duplication to another list
        for(i in 0 until listDuplicate.size){
            if (listDuplicate[i] != 0) {
                DuplicationsList.add(listDuplicate[i])
            }
        }
        // set not duplication v
        var sum2 = 0
        for ( i  in 0 until listDates.size){
            for(j in 0 until listDates.size){
                if( i !=j && listDates[i] == listDates[j]){
                    listCalories[i] = 0
                }
            }
            listDuplicate.add(sum2)
            sum2 = 0
        }
        print("sum2 = " + listCalories.toString()+"\n")

        for(i in 0 until listCalories.size){
                DuplicationsList.add(listCalories[i])
        }
        print("sum3 = " + DuplicationsList.toString()+"\n")

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







