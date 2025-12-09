package com.example.template_app_comp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.template_app_comp.data.model.ItemCarrito
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val Context.carritoDataStore: DataStore<Preferences> by preferencesDataStore(name = "carrito")

class CarritoDataStore(context: Context) {
    private val dataStore = context.carritoDataStore
    private val json = Json { ignoreUnknownKeys = true }

    private companion object {
        val CARRO_KEY = stringPreferencesKey("carrito_json")
    }

    fun obtenerCarrito(): Flow<List<ItemCarrito>> =
        dataStore.data.map { prefs ->
            val jsonString = prefs[CARRO_KEY] ?: "[]"
            try {
                json.decodeFromString<List<ItemCarrito>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }

    suspend fun guardarCarrito(lista: List<ItemCarrito>) {
        dataStore.edit { prefs ->
            val jsonString = json.encodeToString(lista)
            prefs[CARRO_KEY] = jsonString
        }
    }
}










