package com.example.template_app_comp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.datastore.UsuarioDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class InicioViewModel(
    private val usuarioDataStore: UsuarioDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    // Verifica si el usuario está logueado (si tiene nombre guardado)
    // CORRECCIÓN: Convertir Flow a StateFlow con stateIn() para que funcione correctamente
    val estaLogueado: StateFlow<Boolean> = usuarioDataStore.nombre
        .map { nombre ->
            val logueado = nombre.isNotEmpty()
            Log.d("InicioViewModel", "Nombre del usuario: '$nombre', Logueado: $logueado")
            logueado
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    data class InicioUiState(
        val isLoading: Boolean = false
    )

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return InicioViewModel(UsuarioDataStore(context)) as T
                }
            }
        }
    }
}


