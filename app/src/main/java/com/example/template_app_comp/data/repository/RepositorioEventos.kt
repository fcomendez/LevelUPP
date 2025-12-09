package com.example.template_app_comp.data.repository

import com.example.template_app_comp.data.fake.DatosFake
import com.example.template_app_comp.data.model.Evento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositorioEventos {
    fun getCiudades(): Flow<List<String>> = flow {
        emit(DatosFake.ciudades)
    }

    fun getEventosPorCiudad(ciudad: String): Flow<List<Evento>> = flow {
        emit(DatosFake.eventosPorCiudad[ciudad] ?: emptyList())
    }
}








