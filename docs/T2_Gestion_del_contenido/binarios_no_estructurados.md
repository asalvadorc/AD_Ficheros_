# 🔹 Binarios no estructurados

!!! warning "Dónde guardar los ejemplos"
    Programa los ejemplos en el proyecto **`Ficheros`**, dentro del paquete **`contenido`**. Los archivos binarios se leerán y generarán en `documentos`, en la raíz del proyecto.

    Consulta [🧰 Entorno y ubicación de los ejemplos](../00_entorno_y_proyecto.md) si necesitas revisar la estructura completa.

Un fichero binario no estructurado es cualquier fichero binario cuyo contenido no está organizado en registros definidos por nuestro programa. En la mayoría de los casos, simplemente lo tratamos como una secuencia de bytes, sin necesidad de interpretar su formato interno (ficheros de imagen, audio, vídeo, zip, ejecutable...).

---
## 🔹 readAllBytes

| Método | Tipo de acceso | Uso recomendado |
|---------|----------------|-----------------|
| `Files.readAllBytes()` | Carga el fichero completo en memoria | Ficheros pequeños o medianos. Muy útil para encriptación, cálculo de hashes o manipulación sencilla de imágenes, PDF o ZIP. |    

🖥️ **Ejemplo_Lect_esc_ficheroBinario.kt**: lectura y escritura en ficheros binario


Este ejemplo muestra cómo guardar y recuperar datos binarios simples usando `Files` de Java NIO desde Kotlin.

La finalidad del ejercicio es entender que un fichero binario no almacena texto legible, sino datos en bruto, y que por eso su lectura y escritura se realiza en forma de bytes.

```kotlin
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/datos.bin") // (1)!

    val datos = byteArrayOf(1, 2, 3, 4, 5)// (2)!
    Files.write(ruta, datos) // (3)!
    println("Archivo binario creado: ${ruta.toAbsolutePath()}")

    val bytes = Files.readAllBytes(ruta) // (4)!
    println("Contenido leido (byte a byte):")
    for (b in bytes) {
        print("$b ")
    }
}
```

1. Define la ruta del fichero binario.
2. se crea un `ByteArray` con varios valores numéricos, que representan el contenido binario que se quiere almacenar.
3. Escribe todos los bytes del array en disco.
4. Lee el contenido completo del binario en memoria.


!!!note "📤 Salida esperada"
        Archivo binario creado: .../documentos/datos.bin
        Contenido leído (byte a byte):
        1 2 3 4 5 
---

## 🔹 BufferedStream

??? info "Clases y métodos de BufferedInputStream y BufferedOutputStream"

   
    | Clase | Método | Descripción |
    |--------|--------|-------------|
    | `BufferedInputStream` | `read()` | Lee el siguiente byte del flujo y devuelve su valor, o `-1` si se ha alcanzado el final del fichero. |
    | `BufferedInputStream` | `read(byte[] b)` | Lee varios bytes y los almacena en el array indicado. Devuelve el número de bytes leídos. |
    | `BufferedInputStream` | `close()` | Cierra el flujo y libera los recursos asociados. |
    | `BufferedOutputStream` | `write(int b)` | Escribe un único byte en el búfer. |
    | `BufferedOutputStream` | `write(byte[] b)` | Escribe el contenido completo del array de bytes en el búfer. |
    | `BufferedOutputStream` | `flush()` | Fuerza la escritura del contenido del búfer al dispositivo de almacenamiento. |
    | `BufferedOutputStream` | `close()` | Vacía el búfer pendiente, cierra el flujo y libera los recursos asociados. |      

🖥️ **Ejemplo_Lect_esc_ficheroBinario_buf.kt**: lectura y escritura en ficheros binario por flujo

Este ejemplo muestra cómo escribir y leer un fichero binario utilizando `BufferedOutputStream` y `BufferedInputStream` desde Kotlin.

La finalidad del ejercicio es comprender cómo `BufferedOutputStream` y `BufferedInputStream` permiten escribir y leer datos binarios de forma secuencial utilizando un búfer interno. Este búfer reduce el número de accesos al dispositivo de almacenamiento, mejorando el rendimiento, especialmente cuando se trabaja con ficheros de gran tamaño.

```kotlin
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/datos.bin") // (1)!
    val datos = byteArrayOf(1, 2, 3, 4, 5)

    BufferedOutputStream(Files.newOutputStream(ruta)).use { salida -> // (2)!
        salida.write(datos) // (3)!
    }

    println("Archivo binario creado: ${ruta.toAbsolutePath()}")

    BufferedInputStream(Files.newInputStream(ruta)).use { entrada -> // (4)!
        println("Contenido leído (byte a byte):")

        var byte = entrada.read() // (5)!
        while (byte != -1) { // (6)!
            print("$byte ")
            byte = entrada.read() // (7)!
        }
    }
}
```

1. Define la ruta del binario.
2. Abre un flujo de salida con buffer sobre NIO.
3. Escribe el `ByteArray` en el flujo.
4. Abre un flujo de entrada con buffer.
5. Lee el primer byte del flujo.
6. Repite hasta detectar fin de fichero (`-1`).
7. Lee el siguiente byte en cada iteracion.


!!!question "🧠 Comprueba tu comprensión"
    ¿Qué ocurre si intentas abrir un archivo binario (como `.bin` o `.png`) con el Bloc de notas u otro editor de texto simple?

    ??? success "Ver respuesta"
        Verás símbolos extraños y caracteres ilegibles (como ``). Esto se debe a que el Bloc de notas intenta interpretar los bytes (0s y 1s) como si fueran caracteres (ej. UTF-8), en lugar de datos crudos.        




