package com.example.template_app_comp.ui.navigation

object Rutas {
    const val INICIO = "inicio"
    const val CATALOGO = "catalogo"
    const val DETALLE_PRODUCTO = "detalle_producto/{productoId}"
    const val RESENAS = "reseñas/{productoId}"
    const val EVENTOS = "eventos"
    const val EVENTOS_LISTA = "eventos_lista/{ciudad}"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val PERFIL = "perfil"
    const val REFERIDOS = "referidos"
    const val NIVEL = "nivel"
    const val RECOMENDACIONES = "recomendaciones"
    const val CONFIGURACION = "configuracion"
    const val CARRITO = "carrito"
    const val SOPORTE = "soporte"
    const val FAQ = "faq"
    const val CONTACTO = "contacto"
    const val ORIGEN = "origen"
    
    fun detalleProducto(productoId: String) = "detalle_producto/$productoId"
    fun reseñas(productoId: String) = "reseñas/$productoId"
    fun eventosLista(ciudad: String) = "eventos_lista/$ciudad"
}
