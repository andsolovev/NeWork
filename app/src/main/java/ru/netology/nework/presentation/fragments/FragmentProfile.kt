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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentProfileBinding
import ru.netology.nework.domain.model.Job
import ru.netology.nework.domain.model.Post
import ru.netology.nework.presentation.adapter.JobAdapter
import ru.netology.nework.presentation.adapter.WallAdapter
import ru.netology.nework.presentation.adapter.onInteractionListener.OnJobInteractionListener
import ru.netology.nework.presentation.adapter.onInteractionListener.OnPostInteractionListener
import ru.netology.nework.presentation.fragments.FragmentAttachment.Companion.link
import ru.netology.nework.presentation.viewmodel.AuthViewModel
import ru.netology.nework.presentation.viewmodel.JobViewModel
import ru.netology.nework.presentation.viewmodel.PostViewModel
import ru.netology.nework.presentation.viewmodel.UserViewModel

@AndroidEntryPoint
class FragmentProfile : Fragment() {

    private val postViewModel: PostViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private val jobViewModel: JobViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    private val mediaPlayer = MediaPlayer()
    private var isPaused = false

    companion object {
        private const val USER_ID_KEY = "ID_KEY"
        fun createArguments(userId: Int? = 0) =
            bundleOf(USER_ID_KEY to userId)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val userId = requireArguments().getInt(USER_ID_KEY)
        val ownedByMe = userId == authViewModel.state.value?.id

        Log.d("id", "$userId, ${authViewModel.state.value?.id}")

        jobViewModel.getJobsByUserId(userId)
        postViewModel.getWallPosts(userId)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        if (ownedByMe) {
            binding.userProfileToolbar.addJob.visibility = View.VISIBLE
            binding.userProfileToolbar.logout.visibility = View.VISIBLE
            binding.userProfileToolbar.addJob.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_profile_to_fragmentNewJob)
            }
            binding.userProfileToolbar.logout.setOnClickListener {
                authViewModel.logOut()
            }
        } else {
            binding.userProfileToolbar.addJob.visibility = View.GONE
            binding.userProfileToolbar.logout.visibility = View.GONE
        }

        val jobAdapter = JobAdapter(object : OnJobInteractionListener {
            override fun onEditJob(job: Job) {
                jobViewModel.edit(job)
                findNavController().navigate(R.id.action_fragment_profile_to_fragmentNewJob)
            }

            override fun onRemoveJob(job: Job) {
                jobViewModel.removeJobById(job.id)
            }

            override fun onLink(job: Job) {
                if (URLUtil.isValidUrl(job.link)) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.link))
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.invalid_link), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, ownedByMe)

        val wallAdapter = WallAdapter(object : OnPostInteractionListener {

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
                val shareIntent = Intent.createChooser(intent, "Share Post")
                startActivity(shareIntent)
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

            override fun onPauseAudio(post: Post) {
                mediaPlayer.pause()
                isPaused = true
            }

            //
            override fun onStopAudio(post: Post) {
                mediaPlayer.apply {
                    stop()
                    reset()
//                    release()
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

        binding.userProfileToolbar.jobList.adapter = jobAdapter
        binding.postList.adapter = wallAdapter


        lifecycleScope.launch {
            jobViewModel.data.collectLatest {
                jobAdapter.submitList(it)
            }

        }

        lifecycleScope.launch {
            postViewModel.wall.collectLatest {
                wallAdapter.submitList(it)
                binding.noPosts.isVisible = it.isEmpty()
            }
        }

        binding.userProfileToolbar.progressBarAvatar.visibility = View.VISIBLE
        binding.userProfileToolbar.progressBarJob.visibility = View.VISIBLE
        binding.progressBarPosts.visibility = View.VISIBLE
        binding.userProfileToolbar.name.text = ""

        userViewModel.user.observe(viewLifecycleOwner) {

            binding.progressBarPosts.visibility = View.INVISIBLE
            binding.userProfileToolbar.apply {
                name.text = userViewModel.user.value?.name
                name.visibility = View.VISIBLE
                progressBarJob.visibility = View.INVISIBLE
                noAvatar.visibility = View.INVISIBLE
                Glide.with(avatar)
                    .load(userViewModel.user.value?.avatar)
                    .error(R.drawable.ic_person_24)
                    .timeout(10_000)
                    .into(avatar)
                avatar.visibility = View.VISIBLE
                progressBarAvatar.visibility = View.INVISIBLE
            }
        }

        jobViewModel.job.observe(viewLifecycleOwner) {
            binding.userProfileToolbar.noJob.isVisible = it.isEmpty()
        }

        authViewModel.state.observe(viewLifecycleOwner) {
            if (it.id == 0) findNavController().navigate(R.id.fragment_sign_in)
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()

        Log.d("pause", "paused!!!")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("destr", "view destr!!!")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("destr", "destr!!!")
        userViewModel.vipeUser()
        jobViewModel.vipeJob()
        binding.noPosts.visibility = View.INVISIBLE
        binding.userProfileToolbar.noJob.visibility = View.INVISIBLE
    }

    private fun unauthorized() {
        Toast.makeText(context, "Sign in to continue", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_fragment_posts_to_fragmentSignIn)
    }

    override fun onResume() {
        super.onResume()
        Log.d("resume", "view resumed!!!")

    }
}