package com.example.myfitneesnote

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import com.example.myfitneesnote.ChatLogActivity.Companion.TAG
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.model.User
import com.example.myfitneesnote.utils.Constant
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_layout.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd - HH:mm")
    val database = FirebaseDatabase.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setupLineChartData2()
        nav_view.setNavigationItemSelectedListener(this)
        // FirestoreClass().loginUser(this)
        userData()
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

        getDataFromFireStore()
        fullscreen()
        onClick()
        updateNavigationUserDetails()

    }

    private fun userData() {
        val uid = FirebaseAuth.getInstance().uid
        var MainUsername : TextView = findViewById(R.id.tv_main_username)
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").getValue(String::class.java)
                MainUsername.setText(username)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun onClick() {
        var main_menu: ImageView = findViewById(id.main_menu)
        var add_main: ImageView = findViewById(id.Add_main)
        var chat_main: ImageView = findViewById(id.chat_main)
        var cv_chart: CardView = findViewById(id.cv_lineChart)
        var main_image: ImageView = findViewById(id.tv_main_profile_image)
        var diagram_main: ImageView = findViewById(id.main_diagramm)
        var cv_lineChart: LineChart = findViewById(id.lineChart)

        main_image.setOnClickListener {
            animate(main_image)
            startActivity(Intent(this,myProfileActivity::class.java))
        }

        cv_lineChart.setOnClickListener {
            animateCV(cv_chart)
            startActivity(Intent(this,TrainingActivity::class.java))
        }
        main_menu.setOnClickListener {
            animate(main_menu)
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        add_main.setOnClickListener {
            animate(add_main)
            startActivity(Intent(this, WorkoutActivity::class.java))
        }
        chat_main.setOnClickListener {
            animate(chat_main)
            startActivity(Intent(this, ChatActivity::class.java))
        }
        diagram_main.setOnClickListener {
            animate(diagram_main)
            startActivity(Intent(this, TrainingActivity::class.java))
        }
    }
    fun animate(btn: ImageView){
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
    fun animateCV(btn: CardView){
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
     private fun updateNavigationUserDetails() {
         val uid = FirebaseAuth.getInstance().uid
         val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
         ref.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 val username = snapshot.child("username").getValue(String::class.java)
                // val imageUri = snapshot.child("image").getValue(String::class.java)
                // Picasso.get().load(imageUri!!).into(main_drawer_profile_photo)
                 tv_username.text = username
             }
             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }
         })
     }

    @SuppressLint("NewApi")
    private fun getDataFromFireStore() {
        var list1 = arrayListOf<String>()
        var list2 = arrayListOf<String>()
        db.collection(Constant.TRAININGS).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    val list: MutableList<String> = ArrayList()
                    for (document in task.result!!) {
                        if(document.get("user_id").toString() == getCurrentUserID()) {
                            val input = document.get("currentDateTime").toString()
                            val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd - HH:mm")
                            val date: Date = inputFormatter.parse(input)

                            val outputFormatter: DateFormat = SimpleDateFormat("MM/dd/yyyy")
                            val output: String = outputFormatter.format(date) // Output : 01/20/2012

                            list1.add(output)
                            list2.add(document.get("set").toString())
                        }
                    }
                    Log.d("--------Test1------", list1.toString())
                    Log.d("--------Test2------", list2.toString())
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            })
    }

    private fun getDateString(time: Long) : String = simpleDateFormat.format(time * 1000L)

    fun setupLineChartData2() {
        var listXDate = arrayListOf<String>()
        var listYData = arrayListOf<String>()
        var yVals = ArrayList<Entry>()
        db.collection(Constant.TRAININGS).orderBy("date").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if(document.get("user_id").toString() == getCurrentUserID()) {
                            val input = document.get("currentDateTime").toString()
                            val inputFormatter: DateFormat = SimpleDateFormat("yyyy/MM/dd - HH:mm")
                            val date: Date = inputFormatter.parse(input)
                            val outputFormatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                            val output: String = outputFormatter.format(date) // Output : 01/20/2012

                            listXDate.add(output.substring(0, output.length - 8).toInt().toString())
                            listYData.add(document.get("set").toString())
                        }
                    }
                    var i = 0
                    while( i < listXDate.size){
                        yVals.add(Entry(listXDate[i].toFloat(),listYData[i].toFloat(),i))
                        i++
                    }

                    val set1: LineDataSet
                    set1 = LineDataSet(yVals, "DataSet 1")

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
                    lineChart.setData(data)
                    lineChart.description.isEnabled = false
                    lineChart.legend.isEnabled = false
                    //lineChart.setDrawGridBackground()
                    lineChart.xAxis.labelCount = 11
                    lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                    val yAxisRight: YAxis =   lineChart.getAxisRight()
                    yAxisRight.isEnabled = false
                    lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM


                    //----------------------------------------------------------------------

                    lineChart.getAxisLeft().setDrawLabels(false)
                    lineChart.getAxisRight().setDrawLabels(false)
                    //lineChart.getXAxis().setDrawLabels(true)
                    // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                    // https://stackoverflow.com/questions/31263097/mpandroidchart-hide-background-grid
                    lineChart.getAxisLeft().setDrawGridLines(false)
                    lineChart.getXAxis().setDrawGridLines(false)
                    lineChart.getAxisRight().setDrawGridLines(false)
                    lineChart.getData().setHighlightEnabled(false)

                    val xAxis: XAxis = lineChart.getXAxis()
                    xAxis.isEnabled = true

                    val yAxis: YAxis = lineChart.getAxisLeft()
                    yAxis.isEnabled = false

                    val yAxis2: YAxis = lineChart.getAxisRight()
                    yAxis2.isEnabled = false

                    lineChart.setDrawBorders(false)
                    lineChart.setDrawGridBackground(false)

                    lineChart.getLegend().setEnabled(false)
                    // no description text
                    // no description text
                    lineChart.getDescription().setEnabled(false)

                    // enable touch gestures

                    // enable touch gestures
                    lineChart.setTouchEnabled(true)

                    // enable scaling and dragging

                    // enable scaling and dragging
                    lineChart.setDragEnabled(false)
                    lineChart.setScaleEnabled(false)
                    // mChart.setScaleXEnabled(true);
                    // mChart.setScaleYEnabled(true);

                    // if disabled, scaling can be done on x- and y-axis separately
                    // mChart.setScaleXEnabled(true);
                    // mChart.setScaleYEnabled(true);

                    // if disabled, scaling can be done on x- and y-axis separately
                    lineChart.setPinchZoom(false)
                    lineChart.setAutoScaleMinMaxEnabled(true)
                    lineChart.invalidate();
                    // hide legend
                    // hide legend
                    val legend: Legend = lineChart.getLegend()
                    legend.isEnabled = false

                } else {
                    Log.d(ChatLogActivity.TAG, "Error getting documents: ", task.exception)
                }
            })

    }

    fun updateNavigationUserDetails(user: User) {
        var mUserName = user.name
        // The instance of the header view of the navigation view.
        val headerView = nav_view.getHeaderView(0)
        // The instance of the user name TextView of the navigation view.
        val navUsername = headerView.findViewById<TextView>(R.id.tv_username)
        // Set the user name
        navUsername.text = user.name
    }
}


class UserLoged(val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        // load our user image into the picture
        val uri = user.image
        val targetImageView = viewHolder.itemView.main_drawer_profile_photo
        Picasso.get().load(uri).into(targetImageView)
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }


}
class MyFormatter : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        return "ABC"
    }
}







