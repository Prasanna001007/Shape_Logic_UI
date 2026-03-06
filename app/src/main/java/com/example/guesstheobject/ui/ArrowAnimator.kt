package com.example.guesstheobject.ui

import android.graphics.*
import android.view.View
import kotlin.math.*

class ArrowAnimator(private val view: View) {

    // Dashed orange guide line
    private val linePaint = Paint().apply {
        color = Color.rgb(255, 140, 0)
        strokeWidth = 10f
        style = Paint.Style.STROKE
        isAntiAlias = true
        pathEffect = DashPathEffect(floatArrayOf(18f, 10f), 0f)
        alpha = 180
    }

    // Arrowhead
    private val arrowPaint = Paint().apply {
        color = Color.rgb(255, 140, 0)
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        alpha = 220
    }

    // Green start dot
    private val startDotPaint = Paint().apply {
        color = Color.rgb(0, 180, 0)
        style = Paint.Style.FILL
        isAntiAlias = true
        alpha = 220
    }

    private val arrowHeadLength = 45f
    private val arrowHeadAngle = Math.toRadians(30.0)

    /**
     * Draws the arrow guide for the given stroke.
     * Samples points along the actual bezier curve so the
     * dashed line follows the curve correctly (not just start→end).
     * Arrowhead is drawn at the midpoint of the curve.
     * Green dot marks where the child should start.
     */
    fun drawArrow(canvas: Canvas, stroke: Stroke, canvasWidth: Int, canvasHeight: Int) {
        val points = sampleStrokePoints(stroke, canvasWidth, canvasHeight, 80)
        if (points.size < 2) return

        // Draw dashed path along the actual curve
        val path = Path()
        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }
        canvas.drawPath(path, linePaint)

        // Draw arrowhead at midpoint pointing in the direction of travel
        val midIndex = points.size / 2
        if (midIndex > 0) {
            drawArrowHead(canvas, points[midIndex - 1], points[midIndex])
        }

        // Green start dot
        canvas.drawCircle(points.first().x, points.first().y, 26f, startDotPaint)
    }

    private fun drawArrowHead(canvas: Canvas, from: PointF, to: PointF) {
        val angle = atan2((to.y - from.y).toDouble(), (to.x - from.x).toDouble())
        val leftX = to.x - arrowHeadLength * cos(angle - arrowHeadAngle).toFloat()
        val leftY = to.y - arrowHeadLength * sin(angle - arrowHeadAngle).toFloat()
        val rightX = to.x - arrowHeadLength * cos(angle + arrowHeadAngle).toFloat()
        val rightY = to.y - arrowHeadLength * sin(angle + arrowHeadAngle).toFloat()
        canvas.drawLine(to.x, to.y, leftX, leftY, arrowPaint)
        canvas.drawLine(to.x, to.y, rightX, rightY, arrowPaint)
    }

    /**
     * Samples [count] points evenly along the stroke path,
     * scaling normalized coordinates to canvas pixels.
     */
    private fun sampleStrokePoints(
        stroke: Stroke,
        canvasWidth: Int,
        canvasHeight: Int,
        count: Int
    ): List<PointF> {
        val result = mutableListOf<PointF>()
        val startX = stroke.start.x * canvasWidth
        val startY = stroke.start.y * canvasHeight

        // Collect all raw points along the path
        val raw = mutableListOf<PointF>()
        raw.add(PointF(startX, startY))

        var currentX = startX
        var currentY = startY

        for (segment in stroke.segments) {
            when (segment) {
                is LineTo -> {
                    val endX = segment.point.x * canvasWidth
                    val endY = segment.point.y * canvasHeight
                    for (i in 1..count) {
                        val t = i.toFloat() / count
                        raw.add(PointF(
                            currentX + (endX - currentX) * t,
                            currentY + (endY - currentY) * t
                        ))
                    }
                    currentX = endX
                    currentY = endY
                }
                is CubicTo -> {
                    val cx1 = segment.cp1.x * canvasWidth
                    val cy1 = segment.cp1.y * canvasHeight
                    val cx2 = segment.cp2.x * canvasWidth
                    val cy2 = segment.cp2.y * canvasHeight
                    val endX = segment.end.x * canvasWidth
                    val endY = segment.end.y * canvasHeight
                    for (i in 1..count) {
                        val t = i.toFloat() / count
                        val mt = 1 - t
                        raw.add(PointF(
                            mt*mt*mt*currentX + 3*mt*mt*t*cx1 + 3*mt*t*t*cx2 + t*t*t*endX,
                            mt*mt*mt*currentY + 3*mt*mt*t*cy1 + 3*mt*t*t*cy2 + t*t*t*endY
                        ))
                    }
                    currentX = endX
                    currentY = endY
                }
            }
        }

        // Subsample evenly so we always get exactly [count] points
        if (raw.size <= count) return raw
        val step = raw.size.toFloat() / count
        for (i in 0 until count) {
            val idx = (i * step).toInt().coerceIn(0, raw.size - 1)
            result.add(raw[idx])
        }
        result.add(raw.last())
        return result
    }
}