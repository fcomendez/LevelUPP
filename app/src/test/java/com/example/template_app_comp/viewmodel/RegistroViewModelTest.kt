package com.example.template_app_comp.viewmodel

import com.example.template_app_comp.data.datastore.UsuarioDataStore
import com.example.template_app_comp.data.repository.RepositorioUsuario
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RegistroViewModelTest {

    @Mock
    private lateinit var repositorioUsuario: RepositorioUsuario

    @Mock
    private lateinit var usuarioDataStore: UsuarioDataStore

    private lateinit var viewModel: RegistroViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = RegistroViewModel(repositorioUsuario, usuarioDataStore)
    }

    @Test
    fun `validar nombre vacio retorna false`() {
        // Act
        val resultado = viewModel.validarNombre("")

        // Assert
        assertFalse(resultado)
        assertEquals("El nombre es requerido", viewModel.nombreError.value)
    }

    @Test
    fun `validar nombre corto retorna false`() {
        // Act
        val resultado = viewModel.validarNombre("Ab")

        // Assert
        assertFalse(resultado)
        assertEquals("El nombre debe tener al menos 3 caracteres", viewModel.nombreError.value)
    }

    @Test
    fun `validar nombre con numeros retorna false`() {
        // Act
        val resultado = viewModel.validarNombre("Juan123")

        // Assert
        assertFalse(resultado)
        assertEquals("El nombre solo puede contener letras", viewModel.nombreError.value)
    }

    @Test
    fun `validar nombre valido retorna true`() {
        // Act
        val resultado = viewModel.validarNombre("Juan Pérez")

        // Assert
        assertTrue(resultado)
        assertNull(viewModel.nombreError.value)
    }

    @Test
    fun `validar email duoc activa descuento`() {
        // Act
        viewModel.validarEmail("test@duoc.cl")

        // Assert
        assertTrue(viewModel.tieneDescuentoDuoc.value)
    }

    @Test
    fun `calcular edad correctamente`() {
        // Act
        val edad = viewModel.calcularEdad("01/01/2000")

        // Assert
        assertNotNull(edad)
        assertTrue(edad!! >= 18) // Debe ser mayor de edad
    }

    @Test
    fun `validar fecha formato invalido retorna false`() {
        // Act
        val resultado = viewModel.validarFechaNacimiento("01-01-2000")

        // Assert
        assertFalse(resultado)
        assertEquals("El formato debe ser DD/MM/AAAA", viewModel.fechaNacimientoError.value)
    }

    @Test
    fun `validar password sin mayuscula retorna false`() {
        // Act
        val resultado = viewModel.validarPassword("password123")

        // Assert
        assertFalse(resultado)
        assertEquals("La contraseña debe contener al menos una mayúscula", viewModel.passwordError.value)
    }

    @Test
    fun `validar password sin numero retorna false`() {
        // Act
        val resultado = viewModel.validarPassword("Password")

        // Assert
        assertFalse(resultado)
        assertEquals("La contraseña debe contener al menos un número", viewModel.passwordError.value)
    }

    @Test
    fun `validar password valida retorna true`() {
        // Act
        val resultado = viewModel.validarPassword("Password123")

        // Assert
        assertTrue(resultado)
        assertNull(viewModel.passwordError.value)
    }

    @Test
    fun `validar confirmacion password no coincide retorna false`() {
        // Act
        val resultado = viewModel.validarConfirmPassword("Password123", "Password456")

        // Assert
        assertFalse(resultado)
        assertEquals("Las contraseñas no coinciden", viewModel.confirmPasswordError.value)
    }
}

