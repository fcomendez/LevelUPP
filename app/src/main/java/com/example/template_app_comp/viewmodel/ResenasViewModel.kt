package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.model.Resena
import com.example.template_app_comp.data.repository.RepositorioResenas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResenasViewModel(
    private val productoId: String,
    private val repositorioResenas: RepositorioResenas
) : ViewModel() {
    private val _reseñas = MutableStateFlow<List<Resena>>(emptyList())
    val reseñas: StateFlow<List<Resena>> = _reseñas.asStateFlow()

    private val _mostrarFormulario = MutableStateFlow(false)
    val mostrarFormulario: StateFlow<Boolean> = _mostrarFormulario.asStateFlow()

    private val _nuevaResenaAutor = MutableStateFlow("")
    val nuevaResenaAutor: StateFlow<String> = _nuevaResenaAutor.asStateFlow()

    private val _nuevaResenaRating = MutableStateFlow(5)
    val nuevaResenaRating: StateFlow<Int> = _nuevaResenaRating.asStateFlow()

    private val _nuevaResenaComentario = MutableStateFlow("")
    val nuevaResenaComentario: StateFlow<String> = _nuevaResenaComentario.asStateFlow()

    init {
        cargarReseñas()
    }

    private fun cargarReseñas() {
        viewModelScope.launch {
            repositorioResenas.getReseñasDeProducto(productoId).collect { reseñas ->
                _reseñas.value = reseñas
            }
        }
    }

    fun mostrarFormulario(mostrar: Boolean) {
        _mostrarFormulario.value = mostrar
    }

    fun actualizarAutor(autor: String) {
        _nuevaResenaAutor.value = autor
    }

    fun actualizarRating(rating: Int) {
        _nuevaResenaRating.value = rating
    }

    fun actualizarComentario(comentario: String) {
        _nuevaResenaComentario.value = comentario
    }

    fun agregarReseña() {
        viewModelScope.launch {
            if (_nuevaResenaAutor.value.isNotEmpty() && _nuevaResenaComentario.value.isNotEmpty()) {
                val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val nuevaResena = Resena(
                    id = "R${System.currentTimeMillis()}",
                    productoId = productoId,
                    autor = _nuevaResenaAutor.value,
                    rating = _nuevaResenaRating.value,
                    comentario = _nuevaResenaComentario.value,
                    fecha = fechaActual
                )
                repositorioResenas.agregarReseña(nuevaResena)
                
                // Limpiar formulario
                _nuevaResenaAutor.value = ""
                _nuevaResenaComentario.value = ""
                _nuevaResenaRating.value = 5
                _mostrarFormulario.value = false
            }
        }
    }

    companion object {
        fun factory(context: Context, productoId: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ResenasViewModel(productoId, RepositorioResenas()) as T
                }
            }
        }
    }
}










