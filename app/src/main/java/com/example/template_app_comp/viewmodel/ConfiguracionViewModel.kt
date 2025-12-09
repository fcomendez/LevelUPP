package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.datastore.PreferenciasDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfiguracionViewModel(
    private val preferenciasDataStore: PreferenciasDataStore
) : ViewModel() {
    private val _ciudadSeleccionada = MutableStateFlow("Santiago")
    val ciudadSeleccionada: StateFlow<String> = _ciudadSeleccionada.asStateFlow()

    private val _temaOscuro = MutableStateFlow(true)
    val temaOscuro: StateFlow<Boolean> = _temaOscuro.asStateFlow()

    private val _notificaciones = MutableStateFlow(true)
    val notificaciones: StateFlow<Boolean> = _notificaciones.asStateFlow()

    init {
        cargarPreferencias()
    }

    private fun cargarPreferencias() {
        viewModelScope.launch {
            preferenciasDataStore.ciudadSeleccionada.collect { ciudad ->
                _ciudadSeleccionada.value = ciudad
            }
            preferenciasDataStore.temaOscuro.collect { tema ->
                _temaOscuro.value = tema
            }
            preferenciasDataStore.notificaciones.collect { notif ->
                _notificaciones.value = notif
            }
        }
    }

    fun cambiarCiudad(ciudad: String) {
        viewModelScope.launch {
            preferenciasDataStore.guardarCiudad(ciudad)
        }
    }

    fun cambiarTemaOscuro(temaOscuro: Boolean) {
        viewModelScope.launch {
            preferenciasDataStore.guardarTemaOscuro(temaOscuro)
        }
    }

    fun cambiarNotificaciones(notificaciones: Boolean) {
        viewModelScope.launch {
            preferenciasDataStore.guardarNotificaciones(notificaciones)
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ConfiguracionViewModel(PreferenciasDataStore(context)) as T
                }
            }
        }
    }
}








