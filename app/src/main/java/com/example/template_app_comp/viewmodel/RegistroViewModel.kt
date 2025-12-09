package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.datastore.UsuarioDataStore
import com.example.template_app_comp.data.model.Usuario
import com.example.template_app_comp.data.repository.RepositorioUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegistroViewModel(
    private val repositorioUsuario: RepositorioUsuario,
    private val usuarioDataStore: UsuarioDataStore
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _tieneDescuentoDuoc = MutableStateFlow(false)
    val tieneDescuentoDuoc: StateFlow<Boolean> = _tieneDescuentoDuoc.asStateFlow()

    fun calcularEdad(fechaNacimiento: String): Int? {
        return try {
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fecha = formato.parse(fechaNacimiento)
            if (fecha != null) {
                val hoy = Calendar.getInstance()
                val nacimiento = Calendar.getInstance().apply { time = fecha }
                var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
                if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
                    edad--
                }
                edad
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun actualizarEmail(email: String) {
        _tieneDescuentoDuoc.value = email.endsWith("@duoc.cl")
    }

    fun registrar(
        nombre: String,
        email: String,
        fechaNacimiento: String,
        password: String,
        confirmPassword: String,
        onResult: (Boolean) -> Unit
    ) {
        // Validaciones
        if (nombre.isEmpty() || email.isEmpty() || fechaNacimiento.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            _errorMessage.value = "Por favor completa todos los campos"
            onResult(false)
            return
        }

        if (password != confirmPassword) {
            _errorMessage.value = "Las contraseñas no coinciden"
            onResult(false)
            return
        }

        val edad = calcularEdad(fechaNacimiento)
        if (edad == null || edad < 18) {
            _errorMessage.value = "Debes tener al menos 18 años para registrarte"
            onResult(false)
            return
        }

        // Verificar si tiene descuento DUOC
        val tieneDescuento = email.endsWith("@duoc.cl")
        _tieneDescuentoDuoc.value = tieneDescuento

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            
            try {
                // Guardar usuario en DataStore
                usuarioDataStore.guardarNombre(nombre)
                usuarioDataStore.guardarEmail(email)
                usuarioDataStore.guardarAvatar("placeholder_usuario")
                usuarioDataStore.guardarNivel(1)
                usuarioDataStore.guardarPuntos(0)
                
                _isLoading.value = false
                onResult(true)
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error al registrarse: ${e.message}"
                onResult(false)
            }
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RegistroViewModel(
                        RepositorioUsuario(context),
                        UsuarioDataStore(context)
                    ) as T
                }
            }
        }
    }
}
