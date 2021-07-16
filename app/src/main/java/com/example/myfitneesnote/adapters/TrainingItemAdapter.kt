package com.example.myfitneesnote.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R
import com.example.myfitneesnote.model.Workout

class TrainingItemAdapter( private var list : ArrayList<Workout>)
    : RecyclerView.Adapter<TrainingItemAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val itemView = LayoutInflater.
        from(parent.context)
            .inflate(
                (R.layout.item_training),
                parent,
            false)
        return  MyViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val training = list[position]
        holder.user_id.text   = training.user_id
        holder.gymName.text   = training.GymType
        holder.muskelName.text= training.MuskelName
        holder.sets.text      = training.set
        holder.weight.text    = training.weight
        holder.repeat.text    = training.repeat
        holder.breakTime.text = training.BreakTime
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val user_id    : TextView =   itemView.findViewById(R.id.tv_user_id)
        val gymName    : TextView =   itemView.findViewById(R.id.tv_GymName)
        val muskelName : TextView =   itemView.findViewById(R.id.tv_Muscle)
        val sets       : TextView =   itemView.findViewById(R.id.tv_Sets)
        val weight     : TextView =   itemView.findViewById(R.id.tv_GymName)
        val repeat     : TextView =   itemView.findViewById(R.id.tv_repeat)
        val breakTime  : TextView =   itemView.findViewById(R.id.tv_break)
    }

}





