package com.example.template_app_comp.data.repository

import com.example.template_app_comp.data.fake.DatosFake
import com.example.template_app_comp.data.model.Resena
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class RepositorioResenas {
    private val reseñasMutables = MutableStateFlow<Map<String, List<Resena>>>(DatosFake.resenas)

    fun getReseñasDeProducto(idProducto: String): Flow<List<Resena>> = 
        reseñasMutables.map { mapa ->
            mapa[idProducto] ?: emptyList()
        }

    suspend fun agregarReseña(resena: Resena) {
        val mapaActual = reseñasMutables.value.toMutableMap()
        val reseñasDelProducto = mapaActual[resena.productoId]?.toMutableList() ?: mutableListOf()
        reseñasDelProducto.add(resena)
        mapaActual[resena.productoId] = reseñasDelProducto
        reseñasMutables.value = mapaActual
    }
}


