package com.example.sih.database

import androidx.room.*

@Entity(tableName = "score_history_table")
data class StudentScore(

    @PrimaryKey var id : String,

    @TypeConverters(MyConverters::class)
    @ColumnInfo(name = "student_score")
    var studentScore : String?

)

class MyConverters{

    @TypeConverter
    fun fromStringToList(value: String?) : List<Double>?{
        if(value==null){
            return listOf()
        }
        return value.split(",").map { it.trim().toDouble() }
    }

    @TypeConverter
    fun listToString(list : List<Double>?) : String?{

        return list?.joinToString(",")
    }
}