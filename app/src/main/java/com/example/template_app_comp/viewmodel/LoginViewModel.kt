package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.repository.RepositorioUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repositorioUsuario: RepositorioUsuario
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    fun validarEmail(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                _emailError.value = "El email es requerido"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _emailError.value = "El formato del email no es válido"
                false
            }
            else -> {
                _emailError.value = null
                true
            }
        }
    }

    fun validarPassword(password: String): Boolean {
        return when {
            password.isEmpty() -> {
                _passwordError.value = "La contraseña es requerida"
                false
            }
            password.length < 6 -> {
                _passwordError.value = "La contraseña debe tener al menos 6 caracteres"
                false
            }
            else -> {
                _passwordError.value = null
                true
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        val emailValido = validarEmail(email)
        val passwordValido = validarPassword(password)

        if (!emailValido || !passwordValido) {
            _errorMessage.value = "Por favor corrige los errores en el formulario"
            onResult(false)
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            
            try {
                // Simulación de login - verificar si el usuario existe
                // En un caso real, esto consultaría una base de datos
                val usuarioExiste = verificarUsuarioExiste(email)
                
                if (!usuarioExiste) {
                    _isLoading.value = false
                    _errorMessage.value = "Las credenciales no son correctas o el usuario no está registrado"
                    _emailError.value = "Usuario no encontrado"
                    onResult(false)
                    return@launch
                }
                
                repositorioUsuario.loginFake(email, password)
                _isLoading.value = false
                onResult(true)
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error al iniciar sesión: ${e.message}"
                onResult(false)
            }
        }
    }

    private suspend fun verificarUsuarioExiste(email: String): Boolean {
        // Simulación: verificar si el usuario está guardado en DataStore
        // En un caso real, esto consultaría una base de datos
        return try {
            val usuario = repositorioUsuario.getUsuario().first()
            usuario.email == email && usuario.email.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(RepositorioUsuario(context)) as T
                }
            }
        }
    }
}








