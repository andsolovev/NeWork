package ru.netology.nework.domain.models

data class Event(
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String = "",
    val authorAvatar: String? = "",
    val authorJob: String? = "",
    val content: String = "",
    val datetime: String = "",
    val published: String = "",
    val coords: Coordinates? = null,
    val type: EventType = EventType.OFFLINE,
    val likeOwnerIds: Set<Int>? = emptySet(),
    val likedByMe: Boolean = false,
    val speakerIds: Set<Int>? = emptySet(),
    val participantsIds: Set<Int> = emptySet(),
    val participatedByMe: Boolean = false,
    val attachment: Attachment? = null,
    val link: String?  = "",
    val ownedByMe: Boolean = false,
)

enum class EventType {
    ONLINE, OFFLINE
}
