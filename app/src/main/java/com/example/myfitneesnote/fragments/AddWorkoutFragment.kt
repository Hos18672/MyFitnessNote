package com.example.myfitneesnote.fragments

import android.annotation.SuppressLint
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
import com.example.myfitneesnote.R
import com.example.myfitneesnote.adapters.SwipeToDelete
import com.example.myfitneesnote.adapters.TrainingItemAdapter
import com.example.myfitneesnote.model.Workout
import com.example.myfitneesnote.utils.Constant
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class AddWorkoutFragment: Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var trainingItemAdapter2 : TrainingItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var _view: View
    private lateinit var muskelName :String
    private lateinit var workoutName :String

    companion object {
        const val ARG_NAME = "name"
        const val ARG_NAME2 = "name2"
        fun newInstance(name: String,name2 :String): AddWorkoutFragment {
            val fragment = AddWorkoutFragment()

            val bundle = Bundle().apply {
                putString(ARG_NAME, name)
                putString(ARG_NAME2, name2)
            }
            fragment.arguments = bundle

            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _view = inflater.inflate(R.layout.trainings_fragment, container, false)
        muskelName = arguments?.getString(ARG_NAME).toString()
        workoutName = arguments?.getString(ARG_NAME2).toString()
        recyclerView = _view.findViewById(R.id.recyclerView_add)
        recyclerView.setHasFixedSize(true)
        recyclerView.scrollToPosition(0)
        getTrainingsFromFireStore()
        return _view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getTrainingsFromFireStore(){
        val query : Query = db.collection(Constant.USERS)
            .document(getCurrentUserID())
            .collection("Workouts")
            .whereEqualTo("muskelName",muskelName)
            .whereEqualTo("workoutName",workoutName)
            .orderBy("date", Query.Direction.DESCENDING)
        val fireStoreRecyclerOption : FirestoreRecyclerOptions<Workout> = FirestoreRecyclerOptions.Builder<Workout>()
            .setQuery(query, Workout::class.java)
            .build()
        recyclerView = _view.findViewById(R.id.recyclerView_add)
        trainingItemAdapter2 = TrainingItemAdapter(fireStoreRecyclerOption)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.smoothScrollToPosition(0)
        recyclerView.adapter = trainingItemAdapter2

        val item = object : SwipeToDelete(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                trainingItemAdapter2.deleteItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper= ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        trainingItemAdapter2.notifyDataSetChanged()
        recyclerView.startLayoutAnimation()
    }
    override fun onStart() {
        super.onStart()
        trainingItemAdapter2.startListening()
    }
    override fun onStop() {
        super.onStop()
        trainingItemAdapter2.stopListening()
    }
    private fun getCurrentUserID() :String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }


}