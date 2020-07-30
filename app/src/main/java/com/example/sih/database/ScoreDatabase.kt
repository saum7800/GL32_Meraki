package com.example.sih.database

import android.content.Context
import androidx.room.*

@Database(entities = [StudentScore :: class], version = 1, exportSchema = false)
abstract class ScoreDatabase : RoomDatabase() {

    abstract val scoreDatabaseDao : ScoreDatabaseDao

    companion object{


    @Volatile
    private var INSTANCE : ScoreDatabase? = null

    fun getInstance(context: Context) : ScoreDatabase {

        kotlin.synchronized(this) {

            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreDatabase::class.java,
                    "score_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
    }

}