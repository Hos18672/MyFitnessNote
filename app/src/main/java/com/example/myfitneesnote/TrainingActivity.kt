package com.example.myfitneesnote


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.ChatLogActivity.Companion.TAG

import com.example.myfitneesnote.adapters.SwipeToDelete
import com.example.myfitneesnote.adapters.TrainingItemAdapter
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
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
        btnback_training.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        recyclerView = findViewById(R.id.rv_trainings_list)
        //list = arrayListOf()
        // trainingItemAdapter = TrainingItemAdapter(list)       recyclerView.adapter = trainingItemAdapter
        getTrainingsFromFireStore2()
    }

    fun getTrainingsFromFireStore2(){
        db = FirebaseFirestore.getInstance()
        var query : Query = db.collection("Trainings")
        query.orderBy("date", Query.Direction.DESCENDING)
         var fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
            .setQuery(query.whereEqualTo("user_id",getCurrentUserID()), Workout::class.java)
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
        trainingItemAdapter.notifyDataSetChanged()
    }

    fun getTrainingsFromFireStore3(){
        db = FirebaseFirestore.getInstance()
        var query : Query = db.collection("Trainings")
        query.orderBy("date", Query.Direction.DESCENDING)
        query.get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    val list: MutableList<String> = ArrayList()
                    for (document in task.result!!) {
                        if(document.get("user_id").toString() == getCurrentUserID()) {
                            var fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
                                .setQuery(query.whereEqualTo("user_id",getCurrentUserID()), Workout::class.java)
                                .build()
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            })

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
        trainingItemAdapter.notifyDataSetChanged()
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