package com.example.sih.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_history_table")
data class StudentData(

    @PrimaryKey var id : String,

    @ColumnInfo(name = "drowsy")
    var drowsy : Int ,

    @ColumnInfo(name = "inattentive")
    var inattentive : Int ,

    @ColumnInfo(name = "attentive")
    var attentive : Int ,

    @ColumnInfo(name = "interactive")
    var interactive : Int
)