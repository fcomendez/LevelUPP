package com.example.template_app_comp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.template_app_comp.data.model.Pedido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

private val Context.pedidosDataStore: DataStore<Preferences> by preferencesDataStore(name = "pedidos")

class PedidosDataStore(context: Context) {
    private val dataStore = context.pedidosDataStore
    private val json = Json { ignoreUnknownKeys = true }

    private companion object {
        val PEDIDOS_KEY = stringPreferencesKey("pedidos_lista")
    }

    val pedidos: Flow<List<Pedido>> = dataStore.data.map { preferences ->
        val pedidosJson = preferences[PEDIDOS_KEY] ?: "[]"
        try {
            json.decodeFromString<List<Pedido>>(pedidosJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun guardarPedido(pedido: Pedido) {
        dataStore.edit { preferences ->
            val pedidosActuales = try {
                val pedidosJson = preferences[PEDIDOS_KEY] ?: "[]"
                json.decodeFromString<List<Pedido>>(pedidosJson)
            } catch (e: Exception) {
                emptyList()
            }
            val pedidosActualizados = pedidosActuales + pedido
            preferences[PEDIDOS_KEY] = json.encodeToString(pedidosActualizados)
        }
    }

    suspend fun actualizarPedido(pedido: Pedido) {
        dataStore.edit { preferences ->
            val pedidosActuales = try {
                val pedidosJson = preferences[PEDIDOS_KEY] ?: "[]"
                json.decodeFromString<List<Pedido>>(pedidosJson)
            } catch (e: Exception) {
                emptyList()
            }
            val pedidosActualizados = pedidosActuales.map { 
                if (it.id == pedido.id) pedido else it 
            }
            preferences[PEDIDOS_KEY] = json.encodeToString(pedidosActualizados)
        }
    }

    suspend fun limpiar() {
        dataStore.edit { it.clear() }
    }
}

