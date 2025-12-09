package com.example.template_app_comp.ui.screens.reseñas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.template_app_comp.viewmodel.ResenasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReseñas(
    productoId: String,
    navController: NavController,
    viewModel: ResenasViewModel = viewModel(factory = ResenasViewModel.factory(LocalContext.current, productoId)),
    modifier: Modifier = Modifier
) {
    val reseñas by viewModel.reseñas.collectAsState()
    val mostrarFormulario by viewModel.mostrarFormulario.collectAsState()
    val nuevoAutor by viewModel.nuevaResenaAutor.collectAsState()
    val nuevoRating by viewModel.nuevaResenaRating.collectAsState()
    val nuevoComentario by viewModel.nuevaResenaComentario.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reseñas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.mostrarFormulario(true) }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (mostrarFormulario) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Agregar Reseña", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = nuevoAutor,
                            onValueChange = { viewModel.actualizarAutor(it) },
                            label = { Text("Tu nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Calificación: $nuevoRating")
                        Slider(
                            value = nuevoRating.toFloat(),
                            onValueChange = { viewModel.actualizarRating(it.toInt()) },
                            valueRange = 1f..5f,
                            steps = 3
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = nuevoComentario,
                            onValueChange = { viewModel.actualizarComentario(it) },
                            label = { Text("Comentario") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.mostrarFormulario(false) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                            Button(
                                onClick = { viewModel.agregarReseña() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Agregar")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(reseñas) { reseña ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = reseña.autor,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Row {
                                    repeat(reseña.rating) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = reseña.comentario,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = reseña.fecha,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}










