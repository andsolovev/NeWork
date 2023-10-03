package ru.netology.nework.presentation.adapter.onInteractionListener

import ru.netology.nework.domain.model.User

interface OnUserInteractionListener {
    fun onOpenProfile(user: User)
}