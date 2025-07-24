# Ficheros de texto y binarios

En cualquier aplicación real, tarde o temprano, necesitaremos guardar información de forma permanente. Una de las formas más comunes y sencillas de hacerlo es a través de ficheros.

Un fichero es una unidad de almacenamiento que permite guardar datos en el disco, ya sea para leerlos más tarde o para compartirlos con otros programas. Puede contener texto, imágenes, configuraciones, datos binarios, etc.

- **Ficheros de texto**: contienen únicamente caracteres. Su contenido se puede leer y escribir con cualquier editor de texto.

- **Ficheros binarios**: son ficheros que contienen cualquier tipo de información (texto, imágenes, vídeos, ficheros…) codificada como bytes. En general, requiere de programas especiales para mostrar la información que contienen.



Los siguientes ejemplos utilizan las [Clases y métodos](http://127.0.0.1:8000/AD_T2_Gesti_del_contingut_de_fitxers/Introduccion/#clases-y-metodos-para-la-lectura-y-escritura) apropiadas para estos tipos de archivos.

**Lectura y escritura de un archivo de texto**{.azul}


| Tipo de fichero           | Lectura                             | Escritura                            | Comentario                                               |
|---------------------------|--------------------------------------|---------------------------------------|----------------------------------------------------------|
| Texto (líneas)         | `Files.readAllLines(Path)`          | `Files.write(Path, List<String>)`     | Carga todo en memoria                                    |
|                           | `Files.newBufferedReader(Path)`     | `Files.newBufferedWriter(Path)`       | Más eficiente para archivos grandes                      |
|                           | `Files.readString(Path)` (Java 11+) | `Files.writeString(Path, String)`     | Lectura/escritura completa como bloque                  |

       
🖥️ **Ejemplo_Lect_esc_ficheroTexto.kt**: lectura y escritura en ficheros de texto (UTF-8)

        import java.nio.file.Files
        import java.nio.file.Paths
        import java.nio.charset.StandardCharsets

        fun main() {

                //Escritura en fichero de texto

                //writeString
                val texto = "Hola, mundo desde Kotlin"
                Files.writeString(Paths.get("documentos/saludo.txt"), texto)


                //write
                val ruta = Paths.get("documentos/texto.txt")

                
                val lineasParaGuardar = listOf(
                        "Primera línea",
                        "Segunda línea",
                        "¡Hola desde Kotlin!"
                )
                Files.write(ruta, lineasParaGuardar, StandardCharsets.UTF_8)
                println("Fichero de texto escrito.")

                //newBuffered
                Files.newBufferedWriter(Paths.get("documentos/log.txt")).use { writer ->
                writer.write("Log iniciado...\n")
                writer.write("Proceso completado.\n")

                //Lectura del fichero de texto

                //readAllLines
                val lineasLeidas = Files.readAllLines(ruta)
                println("Contenido leído con readAllLines:")
                for (lineas in lineasLeidas) {
                        println(lineas)
                }

                //readString
                val contenido = Files.readString(ruta)
                println("Contenido leído con readString:")
                println(contenido)
                
                //newBufferedReader
                Files.newBufferedReader(ruta).use { reader ->
                        println("Contenido leído con newBufferedReader:")
                        reader.lineSequence().forEach { println(it) }
                }
        }

**Lectura y escritura de un archivo binario**{.azul}

| Tipo de fichero           | Lectura                             | Escritura                            | Comentario                                               |
|---------------------------|--------------------------------------|---------------------------------------|----------------------------------------------------------|
| Binario | `Files.readAllBytes(Path)`          | `Files.write(Path, ByteArray)`        | Lee y escribe bytes puros                               |
|                           | `Files.newInputStream(Path)`        | `Files.newOutputStream(Path)`         | Flujo de bytes directo                                  |



🖥️ **Ejemplo_Lect_esc_ficheroBinario.kt**: lectura y escritura en ficheros binario


        import java.nio.file.Files
        import java.nio.file.Paths


        fun main() {
                val ruta = Paths.get("documentos/datos.bin")

                //Escritura en fichero binario
                val datos = byteArrayOf(1, 2, 3, 4, 5)
                Files.write(ruta, datos)
                println("Archivo binario creado: ${ruta.toAbsolutePath()}")


                val bytes = Files.readAllBytes(ruta)

                println("Contenido leído (byte a byte):")
                for (b in bytes) {
                        print("$b ")
                }
         }