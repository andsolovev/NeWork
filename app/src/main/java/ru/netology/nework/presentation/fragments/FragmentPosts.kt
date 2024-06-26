package ru.netology.nework.presentation.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentPostsBinding
import ru.netology.nework.domain.model.Post
import ru.netology.nework.presentation.adapter.PostAdapter
import ru.netology.nework.presentation.adapter.onInteractionListener.OnPostInteractionListener
import ru.netology.nework.presentation.fragments.FragmentAttachment.Companion.link
import ru.netology.nework.presentation.viewmodel.AuthViewModel
import ru.netology.nework.presentation.viewmodel.PostViewModel
import ru.netology.nework.presentation.viewmodel.UserViewModel

@AndroidEntryPoint
class FragmentPosts : Fragment() {

    private val postViewModel: PostViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val userViewModel: UserViewModel by viewModels()

    private val mediaPlayer = MediaPlayer()
    private var isPaused = false
    private var playerJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostsBinding.inflate(inflater, container, false)

//        postViewModel.getAllPosts()
//        postViewModel.getNewerPosts()

        val adapter = PostAdapter(object : OnPostInteractionListener {

            override fun onEdit(post: Post) {
                postViewModel.edit(post)
                findNavController().navigate(R.id.action_fragment_posts_to_fragmentNewPost)
            }

            override fun onRemove(post: Post) {
                postViewModel.removeById(post.id)
            }

            override fun onLike(post: Post) {
                when (authViewModel.authorized) {
                    true -> {
                        when (post.likedByMe) {
                            true -> postViewModel.unlikeById(post.id)
                            false -> postViewModel.likeById(post.id)
                        }
                    }

                    false -> unauthorized()
                }
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                startActivity(intent)
            }

            override fun onImage(post: Post) {
                findNavController().navigate(
                    R.id.action_fragment_posts_to_fragmentAttachment,
                    Bundle().apply {
                        link = post.link
                    }
                )
            }

            override fun onPlayAudio(post: Post, seekBar: SeekBar, progressBar: View) {
                if (isPaused) {
                    isPaused = false
                    mediaPlayer.start()
                } else {
                    isPaused = false
                    progressBar.visibility = View.VISIBLE
                    mediaPlayer.setDataSource(post.attachment?.url)
                    mediaPlayer.prepare()
                    mediaPlayer.start()

                }
                seekBar.max = mediaPlayer.duration
                playerJob?.cancel()
                playerJob = viewLifecycleOwner.lifecycleScope.launch {
                    while (true) {
                        seekBar.progress = mediaPlayer.currentPosition
                        delay(1_000)
                    }
                }

                mediaPlayer.setOnPreparedListener {
                    progressBar.visibility = View.INVISIBLE
                }
            }

            override fun onPauseAudio(post: Post) {
                mediaPlayer.pause()
                isPaused = true
            }

            override fun onStopAudio(post: Post) {
                mediaPlayer.apply {
                    stop()
                    reset()
                }
            }

            override fun onPlayVideo(
                post: Post,
                videoView: VideoView,
                progressBar: View,
                playButton: View,
                videoGroup: FrameLayout
            ) {
                val uri = Uri.parse(post.attachment?.url)
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

            override fun onLink(post: Post) {
                if (URLUtil.isValidUrl(post.link)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.link))
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.invalid_link), Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onOpenUserProfile(post: Post) {
                if (authViewModel.authorized) {
                    lifecycleScope.launch {
                        userViewModel.getUserById(post.authorId)
                        findNavController().navigate(R.id.action_fragment_posts_to_fragmentProfile)
                    }
                } else unauthorized()
            }
        })

        binding.postList.adapter = adapter

        lifecycleScope.launch {
            postViewModel.data.collect {
                adapter.submitData(it)
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_posts_to_fragmentNewPost)
        }

        return binding.root
    }

    private fun unauthorized() {
        Toast.makeText(context, "Sign in to continue", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_fragment_posts_to_fragmentSignIn)
    }
}
