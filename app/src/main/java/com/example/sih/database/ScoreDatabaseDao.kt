package com.example.sih.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScoreDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(studentScore : StudentScore)

    @Delete
    fun delete(studentScore: StudentScore)

    @Query("SELECT * FROM score_history_table WHERE date= :date")
    fun getScoreByDate(date: String) : List<StudentScore>

}