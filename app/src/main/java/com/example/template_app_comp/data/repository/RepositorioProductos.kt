package com.example.template_app_comp.data.repository

import com.example.template_app_comp.data.fake.DatosFake
import com.example.template_app_comp.data.model.Producto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class RepositorioProductos {

    private val URL_PRODUCTOS_GIST = "https://gist.github.com/6fdef6bbb4b28d9b211c2d00911d8ef9.git"
    // ═══════════════════════════════════════════════════════════════════════════
    
    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false
            })
        }
    }
    
    fun getProductos(): Flow<List<Producto>> = flow {
        try {
            // Si la URL está configurada, intenta leer desde GIST
            if (URL_PRODUCTOS_GIST.isNotEmpty()) {
                val productos = httpClient.get(URL_PRODUCTOS_GIST).body<List<Producto>>()
                emit(productos)
            } else {
                // Si no hay URL configurada, usa datos fake como fallback
                emit(DatosFake.productos)
            }
        } catch (e: Exception) {
            // En caso de error, usa datos fake como fallback
            emit(DatosFake.productos)
        }
    }

    fun getProductoPorId(id: String): Flow<Producto?> = flow {
        try {
            // Si la URL está configurada, intenta leer desde GIST
            if (URL_PRODUCTOS_GIST.isNotEmpty()) {
                val productos = httpClient.get(URL_PRODUCTOS_GIST).body<List<Producto>>()
                emit(productos.find { it.id == id })
            } else {
                // Si no hay URL configurada, usa datos fake como fallback
                emit(DatosFake.productos.find { it.id == id })
            }
        } catch (e: Exception) {
            // En caso de error, usa datos fake como fallback
            emit(DatosFake.productos.find { it.id == id })
        }
    }
}
