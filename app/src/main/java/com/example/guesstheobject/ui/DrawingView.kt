package com.example.guesstheobject.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
//old one


class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val strokes = mutableListOf<Stroke>()
    private var currentPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 8f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private var currentPath = Path()
    private var eraserMode = false
    private var eraserSize = 16f // default eraser size

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (stroke in strokes) {
            canvas.drawPath(stroke.path, stroke.paint)
        }
        canvas.drawPath(currentPath, currentPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> currentPath.lineTo(event.x, event.y)
            MotionEvent.ACTION_UP -> {
                strokes.add(
                    Stroke(
                        currentPath,
                        Paint(currentPaint).apply {
                            if (eraserMode) {
                                color = Color.WHITE // or background color
                                strokeWidth = eraserSize
                            }
                        }
                    )
                )
            }
        }
        invalidate()
        return true
    }

    fun clear() {
        strokes.clear()
        currentPath.reset()
        invalidate()
    }

    fun setPaintColor(color: Int) {
        currentPaint = Paint(currentPaint).apply {
            this.color = color
        }
        eraserMode = false
    }

    fun setStrokeWidth(width: Float) {
        currentPaint = Paint(currentPaint).apply {
            this.strokeWidth = width
        }
        eraserMode = false
    }

    fun enableEraser(size: Float = 16f) {
        eraserMode = true
        eraserSize = size
    }
}
