package com.example.myfitneesnote


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.adapters.TrainingItemAdapter
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_training.*

class TrainingActivity : BaseActivity() {

    private lateinit var list : ArrayList<Workout>
    private lateinit var trainingItemAdapter : TrainingItemAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        recyclerView = findViewById(R.id.rv_trainings_list)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        list = arrayListOf()

        trainingItemAdapter = TrainingItemAdapter(list)

        recyclerView.adapter = trainingItemAdapter

        getTrainingsFromFireStore()
    }

    fun getTrainingsFromFireStore(){

        db = FirebaseFirestore.getInstance()

        db.collection("Trainings")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
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
    }
}