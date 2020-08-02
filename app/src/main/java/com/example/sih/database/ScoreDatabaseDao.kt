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

    @Query("SELECT student_score FROM score_history_table WHERE id = :date ")
    fun getScoreByDate(date: String) : String

    @Query("SELECT id  FROM score_history_table")
    fun getDates(): Cursor

    @Query("SELECT id  FROM score_history_table  WHERE id LIKE :date")
    fun getSimilarDates(date: String): Cursor

    @Query("DELETE FROM score_history_table")
    fun clear()

}