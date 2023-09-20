package ru.netology.nework.domain.models

data class Job(
    val id: Int,
    val name: String,
    val position: String,
    val start: String,
    val finish: String?,
    val link: String?,
)
