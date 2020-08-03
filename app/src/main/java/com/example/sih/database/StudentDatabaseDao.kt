package com.example.sih.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StudentDatabaseDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(studentData: StudentData)

   @Query("SELECT * FROM student_history_table WHERE id LIKE :name")
    fun getSessionByStudents(name:String) : LiveData<List<StudentData>>

    @Query("SELECT * FROM student_history_table")
    fun getIds(): MutableList<StudentData>


}