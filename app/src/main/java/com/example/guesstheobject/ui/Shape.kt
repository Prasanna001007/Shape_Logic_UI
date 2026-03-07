package com.example.guesstheobject.ui

// Represents a single drawable shape with stroke definitions
data class Shape(
    val name: String,
    val strokes: List<Stroke>,
    val outlineResId: Int,
    val fillColor: Int = 0 // set at runtime
)
