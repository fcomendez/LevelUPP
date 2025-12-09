package com.example.template_app_comp.data.model

data class Evento(
    val id: String,
    val ciudad: String,
    val fecha: String,
    val nombre: String,
    val descripcion: String,
    val imagen: String = "placeholder_evento",
    val direccion: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)








