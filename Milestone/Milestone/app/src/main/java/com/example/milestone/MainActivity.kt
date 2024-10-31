package com.example.milestone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.milestone.ui.theme.MilestoneTheme
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MilestoneTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Set up the navigation host
                    NavigationComponent(navController = navController)
                }
            }
        }
    }
}

@Composable
fun NavigationComponent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        // Splash Screen
        composable("splash") {
            SplashScreen(navController = navController)
        }
        // Login Screen
        composable("login") {
            LoginScreen(
                onSignUpClick = { navController.navigate("signup") },
                navController = navController
            )
        }
        // Sign Up Screen (Wrap with Composable)
        composable("signup") {
            SignUpScreen(
                onLoginClick = { navController.navigate("login") },
                navController = navController
            )
        }
        // Main Screen
        composable(route = "main") {
            val context = LocalContext.current
            MainScreen(navController = navController, context = context)
        }

    }
}


@Composable
fun SplashScreen(navController: NavHostController) {
    // This is the splash screen content
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Column to display logo and text vertically aligned
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the logo image in the center
            Image(
                painter = painterResource(id = R.drawable.splashscreen),  // Use the correct image resource
                contentDescription = "Splash Logo",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Display the app name "Milestone" below the logo
            Text(
                text = "Milestone",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Display the creator's name below the app name
            Text(
                text = "A creation by Mihir",
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }

    // Simulate splash screen delay and then navigate to the login screen
    LaunchedEffect(Unit) {
        delay(3000)  // 3 seconds delay for splash screen
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }
}
