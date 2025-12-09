package com.example.template_app_comp.ui.screens.recomendaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.template_app_comp.ui.navigation.Rutas
import com.example.template_app_comp.viewmodel.RecomendacionesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecomendaciones(
    navController: NavController,
    viewModel: RecomendacionesViewModel = viewModel(factory = RecomendacionesViewModel.factory(LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val productosRecomendados by viewModel.productosRecomendados.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recomendaciones para ti") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productosRecomendados) { producto ->
                Card(
                    onClick = { navController.navigate(Rutas.detalleProducto(producto.id)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
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
        }
    }
}










