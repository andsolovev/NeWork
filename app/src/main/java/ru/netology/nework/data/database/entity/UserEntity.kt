package ru.netology.nework.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nework.domain.models.User

@Entity
class UserEntity(
    @PrimaryKey
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String? = null
) {
    fun toDto() = User(id, login, name, avatar)

    companion object {
        fun fromDto(dto: User) = UserEntity(dto.id, dto.login, dto.name, dto.avatar)
    }
}

fun List<UserEntity>.toDto() = map(UserEntity::toDto)
fun List<User>.toUserEntity() = map(UserEntity.Companion::fromDto)

//fun Flow<UserEntity>.toDto() : Flow<User> = this.toDto()
//fun User.toUserEntity() : UserEntity = this.toUserEntity()


//fun toUserEntity(user: User) = UserEntity.fromDto(user)

//fun toUserDto(userEntity: UserEntity) = UserEntity.