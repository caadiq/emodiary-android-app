package com.toy.project.emodiary.view.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeConverter {
    fun stringToDate(date: String, format: String, locale: Locale): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(format, locale)
        return LocalDate.parse(date, formatter)
    }
}