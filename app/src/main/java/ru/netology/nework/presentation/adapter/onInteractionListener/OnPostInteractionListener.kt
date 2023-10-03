package ru.netology.nework.presentation.adapter.onInteractionListener

import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.VideoView
import ru.netology.nework.domain.model.Post

interface OnPostInteractionListener {
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onLink(post: Post)
    fun onImage(post: Post)
    fun onPauseAudio(post: Post)
    fun onStopAudio(post: Post)
    fun onPlayAudio(post: Post, seekBar: SeekBar, progressBar: View)
    fun onPlayVideo(post: Post, videoView: VideoView, progressBar: View, playButton: View, videoGroup: FrameLayout)
    fun onOpenUserProfile(post: Post)
    fun onCoords(post: Post)
}