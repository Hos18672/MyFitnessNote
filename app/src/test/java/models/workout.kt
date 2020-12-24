package models

import java.util.*

class Workout {
       var training : ArrayList<Training> = ArrayList<Training>()
    fun getWorkout(id :Int): Training {
        if (id>= 0 && training.size > id){
            return training.get(id)
        }
        else{ println("workout not founded")
        }
        return training.get(id)
    }
    fun add(t :Training)
    {
        if(t != null){
            if (!training.contains(t)) {
                if (training.size< 100) {
                    training.add(t)
                }else{
                    println("workout not founded1")
                }
            }else{
                println("workout not founded2")
            }
        }else{
            println("workout not founded3")
        }
    }
    override fun toString(): String {
        var gesamt = ""
        for (m in training)
            gesamt += m.toString() + '\n'
        return gesamt
    }

}