package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.fake.DatosFake
import com.example.template_app_comp.data.model.Nivel
import com.example.template_app_comp.data.repository.RepositorioReferidos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NivelViewModel(
    private val repositorioReferidos: RepositorioReferidos
) : ViewModel() {
    private val _niveles = MutableStateFlow<List<Nivel>>(DatosFake.niveles)
    val niveles: StateFlow<List<Nivel>> = _niveles.asStateFlow()

    private val _nivelActual = MutableStateFlow(1)
    val nivelActual: StateFlow<Int> = _nivelActual.asStateFlow()

    private val _puntos = MutableStateFlow(0)
    val puntos: StateFlow<Int> = _puntos.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            repositorioReferidos.getNivel().collect { nivel ->
                _nivelActual.value = nivel
            }
            repositorioReferidos.getPuntos().collect { puntos ->
                _puntos.value = puntos
            }
        }
    }

    fun obtenerNivel(nivel: Int): Nivel? {
        return _niveles.value.find { it.nivel == nivel }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NivelViewModel(RepositorioReferidos(context)) as T
                }
            }
        }
    }
}








