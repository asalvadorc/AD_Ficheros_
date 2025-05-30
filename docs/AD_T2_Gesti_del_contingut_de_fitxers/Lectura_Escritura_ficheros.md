# Ficheros de texto y binarios

En cualquier aplicación real, tarde o temprano, necesitaremos guardar información de forma permanente. Una de las formas más comunes y sencillas de hacerlo es a través de ficheros.

Un fichero es una unidad de almacenamiento que permite guardar datos en el disco, ya sea para leerlos más tarde o para compartirlos con otros programas. Puede contener texto, imágenes, configuraciones, datos binarios, etc.


!!!Note ""
    **Java.nio.file** no tiene constructores públicos. Fue introducido en Java 7 como parte de la nueva API de ficheros (NIO.2). Sigue un diseño más moderno, basado en:  

      - Interfaces: como Path, FileSystem.
      - Métodos estáticos utilitarios: como Paths.get(...), Files.readAllLines(...).
      - Patrón de factoría: no se crean objetos directamente con new, sino que se obtienen mediante métodos de creación controlada.


**Métodos de lectura y escritura de archivos con Files (Java NIO)**{.azul}

| Tipo de archivo     | Operación   | Método                           | Descripción breve                                              | Uso recomendado                              |
|---------------------|-------------|----------------------------------|----------------------------------------------------------------|-----------------------------------------------|
| Texto               | Lectura     | `readString(path)`               | Lee todo el archivo como un único `String`                     | Archivos pequeños de texto plano              |
|                   |      | `readAllLines(path)`             | Devuelve una `List<String>` con todas las líneas               | Procesar líneas en memoria                    |
|                |      | `newBufferedReader(path)`        | Devuelve un lector eficiente para línea a línea (`Reader`)     | Lectura eficiente de archivos grandes         |
|                | Escritura   | `writeString(path, text)`        | Escribe un `String` completo en el archivo                     | Escritura simple de texto completo            |
|                |    | `write(path, List<String>)`      | Escribe varias líneas desde una lista                          | Escritura estructurada en líneas              |
|                |    | `newBufferedWriter(path)`        | Devuelve un escritor (`Writer`) con buffer                     | Escritura eficiente línea a línea             |
| Binario             | Lectura     | `readAllBytes(path)`             | Lee todo el contenido del archivo como un `ByteArray`          | Lectura de imágenes o archivos binarios       |
|              | Escritura   | `write(path, ByteArray)`         | Escribe un array de bytes en el archivo                        | Guardar datos binarios (imagen, bin, etc.)    |




**Ejemplos de lectura y escritura de ficheros en Kotlin (java.nio.file)**{.azul}

---

## **Lectura de ficheros**

1. Leer todo el archivo como texto con **readString**

    
        import java.nio.file.Files
        import java.nio.file.Paths

        val contenido = Files.readString(Paths.get("documentos/archivo.txt"))
        println(contenido)

2. Leer todas las líneas como lista con **readAllLines**

        import java.nio.file.Files
        import java.nio.file.Paths

        val lineas = Files.readAllLines(Paths.get("archivo.txt"))
        lineas.forEach { println(it) }


3. Leer línea por línea con **newBufferedReader**

        import java.nio.file.Files
        import java.nio.file.Paths

        Files.newBufferedReader(Paths.get("archivo.txt")).use { reader ->
            reader.lineSequence().forEach { println(it) }
        }

4. Leer archivo binario como array de bytes con **readAllBytes**

        import java.nio.file.Files
        import java.nio.file.Paths

        val bytes = Files.readAllBytes(Paths.get("imagen.png"))
        println("Tamaño en bytes: ${bytes.size}")

## **Escritura de ficheros**

1. Escribir texto completo con **writeString**

        import java.nio.file.Files
        import java.nio.file.Paths

        val texto = "Hola, mundo desde Kotlin"
        Files.writeString(Paths.get("saludo.txt"), texto)

2. Escribir lista de líneas con **write**

        import java.nio.file.Files
        import java.nio.file.Paths

        val lineas = listOf("Línea 1", "Línea 2", "Línea 3")
        Files.write(Paths.get("lineas.txt"), lineas)

     

3. Escribir usando **newBufferedWriter**

        import java.nio.file.Files
        import java.nio.file.Paths

        Files.newBufferedWriter(Paths.get("log.txt")).use { writer ->
            writer.write("Log iniciado...\n")
            writer.write("Proceso completado.\n")
        }

4. Escribir archivo binario (bytes) con **write**.

        import java.nio.file.Files
        import java.nio.file.Paths

        val datos = byteArrayOf(1, 2, 3, 4, 5)
        Files.write(Paths.get("datos.bin"), datos)


**Ejemplo_Lect_esc_fichero.kt**: lectura y escritura en ficheros de texto (UTF-8)

        import java.nio.file.Files
        import java.nio.file.Paths
        import java.nio.charset.StandardCharsets

        fun main() {
        val ruta = Paths.get("texto.txt")

        //Escritura en fichero de texto
        val lineasParaGuardar = listOf(
                "Primera línea",
                "Segunda línea",
                "¡Hola desde Kotlin!"
        )
        Files.write(ruta, lineasParaGuardar, StandardCharsets.UTF_8)
        println("Fichero de texto escrito.")

        //Lectura del fichero de texto
        val lineasLeidas = Files.readAllLines(ruta, StandardCharsets.UTF_8)
        println("Contenido leído:")
        lineasLeidas.forEach { println(it) }
        }
