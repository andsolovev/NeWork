package ru.netology.nework.data.database.entity

import androidx.room.Entity
import ru.netology.nework.domain.models.EventType

@Entity
data class EventTypeEntity(
    val eventType: String,
) {
    fun toDto() = EventType.valueOf(eventType)

    companion object {
        fun fromDto(dto: EventType) = EventTypeEntity(dto.name)
    }
}