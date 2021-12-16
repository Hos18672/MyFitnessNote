package com.example.myfitneesnote.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.R.drawable.bench_press
import com.example.myfitneesnote.R.drawable.workout_home
import com.example.myfitneesnote.model.Workout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_training.view.*

class TrainingItemAdapter(options: FirestoreRecyclerOptions<Workout>) : FirestoreRecyclerAdapter<Workout,TrainingItemAdapter.MyViewHolder>(options){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate((layout.item_training), parent, false)
        return  MyViewHolder(itemView)
    }
    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, training: Workout) {
            holder.gymName.text    = training.GymType
            holder.muskelName.text = training.MuskelName
            holder.sets.text       = "${training.set} x"
            holder.weight.text     = "${training.weight} kg"
            holder.repeat.text     = "${training.repeat} x"
            holder.breakTime.text  = "${training.BreakTime} min"
            holder.date.text       = training.currentDateTime
        if (training.GymType == "HOME") {
            holder.image.setImageResource(workout_home)
        }else{
            holder.image.setImageResource(bench_press)
        }
    }
     fun deleteItem(i: Int){
        snapshots.getSnapshot(i).reference.delete()
        notifyDataSetChanged()
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val gymName   : TextView = itemView.tv_GymName
        val muskelName: TextView = itemView.tv_Muscle
        val sets      : TextView = itemView.tv_Sets
        val weight    : TextView = itemView.tv_weight
        val repeat    : TextView = itemView.tv_repeat
        val breakTime : TextView = itemView.tv_break
        val date      : TextView = itemView.tv_date
        val image      : ImageView = itemView.workout_image
    }
}







