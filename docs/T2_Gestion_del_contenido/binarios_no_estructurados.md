---
hide:
  - toc
---

# 🔹 Binarios no estructurados

Un fichero binario no estructurado es cualquier fichero binario cuyo contenido no está organizado en registros definidos por nuestro programa. En la mayoría de los casos, simplemente lo tratamos como una secuencia de bytes, sin necesidad de interpretar su formato interno (ficheros de imagen, audio, vídeo, zip, ejecutable...).

---
**readAllBytes**{.azul}

| Método | Tipo de acceso | Uso recomendado |
|---------|----------------|-----------------|
| `Files.readAllBytes()` | Carga el fichero completo en memoria | Ficheros pequeños o medianos. Muy útil para encriptación, cálculo de hashes o manipulación sencilla de imágenes, PDF o ZIP. |    

🖥️ **Ejemplo_Lect_esc_ficheroBinario.kt**: lectura y escritura en ficheros binario


Este ejemplo muestra cómo guardar y recuperar datos binarios simples usando `Files` de Java NIO desde Kotlin.

- Primero se define una ruta con `Paths.get("documentos/datos.bin")`.
- Después se crea un `ByteArray` con varios valores numéricos, que representan el contenido binario que se quiere almacenar.
- Con `Files.write(...)` se escriben directamente esos bytes en el archivo.
- Finalmente, con `Files.readAllBytes(...)` se recupera el contenido completo y se recorre byte a byte para mostrarlo por pantalla.

La finalidad del ejercicio es entender que un fichero binario no almacena texto legible, sino datos en bruto, y que por eso su lectura y escritura se realiza en forma de bytes.


    import java.nio.file.Files
    import java.nio.file.Paths

    fun main() {
        val ruta = Paths.get("documentos/datos.bin")

        // Escritura de bytes puros
        val datos = byteArrayOf(1, 2, 3, 4, 5)
        Files.write(ruta, datos)
        println("Archivo binario creado: ${ruta.toAbsolutePath()}")

        // Lectura completa de bytes
        val bytes = Files.readAllBytes(ruta)
        println("Contenido leido (byte a byte):")
        for (b in bytes) {
            print("$b ")
        }
    }


!!!note "📤 Salida esperada"
        Archivo binario creado: .../documentos/datos.bin
        Contenido leído (byte a byte):
        1 2 3 4 5 
---

**BufferedInputStream/BufferedOutputStream**{.azul}

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

- Primero se define una ruta con `Paths.get("documentos/datos.bin")`.
- Después se crea un `ByteArray` con varios valores numéricos que representan los datos binarios que se desean almacenar.
- A continuación se abre un `BufferedOutputStream` asociado al archivo mediante `Files.newOutputStream(...)` y se escriben todos los bytes utilizando el método `write(...)`.
- Una vez creado el fichero, se abre un `BufferedInputStream` mediante `Files.newInputStream(...)`.
- Mediante llamadas sucesivas a `read()` se recupera un byte cada vez hasta que el método devuelve `-1`, indicando que se ha alcanzado el final del fichero.
- Finalmente, cada byte leído se muestra por pantalla.

La finalidad del ejercicio es comprender cómo `BufferedOutputStream` y `BufferedInputStream` permiten escribir y leer datos binarios de forma secuencial utilizando un búfer interno. Este búfer reduce el número de accesos al dispositivo de almacenamiento, mejorando el rendimiento, especialmente cuando se trabaja con ficheros de gran tamaño.


       import java.io.BufferedInputStream
        import java.io.BufferedOutputStream
        import java.nio.file.Files
        import java.nio.file.Paths

        fun main() {
            val ruta = Paths.get("documentos/datos.bin")

            // Escritura de bytes puros
            val datos = byteArrayOf(1, 2, 3, 4, 5)

            BufferedOutputStream(Files.newOutputStream(ruta)).use { salida ->
                salida.write(datos)
            }

            println("Archivo binario creado: ${ruta.toAbsolutePath()}")

            // Lectura secuencial de bytes
            BufferedInputStream(Files.newInputStream(ruta)).use { entrada ->
                println("Contenido leído (byte a byte):")

                var byte = entrada.read()
                while (byte != -1) {
                    print("$byte ")
                    byte = entrada.read()
                }
            }
        }


!!!question "🧠 Comprueba tu comprensión"
    ¿Qué ocurre si intentas abrir un archivo binario (como `.bin` o `.png`) con el Bloc de notas u otro editor de texto simple?

    ??? success "Ver respuesta"
        Verás símbolos extraños y caracteres ilegibles (como ``). Esto se debe a que el Bloc de notas intenta interpretar los bytes (0s y 1s) como si fueran caracteres (ej. UTF-8), en lugar de datos crudos.        




