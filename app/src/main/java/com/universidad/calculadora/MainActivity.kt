package com.universidad.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.universidad.calculadora.ui.theme.CalculadoraTheme

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
    var display by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf("") }
    var secondNumber by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espaciador para posicionar los numeros hacia abajo
        Spacer(modifier = Modifier.weight(1f))

        // PP visualización
        Text(
            text = display,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Separación entre pantalla y botones

        // Botones en columnas y filas
        Column {
            Row {
                CalculatorButton("1") { handleInput("1", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
                CalculatorButton("2") { handleInput("2", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
                CalculatorButton("3") { handleInput("3", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
            }
            Row {
                CalculatorButton("4") { handleInput("4", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
                CalculatorButton("5") { handleInput("5", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
                CalculatorButton("6") { handleInput("6", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
            }
            Row {
                CalculatorButton("7") { handleInput("7", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
                CalculatorButton("8") { handleInput("8", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
                CalculatorButton("9") { handleInput("9", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
            }
            Row {
                CalculatorButton("0") { handleInput("0", operation.isEmpty(), { firstNumber += it }, { secondNumber += it }, { display = it }) }
                CalculatorButton("+") { operation = "+" }
                CalculatorButton("-") { operation = "-" }
            }
            Row {
                CalculatorButton("=") { display = calculateResult(firstNumber, secondNumber, operation) }
                CalculatorButton("C") {
                    resetCalculator(
                        { display = it },
                        { firstNumber = ""; secondNumber = ""; operation = "" }
                    )
                }
                CalculatorButton("*") { operation = "*" }
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .size(80.dp)
    ) {
        Text(text = text)
    }
}

fun handleInput(
    input: String,
    isFirstNumber: Boolean,
    updateFirst: (String) -> Unit,
    updateSecond: (String) -> Unit,
    updateDisplay: (String) -> Unit
) {
    if (isFirstNumber) {
        updateFirst(input)
        updateDisplay(input)
    } else {
        updateSecond(input)
        updateDisplay(input)
    }
}

fun calculateResult(firstNumber: String, secondNumber: String, operation: String): String {
    val num1 = firstNumber.toDoubleOrNull() ?: return "Error"
    val num2 = secondNumber.toDoubleOrNull() ?: return "Error"
    return when (operation) {
        "+" -> (num1 + num2).toString()
        "-" -> (num1 - num2).toString()
        "*" -> (num1 * num2).toString()
        else -> "Error"
    }
}

fun resetCalculator(updateDisplay: (String) -> Unit, resetInputs: () -> Unit) {
    updateDisplay("0")
    resetInputs()
}

@Preview(showBackground = true)
@Composable
fun CalculatorAppPreview() {
    CalculadoraTheme {
        CalculatorApp()
    }
}

@Preview
@Composable
fun PreviewCalculator() {
    MaterialTheme {
        CalculatorApp()
    }
}