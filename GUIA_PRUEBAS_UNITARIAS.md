GUÍA DE PRUEBAS UNITARIAS - LevelUp App


Las pruebas están en: `app/src/test/java/com/example/template_app_comp/viewmodel/`


PRUEBAS IMPLEMENTADAS

1. CarritoViewModelTest.kt

Prueba 1: Calcular subtotal
**Línea 46-66**


@Test
fun `calcular subtotal correctamente`() = runTest {
    // Arrange
    val producto1 = Producto("1", "Producto 1", "Desc", 100.0, "Categoria")
    val producto2 = Producto("2", "Producto 2", "Desc", 200.0, "Categoria")
    val items = listOf(
        ItemCarrito("1", 2),
        ItemCarrito("2", 1)
    )

    whenever(repositorioCarrito.getCarrito()).thenReturn(flowOf(items))
    whenever(repositorioProductos.getProductoPorId("1")).thenReturn(flowOf(producto1))
    whenever(repositorioProductos.getProductoPorId("2")).thenReturn(flowOf(producto2))

    // Act
    val subtotal = viewModel.subtotal.first()

    // Assert
    assertEquals(400.0, subtotal, 0.01)
}


¿Qué hace?
- Prepara: Producto 1 cuesta $100 (2 unidades), Producto 2 cuesta $200 (1 unidad)
- Ejecuta: Calcula el subtotal del carrito
- Verifica: El subtotal debe ser $400 (100×2 + 200×1)



Prueba 2: Calcular descuento DUOC
Línea 68-90


@Test
fun `calcular descuento DUOC correctamente`() = runTest {
    // Arrange
    val items = listOf(ItemCarrito("1", 1))
    val producto = Producto("1", "Producto", "Desc", 100.0, "Categoria")

    whenever(repositorioCarrito.getCarrito()).thenReturn(flowOf(items))
    whenever(repositorioProductos.getProductoPorId("1")).thenReturn(flowOf(producto))
    whenever(repositorioUsuario.getUsuario()).thenReturn(
        flowOf(Usuario("1", "Test", "test@duoc.cl", "", 1, 0))
    )

    // Act
    val descuento = viewModel.descuento.first()

    // Assert
    assertEquals(20.0, descuento, 0.01)
}


Qué hace
- Prepara: Usuario con email @duoc.cl compra producto de $100
- Ejecuta: Calcula el descuento
- Verifica: El descuento debe ser $20 (20% de $100)


Prueba 3: Calcular total sin descuento
Línea 92-113


@Test
fun `calcular total sin descuento`() = runTest {
    // Arrange
    val items = listOf(ItemCarrito("1", 1))
    val producto = Producto("1", "Producto", "Desc", 100.0, "Categoria")

    whenever(repositorioCarrito.getCarrito()).thenReturn(flowOf(items))
    whenever(repositorioProductos.getProductoPorId("1")).thenReturn(flowOf(producto))
    whenever(repositorioUsuario.getUsuario()).thenReturn(
        flowOf(Usuario("1", "Test", "test@gmail.com", "", 1, 0))
    )

    // Act
    val total = viewModel.total.first()

    // Assert
    assertEquals(100.0, total, 0.01)
}


¿Qué hace?
- Prepara: Usuario sin email DUOC compra producto de $100
- Ejecuta: Calcula el total
- Verifica: El total debe ser $100 (sin descuento)


2. LoginViewModelTest.kt

rueba 1: Validar email vacío
Línea 29-37


@Test
fun `validar email vacio retorna false`() {
    // Act
    val resultado = viewModel.validarEmail("")

    // Assert
    assertFalse(resultado)
    assertEquals("El email es requerido", viewModel.emailError.value)
}


- Ejecuta: Valida un email vacio
- Verifica: Debe retornar `false` y mostrar error "El email es requerido"


prueba 2: Validar email invalido
lnea 39-47


@Test
fun `validar email invalido retorna false`() {
    // Act
    val resultado = viewModel.validarEmail("emailinvalido")

    // Assert
    assertFalse(resultado)
    assertEquals("El formato del email no es válido", viewModel.emailError.value)
}


