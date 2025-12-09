package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.repository.RepositorioReferidos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReferidosViewModel(
    private val repositorioReferidos: RepositorioReferidos
) : ViewModel() {
    private val _puntos = MutableStateFlow(0)
    val puntos: StateFlow<Int> = _puntos.asStateFlow()

    private val _nivel = MutableStateFlow(1)
    val nivel: StateFlow<Int> = _nivel.asStateFlow()

    private val _codigoReferido = MutableStateFlow("LVL-${System.currentTimeMillis().toString().takeLast(6)}")
    val codigoReferido: StateFlow<String> = _codigoReferido.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            repositorioReferidos.getPuntos().collect { puntos ->
                _puntos.value = puntos
            }
            repositorioReferidos.getNivel().collect { nivel ->
                _nivel.value = nivel
            }
        }
    }

    fun obtenerPuntosReferido() {
        viewModelScope.launch {
            repositorioReferidos.agregarPuntos(20) // +20 puntos por referir
        }
    }

    fun progresoHaciaSiguienteNivel(): Float {
        val puntosEnNivelActual = _puntos.value % 100
        return puntosEnNivelActual / 100f
    }

    fun puntosParaSiguienteNivel(): Int {
        return 100 - (_puntos.value % 100)
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ReferidosViewModel(RepositorioReferidos(context)) as T
                }
            }
        }
    }
}








