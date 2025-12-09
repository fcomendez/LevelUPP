package com.example.template_app_comp.data.fake

import com.example.template_app_comp.data.model.*

object DatosFake {
    val productos = listOf(
        // Juegos de Mesa
        Producto("JM001", "Catan", "Juego de estrategia y negociación para 3-4 jugadores", 29990.0, "Juegos de Mesa"),
        Producto("JM002", "Carcassonne", "Juego de construcción de ciudades medievales", 24990.0, "Juegos de Mesa"),
        
        // Accesorios
        Producto("AC001", "Controlador Xbox Series X", "Control inalámbrico para Xbox Series X/S", 59990.0, "Accesorios"),
        Producto("AC002", "Auriculares HyperX Cloud II", "Auriculares gaming con sonido envolvente 7.1", 79990.0, "Accesorios"),
        
        // Consolas
        Producto("CO001", "PlayStation 5", "Consola de videojuegos de última generación", 549990.0, "Consolas"),
        
        // Computadores Gamers
        Producto("PC001", "PC Gamer ASUS ROG Strix", "PC Gamer de alto rendimiento con RTX 4060", 1299990.0, "Computadores Gamers"),
        
        // Sillas Gamers
        Producto("SG001", "Secretlab Titan", "Silla gaming ergonómica con soporte lumbar", 349990.0, "Sillas Gamers"),
        
        // Mouse
        Producto("MS001", "Logitech G502 Hero", "Mouse gaming con sensor HERO de alta precisión", 49990.0, "Mouse"),
        
        // Mousepad
        Producto("MP001", "Razer Goliathus Extended Chroma", "Mousepad extendido RGB para gaming", 29990.0, "Mousepad"),
        
        // Poleras
        Producto("PL001", "Polera \"Level-Up\"", "Polera personalizada con diseño gamer", 14990.0, "Poleras Personalizadas"),
        Producto("PL002", "Polerón Gamer Elite", "Polerón con capucha y diseño gaming", 34990.0, "Polerones Gamers")
    )

    // Direcciones aleatorias por ciudad
    private val direccionesSantiago = listOf(
        "Av. Providencia 1234, Providencia" to Pair(-33.4489, -70.6693),
        "Av. Las Condes 5678, Las Condes" to Pair(-33.4167, -70.5833),
        "Av. Libertador Bernardo O'Higgins 123, Santiago Centro" to Pair(-33.4489, -70.6483),
        "Av. Vitacura 2345, Vitacura" to Pair(-33.4000, -70.5667),
        "Av. Apoquindo 3456, Las Condes" to Pair(-33.4167, -70.6000)
    )
    
    private val direccionesValparaiso = listOf(
        "Av. Argentina 123, Valparaíso" to Pair(-33.0472, -71.6127),
        "Plaza Sotomayor 45, Valparaíso" to Pair(-33.0458, -71.6197),
        "Av. Alemania 567, Valparaíso" to Pair(-33.0500, -71.6000),
        "Cerro Concepción, Valparaíso" to Pair(-33.0431, -71.6292)
    )
    
    private val direccionesConcepcion = listOf(
        "Av. O'Higgins 1234, Concepción" to Pair(-36.8201, -73.0444),
        "Plaza de la Independencia, Concepción" to Pair(-36.8269, -73.0503),
        "Av. Arturo Prat 567, Concepción" to Pair(-36.8200, -73.0400)
    )
    
    private val direccionesVina = listOf(
        "Av. Valparaíso 123, Viña del Mar" to Pair(-33.0246, -71.5518),
        "Av. Libertad 456, Viña del Mar" to Pair(-33.0246, -71.5518),
        "Plaza Vergara, Viña del Mar" to Pair(-33.0246, -71.5518)
    )
    
    private fun obtenerDireccionAleatoria(ciudad: String): Pair<String, Pair<Double, Double>> {
        val direcciones = when (ciudad) {
            "Santiago" -> direccionesSantiago
            "Valparaiso" -> direccionesValparaiso
            "Concepcion" -> direccionesConcepcion
            "Vina del Mar" -> direccionesVina
            else -> direccionesSantiago
        }
        return direcciones.random()
    }

