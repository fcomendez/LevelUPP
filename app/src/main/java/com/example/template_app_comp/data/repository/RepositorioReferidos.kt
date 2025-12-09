package com.example.template_app_comp.data.repository

import android.content.Context
import com.example.template_app_comp.data.datastore.UsuarioDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class RepositorioReferidos(private val context: Context) {
    private val usuarioDataStore = UsuarioDataStore(context)

    fun getPuntos(): Flow<Int> = usuarioDataStore.puntos

    fun getNivel(): Flow<Int> = usuarioDataStore.nivel

    suspend fun agregarPuntos(puntos: Int) {
        val puntosActuales = usuarioDataStore.puntos.first()
        val nuevoPuntos = puntosActuales + puntos
        usuarioDataStore.guardarPuntos(nuevoPuntos)
        
        // Calcular nuevo nivel (cada 100 puntos = 1 nivel, máximo nivel 5)
        val nuevoNivel = minOf(5, (nuevoPuntos / 100) + 1)
        usuarioDataStore.guardarNivel(nuevoNivel)
    }
}
