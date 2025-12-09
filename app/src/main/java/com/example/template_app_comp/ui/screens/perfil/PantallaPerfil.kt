package com.example.template_app_comp.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.template_app_comp.ui.navigation.Rutas
import com.example.template_app_comp.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    navController: NavController,
    viewModel: PerfilViewModel = viewModel(factory = PerfilViewModel.factory(LocalContext.current)),
    modifier: Modifier = Modifier
) {
    val usuario by viewModel.usuario.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        // CORRECCIÓN: Con stateIn() y valor inicial, usuario nunca será null
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // Placeholder de usuario creado en código
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    color = Color(0xFF39FF14) // Verde neón #39FF14
                ) {}
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = usuario.nombre,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = usuario.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Nivel: ${usuario.nivel}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Puntos: ${usuario.puntos}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate(Rutas.REFERIDOS) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Referidos / LevelUp")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { navController.navigate(Rutas.RECOMENDACIONES) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recomendaciones")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { navController.navigate(Rutas.CONFIGURACION) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Configuración")
            }
        }
    }
}

