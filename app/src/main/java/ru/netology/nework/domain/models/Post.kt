package ru.netology.nework.domain.models

data class Post(
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String,
    val authorAvatar: String? = "",
    val authorJob: String? = "",
    val content: String,
    val published: String = "",
    val coords: Coordinates? = null,
    val link: String? = null,
    val likeOwnerIds: Set<Int>? = emptySet(),
    val mentionIds: Set<Int>? = emptySet(),
    val mentionedMe: Boolean = false,
    val likedByMe: Boolean = false,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
)
