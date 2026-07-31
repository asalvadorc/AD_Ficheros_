# 🔑 Soluciones del Examen — USO EXCLUSIVO DEL PROFESOR

!!!caution "Documento reservado"
    Este documento contiene las **soluciones completas** del examen.  
    No debe ser accesible para los alumnos.

---

## 🅰️ Bloque 1 — Soluciones de teoría

### Pregunta 1 — Elección razonada

**a)** Para leer un fichero de 800 MB línea a línea sin agotar la memoria:

✅ **`Files.newBufferedReader(ruta)`** (o `Files.newInputStream` para binario).  
`readAllLines()` carga todo el fichero en una `List<String>` en memoria, lo que con 800 MB causaría un `OutOfMemoryError`. `BufferedReader` lee solo el fragmento necesario en cada momento (buffer).

**b)** Proyecto exclusivamente Kotlin sin Spring Boot:

✅ **`kotlinx.serialization`** con la anotación `@Serializable` y `Json.encodeToString()` / `Json.decodeFromString<T>()`.  
Es la librería oficial de JetBrains, ligera, sin dependencias adicionales de frameworks, y con soporte multiplataforma.

---

### Pregunta 2 — Detecta el error

**Error:** El directorio `informes/` no existe, por lo que `Files.write()` lanza `NoSuchFileException`.

**Código corregido:**

```kotlin
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("informes/resumen.txt")

    // ✅ Corrección: crear el directorio antes de escribir
    Files.createDirectories(ruta.parent)

    val lineas = listOf("Línea 1", "Línea 2", "Línea 3")
    Files.write(ruta, lineas)

    val contenido = Files.readString(ruta)
    println(contenido)
}
```

**Puntuación orientativa:**
- Identificar el error (`NoSuchFileException` / directorio inexistente): 0,5 puntos
- Escribir la corrección correcta con `Files.createDirectories(ruta.parent)`: 0,5 puntos

---

### Pregunta 3 — Relaciona

| Situación | Respuesta correcta |
|---|---|
| 1. Proyecto multiplataforma Kotlin (JVM + JS) | **B** — `kotlinx.serialization` |
| 2. Fichero de log de 2 GB buscando líneas | **A** — `Files.newBufferedReader()` línea a línea |
| 3. Exportar a Microsoft Excel | **D** — Fichero CSV con `;` / OpenCSV |
| 4. XML en Spring Boot | **C** — Jackson con `XmlMapper` |

---

### Pregunta 4 — Verdadero / Falso razonado

**a) FALSA.**  
`Files.readAllBytes()` carga el fichero completo en memoria. Para ficheros grandes puede causar `OutOfMemoryError`. Para ficheros de gran tamaño, es preferible usar flujos (`InputStream`, `BufferedReader`).

**b) VERDADERA.**  
`@Serializable` es obligatorio. Sin ella, `kotlinx.serialization` no puede generar el serializador en tiempo de compilación y lanzará un error.

**c) VERDADERA.**  
`FileChannel` permite posicionarse en cualquier byte | **V** | — |

---

### Pregunta 5 — Conversión de formatos

| Indicador | Puntos |
|---|---|
| Menciona el patrón: leer XML → convertir a objetos (en memoria) → serializar a JSON | 0,25 |
| Sugiere usar Jackson (XmlMapper y ObjectMapper) o kotlinx.serialization | 0,25 |

!!!tip "Nota de corrección"
    Lo esencial es que el alumno entienda que **no hay conversión mágica directa de texto a texto**, sino que se requiere parsear a objetos intermedios.

---

## 🅱️ Bloque 2 — Solución completa del bloque práctico

