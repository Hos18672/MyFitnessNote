package com.example.myfitneesnote.adapters


import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R
import com.example.myfitneesnote.model.Workout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.extensions.LayoutContainer
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
import kotlinx.android.synthetic.main.item_training_main.view.tv_workoutName


class TrainingItemAdapterMain(options: FirestoreRecyclerOptions<Workout>) : LayoutContainer, FirestoreRecyclerAdapter<Workout,TrainingItemAdapterMain.MyViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate((R.layout.item_training_main), parent, false)
        return  MyViewHolder(itemView)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, training: Workout) {
        holder.gymName.text    =    training.GymType
        holder.muskelName.text =    training.MuskelName
        holder.sets.text       = "${training.set} x"
        holder.weight.text     = "${training.weight} kg"
        holder.repeat.text     = "${training.repeat} x"
        holder.breakTime.text  = "${training.BreakTime} min"
        holder.date.text       =    training.currentDateTime
        holder.note.text = training.note
        holder.workoutName.text = training.workoutName

        val isExpandable : Boolean  =  training.expandable
        if (isExpandable){
            holder.expandable_layout.visibility = VISIBLE
            holder.expandable_Container.visibility= GONE
        }else{
            holder.expandable_layout.visibility = GONE
            holder.expandable_Container.visibility= VISIBLE
        }
        holder.ll.setOnClickListener {
            training.expandable = !training.expandable
            notifyItemChanged(position)
        }


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
        val workoutName  : TextView = itemView.tv_workoutName
        val expandable_layout : LinearLayout = itemView.rl_note
        val expandable_Container : ConstraintLayout = itemView.container
    }

    override val containerView
        get() = TODO("Not yet implemented")


}







