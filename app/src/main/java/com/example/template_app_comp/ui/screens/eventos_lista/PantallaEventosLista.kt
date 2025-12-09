package com.example.template_app_comp.ui.screens.eventos_lista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.template_app_comp.viewmodel.EventosListaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEventosLista(
    ciudad: String,
    navController: NavController,
    viewModel: EventosListaViewModel = viewModel(factory = EventosListaViewModel.factory(LocalContext.current, ciudad)),
    modifier: Modifier = Modifier
) {
    val eventos by viewModel.eventos.collectAsState()
    val participandoEnEvento by viewModel.participandoEnEvento.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eventos en $ciudad") },
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
            items(eventos) { evento ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Placeholder de evento creado en código
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E90FF) // Azul eléctrico #1E90FF
                        ) {}
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = evento.nombre,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = evento.fecha,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = evento.descripcion,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.participarEnEvento(evento.id) },
                            enabled = participandoEnEvento != evento.id,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (participandoEnEvento == evento.id) "Participando" else "Participar")
                        }
                    }
                }
            }
        }
    }
}




