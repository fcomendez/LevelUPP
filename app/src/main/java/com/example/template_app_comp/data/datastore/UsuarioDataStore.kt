package com.example.template_app_comp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.usuarioDataStore: DataStore<Preferences> by preferencesDataStore(name = "usuario")

class UsuarioDataStore(context: Context) {
    private val dataStore = context.usuarioDataStore

    private companion object {
        val NOMBRE_KEY = stringPreferencesKey("nombre")
        val EMAIL_KEY = stringPreferencesKey("email")
        val AVATAR_KEY = stringPreferencesKey("avatar")
        val NIVEL_KEY = intPreferencesKey("nivel")
        val PUNTOS_KEY = intPreferencesKey("puntos")
    }

    val nombre: Flow<String> = dataStore.data.map { it[NOMBRE_KEY] ?: "" }
    val email: Flow<String> = dataStore.data.map { it[EMAIL_KEY] ?: "" }
    val avatar: Flow<String> = dataStore.data.map { it[AVATAR_KEY] ?: "placeholder_usuario" }
    val nivel: Flow<Int> = dataStore.data.map { it[NIVEL_KEY] ?: 1 }
    val puntos: Flow<Int> = dataStore.data.map { it[PUNTOS_KEY] ?: 0 }

    suspend fun guardarNombre(nombre: String) {
        dataStore.edit { it[NOMBRE_KEY] = nombre }
    }

    suspend fun guardarEmail(email: String) {
        dataStore.edit { it[EMAIL_KEY] = email }
    }

    suspend fun guardarAvatar(avatar: String) {
        dataStore.edit { it[AVATAR_KEY] = avatar }
    }

    suspend fun guardarNivel(nivel: Int) {
        dataStore.edit { it[NIVEL_KEY] = nivel }
    }

    suspend fun guardarPuntos(puntos: Int) {
        dataStore.edit { it[PUNTOS_KEY] = puntos }
    }

    suspend fun limpiar() {
        dataStore.edit { it.clear() }
    }
}










