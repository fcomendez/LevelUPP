package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.repository.RepositorioEventos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventosViewModel(
    private val repositorioEventos: RepositorioEventos
) : ViewModel() {
    private val _ciudades = MutableStateFlow<List<String>>(emptyList())
    val ciudades: StateFlow<List<String>> = _ciudades.asStateFlow()

    init {
        cargarCiudades()
    }

    private fun cargarCiudades() {
        viewModelScope.launch {
            repositorioEventos.getCiudades().collect { ciudades ->
                _ciudades.value = ciudades
            }
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EventosViewModel(RepositorioEventos()) as T
                }
            }
        }
    }
}








