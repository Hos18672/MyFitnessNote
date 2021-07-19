package com.example.myfitneesnote.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R
import com.example.myfitneesnote.model.Workout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_training.view.*

class TrainingItemAdapter(options: FirestoreRecyclerOptions<Workout>) : FirestoreRecyclerAdapter<Workout,TrainingItemAdapter.MyViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{

        val itemView = LayoutInflater.from(parent.context).inflate((R.layout.item_training), parent, false)

        return  MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, training: Workout) {

        //holder.user_id.text   = training.user_id
        holder.gymName.text   = training.GymType
        holder.muskelName.text= training.MuskelName
        holder.sets.text      = "${training.set} x"
        holder.weight.text    = "${training.weight } kg"
        holder.repeat.text    = "${training.repeat} x"
        holder.breakTime.text = "${training.BreakTime} min"
        holder.date.text      = "${training.date}"
    }
     fun deleteItem(i: Int){
        snapshots.getSnapshot(i).reference.delete()
        notifyDataSetChanged()
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        //  val user_id    =   itemView.tv_user_id
        val gymName    =   itemView.tv_GymName
        val muskelName =   itemView.tv_Muscle
        val sets       =   itemView.tv_Sets
        val weight     =   itemView.tv_weight
        val repeat     =   itemView.tv_repeat
        val breakTime  =   itemView.tv_break
        val date       =   itemView.tv_date
    }



}





