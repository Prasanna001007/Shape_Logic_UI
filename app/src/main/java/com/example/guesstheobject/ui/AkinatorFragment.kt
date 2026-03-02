package com.example.guesstheobject.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.guesstheobject.R
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class AkinatorFragment : Fragment() {

    private lateinit var questionText: TextView
    private lateinit var guessText: TextView
    private lateinit var reactionText: TextView
    private lateinit var analyzingText: TextView
    private lateinit var btnYes: Button
    private lateinit var btnNo: Button
    private lateinit var aiIcon: ImageView

    private val handler = Handler(Looper.getMainLooper())

    private val baseUrl = "http://192.168.101.4:3000/api/akinator"

    private var currentFeature: String? = null
    private var sessionId: String? = null

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

        btnYes.text = "Yes"
        btnNo.text = "No"

        startGame()

        btnYes.setOnClickListener { submitAnswer(true) }
        btnNo.setOnClickListener { submitAnswer(false) }

        return view
    }

    // ---------------- START GAME ----------------
    private fun startGame() {
        analyzingText.visibility = View.VISIBLE
        analyzingText.text = "Thinking..."
        aiIcon.setImageResource(R.drawable.bearthinkingpngnew)

        thread {
            try {
                val url = URL("$baseUrl/start")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true

                val responseText = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(responseText)

                handler.post {
                    analyzingText.visibility = View.GONE

                    if (json.getString("status") == "continue") {
                        sessionId = json.getString("sessionId")
                        currentFeature = json.getString("feature")
                        questionText.text = json.getString("question")
                    } else {
                        questionText.text = "Failed to start game"
                    }
                }

                connection.disconnect()

            } catch (e: Exception) {
                handler.post {
                    questionText.text = "Failed to connect to backend: ${e.message}"
                    analyzingText.visibility = View.GONE
                }
            }
        }
    }

    // ---------------- SUBMIT ANSWER ----------------
    private fun submitAnswer(answer: Boolean) {

        if (currentFeature == null || sessionId == null) return

        analyzingText.visibility = View.VISIBLE
        analyzingText.text = "Analyzing..."
        aiIcon.setImageResource(R.drawable.beardeeplythinkingpng)

        thread {
            try {
                val url = URL("$baseUrl/answer")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")

                val jsonBody = JSONObject()
                jsonBody.put("feature", currentFeature)
                jsonBody.put("answer", answer)
                jsonBody.put("sessionId", sessionId)

                val writer = OutputStreamWriter(connection.outputStream)
                writer.write(jsonBody.toString())
                writer.flush()
                writer.close()

                val responseText = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(responseText)

                handler.post {
                    analyzingText.visibility = View.GONE

                    when (json.getString("status")) {

                        "continue" -> {
                            currentFeature = json.getString("feature")
                            questionText.text = json.getString("question")
                        }

                        "win" -> {
                            val guess = json.getString("guess")
                            questionText.text = "I guess: $guess!"
                            aiIcon.setImageResource(R.drawable.bearnormalphotopng)
                            btnYes.visibility = View.GONE
                            btnNo.visibility = View.GONE
                        }

                        "fail" -> {
                            questionText.text = "I couldn't guess your animal."
                            btnYes.visibility = View.GONE
                            btnNo.visibility = View.GONE
                        }
                    }
                }

                connection.disconnect()

            } catch (e: Exception) {
                handler.post {
                    questionText.text = "Failed to connect to backend: ${e.message}"
                    analyzingText.visibility = View.GONE
                }
            }
        }
    }
}