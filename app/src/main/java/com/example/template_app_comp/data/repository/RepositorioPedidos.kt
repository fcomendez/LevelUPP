package com.example.template_app_comp.data.repository

import android.content.Context
import com.example.template_app_comp.data.datastore.PedidosDataStore
import com.example.template_app_comp.data.model.Pedido
import kotlinx.coroutines.flow.Flow

class RepositorioPedidos(context: Context) {
    private val pedidosDataStore = PedidosDataStore(context)

    fun getPedidos(): Flow<List<Pedido>> = pedidosDataStore.pedidos

    suspend fun guardarPedido(pedido: Pedido) {
        pedidosDataStore.guardarPedido(pedido)
    }

    suspend fun actualizarPedido(pedido: Pedido) {
        pedidosDataStore.actualizarPedido(pedido)
    }
}

