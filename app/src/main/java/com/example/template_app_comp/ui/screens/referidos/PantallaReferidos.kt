package com.example.template_app_comp.ui.screens.referidos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.template_app_comp.viewmodel.ReferidosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReferidos(
    navController: NavController,
    viewModel: ReferidosViewModel = viewModel(factory = ReferidosViewModel.factory(LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val puntos by viewModel.puntos.collectAsState()
    val nivel by viewModel.nivel.collectAsState()
    val codigoReferido by viewModel.codigoReferido.collectAsState()
    val progreso = viewModel.progresoHaciaSiguienteNivel()
    val puntosParaSiguiente = viewModel.puntosParaSiguienteNivel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Programa LevelUp") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nivel $nivel",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$puntos puntos",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(32.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Progreso al siguiente nivel",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Faltan $puntosParaSiguiente puntos para nivel ${nivel + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tu código de referido",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = codigoReferido,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.obtenerPuntosReferido() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Obtener puntos por referido (+20 puntos)")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { navController.navigate(Rutas.NIVEL) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver información de niveles")
            }
        }
    }
}










