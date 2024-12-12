package com.adira.signmaster.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adira.signmaster.data.room.dao.StudyDao
import com.adira.signmaster.data.room.entity.StudyEntity


@Database(entities = [StudyEntity::class], version = 1)
abstract class StudyDatabase : RoomDatabase() {
    abstract fun studyDao(): StudyDao

    companion object {
        @Volatile private var INSTANCE: StudyDatabase? = null

        fun getInstance(context: Context): StudyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyDatabase::class.java,
                    "study_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}



