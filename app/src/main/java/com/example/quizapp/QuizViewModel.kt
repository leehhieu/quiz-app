package com.example.quizapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    // Question 1: single choice
    val question1 = "Which language is officially supported for Android development?"
    val options1 = listOf("Kotlin", "Python", "Java", "Swift")
    val correct1 = "Kotlin"

    // Question 2: multiple choice
    val question2 = "Which of the following are Jetpack Compose layout components?"
    val options2 = listOf("A. Column", "B. TextField", "C. Row", "D. Button")
    val correct2 = setOf("A. Column", "C. Row")

    // Question 3: short answer
    val question3 = "Which annotation is used to declare a composable function in Jetpack Compose?"
    val correct3 = "Composable"

    var answer1 by mutableStateOf<String?>(null)
        private set
    val answer2 = mutableStateListOf<String>()
    var answer3 by mutableStateOf("")
        private set

    var submitted by mutableStateOf(false)
        private set

    val isCorrect1: Boolean
        get() = answer1 == correct1
    val isCorrect2: Boolean
        get() = answer2.toSet() == correct2
    val isCorrect3: Boolean
        get() = answer3.trim().removePrefix("@").equals(correct3, ignoreCase = true)

    val score: Int
        get() = listOf(isCorrect1, isCorrect2, isCorrect3).count { it }

    fun onAnswer1(option: String) {
        answer1 = option
    }

    fun onAnswer2(option: String, checked: Boolean) {
        if (checked) answer2.add(option) else answer2.remove(option)
    }

    fun onAnswer3(text: String) {
        answer3 = text
    }

    fun submit() {
        submitted = true
    }

    fun reset() {
        answer1 = null
        answer2.clear()
        answer3 = ""
        submitted = false
    }
}
