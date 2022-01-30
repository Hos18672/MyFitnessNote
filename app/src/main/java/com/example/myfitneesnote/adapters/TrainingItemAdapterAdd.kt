package com.example.myfitneesnote.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R.*
import com.example.myfitneesnote.model.Workout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_training.view.*
import java.time.LocalDate
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class TrainingItemAdapterAdd(options: FirestoreRecyclerOptions<Workout>) :LayoutContainer, FirestoreRecyclerAdapter<Workout,TrainingItemAdapterAdd.MyViewHolder>(options) {
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate((layout.item_training_new_add), parent, false)
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


     @SuppressLint("NotifyDataSetChanged")
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
     override val containerView
         get() = TODO("Not yet implemented")
 }







