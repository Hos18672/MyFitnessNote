package com.example.myfitneesnote


import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myfitneesnote.adapters.SwipeToDelete
import com.example.myfitneesnote.adapters.TrainingItemAdapter
import com.example.myfitneesnote.model.Workout
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_training.*

class TrainingActivity : BaseActivity() {

    private lateinit var list : ArrayList<Workout>
    private lateinit var trainingItemAdapter : TrainingItemAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var workout : Workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        fullscreen()
        setupActionBar()
        btnback_training.setOnClickListener { onBackPressed() }
        recyclerView = findViewById(R.id.rv_trainings_list)
        //list = arrayListOf()
        // trainingItemAdapter = TrainingItemAdapter(list)       recyclerView.adapter = trainingItemAdapter
        getTrainingsFromFireStore2()
    }

    fun getTrainingsFromFireStore2(){

        db = FirebaseFirestore.getInstance()
        var query : Query = db.collection("Trainings").orderBy("date", Query.Direction.DESCENDING)
        var fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
            .setQuery(query, Workout::class.java)
            .build()
        trainingItemAdapter = TrainingItemAdapter(fireStoreRecyclerOption)

        recyclerView.layoutManager= LinearLayoutManager(this)

        recyclerView.adapter= trainingItemAdapter

        val Item = object : SwipeToDelete(this, 0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               trainingItemAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper= ItemTouchHelper(Item)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    override fun onStart() {
        super.onStart()
        trainingItemAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        trainingItemAdapter!!.stopListening()
    }
    private fun setupActionBar() {
        setSupportActionBar(toolBar_workouts_activity)
        var actionBar = supportActionBar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp)
        }
        toolBar_workouts_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}