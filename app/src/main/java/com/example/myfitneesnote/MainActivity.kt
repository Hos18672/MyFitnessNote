package com.example.myfitneesnote

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.ChatLogActivity.Companion.TAG
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.adapters.TrainingItemAdapterMain
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.mikephil.charting.charts.LineChart
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_layout.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val db = FirebaseFirestore.getInstance()
    private val currentDateAndTime = com.google.firebase.Timestamp.now()
    private lateinit var trainingItemAdapterMain : TrainingItemAdapterMain
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setupLineChartData2()
        nav_view.setNavigationItemSelectedListener(this)
        //FirestoreClass().loginUser(this)
        fullscreen()
        onClick()
        userData()
        recyclerView = findViewById(id.rv_trainings_list_main)
        getTrainingsFromFireStore()
       // userWorkoutsData()
        getDataFromFireStore()
        updateNavigationUserDetails()
    }
    private fun userData() {
        val uid = FirebaseAuth.getInstance().uid
        //val mainUsername : TextView = findViewById(id.tv_main_username)
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java)
                //mainUsername.text = username
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
/*   @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun userWorkoutsData() {
       val tvGymename :TextView = findViewById(id.tv_main_GymName)
       val tvMuskelname :TextView = findViewById(id.tv_main_muscle)
       val tvSet :TextView = findViewById(id.tv_main_sets)
       val tvWeight:TextView = findViewById(id.tv_main_weight)
       val tvBreak :TextView = findViewById(id.tv_main_break)
       val tvRepeat :TextView = findViewById(id.tv_main_repeat)
       val tvDate :TextView = findViewById(id.tv_main_date)
       val constraintLayout :ConstraintLayout = findViewById(id.const_layout_main)
       val tvNoTrainings:TextView = findViewById(id.tv_no_trainings)
       //get current Day of month
       val c= Calendar.getInstance()
       val year = c.get(Calendar.YEAR)
       val day = c.get(Calendar.DAY_OF_MONTH)
       val month = c.get(Calendar.MONTH)
       val currentDate = "${year}/${month +1}/${day}"
      // var output = ""
       var gymeType: String
       var musclename: String
       var sets: String
       var weight: String
       var breakTime: String
       var repeat: String
       var date: String
      // val outputFormatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
       db.collection(Constant.USERS)
           .document(getCurrentUserID())
           .collection(Constant.TRAININGS).whereEqualTo("currentDateTime", currentDate)
           .get()
           .addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   for (document in task.result!!) {
                       if (document.exists()) {
                           gymeType = document.get("gymType").toString()
                           musclename = document.get("muskelName").toString()
                           sets = document.get("set").toString()
                           weight = document.get("weight").toString()
                           breakTime = document.get("breakTime").toString()
                           repeat = document.get("repeat").toString()
                           date = document.get("currentDateTime").toString()
                           tvGymename.text = gymeType
                           // set Data to the TextViews
                           tvMuskelname.text = musclename
                           tvSet.text = "$sets x"
                           tvWeight.text = "$weight kg"
                           tvBreak.text = "$breakTime min"
                           tvRepeat.text = "$repeat x"
                           tvDate.text = date
                           tvNoTrainings.visibility = GONE
                           constraintLayout.visibility = VISIBLE
                       } else {
                           tvNoTrainings.visibility = VISIBLE
                           constraintLayout.visibility = GONE
                       }
                   }
               } else {
                   Log.d(TAG, "Error getting documents:", task.exception)
               }
           }
   }*/
   private fun onClick() {
       val mainMenu: Button = findViewById(id.main_menu)
       val addMenu: ImageView = findViewById(id.Add_main)
       val chatMain: Button = findViewById(id.chat_main)
       val cvChart: CardView = findViewById(id.cv_lineChart)
       val mainImage: Button = findViewById(id.tv_main_profile_image)
       val diagramMain: Button = findViewById(id.main_diagramm)
       val cvLinechart: LineChart = findViewById(id.lineChart)
       mainImage.setOnClickListener {
           animate(mainImage)
           startActivity(Intent(this,myProfileActivity::class.java))
       }

       cvLinechart.setOnClickListener {
           animateCV(cvChart)
           startActivity(Intent(this,TrainingActivity::class.java))
       }
       mainMenu.setOnClickListener {
           animate(mainMenu)
           if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
               drawer_layout.closeDrawer(GravityCompat.START)
           } else {
               drawer_layout.openDrawer(GravityCompat.START)
           }
       }
       addMenu.setOnClickListener {
           startActivity(Intent(this, WorkoutActivity::class.java))
       }
       chatMain.setOnClickListener {
           animate(chatMain)
           startActivity(Intent(this, ChatActivity::class.java))
       }
       diagramMain.setOnClickListener {
           animate(diagramMain)
           startActivity(Intent(this, TrainingActivity::class.java))
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
           finish()
       }
   }
   private fun animate(btn: Button){
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
   private fun animateCV(btn: CardView){
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
    private fun updateNavigationUserDetails() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java)
               // val imageUri = snapshot.child("image").getValue(String::class.java)
               // Picasso.get().load(imageUri!!).into(main_drawer_profile_photo)
                //tv_username.text = username
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
   @SuppressLint("NewApi", "SimpleDateFormat")
   private fun getDataFromFireStore() {
       val list1 = arrayListOf<String>()
       val list2 = arrayListOf<String>()
       db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS).get()
           .addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   for (document in task.result!!) {
                       val input = document.get("currentDateTime").toString()
                       // val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd - HH:mm")
                       val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd")
                       val date: Date = inputFormatter.parse(input)
                       val outputFormatter: DateFormat = SimpleDateFormat("MM/dd/yyyy")
                       val output: String = outputFormatter.format(date) // Output : 01/20/2012
                       list1.add(output)
                       list2.add(document.get("set").toString())
                   }
                   Log.d("--------Test1------", list1.toString())
                   Log.d("--------Test2------", list2.toString())
               } else {
                   Log.d(TAG, "Error getting documents: ", task.exception)
               }
           }
   }
   @SuppressLint("SimpleDateFormat")
   private fun setupLineChartData2() {
       val listXDate = arrayListOf<String>()
       val listYData = arrayListOf<String>()
       val yVals = ArrayList<Entry>()
       var outputMonth: String
       val c= Calendar.getInstance()
       val year = c.get(Calendar.YEAR)
       val day = c.get(Calendar.DAY_OF_MONTH)
       val month = c.get(Calendar.MONTH)
       var datelocal : String = "${year}/${month +1}/${day}"

       db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
           .whereLessThan("date", currentDateAndTime)
           .orderBy("date", Query.Direction.ASCENDING)
           .get()
           .addOnCompleteListener { task ->
               val any = if (task.isSuccessful) {
                   for (document in task.result!!) {
                       val input = document.get("currentDateTime").toString()
                       //val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd - HH:mm")
                       val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd")
                       val date: Date = inputFormatter.parse(input)
                       val d : Date = inputFormatter.parse(datelocal)
                       val outputFormatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                       val output: String = outputFormatter.format(date) // Output : 01/20/2012
                       outputMonth = outputFormatter.format(date)
                       if (d > date) {
                           listXDate.add(output.substring(0, output.length - 8).toInt().toString())
                           listYData.add(document.get("set").toString())
                       }
                   }
                   var i = 0
                   while (i < listXDate.size) {
                       if (listXDate[i] <= day.toString())
                           yVals.add(Entry(listXDate[i].toFloat(), listYData[i].toFloat(), i))
                       i++
                   }
                   val set1: LineDataSet = LineDataSet(yVals, "DataSet 1")
                   set1.color = Color.BLUE
                   set1.setCircleColor(Color.BLUE)
                   set1.lineWidth = 2f
                   set1.circleRadius = 3f
                   lineChart.setScaleEnabled(false);
                   set1.setDrawCircleHole(false)
                   set1.valueTextSize = 0f
                   set1.mode = LineDataSet.Mode.CUBIC_BEZIER
                   set1.setDrawFilled(true)

                   val dataSets = ArrayList<ILineDataSet>()
                   dataSets.add(set1)
                   val data = LineData(dataSets)

                   // set data
                   lineChart.data = data
                   lineChart.description.isEnabled = false
                   lineChart.legend.isEnabled = false
                   //lineChart.setDrawGridBackground()
                   lineChart.xAxis.labelCount = listXDate.size + 1
                   lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                   val yAxisRight: YAxis = lineChart.axisRight
                   yAxisRight.isEnabled = false
                   lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                   //----------------------------------------------------------------------
                   lineChart.axisLeft.setDrawLabels(false)
                   lineChart.axisRight.setDrawLabels(false)
                   //lineChart.getXAxis().setDrawLabels(true)
                   // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                   lineChart.axisLeft.setDrawGridLines(false)
                   lineChart.xAxis.setDrawGridLines(false)
                   lineChart.axisRight.setDrawGridLines(false)
                   lineChart.data.isHighlightEnabled = false

                   val xAxis: XAxis = lineChart.xAxis
                   xAxis.isEnabled = true
                   val yAxis: YAxis = lineChart.axisLeft
                   yAxis.isEnabled = false
                   lineChart.axisRight

                   val c = Calendar.getInstance()
                   val currentMonth = c.get(Calendar.MONTH) + 1
                   val month: String = currentMonth.toString()

                   val vf: ValueFormatter = object : ValueFormatter() {
                       //value format here, here is the overridden method
                       override fun getFormattedValue(value: Float): String {
                           return "${month}/" + value.toInt()
                       }
                   }
                   xAxis.valueFormatter = vf
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
                   lineChart.isAutoScaleMinMaxEnabled = true
                   lineChart.invalidate()
                   lineChart.setNoDataTextColor(Color.BLUE)
                   // hide legend
                   val legend: Legend = lineChart.legend
                   legend.isEnabled = false
               } else {
                   Log.d(TAG, "Error getting documents: ", task.exception)
               }
           }
   }
    private fun getTrainingsFromFireStore(){
        val c= Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val currentDate = "${year}/${month +1}/${day}"
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







