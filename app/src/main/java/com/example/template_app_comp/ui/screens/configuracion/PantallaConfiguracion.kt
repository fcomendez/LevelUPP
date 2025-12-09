package com.example.template_app_comp.ui.screens.configuracion

import androidx.compose.foundation.layout.*
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
import com.example.template_app_comp.viewmodel.ConfiguracionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConfiguracion(
    navController: NavController,
    viewModel: ConfiguracionViewModel = viewModel(factory = ConfiguracionViewModel.factory(LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val ciudadSeleccionada by viewModel.ciudadSeleccionada.collectAsState()
    val temaOscuro by viewModel.temaOscuro.collectAsState()
    val notificaciones by viewModel.notificaciones.collectAsState()

    val ciudades = listOf("Santiago", "Valparaíso", "Concepción")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ciudad",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ciudades.forEach { ciudad ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = ciudadSeleccionada == ciudad,
                                onClick = { viewModel.cambiarCiudad(ciudad) }
                            )
                            Text(
                                text = ciudad,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tema oscuro",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = temaOscuro,
                        onCheckedChange = { viewModel.cambiarTemaOscuro(it) }
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = "Notificaciones",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = notificaciones,
                        onCheckedChange = { viewModel.cambiarNotificaciones(it) }
                    )
                }
            }
        }
    }
}










