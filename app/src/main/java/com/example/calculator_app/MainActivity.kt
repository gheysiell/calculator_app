package com.example.calculator_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var display by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DisplayScreen(
                display,
                result,
            )
            Spacer(modifier = Modifier.height(16.dp))
            CalculatorButtons { button ->
                when (button) {
                    "=" -> {
                        result = try {
                            display.let {
                                val evaluation = evaluateExpression(it)
                                display = evaluation
                                evaluation
                            }
                        } catch (e: Exception) {
                            "Erro"
                        }
                    }
                    "C" -> {
                        display = ""
                        result = ""
                    }
                    else -> {
                        display += button
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayScreen(
    display: String,
    result: String,
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = display,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier.height(8.dp),
        )
        Text(
            text = result,
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun CalculatorButtons(onClick: (String) -> Unit) {
    val buttonLabels = listOf(
        "7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "C", "0", "=", "+"
    )

    Column {
        buttonLabels.chunked(4).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                row.forEach { button ->
                    Button(
                        onClick = { onClick(button) },
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                            .height(80.dp)
                    ) {
                        Text(
                            text = button,
                            fontSize = 24.sp,
                        )
                    }
                }
            }
        }
    }
}

fun evaluateExpression(expression: String): String {
    try {
        val tokens = expression.split("(?=[-+*/])|(?<=[-+*/])".toRegex()).map { it.trim() }
        val result = calculate(tokens)
        return result.toString()
    } catch (e: Exception) {
        return "Erro"
    }
}

fun calculate(tokens: List<String>): Double {
    var result = tokens[0].toDouble()

    for (i in 1 until tokens.size step 2) {
        val operator = tokens[i]
        val number = tokens[i + 1].toDouble()

        result = when (operator) {
            "+" -> result + number
            "-" -> result - number
            "*" -> result * number
            "/" -> if (number != 0.0) result / number else return Double.NaN
            else -> result
        }
    }
    return result
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}