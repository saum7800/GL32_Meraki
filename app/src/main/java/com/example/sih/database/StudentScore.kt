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
    fun fromStringToList(value: String?) : List<Long>?{
        return value?.split(",")?.map { it.trim().toLong() }
    }

    @TypeConverter
    fun listToString(list : List<Long>?) : String?{

        return list?.joinToString(",")
    }
}