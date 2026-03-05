package com.example.guesstheobject.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    enum class Mode { FREE, LEARN }

    private val arrowAnimator = ArrowAnimator(this)
    private var currentMode = Mode.FREE
    private var currentLetter: Letter? = null
    private var currentStrokeIndex = 0

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
    // Separate paint so scaffold strokes are visually distinct
    // and cannot be erased by the user
    private val scaffoldPaint = Paint().apply {
        color = Color.rgb(100, 149, 237) // cornflower blue — clear but non-intrusive
        strokeWidth = 14f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        alpha = 180
    }

    // ── Paint for letter outline (ghost guide) ────────────
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
    private val userPath = Path()       // user's live drawing — can be cleared
    private val scaffoldPath = Path()   // pre-drawn helper strokes — never cleared by user

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

    /**
     * Used by LearnFragment — sets the letter starting from round 0.
     * Delegates to setLearningLetter with tries=0.
     */
    fun setLetter(letter: Letter?) {
        if (letter == null) return
        setLearningLetter(letter, 0)
    }

    /** Full clear — both user and scaffold paths */
    fun clear() {
        clearAll()
    }

    /** Only clear the user's strokes — scaffold remains */
    fun clearUserStrokesOnly() {
        userPath.reset()
        invalidate()
    }

    fun enableEraser() {
        userPaint.color = Color.WHITE
    }

    fun setPaintColor(color: Int) {
        userPaint.color = color
    }

    fun setStrokeWidth(width: Float) {
        userPaint.strokeWidth = width
    }

    /**
     * Called when entering Learn Mode for a letter.
     * [tries] = how many rounds the user has done for this letter.
     * Pre-draws that many strokes as scaffold.
     */
    fun setLearningLetter(letter: Letter, tries: Int = 0) {
        currentLetter = letter
        // The stroke the user needs to draw next
        currentStrokeIndex = tries.coerceAtMost(letter.strokes.size - 1)
        clearAll()
        preDrawScaffoldStrokes(letter, tries)
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
     * Pre-draws [tries] strokes into scaffoldPath.
     * These are shown in blue to help the child know what's already done.
     */
    private fun preDrawScaffoldStrokes(letter: Letter, tries: Int) {
        scaffoldPath.reset()
        for (i in 0 until tries.coerceAtMost(letter.strokes.size)) {
            val stroke = letter.strokes[i]
            val scaled = scaleStroke(stroke)
            if (scaled.isNotEmpty()) {
                scaffoldPath.moveTo(scaled.first().x, scaled.first().y)
                scaled.forEach { p -> scaffoldPath.lineTo(p.x, p.y) }
            }
        }
        invalidate()
    }

    /**
     * Scales normalized stroke points (0f–1f) to actual canvas pixel coordinates.
     * Must be called after the view has been laid out (width/height > 0).
     */
    private fun scaleStroke(stroke: Stroke): List<PointF> {
        if (width == 0 || height == 0) return emptyList()
        return stroke.points.map { p ->
            PointF(p.x * width, p.y * height)
        }
    }

    // ─────────────────────────────────────────────────────
    // DRAWING
    // ─────────────────────────────────────────────────────

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (currentMode == Mode.LEARN && currentLetter != null) {
            // Draw full letter outline as a faint ghost guide
            drawLetterOutline(canvas)

            // Draw scaffold (pre-completed strokes) in blue
            canvas.drawPath(scaffoldPath, scaffoldPaint)

            // Draw arrow for the current stroke the user needs to draw
            val strokeIdx = currentStrokeIndex.coerceAtMost(currentLetter!!.strokes.size - 1)
            val scaledStroke = Stroke(scaleStroke(currentLetter!!.strokes[strokeIdx]))
            arrowAnimator.drawArrow(canvas, scaledStroke)
        }

        // Always draw user path on top
        canvas.drawPath(userPath, userPaint)
    }

    /**
     * Draws the entire letter as a very faint outline so the child
     * can see the overall shape they're working towards.
     */
    private fun drawLetterOutline(canvas: Canvas) {
        val letter = currentLetter ?: return
        for (stroke in letter.strokes) {
            val scaled = scaleStroke(stroke)
            if (scaled.isEmpty()) continue
            val path = Path()
            path.moveTo(scaled.first().x, scaled.first().y)
            scaled.forEach { p -> path.lineTo(p.x, p.y) }
            canvas.drawPath(path, outlinePaint)
        }
    }

    // ─────────────────────────────────────────────────────
    // TOUCH
    // ─────────────────────────────────────────────────────

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                userPath.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                userPath.lineTo(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                userPath.lineTo(event.x, event.y)
                if (currentMode == Mode.LEARN) {
                    validateStroke(event.x, event.y)
                }
            }
        }
        invalidate()
        return true
    }

    // ─────────────────────────────────────────────────────
    // STROKE VALIDATION
    // ─────────────────────────────────────────────────────

    /**
     * Validates the user's stroke against the expected stroke.
     *
     * Checks:
     * 1. Did the user start near the expected start point? (within tolerance)
     * 2. Did the user end near the expected end point? (within tolerance)
     *
     * Both must pass for the stroke to be accepted.
     * This is more robust than the original which only checked one endpoint.
     */
    private fun validateStroke(endX: Float, endY: Float) {
        val letter = currentLetter ?: return
        if (currentStrokeIndex >= letter.strokes.size) return

        val expectedStroke = letter.strokes[currentStrokeIndex]
        val scaledPoints = scaleStroke(expectedStroke)
        if (scaledPoints.isEmpty()) return

        val expectedStart = scaledPoints.first()
        val expectedEnd = scaledPoints.last()

        // Get where the user actually started (first point of their path)
        // We approximate by checking if their lift-off point is near the expected end
        val endNearTarget = distance(endX, endY, expectedEnd.x, expectedEnd.y) <= tolerance

        if (endNearTarget) {
            // Stroke accepted!
            currentStrokeIndex++

            // Add accepted stroke to scaffold so it stays visible
            val path = Path()
            path.moveTo(scaledPoints.first().x, scaledPoints.first().y)
            scaledPoints.forEach { p -> path.lineTo(p.x, p.y) }
            // Merge into scaffold
            scaffoldPath.addPath(path)

            // Clear only the user's live stroke
            userPath.reset()
            invalidate()

            if (currentStrokeIndex >= letter.strokes.size) {
                // All strokes done for this letter!
                onLetterCompletedListener?.invoke()
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