¿Qué hace?
- Ejecuta: Valida email sin @ (formato inválido)
- Verifica: Debe retornar `false` y mostrar error de formato


prueba 3: Validar email válido
Línea 49-57


@Test
fun `validar email valido retorna true`() {
    // Act
    val resultado = viewModel.validarEmail("test@example.com")

    // Assert
    assertTrue(resultado)
    assertNull(viewModel.emailError.value)
}
```

¿Qué hace?
- Ejecuta: Valida un email con formato correcto
- Verifica: Debe retornar `true` y no mostrar error



Prueba 4: Validar password vacía
Línea 59-67


@Test
fun `validar password vacia retorna false`() {
    // Act
    val resultado = viewModel.validarPassword("")

    // Assert
    assertFalse(resultado)
    assertEquals("La contraseña es requerida", viewModel.passwordError.value)
}



- Ejecuta: Valida una contraseña vacía
- Verifica: Debe retornar `false` y mostrar error



prueba 5: Validar password corta
linea 69-77


@Test
fun `validar password corta retorna false`() {
    // Act
    val resultado = viewModel.validarPassword("12345")

    // Assert
    assertFalse(resultado)
    assertEquals("La contraseña debe tener al menos 6 caracteres", viewModel.passwordError.value)
}


¿Qué hace?
- Ejecuta: Valida password con solo 5 caracteres
- Verifica: Debe retornar `false` porque necesita mínimo 6 caracteres



prueba 6: Validar password válida
Línea 79-87


@Test
fun `validar password valida retorna true`() {
    // Act
    val resultado = viewModel.validarPassword("password123")

    // Assert
    assertTrue(resultado)
    assertNull(viewModel.passwordError.value)
}


Qué hace?
- Ejecuta: Valida password con 6+ caracteres
- Verifica: Debe retornar `true` y no mostrar error



Prueba 7: Login con usuario existente
Línea 89-109


@Test
fun `login con usuario existente es exitoso`() = runTest {
    // Arrange
    val email = "test@duoc.cl"
    val password = "password123"
    val usuario = Usuario("1", "Test", email, "", 1, 0)

    whenever(repositorioUsuario.getUsuario()).thenReturn(flowOf(usuario))

    var resultadoExitoso = false
    // Act
    viewModel.login(email, password) { exito ->
        resultadoExitoso = exito
    }

    // Assert
    assertTrue(resultadoExitoso)
}

¿Qué hace?
- Prepara: Usuario existente en el sistema
- Ejecuta: Intenta hacer login
- Verifica: El login debe ser exitoso



3. RegistroViewModelTest.kt

Prueba 1: Validar nombre vacío
linea 32-40


@Test
fun `validar nombre vacio retorna false`() {
    // Act
    val resultado = viewModel.validarNombre("")

    // Assert
    assertFalse(resultado)
    assertEquals("El nombre es requerido", viewModel.nombreError.value)
}


¿Qué hace?
- Ejecuta: Valida nombre vacío
- Verifica: Debe retornar `false` y mostrar error

Prueba 2: Validar nombre corto
Línea 42-50


@Test
fun `validar nombre corto retorna false`() {
    // Act
    val resultado = viewModel.validarNombre("Ab")

    // Assert
    assertFalse(resultado)
    assertEquals("El nombre debe tener al menos 3 caracteres", viewModel.nombreError.value)
}


**¿Qué hace?**
- Ejecuta: Valida nombre con solo 2 letras
- Verifica: Debe retornar `false` porque necesita mínimo 3 caracteres



Prueba 3: Validar nombre con números
Línea 52-60


@Test
fun `validar nombre con numeros retorna false`() {
    // Act
    val resultado = viewModel.validarNombre("Juan123")

    // Assert
    assertFalse(resultado)
    assertEquals("El nombre solo puede contener letras", viewModel.nombreError.value)
}


¿Qué hace?
- Ejecuta: Valida nombre que contiene números
- Verifica: Debe retornar `false` porque solo acepta letras



Prueba 4: Validar nombre válido
Línea 62-70


@Test
fun `validar nombre valido retorna true`() {
    // Act
    val resultado = viewModel.validarNombre("Juan Pérez")

    // Assert
    assertTrue(resultado)
    assertNull(viewModel.nombreError.value)
}


Qué hace?
- Ejecuta: Valida nombre con letras y espacios
- Verifica: Debe retornar `true` y no mostrar error

Prueba 5: Validar email DUOC activa descuento
linea 72-79

@Test
fun `validar email duoc activa descuento`() {
    // Act
    viewModel.validarEmail("test@duoc.cl")

    // Assert
    assertTrue(viewModel.tieneDescuentoDuoc.value)
}

¿Qué hace?
- Ejecuta: Valida email que termina en @duoc.cl
- Verifica: Debe activar el descuento DUOC



Prueba 6: Calcular edad
Línea 81-89


@Test
fun `calcular edad correctamente`() {
    // Act
    val edad = viewModel.calcularEdad("01/01/2000")

    // Assert
    assertNotNull(edad)
    assertTrue(edad!! >= 18)
}


¿Qué hace?
- Ejecuta: Calcula la edad desde fecha de nacimiento
- Verifica: La edad debe ser mayor o igual a 18 años


Prueba 7: Validar fecha formato inválido
linea 91-99

@Test
fun `validar fecha formato invalido retorna false`() {
    // Act
    val resultado = viewModel.validarFechaNacimiento("01-01-2000")

    // Assert
    assertFalse(resultado)
    assertEquals("El formato debe ser DD/MM/AAAA", viewModel.fechaNacimientoError.value)
}


Qué hace?
- Ejecuta: Valida fecha con formato incorrecto (guiones en vez de barras)
- Verifica: Debe retornar `false` y mostrar error de formato



Prueba 8: Validar password sin mayúscula
Línea 101-109


@Test
fun `validar password sin mayuscula retorna false`() {
    // Act
    val resultado = viewModel.validarPassword("password123")

    // Assert
    assertFalse(resultado)
    assertEquals("La contraseña debe contener al menos una mayúscula", viewModel.passwordError.value)
}


¿Qué hace?
- Ejecuta: Valida password sin letras mayúsculas
- Verifica: Debe retornar `false` porque necesita mayúscula

Prueba 9: Validar password sin número
Línea 111-119


@Test
fun `validar password sin numero retorna false`() {
    // Act
    val resultado = viewModel.validarPassword("Password")

    // Assert
    assertFalse(resultado)
    assertEquals("La contraseña debe contener al menos un número", viewModel.passwordError.value)
}

Qué hace?
- Ejecuta: Valida password sin números
- Verifica: Debe retornar `false` porque necesita número



prueba 10: alidar password válida
Línea 121-129


@Test
fun `validar password valida retorna true`() {
    // Act
    val resultado = viewModel.validarPassword("Password123")

    // Assert
    assertTrue(resultado)
    assertNull(viewModel.passwordError.value)
}


¿Qué hace?
- Ejecuta: Valida password con mayúscula y número
- Verifica: Debe retornar `true` y no mostrar error


prueba 11: Validar confirmación password no coincide
Línea 131-139


@Test
fun `validar confirmacion password no coincide retorna false`() {
    // Act
    val resultado = viewModel.validarConfirmPassword("Password123", "Password456")

    // Assert
    assertFalse(resultado)
    assertEquals("Las contraseñas no coinciden", viewModel.confirmPasswordError.value)
}


Qué hace?
- Ejecuta: Valida que las dos contraseñas sean iguales
- Verifica: Debe retornar `false` porque no coinciden


 Cómo ejecutar las pruebas

1. Abre Android Studio
2. Click derecho en `app/src/test/`
3. Selecciona "Run 'Tests in 'test''"


Cada prueba tiene 3 partes:
1. **Preparar** (Arrange): Configurar datos de prueba
2. **Ejecutar** (Act): Llamar a la función que quieres probar
3. **Verificar** (Assert): Comprobar que el resultado es correcto
