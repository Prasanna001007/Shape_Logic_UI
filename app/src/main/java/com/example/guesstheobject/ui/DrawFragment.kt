package com.example.guesstheobject.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.guesstheobject.R

class DrawFragment : Fragment() {

    // ── Screens ──────────────────────────────────────────
    private lateinit var screenFreeMode: ConstraintLayout
    private lateinit var screenGuessResult: LinearLayout

    // ── Free Mode views ──────────────────────────────────
    private lateinit var drawView: DrawingView
    private lateinit var toolbar: LinearLayout
    private lateinit var btnSend: Button
    private lateinit var btnClear: ImageButton
    private lateinit var btnEraser: ImageButton
    private lateinit var btnColor: ImageButton
    private lateinit var btnWidth: ImageButton
    private lateinit var imgBear: ImageView
    private lateinit var txtBearGuess: TextView
    private lateinit var txtGuessResult: TextView
    private lateinit var btnDrawAgain: Button

    // ── Bear random text cycling ─────────────────────────
    private val handler = Handler(Looper.getMainLooper())
    private var bearTextRunning = false

    private val bearTexts = listOf(
        "Hmm, is this a stroke of genius? 🤔",
        "What color shall you give it? 🎨",
        "I think I see something... 👀",
        "Keep going, this looks amazing! ✨",
        "Is that a dog? A cat? A spaceship? 🚀",
        "My paws are tingling! 🐾",
        "Ooh, I almost know what this is! 😮",
        "Are you drawing what I think you are? 🌈",
        "This is going to be great! 🌟",
        "I'm so excited to guess this! 🎉"
    )

    private val bearTextRunnable = object : Runnable {
        override fun run() {
            if (bearTextRunning) {
                txtBearGuess.text = bearTexts.random()
                handler.postDelayed(this, 3000)
            }
        }
    }

    // ── Guess results ────────────────────────────────────
    private val guessResults = listOf(
        "a 🌳 Tree!", "a 🐶 Dog!", "a 🏠 House!",
        "a 🌸 Flower!", "a 🐱 Cat!", "a ⭐ Star!",
        "a 🚗 Car!", "a 🌙 Moon!", "a 🐟 Fish!",
        "a 🍎 Apple!"
    )

    // ── Screen stack for internal back navigation ────────
    private val screenStack = ArrayDeque<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_draw, container, false)
        bindViews(view)
        showScreen("freeMode")
        activateFreeMode()
        setupToolbar()
        setupGuessFlow()
        return view
    }

    // ─────────────────────────────────────────────────────
    // VIEW BINDING
    // ─────────────────────────────────────────────────────

    private fun bindViews(view: View) {
        screenFreeMode    = view.findViewById(R.id.screenFreeMode)
        screenGuessResult = view.findViewById(R.id.screenGuessResult)

        drawView          = view.findViewById(R.id.drawView)
        toolbar           = view.findViewById(R.id.toolbar)
        btnSend           = view.findViewById(R.id.btnSend)
        btnClear          = view.findViewById(R.id.btnClear)
        btnEraser         = view.findViewById(R.id.btnEraser)
        btnColor          = view.findViewById(R.id.btnColor)
        btnWidth          = view.findViewById(R.id.btnWidth)
        imgBear           = view.findViewById(R.id.imgBear)
        txtBearGuess      = view.findViewById(R.id.txtBearGuess)
        txtGuessResult    = view.findViewById(R.id.txtGuessResult)
        btnDrawAgain      = view.findViewById(R.id.btnDrawAgain)
    }

    // ─────────────────────────────────────────────────────
    // SCREEN SWITCHER
    // ─────────────────────────────────────────────────────

    private fun showScreen(name: String, addToStack: Boolean = true) {
        if (addToStack && (screenStack.isEmpty() || screenStack.last() != name)) {
            screenStack.addLast(name)
        }
        screenFreeMode.visibility    = View.GONE
        screenGuessResult.visibility = View.GONE

        when (name) {
            "freeMode"    -> screenFreeMode.visibility    = View.VISIBLE
            "guessResult" -> screenGuessResult.visibility = View.VISIBLE
        }
    }

    /** Called by MainActivity's back press handler */
    fun onBackPressed(): Boolean {
        stopBearTextCycle()
        if (screenStack.size > 1) {
            screenStack.removeLast()
            val previous = screenStack.last()
            showScreen(previous, addToStack = false)
            if (previous == "freeMode") startBearTextCycle()
            return true
        }
        return false
    }

    // ─────────────────────────────────────────────────────
    // FREE MODE
    // ─────────────────────────────────────────────────────

    private fun activateFreeMode() {
        drawView.setMode(DrawingView.Mode.FREE)
        drawView.clear()
        startBearTextCycle()
    }

    private fun setupToolbar() {
        btnClear.setOnClickListener { drawView.clear() }
        btnEraser.setOnClickListener { drawView.enableEraser() }

        btnColor.setOnClickListener {
            val colors = arrayOf("⚫ Black", "🔴 Red", "🔵 Blue", "🟢 Green", "🟣 Purple")
            val values = intArrayOf(Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.rgb(128, 0, 128))
            AlertDialog.Builder(requireContext())
                .setTitle("Pick a Color 🎨")
                .setItems(colors) { _, which -> drawView.setPaintColor(values[which]) }
                .show()
        }

        btnWidth.setOnClickListener {
            val labels = arrayOf("Thin (4)", "Normal (8)", "Thick (12)", "Extra Thick (18)")
            val widths = floatArrayOf(4f, 8f, 12f, 18f)
            AlertDialog.Builder(requireContext())
                .setTitle("Pick Stroke Width ✏️")
                .setItems(labels) { _, which -> drawView.setStrokeWidth(widths[which]) }
                .show()
        }
    }

    // ─────────────────────────────────────────────────────
    // GUESS FLOW
    // ─────────────────────────────────────────────────────

    private fun setupGuessFlow() {
        btnSend.setOnClickListener {
            stopBearTextCycle()
            txtGuessResult.text = "I think your drawing is ${guessResults.random()}"
            showScreen("guessResult")
        }

        btnDrawAgain.setOnClickListener {
            drawView.clear()
            showScreen("freeMode")
            startBearTextCycle()
        }
    }

    // ─────────────────────────────────────────────────────
    // BEAR TEXT CYCLING
    // ─────────────────────────────────────────────────────

    private fun startBearTextCycle() {
        bearTextRunning = true
        txtBearGuess.text = bearTexts.random()
        handler.postDelayed(bearTextRunnable, 3000)
    }

    private fun stopBearTextCycle() {
        bearTextRunning = false
        handler.removeCallbacks(bearTextRunnable)
    }

    // ─────────────────────────────────────────────────────
    // LIFECYCLE
    // ─────────────────────────────────────────────────────

    override fun onDestroyView() {
        super.onDestroyView()
        stopBearTextCycle()
    }
}
