package com.example.guru2.Records

import androidx.room.Entity

@Entity(tableName = "exercisename")
data class ExerciseNameModel(
    val exerciseName: String = ""
)