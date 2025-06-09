package com.example.myquiz

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class QuizDbHelper(context: Context) : SQLiteOpenHelper(context, "quiz.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                text TEXT,
                optionA TEXT,
                optionB TEXT,
                optionC TEXT,
                optionD TEXT,
                correctIndex INTEGER
            )
        """.trimIndent())

        insertDefaultQuestions(db)
    }

    private fun insertDefaultQuestions(db: SQLiteDatabase) {
        insertQuestion(
            db,
            "What is the capital of France?",
            "London", "Berlin", "Paris", "Rome",
            2
        )
        insertQuestion(
            db,
            "What is 2 + 2?",
            "3", "4", "5", "6",
            1
        )
        insertQuestion(
            db,
            "Which planet is known as the Red Planet?",
            "Earth", "Venus", "Mars", "Jupiter",
            2
        )
        insertQuestion(
            db,
            "Who wrote Hamlet?",
            "Shakespeare", "Dickens", "Austen", "Rowling",
            0
        )
    }

    private fun insertQuestion(
        db: SQLiteDatabase,
        text: String,
        a: String, b: String, c: String, d: String,
        correctIndex: Int
    ) {
        val values = ContentValues().apply {
            put("text", text)
            put("optionA", a)
            put("optionB", b)
            put("optionC", c)
            put("optionD", d)
            put("correctIndex", correctIndex)
        }
        db.insert("questions", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS questions")
        onCreate(db)
    }

    fun getAllQuestions(): List<Question> {
        val list = mutableListOf<Question>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM questions", null)

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Question(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        text = cursor.getString(cursor.getColumnIndexOrThrow("text")),
                        optionA = cursor.getString(cursor.getColumnIndexOrThrow("optionA")),
                        optionB = cursor.getString(cursor.getColumnIndexOrThrow("optionB")),
                        optionC = cursor.getString(cursor.getColumnIndexOrThrow("optionC")),
                        optionD = cursor.getString(cursor.getColumnIndexOrThrow("optionD")),
                        correctIndex = cursor.getInt(cursor.getColumnIndexOrThrow("correctIndex"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}