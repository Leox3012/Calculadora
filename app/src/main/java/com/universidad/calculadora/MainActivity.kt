package com.universidad.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universidad.calculadora.ui.theme.CalculadoraTheme
import java.util.Locale

data class CalculatorState(
    val display: String = "0",
    val currentInput: String = "",
    val expression: MutableList<String> = mutableListOf(),
    val history: List<String> = emptyList()
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp()
                }
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var state by remember {
        mutableStateOf(
            CalculatorState(
                display = "0",
                currentInput = "",
                expression = mutableListOf()
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))

        // Historial
        Text(
            text = state.history.joinToString("\n"),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        // Expresión actual
        Text(
            text = state.expression.joinToString(" ") + " " + state.currentInput,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray
        )

        // Display
        Text(
            text = state.display,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("7") { handleNumber("7", state) { newState -> state = newState } }
                CalculatorButton("8") { handleNumber("8", state) { newState -> state = newState } }
                CalculatorButton("9") { handleNumber("9", state) { newState -> state = newState } }
                CalculatorButton("/") { handleOperator("/", state) { newState -> state = newState } }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("4") { handleNumber("4", state) { newState -> state = newState } }
                CalculatorButton("5") { handleNumber("5", state) { newState -> state = newState } }
                CalculatorButton("6") { handleNumber("6", state) { newState -> state = newState } }
                CalculatorButton("*") { handleOperator("*", state) { newState -> state = newState } }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("1") { handleNumber("1", state) { newState -> state = newState } }
                CalculatorButton("2") { handleNumber("2", state) { newState -> state = newState } }
                CalculatorButton("3") { handleNumber("3", state) { newState -> state = newState } }
                CalculatorButton("-") { handleOperator("-", state) { newState -> state = newState } }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("0") { handleNumber("0", state) { newState -> state = newState } }
                CalculatorButton(".") { handleDecimal(state) { newState -> state = newState } }
                CalculatorButton("+") { handleOperator("+", state) { newState -> state = newState } }
                CalculatorButton("=") { calculateResult(state) { newState -> state = newState } }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("C") { clearCalculator { newState -> state = newState } }
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp)
    ) {
        Text(text = text)
    }
}

// Funciones
fun handleNumber(number: String, state: CalculatorState, updateState: (CalculatorState) -> Unit) {
    if (state.currentInput.length >= 15) return

    val newInput = if (state.currentInput == "0") number else state.currentInput + number
    updateState(state.copy(
        currentInput = newInput,
        display = newInput
    ))
}

fun handleDecimal(state: CalculatorState, updateState: (CalculatorState) -> Unit) {
    if (!state.currentInput.contains(".")) {
        val newInput = if (state.currentInput.isEmpty()) "0." else state.currentInput + "."
        updateState(state.copy(
            currentInput = newInput,
            display = newInput
        ))
    }
}

fun handleOperator(operator: String, state: CalculatorState, updateState: (CalculatorState) -> Unit) {
    val newExpression = state.expression.toMutableList()

    if (state.currentInput.isNotEmpty()) {
        newExpression.add(state.currentInput)
        newExpression.add(operator)
        updateState(state.copy(
            expression = newExpression,
            currentInput = "",
            display = operator
        ))
    } else if (newExpression.isNotEmpty() && newExpression.last() in listOf("+", "-", "*", "/")) {
        newExpression[newExpression.lastIndex] = operator
        updateState(state.copy(
            expression = newExpression,
            display = operator
        ))
    }
}

fun calculateResult(state: CalculatorState, updateState: (CalculatorState) -> Unit) {
    try {
        val fullExpression = state.expression.toMutableList()
        if (state.currentInput.isNotEmpty()) {
            fullExpression.add(state.currentInput)
        }

        if (fullExpression.size < 3 || fullExpression.size % 2 == 0) {
            updateState(state.copy(display = "Error"))
            return
        }

        var result = fullExpression[0].toDouble()
        for (i in 1 until fullExpression.size step 2) {
            val operator = fullExpression[i]
            val operand = fullExpression[i + 1].toDouble()

            result = when (operator) {
                "+" -> result + operand
                "-" -> result - operand
                "*" -> result * operand
                "/" -> {
                    if (operand == 0.0) throw ArithmeticException()
                    result / operand
                }
                else -> throw IllegalArgumentException()
            }
        }

        val resultString = if (result % 1 == 0.0)
            result.toInt().toString()
        else
            String.format(Locale.US, "%.2f", result).trimEnd('0').trimEnd('.')

        updateState(state.copy(
            display = resultString,
            currentInput = "",
            expression = mutableListOf(resultString),
            history = state.history + "${fullExpression.joinToString(" ")} = $resultString"
        ))
    } catch (e: Exception) {
        updateState(state.copy(display = when (e) {
            is ArithmeticException -> "Div/0"
            else -> "Error"
        }))
    }
}

fun clearCalculator(updateState: (CalculatorState) -> Unit) {
    updateState(CalculatorState())
}

@Preview(showBackground = true)
@Composable
fun CalculatorAppPreview() {
    CalculadoraTheme {
        CalculatorApp()
    }
}