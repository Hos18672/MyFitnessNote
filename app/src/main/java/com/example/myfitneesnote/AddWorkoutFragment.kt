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
import com.example.myfitneesnote.adapters.TrainingItemAdapterAdd
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.time.LocalDate
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class AddWorkoutFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var trainingItemAdapter : TrainingItemAdapterAdd
    private lateinit var recyclerView: RecyclerView
    private lateinit var _view: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _view = inflater.inflate(R.layout.trainings_fragment, container, false)
        getTrainingsFromFireStore()
        recyclerView = _view.findViewById(R.id.recyclerView_add);
        recyclerView.setHasFixedSize(true);
        return _view
    }

    private fun dateFormatter(date: String): ChronoLocalDate? {
        val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"))

        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return currentDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): Timestamp {
        val c = Calendar.getInstance()
        val currentMonth = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        val months = currentMonth.toString()
        val day = c.get(Calendar.DAY_OF_MONTH)
        return Timestamp(Date(year, months.toInt(), day).time / 1000, 0)
    }
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
        trainingItemAdapter = TrainingItemAdapterAdd(fireStoreRecyclerOption)
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
    fun getCurrentUserID() :String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }



}