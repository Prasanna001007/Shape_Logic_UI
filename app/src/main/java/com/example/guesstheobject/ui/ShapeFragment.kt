package com.example.guesstheobject.ui

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.guesstheobject.R
import com.google.android.material.snackbar.Snackbar

class ShapeFragment : Fragment() {

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

        view.findViewById<View>(R.id.btnShapeClear).setOnClickListener {
            drawView.clearUserStrokesOnly()
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

        if (completedTries < maxRounds && completedTries < shape.strokes.size) {
            progressManager.advanceTries(shape)
            Snackbar.make(
                requireView(),
                "Great! Round ${progressManager.attempt + 1} — fewer dots this time! 💙",
                Snackbar.LENGTH_SHORT
            ).show()
            loadShape()
        } else {
            progressManager.advanceTries(shape)
            val next = getNextShapeName()
            Snackbar.make(
                requireView(),
                "⭐ Amazing! Moving to $next!",
                Snackbar.LENGTH_SHORT
            ).show()
            progressManager.nextShape()
            loadShape()
        }
    }

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
