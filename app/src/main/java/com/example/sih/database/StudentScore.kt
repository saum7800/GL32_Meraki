package com.example.sih.database

import androidx.room.*

@Entity(tableName = "score_history_table")
data class StudentScore(

    @PrimaryKey var id : String,

    @TypeConverters(MyConverters::class)
    @ColumnInfo(name = "student_score")
    var studentScore : String?

)
       /*
        scoreRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val score = snapshot.getValue<Long>()
                if (score != null) {
                    scores.add(score)
                }
                //Log.d(TAG, "Value is score : $score")
            }

        })*/
class MyConverters{

    @TypeConverter
    fun fromStringToList(value: String?) : List<Double>?{
        return value?.split(",")?.map { it.trim().toDouble() }
    }

    @TypeConverter
    fun listToString(list : List<Double>?) : String?{

        return list?.joinToString(",")
    }
}