package com.example.guesstheobject.ui

import android.graphics.PointF

// Represents a single stroke of a letter
data class Stroke(
    val points: List<PointF>
)