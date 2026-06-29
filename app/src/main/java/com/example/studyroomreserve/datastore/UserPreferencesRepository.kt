package com.example.studyroomreserve.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(context: Context) {
    private val dataStore = context.dataStore

    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val DEFAULT_BUILDING = stringPreferencesKey("default_building")
    }

    val darkModeFlow: Flow<Boolean> = dataStore.data
        .map { it[Keys.DARK_MODE] ?: false }

    val defaultBuildingFlow: Flow<String> = dataStore.data
        .map { it[Keys.DEFAULT_BUILDING] ?: "全部" }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[Keys.DARK_MODE] = enabled }
    }

    suspend fun setDefaultBuilding(building: String) {
        dataStore.edit { it[Keys.DEFAULT_BUILDING] = building }
    }
}