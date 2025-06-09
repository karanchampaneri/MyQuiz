package com.example.myquiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var options: List<Button>
    private lateinit var nextButton: Button

    private lateinit var questions: List<Question>
    private var currentIndex = 0
    private var hasAnswered = false
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind UI
        questionText = findViewById(R.id.questionText)
        options = listOf(
            findViewById(R.id.optionA),
            findViewById(R.id.optionB),
            findViewById(R.id.optionC),
            findViewById(R.id.optionD)
        )
        nextButton = findViewById(R.id.nextButton)

        // Load questions from SQLite
        val db = QuizDbHelper(this)
        questions = db.getAllQuestions()

        // Load the first question
        loadQuestion()

        // Set up option button listeners
        options.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (!hasAnswered) checkAnswer(index)
            }
        }

        // Next button logic
        nextButton.setOnClickListener {
            currentIndex++
            if (currentIndex < questions.size) {
                loadQuestion()
            } else {

                AlertDialog.Builder(this)
                    .setTitle("Quiz Complete")
                    .setMessage("You got $score out of ${questions.size}!\nPlay again?")
                    .setPositiveButton("Restart") { _, _ ->
                        currentIndex = 0
                        score = 0
                        loadQuestion()
                    }
                    .setNegativeButton("Exit") { _, _ ->
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
    }

    private fun loadQuestion() {
        val q = questions[currentIndex]
        hasAnswered = false

        questionText.text = q.text
        options[0].text = q.optionA
        options[1].text = q.optionB
        options[2].text = q.optionC
        options[3].text = q.optionD

        options.forEach { btn ->
            btn.setBackgroundColor(Color.LTGRAY)
            btn.isEnabled = true
        }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val correctIndex = questions[currentIndex].correctIndex
        hasAnswered = true

        options.forEach { it.isEnabled = false }

        if (selectedIndex == correctIndex) {
            score++
            options[selectedIndex].setBackgroundColor(Color.GREEN)
            score++
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            options[selectedIndex].setBackgroundColor(Color.RED)
            options[correctIndex].setBackgroundColor(Color.GREEN)
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
        }
    }
}