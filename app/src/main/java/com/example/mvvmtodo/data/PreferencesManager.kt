package com.example.mvvmtodo.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder { BY_NAME, BY_DATE }

data class FilterPrefs(val sordOrder: SortOrder, val hideCompleted: Boolean)

private const val TAG = "PreferencesManager"

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val datastore = context.createDataStore("user_preferences")

    val preferencesFlow = datastore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_NAME.name
            )

            val hideCompleted = preferences[PreferencesKeys.HIDE_COMPLETED] ?: false
            FilterPrefs(sortOrder, hideCompleted)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) =
        datastore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }

    suspend fun updateHideCompleted(hideCompleted: Boolean) =
        datastore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }

    private object PreferencesKeys {
        val SORT_ORDER = preferencesKey<String>("sortOrder")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hideCompleted")
    }

}