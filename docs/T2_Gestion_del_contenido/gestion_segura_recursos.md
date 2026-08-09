---
hide:
  - toc
---

# 🔹 Manejo de errores y recursos


Una **excepción** es un error que ocurre en tiempo de ejecución y que interrumpe el flujo normal del programa (por ejemplo: dividir por cero, leer un archivo que no existe, etc.).

La gestión de excepciones implica detectar, manejar y recuperarse de errores en el código.
Kotlin utiliza el mismo modelo de excepciones que Java, pero con algunas diferencias importantes. En Kotlin hay dos formas comunes de gestionar excepciones:

**1. try-catch → Captura y manejo explícito de errores**{.azul}

Se utiliza cuando queremos atrapar y manejar una excepción que puede producirse en un bloque de código.

**Sintaxis**
```kotlin
        try {
            //  Código que puede fallar
        } catch (e1: IOException) {
            // Código para manejar el error
        } catch (e2: Exception) {
            // Código para manejar el error
        } finally {
            // opcional. Finalización y liberación de recursos
        }
```


**Ejemplo**

```kotlin
fun main() {
    try { // (1)!
        val resultado = 10 / 0
        println("Resultado: $resultado")
    } catch (e: ArithmeticException) { // (2)!
        println("Error: no se puede dividir entre cero.")
    } finally { // (3)!
        println("Fin del bloque try-catch.")
    }
}
```

1. Delimita el bloque que puede lanzar una excepcion.
2. Captura una excepcion especifica (`ArithmeticException`).
3. Ejecuta el bloque de limpieza final (`finally`).

**2. use → Gestión automática de recursos**{.azul}

Se utiliza con objetos que implementan la interfaz Closeable o AutoCloseable (como archivos, streams, lectores, sockets...). Es el equivalente de **try-with-resources** en Java.

Con **use**, Kotlin abre, utiliza y cierra automáticamente el recurso (como un archivo), incluso si ocurre una excepción.


        recurso.use {
            // Aquí usas el recurso
        }

    - recurso es un objeto como BufferedReader, FileWriter, InputStream, etc.
    
    
 **Ejemplo 1:** Leer archivo con BufferedReader

```kotlin
import java.io.BufferedReader
import java.io.FileReader

fun main() {
    BufferedReader(FileReader("archivo.txt")).use { reader -> // (1)!
        val linea = reader.readLine() // (2)!
        println(linea)
    }
}
```

1. Abre y cierra automaticamente el recurso con `use`.
2. Lee una linea desde el `BufferedReader`.

    - Cuando el bloque use { ... } termina, el reader se cierra automáticamente.

**Ejemplo 2:** Escribir archivo con FileWriter

```kotlin
import java.io.FileWriter

fun main() {
    FileWriter("archivo.txt").use { // (1)!
        it.write("Hola, Kotlin\n") // (2)!
    }
}
```

1. Gestiona cierre automatico del `FileWriter` con `use`.
2. Escribe texto en el fichero.

Internamente, **use** hace lo mismo que esto:

```kotlin
val writer = FileWriter("archivo.txt") // (1)!
try { // (2)!
    writer.write("Texto") // (3)!
} finally { // (4)!
    writer.close() // (5)!
}
```

1. Abre el recurso manualmente.
2. Delimita bloque protegido frente a errores.
3. Realiza la escritura del contenido.
4. Garantiza la ejecucion del cierre.
5. Libera el recurso explicitamente.

**Ejemplo 3:** Ejemplo con BufferedReader. **Use** se encarga de cerrar el BufferedReader automáticamente, aunque haya un error.

```kotlin
import java.io.File

fun main() {
    val archivo = File("datos.txt") // (1)!

    try { // (2)!
        archivo.bufferedReader().use { reader -> // (3)!
            println("Primera línea: ${reader.readLine()}") // (4)!
        }
    } catch (e: Exception) { // (5)!
        println("Error al leer el archivo: ${e.message}")
    }
}
```

1. Define el fichero de entrada.
2. Encapsula la lectura en un bloque con manejo de errores.
3. Abre un `BufferedReader` con cierre automatico mediante `use`.
4. Ejecuta la lectura de una linea.
5. Captura errores de lectura.


**Recomendaciones didácticas**{.azul}

- Capturar excepciones específicas siempre que sea posible (por ejemplo, `IOException`).
- Evitar `catch (Exception)` como única estrategia; usarlo solo como respaldo.
- Mostrar mensajes de error claros para facilitar depuración y mantenimiento.
- Validar rutas y existencia de ficheros antes de operar cuando sea necesario.

