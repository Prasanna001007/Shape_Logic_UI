package com.example.guesstheobject.ui

import android.graphics.PointF

// Base type for all stroke segments
sealed class StrokeSegment

// Straight line to a point
data class LineTo(
    val point: PointF
) : StrokeSegment()

// Cubic bezier curve with two control points and an end point
data class CubicTo(
    val cp1: PointF,   // first control point
    val cp2: PointF,   // second control point
    val end: PointF    // destination point
) : StrokeSegment()

// A single stroke — has a start point and a list of segments
data class Stroke(
    val start: PointF,
    val segments: List<StrokeSegment>
) {
    // Convenience to get the final point of this stroke
    val endPoint: PointF
        get() = when (val last = segments.last()) {
            is LineTo -> last.point
            is CubicTo -> last.end
        }
}
