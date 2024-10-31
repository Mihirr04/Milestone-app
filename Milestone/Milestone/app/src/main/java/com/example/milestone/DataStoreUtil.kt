package com.example.milestone


import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance
val Context.dataStore by preferencesDataStore(name = "user_credentials")

// Define keys for username and password
object UserPreferencesKeys {
    val USERNAME_KEY = stringPreferencesKey("username")
    val PASSWORD_KEY = stringPreferencesKey("password")
}

// Function to save credentials
suspend fun saveCredentials(context: Context, username: String, password: String) {
    context.dataStore.edit { preferences ->
        preferences[UserPreferencesKeys.USERNAME_KEY] = username
        preferences[UserPreferencesKeys.PASSWORD_KEY] = password
    }
}

// Function to retrieve username
fun getUsername(context: Context): Flow<String?> {
    return context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.USERNAME_KEY]
    }
}

// Function to retrieve password
fun getPassword(context: Context): Flow<String?> {
    return context.dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.PASSWORD_KEY]
    }
}
