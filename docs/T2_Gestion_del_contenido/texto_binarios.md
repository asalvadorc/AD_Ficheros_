# 🔹 Ficheros de texto y binarios

En el desarrollo de software, los ficheros de texto y los ficheros binarios son los dos tipos de archivos más comunes y utilizados para almacenar y gestionar información.

- **Ficheros de texto**: contienen únicamente caracteres. Su contenido se puede leer y escribir con cualquier editor de texto, como .txt, .csv, .json, .xml, etc.

- **Ficheros binarios**: son ficheros que contienen cualquier tipo de información (texto, imágenes, vídeos, ficheros…) codificada como bytes. En general, requiere de programas especiales para mostrar la información que contienen.


🖥️ **Ejemplo_Lect_esc_ficheroTexto.kt**: lectura y escritura en ficheros de texto (UTF-8)


Este ejemplo muestra el ciclo completo de trabajo con un fichero de texto usando la API `Files` de Java NIO desde Kotlin.

- Primero se define una ruta con `Paths.get("documentos/texto.txt")`.
- Después se escriben varias líneas en el archivo con `Files.write(...)`, usando codificación UTF-8 para que los caracteres especiales se guarden correctamente.
- A continuación se presentan tres formas de leer el mismo fichero:
        - `readAllLines()`: carga todas las líneas en una lista.
        - `readString()`: recupera todo el contenido como una sola cadena.
        - `newBufferedReader()`: permite leer el archivo de forma secuencial, útil cuando el fichero es grande.

La idea del ejercicio es comparar estas tres formas de lectura y entender cuándo conviene usar cada una.

```kotlin
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.StandardCharsets

fun main() {
    val ruta = Paths.get("documentos/texto.txt") // (1)!

    //Escritura en fichero de texto
    val lineasParaGuardar = listOf(
        "Primera línea",
        "Segunda línea",
        "¡Hola desde Kotlin!"
    )
    Files.write(ruta, lineasParaGuardar, StandardCharsets.UTF_8) // (2)!
    println("Fichero de texto escrito.")

    //Lectura del fichero de texto
    val lineasLeidas = Files.readAllLines(ruta) // (3)!
    println("Contenido leído con readAllLines:")
    for (lineas in lineasLeidas) {
        println(lineas)
    }

    val contenido = Files.readString(ruta) // (4)!
    println("Contenido leído con readString:")
    println(contenido)

    Files.newBufferedReader(ruta).use { reader -> // (5)!
        println("Contenido leído con newBufferedReader:")
        reader.lineSequence().forEach { println(it) }
    }
}
```

1. Crea la ruta del fichero de texto.
2. Escribe lineas con `UTF_8`.
3. Lee el archivo completo como lista.
4. Lee el archivo completo como bloque de texto.
5. Abre lectura secuencial con `BufferedReader`.

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

**🧩 Lectura y escritura de un archivo binario**{.azul} 

| Tipo de fichero           | Lectura                             | Escritura                            | Comentario                                               |
|---------------------------|--------------------------------------|---------------------------------------|----------------------------------------------------------|
| Binario | `Files.readAllBytes(Path)`          | `Files.write(Path, ByteArray)`        | Lee y escribe bytes puros                               |
|                           | `Files.newInputStream(Path)`        | `Files.newOutputStream(Path)`         | Flujo de bytes directo                                  |


🖥️ **Ejemplo_Lect_esc_ficheroBinario.kt**: lectura y escritura en ficheros binario


Este ejemplo muestra cómo guardar y recuperar datos binarios simples usando `Files` de Java NIO desde Kotlin.

- Primero se define una ruta con `Paths.get("documentos/datos.bin")`.
- Después se crea un `ByteArray` con varios valores numéricos, que representan el contenido binario que se quiere almacenar.
- Con `Files.write(...)` se escriben directamente esos bytes en el archivo.
- Finalmente, con `Files.readAllBytes(...)` se recupera el contenido completo y se recorre byte a byte para mostrarlo por pantalla.

La finalidad del ejercicio es entender que un fichero binario no almacena texto legible, sino datos en bruto, y que por eso su lectura y escritura se realiza en forma de bytes.

```kotlin
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
        val ruta = Paths.get("documentos/datos.bin") // (1)!

        val datos = byteArrayOf(1, 2, 3, 4, 5)
        Files.write(ruta, datos) // (2)!
        println("Archivo binario creado: ${ruta.toAbsolutePath()}")

        val bytes = Files.readAllBytes(ruta) // (3)!
        println("Contenido leído (byte a byte):")
        for (b in bytes) {
                print("$b ")
        }
}
```

1. Define la ruta del fichero binario.
2. Escribe el `ByteArray` completo en disco.
3. Recupera todos los bytes del fichero con `readAllBytes()`.

!!!note "📤 Salida esperada"
        Archivo binario creado: .../documentos/datos.bin
        Contenido leído (byte a byte):
        1 2 3 4 5 

---

!!!question "🧠 Comprueba tu comprensión"
    1. Si quieres leer un archivo de texto muy grande (por ejemplo, de 500 MB), ¿usarías `Files.readAllLines()` o `Files.newBufferedReader()`?
    2. ¿Qué ocurre si intentas abrir un archivo binario (como `.bin` o `.png`) con el Bloc de notas u otro editor de texto simple?

    ??? success "Ver respuestas"
        1. Usaría `Files.newBufferedReader()`. `readAllLines` carga todas las líneas en la memoria RAM de golpe y podría causar un error (*Out Of Memory*). El *BufferedReader* lee de forma secuencial y eficiente, línea a línea.
        2. Verás símbolos extraños y caracteres ilegibles (como ``). Esto se debe a que el Bloc de notas intenta interpretar los bytes (0s y 1s) como si fueran caracteres (ej. UTF-8), en lugar de datos crudos.


