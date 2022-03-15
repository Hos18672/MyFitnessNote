package com.example.myfitneesnote.adapters


import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.R.drawable.*
import com.example.myfitneesnote.model.Workout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_training.view.*
import java.time.LocalDate
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TrainingItemAdapter(options: FirestoreRecyclerOptions<Workout>?) : FirestoreRecyclerAdapter<Workout,TrainingItemAdapter.MyViewHolder>(
    options as FirestoreRecyclerOptions<Workout>
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate((layout.item_training), parent, false)
        return  MyViewHolder(itemView)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, training: Workout) {
            holder.gymName.text = training.GymType
            holder.muskelName.text = training.MuskelName
            holder.sets.text = "${training.set} x"
            holder.weight.text = "${training.weight} kg"
            holder.repeat.text = "${training.repeat} x"
            holder.breakTime.text = "${training.BreakTime} min"
            holder.date.text = training.currentDateTime
            holder.note.text = training.note
            holder.workoutName.text = training.workoutName
          val isExpandable : Boolean  = training.expandable
           holder.expandable_layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        var v = 0
        if(isExpandable)
        {
            v =  View.VISIBLE
            holder.arrow.setBackgroundResource(arrow_up)
        }
        else
        {
            v= View.GONE
            holder.arrow.setBackgroundResource(arrow_down)
        }
        holder.expandable_layout.visibility = v
           holder.ll.setOnClickListener {
                training.expandable = !training.expandable
                notifyItemChanged(position)
           }

       if (dateFormatter(training.currentDateTime)!! > getCurrentDate() ) {
            holder.image.setBackgroundResource(todo)
        }else if (dateFormatter(training.currentDateTime)!! == getCurrentDate()){
            holder.image.setBackgroundResource(notdone)
        }
        else{
           holder.image.setBackgroundResource(done)
       }
    }


    private fun dateFormatter(date: String): ChronoLocalDate? {
        val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return currentDate
    }

    private  fun getCurrentDate() : ChronoLocalDate? {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var currentDate = "${year}-${month + 1}-${day}"
        return  dateFormatter(currentDate)
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
        val arrow      : ImageView = itemView.item_arrow
        val note      : TextView = itemView.tv_note
        val workoutName  : TextView = itemView.tv_workoutName
        val ll        : LinearLayout = itemView.RowLL
        val expandable_layout : LinearLayout = itemView.rl_note
    }
}







