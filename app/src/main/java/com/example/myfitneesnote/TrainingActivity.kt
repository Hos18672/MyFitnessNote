package com.example.myfitneesnote


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.view.postOnAnimationDelayed
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.adapters.SwipeToDelete
import com.example.myfitneesnote.adapters.TrainingItemAdapter
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_training.*
import com.example.myfitneesnote.R.anim.layout_animation

class TrainingActivity : BaseActivity() {
    private lateinit var trainingItemAdapter : TrainingItemAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        fullscreen()
        setupActionBar()
        btnback_training.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
        recyclerView = findViewById(R.id.rv_trainings_list)
        getTrainingsFromFireStore()
        recyclerView.startLayoutAnimation()


    }
    private fun getTrainingsFromFireStore(){
        db = FirebaseFirestore.getInstance()
        val query : Query = db.collection(Constant.USERS)
            .document(getCurrentUserID())
            .collection(Constant.TRAININGS)
            .orderBy("date",Query.Direction.DESCENDING)
        val fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
            .setQuery(query, Workout::class.java)
            .build()
        trainingItemAdapter = TrainingItemAdapter(fireStoreRecyclerOption)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter= trainingItemAdapter
        val lac = LayoutAnimationController(AnimationUtils.loadAnimation(this,R.anim.slide_out_left))
        lac.delay = 0.20f
        lac.order = LayoutAnimationController.ORDER_NORMAL
        recyclerView.layoutAnimation = lac

        val item = object : SwipeToDelete(this, 0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
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
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_workouts_activity)
        val actionBar = supportActionBar
        if(actionBar!=null) {
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