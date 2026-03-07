package com.example.guesstheobject.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ShapePreviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var shape: Shape? = null
    private var fillColor: Int = Color.LTGRAY

    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        alpha = 200
    }

    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
        alpha = 180
    }

    fun setShape(shape: Shape, color: Int) {
        this.shape = shape
        this.fillColor = color
        fillPaint.color = color
        // Slightly darker border
        strokePaint.color = darken(color, 0.8f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val s = shape ?: return
        if (width == 0 || height == 0) return

        val path = buildShapePath(s)
        canvas.drawPath(path, fillPaint)
        canvas.drawPath(path, strokePaint)
    }

    private fun buildShapePath(shape: Shape): Path {
        val path = Path()
        for (stroke in shape.strokes) {
            val startX = stroke.start.x * width
            val startY = stroke.start.y * height
            path.moveTo(startX, startY)
            var currentX = startX
            var currentY = startY
            for (segment in stroke.segments) {
                when (segment) {
                    is LineTo -> {
                        val ex = segment.point.x * width
                        val ey = segment.point.y * height
                        path.lineTo(ex, ey)
                        currentX = ex; currentY = ey
                    }
                    is CubicTo -> {
                        path.cubicTo(
                            segment.cp1.x * width, segment.cp1.y * height,
                            segment.cp2.x * width, segment.cp2.y * height,
                            segment.end.x * width, segment.end.y * height
                        )
                        currentX = segment.end.x * width
                        currentY = segment.end.y * height
                    }
                }
            }
        }
        path.close()
        return path
    }

    private fun darken(color: Int, factor: Float): Int {
        val r = (Color.red(color) * factor).toInt().coerceIn(0, 255)
        val g = (Color.green(color) * factor).toInt().coerceIn(0, 255)
        val b = (Color.blue(color) * factor).toInt().coerceIn(0, 255)
        return Color.rgb(r, g, b)
    }
}
