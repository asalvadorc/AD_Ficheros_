# Ficheros de texto y binarios

En cualquier aplicación real, tarde o temprano, necesitaremos guardar información de forma permanente. Una de las formas más comunes y sencillas de hacerlo es a través de ficheros.

Un fichero es una unidad de almacenamiento que permite guardar datos en el disco, ya sea para leerlos más tarde o para compartirlos con otros programas. Puede contener texto, imágenes, configuraciones, datos binarios, etc.

- **Ficheros de texto**: contienen únicamente caracteres. Su contenido se puede leer y escribir con cualquier editor de texto.

- **Ficheros binarios**: son ficheros que contienen cualquier tipo de información (texto, imágenes, vídeos, ficheros…) codificada como bytes. En general, requiere de programas especiales para mostrar la información que contienen.


<!--
**Métodos de lectura y escritura de archivos con Files (Java NIO)**{.azul}

| Tipo | Operación   | Método                           | Descripción breve                                              | Uso recomendado                              |
|---------------------|-------------|----------------------------------|----------------------------------------------------------------|-----------------------------------------------|
| Texto               | Lectura     | `readString(path)`               | Lee todo el archivo como un único `String`                     | Archivos pequeños de texto plano              |
|                   |      | `readAllLines(path)`             | Devuelve una `List<String>` con todas las líneas               | Procesar líneas en memoria                    |
|                |      | `newBufferedReader(path)`        | Devuelve un lector eficiente para línea a línea (`Reader`)     | Lectura eficiente de archivos grandes         |
|                | Escritura   | `writeString(path, text)`        | Escribe un `String` completo en el archivo                     | Escritura simple de texto completo            |
|                |    | `write(path, List<String>)`      | Escribe varias líneas desde una lista                          | Escritura estructurada en líneas              |
|                |    | `newBufferedWriter(path)`        | Devuelve un escritor (`Writer`) con buffer                     | Escritura eficiente línea a línea             |
| Binario             | Lectura     | `readAllBytes(path)`             | Lee todo el contenido del archivo como un `ByteArray`          | Lectura de imágenes o archivos binarios       |
|              | Escritura   | `write(path, ByteArray)`         | Escribe un array de bytes en el archivo                        | Guardar datos binarios (imagen, bin, etc.)    |

-->


Los siguientes ejemplos utilizan las [Clases y métodos](http://127.0.0.1:8000/AD_T2_Gesti_del_contingut_de_fitxers/Introduccion/#clases-y-metodos-para-la-lectura-y-escritura) apropiadas para estos tipos de archivos.

**Lectura y escritura de un archivo de texto**{.azul}

       
**Ejemplo_Lect_esc_ficheroTexto.kt**: lectura y escritura en ficheros de texto (UTF-8)

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

**Ejemplo_Lect_esc_ficheroBinario.kt**: lectura y escritura en ficheros binario


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