package ru.netology.nework.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.domain.model.Attachment
import ru.netology.nework.domain.model.Post
import ru.netology.nework.domain.repository.PostRepository
import ru.netology.nework.domain.usecases.post.GetAllPostsUseCase
import ru.netology.nework.domain.usecases.post.GetNewerPostsUseCase
import ru.netology.nework.domain.usecases.post.GetWallUseCase
import ru.netology.nework.domain.usecases.post.LikePostByIdUseCase
import ru.netology.nework.domain.usecases.post.RemovePostByIdUseCase
import ru.netology.nework.domain.usecases.post.SavePostUseCase
import ru.netology.nework.domain.usecases.post.UnlikePostByIdUseCase
import ru.netology.nework.utils.SingleLiveEvent
import javax.inject.Inject

val emptyPost = Post(
    id = 0,
    authorId = 0,
    author = "",
    authorAvatar = "",
    authorJob = "",
    content = "",
    published = "",
    coords = null,
    link = "",
    likeOwnerIds = emptySet(),
    mentionIds = emptySet(),
    mentionedMe = false,
    likedByMe = false,
    attachment = null,
    ownedByMe = false
)

@HiltViewModel
class PostViewModel @Inject constructor(
    repository: PostRepository
) : ViewModel() {

    val getAllPostsUseCase = GetAllPostsUseCase(repository)
    val savePostUseCase = SavePostUseCase(repository)
    val likePostByIdUseCase = LikePostByIdUseCase(repository)
    val unlikePostByIdUseCase = UnlikePostByIdUseCase(repository)
    val removePostByIdUseCase = RemovePostByIdUseCase(repository)
    val getWallUseCase = GetWallUseCase(repository)
    val getNewerPostsUseCase = GetNewerPostsUseCase(repository)

    val data = repository.data

    val wall = repository.wall

    val edited = MutableLiveData(emptyPost)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    fun getAllPosts() = viewModelScope.launch {
        getAllPostsUseCase()
    }

    fun getNewerPosts() = viewModelScope.launch {
        getNewerPostsUseCase()
    }

    fun getWallPosts(userId: Int) = viewModelScope.launch {
        getWallUseCase.getWall(userId)
    }

    fun savePost() {
        edited.value?.let {
            viewModelScope.launch {
                try {
                    savePostUseCase.savePost(it)
                    _postCreated.value = Unit
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        edited.value = emptyPost
    }

    fun changeContent(content: String, link: String, attachment: Attachment?) {
        val text = content.trim()
        if (edited.value?.content != text || edited.value?.link != link) {
            edited.value = edited.value?.copy(content = text)
            edited.value = edited.value?.copy(link = link)
            edited.value = edited.value?.copy(attachment = attachment)
            Log.d("att2", "$attachment")
        }
        return
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun removeById(id: Int) = viewModelScope.launch {
        removePostByIdUseCase.removePostById(id)
    }

    fun likeById(id: Int) = viewModelScope.launch {
        likePostByIdUseCase.likePostById(id)
    }

    fun unlikeById(id: Int) = viewModelScope.launch {
        unlikePostByIdUseCase.unlikePostById(id)
    }
}