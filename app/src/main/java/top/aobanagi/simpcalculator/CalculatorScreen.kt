package top.aobanagi.simpcalculator

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import top.aobanagi.simpcalculator.calculator.Calculation
import top.aobanagi.simpcalculator.calculator.CalculationDatabase
import top.aobanagi.simpcalculator.ui.theme.onPrimaryDark
import top.aobanagi.simpcalculator.ui.theme.onPrimaryLight
import top.aobanagi.simpcalculator.ui.theme.primaryDark
import top.aobanagi.simpcalculator.ui.theme.primaryLight
import top.aobanagi.simpcalculator.ui.theme.secondaryContainerDark
import top.aobanagi.simpcalculator.ui.theme.secondaryContainerLight
import top.aobanagi.simpcalculator.ui.theme.secondaryDark
import top.aobanagi.simpcalculator.ui.theme.secondaryLight
import top.aobanagi.simpcalculator.ui.theme.surfaceDark
import top.aobanagi.simpcalculator.ui.theme.surfaceLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(navController: NavController, modifier: Modifier = Modifier) {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var memory by remember { mutableStateOf(0.0) }
    var showMemoryIndicator by remember { mutableStateOf(false) }

    val backgroundColor = if (isSystemInDarkTheme()) surfaceDark else surfaceLight
    val buttonBackgroundColor = if (isSystemInDarkTheme()) secondaryContainerDark else secondaryContainerLight
    val buttonTextColor = if (isSystemInDarkTheme()) secondaryDark else secondaryLight
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = CalculationDatabase.getDatabase(context)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "计算器",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                        color = if (isSystemInDarkTheme()) primaryDark else primaryLight,
                    )
                },
                actions = {
                          IconButton(
                              onClick={
                                  navController.navigate("calc_history")
                              }
                          ){
                              Icon(
                                  imageVector = Icons.Default.Menu,
                                  contentDescription = "计算历史",
                                  tint = if (isSystemInDarkTheme()) primaryDark else primaryLight,
                              )
                          }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (showMemoryIndicator) {
                        Text(
                            text = "M",
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp),
                            color = if (isSystemInDarkTheme()) primaryDark.copy(alpha = 0.5f) else primaryLight.copy(alpha = 0.5f),
                        )
                    }
                    Text(
                        text = expression,
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 36.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.End,
                        color = if (isSystemInDarkTheme()) primaryDark else primaryLight,
                    )
                }

                Text(
                    text = result,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 48.sp),
                    color = if (isSystemInDarkTheme()) primaryDark else primaryLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.End,
                )

                Spacer(modifier = Modifier.weight(1f))

                val buttons = listOf(
                    listOf("MC", "M+", "M-", "MR"),
                    listOf("C", "/", "*", "Del"),
                    listOf("7", "8", "9", "-"),
                    listOf("4", "5", "6", "+"),
                    listOf("1", "2", "3", "."),
                    listOf("(", "0", ")", "=")
                )

                for (row in buttons) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (buttonText in row) {
                            CalculatorButton(
                                text = buttonText,
                                modifier = if (buttonText == "=") Modifier
                                else Modifier,
                                onClick = {
                                    when (buttonText) {
                                        "=" -> {
                                            try {
                                                result =
                                                    ExpressionBuilder(expression).build().evaluate()
                                                        .toString()
                                                scope.launch {
                                                    db.calculationDao().insertCalculation(
                                                        Calculation(expression = expression, result = result)
                                                    )
                                                }
                                            } catch (e: Exception) {
                                                result = "Error"
                                                Toast.makeText(context, "计算错误", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        "C" -> {
                                            expression = ""
                                            result = ""
                                        }

                                        "Del" -> {
                                            if (expression.isNotEmpty()) {
                                                expression = expression.dropLast(1)
                                            }
                                        }

                                        "MC" -> {
                                            memory = 0.0
                                            showMemoryIndicator = false
                                        }

                                        "M+" -> {
                                            try {
                                                memory += ExpressionBuilder(expression).build().evaluate()
                                                showMemoryIndicator = true
                                            } catch (e: Exception) {
                                                result = "Error"
                                            }
                                        }

                                        "M-" -> {
                                            try {
                                                memory -= ExpressionBuilder(expression).build().evaluate()
                                                showMemoryIndicator = true
                                            } catch (e: Exception) {
                                                result = "Error"
                                            }
                                        }

                                        "MR" -> {
                                            expression = memory.toString()
                                            showMemoryIndicator = true
                                        }

                                        "(" -> {
                                            expression += if (expression.isNotEmpty() && expression.last().isDigit()) "*" else ""
                                            expression += "("
                                        }

                                        ")" -> {
                                            expression += ")"
                                        }

                                        else -> {
                                            expression += buttonText
                                        }
                                    }
                                },
                                colors = if (buttonText == "=") {
                                    ButtonDefaults.buttonColors(
                                        containerColor = if(isSystemInDarkTheme()) primaryDark else primaryLight,
                                        contentColor = if(isSystemInDarkTheme()) onPrimaryDark else onPrimaryLight
                                    )
                                } else {
                                    ButtonDefaults.buttonColors(
                                        containerColor = buttonBackgroundColor,
                                        contentColor = buttonTextColor
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    )
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(80.dp)
            .padding(4.dp),
        colors = colors,
        shape = CircleShape
    ) {
        when (text) {
            "/" -> Icon(painterResource(id = R.drawable.baseline_divide_24), contentDescription = null)
            "*" -> Icon(painterResource(id = R.drawable.baseline_clear_24), contentDescription = null)
            "-" -> Icon(painterResource(id = R.drawable.baseline_remove_24), contentDescription = null)
            "+" -> Icon(painterResource(id = R.drawable.baseline_add_24), contentDescription = null)
            "Del" -> Icon(painterResource(id = R.drawable.baseline_backspace_24), contentDescription = null)
            "MC", "M+", "M-", "MR" -> Text(text = text, fontSize = 15.sp)
            else -> Text(text = text, fontSize = 20.sp)
        }
    }
}
