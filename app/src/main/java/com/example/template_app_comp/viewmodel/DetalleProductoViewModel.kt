package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.model.Producto
import com.example.template_app_comp.data.repository.RepositorioProductos
import com.example.template_app_comp.data.repository.RepositorioCarrito
import com.example.template_app_comp.data.repository.RepositorioUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetalleProductoViewModel(
    private val productoId: String,
    private val repositorioProductos: RepositorioProductos,
    private val repositorioCarrito: RepositorioCarrito,
    private val repositorioUsuario: RepositorioUsuario
) : ViewModel() {
    private val _producto = MutableStateFlow<Producto?>(null)
    val producto: StateFlow<Producto?> = _producto.asStateFlow()

    private val _tieneDescuentoDuoc = MutableStateFlow(false)
    val tieneDescuentoDuoc: StateFlow<Boolean> = _tieneDescuentoDuoc.asStateFlow()

    private val _agregadoAlCarrito = MutableStateFlow(false)
    val agregadoAlCarrito: StateFlow<Boolean> = _agregadoAlCarrito.asStateFlow()

    init {
        cargarProducto()
        verificarDescuentoDuoc()
    }

    private fun cargarProducto() {
        viewModelScope.launch {
            repositorioProductos.getProductoPorId(productoId).collect { producto ->
                _producto.value = producto
            }
        }
    }

    private fun verificarDescuentoDuoc() {
        viewModelScope.launch {
            val email = repositorioUsuario.getUsuario().first().email
            _tieneDescuentoDuoc.value = email.endsWith("@duoc.cl")
        }
    }

    fun precioConDescuento(): Double {
        val producto = _producto.value ?: return 0.0
        return if (_tieneDescuentoDuoc.value) {
            producto.precio * 0.8 // 20% de descuento
        } else {
            producto.precio
        }
    }

    fun agregarAlCarrito() {
        viewModelScope.launch {
            _producto.value?.let { producto ->
                repositorioCarrito.agregar(producto)
                _agregadoAlCarrito.value = true
            }
        }
    }

    companion object {
        fun factory(context: Context, productoId: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetalleProductoViewModel(
                        productoId,
                        RepositorioProductos(),
                        RepositorioCarrito(context),
                        RepositorioUsuario(context)
                    ) as T
                }
            }
        }
    }
}








