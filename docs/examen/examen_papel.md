# 📝 Examen (versión papel) — Acceso a Datos: Gestión de Ficheros en Kotlin

!!!warning "Instrucciones generales"
    - Duración: **90 minutos**.
    - El examen consta de **dos bloques**: teoría (40%) y práctica (60%).
    - Se permite consultar la **[📋 Chuleta de métodos](chuleta.md)**.
    - No se permite consultar los apuntes ni ningún otro recurso.
    - Escribe con letra clara. En el bloque práctico se valorará la **lógica**, no la perfección sintáctica.

---

## 🅰️ Bloque 1 — Teoría y comprensión (4 puntos · 40%)

### Pregunta 1 — Elección razonada (1,5 puntos)

Para cada caso, indica **qué clase o librería usarías** y **justifica brevemente** (1-2 líneas):

**a)** Necesitas leer un fichero de texto de **800 MB** línea a línea sin que el programa se quede sin memoria.

&nbsp;

&nbsp;

**b)** Tu proyecto es exclusivamente Kotlin y necesitas guardar una lista de objetos en un fichero **JSON**.

&nbsp;

&nbsp;

**c)** Recibes un **CSV** generado por Excel (con `;` como separador y cabecera en la primera fila) y debes convertir cada fila en un objeto `Alumno`.

&nbsp;

&nbsp;

---

### Pregunta 2 — Detecta el error (1 punto)

El siguiente código compila pero **falla en ejecución**. Rodea el error y escribe la corrección debajo:

```kotlin
fun main() {
    val ruta = Paths.get("informes/resumen.txt")
    val lineas = listOf("Línea 1", "Línea 2", "Línea 3")
    Files.write(ruta, lineas)
    println(Files.readString(ruta))
}
```

**Error detectado:**

&nbsp;

**Corrección:**

&nbsp;

---

### Pregunta 3 — Relaciona (1 punto)

Escribe la letra de la opción correcta junto a cada situación:

| # | Situación | Respuesta |
|---|---|---|
| 1 | Guardar objetos Kotlin en proyecto multiplataforma (JVM + JS) | |
| 2 | Procesar un log de 2 GB buscando líneas con `"ERROR"` | |
| 3 | Exportar datos para abrir en Microsoft Excel | |
| 4 | Persistir objetos en XML dentro de un proyecto Spring Boot | |

**Opciones:**

- **A)** `Files.newBufferedReader()` con lectura línea a línea
- **B)** `kotlinx.serialization` con `Json.encodeToString()`
- **C)** `Jackson` con `XmlMapper`
- **D)** Fichero `.csv` con OpenCSV o Kotlin-CSV

---

### Pregunta 4 — Verdadero / Falso razonado (0,5 puntos)

Indica **V** o **F** y corrige las falsas en una línea:

**a)** `Files.readAllBytes()` es la mejor opción para cualquier tamaño de fichero.

&nbsp;

**b)** Para usar `kotlinx.serialization` es obligatorio anotar la clase con `@Serializable`.

&nbsp;

**c)** `FileChannel` permite posicionarse en cualquier byte de un fichero para leer o escribir.

&nbsp;

---

## 🅱️ Bloque 2 — Práctica: completa el código (6 puntos · 60%)

### Contexto

Se trabaja con la siguiente clase y un fichero `productos.csv` con separador `;` y cabecera:

```kotlin
@Serializable
data class Producto(val nombre: String, val categoria: String,
                    val precio: Double, val stock: Int)
```

```
nombre;categoria;precio;stock
Teclado;Periféricos;45.99;10
Monitor;Pantallas;299.00;3
Ratón;Periféricos;25.50;0
```

Completa los huecos `__________` del código siguiente. Cada hueco vale lo indicado.

---

### Función 1 — `leerProductosCSV` (2 puntos)

```kotlin
fun leerProductosCSV(rutaCsv: String): List<Producto> {
    return try {

        val filas = csvReader { __________ = ';' }   // (0,25 p)
                        .__________( File(rutaCsv) )  // (0,25 p)

        filas.mapNotNull { fila ->
            val nombre    = fila[ __________ ]        // (0,25 p)
            val categoria = fila["categoria"]
            val precio    = fila["precio"]
                                ?.toDoubleOrNull()
            val stock     = fila["stock"]
                                ?.toIntOrNull()

            if (nombre != null && categoria != null
                && precio != null && stock != null)
                Producto(nombre, categoria, precio, stock)
            else
                null
        }

    } catch (e: __________ ) {                        // (0,5 p)
        println("Error al leer el CSV: ${e.message}")
        listOf()
    } catch (e: Exception) {
        println("Fichero no encontrado: ${e.message}")
        listOf()
    }
}
```

---

### Función 2 — `guardarComoJSON` (2 puntos)

```kotlin
fun guardarComoJSON(productos: List<Producto>, rutaJson: String) {
    val ruta = Paths.get(rutaJson)
    try {
        Files.__________(ruta.__________)              // (0,5 p)

        val contenido = Json { __________ = true }    // (0,5 p)
                            .encodeToString(productos)

        Files.__________(ruta, contenido)              // (0,5 p)

        println("JSON guardado en: ${ruta.toAbsolutePath()}")

    } catch (e: IOException) {
        println("Error de E/S: ${e.message}")          // (0,5 p)
    }
}
```

> ⬇️ Explica en una frase qué hace la línea `Files.__________(ruta.__________)`:

&nbsp;

&nbsp;

---

### Función 3 — `filtrarSinStock` (1 punto)

```kotlin
fun filtrarSinStock(productos: List<Producto>): List<Producto> {
    val sinStock = mutableListOf<Producto>()

    for (producto in productos) {
        if (producto.__________ __ 0) {               // (0,25 p)
            sinStock.__________(producto)              // (0,25 p)
            println("⚠️ Sin stock: ${producto.nombre}" +
                    " → ${producto.stock} unidades")
        }
    }

    return __________                                  // (0,5 p)
}
```

---

### Función 4 — `resumenPorCategoria` (1 punto)

```kotlin
fun resumenPorCategoria(productos: List<Producto>) {

    val grupos = __________<String, MutableList<Producto>>()  // (0,25 p)

    for (producto in productos) {
        if (grupos.__________(producto.categoria)) {           // (0,25 p)
            grupos[producto.categoria]!!.add(producto)
        } else {
            grupos[producto.categoria] = mutableListOf(producto)
        }
    }

    for ((categoria, lista) in grupos) {
        var suma = 0.0
        for (p in lista) { suma __________ p.precio }         // (0,25 p)
        val media = suma __ lista.__________                   // (0,25 p)
        println("📦 $categoria → ${lista.size} productos" +
                " | Precio medio: ${"%.2f".format(media)} €")
    }
}
```
