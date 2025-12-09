package com.example.template_app_comp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.template_app_comp.data.model.EstadoPedido
import com.example.template_app_comp.data.model.Pedido
import com.example.template_app_comp.data.repository.RepositorioPedidos
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PedidosViewModel(
    private val repositorioPedidos: RepositorioPedidos
) : ViewModel() {
    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    init {
        cargarPedidos()
        iniciarSimulacionSeguimiento()
    }

    private fun cargarPedidos() {
        viewModelScope.launch {
            repositorioPedidos.getPedidos().collect { listaPedidos ->
                _pedidos.value = listaPedidos
            }
        }
    }

    private fun iniciarSimulacionSeguimiento() {
        viewModelScope.launch {
            while (true) {
                delay(10000) // Actualizar cada 10 segundos
                val pedidosActuales = _pedidos.value.toMutableList()
                var hayCambios = false

                pedidosActuales.forEachIndexed { index, pedido ->
                    if (pedido.estado != EstadoPedido.ENTREGADO && pedido.estado != EstadoPedido.CANCELADO) {
                        val nuevoEstado = avanzarEstado(pedido.estado)
                        if (nuevoEstado != pedido.estado) {
                            pedidosActuales[index] = pedido.copy(estado = nuevoEstado)
                            hayCambios = true
                        }
                    }
                }

                if (hayCambios) {
                    pedidosActuales.forEach { pedido ->
                        repositorioPedidos.actualizarPedido(pedido)
                    }
                }
            }
        }
    }

    private fun avanzarEstado(estadoActual: EstadoPedido): EstadoPedido {
        return when (estadoActual) {
            EstadoPedido.PENDIENTE -> EstadoPedido.EN_PREPARACION
            EstadoPedido.EN_PREPARACION -> EstadoPedido.EN_CAMINO
            EstadoPedido.EN_CAMINO -> EstadoPedido.ENTREGADO
            else -> estadoActual
        }
    }

    fun calcularFechaEntrega(fechaCompra: String): String {
        return try {
            val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val fecha = formato.parse(fechaCompra)
            if (fecha != null) {
                val calendario = Calendar.getInstance()
                calendario.time = fecha
                calendario.add(Calendar.DAY_OF_MONTH, 3) // 3 días hábiles
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendario.time)
            } else {
                "Fecha no disponible"
            }
        } catch (e: Exception) {
            "Fecha no disponible"
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PedidosViewModel(RepositorioPedidos(context)) as T
                }
            }
        }
    }
}

