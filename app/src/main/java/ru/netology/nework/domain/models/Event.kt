package ru.netology.nework.domain.models

data class Event(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates,
    val type: EventType,
    val likeOwnerIds: Set<Int>,
    val likedByMe: Boolean,
    val speakerIds: Set<Int>,
    val participantsIds: Set<Int>,
    val participatedByMe: Boolean,
    val attachment: Attachment?,
    val link: String?,
    val ownedByMe: Boolean,
)

enum class EventType {
    ONLINE, OFFLINE
}
