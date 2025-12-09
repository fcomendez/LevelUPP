package com.example.template_app_comp.data.repository

import android.content.Context
import com.example.template_app_comp.data.datastore.UsuarioDataStore
import com.example.template_app_comp.data.fake.DatosFake
import com.example.template_app_comp.data.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class RepositorioUsuario(private val context: Context) {
    private val usuarioDataStore = UsuarioDataStore(context)

    fun getUsuario(): Flow<Usuario> = combine(
        usuarioDataStore.nombre,
        usuarioDataStore.email,
        usuarioDataStore.avatar,
        usuarioDataStore.nivel,
        usuarioDataStore.puntos
    ) { nombre, email, avatar, nivel, puntos ->
        if (nombre.isNotEmpty()) {
            Usuario("1", nombre, email, avatar, nivel, puntos)
        } else {
            DatosFake.usuarioInicial
        }
    }

    suspend fun loginFake(email: String, pass: String) {
        // Simulación de login - guardamos los datos del usuario fake
        usuarioDataStore.guardarEmail(email)
        usuarioDataStore.guardarNombre("Estudiante DUOC")
        usuarioDataStore.guardarAvatar("placeholder_usuario")
        usuarioDataStore.guardarNivel(1)
        usuarioDataStore.guardarPuntos(0)
    }

    suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDataStore.guardarNombre(usuario.nombre)
        usuarioDataStore.guardarEmail(usuario.email)
        usuarioDataStore.guardarAvatar(usuario.avatar)
        usuarioDataStore.guardarNivel(usuario.nivel)
        usuarioDataStore.guardarPuntos(usuario.puntos)
    }

    suspend fun cerrarSesion() {
        usuarioDataStore.limpiar()
    }
}




