package com.example.myfitneesnote.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R
import com.example.myfitneesnote.model.Workout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_training.view.*
import kotlinx.android.synthetic.main.item_training.view.tv_GymName
import kotlinx.android.synthetic.main.item_training.view.tv_Muscle
import kotlinx.android.synthetic.main.item_training.view.tv_Sets
import kotlinx.android.synthetic.main.item_training.view.tv_break
import kotlinx.android.synthetic.main.item_training.view.tv_date
import kotlinx.android.synthetic.main.item_training.view.tv_repeat
import kotlinx.android.synthetic.main.item_training.view.tv_weight
import kotlinx.android.synthetic.main.item_training_main.view.*
import kotlinx.android.synthetic.main.item_training_main.view.RowLL
import kotlinx.android.synthetic.main.item_training_main.view.rl_note
import kotlinx.android.synthetic.main.item_training_main.view.tv_note

class TrainingItemAdapterMain(options: FirestoreRecyclerOptions<Workout>)
    : FirestoreRecyclerAdapter<Workout,TrainingItemAdapterMain.MyViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{

        val itemView = LayoutInflater.from(parent.context).inflate((R.layout.item_training_main), parent, false)
        return  MyViewHolder(itemView)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, training: Workout) {
            holder.gymName.text    =    training.GymType
            holder.muskelName.text =    training.MuskelName
            holder.sets.text       = "${training.set} x"
            holder.weight.text     = "${training.weight} kg"
            holder.repeat.text     = "${training.repeat} x"
            holder.breakTime.text  = "${training.BreakTime} min"
            holder.date.text       =    training.currentDateTime
            holder.note.text = training.note

            val isExpandable : Boolean  = training.expandable
            holder.expandable_layout.visibility = if(isExpandable) View.VISIBLE else View.GONE
            holder.ll.setOnClickListener {
                training.expandable = !training.expandable
                notifyItemChanged(position)
            }
/*
        if (training.GymType == "HOME") {
            holder.image.setImageResource(R.drawable.workout_home)
        }else{
            holder.image.setImageResource(R.drawable.bench_press)
        }*/
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val gymName   : TextView = itemView.tv_GymName
        val muskelName: TextView = itemView.tv_Muscle
        val sets      : TextView = itemView.tv_Sets
        val weight    : TextView = itemView.tv_weight
        val repeat    : TextView = itemView.tv_repeat
        val breakTime : TextView = itemView.tv_break
        val date      : TextView = itemView.tv_date
        val image     : ImageView = itemView.main_workout_image
        val note      : TextView = itemView.tv_note
        val ll        : LinearLayout = itemView.RowLL
        val expandable_layout : LinearLayout = itemView.rl_note
    }

}





