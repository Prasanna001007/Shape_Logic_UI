package com.example.guesstheobject.ui

import android.graphics.PointF
import com.example.guesstheobject.R

object ShapeRepository {

    val shapes: List<Shape> = listOf(

        // ── CIRCLE ─────────────────────────────────────────
        // One continuous stroke — full circle using 2 cubic beziers
        Shape("Circle", listOf(
            Stroke(PointF(0.5f, 0.1f), listOf(
                CubicTo(PointF(0.9f, 0.1f), PointF(0.9f, 0.9f), PointF(0.5f, 0.9f)),
                CubicTo(PointF(0.1f, 0.9f), PointF(0.1f, 0.1f), PointF(0.5f, 0.1f))
            ))
        ), R.drawable.shape_circle_outline),

        // ── SQUARE ─────────────────────────────────────────
        // 4 strokes — top, right, bottom, left
        Shape("Square", listOf(
            Stroke(PointF(0.15f, 0.15f), listOf(LineTo(PointF(0.85f, 0.15f)))),
            Stroke(PointF(0.85f, 0.15f), listOf(LineTo(PointF(0.85f, 0.85f)))),
            Stroke(PointF(0.85f, 0.85f), listOf(LineTo(PointF(0.15f, 0.85f)))),
            Stroke(PointF(0.15f, 0.85f), listOf(LineTo(PointF(0.15f, 0.15f))))
        ), R.drawable.shape_square_outline),

        // ── TRIANGLE ───────────────────────────────────────
        // 3 strokes — left side, right side, base
        Shape("Triangle", listOf(
            Stroke(PointF(0.5f, 0.1f), listOf(LineTo(PointF(0.1f, 0.9f)))),
            Stroke(PointF(0.1f, 0.9f), listOf(LineTo(PointF(0.9f, 0.9f)))),
            Stroke(PointF(0.9f, 0.9f), listOf(LineTo(PointF(0.5f, 0.1f))))
        ), R.drawable.shape_triangle_outline),

        // ── RECTANGLE ──────────────────────────────────────
        // 4 strokes — top, right, bottom, left
        Shape("Rectangle", listOf(
            Stroke(PointF(0.1f, 0.25f), listOf(LineTo(PointF(0.9f, 0.25f)))),
            Stroke(PointF(0.9f, 0.25f), listOf(LineTo(PointF(0.9f, 0.75f)))),
            Stroke(PointF(0.9f, 0.75f), listOf(LineTo(PointF(0.1f, 0.75f)))),
            Stroke(PointF(0.1f, 0.75f), listOf(LineTo(PointF(0.1f, 0.25f))))
        ), R.drawable.shape_rectangle_outline),

        // ── DIAMOND ────────────────────────────────────────
        // 4 strokes — top-left, bottom-left, bottom-right, top-right
        Shape("Diamond", listOf(
            Stroke(PointF(0.5f, 0.1f), listOf(LineTo(PointF(0.1f, 0.5f)))),
            Stroke(PointF(0.1f, 0.5f), listOf(LineTo(PointF(0.5f, 0.9f)))),
            Stroke(PointF(0.5f, 0.9f), listOf(LineTo(PointF(0.9f, 0.5f)))),
            Stroke(PointF(0.9f, 0.5f), listOf(LineTo(PointF(0.5f, 0.1f))))
        ), R.drawable.shape_diamond_outline),

        // ── STAR ───────────────────────────────────────────
        // 5 strokes — each point of the star
        Shape("Star", listOf(
            Stroke(PointF(0.5f, 0.1f), listOf(LineTo(PointF(0.63f, 0.38f)))),
            Stroke(PointF(0.63f, 0.38f), listOf(LineTo(PointF(0.9f, 0.38f)))),
            Stroke(PointF(0.9f, 0.38f), listOf(LineTo(PointF(0.69f, 0.57f)))),
            Stroke(PointF(0.69f, 0.57f), listOf(LineTo(PointF(0.79f, 0.85f)))),
            Stroke(PointF(0.79f, 0.85f), listOf(LineTo(PointF(0.5f, 0.68f)))),
            Stroke(PointF(0.5f, 0.68f), listOf(LineTo(PointF(0.21f, 0.85f)))),
            Stroke(PointF(0.21f, 0.85f), listOf(LineTo(PointF(0.31f, 0.57f)))),
            Stroke(PointF(0.31f, 0.57f), listOf(LineTo(PointF(0.1f, 0.38f)))),
            Stroke(PointF(0.1f, 0.38f), listOf(LineTo(PointF(0.37f, 0.38f)))),
            Stroke(PointF(0.37f, 0.38f), listOf(LineTo(PointF(0.5f, 0.1f))))
        ), R.drawable.shape_star_outline),

        // ── HEART ──────────────────────────────────────────
        // 2 strokes — left bump, right bump meeting at bottom
        Shape("Heart", listOf(
            Stroke(PointF(0.5f, 0.35f), listOf(
                CubicTo(PointF(0.5f, 0.2f), PointF(0.15f, 0.2f), PointF(0.15f, 0.45f)),
                CubicTo(PointF(0.15f, 0.65f), PointF(0.5f, 0.8f), PointF(0.5f, 0.9f))
            )),
            Stroke(PointF(0.5f, 0.35f), listOf(
                CubicTo(PointF(0.5f, 0.2f), PointF(0.85f, 0.2f), PointF(0.85f, 0.45f)),
                CubicTo(PointF(0.85f, 0.65f), PointF(0.5f, 0.8f), PointF(0.5f, 0.9f))
            ))
        ), R.drawable.shape_heart_outline),

        // ── PENTAGON ───────────────────────────────────────
        // 5 strokes — top, top-right, bottom-right, bottom-left, top-left
        Shape("Pentagon", listOf(
            Stroke(PointF(0.5f, 0.1f), listOf(LineTo(PointF(0.88f, 0.38f)))),
            Stroke(PointF(0.88f, 0.38f), listOf(LineTo(PointF(0.73f, 0.82f)))),
            Stroke(PointF(0.73f, 0.82f), listOf(LineTo(PointF(0.27f, 0.82f)))),
            Stroke(PointF(0.27f, 0.82f), listOf(LineTo(PointF(0.12f, 0.38f)))),
            Stroke(PointF(0.12f, 0.38f), listOf(LineTo(PointF(0.5f, 0.1f))))
        ), R.drawable.shape_pentagon_outline),

        // ── HEXAGON ────────────────────────────────────────
        // 6 strokes — one per side
        Shape("Hexagon", listOf(
            Stroke(PointF(0.5f, 0.1f), listOf(LineTo(PointF(0.85f, 0.3f)))),
            Stroke(PointF(0.85f, 0.3f), listOf(LineTo(PointF(0.85f, 0.7f)))),
            Stroke(PointF(0.85f, 0.7f), listOf(LineTo(PointF(0.5f, 0.9f)))),
            Stroke(PointF(0.5f, 0.9f), listOf(LineTo(PointF(0.15f, 0.7f)))),
            Stroke(PointF(0.15f, 0.7f), listOf(LineTo(PointF(0.15f, 0.3f)))),
            Stroke(PointF(0.15f, 0.3f), listOf(LineTo(PointF(0.5f, 0.1f))))
        ), R.drawable.shape_hexagon_outline),

        // ── ARROW ──────────────────────────────────────────
        // 3 strokes — shaft, top wing, bottom wing
        Shape("Arrow", listOf(
            Stroke(PointF(0.1f, 0.5f), listOf(LineTo(PointF(0.9f, 0.5f)))),
            Stroke(PointF(0.9f, 0.5f), listOf(LineTo(PointF(0.65f, 0.25f)))),
            Stroke(PointF(0.9f, 0.5f), listOf(LineTo(PointF(0.65f, 0.75f))))
        ), R.drawable.shape_arrow_outline)
    )

    fun getShape(name: String): Shape? {
        return shapes.find { it.name.equals(name, true) }
    }

    // Warm, soft colors — not too bright, not too dark
    val warmColors = listOf(
        0xFFE8A87C.toInt(), // warm peach
        0xFFE8C07C.toInt(), // warm gold
        0xFFE8907C.toInt(), // warm salmon
        0xFFD4A574.toInt(), // warm tan
        0xFFE8B4A0.toInt(), // warm rose
        0xFFD4B896.toInt(), // warm sand
        0xFFE8C4A0.toInt(), // warm apricot
        0xFFCFA882.toInt()  // warm caramel
    )

    fun randomWarmColor(): Int = warmColors.random()
}
