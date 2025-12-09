package com.example.template_app_comp.ui.screens.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.template_app_comp.data.model.EstadoPedido
import com.example.template_app_comp.data.model.Pedido
import com.example.template_app_comp.viewmodel.PedidosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPedidos(
    navController: NavController,
    viewModel: PedidosViewModel = viewModel(factory = PedidosViewModel.factory(LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val pedidos by viewModel.pedidos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (pedidos.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No tienes pedidos aún",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tus compras aparecerán aquí",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos) { pedido ->
                    CardPedido(pedido, viewModel)
                }
            }
        }
    }
}

@Composable
fun CardPedido(pedido: Pedido, viewModel: PedidosViewModel) {
    val fechaEntrega = viewModel.calcularFechaEntrega(pedido.fechaCompra)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (pedido.estado) {
                EstadoPedido.ENTREGADO -> MaterialTheme.colorScheme.primaryContainer
                EstadoPedido.EN_CAMINO -> MaterialTheme.colorScheme.secondaryContainer
                EstadoPedido.EN_PREPARACION -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pedido #${pedido.id.take(8)}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Fecha: ${pedido.fechaCompra}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconoEstado(pedido.estado)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            HorizontalDivider()
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Productos: ${pedido.productos.size}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Estado:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = obtenerTextoEstado(pedido.estado),
                        style = MaterialTheme.typography.bodyLarge,
                        color = obtenerColorEstado(pedido.estado)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${String.format("%.0f", pedido.total)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (pedido.estado != EstadoPedido.ENTREGADO && pedido.estado != EstadoPedido.CANCELADO) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "📦 Entrega estimada: $fechaEntrega",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            if (pedido.direccionEntrega.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "📍 ${pedido.direccionEntrega}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun IconoEstado(estado: EstadoPedido) {
    val (icono, color) = when (estado) {
        EstadoPedido.PENDIENTE -> Icons.Default.Pending to MaterialTheme.colorScheme.onSurfaceVariant
        EstadoPedido.EN_PREPARACION -> Icons.Default.Restaurant to MaterialTheme.colorScheme.tertiary
        EstadoPedido.EN_CAMINO -> Icons.Default.LocalShipping to MaterialTheme.colorScheme.secondary
        EstadoPedido.ENTREGADO -> Icons.Default.CheckCircle to MaterialTheme.colorScheme.primary
        EstadoPedido.CANCELADO -> Icons.Default.Pending to MaterialTheme.colorScheme.error
    }
    
    Icon(
        imageVector = icono,
        contentDescription = obtenerTextoEstado(estado),
        tint = color,
        modifier = Modifier.size(32.dp)
    )
}

fun obtenerTextoEstado(estado: EstadoPedido): String {
    return when (estado) {
        EstadoPedido.PENDIENTE -> "Pendiente"
        EstadoPedido.EN_PREPARACION -> "En Preparación"
        EstadoPedido.EN_CAMINO -> "En Camino"
        EstadoPedido.ENTREGADO -> "Entregado"
        EstadoPedido.CANCELADO -> "Cancelado"
    }
}

@Composable
fun obtenerColorEstado(estado: EstadoPedido): androidx.compose.ui.graphics.Color {
    return when (estado) {
        EstadoPedido.PENDIENTE -> MaterialTheme.colorScheme.onSurfaceVariant
        EstadoPedido.EN_PREPARACION -> MaterialTheme.colorScheme.tertiary
        EstadoPedido.EN_CAMINO -> MaterialTheme.colorScheme.secondary
        EstadoPedido.ENTREGADO -> MaterialTheme.colorScheme.primary
        EstadoPedido.CANCELADO -> MaterialTheme.colorScheme.error
    }
}

