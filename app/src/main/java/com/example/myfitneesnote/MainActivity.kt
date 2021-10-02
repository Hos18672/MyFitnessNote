package com.example.myfitneesnote


import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Pair.create
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.ChatLogActivity.Companion.TAG
import com.example.myfitneesnote.R.*
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
import kotlinx.android.synthetic.main.activity_intro.*
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
    private lateinit var toggleButton: ToggleButton

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setupLineChartDataLastWeek()
        nav_view.setNavigationItemSelectedListener(this)
        //FirestoreClass().loginUser(this)
        constraintLayout3.bringToFront()
        fullscreen()
        onClick()
        userData()
        animat()
        recyclerView = findViewById(id.rv_trainings_list_main)
        getTrainingsFromFireStore()
        // userWorkoutsData()
        getDataFromFireStoreTest()
        updateNavigationUserDetails()
        toggleButtonsGroup.checkedButtonId
        toggleButtonsGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    id.LastWeek -> setupLineChartDataLastWeek()
                    id.TwoWeeks -> setupLineChartDataLastTwoWeek()
                    id.ThreeWeeks -> setupLineChartDataLastThreeWeek()
                    id.OneMonth -> setupLineChartDataLastMonth()
                    id.TwoMonth -> setupLineChartDataLastTwoMonth()
                    id.ThreeMonth -> setupLineChartData2()
                }
            } else {
            }
        }
    }


    private fun showToast(str: String){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
    private fun userData() {
        val uid = FirebaseAuth.getInstance().uid
        //val mainUsername : TextView = findViewById(id.tv_main_username)
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
        //val cvChart: CardView = findViewById(id.cv_lineChart)
        val mainImage: ImageButton = findViewById(id.tv_main_profile_image)
        val diagramMain: ImageButton = findViewById(id.main_diagramm)
        //val cvLinechart: LineChart = findViewById(id.lineChart)
        val tip1 : CardView = findViewById(id.tip1)
        val tip2 : CardView = findViewById(id.tip2)
        val tip3 : CardView = findViewById(id.tip3)
        val tip4 : CardView = findViewById(id.tip4)
        tip1.setOnClickListener {
            animateConst(tip1)
            startActivity(Intent(this,tipsActivity::class.java))
        }
        tip2.setOnClickListener {
            animateConst(tip2)
            startActivity(Intent(this,tipsActivity::class.java))
        }
        tip3.setOnClickListener {
            animateConst(tip3)
            startActivity(Intent(this,tipsActivity::class.java))
        }
        tip4.setOnClickListener {
            animateConst(tip4)
            startActivity(Intent(this,tipsActivity::class.java))
        }

        mainImage.setOnClickListener {
            animate(mainImage)
            val intent = Intent(this, myProfileActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, tv_main_profile_image, "profileImage")
            startActivity(intent, options.toBundle())
        }
//       cvLinechart.setOnClickListener {
//           animateCV(cvChart)
//           startActivity(Intent(this,TrainingActivity::class.java))
//       }

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
    }

    fun animat(){
        val rtl = AnimationUtils.loadAnimation(this, R.anim.rtl)
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

            id.nav_Settings ->{
                startActivity(Intent(this, SecurityActivity::class.java))
            }
            id.btn_sing_out_draw_layout ->{
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
    private fun getDataFromFireStoreTest() {
        val list1 = arrayListOf<String>()
        val ListDays = arrayListOf<String>()
        val list2 = arrayListOf<String>()
        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val input = document.get("currentDateTime").toString()
                        // val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd - HH:mm")
                        val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd")
                        val date: Date = inputFormatter.parse(input)
                        val outputFormatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val output: String = outputFormatter.format(date) // Output : 01/20/2012
                        val dayNum = output.substring(0, output.length - 8).toInt().toString()
                        ListDays.add(dayNum)
                        list1.add(output)
                        list2.add(document.get("set").toString())
                    }
                    Log.d("--------Test1------", list1.toString())
                    Log.d("--------Test1------", ListDays.toString())
                    Log.d("--------Test2------", list2.toString())
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupLineChartData2() {
        val listXDate = arrayListOf<String>()
        val listXDate2 = arrayListOf<String>()
        val listYData = arrayListOf<String>()
        val listYData2 = arrayListOf<String>()
        val yVals = ArrayList<Entry>()
        var outputMonth: String
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        val month: String = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        var datelocal : String = "${year}/${month}/${day}"

        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener { task ->
                val any = if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val input = document.get("currentDateTime").toString()
                        //val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd - HH:mm")
                        val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd")
                        val date: Date = inputFormatter.parse(input)
                        //val d : Date = inputFormatter.parse(datelocal)
                        val outputFormatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val output: String = outputFormatter.format(date) // Output : 01/20/2012
                        val dayNum = output.substring(0, output.length - 8).toInt().toString()
                        outputMonth = outputFormatter.format(date)
                        var m = document.get("set").toString().toInt()

                        //if (d > date) {
                            listXDate.add(dayNum)
                            listYData.add(m.toString())
                      //  }
                    }
                    Log.d("--------TestX------", listXDate.toString())
                    Log.d("--------TestY------", listYData.toString())

                    var i = 0
                    while (i <= listXDate.size-1) {
                        yVals.add(Entry(listXDate[i].toFloat(), listYData[i].toFloat(), i))
                        i++
                    }
                    val set1: LineDataSet = LineDataSet(yVals, "DataSet 1")
                    set1.color = Color.BLUE
                    set1.lineWidth = 2f
                    set1.circleRadius = 3f
                    lineChart.setScaleEnabled(true)
                    set1.valueTextSize = 0f
                    set1.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set1.setDrawFilled(true)
                    set1.setDrawCircleHole(true)
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
                    lineChart.getXAxis().setDrawLabels(true)
                    // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                    lineChart.axisLeft.setDrawGridLines(false)
                    lineChart.xAxis.setDrawGridLines(false)
                    lineChart.axisRight.setDrawGridLines(false)
                    lineChart.data.isHighlightEnabled = false
                    val xAxis: XAxis = lineChart.xAxis
                    xAxis.isEnabled = true

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
                            return "" + value.toInt()
                        }
                    }
                    xAxis.valueFormatter = vf
                    yAxis.valueFormatter = vf2
                    //xAxis.labelCount = listXDate.size
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
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
    @SuppressLint("SimpleDateFormat")
    private fun setupLineChartDataLastWeek() {
        val listXDate = arrayListOf<String>()
        val listXDate2 = arrayListOf<String>()
        val listYData = arrayListOf<String>()
        val listYData2 = arrayListOf<String>()
        val yVals = ArrayList<Entry>()
        var outputMonth: String
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        val month: String = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        var datelocal : String = "${year}/${month}/${day.minus(7)}"

        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
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
                        val dayNum = output.substring(0, output.length - 8).toInt().toString()
                        outputMonth = outputFormatter.format(date)
                        var m = document.get("set").toString().toInt()

                        if (d < date) {
                            listXDate.add(dayNum)
                            listYData.add(m.toString())
                        }
                    }
                    /*  var j = 0
                   var sum = 0
                   var m = 0
                   var n =0
                   var k = 1
                   while ( j < listXDate.size) {
                       n = listYData.get(j).toInt()
                       while (k  < listXDate.size ) {
                           if (listXDate.get(j).toInt().equals(listXDate.get(k).toInt()) && j != k ) {
                               m += listYData.get(k).toInt()
                               m += n
                               sum +=  m
                               m = 0
                               n = 0
                           }
                           k++

                       }
                        k = 0
                        n = 0

                       if(sum == 0 ){
                        sum = listYData.get(j).toInt()
                           listXDate2.add(listXDate.get(j))
                           listYData2.add(sum.toString())
                           sum = 0
                           j++
                       }else {
                           listXDate2.add(listXDate.get(j))
                           listYData2.add(sum.toString())
                           sum = 0
                           j++
                       }
                   }
                   // delete duplication in List X
                   val hashSet: Set<String> = LinkedHashSet(listXDate2)
                   val removedDuplicates = ArrayList(hashSet)
                   // delete duplication in List Y
                val hashSet2: Set<String> = LinkedHashSet(listYData2)
                   val removedDuplicates2 = ArrayList(hashSet2)*/
                    var i = 0
                    while (i <= listXDate.size-1) {
                        yVals.add(Entry(listXDate[i].toFloat(), listYData[i].toFloat(), i))
                        i++
                    }
                    val set1: LineDataSet = LineDataSet(yVals, "DataSet 1")
                    set1.color = Color.BLUE
                    set1.lineWidth = 2f
                    set1.circleRadius = 3f
                    lineChart.setScaleEnabled(true);
                    set1.valueTextSize = 0f
                    set1.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set1.setDrawFilled(true)
                    set1.setDrawCircleHole(true)
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
                    lineChart.getXAxis().setDrawLabels(true)
                    // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                    lineChart.axisLeft.setDrawGridLines(false)
                    lineChart.xAxis.setDrawGridLines(false)
                    lineChart.axisRight.setDrawGridLines(false)
                    lineChart.data.isHighlightEnabled = false
                    val xAxis: XAxis = lineChart.xAxis
                    xAxis.isEnabled = true

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
                            return "" + value.toInt()
                        }
                    }
                    xAxis.valueFormatter = vf
                    yAxis.valueFormatter = vf2
                    xAxis.labelCount = listXDate.size
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
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
    private fun setupLineChartDataLastTwoWeek() {
        val listXDate = arrayListOf<String>()
        val listXDate2 = arrayListOf<String>()
        val listYData = arrayListOf<String>()
        val listYData2 = arrayListOf<String>()
        val yVals = ArrayList<Entry>()
        var outputMonth: String
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        val month: String = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        var datelocal : String = "${year}/${month}/${day.minus(14)}"

        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
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
                        val dayNum = output.substring(0, output.length - 8).toInt().toString()
                        outputMonth = outputFormatter.format(date)
                        var m = document.get("set").toString().toInt()

                        if (d < date) {
                            listXDate.add(dayNum)
                            listYData.add(m.toString())
                        }
                    }
                    /*  var j = 0
                   var sum = 0
                   var m = 0
                   var n =0
                   var k = 1
                   while ( j < listXDate.size) {
                       n = listYData.get(j).toInt()
                       while (k  < listXDate.size ) {
                           if (listXDate.get(j).toInt().equals(listXDate.get(k).toInt()) && j != k ) {
                               m += listYData.get(k).toInt()
                               m += n
                               sum +=  m
                               m = 0
                               n = 0
                           }
                           k++

                       }
                        k = 0
                        n = 0

                       if(sum == 0 ){
                        sum = listYData.get(j).toInt()
                           listXDate2.add(listXDate.get(j))
                           listYData2.add(sum.toString())
                           sum = 0
                           j++
                       }else {
                           listXDate2.add(listXDate.get(j))
                           listYData2.add(sum.toString())
                           sum = 0
                           j++
                       }
                   }
                   // delete duplication in List X
                   val hashSet: Set<String> = LinkedHashSet(listXDate2)
                   val removedDuplicates = ArrayList(hashSet)
                   // delete duplication in List Y
                val hashSet2: Set<String> = LinkedHashSet(listYData2)
                   val removedDuplicates2 = ArrayList(hashSet2)*/
                    var i = 0
                    while (i <= listXDate.size-1) {
                        yVals.add(Entry(listXDate[i].toFloat(), listYData[i].toFloat(), i))
                        i++
                    }
                    val set1: LineDataSet = LineDataSet(yVals, "DataSet 1")
                    set1.color = Color.BLUE
                    set1.lineWidth = 2f
                    set1.circleRadius = 3f
                    lineChart.setScaleEnabled(true);
                    set1.valueTextSize = 0f
                    set1.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set1.setDrawFilled(true)
                    set1.setDrawCircleHole(true)
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
                    lineChart.getXAxis().setDrawLabels(true)
                    // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                    lineChart.axisLeft.setDrawGridLines(false)
                    lineChart.xAxis.setDrawGridLines(false)
                    lineChart.axisRight.setDrawGridLines(false)
                    lineChart.data.isHighlightEnabled = false
                    val xAxis: XAxis = lineChart.xAxis
                    xAxis.isEnabled = true

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
                            return "" + value.toInt()
                        }
                    }
                    xAxis.valueFormatter = vf
                    yAxis.valueFormatter = vf2
                    xAxis.labelCount = listXDate.size
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
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
    private fun setupLineChartDataLastThreeWeek() {
        val listXDate = arrayListOf<String>()
        val listXDate2 = arrayListOf<String>()
        val listYData = arrayListOf<String>()
        val listYData2 = arrayListOf<String>()
        val yVals = ArrayList<Entry>()
        var outputMonth: String
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        val month: String = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        var datelocal : String = "${year}/${month.toInt()-1}/${day}"

        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
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
                        val dayNum = output.substring(0, output.length - 8).toInt().toString()
                        outputMonth = outputFormatter.format(date)
                        var m = document.get("set").toString().toInt()

                        if (d < date) {
                            listXDate.add(dayNum)
                            listYData.add(m.toString())
                        }
                    }
                    /*  var j = 0
                   var sum = 0
                   var m = 0
                   var n =0
                   var k = 1
                   while ( j < listXDate.size) {
                       n = listYData.get(j).toInt()
                       while (k  < listXDate.size ) {
                           if (listXDate.get(j).toInt().equals(listXDate.get(k).toInt()) && j != k ) {
                               m += listYData.get(k).toInt()
                               m += n
                               sum +=  m
                               m = 0
                               n = 0
                           }
                           k++

                       }
                        k = 0
                        n = 0

                       if(sum == 0 ){
                        sum = listYData.get(j).toInt()
                           listXDate2.add(listXDate.get(j))
                           listYData2.add(sum.toString())
                           sum = 0
                           j++
                       }else {
                           listXDate2.add(listXDate.get(j))
                           listYData2.add(sum.toString())
                           sum = 0
                           j++
                       }
                   }
                   // delete duplication in List X
                   val hashSet: Set<String> = LinkedHashSet(listXDate2)
                   val removedDuplicates = ArrayList(hashSet)
                   // delete duplication in List Y
                val hashSet2: Set<String> = LinkedHashSet(listYData2)
                   val removedDuplicates2 = ArrayList(hashSet2)*/
                    var i = 0
                    while (i <= listXDate.size-1) {
                        yVals.add(Entry(listXDate[i].toFloat(), listYData[i].toFloat(), i))
                        i++
                    }
                    val set1: LineDataSet = LineDataSet(yVals, "DataSet 1")
                    set1.color = Color.BLUE
                    set1.lineWidth = 2f
                    set1.circleRadius = 3f
                    lineChart.setScaleEnabled(true);
                    set1.valueTextSize = 0f
                    set1.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set1.setDrawFilled(true)
                    set1.setDrawCircleHole(true)
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
                    lineChart.getXAxis().setDrawLabels(true)
                    // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                    lineChart.axisLeft.setDrawGridLines(false)
                    lineChart.xAxis.setDrawGridLines(false)
                    lineChart.axisRight.setDrawGridLines(false)
                    lineChart.data.isHighlightEnabled = false
                    val xAxis: XAxis = lineChart.xAxis
                    xAxis.isEnabled = true

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
                            return "" + value.toInt()
                        }
                    }
                    xAxis.valueFormatter = vf
                    yAxis.valueFormatter = vf2
                    xAxis.labelCount = listXDate.size
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
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
    private fun setupLineChartDataLastMonth() {
        val listXDate = arrayListOf<String>()
        val listXDate2 = arrayListOf<String>()
        val listYData = arrayListOf<String>()
        val listYData2 = arrayListOf<String>()
        val yVals = ArrayList<Entry>()
        var outputMonth: String
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        val month: String = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        var datelocal : String = "${year}/${month}/${day-29}"

        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
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
                        val dayNum = output.substring(0, output.length - 8).toInt().toString()
                        outputMonth = outputFormatter.format(date)
                        var m = document.get("set").toString().toInt()

                        if (d < date) {
                            listXDate.add(dayNum)
                            listYData.add(m.toString())
                        }
                    }
                    var i = 0
                    while (i <= listXDate.size-1) {
                        yVals.add(Entry(listXDate[i].toFloat(), listYData[i].toFloat(), i))
                        i++
                    }
                    val set1: LineDataSet = LineDataSet(yVals, "DataSet 1")
                    set1.color = Color.BLUE
                    set1.lineWidth = 2f
                    set1.circleRadius = 3f
                    lineChart.setScaleEnabled(true);
                    set1.valueTextSize = 0f
                    set1.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set1.setDrawFilled(true)
                    set1.setDrawCircleHole(true)
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
                    lineChart.getXAxis().setDrawLabels(true)
                    // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                    lineChart.axisLeft.setDrawGridLines(false)
                    lineChart.xAxis.setDrawGridLines(false)
                    lineChart.axisRight.setDrawGridLines(false)
                    lineChart.data.isHighlightEnabled = false
                    val xAxis: XAxis = lineChart.xAxis
                    xAxis.isEnabled = true

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
                            return "" + value.toInt()
                        }
                    }
                    xAxis.valueFormatter = vf
                    yAxis.valueFormatter = vf2
                    xAxis.labelCount = listXDate.size
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
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
    private fun setupLineChartDataLastTwoMonth() {
        val listXDate = arrayListOf<String>()
        val listXDate2 = arrayListOf<String>()
        val listYData = arrayListOf<String>()
        val listYData2 = arrayListOf<String>()
        val yVals = ArrayList<Entry>()
        var outputMonth: String
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 5
        val year = c.get(Calendar.YEAR)
        val month: String = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        var datelocal : String = "${year}/${month}/${day-59}"

        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
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
                        val dayNum = output.substring(0, output.length - 8).toInt().toString()
                        outputMonth = outputFormatter.format(date)
                        var m = document.get("set").toString().toInt()

                        if (d < date) {
                            listXDate.add(dayNum)
                            listYData.add(m.toString())
                        }
                    }
                    var i = 0
                    while (i <= listXDate.size-1) {
                        yVals.add(Entry(listXDate[i].toFloat(), listYData[i].toFloat(), i))
                        i++
                    }
                    val set1: LineDataSet = LineDataSet(yVals, "DataSet 1")
                    set1.color = Color.BLUE
                    set1.lineWidth = 2f
                    set1.circleRadius = 3f
                    lineChart.setScaleEnabled(true);
                    set1.valueTextSize = 0f
                    set1.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set1.setDrawFilled(true)
                    set1.setDrawCircleHole(true)
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
                    lineChart.getXAxis().setDrawLabels(true)
                    // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                    lineChart.axisLeft.setDrawGridLines(false)
                    lineChart.xAxis.setDrawGridLines(false)
                    lineChart.axisRight.setDrawGridLines(false)
                    lineChart.data.isHighlightEnabled = false
                    val xAxis: XAxis = lineChart.xAxis
                    xAxis.isEnabled = true

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
                            return "" + value.toInt()
                        }
                    }
                    xAxis.valueFormatter = vf
                    yAxis.valueFormatter = vf2
                    xAxis.labelCount = listXDate.size
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
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
    private fun setupLineChartDataLastThreeMonth() {
        val listXDate = arrayListOf<String>()
        val listXDate2 = arrayListOf<String>()
        val listYData = arrayListOf<String>()
        val listYData2 = arrayListOf<String>()
        val yVals = ArrayList<Entry>()
        var outputMonth: String
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 5
        val year = c.get(Calendar.YEAR)
        val month: String = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        var datelocal : String = "${year}/${month}/${day.minus(7)}"

        db.collection(Constant.USERS).document(getCurrentUserID()).collection(Constant.TRAININGS)
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
                        val dayNum = output.substring(0, output.length - 8).toInt().toString()
                        outputMonth = outputFormatter.format(date)
                        var m = document.get("set").toString().toInt()

                        if (d < date) {
                            listXDate.add(dayNum)
                            listYData.add(m.toString())
                        }
                    }
                    var i = 0
                    while (i <= listXDate.size-1) {
                        yVals.add(Entry(listXDate[i].toFloat(), listYData[i].toFloat(), i))
                        i++
                    }
                    val set1: LineDataSet = LineDataSet(yVals, "DataSet 1")
                    set1.color = Color.BLUE
                    set1.lineWidth = 2f
                    set1.circleRadius = 3f
                    lineChart.setScaleEnabled(true);
                    set1.valueTextSize = 0f
                    set1.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set1.setDrawFilled(true)
                    set1.setDrawCircleHole(true)
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
                    lineChart.getXAxis().setDrawLabels(true)
                    // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                    lineChart.axisLeft.setDrawGridLines(false)
                    lineChart.xAxis.setDrawGridLines(false)
                    lineChart.axisRight.setDrawGridLines(false)
                    lineChart.data.isHighlightEnabled = false
                    val xAxis: XAxis = lineChart.xAxis
                    xAxis.isEnabled = true

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
                            return "" + value.toInt()
                        }
                    }
                    xAxis.valueFormatter = vf
                    yAxis.valueFormatter = vf2
                    xAxis.labelCount = listXDate.size
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







