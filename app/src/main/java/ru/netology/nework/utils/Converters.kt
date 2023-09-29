package ru.netology.nework.utils

import androidx.room.TypeConverter
import ru.netology.nework.domain.models.AttachmentType
import ru.netology.nework.domain.models.EventType

class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)
    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
    @TypeConverter
    fun toEventType(value: String) = enumValueOf<EventType>(value)
    @TypeConverter
    fun fromEventType(value: EventType) = value.name
    @TypeConverter
    fun fromSet(value: Set<Int>): String = value.joinToString()
    @TypeConverter
    fun toSet(value: String): Set<Int> = if (value.isBlank()) {
        emptySet()
    } else value.split(", ").map { it.toInt() }.toSet()
}