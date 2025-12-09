package com.example.template_app_comp.ui.screens.eventos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.template_app_comp.ui.navigation.Rutas
import com.example.template_app_comp.viewmodel.EventosViewModel

@Composable
fun PantallaEventos(
    navController: NavController,
    viewModel: EventosViewModel = viewModel(factory = EventosViewModel.factory(LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val ciudades by viewModel.ciudades.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Selecciona una ciudad",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ciudades) { ciudad ->
                Card(
                    onClick = { 
                        // Usar codificación URL para manejar espacios y caracteres especiales
                        val ciudadCodificada = java.net.URLEncoder.encode(ciudad, "UTF-8")
                        navController.navigate(Rutas.eventosLista(ciudadCodificada))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = ciudad,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}


