package com.example.myfitneesnote


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.adapters.SwipeToDelete
import com.example.myfitneesnote.adapters.TrainingItemAdapter
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.*
import com.vivekkaushik.datepicker.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_add_workout.*
import kotlinx.android.synthetic.main.activity_training.*
import java.util.*
import kotlin.collections.ArrayList

class WorkoutsActivity : BaseActivity() {
    private lateinit var trainingItemAdapter : TrainingItemAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    var currentDate = ""
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        window.navigationBarColor = android.R.color.white
        //fullscreen()
        print( "===============Currentdate===========" + getCurrentDate().toString())
        setupActionBar()
        btnback_training.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btnback_training, "trainingsBtn")
            startActivity(intent, options.toBundle())
            finish()}

        recyclerView = findViewById(R.id.rv_trainings_list)
        toolBar_workouts_activity.elevation = 0f
        recyclerView?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);

                if(toolBar_workouts_activity == null) {
                    return;
                }

                if(!recyclerView.canScrollVertically(-1)) {
                    // we have reached the top of the list
                    toolBar_workouts_activity.elevation = 0f
                } else {
                    // we are not at the top yet
                    toolBar_workouts_activity.elevation = 50f
                }
            }
        })
        recyclerView.layoutManager= LinearLayoutManager(this)
        val lac = LayoutAnimationController(AnimationUtils.loadAnimation(this,R.anim.slide_in_animation))
        recyclerView.startLayoutAnimation()
                db = FirebaseFirestore.getInstance()
                val query : Query = db.collection(Constant.USERS)
                    .document(getCurrentUserID())
                    .collection("Workouts")
                    .orderBy("date",Query.Direction.DESCENDING)

                val fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
                    .setQuery(query, Workout::class.java)
                    .build()
                trainingItemAdapter = TrainingItemAdapter(fireStoreRecyclerOption)

                recyclerView.adapter= trainingItemAdapter

                lac.delay = 0.20f
                lac.order = LayoutAnimationController.ORDER_NORMAL
                recyclerView.layoutAnimation = lac

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



    @JvmName("getCurrentDate1")
    fun getCurrentDate() : String{
        return  currentDate
    }
    private fun getTrainingsFromFireStore(d :String){

    }
    override fun onStart() {
        super.onStart()
        trainingItemAdapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        trainingItemAdapter.stopListening()
    }
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