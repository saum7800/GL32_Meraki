package com.example.sih.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StudentDatabaseDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(studentData: StudentData)

    @Query("SELECT * FROM student_history_table WHERE id LIKE '%'|| :date")
    fun getStudentsBySession(date:String)
}