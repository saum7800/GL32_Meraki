package com.example.sih.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_history_table")
data class StudentScore(

    @PrimaryKey val id : String,

    @ColumnInfo(name = "name")
    val name : String,

    @ColumnInfo(name = "score")
    var score : Double = 0.0,

    @ColumnInfo(name = "date")
    val date : String
)