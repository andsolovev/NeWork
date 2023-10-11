package ru.netology.nework.domain.model

data class Job(
    val id: Int = 0,
    val name: String,
    val position: String,
    val start: String,
    val finish: String?,
    val link: String?,
)
