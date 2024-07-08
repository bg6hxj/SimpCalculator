package top.aobanagi.simpcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import top.aobanagi.simpcalculator.calculator.CalcHistoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpCalculatorTheme()
        }
    }
}

@Composable
fun SimpCalculatorTheme() {
    val navController = rememberNavController()     //导航控制器
    val navBackStackEntry by navController.currentBackStackEntryAsState()   //获取当前导航栈
    val currentRoute = navBackStackEntry?.destination?.route    //获取当前路由

    NavHost(
        navController = navController,
            startDestination = "calculator",
            modifier = Modifier.padding()
    ){
        composable("calculator") {//计算器页面
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                ) {
                CalculatorScreen(
                    navController = navController,
                    modifier = Modifier
                        .align(Alignment.Center)
                    )
                }
            }
        composable("calc_history") {    //计算历史
            CalcHistoryScreen(navController)
        }
    }
}
