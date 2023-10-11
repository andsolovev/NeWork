package ru.netology.nework.presentation.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.Toast
import android.widget.VideoView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentEventsBinding
import ru.netology.nework.domain.model.Event
import ru.netology.nework.presentation.adapter.EventAdapter
import ru.netology.nework.presentation.adapter.onInteractionListener.OnEventInteractionListener
import ru.netology.nework.presentation.fragments.FragmentAttachment.Companion.link
import ru.netology.nework.presentation.viewmodel.AuthViewModel
import ru.netology.nework.presentation.viewmodel.EventViewModel
import ru.netology.nework.presentation.viewmodel.UserViewModel

@AndroidEntryPoint
class FragmentEvents : Fragment() {
    private val eventViewModel: EventViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private val mediaPlayer = MediaPlayer()
    private var isPaused = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)
        val adapter = EventAdapter(object : OnEventInteractionListener {

            override fun onEdit(event: Event) {
                eventViewModel.edit(event)
                findNavController().navigate(R.id.fragmentNewEvent)
            }

            override fun onRemove(event: Event) {
                eventViewModel.removeById(event.id)
            }

            override fun onLike(event: Event) {
                when (authViewModel.authorized) {
                    true -> {
                        when (event.likedByMe) {
                            true -> eventViewModel.unlikeById(event.id)
                            false -> eventViewModel.likeById(event.id)
                        }
                    }

                    false -> unauthorized()
                }
            }

            override fun onParticipate(event: Event) {
                when (authViewModel.authorized) {
                    true -> {
                        when (event.participatedByMe) {
                            true -> eventViewModel.unparticipateById(event.id)
                            false -> eventViewModel.participateById(event.id)
                        }
                    }

                    false -> unauthorized()
                }
            }

            override fun onShare(event: Event) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, event.content)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, "Share Post")
                startActivity(shareIntent)
            }

            override fun onImage(event: Event) {
                findNavController().navigate(
                    R.id.fragmentAttachment,
                    Bundle().apply {
                        link = event.attachment?.url
                    }
                )
            }

            override fun onPlayAudio(event: Event, seekBar: SeekBar, progressBar: View) {
                if (isPaused) {
                    isPaused = false
                    mediaPlayer.start()
                } else {
                    isPaused = false
                    progressBar.visibility = View.VISIBLE
//                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(event.attachment?.url)
                    mediaPlayer.prepare()
                    mediaPlayer.start()

                }
                seekBar.max = mediaPlayer.duration
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        try {
                            seekBar.progress = mediaPlayer.currentPosition
                            handler.postDelayed(this, 1000)
                        } catch (e: Exception) {
                            seekBar.progress = 0
                        }
                    }
                }, 0)
                mediaPlayer.setOnPreparedListener {
                    progressBar.visibility = View.INVISIBLE
                }
            }

            override fun onPauseAudio(event: Event) {
                mediaPlayer.pause()
                isPaused = true
            }

            override fun onStopAudio(event: Event) {
                mediaPlayer.apply {
                    stop()
                    reset()
//                    release()
                }
            }

            override fun onPlayVideo(
                event: Event,
                videoView: VideoView,
                progressBar: View,
                playButton: View,
                videoGroup: FrameLayout
            ) {
                val uri = Uri.parse(event.attachment?.url)
                val displayWidth = resources.displayMetrics.widthPixels - 64

                videoView.apply {
                    setMediaController(MediaController(requireContext()))
                    setVideoURI(uri)
                    setOnPreparedListener { mp ->
                        progressBar.visibility = View.GONE

                        if (mp.videoHeight > mp.videoWidth) {
                            videoGroup.layoutParams?.height =
                                (displayWidth * (mp.videoHeight.toDouble() / mp.videoWidth)).toInt()
                            videoView.layoutParams.height = videoGroup.layoutParams?.height!!
                        }
                        start()
                    }

                    setOnCompletionListener {
                        playButton.visibility = View.VISIBLE
                        stopPlayback()
                        it.release()
                    }
                }
            }

            override fun onLink(event: Event) {
                if (URLUtil.isValidUrl(event.link)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.invalid_link), Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCoords(event: Event) {
                Log.d("coords", "$event, ${event.coords?.lat}, ${event.coords?.long}")
                if (event.coords?.lat != null) {
                    val lat = event.coords.lat.toDouble()
                    val long = event.coords.long.toDouble()
                    Log.d("coords", "$lat, $long")
                    findNavController().navigate(
                        R.id.mapFragment,
                        bundleOf(
                            FragmentMap.LAT_KEY to lat,
                            FragmentMap.LONG_KEY to long
                        )
                    )
                } else {
                    Toast.makeText(activity, "Wrong coordinates!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onOpenUserProfile(event: Event) {
                if (authViewModel.authorized) {
                    lifecycleScope.launch {
                        userViewModel.getUserById(event.authorId)
                        findNavController().navigate(R.id.fragment_profile)
                    }
                } else unauthorized()
            }
        })

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.fragmentNewEvent)
        }

        binding.eventList.adapter = adapter

        eventViewModel.getAllEvents()

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            eventViewModel.data.collectLatest { events ->
                adapter.submitList(events)
                binding.emptyList.isVisible = events.isEmpty()
            }
        }
        return binding.root
    }

    private fun unauthorized() {
        Toast.makeText(context, "Sign in to continue", Toast.LENGTH_LONG).show()
         findNavController().navigate(R.id.fragment_sign_in)
    }

}