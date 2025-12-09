package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.model.Usuario
import com.example.template_app_comp.data.repository.RepositorioUsuario
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class PerfilViewModel(
    private val repositorioUsuario: RepositorioUsuario
) : ViewModel() {
    // CORRECCIÓN: Usar stateIn() con valor inicial para evitar loading infinito
    val usuario: StateFlow<Usuario> = repositorioUsuario.getUsuario()
        .catch { e ->
            // Si hay un error, emitir usuario inicial
            emit(com.example.template_app_comp.data.fake.DatosFake.usuarioInicial)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = com.example.template_app_comp.data.fake.DatosFake.usuarioInicial
        )

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PerfilViewModel(RepositorioUsuario(context)) as T
                }
            }
        }
    }
}


