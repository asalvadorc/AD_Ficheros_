# 🔑 Soluciones — Examen versión papel (USO EXCLUSIVO DEL PROFESOR)

!!!caution "Documento reservado"
    Contiene las respuestas completas de la versión papel del examen.

---

## 🅰️ Bloque 1 — Teoría

### Pregunta 1

**a)** `Files.newBufferedReader(ruta)` — Lee el fichero de forma secuencial usando un buffer, sin cargar todo en memoria. `readAllLines()` cargaría 800 MB en una lista y causaría `OutOfMemoryError`.

**b)** `kotlinx.serialization` con `@Serializable` y `Json.encodeToString()` — Librería oficial de JetBrains, ligera, sin necesidad de frameworks adicionales.

---

### Pregunta 2

**Error:** El directorio `informes/` no existe. `Files.write()` lanza `NoSuchFileException` porque no puede crear ficheros en directorios inexistentes.

**Corrección:** Añadir antes de `Files.write()`:
```kotlin
Files.createDirectories(ruta.parent)
```

---

### Pregunta 3

| # | Respuesta |
|---|---|
| 1 | **B** — `kotlinx.serialization` (multiplataforma) |
| 2 | **A** — `Files.newBufferedReader()` línea a línea (fichero grande) |
| 3 | **D** — CSV con OpenCSV o Kotlin-CSV (compatible con Excel) |
| 4 | **C** — Jackson con `XmlMapper` (Spring Boot) |

---

### Pregunta 4

| Afirmación | Respuesta | Corrección si es falsa |
|---|---|---|
| **a)** `readAllBytes()` es la mejor opción para cualquier tamaño | **F** | Solo es adecuado para ficheros pequeños. Para ficheros grandes provoca `OutOfMemoryError`. Usar `BufferedReader` o `InputStream`. |
| **b)** `@Serializable` es obligatorio en `kotlinx.serialization` | **V** | — |
| **c)** `FileChannel` permite posicionarse en cualquier byte | **V** | — |

---

### Pregunta 5 — Conversión de formatos

**Proceso conceptual:**  
No existe una conversión directa "de fichero a fichero". El proceso es:
1. **Leer** el fichero XML y **deserializarlo** convirtiéndolo a objetos en memoria.
2. **Serializar** esos objetos a formato JSON y **escribirlos** en el fichero de destino.

**Herramientas recomendadas:**  
- Para leer el XML y escribir el JSON: **Jackson** (con `XmlMapper` y `ObjectMapper`) o **kotlinx.serialization** (con el módulo XML de formato experimental).

---

## 🅱️ Bloque 2 — Solución de los huecos

### Función 1 — `leerProductosCSV`

```kotlin
fun leerProductosCSV(rutaCsv: String): List<Producto> {
    return try {

        val filas = csvReader { delimiter = ';' }      // ← delimiter
                        .readAllWithHeader( File(rutaCsv) )  // ← readAllWithHeader

        filas.mapNotNull { fila ->
            val nombre    = fila[ "nombre" ]            // ← "nombre"
            val categoria = fila["categoria"]
            val precio    = fila["precio"]?.toDoubleOrNull()
            val stock     = fila["stock"]?.toIntOrNull()

            if (nombre != null && categoria != null
                && precio != null && stock != null)
                Producto(nombre, categoria, precio, stock)
            else null
        }

    } catch (e: IOException) {                          // ← IOException
        println("Error al leer el CSV: ${e.message}")
        listOf()
    } catch (e: Exception) {
        println("Fichero no encontrado: ${e.message}")
        listOf()
    }
}
```

| Hueco | Respuesta correcta | Puntos |
|---|---|---|
| `__________ = ';'` | `delimiter` | 0,25 |
| `.__________( File(...) )` | `readAllWithHeader` | 0,25 |
| `fila[ __________ ]` | `"nombre"` | 0,25 |
| `catch (e: __________)` | `IOException` | 0,5 |
| **Subtotal** | | **1,25** |

