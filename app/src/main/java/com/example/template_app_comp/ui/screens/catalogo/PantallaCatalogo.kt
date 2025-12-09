package com.example.template_app_comp.ui.screens.catalogo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.template_app_comp.data.model.Producto
import com.example.template_app_comp.ui.navigation.Rutas
import com.example.template_app_comp.viewmodel.CatalogoViewModel

@Composable
fun PantallaCatalogo(
    navController: NavController,
    viewModel: CatalogoViewModel = viewModel(factory = CatalogoViewModel.factory(LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val productos by viewModel.productos.collectAsState()
    val categorias = viewModel.categorias()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    
    val productosFiltrados = if (categoriaSeleccionada == null) {
        productos
    } else {
        productos.filter { it.categoria == categoriaSeleccionada }
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = categoriaSeleccionada == null,
                    onClick = { viewModel.seleccionarCategoria(null) },
                    label = { Text("Todos") }
                )
            }
            items(categorias) { categoria ->
                FilterChip(
                    selected = categoriaSeleccionada == categoria,
                    onClick = { viewModel.seleccionarCategoria(categoria) },
                    label = { Text(categoria) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productosFiltrados) { producto ->
                ProductoCard(producto = producto, navController = navController)
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { navController.navigate(Rutas.detalleProducto(producto.id)) },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = producto.categoria,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${String.format("%.0f", producto.precio)}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
