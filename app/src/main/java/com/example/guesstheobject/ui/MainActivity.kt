package com.example.guesstheobject.ui


import androidx.fragment.app.FragmentManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.guesstheobject.R
import com.example.guesstheobject.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var progress = 0

    private lateinit var bgMusic: MediaPlayer
    private lateinit var clickSound: MediaPlayer

    private val captions = listOf(
        "AI is thinking… please hold on!",
        "Crunching some pixels…",
        "Your object is almost revealed!",
        "Analyzing patterns…",
        "Brainstorming… almost there!",
        "Let’s see what it is…",
        "Processing your guess…",
        "Unlocking the mystery…",
        "One moment, genius at work…",
        "Detective AI on the case…"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flFragment.visibility = View.GONE

        testBackendConnection()

        // ----------------- MediaPlayer Setup -----------------
        bgMusic = MediaPlayer.create(this, R.raw.backgroundmusic).apply {
            isLooping = true
            start()
        }

        clickSound = MediaPlayer.create(this, R.raw.button_click)

        // Show loading overlay
        binding.loadingOverlay.visibility = View.VISIBLE
        binding.mainMenu.visibility = View.GONE

        startProgressAnimation()
        startCaptionCycle()

        // After 4 seconds, hide loading and show main menu
        handler.postDelayed({
            binding.loadingOverlay.visibility = View.GONE
            binding.mainMenu.visibility = View.VISIBLE
            stopCaptionCycle()
        }, 4000)

        setupButtons()
        setupBackPressHandler()
    }

    fun testBackendConnection() {
        thread {
            try {
                val url = URL("http://192.168.101.4:3000/animals")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val responseCode = connection.responseCode
                Log.d("API_TEST", "Zoo Backend Response Code: $responseCode")
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentFragment = supportFragmentManager
                        .findFragmentById(R.id.flFragment)

                    if (currentFragment is DrawFragment && currentFragment.onBackPressed()) {
                        return
                    }

                    if (supportFragmentManager.backStackEntryCount > 0) {
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.addOnBackStackChangedListener(object :
                            FragmentManager.OnBackStackChangedListener {
                            override fun onBackStackChanged() {
                                supportFragmentManager.removeOnBackStackChangedListener(this)
                                if (supportFragmentManager.backStackEntryCount == 0) {
                                    binding.mainMenu.visibility = View.VISIBLE
                                    binding.flFragment.visibility = View.GONE
                                    bgMusic.start()
                                }
                            }
                        })
                    } else {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

    private fun setupButtons() {
        binding.btnAkinatorMode.setOnClickListener {

            playClickSound()

            binding.mainMenu.visibility = View.GONE
            binding.flFragment.visibility = View.VISIBLE

            bgMusic.pause()

            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, AkinatorFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnDrawingMode.setOnClickListener {

            playClickSound()

            binding.mainMenu.visibility = View.GONE
            binding.flFragment.visibility = View.VISIBLE

            bgMusic.pause()

            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, ModeSelectionFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun playClickSound() {
        if (clickSound.isPlaying) {
            clickSound.pause()
            clickSound.seekTo(0)
        }
        clickSound.start()
    }

    // ------------------- Progress Bar -------------------
    private val progressRunnable = object : Runnable {
        override fun run() {
            if (progress < 100) {
                progress += 2
                binding.loadingProgress.progress = progress
                handler.postDelayed(this, 60)
            }
        }
    }

    private fun startProgressAnimation() {
        progress = 0
        binding.loadingProgress.progress = 0
        handler.post(progressRunnable)
    }

    // ------------------- Caption Cycle -------------------
    private val captionRunnable = object : Runnable {
        override fun run() {
            binding.loadingText.text = captions.random()
            handler.postDelayed(this, 1000)
        }
    }

    private fun startCaptionCycle() {
        handler.post(captionRunnable)
    }

    private fun stopCaptionCycle() {
        handler.removeCallbacks(captionRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        bgMusic.release()
        clickSound.release()
    }
}