package com.example.guesstheobject.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.guesstheobject.R
import com.google.android.material.snackbar.Snackbar

class LearnFragment : Fragment(R.layout.fragment_learn) {

    private lateinit var drawView: DrawingView
    private lateinit var tvLetter: TextView
    private lateinit var tvRoundInfo: TextView
    private lateinit var btnClear: Button

    private val progressManager = LearnProgressManager()
    private var strokeAttempts = 0

    // How many rounds per letter before moving on
    private val maxRounds = 3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawView   = view.findViewById(R.id.learnDrawView)
        tvLetter   = view.findViewById(R.id.tvLetter)
        tvRoundInfo = view.findViewById(R.id.tvRoundInfo)
        btnClear   = view.findViewById(R.id.btnLearnClear)

        drawView.setMode(DrawingView.Mode.LEARN)

        btnClear.setOnClickListener {
            drawView.clearUserStrokesOnly()
        }

        loadLetter()
    }

    // ─────────────────────────────────────────────────────
    // LOAD CURRENT LETTER
    // ─────────────────────────────────────────────────────

    private fun loadLetter() {
        val letter = LetterStrokeRepository.getLetter(progressManager.currentLetter)

        if (letter == null) {
            // All letters done
            Snackbar.make(requireView(), "🎉 Amazing! You finished all letters!", Snackbar.LENGTH_LONG).show()
            progressManager.reset()
            loadLetter()
            return
        }

        // Update header
        tvLetter.text = progressManager.currentLetter.toString()
        updateRoundInfo()

        // Set letter on canvas with current scaffold level
        drawView.setLearningLetter(letter, progressManager.attempt)

        // Wire up stroke callbacks
        drawView.onLetterCompletedListener = {
            strokeAttempts = 0
            onLetterCompleted(letter)
        }

        drawView.onStrokeFailedListener = {
            strokeAttempts++
            onStrokeFailed()
        }
    }

    // ─────────────────────────────────────────────────────
    // ROUND PROGRESSION
    // ─────────────────────────────────────────────────────

    /**
     * Called when the user successfully completes all strokes of the letter.
     * If rounds remain for this letter, advance the scaffold.
     * Otherwise move to the next letter.
     */
    private fun onLetterCompleted(letter: Letter) {
        val completedTries = progressManager.attempt + 1

        if (completedTries < maxRounds && completedTries < letter.strokes.size) {
            progressManager.advanceTries(letter)
            Snackbar.make(
                requireView(),
                "Great! Now try round ${progressManager.attempt + 1} — watch the blue strokes! 💙",
                Snackbar.LENGTH_SHORT
            ).show()
            loadLetter()
        } else {
            progressManager.advanceTries(letter)
            Snackbar.make(
                requireView(),
                "⭐ Well done! Moving to ${getNextLetterChar()}!",
                Snackbar.LENGTH_SHORT
            ).show()
            progressManager.nextLetter()
            loadLetter()
        }
    }

    /**
     * Called when the user draws a stroke that doesn't match the expected one.
     * After 3 failed attempts, skip to next letter.
     */
    private fun onStrokeFailed() {
        strokeAttempts++
        val remaining = 3 - strokeAttempts
        if (remaining > 0) {
            Snackbar.make(
                requireView(),
                "Oops! Try again — $remaining tries left 💪",
                Snackbar.LENGTH_SHORT
            ).show()
        } else {
            strokeAttempts = 0
            Snackbar.make(
                requireView(),
                "No worries, let's try that again! 😊",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        // Always clear and stay on same letter, same round
        drawView.clearUserStrokesOnly()
    }
    // ─────────────────────────────────────────────────────
    // UI HELPERS
    // ─────────────────────────────────────────────────────

    private fun updateRoundInfo() {
        val round = progressManager.attempt + 1
        tvRoundInfo.text = "Round $round of $maxRounds"
    }

    private fun getNextLetterChar(): String {
        return if (progressManager.currentLetter < 'Z')
            (progressManager.currentLetter + 1).toString()
        else "A"
    }
}
