# 📝 Examen: Acceso a Datos — Gestión de Ficheros en Kotlin

!!!warning "Instrucciones generales"
    - Duración: **90 minutos**.
    - El examen consta de **dos bloques**: teoría (40%) y práctica (60%).
    - En el bloque práctico se proporciona un fichero `.kt` con el esqueleto del código.  
      📥 **[Descargar esqueleto del examen (Examen_Esqueleto.kt)](../recursos/Examen_Esqueleto.kt)**
    - Se permite consultar la **[📋 Chuleta de métodos](chuleta.md)** durante todo el examen.
    - No se permite copiar código de los apuntes ni de Internet.
    - Se valorará la **gestión de excepciones** y la **claridad del código**.

---

## 🅰️ Bloque 1 — Teoría y comprensión (4 puntos · 40%)

### Pregunta 1 — Elección razonada (1 punto)

Para cada uno de los dos casos siguientes, indica **qué clase o librería usarías** y **justifica brevemente tu respuesta** (1-2 líneas por caso):

**a)** Necesitas leer un fichero de texto de **800 MB** línea a línea sin que el programa se quede sin memoria.

**b)** Tu aplicación Kotlin necesita guardar una lista de objetos en un fichero **JSON** y luego recuperarla. El proyecto es exclusivamente Kotlin y no usa Spring Boot.

---

### Pregunta 2 — Detecta el error (1 punto)

El siguiente código compila pero **falla en ejecución**. Identifica el/los errores y escribe la versión corregida:

```kotlin
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("informes/resumen.txt")

    val lineas = listOf("Línea 1", "Línea 2", "Línea 3")
    Files.write(ruta, lineas)

    val contenido = Files.readString(ruta)
    println(contenido)
}
```

!!!question "Pista"
    Piensa en qué debe existir antes de poder escribir en un fichero.

---

### Pregunta 3 — Relaciona (1 punto)

Conecta cada situación con la opción más adecuada. Escribe la letra de la opción junto al número de la situación:

**Situaciones:**

1. Guardar y recuperar objetos Kotlin en un proyecto multiplataforma (JVM + JS).
2. Procesar un fichero de registro (log) de **2 GB** buscando líneas con la palabra `"ERROR"`.
3. Exportar una lista de productos a un fichero que se abrirá en **Microsoft Excel**.
4. Persistir objetos complejos en **XML** dentro de un proyecto Spring Boot.

**Opciones:**

- A) `Files.newBufferedReader()` con lectura línea a línea.
- B) `kotlinx.serialization` con `Json.encodeToString()`.
- C) `Jackson` con `XmlMapper` (módulo `jackson-dataformat-xml`).
- D) Escribir un fichero `.csv` separado por `;` o con `OpenCSV`.

---

### Pregunta 4 — Verdadero / Falso razonado (0,5 puntos)

Indica si cada afirmación es **verdadera (V) o falsa (F)** y, si es falsa, corrige la afirmación en una línea:

**a)** `Files.readAllBytes()` es la mejor opción para leer cualquier fichero, independientemente de su tamaño.

**b)** Para usar `kotlinx.serialization`, es obligatorio añadir la anotación `@Serializable` a la clase que se quiere serializar.

**c)** `FileChannel` y `ByteBuffer` permiten posicionarse en cualquier byte de un fichero para lectura/escritura aleatoria.

---

### Pregunta 5 — Conversión de formatos (0,5 puntos)

Describe brevemente (2-3 líneas) cuál es el proceso conceptual correcto para convertir la información de un fichero **XML** a un fichero **JSON** en tu aplicación. ¿Qué pasos seguirías y qué librerías recomendarías usar?

---

## 🅱️ Bloque 2 — Práctica guiada (6 puntos · 60%)

### Contexto

Se te proporciona la siguiente `data class`:

```kotlin
@Serializable
data class Producto(
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int
)
```

Y un fichero `productos.csv` con el siguiente contenido:

```
nombre;categoria;precio;stock
Teclado;Periféricos;45.99;10
Monitor;Pantallas;299.00;3
Ratón;Periféricos;25.50;0
Auriculares;Audio;79.95;7
Webcam;Periféricos;89.00;0
SSD 1TB;Almacenamiento;110.00;15
```

Descarga el esqueleto y **completa las funciones marcadas con `// TODO`**. No debes modificar las firmas de las funciones.

📥 **[Descargar esqueleto del examen (Examen_Esqueleto.kt)](../recursos/Examen_Esqueleto.kt)**

---

### Funciones a implementar

**Función 1 — `leerProductosCSV` (2 puntos)**

Lee el fichero CSV usando **Kotlin-CSV** (`csvReader`) y devuelve una lista de objetos `Producto`.  
La primera fila es la cabecera y **debe ignorarse**.  
Si el fichero no existe, debe capturarse la excepción y devolver una lista vacía.

---

**Función 2 — `guardarComoJSON` (2 puntos)**

Recibe la lista de productos y la guarda en un fichero `productos.json` usando **`kotlinx.serialization`**.  
El JSON debe tener formato legible (`prettyPrint = true`).  
Si la carpeta de destino no existe, debe crearse automáticamente.  
Se deben capturar las posibles excepciones de I/O.

---

**Función 3 — `filtrarSinStock` (1 punto)**

Recibe la lista de productos y **devuelve solo los que tienen `stock == 0`**.

- Recorre la lista con un **bucle `for`**.
- Si el `stock` del producto es `0`, añádelo a una lista de resultados e **imprímelo por pantalla** con el formato:
```
⚠️ Sin stock: Ratón inalámbrico → 0 unidades
```
- Devuelve la lista de productos sin stock.

---

**Función 4 — `resumenPorCategoria` (1 punto)**

Recibe la lista de productos y muestra por pantalla cuántos productos hay de cada categoría y su precio medio.

- Usa un **`mutableMapOf<String, MutableList<Producto>>()`** para agrupar manualmente los productos por categoría.
- Recorre la lista con un **bucle `for`**: si la categoría ya existe en el mapa, añade el producto a su lista; si no, crea una lista nueva.
- Después, recorre el mapa e imprime:
```
📦 Periféricos   → 3 productos | Precio medio: 53.50 €
📦 Pantallas     → 1 productos | Precio medio: 299.00 €
```

!!!tip "Cálculo del precio medio"
    Suma los precios de todos los productos de la categoría y divídela entre el número de productos de esa categoría.

---

### Rúbrica de evaluación del bloque práctico

| Criterio | Puntos |
|---|---|
| `leerProductosCSV`: lectura correcta con Kotlin-CSV | 1 |
| `leerProductosCSV`: gestión de excepciones y cabecera ignorada | 1 |
| `guardarComoJSON`: serialización correcta con `kotlinx.serialization` | 1 |
| `guardarComoJSON`: creación de directorio + gestión de excepciones | 1 |
| `filtrarSinStock`: filtrado correcto + salida con el formato indicado | 1 |
| `resumenPorCategoria`: agrupación y cálculo del precio medio | 1 |
| **Total** | **6** |
