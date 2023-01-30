package com.example.tvshow.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreSearchTerm(private val context: Context) {

    //to make sure only one instance exists
    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("SearchTerm")
        val SEARCH_TERM_KEY = stringPreferencesKey("search_term")
    }

    val getSearchTerm: Flow<String?> = context.datastore.data
        .map { preferences ->
            preferences[SEARCH_TERM_KEY] ?: ""
        }

    suspend fun saveSearchTerm(searchTerm: String) {
        context.datastore.edit { preferences->
            preferences[SEARCH_TERM_KEY] = searchTerm
        }
    }
}