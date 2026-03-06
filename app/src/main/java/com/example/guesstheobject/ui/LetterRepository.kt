package com.example.guesstheobject.ui

import android.graphics.PointF
import com.example.guesstheobject.R

object LetterStrokeRepository {

    val letters: List<Letter> = listOf(

        // ── A ──────────────────────────────────────────────
        Letter('A', listOf(
            Stroke(PointF(0.15f, 0.9f), listOf(
                CubicTo(PointF(0.25f, 0.65f), PointF(0.35f, 0.4f), PointF(0.5f, 0.1f))
            )),
            Stroke(PointF(0.5f, 0.1f), listOf(
                CubicTo(PointF(0.65f, 0.4f), PointF(0.75f, 0.65f), PointF(0.85f, 0.9f))
            )),
            Stroke(PointF(0.28f, 0.55f), listOf(
                LineTo(PointF(0.72f, 0.55f))
            ))
        ), R.drawable.letter_a_outline, R.drawable.ic_arrow),

        // ── B ──────────────────────────────────────────────
        Letter('B', listOf(
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.9f))
            )),
            Stroke(PointF(0.2f, 0.1f), listOf(
                CubicTo(PointF(0.7f, 0.1f), PointF(0.7f, 0.5f), PointF(0.2f, 0.5f))
            )),
            Stroke(PointF(0.2f, 0.5f), listOf(
                CubicTo(PointF(0.8f, 0.5f), PointF(0.8f, 0.9f), PointF(0.2f, 0.9f))
            ))
        ), R.drawable.letter_b_outline, R.drawable.ic_arrow),

        // ── C ──────────────────────────────────────────────
        Letter('C', listOf(
            Stroke(PointF(0.75f, 0.25f), listOf(
                CubicTo(PointF(0.55f, 0.05f), PointF(0.2f, 0.05f), PointF(0.15f, 0.5f)),
                CubicTo(PointF(0.2f, 0.95f), PointF(0.55f, 0.95f), PointF(0.75f, 0.75f))
            ))
        ), R.drawable.letter_c_outline, R.drawable.ic_arrow),

        // ── D ──────────────────────────────────────────────
        Letter('D', listOf(
            Stroke(PointF(0.25f, 0.1f), listOf(
                LineTo(PointF(0.25f, 0.9f))
            )),
            Stroke(PointF(0.25f, 0.1f), listOf(
                CubicTo(PointF(0.75f, 0.1f), PointF(0.75f, 0.9f), PointF(0.25f, 0.9f))
            ))
        ), R.drawable.letter_d_outline, R.drawable.ic_arrow),

        // ── E ──────────────────────────────────────────────
        Letter('E', listOf(
            Stroke(PointF(0.7f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.1f))
            )),
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.9f))
            )),
            Stroke(PointF(0.2f, 0.5f), listOf(
                LineTo(PointF(0.6f, 0.5f))
            )),
            Stroke(PointF(0.2f, 0.9f), listOf(
                LineTo(PointF(0.7f, 0.9f))
            ))
        ), R.drawable.letter_e_outline, R.drawable.ic_arrow),

        // ── F ──────────────────────────────────────────────
        Letter('F', listOf(
            Stroke(PointF(0.7f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.1f))
            )),
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.9f))
            )),
            Stroke(PointF(0.2f, 0.5f), listOf(
                LineTo(PointF(0.6f, 0.5f))
            ))
        ), R.drawable.letter_f_outline, R.drawable.ic_arrow),

        // ── G ──────────────────────────────────────────────
        Letter('G', listOf(
            Stroke(PointF(0.75f, 0.25f), listOf(
                CubicTo(PointF(0.55f, 0.05f), PointF(0.2f, 0.05f), PointF(0.15f, 0.5f)),
                CubicTo(PointF(0.2f, 0.95f), PointF(0.55f, 0.95f), PointF(0.75f, 0.75f)),
                LineTo(PointF(0.75f, 0.5f))
            )),
            Stroke(PointF(0.75f, 0.5f), listOf(
                LineTo(PointF(0.5f, 0.5f))
            ))
        ), R.drawable.letter_g_outline, R.drawable.ic_arrow),

        // ── H ──────────────────────────────────────────────
        Letter('H', listOf(
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.9f))
            )),
            Stroke(PointF(0.8f, 0.1f), listOf(
                LineTo(PointF(0.8f, 0.9f))
            )),
            Stroke(PointF(0.2f, 0.5f), listOf(
                LineTo(PointF(0.8f, 0.5f))
            ))
        ), R.drawable.letter_h_outline, R.drawable.ic_arrow),

        // ── I ──────────────────────────────────────────────
        Letter('I', listOf(
            Stroke(PointF(0.3f, 0.1f), listOf(
                LineTo(PointF(0.7f, 0.1f))
            )),
            Stroke(PointF(0.5f, 0.1f), listOf(
                LineTo(PointF(0.5f, 0.9f))
            )),
            Stroke(PointF(0.3f, 0.9f), listOf(
                LineTo(PointF(0.7f, 0.9f))
            ))
        ), R.drawable.letter_i_outline, R.drawable.ic_arrow),

        // ── J ──────────────────────────────────────────────
        Letter('J', listOf(
            Stroke(PointF(0.4f, 0.1f), listOf(
                LineTo(PointF(0.7f, 0.1f))
            )),
            Stroke(PointF(0.6f, 0.1f), listOf(
                LineTo(PointF(0.6f, 0.75f)),
                CubicTo(PointF(0.6f, 0.95f), PointF(0.35f, 0.95f), PointF(0.25f, 0.75f))
            ))
        ), R.drawable.letter_j_outline, R.drawable.ic_arrow),

        // ── K ──────────────────────────────────────────────
        Letter('K', listOf(
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.9f))
            )),
            Stroke(PointF(0.75f, 0.1f), listOf(
                CubicTo(PointF(0.55f, 0.25f), PointF(0.35f, 0.4f), PointF(0.2f, 0.5f))
            )),
            Stroke(PointF(0.2f, 0.5f), listOf(
                CubicTo(PointF(0.35f, 0.6f), PointF(0.55f, 0.75f), PointF(0.75f, 0.9f))
            ))
        ), R.drawable.letter_k_outline, R.drawable.ic_arrow),

        // ── L ──────────────────────────────────────────────
        Letter('L', listOf(
            Stroke(PointF(0.25f, 0.1f), listOf(
                LineTo(PointF(0.25f, 0.9f))
            )),
            Stroke(PointF(0.25f, 0.9f), listOf(
                LineTo(PointF(0.75f, 0.9f))
            ))
        ), R.drawable.letter_l_outline, R.drawable.ic_arrow),

        // ── M ──────────────────────────────────────────────
        Letter('M', listOf(
            Stroke(PointF(0.1f, 0.9f), listOf(
                LineTo(PointF(0.1f, 0.1f))
            )),
            Stroke(PointF(0.1f, 0.1f), listOf(
                LineTo(PointF(0.5f, 0.6f))
            )),
            Stroke(PointF(0.5f, 0.6f), listOf(
                LineTo(PointF(0.9f, 0.1f))
            )),
            Stroke(PointF(0.9f, 0.1f), listOf(
                LineTo(PointF(0.9f, 0.9f))
            ))
        ), R.drawable.letter_m_outline, R.drawable.ic_arrow),

        // ── N ──────────────────────────────────────────────
        Letter('N', listOf(
            Stroke(PointF(0.2f, 0.9f), listOf(
                LineTo(PointF(0.2f, 0.1f))
            )),
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.8f, 0.9f))
            )),
            Stroke(PointF(0.8f, 0.9f), listOf(
                LineTo(PointF(0.8f, 0.1f))
            ))
        ), R.drawable.letter_n_outline, R.drawable.ic_arrow),

        // ── O ──────────────────────────────────────────────
        Letter('O', listOf(
            Stroke(PointF(0.5f, 0.1f), listOf(
                CubicTo(PointF(0.9f, 0.1f), PointF(0.9f, 0.9f), PointF(0.5f, 0.9f)),
                CubicTo(PointF(0.1f, 0.9f), PointF(0.1f, 0.1f), PointF(0.5f, 0.1f))
            ))
        ), R.drawable.letter_o_outline, R.drawable.ic_arrow),

        // ── P ──────────────────────────────────────────────
        Letter('P', listOf(
            Stroke(PointF(0.2f, 0.9f), listOf(
                LineTo(PointF(0.2f, 0.1f))
            )),
            Stroke(PointF(0.2f, 0.1f), listOf(
                CubicTo(PointF(0.75f, 0.1f), PointF(0.75f, 0.5f), PointF(0.2f, 0.5f))
            ))
        ), R.drawable.letter_p_outline, R.drawable.ic_arrow),

        // ── Q ──────────────────────────────────────────────
        Letter('Q', listOf(
            Stroke(PointF(0.5f, 0.1f), listOf(
                CubicTo(PointF(0.9f, 0.1f), PointF(0.9f, 0.9f), PointF(0.5f, 0.9f)),
                CubicTo(PointF(0.1f, 0.9f), PointF(0.1f, 0.1f), PointF(0.5f, 0.1f))
            )),
            Stroke(PointF(0.6f, 0.7f), listOf(
                LineTo(PointF(0.85f, 0.95f))
            ))
        ), R.drawable.letter_q_outline, R.drawable.ic_arrow),

        // ── R ──────────────────────────────────────────────
        Letter('R', listOf(
            Stroke(PointF(0.2f, 0.9f), listOf(
                LineTo(PointF(0.2f, 0.1f))
            )),
            Stroke(PointF(0.2f, 0.1f), listOf(
                CubicTo(PointF(0.75f, 0.1f), PointF(0.75f, 0.5f), PointF(0.2f, 0.5f))
            )),
            Stroke(PointF(0.2f, 0.5f), listOf(
                CubicTo(PointF(0.5f, 0.5f), PointF(0.6f, 0.65f), PointF(0.75f, 0.9f))
            ))
        ), R.drawable.letter_r_outline, R.drawable.ic_arrow),

        // ── S ──────────────────────────────────────────────
        Letter('S', listOf(
            Stroke(PointF(0.75f, 0.2f), listOf(
                CubicTo(PointF(0.6f, 0.05f), PointF(0.25f, 0.05f), PointF(0.2f, 0.3f)),
                CubicTo(PointF(0.2f, 0.5f), PointF(0.8f, 0.5f), PointF(0.8f, 0.7f)),
                CubicTo(PointF(0.75f, 0.95f), PointF(0.4f, 0.95f), PointF(0.25f, 0.8f))
            ))
        ), R.drawable.letter_s_outline, R.drawable.ic_arrow),

        // ── T ──────────────────────────────────────────────
        Letter('T', listOf(
            Stroke(PointF(0.15f, 0.1f), listOf(
                LineTo(PointF(0.85f, 0.1f))
            )),
            Stroke(PointF(0.5f, 0.1f), listOf(
                LineTo(PointF(0.5f, 0.9f))
            ))
        ), R.drawable.letter_t_outline, R.drawable.ic_arrow),

        // ── U ──────────────────────────────────────────────
        Letter('U', listOf(
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.7f)),
                CubicTo(PointF(0.2f, 0.95f), PointF(0.8f, 0.95f), PointF(0.8f, 0.7f)),
                LineTo(PointF(0.8f, 0.1f))
            ))
        ), R.drawable.letter_u_outline, R.drawable.ic_arrow),

        // ── V ──────────────────────────────────────────────
        Letter('V', listOf(
            Stroke(PointF(0.15f, 0.1f), listOf(
                LineTo(PointF(0.5f, 0.9f))
            )),
            Stroke(PointF(0.5f, 0.9f), listOf(
                LineTo(PointF(0.85f, 0.1f))
            ))
        ), R.drawable.letter_v_outline, R.drawable.ic_arrow),

        // ── W ──────────────────────────────────────────────
        Letter('W', listOf(
            Stroke(PointF(0.1f, 0.1f), listOf(
                LineTo(PointF(0.25f, 0.9f))
            )),
            Stroke(PointF(0.25f, 0.9f), listOf(
                LineTo(PointF(0.5f, 0.5f))
            )),
            Stroke(PointF(0.5f, 0.5f), listOf(
                LineTo(PointF(0.75f, 0.9f))
            )),
            Stroke(PointF(0.75f, 0.9f), listOf(
                LineTo(PointF(0.9f, 0.1f))
            ))
        ), R.drawable.letter_w_outline, R.drawable.ic_arrow),

        // ── X ──────────────────────────────────────────────
        Letter('X', listOf(
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.8f, 0.9f))
            )),
            Stroke(PointF(0.8f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.9f))
            ))
        ), R.drawable.letter_x_outline, R.drawable.ic_arrow),

        // ── Y ──────────────────────────────────────────────
        Letter('Y', listOf(
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.5f, 0.5f))
            )),
            Stroke(PointF(0.8f, 0.1f), listOf(
                LineTo(PointF(0.5f, 0.5f))
            )),
            Stroke(PointF(0.5f, 0.5f), listOf(
                LineTo(PointF(0.5f, 0.9f))
            ))
        ), R.drawable.letter_y_outline, R.drawable.ic_arrow),

        // ── Z ──────────────────────────────────────────────
        Letter('Z', listOf(
            Stroke(PointF(0.2f, 0.1f), listOf(
                LineTo(PointF(0.8f, 0.1f))
            )),
            Stroke(PointF(0.8f, 0.1f), listOf(
                LineTo(PointF(0.2f, 0.9f))
            )),
            Stroke(PointF(0.2f, 0.9f), listOf(
                LineTo(PointF(0.8f, 0.9f))
            ))
        ), R.drawable.letter_z_outline, R.drawable.ic_arrow)
    )

    fun getLetter(char: Char): Letter? {
        return letters.find { it.character.equals(char, true) }
    }
}