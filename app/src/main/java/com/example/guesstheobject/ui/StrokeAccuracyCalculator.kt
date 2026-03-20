package com.example.guesstheobject.ui

import android.graphics.PointF
import kotlin.math.min
import kotlin.math.sqrt

object StrokeAccuracyCalculator {

    /**
     * Calculates accuracy (0-100) of how close the user's drawn path
     * was relative to the expected stroke path.
     */
    fun calculate(
        userPoints: List<PointF>,
        expectedStroke: Stroke,
        canvasWidth: Int,
        canvasHeight: Int
    ): Float {
        if (userPoints.isEmpty()) return 0f

        // Sample points along expected stroke path
        val expectedPoints = sampleStrokePoints(expectedStroke, canvasWidth, canvasHeight, 100)
        if (expectedPoints.isEmpty()) return 0f

        // For each user point, find the closest expected point distance
        val totalDistance = userPoints.sumOf { userPt ->
            expectedPoints.minOf { expPt ->
                distance(userPt, expPt).toDouble()
            }
        }

        val avgDistance = totalDistance / userPoints.size

        // Normalize: max acceptable distance is 15% of canvas width
        val maxAcceptableDistance = canvasWidth * 0.15f
        val accuracy = (1f - (avgDistance / maxAcceptableDistance)).coerceIn(0.0, 1.0)

        return (accuracy * 100).toFloat()
    }

    private fun distance(a: PointF, b: PointF): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx * dx + dy * dy)
    }

    private fun sampleStrokePoints(
        stroke: Stroke,
        canvasWidth: Int,
        canvasHeight: Int,
        count: Int
    ): List<PointF> {
        val result = mutableListOf<PointF>()
        val startX = stroke.start.x * canvasWidth
        val startY = stroke.start.y * canvasHeight
        result.add(PointF(startX, startY))

        var currentX = startX
        var currentY = startY

        for (segment in stroke.segments) {
            when (segment) {
                is LineTo -> {
                    val endX = segment.point.x * canvasWidth
                    val endY = segment.point.y * canvasHeight
                    for (i in 1..count) {
                        val t = i.toFloat() / count
                        result.add(PointF(
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
                        result.add(PointF(
                            mt*mt*mt*currentX + 3*mt*mt*t*cx1 + 3*mt*t*t*cx2 + t*t*t*endX,
                            mt*mt*mt*currentY + 3*mt*mt*t*cy1 + 3*mt*t*t*cy2 + t*t*t*endY
                        ))
                    }
                    currentX = endX
                    currentY = endY
                }
            }
        }
        return result
    }
}
