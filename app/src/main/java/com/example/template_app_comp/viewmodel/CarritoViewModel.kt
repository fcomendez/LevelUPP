package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.model.ItemCarrito
import com.example.template_app_comp.data.model.Producto
import com.example.template_app_comp.data.repository.RepositorioCarrito
import com.example.template_app_comp.data.repository.RepositorioProductos
import com.example.template_app_comp.data.repository.RepositorioUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class CarritoViewModel(
    private val repositorioCarrito: RepositorioCarrito,
    private val repositorioProductos: RepositorioProductos,
    private val repositorioUsuario: RepositorioUsuario
) : ViewModel() {
    private val _items = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val items: StateFlow<List<ItemCarrito>> = _items.asStateFlow()

    private val _productos = MutableStateFlow<Map<String, Producto>>(emptyMap())
    val productos: StateFlow<Map<String, Producto>> = _productos.asStateFlow()

    private val _tieneDescuentoDuoc = MutableStateFlow(false)
    val tieneDescuentoDuoc: StateFlow<Boolean> = _tieneDescuentoDuoc.asStateFlow()

    // Estados para el diálogo y compra
    private val _mostrarDialogoConfirmacion = MutableStateFlow(false)
    val mostrarDialogoConfirmacion: StateFlow<Boolean> = _mostrarDialogoConfirmacion.asStateFlow()

    private val _mensajeCompra = MutableStateFlow("")
    val mensajeCompra: StateFlow<String> = _mensajeCompra.asStateFlow()

    private val _isProcesandoCompra = MutableStateFlow(false)
    val isProcesandoCompra: StateFlow<Boolean> = _isProcesandoCompra.asStateFlow()

    // StateFlow para cálculos reactivos
    val subtotal: StateFlow<Double> = combine(_items, _productos) { items, productos ->
        items.sumOf { item ->
            val producto = productos[item.productoId]
            (producto?.precio ?: 0.0) * item.cantidad
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val descuento: StateFlow<Double> = combine(subtotal, _tieneDescuentoDuoc) { subtotal, tieneDescuento ->
        if (tieneDescuento) {
            subtotal * 0.2 // 20% de descuento
        } else {
            0.0
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val total: StateFlow<Double> = combine(subtotal, descuento) { subtotal, descuento ->
        subtotal - descuento
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    init {
        cargarCarrito()
        verificarDescuentoDuoc()
    }

    private fun cargarCarrito() {
        viewModelScope.launch {
            repositorioCarrito.getCarrito().collect { items ->
                _items.value = items
                // Cargar detalles de productos con manejo de errores
                val productosMap = mutableMapOf<String, Producto>()
                items.forEach { item ->
                    try {
                        repositorioProductos.getProductoPorId(item.productoId).first()?.let { producto ->
                            productosMap[item.productoId] = producto
                        }
                    } catch (e: Exception) {
                        // Si hay error al obtener el producto (ej: error de red), simplemente lo omitimos
                        // El producto no aparecerá en la lista pero no crasheará la app
                    }
                }
                _productos.value = productosMap
            }
        }
    }

    private fun verificarDescuentoDuoc() {
        viewModelScope.launch {
            try {
                val usuario = repositorioUsuario.getUsuario().first()
                _tieneDescuentoDuoc.value = usuario.email.endsWith("@duoc.cl")
            } catch (e: Exception) {
                // Si hay error al obtener el usuario, no aplicamos descuento
                _tieneDescuentoDuoc.value = false
            }
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repositorioCarrito.eliminar(producto)
        }
    }

    fun actualizarCantidad(item: ItemCarrito, nuevaCantidad: Int) {
        viewModelScope.launch {
            if (nuevaCantidad <= 0) {
                val producto = _productos.value[item.productoId]
                if (producto != null) {
                    repositorioCarrito.eliminar(producto)
                }
            } else {
                repositorioCarrito.actualizarCantidad(item.productoId, nuevaCantidad)
            }
        }
    }

    fun procesarCompra() {
        viewModelScope.launch {
            try {
                // 1. Validar carrito no vacío (PRIMERO)
                val itemsActuales = _items.value
                if (itemsActuales.isEmpty()) {
                    _mensajeCompra.value = "Tu carrito está vacío"
                    _mostrarDialogoConfirmacion.value = true
                    return@launch
                }
                
                // 2. Capturar total ANTES del delay (evita cambios)
                val totalCompra = total.value
                
                // 3. Validar total válido
                if (totalCompra <= 0) {
                    _mensajeCompra.value = "El total debe ser mayor a 0"
                    _mostrarDialogoConfirmacion.value = true
                    return@launch
                }
                
                // 4. Activar estado de carga
                _isProcesandoCompra.value = true
                
                // 5. Simular procesamiento (con verificación de scope)
                kotlinx.coroutines.delay(1500)
                
                /* 6. Verificar que el ViewModelScope sigue activo
                if (!viewModelScope.isActive) {
                    _isProcesandoCompra.value = false
                    return@launch
                }*/
                
                // 7. Limpiar carrito (solo si todo está bien)
                repositorioCarrito.limpiar()
                
                // 8. Desactivar carga y mostrar confirmación
                _isProcesandoCompra.value = false
                _mensajeCompra.value = "¡Compra realizada exitosamente!\nTotal: $${String.format("%.0f", totalCompra)}"
                _mostrarDialogoConfirmacion.value = true
                
            } catch (e: Exception) {
                // Manejo de errores completo
                _isProcesandoCompra.value = false
                _mensajeCompra.value = "Error al procesar la compra. Por favor intenta nuevamente."
                _mostrarDialogoConfirmacion.value = true
            }
        }
    }

    fun cerrarDialogo() {
        _mostrarDialogoConfirmacion.value = false
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CarritoViewModel(
                        RepositorioCarrito(context),
                        RepositorioProductos(),
                        RepositorioUsuario(context)
                    ) as T
                }
            }
        }
    }
}
