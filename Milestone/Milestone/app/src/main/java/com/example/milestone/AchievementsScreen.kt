package com.example.milestone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AchievementsScreen() {
    var achievementText by remember { mutableStateOf(TextFieldValue("")) }
    var achievementsList by remember { mutableStateOf(mutableListOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(text = "Achievements", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // TextField for inputting new achievement
        BasicTextField(
            value = achievementText,
            onValueChange = { achievementText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
                .padding(8.dp),
            singleLine = true
        )

        // Button to add achievement to the list
        Button(
            onClick = {
                if (achievementText.text.isNotEmpty()) {
                    achievementsList.add(achievementText.text)
                    achievementText = TextFieldValue("") // Clear the input field after adding
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Add Achievement")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display achievements
        Text(text = "Your Achievements:", fontSize = 18.sp)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(achievementsList) { achievement ->
                Text(text = achievement, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
