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

    private val _nombreError = MutableStateFlow<String?>(null)
    val nombreError: StateFlow<String?> = _nombreError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _fechaNacimientoError = MutableStateFlow<String?>(null)
    val fechaNacimientoError: StateFlow<String?> = _fechaNacimientoError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

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

    fun validarNombre(nombre: String): Boolean {
        return when {
            nombre.isEmpty() -> {
                _nombreError.value = "El nombre es requerido"
                false
            }
            nombre.length < 3 -> {
                _nombreError.value = "El nombre debe tener al menos 3 caracteres"
                false
            }
            !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) -> {
                _nombreError.value = "El nombre solo puede contener letras"
                false
            }
            else -> {
                _nombreError.value = null
                true
            }
        }
    }

    fun validarEmail(email: String): Boolean {
        val tieneDescuento = email.endsWith("@duoc.cl")
        _tieneDescuentoDuoc.value = tieneDescuento
        
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

    fun validarFechaNacimiento(fechaNacimiento: String): Boolean {
        return when {
            fechaNacimiento.isEmpty() -> {
                _fechaNacimientoError.value = "La fecha de nacimiento es requerida"
                false
            }
            !fechaNacimiento.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$")) -> {
                _fechaNacimientoError.value = "El formato debe ser DD/MM/AAAA"
                false
            }
            calcularEdad(fechaNacimiento) == null -> {
                _fechaNacimientoError.value = "La fecha no es válida"
                false
            }
            calcularEdad(fechaNacimiento)!! < 18 -> {
                _fechaNacimientoError.value = "Debes tener al menos 18 años"
                false
            }
            else -> {
                _fechaNacimientoError.value = null
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
            !password.matches(Regex(".*[A-Z].*")) -> {
                _passwordError.value = "La contraseña debe contener al menos una mayúscula"
                false
            }
            !password.matches(Regex(".*[0-9].*")) -> {
                _passwordError.value = "La contraseña debe contener al menos un número"
                false
            }
            else -> {
                _passwordError.value = null
                true
            }
        }
    }

    fun validarConfirmPassword(password: String, confirmPassword: String): Boolean {
        return when {
            confirmPassword.isEmpty() -> {
                _confirmPasswordError.value = "Confirma tu contraseña"
                false
            }
            password != confirmPassword -> {
                _confirmPasswordError.value = "Las contraseñas no coinciden"
                false
            }
            else -> {
                _confirmPasswordError.value = null
                true
            }
        }
    }

    fun actualizarEmail(email: String) {
        validarEmail(email)
    }

    fun registrar(
        nombre: String,
        email: String,
        fechaNacimiento: String,
        password: String,
        confirmPassword: String,
        onResult: (Boolean) -> Unit
    ) {
        // Validar todos los campos
        val nombreValido = validarNombre(nombre)
        val emailValido = validarEmail(email)
        val fechaValida = validarFechaNacimiento(fechaNacimiento)
        val passwordValido = validarPassword(password)
        val confirmPasswordValido = validarConfirmPassword(password, confirmPassword)

        if (!nombreValido || !emailValido || !fechaValida || !passwordValido || !confirmPasswordValido) {
            _errorMessage.value = "Por favor corrige los errores en el formulario"
            onResult(false)
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            
            try {
                // Verificar si el email ya está registrado
                val emailYaRegistrado = try {
                    val usuario = repositorioUsuario.getUsuario().first()
                    usuario.email == email && usuario.email.isNotEmpty()
                } catch (e: Exception) {
                    false
                }
                
                if (emailYaRegistrado) {
                    _isLoading.value = false
                    _errorMessage.value = "Este email ya está registrado. Por favor inicia sesión."
                    _emailError.value = "Email ya registrado"
                    onResult(false)
                    return@launch
                }
                
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
