package ru.netology.nework.data.database.entity

import androidx.room.Entity
import ru.netology.nework.domain.models.Coordinates

@Entity
data class CoordinatesEntity(
    val latitude: String,
    val longitude: String
) {

    fun toDto() = Coordinates(latitude, longitude)

    companion object {
        fun fromDto(dto: Coordinates?): CoordinatesEntity? {
            return if (dto != null) CoordinatesEntity(dto.latitude, dto.longitude) else null
        }
    }
}

