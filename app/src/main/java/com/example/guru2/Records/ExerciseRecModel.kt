package com.example.guru2.Records

import androidx.room.Entity

@Entity(tableName = "exercise")
data class ExerciseRecModel(
    val exerName2: String = "",
    var exerDate: String = "",
    val count: String = "",
    val set: String = ""
)