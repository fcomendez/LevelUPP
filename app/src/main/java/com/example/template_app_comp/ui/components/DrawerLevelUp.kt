package com.example.template_app_comp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template_app_comp.ui.navigation.Rutas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerLevelUp(
    onNavigate: (String) -> Unit,
    currentRoute: String?,
    onClose: () -> Unit,
    estaLogueado: Boolean,
    onCerrarSesion: suspend () -> Unit,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "LevelUp",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Principal",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            NavigationDrawerItem(
                label = { Text("Inicio") },
                selected = currentRoute == Rutas.INICIO,
                onClick = { onNavigate(Rutas.INICIO); onClose() },
                icon = { Icon(Icons.Default.Home, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Catálogo") },
                selected = currentRoute == Rutas.CATALOGO,
                onClick = { onNavigate(Rutas.CATALOGO); onClose() },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Carrito") },
                selected = currentRoute == Rutas.CARRITO,
                onClick = { onNavigate(Rutas.CARRITO); onClose() },
                icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) }
            )
            if (estaLogueado) {
                NavigationDrawerItem(
                    label = { Text("Mis Pedidos") },
                    selected = currentRoute == Rutas.PEDIDOS,
                    onClick = { onNavigate(Rutas.PEDIDOS); onClose() },
                    icon = { Icon(Icons.Default.Receipt, contentDescription = null) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Mi Cuenta",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            NavigationDrawerItem(
                label = { Text("Perfil") },
                selected = currentRoute == Rutas.PERFIL,
                onClick = {
                    if (estaLogueado) {
                        onNavigate(Rutas.PERFIL)
                    } else {
                        onNavigate(Rutas.LOGIN)
                    }
                    onClose()
                },
                icon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Referidos / LevelUp") },
                selected = currentRoute == Rutas.REFERIDOS,
                onClick = { onNavigate(Rutas.REFERIDOS); onClose() },
                icon = { Icon(Icons.Default.Star, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Comunidad",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            NavigationDrawerItem(
                label = { Text("Eventos") },
                selected = currentRoute == Rutas.EVENTOS,
                onClick = { onNavigate(Rutas.EVENTOS); onClose() },
                icon = { Icon(Icons.Default.Event, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Soporte",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            NavigationDrawerItem(
                label = { Text("Soporte Técnico") },
                selected = currentRoute == Rutas.SOPORTE,
                onClick = { onNavigate(Rutas.SOPORTE); onClose() },
                icon = { Icon(Icons.Default.Support, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Preguntas Frecuentes") },
                selected = currentRoute == Rutas.FAQ,
                onClick = { onNavigate(Rutas.FAQ); onClose() },
                icon = { Icon(Icons.Default.Info, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Contacto") },
                selected = currentRoute == Rutas.CONTACTO,
                onClick = { onNavigate(Rutas.CONTACTO); onClose() },
                icon = { Icon(Icons.Default.Email, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            NavigationDrawerItem(
                label = { Text("Origen de Productos") },
                selected = currentRoute == Rutas.ORIGEN,
                onClick = { onNavigate(Rutas.ORIGEN); onClose() },
                icon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
            )
            NavigationDrawerItem(
                label = { Text("Recomendaciones") },
                selected = currentRoute == Rutas.RECOMENDACIONES,
                onClick = { onNavigate(Rutas.RECOMENDACIONES); onClose() },
                icon = { Icon(Icons.Default.ThumbUp, contentDescription = null) }
            )

            // Botón de Cerrar Sesión (solo visible si está logueado)
            if (estaLogueado) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            onCerrarSesion()
                            onNavigate(Rutas.INICIO)
                            onClose()
                        }
                    },
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onErrorContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}



