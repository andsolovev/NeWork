package ru.netology.nework.data.database.entity

import androidx.room.Entity
import ru.netology.nework.domain.model.Attachment
import ru.netology.nework.domain.model.AttachmentType

@Entity
data class AttachmentEntity(
    val url: String,
    val type: AttachmentType
) {
    fun toDto() = Attachment(url, type)

    companion object {
        fun fromDto(dto: Attachment?): AttachmentEntity? {
            return if (dto != null) AttachmentEntity(dto.url, dto.type) else null
        }
    }
}
