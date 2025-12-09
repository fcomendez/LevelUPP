package com.example.template_app_comp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import com.example.template_app_comp.ui.components.DrawerLevelUp
import com.example.template_app_comp.ui.navigation.Rutas
import com.example.template_app_comp.ui.screens.carrito.PantallaCarrito
import com.example.template_app_comp.ui.screens.catalogo.PantallaCatalogo
import com.example.template_app_comp.ui.screens.configuracion.PantallaConfiguracion
import com.example.template_app_comp.ui.screens.eventos.PantallaEventos
import com.example.template_app_comp.ui.screens.eventos_lista.PantallaEventosLista
import com.example.template_app_comp.ui.screens.inicio.PantallaInicio
import com.example.template_app_comp.ui.screens.login.PantallaLogin
import com.example.template_app_comp.ui.screens.nivel.PantallaNivel
import com.example.template_app_comp.ui.screens.perfil.PantallaPerfil
import com.example.template_app_comp.ui.screens.producto_detalle.PantallaDetalleProducto
import com.example.template_app_comp.ui.screens.recomendaciones.PantallaRecomendaciones
import com.example.template_app_comp.ui.screens.referidos.PantallaReferidos
import com.example.template_app_comp.ui.screens.registro.PantallaRegistro
import com.example.template_app_comp.ui.screens.reseñas.PantallaReseñas
import com.example.template_app_comp.ui.theme.TemaLevelUp
import com.example.template_app_comp.viewmodel.*
import com.example.template_app_comp.data.repository.RepositorioUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHostController() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    val drawerState = androidx.compose.material3.rememberDrawerState(
        initialValue = androidx.compose.material3.DrawerValue.Closed
    )
    
    // Obtener el estado de login para pasarlo al Drawer
    val context = LocalContext.current
    val inicioViewModel: InicioViewModel = viewModel(factory = InicioViewModel.factory(context))
    val estaLogueado by inicioViewModel.estaLogueado.collectAsState()
    val repositorioUsuario = RepositorioUsuario(context)

    TemaLevelUp {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerLevelUp(
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    currentRoute = currentRoute,
                    onClose = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    estaLogueado = estaLogueado,
                    onCerrarSesion = {
                        repositorioUsuario.cerrarSesion()
                    },
                    scope = scope
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("LevelUp") },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Menu,
                                    "Menu"
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Rutas.INICIO,
                    modifier = Modifier.padding(padding)
                ) {
                    composable(Rutas.INICIO) {
                        PantallaInicio(navController)
                    }
                    composable(Rutas.CATALOGO) {
                        PantallaCatalogo(navController)
                    }
                    composable(
                        route = Rutas.DETALLE_PRODUCTO,
                        arguments = listOf(navArgument("productoId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
                        PantallaDetalleProducto(productoId, navController)
                    }
                    composable(
                        route = Rutas.RESENAS,
                        arguments = listOf(navArgument("productoId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
                        PantallaReseñas(productoId, navController)
                    }
                    composable(Rutas.EVENTOS) {
                        PantallaEventos(navController)
                    }
                    composable(
                        route = Rutas.EVENTOS_LISTA,
                        arguments = listOf(navArgument("ciudad") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val ciudadCodificada = backStackEntry.arguments?.getString("ciudad") ?: ""
                        // Decodificar la ciudad para obtener el nombre original
                        val ciudad = try {
                            java.net.URLDecoder.decode(ciudadCodificada, "UTF-8")
                        } catch (e: Exception) {
                            ciudadCodificada
                        }
                        PantallaEventosLista(ciudad, navController)
                    }
                    composable(Rutas.PERFIL) {
                        PantallaPerfil(navController)
                    }
                    composable(Rutas.REFERIDOS) {
                        PantallaReferidos(navController)
                    }
                    composable(Rutas.NIVEL) {
                        PantallaNivel(navController)
                    }
                    composable(Rutas.RECOMENDACIONES) {
                        PantallaRecomendaciones(navController)
                    }
                    composable(Rutas.CONFIGURACION) {
                        PantallaConfiguracion(navController)
                    }
                    composable(Rutas.CARRITO) {
                        PantallaCarrito(navController)
                    }
                    composable(Rutas.LOGIN) {
                        PantallaLogin(navController)
                    }
                    composable(Rutas.REGISTRO) {
                        PantallaRegistro(navController)
                    }
                    // Pantallas adicionales mencionadas en el drawer
                    composable(Rutas.SOPORTE) {
                        val context = LocalContext.current
                        Scaffold { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    "Soporte Técnico",
                                    style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        val intent = android.content.Intent(
                                            android.content.Intent.ACTION_VIEW,
                                            android.net.Uri.parse("https://wa.me/56912345678")
                                        )
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Abrir WhatsApp")
                                }
                            }
                        }
                    }
                    composable(Rutas.FAQ) {
                        Scaffold { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    "Preguntas Frecuentes",
                                    style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "\uD83D\uDD10 Mi Cuenta\n" +
                                            "\n" +
                                            "Olvidé mi contraseña: En la pantalla de inicio, toca \"¿Olvidaste tu contraseña?\". Te enviaremos un correo para crear una nueva.\n" +
                                            "\n" +
                                            "Quiero cambiar mis datos: Ve a tu Perfil y toca el botón de \"Editar\".\n" +
                                            "\n" +
                                            "¿Cómo borro mi cuenta?: Entra a Ajustes > Privacidad y elige \"Eliminar cuenta\".\n" +
                                            "\n" +
                                            "\uD83D\uDCF1 La App\n" +
                                            "\n" +
                                            "La app no funciona bien: Asegúrate de tener la última actualización instalada desde la tienda.\n" +
                                            "\n" +
                                            "No me llegan las notificaciones: Revisa la configuración de tu teléfono y asegúrate de dar permiso a la app.\n" +
                                            "\n" +
                                            "¿La app es gratis?: La descarga es gratis. Algunas funciones especiales requieren suscripción.\n" +
                                            "\n" +
                                            "\uD83C\uDD98 Ayuda\n" +
                                            "\n" +
                                            "Tengo otra duda: Escríbenos a hola@tuapp.com y te responderemos pron",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                    composable(Rutas.CONTACTO) {
                        Scaffold { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    "Contacto",
                                    style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Email: contacto@levelup.cl\nTeléfono: +56 9 1234 5678",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                    composable(Rutas.ORIGEN) {
                        Scaffold { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    "Origen de Productos",
                                    style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "\uD83D\uDCE6 Origen, Calidad y Autenticidad\n" +
                                            "¿De dónde provienen los productos que venden? Contamos con una red global de distribuidores autorizados y fabricantes directos. Dependiendo de la marca y la categoría, tu producto puede provenir de nuestros almacenes locales o ser importado directamente desde las sedes principales de cada fabricante para garantizarte el mejor precio del mercado.\n" +
                                            "\n" +
                                            "¿Los productos son 100% originales? Absolutamente. En nuestra app tenemos una política de tolerancia cero con las falsificaciones. Todos los artículos que vendemos son nuevos, originales y se envían en su embalaje oficial. Mantenemos relaciones directas con las marcas para asegurar la trazabilidad de cada artículo.\n" +
                                            "\n" +
                                            "¿Qué tipo de control de calidad realizan? Antes de ser empaquetado para el envío, cada producto pasa por una inspección visual en nuestro centro de distribución. Verificamos que el sellado de fábrica esté intacto, que no existan daños estéticos en la caja y que el producto corresponda exactamente a lo que pediste.\n" +
                                            "\n" +
                                            "¿Los productos incluyen garantía y manuales? Sí. Al ser productos originales, todos incluyen la documentación oficial del fabricante (manuales de usuario, certificados de autenticidad y tarjetas de garantía). Esto te permite reclamar soporte técnico oficial en caso de defectos de fabricación.\n" +
                                            "\n" +
                                            "¿Por qué algunos productos dicen \"Versión Internacional\"? A veces importamos productos que aún no se han lanzado oficialmente en tu país o que tienen especificaciones globales. Estos productos son idénticos en calidad, pero pueden incluir cargadores con adaptadores o manuales en varios idiomas. Siempre indicaremos esto claramente en la descripción. ",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}