---
hide:
  - toc
---

# 🔹 Lectura y escritura de texto (Flujos de caracteres)

Los ficheros de texto almacenan caracteres y se usan para datos legibles por humanos: `.txt`, `.csv`, `.json`, `.xml`, etc.

En Kotlin, trabajaremos con la API `java.nio.file.Files` para leer y escribir texto de forma segura y eficiente.

???+ info "Metodos principales para texto"

    | Necesidad | Lectura | Escritura | Observaciones |
    |-----------|---------|-----------|---------------|
    | Archivo pequeno/mediano completo | `Files.readAllLines(path)` | `Files.write(path, lineas, UTF_8)` | Carga todas las lineas en memoria |
    | Archivo completo como bloque | `Files.readString(path)` | `Files.writeString(path, texto, UTF_8)` | Muy comodo para contenido corto |
    | Archivo grande o lectura progresiva | `Files.newBufferedReader(path, UTF_8)` | `Files.newBufferedWriter(path, UTF_8)` | Recomendado para no saturar memoria |

!!!Note ""
    La idea del ejercicio siguiente es comparar estas tres formas de lectura y entender cuándo conviene usar cada una.

🖥️ **Ejemplo_Lect_esc_ficheroTexto.kt**: lectura y escritura en ficheros de texto (UTF-8)


Este ejemplo muestra el ciclo completo de trabajo con un fichero de texto usando la API `Files` de Java NIO desde Kotlin.

A continuación se presentan tres formas de leer el mismo fichero:  
 
- `readAllLines()`: carga todas las líneas en una lista.
- `readString()`: recupera todo el contenido como una sola cadena.
- `newBufferedReader()`: permite leer el archivo de forma secuencial, útil cuando el fichero es grande.


---

```kotlin
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/texto.txt") // (1)!

    // Escritura en fichero de texto
    val lineasParaGuardar = listOf(
        "Primera linea",
        "Segunda linea",
        "Hola desde Kotlin"
    )
    Files.write(ruta, lineasParaGuardar, StandardCharsets.UTF_8) // (2)!
    println("Fichero de texto escrito.")

    // 1) readAllLines: devuelve lista de lineas
    val lineasLeidas = Files.readAllLines(ruta, StandardCharsets.UTF_8) // (3)!
    println("Contenido leido con readAllLines:")
    for (linea in lineasLeidas) {
        println(linea)
    }

    // 2) readString: devuelve todo el texto en un String
    val contenido = Files.readString(ruta, StandardCharsets.UTF_8) // (4)!
    println("Contenido leido con readString:")
    println(contenido)

    // 3) newBufferedReader: lectura secuencial (util en archivos grandes)
    Files.newBufferedReader(ruta, StandardCharsets.UTF_8).use { reader -> // (5)!
        println("Contenido leido con newBufferedReader:")
        reader.lineSequence().forEach { println(it) }
    }
}
```

1. Crea el `Path` del fichero de texto.
2. Escribe las lineas en disco con codificacion `UTF_8`.
3. Lee todo el fichero como lista de lineas.
4. Lee todo el fichero como un unico `String`.
5. Abre un lector secuencial (`BufferedReader`) para lectura progresiva.


!!!note "📤 Salida esperada"
        Fichero de texto escrito.
        Contenido leído con readAllLines:
        Primera línea
        Segunda línea
        ¡Hola desde Kotlin!
        Contenido leído con readString:
        Primera línea
        Segunda línea
        ¡Hola desde Kotlin!
        Contenido leído con newBufferedReader:
        Primera línea
        Segunda línea
        ¡Hola desde Kotlin!

**Buenas practicas**{.azul}

- Usa siempre codificacion explicita (`UTF_8`) para evitar problemas de tildes y simbolos.
- Para archivos muy grandes, evita `readAllLines` y prefiere `newBufferedReader`.
- Usa `use { ... }` en lectores/escritores para cerrar recursos automaticamente.



!!!question "Comprueba tu comprension"
	Si quieres leer un archivo de texto muy grande (por ejemplo, de 500 MB), ¿usarías `Files.readAllLines()` o `Files.newBufferedReader()`?
    ??? success "Ver respuesta"
        Usaría `Files.newBufferedReader()`. `readAllLines` carga todas las líneas en la memoria RAM de golpe y podría causar un error (*Out Of Memory*). El *BufferedReader* lee de forma secuencial y eficiente, línea a línea.

