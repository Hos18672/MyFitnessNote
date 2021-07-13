package com.example.myfitneesnote.adapters

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitneesnote.R
import com.example.myfitneesnote.model.Workout
import kotlinx.android.synthetic.main.item_training.view.*

open class TrainingItemAdapter(private val context: Context,
                                   private var list : ArrayList<Workout>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate((R.layout.item_training),
                    parent,
                    false))
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      val model = list[position]
        if(holder is MyViewHolder){
            holder.itemView.tv_GymName.text = model.GymType
            holder.itemView.tv_Muscle.text= model.MuskelName
            holder.itemView.tv_Sets.text = model.set
            holder.itemView.tv_weight.text = model.weight
            holder.itemView.tv_repeat.text= model.repeat
            holder.itemView.tv_break.text = model.Break

            holder.itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }
    interface OnClickListener {
        fun onClick(position: Int, model: Workout)
    }


    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view){



    }

}

