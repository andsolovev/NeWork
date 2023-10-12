package ru.netology.nework.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostRemoteKeyEntity(
    @PrimaryKey
    val type: KeyType,
    val key: Int,
) {
    enum class KeyType {
        AFTER,
        BEFORE
    }
}
