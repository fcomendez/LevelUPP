package com.example.template_app_comp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.preferenciasDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias")

class PreferenciasDataStore(context: Context) {
    private val dataStore = context.preferenciasDataStore

    private companion object {
        val CIUDAD_SELECCIONADA_KEY = stringPreferencesKey("ciudad_seleccionada")
        val TEMA_OSCURO_KEY = booleanPreferencesKey("tema_oscuro")
        val NOTIFICACIONES_KEY = booleanPreferencesKey("notificaciones")
    }

    val ciudadSeleccionada: Flow<String> = dataStore.data.map { it[CIUDAD_SELECCIONADA_KEY] ?: "Santiago" }
    val temaOscuro: Flow<Boolean> = dataStore.data.map { it[TEMA_OSCURO_KEY] ?: true }
    val notificaciones: Flow<Boolean> = dataStore.data.map { it[NOTIFICACIONES_KEY] ?: true }

    suspend fun guardarCiudad(ciudad: String) {
        dataStore.edit { it[CIUDAD_SELECCIONADA_KEY] = ciudad }
    }

    suspend fun guardarTemaOscuro(temaOscuro: Boolean) {
        dataStore.edit { it[TEMA_OSCURO_KEY] = temaOscuro }
    }

    suspend fun guardarNotificaciones(notificaciones: Boolean) {
        dataStore.edit { it[NOTIFICACIONES_KEY] = notificaciones }
    }
}










