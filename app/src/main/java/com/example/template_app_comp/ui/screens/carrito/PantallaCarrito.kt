package com.example.template_app_comp.ui.screens.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.template_app_comp.ui.navigation.Rutas
import com.example.template_app_comp.viewmodel.CarritoViewModel

@Composable
fun PantallaCarrito(
    navController: NavController,
    viewModel: CarritoViewModel = viewModel(factory = CarritoViewModel.factory(LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val items by viewModel.items.collectAsState()
    val productos by viewModel.productos.collectAsState()
    val tieneDescuento by viewModel.tieneDescuentoDuoc.collectAsState()
    val subtotal by viewModel.subtotal.collectAsState()
    val descuento by viewModel.descuento.collectAsState()
    val total by viewModel.total.collectAsState()
    val mostrarDialogo by viewModel.mostrarDialogoConfirmacion.collectAsState()
    val mensajeCompra by viewModel.mensajeCompra.collectAsState()
    val isProcesando by viewModel.isProcesandoCompra.collectAsState()
    val compraExitosa by viewModel.compraExitosa.collectAsState()

    // Redirección automática después de compra exitosa
    LaunchedEffect(compraExitosa) {
        if (compraExitosa && mensajeCompra.contains("exitosamente")) {
            kotlinx.coroutines.delay(2000) // Esperar 2 segundos para que el usuario vea el mensaje
            navController.navigate(Rutas.CATALOGO) {
                popUpTo(Rutas.CARRITO) { inclusive = true }
            }
            viewModel.cerrarDialogo()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Carrito de compras",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    val producto = productos[item.productoId]
                    if (producto != null) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = producto.nombre,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "$${String.format("%.0f", producto.precio)} x ${item.cantidad}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    if (tieneDescuento) {
                                        Text(
                                            text = "Con descuento DUOC: $${String.format("%.0f", producto.precio * 0.8)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                IconButton(onClick = { viewModel.eliminarProducto(producto) }) {
                                    Icon(Icons.Default.Delete, "Eliminar")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal:")
                        Text("$${String.format("%.0f", subtotal)}")
                    }
                    if (tieneDescuento) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Descuento DUOC (20%):")
                            Text(
                                "-$${String.format("%.0f", descuento)}",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total:",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "$${String.format("%.0f", total)}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.procesarCompra() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isProcesando && items.isNotEmpty()
                    ) {
                        if (isProcesando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Procesando...")
                        } else {
                            Text("Comprar")
                        }
                    }
                }
            }
        }
        
        // Diálogo de confirmación
        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { viewModel.cerrarDialogo() },
                title = { 
                    Text(
                        if (mensajeCompra.contains("exitosamente")) "¡Compra Exitosa!" 
                        else "Compra"
                    )
                },
                text = { Text(mensajeCompra) },
                confirmButton = {
                    TextButton(
                        onClick = { 
                            viewModel.cerrarDialogo()
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}



