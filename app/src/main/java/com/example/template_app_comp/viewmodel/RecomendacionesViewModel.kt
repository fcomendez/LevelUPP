package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.model.Producto
import com.example.template_app_comp.data.repository.RepositorioRecomendaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecomendacionesViewModel(
    private val repositorioRecomendaciones: RepositorioRecomendaciones
) : ViewModel() {
    private val _productosRecomendados = MutableStateFlow<List<Producto>>(emptyList())
    val productosRecomendados: StateFlow<List<Producto>> = _productosRecomendados.asStateFlow()

    private val _historialCategorias = MutableStateFlow<List<String>>(listOf("Juegos de Mesa", "Accesorios"))
    val historialCategorias: StateFlow<List<String>> = _historialCategorias.asStateFlow()

    init {
        cargarRecomendaciones()
    }

    private fun cargarRecomendaciones() {
        viewModelScope.launch {
            repositorioRecomendaciones.obtenerRecomendaciones(_historialCategorias.value).collect { productos ->
                _productosRecomendados.value = productos
            }
        }
    }

    fun actualizarHistorial(categoria: String) {
        _historialCategorias.value = _historialCategorias.value + categoria
        cargarRecomendaciones()
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RecomendacionesViewModel(RepositorioRecomendaciones()) as T
                }
            }
        }
    }
}










