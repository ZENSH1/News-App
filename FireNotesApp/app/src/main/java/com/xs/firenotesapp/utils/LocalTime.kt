package com.xs.firenotesapp.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun localTime():String{
    val time = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("| dd - MM - yyyy | hh:mm |")
    return time.format(formatter).toString()
}