package ru.netology.nework.domain.models

data class Attachment(
    val url: String = "",
    val type: AttachmentType = AttachmentType.IMAGE
)

enum class AttachmentType {
    IMAGE, VIDEO, AUDIO
}
