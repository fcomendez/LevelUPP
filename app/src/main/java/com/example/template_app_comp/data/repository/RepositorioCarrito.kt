package com.example.template_app_comp.data.repository

import android.content.Context
import com.example.template_app_comp.data.datastore.CarritoDataStore
import com.example.template_app_comp.data.model.ItemCarrito
import com.example.template_app_comp.data.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class RepositorioCarrito(private val context: Context) {
    private val carritoDataStore = CarritoDataStore(context)

    fun getCarrito(): Flow<List<ItemCarrito>> = carritoDataStore.obtenerCarrito()

    suspend fun agregar(producto: Producto) {
        val listaActual = carritoDataStore.obtenerCarrito().first().toMutableList()
        val itemExistente = listaActual.find { it.productoId == producto.id }
        
        if (itemExistente != null) {
            listaActual.replaceAll { item ->
                if (item.productoId == producto.id) {
                    item.copy(cantidad = item.cantidad + 1)
                } else {
                    item
                }
            }
        } else {
            listaActual.add(ItemCarrito(producto.id, 1))
        }
        
        carritoDataStore.guardarCarrito(listaActual)
    }

    suspend fun eliminar(producto: Producto) {
        val listaActual = carritoDataStore.obtenerCarrito().first().filter { it.productoId != producto.id }
        carritoDataStore.guardarCarrito(listaActual)
    }

    suspend fun actualizarCantidad(productoId: String, nuevaCantidad: Int) {
        val listaActual = carritoDataStore.obtenerCarrito().first().toMutableList()
        val index = listaActual.indexOfFirst { it.productoId == productoId }
        
        if (nuevaCantidad <= 0) {
            // Si la cantidad es 0 o menor, eliminar el producto
            listaActual.removeAll { it.productoId == productoId }
        } else if (index >= 0) {
            // Actualizar cantidad del producto existente
            listaActual[index] = listaActual[index].copy(cantidad = nuevaCantidad)
        }
        
        carritoDataStore.guardarCarrito(listaActual)
    }

    suspend fun limpiar() {
        carritoDataStore.guardarCarrito(emptyList())
    }
}
