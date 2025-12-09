package com.example.template_app_comp.data.repository

import com.example.template_app_comp.data.fake.DatosFake
import com.example.template_app_comp.data.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositorioRecomendaciones {
    fun obtenerRecomendaciones(historialCategorias: List<String>): Flow<List<Producto>> = flow {
        if (historialCategorias.isEmpty()) {
            // Si no hay historial, retornar productos más populares
            emit(DatosFake.productos.take(5))
        } else {
            // Obtener la categoría más vista
            val categoriaMasVista = historialCategorias.groupingBy { it }.eachCount()
                .maxByOrNull { it.value }?.key ?: historialCategorias.firstOrNull()
            
            // Retornar productos de esa categoría
            val recomendados = DatosFake.productos.filter { it.categoria == categoriaMasVista }
            emit(if (recomendados.isNotEmpty()) recomendados.take(5) else DatosFake.productos.take(5))
        }
    }
}








