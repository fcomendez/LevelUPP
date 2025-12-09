package com.example.template_app_comp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imagen: String = "placeholder_producto"
)





