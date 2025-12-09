package com.example.template_app_comp.data.model

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val avatar: String = "placeholder_usuario",
    val nivel: Int = 1,
    val puntos: Int = 0
)








