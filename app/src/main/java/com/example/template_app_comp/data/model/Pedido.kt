package com.example.template_app_comp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Pedido(
    val id: String,
    val fechaCompra: String,
    val productos: List<ItemCarrito>,
    val total: Double,
    val estado: EstadoPedido,
    val fechaEntregaEstimada: String,
    val direccionEntrega: String = ""
)

@Serializable
enum class EstadoPedido {
    PENDIENTE,
    EN_PREPARACION,
    EN_CAMINO,
    ENTREGADO,
    CANCELADO
}