```kotlin
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.io.File

@Serializable
data class Producto(
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int
)

// ─────────────────────────────────────────────
// FUNCIÓN 1 — leerProductosCSV (2 puntos)
// ─────────────────────────────────────────────
fun leerProductosCSV(rutaCsv: String): List<Producto> {
    return try {
        // readAllWithHeader ignora automáticamente la primera fila (cabecera)
        val filas: List<Map<String, String>> = csvReader {
            delimiter = ';'
        }.readAllWithHeader(File(rutaCsv))

        filas.mapNotNull { fila ->
            val nombre    = fila["nombre"]    ?: return@mapNotNull null
            val categoria = fila["categoria"] ?: return@mapNotNull null
            val precio    = fila["precio"]?.replace(",", ".")?.toDoubleOrNull() ?: return@mapNotNull null
            val stock     = fila["stock"]?.toIntOrNull() ?: return@mapNotNull null
            Producto(nombre, categoria, precio, stock)
        }
    } catch (e: IOException) {
        println("⚠️ Error al leer el CSV: ${e.message}")
        listOf()
    } catch (e: Exception) {
        println("⚠️ El fichero no existe o tiene un formato incorrecto: ${e.message}")
        listOf()
    }
}

// ─────────────────────────────────────────────
// FUNCIÓN 2 — guardarComoJSON (2 puntos)
// ─────────────────────────────────────────────
fun guardarComoJSON(productos: List<Producto>, rutaJson: String) {
    val ruta = Paths.get(rutaJson)
    try {
        // Crear el directorio si no existe
        Files.createDirectories(ruta.parent)

        val json = Json { prettyPrint = true }
        val contenido = json.encodeToString(productos)
        Files.writeString(ruta, contenido)

        println("✅ JSON guardado en: ${ruta.toAbsolutePath()}")
    } catch (e: IOException) {
        println("⚠️ Error de E/S al guardar el JSON: ${e.message}")
    } catch (e: Exception) {
        println("⚠️ Error inesperado: ${e.message}")
    }
}

// ─────────────────────────────────────────────
// FUNCIÓN 3 — filtrarSinStock (1 punto)
// ─────────────────────────────────────────────
fun filtrarSinStock(productos: List<Producto>): List<Producto> {
    val sinStock = mutableListOf<Producto>()
    for (producto in productos) {
        if (producto.stock == 0) {
            sinStock.add(producto)
            println("⚠️ Sin stock: ${producto.nombre} → ${producto.stock} unidades")
        }
    }
    return sinStock
}

// ─────────────────────────────────────────────
// FUNCIÓN 4 — resumenPorCategoria (1 punto)
// ─────────────────────────────────────────────
fun resumenPorCategoria(productos: List<Producto>) {
    // Agrupación manual con mutableMapOf
    val grupos = mutableMapOf<String, MutableList<Producto>>()

    for (producto in productos) {
        if (grupos.containsKey(producto.categoria)) {
            grupos[producto.categoria]!!.add(producto)
        } else {
            grupos[producto.categoria] = mutableListOf(producto)
        }
    }

    // Cálculo e impresión del resumen
    for ((categoria, lista) in grupos) {
        var sumaPrecio = 0.0
        for (p in lista) {
            sumaPrecio += p.precio
        }
        val precioMedio = sumaPrecio / lista.size
        println("📦 %-20s → %d productos | Precio medio: %.2f €"
            .format(categoria, lista.size, precioMedio))
    }
}

// --- Main de prueba ---
fun main() {
    val rutaCSV  = "documentos/productos.csv"
    val rutaJSON = "documentos/productos.json"

    println("=== Leyendo CSV ===")
    val productos = leerProductosCSV(rutaCSV)
    println("Productos leídos: ${productos.size}")

    println("\n=== Guardando JSON ===")
    guardarComoJSON(productos, rutaJSON)

    println("\n=== Productos sin stock ===")
    val sinStock = filtrarSinStock(productos)
    println("Total sin stock: ${sinStock.size}")

    println("\n=== Resumen por categoría ===")
    resumenPorCategoria(productos)
}
```

---

## 📋 Criterios de corrección detallados

### Función 1 — leerProductosCSV

| Indicador | Puntos |
|---|---|
| Usa `csvReader` de Kotlin-CSV correctamente | 0,5 |
| Usa `readAllWithHeader` (o equivalente que ignore cabecera) | 0,5 |
| Convierte correctamente cada fila a `Producto` | 0,5 |
| Captura `IOException` u otras excepciones y devuelve `listOf()` | 0,5 |

### Función 2 — guardarComoJSON

| Indicador | Puntos |
|---|---|
| Usa `kotlinx.serialization` con `Json.encodeToString()` | 0,5 |
| Activa `prettyPrint = true` | 0,25 |
| Crea el directorio con `Files.createDirectories(ruta.parent)` | 0,5 |
| Captura excepciones de I/O | 0,25 |
| Escribe correctamente el fichero con `Files.writeString()` o equivalente | 0,5 |

### Función 3 — filtrarSinStock

| Indicador | Puntos |
|---|---|
| Recorre la lista con un bucle `for` y comprueba `stock == 0` | 0,5 |
| Añade el producto a la lista de resultados **e** imprime el formato exacto | 0,5 |

### Función 4 — resumenPorCategoria

| Indicador | Puntos |
|---|---|
| Usa `mutableMapOf` y un bucle `for` para agrupar correctamente por categoría | 0,5 |
| Calcula el precio medio con suma / número de elementos | 0,25 |
| Imprime el resultado con el formato reconocible indicado | 0,25 |

!!!tip "Nota de corrección"
    Se acepta cualquier solución que use estructuras básicas (`for`, `if`, `mutableListOf`, `mutableMapOf`) y obtenga el resultado correcto. **No** se espera el uso de funciones de la API de colecciones como `groupBy`, `filter` o `average`.

---

## 📊 Tabla de calificación global

| Bloque | Peso | Puntuación máxima |
|---|---|---|
| Bloque 1 — Teoría | 40% | 4 puntos |
| Bloque 2 — Práctica | 60% | 6 puntos |
| **Total** | **100%** | **10 puntos** |

!!!tip "Nota de corrección"
    Se acepta cualquier solución funcionalmente equivalente aunque use una sintaxis diferente (por ejemplo, usar `use {}` en lugar de bloques `try/catch` explícitos, o usar `OpenCSV` en lugar de `Kotlin-CSV` en la función 1, siempre que el resultado sea correcto).
