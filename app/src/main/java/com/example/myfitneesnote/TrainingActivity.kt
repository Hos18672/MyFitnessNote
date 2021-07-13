package com.example.myfitneesnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.adapters.TrainingItemAdapter
import com.example.myfitneesnote.model.Workout
import kotlinx.android.synthetic.main.activity_training.*

class TrainingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
    }
    fun populateWorkoutListToUi(woukoutList: ArrayList<Workout>){
        if(woukoutList.size > 0 ){
            rv_trainings_list.visibility= View.VISIBLE
            tv_no_training_available.visibility = View.GONE
            rv_trainings_list.layoutManager = LinearLayoutManager(this)
            rv_trainings_list.setHasFixedSize(true)

            val adapter = TrainingItemAdapter(this, woukoutList)
            rv_trainings_list.adapter = adapter
        }else{
            rv_trainings_list.visibility= View.GONE
            tv_no_training_available.visibility = View.VISIBLE
        }
    }
}