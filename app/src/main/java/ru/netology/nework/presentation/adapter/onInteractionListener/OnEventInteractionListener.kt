package ru.netology.nework.presentation.adapter.onInteractionListener

import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.VideoView
import ru.netology.nework.domain.model.Event

interface OnEventInteractionListener {
    fun onEdit(event: Event)
    fun onRemove(event: Event)
    fun onLike(event: Event)
    fun onParticipate(event: Event)
    fun onShare(event: Event)
    fun onLink(event: Event)
    fun onImage(event: Event)
    fun onPauseAudio(event: Event)
    fun onStopAudio(event: Event)
    fun onPlayAudio(event: Event, seekBar: SeekBar, progressBar: View)
    fun onPlayVideo(event: Event, videoView: VideoView, progressBar: View, playButton: View, videoGroup: FrameLayout)
    fun onOpenUserProfile(event: Event)
    fun onCoords(event: Event)
}