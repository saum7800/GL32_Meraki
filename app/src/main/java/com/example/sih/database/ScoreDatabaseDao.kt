package com.example.sih.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface ScoreDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(studentScore : StudentScore)

    @Delete
    fun delete(studentScore: StudentScore)

    @Query("SELECT * FROM score_history_table WHERE date= :date")
    fun getScoreByDate(date: String) : List<StudentScore>

    @Query("SELECT DISTINCT date FROM score_history_table")
    fun getDates(): Cursor

    @Query("SELECT DISTINCT  name FROM score_history_table")
    fun getNames(): Cursor

    @Query("SELECT * FROM score_history_table WHERE name= :student")
    fun getScoreByStudent(student : String) : List<StudentScore>

    @Query("DELETE FROM score_history_table")
    fun clear()

}