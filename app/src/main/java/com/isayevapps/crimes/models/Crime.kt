package com.isayevapps.crimes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Crime(
    @PrimaryKey
    val id: Int = 0,
    val title: String = "",
    val date: Date = Date(),
    val isSolved: Boolean = false
)
