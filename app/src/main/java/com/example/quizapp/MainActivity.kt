package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizapp.ui.theme.QuizAppTheme

val CorrectColor = Color(0xFF2E7D32)
val WrongColor = Color(0xFFC62828)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuizScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// After submitting: right options are green, chosen wrong options are red.
fun optionColor(submitted: Boolean, isRight: Boolean, isChosen: Boolean): Color = when {
    !submitted -> Color.Unspecified
    isRight -> CorrectColor
    isChosen -> WrongColor
    else -> Color.Unspecified
}

fun resultText(isCorrect: Boolean): String = if (isCorrect) "Correct" else "Wrong"

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = viewModel()
) {
    val submitted = viewModel.submitted

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Android Quiz", style = MaterialTheme.typography.headlineSmall)
        HorizontalDivider()

        // Question 1: single choice
        Text("Question 1: Single Choice", fontWeight = FontWeight.Bold)
        Text(viewModel.question1)
        viewModel.options1.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = viewModel.answer1 == option,
                    onClick = { viewModel.onAnswer1(option) },
                    enabled = !submitted
                )
                Text(
                    option,
                    color = optionColor(
                        submitted,
                        option == viewModel.correct1,
                        viewModel.answer1 == option
                    )
                )
            }
        }
        HorizontalDivider()

        // Question 2: multiple choice
        Text("Question 2: Multiple Choice", fontWeight = FontWeight.Bold)
        Text(viewModel.question2)
        viewModel.options2.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = option in viewModel.answer2,
                    onCheckedChange = { viewModel.onAnswer2(option, it) },
                    enabled = !submitted
                )
                Text(
                    option,
                    color = optionColor(
                        submitted,
                        option in viewModel.correct2,
                        option in viewModel.answer2
                    )
                )
            }
        }
        HorizontalDivider()

        // Question 3: short answer
        Text("Question 3: Short Answer", fontWeight = FontWeight.Bold)
        Text(viewModel.question3)
        OutlinedTextField(
            value = viewModel.answer3,
            onValueChange = viewModel::onAnswer3,
            label = { Text("Your answer") },
            singleLine = true,
            enabled = !submitted,
            modifier = Modifier.fillMaxWidth()
        )
        if (submitted) {
            if (viewModel.isCorrect3) {
                Text("Correct!", color = CorrectColor)
            } else {
                Text("Wrong. Correct answer: ${viewModel.correct3}", color = WrongColor)
            }
        }

        Button(
            onClick = viewModel::submit,
            enabled = !submitted,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Answers")
        }

        if (submitted) {
            Text(
                "Your Score: ${viewModel.score} / 3",
                style = MaterialTheme.typography.titleLarge
            )
            Text("Question 1: ${resultText(viewModel.isCorrect1)}")
            Text("Question 2: ${resultText(viewModel.isCorrect2)}")
            Text("Question 3: ${resultText(viewModel.isCorrect3)}")
        }

        OutlinedButton(
            onClick = viewModel::reset,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset Quiz")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizAppTheme {
        QuizScreen()
    }
}
