package com.example.template_app_comp.viewmodel

import com.example.template_app_comp.data.repository.RepositorioUsuario
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LoginViewModelTest {

    @Mock
    private lateinit var repositorioUsuario: RepositorioUsuario

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(repositorioUsuario)
    }

    @Test
    fun `validar email vacio retorna false`() {
        // Act
        val resultado = viewModel.validarEmail("")

        // Assert
        assertFalse(resultado)
        assertEquals("El email es requerido", viewModel.emailError.value)
    }

    @Test
    fun `validar email invalido retorna false`() {
        // Act
        val resultado = viewModel.validarEmail("emailinvalido")

        // Assert
        assertFalse(resultado)
        assertEquals("El formato del email no es válido", viewModel.emailError.value)
    }

    @Test
    fun `validar email valido retorna true`() {
        // Act
        val resultado = viewModel.validarEmail("test@example.com")

        // Assert
        assertTrue(resultado)
        assertNull(viewModel.emailError.value)
    }

    @Test
    fun `validar password vacia retorna false`() {
        // Act
        val resultado = viewModel.validarPassword("")

        // Assert
        assertFalse(resultado)
        assertEquals("La contraseña es requerida", viewModel.passwordError.value)
    }

    @Test
    fun `validar password corta retorna false`() {
        // Act
        val resultado = viewModel.validarPassword("12345")

        // Assert
        assertFalse(resultado)
        assertEquals("La contraseña debe tener al menos 6 caracteres", viewModel.passwordError.value)
    }

    @Test
    fun `validar password valida retorna true`() {
        // Act
        val resultado = viewModel.validarPassword("password123")

        // Assert
        assertTrue(resultado)
        assertNull(viewModel.passwordError.value)
    }

    @Test
    fun `login con usuario existente es exitoso`() = runTest {
        // Arrange
        val email = "test@duoc.cl"
        val password = "password123"
        val usuario = com.example.template_app_comp.data.model.Usuario(
            "1", "Test", email, "", 1, 0
        )

        whenever(repositorioUsuario.getUsuario()).thenReturn(flowOf(usuario))

        var resultadoExitoso = false
        // Act
        viewModel.login(email, password) { exito ->
            resultadoExitoso = exito
        }

        // Assert
        assertTrue(resultadoExitoso)
        verify(repositorioUsuario).loginFake(email, password)
    }
}

