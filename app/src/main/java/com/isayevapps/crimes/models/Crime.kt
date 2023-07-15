package com.isayevapps.crimes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Crime(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var solved: Boolean = false
)
