package com.example.myfitneesnote


import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.adapters.TrainingItemAdapter
import com.example.myfitneesnote.model.Workout
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_muskel_group.*
import kotlinx.android.synthetic.main.activity_muskel_group.toolBar_muscle_gruppe_activity
import kotlinx.android.synthetic.main.activity_training.*
import kotlinx.android.synthetic.main.item_training.*

class TrainingActivity : BaseActivity() {

    private lateinit var list : ArrayList<Workout>
    private lateinit var trainingItemAdapter : TrainingItemAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var workout: Workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        fullscreen()
        setupActionBar()
        btnback_training.setOnClickListener { onBackPressed() }
        recyclerView = findViewById(R.id.rv_trainings_list)
        list = arrayListOf()
       // trainingItemAdapter = TrainingItemAdapter(list)       recyclerView.adapter = trainingItemAdapter
        getTrainingsFromFireStore2()
    }

/*    fun getTrainingsFromFireStore(){

        db = FirebaseFirestore.getInstance()
        db.collection("Trainings").orderBy("breakTime", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for(dc: DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            list.add(dc.document.toObject(Workout::class.java))
                        }
                    }

                    trainingItemAdapter.notifyDataSetChanged()
                }
            })
    }*/
    fun getTrainingsFromFireStore2(){

        db = FirebaseFirestore.getInstance()
        var query : Query = db.collection("Trainings").orderBy("date", Query.Direction.ASCENDING)
        var fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
            .setQuery(query, Workout::class.java)
            .build()
        trainingItemAdapter = TrainingItemAdapter(fireStoreRecyclerOption)

        recyclerView.layoutManager= LinearLayoutManager(this)

        recyclerView.adapter= trainingItemAdapter
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