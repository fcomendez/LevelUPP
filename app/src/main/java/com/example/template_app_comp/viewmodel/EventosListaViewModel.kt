package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.model.Evento
import com.example.template_app_comp.data.repository.RepositorioEventos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventosListaViewModel(
    private val ciudad: String,
    private val repositorioEventos: RepositorioEventos
) : ViewModel() {
    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos.asStateFlow()

    private val _participandoEnEvento = MutableStateFlow<String?>(null)
    val participandoEnEvento: StateFlow<String?> = _participandoEnEvento.asStateFlow()

    init {
        cargarEventos()
    }

    private fun cargarEventos() {
        viewModelScope.launch {
            repositorioEventos.getEventosPorCiudad(ciudad).collect { eventos ->
                _eventos.value = eventos
            }
        }
    }

    fun participarEnEvento(eventoId: String) {
        _participandoEnEvento.value = eventoId
        // Aquí se podría agregar lógica para guardar la participación
    }

    companion object {
        fun factory(context: Context, ciudad: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EventosListaViewModel(ciudad, RepositorioEventos()) as T
                }
            }
        }
    }
}








