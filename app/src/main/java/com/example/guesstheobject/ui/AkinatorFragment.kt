package com.example.guesstheobject.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.guesstheobject.R

class AkinatorFragment : Fragment() {

    private lateinit var questionText: TextView
    private lateinit var guessText: TextView
    private lateinit var reactionText: TextView
    private lateinit var analyzingText: TextView
    private lateinit var btnYes: Button
    private lateinit var btnNo: Button
    private lateinit var btnClose: Button
    private lateinit var btnNotClose: Button
    private lateinit var aiIcon: ImageView

    private val questions = listOf(
        "Is the object a non-living thing",
        "Is it a plant?",
        "Is it a domestic animal?",
        "Is it a wild animal?",
        "Is it cold-blooded?",
        "Is it a bird?",
        "Is it an herbivore?",
        "Does it belong to the cat family (Felidae)?",
        "Is it larger than a lion?",
        "Does it roar?",
        "Is it mainly adapted for extreme speed like a cheetah?",
        "Does it have a spotted coat pattern?",
        "Are the spots solid black (not rosettes)?"
    )

    private var currentIndex = 0

    // AI expressions for every 5 questions
    private val aiExpressions = listOf(
        R.drawable.bearnormalphotopng,   // 0-4 questions
        R.drawable.bearthinkingpngnew,      // 5-9 questions
        R.drawable.beardeeplythinkingpng // 10+ questions
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_akinator, container, false)

        aiIcon = view.findViewById(R.id.aiIcon)
        questionText = view.findViewById(R.id.questionText)
        guessText = view.findViewById(R.id.guessText)
        reactionText = view.findViewById(R.id.reactionText)
        analyzingText = view.findViewById(R.id.analyzingText)
        btnYes = view.findViewById(R.id.btnYes)
        btnNo = view.findViewById(R.id.btnNo)
        btnClose = view.findViewById(R.id.btnClose)
        btnNotClose = view.findViewById(R.id.btnNotClose)

        // Initially hide after-guess UI
        guessText.visibility = View.GONE
        btnClose.visibility = View.GONE
        btnNotClose.visibility = View.GONE
        reactionText.visibility = View.GONE
        analyzingText.visibility = View.GONE

        updateQuestion()

        btnYes.setOnClickListener { nextQuestion() }
        btnNo.setOnClickListener { nextQuestion() }

        btnClose.setOnClickListener {
            reactionText.text = "Thanks , It motivates me"
            aiIcon.setImageResource(R.drawable.bearnormalphotopng)
            btnClose.isEnabled = false
            btnNotClose.isEnabled = false
        }

        btnNotClose.setOnClickListener {
            reactionText.text = "I will try better next time."
            aiIcon.setImageResource(R.drawable.beardeeplythinkingpng)
            btnClose.isEnabled = false
            btnNotClose.isEnabled = false
        }

        return view
    }

    private fun nextQuestion() {
        currentIndex++
        if (currentIndex < questions.size) {
            updateQuestion()
        } else {
            // Hide Yes/No buttons & question text
            btnYes.visibility = View.GONE
            btnNo.visibility = View.GONE
            questionText.visibility = View.GONE

            // Show analyzing stage
            analyzingText.text = "Analyzing your answers..."
            analyzingText.visibility = View.VISIBLE
            aiIcon.setImageResource(R.drawable.bearthinkingpngnew)

            // Delay to simulate thinking (1.5 seconds)
            Handler(Looper.getMainLooper()).postDelayed({
                // Hide analyzing
                analyzingText.visibility = View.GONE

                // Show final guess and after-guess UI
                questionText.text = "I guess your object is: Leopard"
                questionText.visibility = View.VISIBLE

                guessText.visibility = View.VISIBLE
                btnClose.visibility = View.VISIBLE
                btnNotClose.visibility = View.VISIBLE
                reactionText.visibility = View.VISIBLE

                // Reset AI icon to neutral
                aiIcon.setImageResource(R.drawable.bearnormalphotopng)
            }, 1500)
        }
    }

    private fun updateQuestion() {
        questionText.text = questions[currentIndex]

        // Change AI icon based on how many questions have passed
        aiIcon.setImageResource(
            when {
                currentIndex < 4 -> aiExpressions[0]
                currentIndex < 8 -> aiExpressions[1]
                else -> aiExpressions[2]
            }
        )
    }
}
