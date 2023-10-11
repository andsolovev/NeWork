package ru.netology.nework.utils

import android.content.Context
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDateTimeFromUTC(dateTime: String) = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    .format(LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME))

fun formatDateJobFromUTC(date: String?) = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    .format(LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME))

fun pickDate(editText: EditText?, context: Context?, fragmentManager: FragmentManager) {

    val materialDatePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date")
        .build()

    materialDatePicker.addOnPositiveButtonClickListener {
        editText?.setText(dateFormat(materialDatePicker.selection!!))
    }

    materialDatePicker.show(fragmentManager, "Material date picker")
}

private fun dateFormat(date: Long) = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)