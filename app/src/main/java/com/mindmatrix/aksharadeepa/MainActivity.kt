package com.mindmatrix.aksharadeepa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.mindmatrix.aksharadeepa.ui.*
import com.mindmatrix.aksharadeepa.data.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Syllabus : Screen("syllabus", "Subjects", Icons.Default.List)
    object Stats : Screen("stats", "Analytics", Icons.Default.Info)
    object Settings : Screen("settings", "Profile", Icons.Default.Person)
}

@Composable
fun MainScreen(viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()
    var activeQuiz by remember { mutableStateOf<Chapter?>(null) }
    val progress by viewModel.progress

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val items = listOf(Screen.Home, Screen.Syllabus, Screen.Stats, Screen.Settings)
                
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { 
                HomeScreen(progress.completedChapters.size, progress.streak, progress.userName) 
            }
            composable(Screen.Syllabus.route) { 
                SyllabusScreen(
                    completedChapters = progress.completedChapters,
                    onToggleChapter = { viewModel.toggleChapter(it) },
                    onStartQuiz = { activeQuiz = it }
                ) 
            }
            composable(Screen.Stats.route) { 
                StatsScreen(progress.quizScores) 
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    userName = progress.userName,
                    onNameChange = { viewModel.updateUserName(it) },
                    onReset = { viewModel.resetProgress() }
                )
            }
        }

        if (activeQuiz != null) {
            QuizOverlay(
                chapter = activeQuiz!!,
                onDismiss = { activeQuiz = null },
                onComplete = { score ->
                    viewModel.saveScore(activeQuiz!!.id, score)
                    activeQuiz = null
                }
            )
        }
    }
}