> ⚠️ Se asigna 0,75 puntos adicionales si la función **en conjunto** es coherente y devuelve el tipo correcto.

---

### Función 2 — `guardarComoJSON`

```kotlin
fun guardarComoJSON(productos: List<Producto>, rutaJson: String) {
    val ruta = Paths.get(rutaJson)
    try {
        Files.createDirectories(ruta.parent)           // ← createDirectories / parent

        val contenido = Json { prettyPrint = true }    // ← prettyPrint
                            .encodeToString(productos)

        Files.writeString(ruta, contenido)             // ← writeString

        println("JSON guardado en: ${ruta.toAbsolutePath()}")

    } catch (e: IOException) {                         // ← ya estaba
        println("Error de E/S: ${e.message}")
    }
}
```

| Hueco | Respuesta correcta | Puntos |
|---|---|---|
| `Files.__________( ruta.__________)` | `createDirectories` / `parent` | 0,5 |
| `Json { __________ = true }` | `prettyPrint` | 0,5 |
| `Files.__________(ruta, contenido)` | `writeString` | 0,5 |
| Explicación de `createDirectories` | Crea el directorio destino si no existe, evitando `NoSuchFileException` | 0,5 |
| **Subtotal** | | **2** |

---

### Función 3 — `filtrarSinStock`

```kotlin
fun filtrarSinStock(productos: List<Producto>): List<Producto> {
    val sinStock = mutableListOf<Producto>()

    for (producto in productos) {
        if (producto.stock == 0) {                     // ← stock == 0
            sinStock.add(producto)                     // ← add
            println("⚠️ Sin stock: ${producto.nombre}" +
                    " → ${producto.stock} unidades")
        }
    }

    return sinStock                                    // ← sinStock
}
```

| Hueco | Respuesta correcta | Puntos |
|---|---|---|
| `producto.__________ __ 0` | `stock == 0` | 0,25 |
| `sinStock.__________(producto)` | `add` | 0,25 |
| `return __________` | `sinStock` | 0,5 |
| **Subtotal** | | **1** |

---

### Función 4 — `resumenPorCategoria`

```kotlin
fun resumenPorCategoria(productos: List<Producto>) {

    val grupos = mutableMapOf<String, MutableList<Producto>>()  // ← mutableMapOf

    for (producto in productos) {
        if (grupos.containsKey(producto.categoria)) {            // ← containsKey
            grupos[producto.categoria]!!.add(producto)
        } else {
            grupos[producto.categoria] = mutableListOf(producto)
        }
    }

    for ((categoria, lista) in grupos) {
        var suma = 0.0
        for (p in lista) { suma += p.precio }                    // ← +=
        val media = suma / lista.size                            // ← / size
        println("📦 $categoria → ${lista.size} productos" +
                " | Precio medio: ${"%.2f".format(media)} €")
    }
}
```

| Hueco | Respuesta correcta | Puntos |
|---|---|---|
| `__________<String, MutableList<Producto>>()` | `mutableMapOf` | 0,25 |
| `grupos.__________(producto.categoria)` | `containsKey` | 0,25 |
| `suma __________ p.precio` | `+=` | 0,25 |
| `suma __ lista.__________` | `/ size` | 0,25 |
| **Subtotal** | | **1** |

---

## 📊 Tabla de calificación global

| Bloque | Peso | Puntuación máxima |
|---|---|---|
| Bloque 1 — Teoría | 40% | 4 puntos |
| Bloque 2 — Práctica (huecos) | 60% | 6 puntos |
| **Total** | **100%** | **10 puntos** |

!!!tip "Criterio de corrección"
    En el bloque práctico se valorará la **lógica y comprensión**. Un alumno que escribe `equals(0)` en lugar de `== 0` no debe ser penalizado si el razonamiento es correcto. Sí se penaliza si el hueco demuestra que no entiende el concepto (ej. `containsKey` sustituido por algo sin sentido).
