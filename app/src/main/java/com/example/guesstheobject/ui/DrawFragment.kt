package com.example.guesstheobject.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.guesstheobject.R

class DrawFragment : Fragment() {

    private lateinit var drawView: DrawingView
    private lateinit var toolbar: LinearLayout
    private lateinit var btnClear: ImageButton
    private lateinit var btnEraser: ImageButton
    private lateinit var btnColor: ImageButton
    private lateinit var btnWidth: ImageButton
    private lateinit var btnSend: Button

    private lateinit var aiGuessText: TextView
    private lateinit var aiIcon: ImageView

    // Result views
    private lateinit var resultLayout: LinearLayout
    private lateinit var resultAiIcon: ImageView
    private lateinit var resultText: TextView
    private lateinit var btnDrawAgain: Button
    private lateinit var btnClose: Button
    private lateinit var btnNotClose: Button
    private lateinit var feedbackResult: TextView

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var commentRunnable: Runnable
    private var hasStartedDrawing = false

    private val funComments = listOf(
        "Looks like a line 🤔",
        "Hmm… a curve?",
        "Interesting shape!",
        "What color is that?",
        "Nice strokes!"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_draw, container, false)

        drawView = view.findViewById(R.id.drawView)
        toolbar = view.findViewById(R.id.toolbar)
        btnClear = view.findViewById(R.id.btnClear)
        btnEraser = view.findViewById(R.id.btnEraser)
        btnColor = view.findViewById(R.id.btnColor)
        btnWidth = view.findViewById(R.id.btnWidth)
        btnSend = view.findViewById(R.id.btnSend)

        aiGuessText = view.findViewById(R.id.aiGuessText)
        aiIcon = view.findViewById(R.id.aiIcon)

        resultLayout = view.findViewById(R.id.resultLayout)
        resultAiIcon = view.findViewById(R.id.resultAiIcon)
        resultText = view.findViewById(R.id.resultText)
        btnDrawAgain = view.findViewById(R.id.btnDrawAgain)
        btnClose = view.findViewById(R.id.btnClose)
        btnNotClose = view.findViewById(R.id.btnNotClose)
        feedbackResult = view.findViewById(R.id.feedbackResult)

        aiGuessText.text = "What are you going to draw today?"
        aiIcon.setImageResource(R.drawable.bearnormalphotopng)

        setupToolbar()
        setupResultActions()
        detectDrawingStart()

        return view
    }

    private fun detectDrawingStart() {
        drawView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && !hasStartedDrawing) {
                hasStartedDrawing = true
                startFunnyComments()
            }
            false
        }
    }

    private fun startFunnyComments() {
        commentRunnable = object : Runnable {
            override fun run() {
                aiGuessText.text = funComments.random()
                handler.postDelayed(this, 1500)
            }
        }
        handler.post(commentRunnable)
    }

    private fun setupToolbar() {

        btnClear.setOnClickListener {
            drawView.clear()
            resetToInitialState()
        }

        btnEraser.setOnClickListener { drawView.enableEraser() }

        btnColor.setOnClickListener {
            val colors = arrayOf("Black", "Red", "Blue", "Green")
            val values = arrayOf(Color.BLACK, Color.RED, Color.BLUE, Color.GREEN)

            AlertDialog.Builder(requireContext())
                .setTitle("Pick Color")
                .setItems(colors) { _, which ->
                    drawView.setPaintColor(values[which])
                }.show()
        }

        btnWidth.setOnClickListener {
            val widths = arrayOf("4", "8", "12", "16")
            AlertDialog.Builder(requireContext())
                .setTitle("Pick Stroke Width")
                .setItems(widths) { _, which ->
                    drawView.setStrokeWidth(widths[which].toFloat())
                }.show()
        }

        btnSend.setOnClickListener {
            handler.removeCallbacks(commentRunnable)
            aiGuessText.text = "Analyzing your drawing..."
            aiIcon.setImageResource(R.drawable.beardeeplythinkingpng)

            handler.postDelayed({
                showResult()
            }, 1500)
        }
    }

    private fun showResult() {
        drawView.visibility = View.GONE
        toolbar.visibility = View.GONE
        aiGuessText.visibility = View.GONE
        aiIcon.visibility = View.GONE

        resultLayout.visibility = View.VISIBLE
        resultAiIcon.setImageResource(R.drawable.bearnormalphotopng)
        resultText.text = "It's a Tree 🌳"
        feedbackResult.text = ""
    }

    private fun setupResultActions() {

        btnClose.setOnClickListener {
            feedbackResult.text = "Great! This helps me improve 😊"
        }

        btnNotClose.setOnClickListener {
            feedbackResult.text = "Thanks! I’ll try better next time 🤖"
        }

        btnDrawAgain.setOnClickListener {
            drawView.clear()
            resetToInitialState()
        }
    }

    private fun resetToInitialState() {
        handler.removeCallbacksAndMessages(null)
        hasStartedDrawing = false

        resultLayout.visibility = View.GONE
        drawView.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
        aiGuessText.visibility = View.VISIBLE
        aiIcon.visibility = View.VISIBLE

        aiGuessText.text = "What are you going to draw today?"
        aiIcon.setImageResource(R.drawable.bearnormalphotopng)
        feedbackResult.text = ""
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}
