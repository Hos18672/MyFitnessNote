package com.example.myfitneesnote.model

class Test {

    object TestSave {
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                val w : Workout = Workout()
                var h : HomeTrainig = HomeTrainig(1,"Body","Home","Only home training")
                var g : Workout =
                    Workout(2,"Body","Gym","Only Gym training")
                w.add(h)
                w.add(g)
                println(w.toString())
                println( " --------------Find workout by ID--------------- \n " +w.getWorkout(1))

            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}