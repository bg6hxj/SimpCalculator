package top.aobanagi.simpcalculator.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import top.aobanagi.simpcalculator.ui.theme.primaryDark
import top.aobanagi.simpcalculator.ui.theme.primaryLight
import top.aobanagi.simpcalculator.ui.theme.secondaryContainerDark
import top.aobanagi.simpcalculator.ui.theme.secondaryContainerLight
import top.aobanagi.simpcalculator.ui.theme.surfaceDark
import top.aobanagi.simpcalculator.ui.theme.surfaceLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalcHistoryScreen(navController: NavController) {
    val backgroundColor = if (isSystemInDarkTheme()) surfaceDark else surfaceLight
    val context = LocalContext.current
    val db = CalculationDatabase.getDatabase(context)
    var calculations by remember { mutableStateOf(listOf<Calculation>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        calculations = db.calculationDao().getAllCalculations()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "计算历史",color = if (isSystemInDarkTheme()) primaryDark else primaryLight,) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回",tint = if (isSystemInDarkTheme()) primaryDark else primaryLight)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            db.calculationDao().clearCalculations()
                            calculations = emptyList()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "清除",tint = if (isSystemInDarkTheme()) primaryDark else primaryLight)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                LazyColumn(
                    contentPadding = padding,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .background(backgroundColor)
                ) {
                    items(calculations) { calculation ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = calculation.expression,
                                color = if (isSystemInDarkTheme()) primaryDark else primaryLight
                            )
                            Text(
                                text = calculation.result,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSystemInDarkTheme()) primaryDark else primaryLight,
                            )
                        }
                        HorizontalDivider(color = if (isSystemInDarkTheme()) secondaryContainerDark else secondaryContainerLight)
                    }
                }
                Text(
                    text = "Copyright © BG6HXJ",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = if (isSystemInDarkTheme()) primaryDark.copy(alpha = 0.5f) else primaryLight.copy(alpha = 0.5f),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
            }
        }
    )
}
