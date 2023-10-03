package ru.netology.nework.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nework.domain.model.Event

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    @Embedded
    val coords: CoordinatesEntity?,
    @Embedded
    val type: EventTypeEntity,
    val likeOwnerIds: Set<Int>?,
    val likedByMe: Boolean,
    val speakerIds: Set<Int>?,
    val participantsIds: Set<Int>,
    val participatedByMe: Boolean,
    @Embedded
    val attachment: AttachmentEntity?,
    val link: String? = null,
    val ownedByMe: Boolean,
) {
    fun toDto() = Event(
        id = id,
        authorId = authorId,
        author = author,
        authorAvatar = authorAvatar,
        authorJob = authorJob,
        content = content,
        datetime = datetime,
        published = published,
        coords = coords?.toDto(),
        type = type.toDto(),
        likeOwnerIds = likeOwnerIds,
        likedByMe = likedByMe,
        speakerIds = speakerIds,
        participantsIds = participantsIds,
        participatedByMe = participatedByMe,
        attachment = attachment?.toDto(),
        link = link,
        ownedByMe = ownedByMe
    )
    companion object {
        fun fromDto(dto: Event) =
            EventEntity(
                id = dto.id,
                authorId = dto.authorId,
                author = dto.author,
                authorAvatar = dto.authorAvatar,
                authorJob = dto.authorJob,
                content = dto.content,
                datetime = dto.datetime,
                published = dto.published,
                coords = CoordinatesEntity.fromDto(dto.coords),
                type = EventTypeEntity.fromDto(dto.type),
                likeOwnerIds = dto.likeOwnerIds,
                likedByMe = dto.likedByMe,
                speakerIds = dto.speakerIds,
                participantsIds = dto.participantsIds,
                participatedByMe = dto.participatedByMe,
                attachment = AttachmentEntity.fromDto(dto.attachment),
                link = dto.link,
                ownedByMe = dto.ownedByMe
            )
    }
}
fun List<EventEntity>.toDto(): List<Event> = map(EventEntity::toDto)
fun List<Event>.toEntity() = map { EventEntity.fromDto(it) }
