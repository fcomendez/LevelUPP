package com.example.template_app_comp.ui.screens.producto_detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.template_app_comp.ui.navigation.Rutas
import com.example.template_app_comp.viewmodel.DetalleProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleProducto(
    productoId: String,
    navController: NavController,
    viewModel: DetalleProductoViewModel = viewModel(
        factory = DetalleProductoViewModel.factory(LocalContext.current, productoId)
    ),
    modifier: Modifier = Modifier
) {
    val producto by viewModel.producto.collectAsState()
    val tieneDescuento by viewModel.tieneDescuentoDuoc.collectAsState()
    val agregadoAlCarrito by viewModel.agregadoAlCarrito.collectAsState()
    val precioConDescuento = viewModel.precioConDescuento()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            producto?.let { prod ->
                Text(
                    text = prod.nombre,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = prod.categoria,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = prod.descripcion,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                if (tieneDescuento) {
                    Text(
                        text = "Precio: $${String.format("%.0f", prod.precio)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Precio con descuento DUOC (20%): $${String.format("%.0f", precioConDescuento)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "Precio: $${String.format("%.0f", precioConDescuento)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { viewModel.agregarAlCarrito() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !agregadoAlCarrito
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (agregadoAlCarrito) "Agregado al carrito" else "Agregar al carrito")
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { navController.navigate(Rutas.reseñas(prod.id)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver reseñas")
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { navController.navigate(Rutas.RECOMENDACIONES) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Recomendados similares")
                }
            }
        }
    }
}
