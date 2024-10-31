package com.example.milestone

import androidx.navigation.NavController

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Composable
fun MainScreen(navController : NavController,  context: Context) {
    var selectedTab by remember { mutableStateOf("chat") }  // Track the selected tab
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var chatResponse by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val requestQueue = Volley.newRequestQueue(context)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tab -> selectedTab = tab }
            )
        },
        content = { paddingValues ->
            // Handling different tabs
            when (selectedTab) {
                "chat" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Chat Input Area
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            // Display the chat response
                            if (chatResponse.isNotEmpty()) {
                                Text(
                                    text = chatResponse,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                Text(
                                    text = "Hey! I'm Myles. How can I assist you today?",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        // Text input for ChatGPT
                        BasicTextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(Color.White)
                                .padding(8.dp),
                            singleLine = true
                        )

                        // Submit button to send the text to the OpenAI API
                        Button(
                            onClick = {
                                if (userInput.text.isNotEmpty()) {
                                    coroutineScope.launch {
                                        chatResponse = getOpenAIResponse(userInput.text, requestQueue, context)
                                    }
                                } else {
                                    Toast.makeText(context, "Please enter a query.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Send")
                        }
                    }
                }
                "achievements" -> {
                    // New Achievements Screen
                    AchievementsScreen()
                }
                "plans" -> {
                    // New Plans and Notes Screen
                    PlansScreen()
                }
                // You can add more screens for other tabs like "notes" if needed
            }
        }
    )
}


@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    BottomAppBar(
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left tab: Achievements
                IconButton(onClick = { onTabSelected("achievements") }) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Achievements",
                        tint = if (selectedTab == "achievements") Color.Blue else Color.Gray
                    )
                }

                // Middle tab: Chat (Selected by default)
                IconButton(onClick = { onTabSelected("chat") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Chat,
                        contentDescription = "Chat",
                        tint = if (selectedTab == "chat") Color.Blue else Color.Gray
                    )
                }

                // Right tab: Notes
                IconButton(onClick = { onTabSelected("plans") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Notes,
                        contentDescription = "Plans",
                        tint = if (selectedTab == "plans") Color.Blue else Color.Gray
                    )
                }
            }
        }
    )
}


// Function to make the OpenAI API call using GPT-4 model
suspend fun getOpenAIResponse(query: String, requestQueue: RequestQueue, context: Context): String = suspendCoroutine { continuation ->
    val url = "https://api.openai.com/v1/chat/completions"
    val jsonObject = JSONObject()

    // Request body setup for the GPT-4 model
    jsonObject.put("model", "gpt-4") // GPT-4 model specified here
    jsonObject.put("messages", JSONArray().apply {
        put(JSONObject().apply {
            put("role", "system")
            put("content", "You are a helpful assistant to generate plans and notes for my life.") // Initial instruction for the assistant
        })
        put(JSONObject().apply {
            put("role", "user")
            put("content", query) // User query
        })
    })
    jsonObject.put("temperature", 0.7) // Adjust based on desired response creativity
    jsonObject.put("max_tokens", 100) // Adjust as needed

    val postRequest = object : JsonObjectRequest(
        Method.POST, url, jsonObject,
        { response ->
            val result = response
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content").trim()
            continuation.resume(result) // Resume coroutine with response
        },
        { error ->
            Log.e("TAGAPI", "Error: ${error.message}")
            Toast.makeText(context, "Error fetching response.", Toast.LENGTH_SHORT).show()
            continuation.resumeWithException(error) // Resume coroutine with exception
        }
    ) {
        override fun getHeaders(): MutableMap<String, String> {
            val params: MutableMap<String, String> = HashMap()
            params["Content-Type"] = "application/json"
            params["Authorization"] = "Bearer ${BuildConfig.OPENAI_API_KEY}" // API Key in BuildConfig
            return params
        }
    }

    postRequest.retryPolicy = DefaultRetryPolicy(
        5000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )

    // Add request to the queue
    requestQueue.add(postRequest)
}

