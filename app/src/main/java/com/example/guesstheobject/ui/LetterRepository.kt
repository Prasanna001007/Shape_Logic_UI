package com.example.guesstheobject.ui

import android.graphics.PointF
import com.example.guesstheobject.R

object LetterStrokeRepository {

    val letters: List<Letter> = listOf(
        Letter(
            'A',
            listOf(
                Stroke(listOf(PointF(0.1f, 0.9f), PointF(0.5f, 0.1f))),
                Stroke(listOf(PointF(0.5f, 0.1f), PointF(0.9f, 0.9f))),
                Stroke(listOf(PointF(0.25f, 0.5f), PointF(0.75f, 0.5f)))
            ),
            outlineResId = R.drawable.letter_a_outline,
            arrowResId = R.drawable.ic_arrow
        ),
        Letter(
            'B',
            listOf(
                // Stroke 1: vertical line down
                Stroke(listOf(
                    PointF(0.2f, 0.1f),
                    PointF(0.2f, 0.9f)
                )),
                // Stroke 2: top bump (curves right from top)
                Stroke(listOf(
                    PointF(0.2f, 0.1f),
                    PointF(0.35f, 0.12f),
                    PointF(0.55f, 0.18f),
                    PointF(0.62f, 0.28f),
                    PointF(0.55f, 0.38f),
                    PointF(0.35f, 0.45f),
                    PointF(0.2f, 0.5f)
                )),
                // Stroke 3: bottom bump (curves right from middle, bigger)
                Stroke(listOf(
                    PointF(0.2f, 0.5f),
                    PointF(0.38f, 0.52f),
                    PointF(0.65f, 0.58f),
                    PointF(0.72f, 0.68f),
                    PointF(0.65f, 0.78f),
                    PointF(0.38f, 0.88f),
                    PointF(0.2f, 0.9f)
                ))
            ),
            outlineResId = R.drawable.letter_b_outline,
            arrowResId = R.drawable.ic_arrow
        )
        // Extend with C-Z
    )

    fun getLetter(char: Char): Letter? {
        return letters.find { it.character.equals(char, true) }
    }
}