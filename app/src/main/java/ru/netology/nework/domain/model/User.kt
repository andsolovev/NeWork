package ru.netology.nework.domain.model

data class User(
    val id: Int = 0,
    val login: String = "",
    val name: String = "",
    val avatar: String? = null
)
