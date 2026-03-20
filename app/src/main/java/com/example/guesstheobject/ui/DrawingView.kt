package com.example.guesstheobject.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow
import androidx.core.view.doOnLayout
import kotlin.math.sqrt

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    enum class Mode { FREE, LEARN }

    private val arrowAnimator = ArrowAnimator(this)
    var currentMode = Mode.FREE
    private var currentLetter: Letter? = null
     var currentStrokeIndex = 0
    private var isTransitioning = false

    // Tolerance in pixels for stroke endpoint validation
    private val tolerance = 80f

    // ── Paint for user drawing ────────────────────────────
    private val userPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 12f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
    }

    // ── Paint for pre-drawn scaffold strokes ─────────────
    private val scaffoldPaint = Paint().apply {
        color = Color.rgb(100, 149, 237)
        strokeWidth = 14f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        alpha = 180
    }

    // ── Paint for letter outline ghost guide ──────────────
    private val outlinePaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 18f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        alpha = 80
    }

    // ── Separate paths ────────────────────────────────────
    private val userPath = Path()       // user's live drawing
    private val scaffoldPath = Path()   // pre-drawn helper strokes

    // ── Callbacks ─────────────────────────────────────────
    var onLetterCompletedListener: (() -> Unit)? = null
    var onStrokeFailedListener: (() -> Unit)? = null

    // ─────────────────────────────────────────────────────
    // PUBLIC API
    // ─────────────────────────────────────────────────────

    fun setMode(mode: Mode) {
        currentMode = mode
        clearAll()
    }

    fun setLetter(letter: Letter?) {
        if (letter == null) return
        setLearningLetter(letter, 0)
    }

    fun clear() { clearAll() }

    fun clearUserStrokesOnly() {
        userPath.reset()
        invalidate()
    }

    fun enableEraser() { userPaint.color = Color.WHITE }
    fun setPaintColor(color: Int) { userPaint.color = color }
    fun setStrokeWidth(width: Float) { userPaint.strokeWidth = width }

    fun setLearningLetter(letter: Letter, tries: Int = 0) {
        currentLetter = letter
        currentStrokeIndex = tries
        isTransitioning = false
        clearAll()
        if (width > 0 && height > 0) {
            preDrawScaffoldStrokes(letter, tries)
        } else {
            doOnLayout { preDrawScaffoldStrokes(letter, tries) }
        }
    }

    // ─────────────────────────────────────────────────────
    // INTERNAL
    // ─────────────────────────────────────────────────────

    private fun clearAll() {
        userPath.reset()
        scaffoldPath.reset()
        invalidate()
    }

    /**
     * Builds an Android Path from a Stroke using the correct
     * drawing primitive for each segment type (lineTo or cubicTo).
     * All normalized coordinates are scaled to canvas pixels.
     */
    private fun buildPath(stroke: Stroke): Path {
        val path = Path()
        if (width == 0 || height == 0) return path

        val startX = stroke.start.x * width
        val startY = stroke.start.y * height
        path.moveTo(startX, startY)

        for (segment in stroke.segments) {
            when (segment) {
                is LineTo -> {
                    path.lineTo(
                        segment.point.x * width,
                        segment.point.y * height
                    )
                }
                is CubicTo -> {
                    path.cubicTo(
                        segment.cp1.x * width,  segment.cp1.y * height,
                        segment.cp2.x * width,  segment.cp2.y * height,
                        segment.end.x * width,  segment.end.y * height
                    )
                }
            }
        }
        return path
    }

    /**
     * Pre-draws [tries] completed strokes into scaffoldPath in blue.
     */
    private fun preDrawScaffoldStrokes(letter: Letter, tries: Int) {
        scaffoldPath.reset()
        for (i in 0 until tries.coerceAtMost(letter.strokes.size)) {
            scaffoldPath.addPath(buildPath(letter.strokes[i]))
        }
        invalidate()
    }

    /**
     * Returns the scaled start and end pixel points of a stroke.
     * Used for validation and arrow drawing.
     */
    private fun getScaledEndpoints(stroke: Stroke): Pair<PointF, PointF> {
        val start = PointF(stroke.start.x * width, stroke.start.y * height)
        val end = stroke.endPoint.let { PointF(it.x * width, it.y * height) }
        return Pair(start, end)
    }

    // ─────────────────────────────────────────────────────
    // DRAWING
    // ─────────────────────────────────────────────────────

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (currentMode == Mode.LEARN && currentLetter != null) {
            // Faint full letter outline
            drawLetterOutline(canvas)

            // Blue scaffold strokes
            canvas.drawPath(scaffoldPath, scaffoldPaint)

            // Arrow for current stroke
            if (currentStrokeIndex < currentLetter!!.strokes.size) {
                val currentStroke = currentLetter!!.strokes[currentStrokeIndex]
                arrowAnimator.drawArrow(canvas, currentStroke, width, height)
            }
        }

        canvas.drawPath(userPath, userPaint)
    }

    private fun drawLetterOutline(canvas: Canvas) {
        val letter = currentLetter ?: return
        for (stroke in letter.strokes) {
            drawDottedStroke(canvas, stroke)
        }
    }

    private fun drawDottedStroke(canvas: Canvas, stroke: Stroke) {
        if (width == 0 || height == 0) return

        val dotPaint = Paint().apply {
            color = Color.rgb(200, 200, 200)
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val dotRadius = minOf(width, height) * 0.030f
        val dotSpacing = minOf(width, height) * 0.80f
        // Sample points along the stroke and draw a dot every dotSpacing pixels
        val points = sampleStrokePoints(stroke, 500)
        var accumulated = 0f
        var lastDotX = -999f
        var lastDotY = -999f

        for (pt in points) {
            if (lastDotX < -998f) {
                canvas.drawCircle(pt.x, pt.y, dotRadius, dotPaint)
                lastDotX = pt.x
                lastDotY = pt.y
                continue
            }
            val dx = pt.x - lastDotX
            val dy = pt.y - lastDotY
            accumulated += Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
            if (accumulated >= dotSpacing) {
                canvas.drawCircle(pt.x, pt.y, dotRadius, dotPaint)
                lastDotX = pt.x
                lastDotY = pt.y
                accumulated = 0f
            }
        }
    }

    private fun sampleStrokePoints(stroke: Stroke, samples: Int): List<PointF> {
        val result = mutableListOf<PointF>()
        val startX = stroke.start.x * width
        val startY = stroke.start.y * height
        result.add(PointF(startX, startY))

        var currentX = startX
        var currentY = startY

        for (segment in stroke.segments) {
            when (segment) {
                is LineTo -> {
                    val endX = segment.point.x * width
                    val endY = segment.point.y * height
                    for (i in 1..samples) {
                        val t = i.toFloat() / samples
                        result.add(PointF(
                            currentX + (endX - currentX) * t,
                            currentY + (endY - currentY) * t
                        ))
                    }
                    currentX = endX
                    currentY = endY
                }
                is CubicTo -> {
                    val cx1 = segment.cp1.x * width
                    val cy1 = segment.cp1.y * height
                    val cx2 = segment.cp2.x * width
                    val cy2 = segment.cp2.y * height
                    val endX = segment.end.x * width
                    val endY = segment.end.y * height
                    for (i in 1..samples) {
                        val t = i.toFloat() / samples
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
    // ─────────────────────────────────────────────────────
    // TOUCH
    // ─────────────────────────────────────────────────────

    private var strokeStartX = 0f
    private var strokeStartY = 0f


    val currentStrokePoints = mutableListOf<PointF>()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                strokeStartX = event.x
                strokeStartY = event.y
                userPath.moveTo(event.x, event.y)
                currentStrokePoints.clear()
                currentStrokePoints.add(PointF(event.x, event.y))
            }

// In ACTION_MOVE, also record points:
            MotionEvent.ACTION_MOVE -> {
                userPath.lineTo(event.x, event.y)
                currentStrokePoints.add(PointF(event.x, event.y))
            }

// In ACTION_UP, also record last point:
            MotionEvent.ACTION_UP -> {
                userPath.lineTo(event.x, event.y)
                currentStrokePoints.add(PointF(event.x, event.y))
                if (currentMode == Mode.LEARN) validateStroke(event.x, event.y, strokeStartX, strokeStartY)
            }
        }
        invalidate()
        return true
    }

    // ─────────────────────────────────────────────────────
    // STROKE VALIDATION
    // ─────────────────────────────────────────────────────

    private fun validateStroke(endX: Float, endY: Float, startX: Float, startY: Float) {
        if (isTransitioning) return

        val letter = currentLetter ?: return
        if (currentStrokeIndex >= letter.strokes.size) return
        android.util.Log.d("STROKE", "Validating stroke $currentStrokeIndex, endX=$endX endY=$endY, expectedEnd=${getScaledEndpoints(letter.strokes[currentStrokeIndex]).second}")



        val expectedStroke = letter.strokes[currentStrokeIndex]
        val (expectedStart, expectedEnd) = getScaledEndpoints(expectedStroke)

        val startNearTarget = distance(startX, startY, expectedStart.x, expectedStart.y) <= tolerance
        val endNearTarget = distance(endX, endY, expectedEnd.x, expectedEnd.y) <= tolerance

        if (startNearTarget && endNearTarget) {
            currentStrokeIndex++
            android.util.Log.d("STROKE", "Stroke accepted! currentStrokeIndex is now $currentStrokeIndex")
            scaffoldPath.addPath(buildPath(expectedStroke))
            userPath.reset()
            invalidate()

            if (currentStrokeIndex >= letter.strokes.size) {
                isTransitioning = true
                post { onLetterCompletedListener?.invoke() }
            }
        } else {
            userPath.reset()
            invalidate()
            onStrokeFailedListener?.invoke()
        }
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float) =
        sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
}