    val eventosPorCiudad = mapOf(
        "Santiago" to listOf(
            run {
                val (direccion, coords) = obtenerDireccionAleatoria("Santiago")
                Evento("E001", "Santiago", "2024-12-15 18:00", "Torneo de Catan", "Torneo oficial de Catan en centro de eventos", "placeholder_evento", direccion, coords.first, coords.second)
            },
            run {
                val (direccion, coords) = obtenerDireccionAleatoria("Santiago")
                Evento("E002", "Santiago", "2024-12-20 19:00", "LAN Party Gaming", "Evento de gaming con PCs y consolas", "placeholder_evento", direccion, coords.first, coords.second)
            },
            run {
                val (direccion, coords) = obtenerDireccionAleatoria("Santiago")
                Evento("E003", "Santiago", "2024-12-25 17:00", "Expo Tecnologia", "Exposicion de productos gaming y tecnologia", "placeholder_evento", direccion, coords.first, coords.second)
            }
        ),
        "Valparaiso" to listOf(
            run {
                val (direccion, coords) = obtenerDireccionAleatoria("Valparaiso")
                Evento("E004", "Valparaiso", "2024-12-18 18:30", "Torneo de Videojuegos", "Competencia de videojuegos locales", "placeholder_evento", direccion, coords.first, coords.second)
            },
            run {
                val (direccion, coords) = obtenerDireccionAleatoria("Valparaiso")
                Evento("E005", "Valparaiso", "2024-12-22 20:00", "Meetup Gamers", "Encuentro de gamers de la region", "placeholder_evento", direccion, coords.first, coords.second)
            }
        ),
        "Concepcion" to listOf(
            run {
                val (direccion, coords) = obtenerDireccionAleatoria("Concepcion")
                Evento("E006", "Concepcion", "2024-12-16 19:00", "Torneo E-Sports", "Competencia profesional de e-sports", "placeholder_evento", direccion, coords.first, coords.second)
            },
            run {
                val (direccion, coords) = obtenerDireccionAleatoria("Concepcion")
                Evento("E007", "Concepcion", "2024-12-21 18:00", "Expo Gaming Sur", "Exposicion de productos gaming", "placeholder_evento", direccion, coords.first, coords.second)
            }
        ),
        "Vina del Mar" to listOf(
            run {
                val (direccion, coords) = obtenerDireccionAleatoria("Vina del Mar")
                Evento("E008", "Vina del Mar", "2024-12-19 19:00", "Torneo Gaming Costero", "Competencia de gaming en la costa", "placeholder_evento", direccion, coords.first, coords.second)
            },
            run {
                val (direccion, coords) = obtenerDireccionAleatoria("Vina del Mar")
                Evento("E009", "Vina del Mar", "2024-12-23 18:00", "Meetup Tech", "Encuentro de tecnologia y gaming", "placeholder_evento", direccion, coords.first, coords.second)
            }
        )
    )

    val ciudades = listOf("Santiago", "Valparaiso", "Concepcion", "Vina del Mar")

    val usuarioInicial = Usuario(
        id = "1",
        nombre = "juanito",
        email = "juanito@duoc.cl",
        avatar = "placeholder_usuario",
        nivel = 1,
        puntos = 0
    )

    val resenas = mapOf(
        "JM001" to listOf(
            Resena("R001", "JM001", "Juan Pérez", 5, "Excelente juego, muy entretenido", "2024-12-01"),
            Resena("R002", "JM001", "María González", 4, "Muy bueno, pero complejo al inicio", "2024-12-05")
        ),
        "AC001" to listOf(
            Resena("R003", "AC001", "Carlos López", 5, "Control de excelente calidad", "2024-12-03")
        ),
        "CO001" to listOf(
            Resena("R004", "CO001", "Ana Silva", 5, "Consola increíble, gráficos espectaculares", "2024-12-02"),
            Resena("R005", "CO001", "Pedro Martínez", 4, "Muy buena, pero cara", "2024-12-04")
        ),
        "PC001" to listOf(
            Resena("R006", "PC001", "Luis Rodríguez", 5, "PC potente, corre todo sin problemas", "2024-12-06")
        ),
        "SG001" to listOf(
            Resena("R007", "SG001", "Fernanda Torres", 5, "Muy cómoda, perfecta para largas sesiones", "2024-12-07")
        ),
        "MS001" to listOf(
            Resena("R008", "MS001", "Roberto Vargas", 4, "Mouse preciso y confiable", "2024-12-08")
        )
    )

    val niveles = listOf(
        Nivel(1, 0, 99, "Novato - Comienza tu viaje"),
        Nivel(2, 100, 199, "Gamer - Estás mejorando"),
        Nivel(3, 200, 299, "Experto - Dominas el juego"),
        Nivel(4, 300, 399, "Master - Eres un profesional"),
        Nivel(5, 400, Int.MAX_VALUE, "Legendario - Has alcanzado la cima")
    )
}


