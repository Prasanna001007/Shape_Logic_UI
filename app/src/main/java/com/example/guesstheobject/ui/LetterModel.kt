package com.example.guesstheobject.ui

data class Letter(
    val character: Char,
    val strokes: List<Stroke>,
    val outlineResId: Int,   // drawable for letter outline
    val arrowResId: Int      // drawable for stroke guidance arrow
)