package com.adira.signmaster.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "studies")
data class StudyEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val icon_url: String,
    val saved: Boolean
)




