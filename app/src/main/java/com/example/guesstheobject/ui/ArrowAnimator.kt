package com.example.guesstheobject.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class ArrowAnimator(private val view: View) {

    // Dashed line paint — subtle guide, not overpowering
    private val linePaint = Paint().apply {
        color = Color.rgb(255, 140, 0) // warm orange — friendly for kids
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
        pathEffect = DashPathEffect(floatArrayOf(18f, 10f), 0f)
        alpha = 160
    }

    // Arrowhead paint — slightly bolder
    private val arrowPaint = Paint().apply {
        color = Color.rgb(255, 140, 0)
        strokeWidth = 6f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        alpha = 220
    }

    private val arrowHeadLength = 28f
    private val arrowHeadAngle = Math.toRadians(30.0)

    /**
     * Draws a dashed guide line along the stroke path with a
     * single arrowhead at the midpoint to show direction.
     *
     * Much cleaner than drawing an arrowhead at every segment.
     */
    fun drawArrow(canvas: Canvas, stroke: Stroke) {
        val points = stroke.points
        if (points.size < 2) return

        // Draw the dashed path
        val path = Path()
        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }
        canvas.drawPath(path, linePaint)

        // Draw ONE arrowhead at the midpoint of the stroke
        val midIndex = points.size / 2
        if (midIndex > 0) {
            val from = points[midIndex - 1]
            val to = points[midIndex]
            drawArrowHead(canvas, from, to)
        }

        // Also draw a small circle at the start to show where to begin
        val startPaint = Paint().apply {
            color = Color.rgb(0, 180, 0) // green dot = "start here"
            style = Paint.Style.FILL
            isAntiAlias = true
            alpha = 200
        }
        canvas.drawCircle(points.first().x, points.first().y, 14f, startPaint)
    }

    private fun drawArrowHead(canvas: Canvas, start: PointF, end: PointF) {
        val angle = atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble())

        val leftX = end.x - arrowHeadLength * cos(angle - arrowHeadAngle).toFloat()
        val leftY = end.y - arrowHeadLength * sin(angle - arrowHeadAngle).toFloat()
        val rightX = end.x - arrowHeadLength * cos(angle + arrowHeadAngle).toFloat()
        val rightY = end.y - arrowHeadLength * sin(angle + arrowHeadAngle).toFloat()

        canvas.drawLine(end.x, end.y, leftX, leftY, arrowPaint)
        canvas.drawLine(end.x, end.y, rightX, rightY, arrowPaint)
    }
}
