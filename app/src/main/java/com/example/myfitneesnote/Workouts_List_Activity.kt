package com.example.myfitneesnote

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_workouts_list.*
import kotlinx.android.synthetic.main.activity_workouts_list.btn1
import kotlinx.android.synthetic.main.activity_workouts_list.btn2

class Workouts_List_Activity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts_list)
        //recyclerView = findViewById(R.id.rv_trainings_list)
        toolBar_workouts_activity.elevation = 0f
        //fullscreen()
        setupActionBar()
        btn1.setCardBackgroundColor(Color.parseColor("#00AEFF"))
        val trainingsFragment = WorkoutListFragmentToday()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.root_container, trainingsFragment).commit()
        }
        tip()
    }


    @SuppressLint("Range")
    private fun tip() {
        val nightModeFlags: Int = applicationContext.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                btn2.setCardBackgroundColor(Color.parseColor("#26282C"))
                btn3.setCardBackgroundColor(Color.parseColor("#26282C"))
                btn1.setOnClickListener {
                    val trainingsFragment = WorkoutListFragmentToday()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment).commit()
                        btn2.setCardBackgroundColor(Color.parseColor("#26282C"))
                        btn3.setCardBackgroundColor(Color.parseColor("#26282C"))
                        btn1.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                    }

                }

                btn2.setOnClickListener {
                    val trainingsFragment2 = WorkoutListFragmentTomorrow()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment2).commit()
                        btn2.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                        btn1.setCardBackgroundColor(Color.parseColor("#26282C"))
                        btn3.setCardBackgroundColor(Color.parseColor("#26282C"))
                    }

                }

                btn3.setOnClickListener {
                    val trainingsFragment2 = WorkoutListFragmentAll()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment2).commit()
                        btn3.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                        btn1.setCardBackgroundColor(Color.parseColor("#26282C"))
                        btn2.setCardBackgroundColor(Color.parseColor("#26282C"))
                    }
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {

                btn1.setOnClickListener {
                    val trainingsFragment = WorkoutListFragmentToday()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment).commit()
                        btn3.setCardBackgroundColor(Color.WHITE)
                        btn2.setCardBackgroundColor(Color.WHITE)
                        btn1.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                    }

                }

                btn2.setOnClickListener {
                    val trainingsFragment2 = WorkoutListFragmentTomorrow()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment2).commit()

                        btn2.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                        btn1.setCardBackgroundColor(Color.WHITE)
                        btn3.setCardBackgroundColor(Color.WHITE)
                    }

                }
                btn3.setOnClickListener {
                    val trainingsFragment2 = WorkoutListFragmentAll()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.root_container, trainingsFragment2).commit()

                        btn3.setCardBackgroundColor(Color.parseColor("#00AEFF"))
                        btn1.setCardBackgroundColor(Color.WHITE)
                        btn2.setCardBackgroundColor(Color.WHITE)
                    }
                }
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {

            }
        }
    }


//    @SuppressLint("NotifyDataSetChanged")
//    private fun setRecyclerview(){
//        recyclerView.layoutManager= LinearLayoutManager(this)
//        val lac = LayoutAnimationController(AnimationUtils.loadAnimation(this,R.anim.slide_in_animation))
//        recyclerView.startLayoutAnimation()
//        db = FirebaseFirestore.getInstance()
//        val query : Query = db.collection(Constant.USERS)
//            .document(getCurrentUserID())
//            .collection("Workouts")
//            .orderBy("date",Query.Direction.DESCENDING)
//        val fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
//            .setQuery(query, Workout::class.java)
//            .build()
//        trainingItemAdapter = TrainingItemAdapter(fireStoreRecyclerOption)
//        recyclerView.adapter= trainingItemAdapter
//        lac.delay = 0.20f
//        lac.order = LayoutAnimationController.ORDER_NORMAL
//        recyclerView.layoutAnimation = lac
//        val item = object : SwipeToDelete(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                trainingItemAdapter.deleteItem(viewHolder.adapterPosition)
//            }
//        }
//        val itemTouchHelper= ItemTouchHelper(item)
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//        trainingItemAdapter.notifyDataSetChanged()
//        recyclerView.startLayoutAnimation()
//    }
//
//    @JvmName("getCurrentDate1")
//    private fun getCurrentDate() : String{
//        return  currentDate
//    }
//    override fun onStart() {
//        super.onStart()
//        trainingItemAdapter.startListening()
//    }
//    override fun onStop() {
//        super.onStop()
//        trainingItemAdapter.stopListening()
//    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_workouts_activity)
        val actionBar = supportActionBar
        if(actionBar!=null) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_workouts_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    override fun onBackPressed() {
        finish()
    }

}