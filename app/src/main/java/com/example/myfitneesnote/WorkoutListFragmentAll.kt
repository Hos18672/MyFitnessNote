package com.example.myfitneesnote

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.adapters.SwipeToDelete
import com.example.myfitneesnote.adapters.TrainingItemAdapter
import com.example.myfitneesnote.adapters.TrainingItemAdapterAdd
import com.example.myfitneesnote.adapters.WorkoutListMainAdapter
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.time.LocalDate
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class WorkoutListFragmentAll : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var trainingItemAdapter : TrainingItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var _view: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = inflater.inflate(R.layout.workout_list__fragment_all, container, false)
        getTrainingsFromFireStore()
        recyclerView = _view.findViewById(R.id.recyclerView_add);
        recyclerView.setHasFixedSize(true);
        return _view
    }

/*
    private fun getTrainingsFromFireStore(){
        val query : Query = db.collection(Constant.USERS)
            .document(getCurrentUserID())
            .collection("Workouts")
            .orderBy("date", Query.Direction.DESCENDING)
        val fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
            .setQuery(query, Workout::class.java)
            .build()
        recyclerView = _view.findViewById(R.id.recyclerView_add);
        trainingItemAdapter = TrainingItemAdapter(fireStoreRecyclerOption)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.smoothScrollToPosition(0)
        recyclerView.adapter = trainingItemAdapter

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
*/


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTrainingsFromFireStore(){
        val query : Query = db.collection(Constant.USERS)
            .document(getCurrentUserID())
            .collection("Workouts")
            .orderBy("date", Query.Direction.DESCENDING)

        val fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
            .setQuery(query, Workout::class.java)
            .build()


        recyclerView = _view.findViewById(R.id.recyclerView_add);
        trainingItemAdapter = TrainingItemAdapter(fireStoreRecyclerOption)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.smoothScrollToPosition(0)
        recyclerView.adapter = trainingItemAdapter

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


    override fun onStart() {
        super.onStart()
        trainingItemAdapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        trainingItemAdapter.stopListening()
    }
    private fun getCurrentUserID() :String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    private  fun getCurrentDate() : String? {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return  "${year}-${month + 1}-${day}"
    }
    private fun dateFormatter(date: String): ChronoLocalDate? {
        val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return currentDate
    }




}