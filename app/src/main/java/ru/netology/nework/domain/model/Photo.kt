package ru.netology.nework.domain.model

import android.net.Uri
import java.io.File

data class Photo(
    val uri: Uri? = null,
    val file: File? = null
)
