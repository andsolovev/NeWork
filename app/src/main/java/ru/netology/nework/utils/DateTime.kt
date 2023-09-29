package ru.netology.nework.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

fun formatDateTime(dateTime: String): String? {
    return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
        ?.let { formatter.format(it) }
}