package com.adira.signmaster.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adira.signmaster.data.room.entity.StudyEntity

@Dao
interface StudyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudy(study: StudyEntity)

    @Delete
    suspend fun deleteStudy(study: StudyEntity)

    @Query("SELECT * FROM studies")
    suspend fun getAllStudies(): List<StudyEntity>

    @Query("SELECT * FROM studies WHERE id = :id LIMIT 1")
    suspend fun getStudyById(id: Int): StudyEntity?

}







