package com.isayevapps.crimes.models

import java.util.Date

data class Crime(
    val id: Int = 0,
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false
)
