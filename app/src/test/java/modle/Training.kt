package modle

 open abstract class Training : Comparable<Training> {
    var id :Int =0
    var name :String? = null
    var type :String? = null
    var descript : String? = null

    constructor(id: Int, name: String?, type: String?, descript: String?) {
        this.id = id
        this.name = name
        this.type = type
        this.descript = descript
    }
     override fun toString(): String {
         return "Training(id= $id, name= $name, type= $type, descript= $descript)"
     }
     class TypeComparator : Comparator<Training?> {
         override fun compare(o1: Training?, o2: Training?): Int {
             return o2?.type?.let { o1!!.type!!.compareTo(it) }!!
         }
     }
     class IdComparator : Comparator<Training?> {
         override fun compare(m1: Training?, m2: Training?): Int {
             return m1!!.id - m2!!.id
         }
     }
 }
