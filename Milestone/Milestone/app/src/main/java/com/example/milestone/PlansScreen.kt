package com.example.milestone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun PlansScreen() {
    // State for flowchart steps
    var stepText by remember { mutableStateOf(TextFieldValue("")) }
    val flowchartSteps = remember { mutableStateListOf<String>() }  // List to hold steps

    // State for notes
    var noteText by remember { mutableStateOf(TextFieldValue("")) }
    val notesList = remember { mutableStateListOf<String>() }  // List to hold notes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Flowchart section
        Text(text = "Flowchart Steps", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // TextField for inputting new flowchart step
        BasicTextField(
            value = stepText,
            onValueChange = { stepText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
                .padding(8.dp),
            singleLine = true
        )

        // Button to add step to the flowchart
        Button(
            onClick = {
                if (stepText.text.isNotEmpty()) {
                    flowchartSteps.add(stepText.text)
                    stepText = TextFieldValue("")  // Clear the input field after adding
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Add Flowchart Step")
        }

        // Display flowchart steps
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Your Flowchart:", fontSize = 18.sp)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(flowchartSteps) { step ->
                Text(text = step, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Notes section
        Text(text = "Notes", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // TextField for inputting new note
        BasicTextField(
            value = noteText,
            onValueChange = { noteText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
                .padding(8.dp),
            singleLine = true
        )

        // Button to add note to the list
        Button(
            onClick = {
                if (noteText.text.isNotEmpty()) {
                    notesList.add(noteText.text)
                    noteText = TextFieldValue("")  // Clear the input field after adding
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Add Note")
        }

        // Display notes
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Your Notes:", fontSize = 18.sp)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(notesList) { note ->
                Text(text = note, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
