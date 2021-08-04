package com.example.myfitneesnote.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R
import com.example.myfitneesnote.model.Workout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_training.view.*

class TrainingItemAdapterMain(options: FirestoreRecyclerOptions<Workout>)
    : FirestoreRecyclerAdapter<Workout,TrainingItemAdapterMain.MyViewHolder>(options){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate((R.layout.item_training_main), parent, false)
        return  MyViewHolder(itemView)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, training: Workout) {
            holder.gymName.text    = training.GymType
            holder.muskelName.text = training.MuskelName
            holder.sets.text       = "${training.set} x"
            holder.weight.text     = "${training.weight} kg"
            holder.repeat.text     = "${training.repeat} x"
            holder.breakTime.text  = "${training.BreakTime} min"
            holder.date.text       = training.currentDateTime
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
    }
}




