package com.example.guesstheobject.ui

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.guesstheobject.R
import com.google.android.material.snackbar.Snackbar


class ShapeFragment : Fragment() {


    private val resultManager = LearnResultManager()
    private lateinit var screenCanvas: View
    private lateinit var screenResult: View
    private lateinit var tvResultTitle: TextView
    private lateinit var tvOverallScore: TextView
    private lateinit var tvScoreMessage: TextView
    private lateinit var tvGoodLetters: TextView
    private lateinit var tvFocusLetters: TextView
    private lateinit var btnTryAgain: Button
    private lateinit var btnBackToMenu: Button
    private lateinit var drawView: DrawingView
    private lateinit var tvShapeName: TextView
    private lateinit var tvRoundInfo: TextView
    private lateinit var shapePreview: ShapePreviewView

    private val progressManager = ShapeProgressManager()
    private val maxRounds = 3
    private var strokeAttempts = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_shape, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawView = view.findViewById(R.id.shapeDrawView)
        tvShapeName = view.findViewById(R.id.tvShapeName)
        tvRoundInfo = view.findViewById(R.id.tvShapeRoundInfo)
        shapePreview = view.findViewById(R.id.shapePreview)
        screenCanvas = view.findViewById(R.id.screenCanvas)
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
            loadShape()
        }

        btnBackToMenu.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        drawView.currentMode = DrawingView.Mode.LEARN

        drawView.onLetterCompletedListener = {
            onShapeCompleted(progressManager.currentShape)
        }

        drawView.onStrokeFailedListener = {
            onStrokeFailed()
        }

        loadShape()
    }

    private fun loadShape() {
        val shape = progressManager.currentShape
        val color = ShapeRepository.randomWarmColor()

        tvShapeName.text = shape.name
        tvRoundInfo.text = "Round ${progressManager.attempt + 1} of $maxRounds"

        shapePreview.setShape(shape, color)

        // Convert Shape strokes to Letter format for DrawingView reuse
        val asLetter = shape.toLetterCompat()
        drawView.setLearningLetter(asLetter, progressManager.attempt)

        strokeAttempts = 0
    }

    private fun onShapeCompleted(shape: Shape) {
        val completedTries = progressManager.attempt + 1

        // Record accuracy
        val asLetter = shape.toLetterCompat()
        val strokeIndex = (drawView.currentStrokeIndex - 1).coerceAtLeast(0)
        if (strokeIndex < asLetter.strokes.size) {
            val accuracy = StrokeAccuracyCalculator.calculate(
                drawView.currentStrokePoints.toList(),
                asLetter.strokes[strokeIndex],
                drawView.width,
                drawView.height
            )
            resultManager.recordAccuracy(shape.name.first(), accuracy)
        }

        if (completedTries < maxRounds && completedTries < shape.strokes.size) {
            progressManager.advanceTries(shape)
            Snackbar.make(
                requireView(),
                "Great! Round ${progressManager.attempt + 1} — fewer dots! 💙",
                Snackbar.LENGTH_SHORT
            ).show()
            loadShape()
        } else {
            progressManager.advanceTries(shape)
            progressManager.nextShape()
            if (progressManager.isCompleted) {
                showResultScreen()
            } else {
                Snackbar.make(
                    requireView(),
                    "⭐ Amazing! Moving to ${progressManager.currentShape.name}!",
                    Snackbar.LENGTH_SHORT
                ).show()
                loadShape()
            }
        }
    }
    private fun showResultScreen() {
        val overall = resultManager.getOverallAccuracy()

        tvResultTitle.text = "You finished all shapes!"
        tvOverallScore.text = "Overall Score: ${overall.toInt()}%"
        tvScoreMessage.text = when {
            overall >= 90 -> "Amazing work! You're a drawing star! ⭐"
            overall >= 75 -> "Great job! Keep it up! 🎉"
            overall >= 60 -> "Good effort! Practice makes perfect! 💪"
            else -> "Keep practicing, you're getting better! 🌟"
        }

        val goodShapes = ShapeRepository.shapes
            .filter { resultManager.getAccuracy(it.name.first()) >= 75f }
            .joinToString(", ") { it.name }

        val focusShapes = ShapeRepository.shapes
            .filter { resultManager.getAccuracy(it.name.first()) < 75f }
            .joinToString(", ") { it.name }

        tvGoodLetters.text = if (goodShapes.isEmpty()) "Keep practicing!" else goodShapes
        tvFocusLetters.text = if (focusShapes.isEmpty()) "None — excellent!" else focusShapes

        screenCanvas.visibility = View.GONE
        screenResult.visibility = View.VISIBLE
    }
    private fun onStrokeFailed() {
        strokeAttempts++
        val remaining = 3 - strokeAttempts
        resultManager.recordAccuracy(progressManager.currentShape.name.first(), 20f)
        if (remaining > 0) {
            Snackbar.make(requireView(), "Oops! Try again — $remaining tries left 💪", Snackbar.LENGTH_SHORT).show()
        } else {
            strokeAttempts = 0
            Snackbar.make(requireView(), "No worries, let's try that again! 😊", Snackbar.LENGTH_SHORT).show()
        }
        drawView.clearUserStrokesOnly()
    }
    private fun getNextShapeName(): String {
        val shapes = ShapeRepository.shapes
        val currentIndex = shapes.indexOfFirst { it.name == progressManager.currentShape.name }
        return if (currentIndex < shapes.size - 1) shapes[currentIndex + 1].name else "the start!"
    }

    // Convert Shape → Letter so DrawingView can reuse all its existing logic
    private fun Shape.toLetterCompat(): Letter {
        return Letter(
            character = name.first(),
            strokes = strokes,
            outlineResId = outlineResId,
            arrowResId = R.drawable.ic_arrow
        )
    }
}
