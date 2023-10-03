package ru.netology.nework.domain.model

data class StateModel(
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
)
