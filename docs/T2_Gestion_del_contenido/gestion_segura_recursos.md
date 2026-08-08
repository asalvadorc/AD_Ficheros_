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

        try {
            //  Código que puede fallar
        } catch (e1: IOException) {
            // Código para manejar el error
        } catch (e2: Exception) {
            // Código para manejar el error
        } finally {
            // opcional. Finalización y liberación de recursos
        }


**Ejemplo**

        fun main() {
        try {
            val resultado = 10 / 0
            println("Resultado: $resultado")
        } catch (e: ArithmeticException) {
            println("Error: no se puede dividir entre cero.")
        } finally {
            println("Fin del bloque try-catch.")
        }
    }

**2. use → Gestión automática de recursos**{.azul}

Se utiliza con objetos que implementan la interfaz Closeable o AutoCloseable (como archivos, streams, lectores, sockets...). Es el equivalente de **try-with-resources** en Java.

Con **use**, Kotlin abre, utiliza y cierra automáticamente el recurso (como un archivo), incluso si ocurre una excepción.


        recurso.use {
            // Aquí usas el recurso
        }

    - recurso es un objeto como BufferedReader, FileWriter, InputStream, etc.
    
    
 **Ejemplo 1:** Leer archivo con BufferedReader

        import java.io.BufferedReader
        import java.io.FileReader

        fun main() {
            BufferedReader(FileReader("archivo.txt")).use { reader ->
                val linea = reader.readLine()
                println(linea)
            }
        }

    - Cuando el bloque use { ... } termina, el reader se cierra automáticamente.

**Ejemplo 2:** Escribir archivo con FileWriter

        import java.io.FileWriter

        fun main() {
            FileWriter("archivo.txt").use {
                it.write("Hola, Kotlin\n")
            }
        }

Internamente, **use** hace lo mismo que esto:

            val writer = FileWriter("archivo.txt")
            try {
                writer.write("Texto")
            } finally {
                writer.close()
            }

**Ejemplo 3:** Ejemplo con BufferedReader. **Use** se encarga de cerrar el BufferedReader automáticamente, aunque haya un error.

    import java.io.File

    fun main() {
        val archivo = File("datos.txt")

        try {
            archivo.bufferedReader().use { reader ->
                println("Primera línea: ${reader.readLine()}")
            }
        } catch (e: Exception) {
            println("Error al leer el archivo: ${e.message}")
        }
    }


**Recomendaciones didácticas**{.azul}

- Capturar excepciones específicas siempre que sea posible (por ejemplo, `IOException`).
- Evitar `catch (Exception)` como única estrategia; usarlo solo como respaldo.
- Mostrar mensajes de error claros para facilitar depuración y mantenimiento.
- Validar rutas y existencia de ficheros antes de operar cuando sea necesario.

