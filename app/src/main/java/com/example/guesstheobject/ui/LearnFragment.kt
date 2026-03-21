package com.example.guesstheobject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.guesstheobject.R
import com.google.android.material.snackbar.Snackbar

class LearnFragment : Fragment() {

    private lateinit var drawView: DrawingView
    private lateinit var tvLetter: TextView
    private lateinit var tvRoundInfo: TextView

    private lateinit var screenCanvas: View
    private lateinit var screenResult: View
    private lateinit var tvResultTitle: TextView
    private lateinit var tvOverallScore: TextView
    private lateinit var tvScoreMessage: TextView
    private lateinit var tvGoodLetters: TextView
    private lateinit var tvFocusLetters: TextView
    private lateinit var btnTryAgain: Button
    private lateinit var btnBackToMenu: Button

    private val progressManager = LearnProgressManager()
    private val resultManager = LearnResultManager()
    private val maxRounds = 3
    private var strokeAttempts = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_learn, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screenCanvas = view.findViewById(R.id.screenCanvas)
        drawView = view.findViewById(R.id.learnDrawView)
        tvLetter = view.findViewById(R.id.tvLetter)
        tvRoundInfo = view.findViewById(R.id.tvRoundInfo)

        screenResult = view.findViewById(R.id.screenResult)
        tvResultTitle = view.findViewById(R.id.tvResultTitle)
        tvOverallScore = view.findViewById(R.id.tvOverallScore)
        tvScoreMessage = view.findViewById(R.id.tvScoreMessage)
        tvGoodLetters = view.findViewById(R.id.tvGoodLetters)
        tvFocusLetters = view.findViewById(R.id.tvFocusLetters)
        btnTryAgain = view.findViewById(R.id.btnTryAgain)
        btnBackToMenu = view.findViewById(R.id.btnBackToMenu)
        view.findViewById<Button>(R.id.btnEvaluate).setOnClickListener {
            if (resultManager.getOverallAccuracy() == 0f) {
                Snackbar.make(requireView(), "Draw at least one letter first! ✏️", Snackbar.LENGTH_SHORT).show()
            } else {
                showResultScreen()
            }
        }

        btnTryAgain.setOnClickListener {
            progressManager.reset()
            resultManager.reset()
            screenResult.visibility = View.GONE
            screenCanvas.visibility = View.VISIBLE
            loadLetter()
        }

        btnBackToMenu.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        drawView.currentMode = DrawingView.Mode.LEARN

        drawView.onLetterCompletedListener = {
            onLetterCompleted(progressManager.currentLetter)
        }

        drawView.onStrokeFailedListener = {
            onStrokeFailed()
        }

        loadLetter()
    }

    private fun loadLetter() {
        val letter = LetterStrokeRepository.getLetter(progressManager.currentLetter) ?: return
        tvLetter.text = progressManager.currentLetter.toString()
        tvRoundInfo.text = "Round ${progressManager.attempt + 1} of $maxRounds"
        drawView.setLearningLetter(letter, progressManager.attempt)
        strokeAttempts = 0
    }

    private fun onLetterCompleted(letter: Char) {
        val letterObj = LetterStrokeRepository.getLetter(letter) ?: return
        val completedTries = progressManager.attempt + 1

        // Calculate accuracy for completed strokes
        val strokeIndex = (drawView.currentStrokeIndex - 1).coerceAtLeast(0)
        if (strokeIndex < letterObj.strokes.size) {
            val accuracy = StrokeAccuracyCalculator.calculate(
                drawView.currentStrokePoints.toList(),
                letterObj.strokes[strokeIndex],
                drawView.width,
                drawView.height
            )
            resultManager.recordAccuracy(letter, accuracy)
        }

        if (completedTries < maxRounds && completedTries < letterObj.strokes.size) {
            progressManager.advanceTries(letterObj)
            Snackbar.make(
                requireView(),
                "Great! Round ${progressManager.attempt + 1} — fewer dots! 💙",
                Snackbar.LENGTH_SHORT
            ).show()
            loadLetter()
        } else {
            progressManager.advanceTries(letterObj)
            if (letter == 'I') {
                showResultScreen()
            } else {
                val nextChar = (letter.code + 1).toChar()
                Snackbar.make(
                    requireView(),
                    "⭐ Well done! Moving to $nextChar!",
                    Snackbar.LENGTH_SHORT
                ).show()
                progressManager.nextLetter()
                if (progressManager.isCompleted) {
                    showResultScreen()
                } else {
                    val nextChar = progressManager.currentLetter
                    Snackbar.make(
                        requireView(),
                        "⭐ Well done! Moving to $nextChar!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    loadLetter()
                }
                loadLetter()
            }
        }
    }

    private fun showResultScreen() {
        val overall = resultManager.getOverallAccuracy()
        val goodLetters = resultManager.getGoodLetters()
        val focusLetters = resultManager.getLettersToFocus()

        tvOverallScore.text = "Overall Score: ${overall.toInt()}%"
        tvScoreMessage.text = when {
            overall >= 90 -> "Amazing work! You're a drawing star! ⭐"
            overall >= 75 -> "Great job! Keep it up! 🎉"
            overall >= 60 -> "Good effort! Practice makes perfect! 💪"
            else -> "Keep practicing, you're getting better! 🌟"
        }
        tvGoodLetters.text = if (goodLetters.isEmpty()) "Keep practicing!"
        else goodLetters.joinToString(", ")
        tvFocusLetters.text = if (focusLetters.isEmpty()) "None — excellent work!"
        else focusLetters.joinToString(", ")

        screenCanvas.visibility = View.GONE
        screenResult.visibility = View.VISIBLE
    }

    private fun onStrokeFailed() {
        strokeAttempts++
        val remaining = 3 - strokeAttempts
        resultManager.recordAccuracy(progressManager.currentLetter, 20f)
        if (remaining > 0) {
            Snackbar.make(requireView(), "Oops! Try again — $remaining tries left 💪", Snackbar.LENGTH_SHORT).show()
        } else {
            strokeAttempts = 0
            Snackbar.make(requireView(), "No worries, let's try that again! 😊", Snackbar.LENGTH_SHORT).show()
        }
        drawView.clearUserStrokesOnly()
    }
}
