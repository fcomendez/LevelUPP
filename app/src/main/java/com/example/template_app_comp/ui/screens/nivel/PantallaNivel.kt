package com.example.template_app_comp.ui.screens.nivel

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
import com.example.template_app_comp.viewmodel.NivelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNivel(
    navController: NavController,
    viewModel: NivelViewModel = viewModel(factory = NivelViewModel.factory(LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val niveles by viewModel.niveles.collectAsState()
    val nivelActual by viewModel.nivelActual.collectAsState()
    val puntos by viewModel.puntos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sistema de Niveles LevelUp") },
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
            items(niveles) { nivel ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (nivel.nivel == nivelActual) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Nivel ${nivel.nivel}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            if (nivel.nivel == nivelActual) {
                                Text(
                                    text = "Actual",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${nivel.puntosMinimos} - ${if (nivel.puntosMaximos == Int.MAX_VALUE) "∞" else nivel.puntosMaximos.toString()} puntos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = nivel.descripcion,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}










