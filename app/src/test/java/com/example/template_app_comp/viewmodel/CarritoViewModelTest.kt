package com.example.template_app_comp.viewmodel

import com.example.template_app_comp.data.model.ItemCarrito
import com.example.template_app_comp.data.model.Producto
import com.example.template_app_comp.data.repository.RepositorioCarrito
import com.example.template_app_comp.data.repository.RepositorioPedidos
import com.example.template_app_comp.data.repository.RepositorioProductos
import com.example.template_app_comp.data.repository.RepositorioUsuario
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class CarritoViewModelTest {

    @Mock
    private lateinit var repositorioCarrito: RepositorioCarrito

    @Mock
    private lateinit var repositorioProductos: RepositorioProductos

    @Mock
    private lateinit var repositorioUsuario: RepositorioUsuario

    @Mock
    private lateinit var repositorioPedidos: RepositorioPedidos

    private lateinit var viewModel: CarritoViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = CarritoViewModel(
            repositorioCarrito,
            repositorioProductos,
            repositorioUsuario,
            repositorioPedidos
        )
    }

    @Test
    fun `calcular subtotal correctamente`() = runTest {
        // Arrange
        val producto1 = Producto("1", "Producto 1", "Desc", 100.0, "Categoria")
        val producto2 = Producto("2", "Producto 2", "Desc", 200.0, "Categoria")
        val items = listOf(
            ItemCarrito("1", 2),
            ItemCarrito("2", 1)
        )

        whenever(repositorioCarrito.getCarrito()).thenReturn(flowOf(items))
        whenever(repositorioProductos.getProductoPorId("1")).thenReturn(flowOf(producto1))
        whenever(repositorioProductos.getProductoPorId("2")).thenReturn(flowOf(producto2))

        // Act
        val subtotal = viewModel.subtotal.first()

        // Assert
        // (100 * 2) + (200 * 1) = 400
        assertEquals(400.0, subtotal, 0.01)
    }

    @Test
    fun `calcular descuento DUOC correctamente`() = runTest {
        // Arrange
        val items = listOf(ItemCarrito("1", 1))
        val producto = Producto("1", "Producto", "Desc", 100.0, "Categoria")

        whenever(repositorioCarrito.getCarrito()).thenReturn(flowOf(items))
        whenever(repositorioProductos.getProductoPorId("1")).thenReturn(flowOf(producto))
        whenever(repositorioUsuario.getUsuario()).thenReturn(
            flowOf(
                com.example.template_app_comp.data.model.Usuario(
                    "1", "Test", "test@duoc.cl", "", 1, 0
                )
            )
        )

        // Act
        val descuento = viewModel.descuento.first()

        // Assert
        // 20% de 100 = 20
        assertEquals(20.0, descuento, 0.01)
    }

    @Test
    fun `calcular total sin descuento`() = runTest {
        // Arrange
        val items = listOf(ItemCarrito("1", 1))
        val producto = Producto("1", "Producto", "Desc", 100.0, "Categoria")

        whenever(repositorioCarrito.getCarrito()).thenReturn(flowOf(items))
        whenever(repositorioProductos.getProductoPorId("1")).thenReturn(flowOf(producto))
        whenever(repositorioUsuario.getUsuario()).thenReturn(
            flowOf(
                com.example.template_app_comp.data.model.Usuario(
                    "1", "Test", "test@gmail.com", "", 1, 0
                )
            )
        )

        // Act
        val total = viewModel.total.first()

        // Assert
        assertEquals(100.0, total, 0.01)
    }
}

