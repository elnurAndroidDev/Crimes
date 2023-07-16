package com.isayevapps.crimes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Crime(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val date: Date = Date(),
    val solved: Boolean = false,
    val suspect: String = "",
    val photoFileName: String? = null
)
