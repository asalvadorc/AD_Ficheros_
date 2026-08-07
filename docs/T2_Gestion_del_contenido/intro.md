# 🔹 Introducción

## 🛠️ Preparación de los ejemplos

!!!warning ""
    <span class="setup-tag setup-tag-ide">SETUP_IDE</span> <span class="setup-tag setup-tag-paquetes">SETUP_PAQUETES</span> Todos los ejemplos de código del **Tema 2** se deben programar en el proyecto **`Ficheros`**, dentro del paquete **`contenido`**.   
    
    <span class="setup-tag setup-tag-carpetas">SETUP_CARPETAS</span> Si todavía no has preparado las carpetas de trabajo (`documentos` y `documentos2`).   
      
    Si tienes dudas, revisa la configuración en:[🧰 Entorno y Ubicación de Ejemplos](../00_entorno_y_proyecto.md)

## 📖 Conceptos teóricos
En el desarrollo de aplicaciones es común tener que leer y escribir datos almacenados en archivos. Según el tipo de contenido del fichero (texto, binario, imagen, estructurado...), se utilizan clases y métodos distintos para acceder a ellos de forma eficiente y segura.

**Kotlin** se apoya en las bibliotecas de Java (**java.io** y **java.nio.file**) para realizar estas operaciones, permitiendo un control detallado tanto para acceso secuencial como aleatorio.

- **java.io**: API tradicional basada en flujos de bytes o caracteres.

- **java.nio** (New I/O): API moderna basada en canales y buffers, introducida para mejorar el rendimiento y la flexibilidad.

En el desarrollo actual, especialmente en aplicaciones que manejan datos estructurados, binarios o grandes volúmenes de información, es preferible utilizar **java.nio**, por lo que será la API que utilizaremos en los ejemplos y ejercicios.

<!--
??? info "Resumen de clases y métodos para el acceso a ficheros en Kotlin (Java NIO)"

    | Tipo de fichero           | Lectura                             | Escritura                            | Comentario                                               |
    |---------------------------|-------------------------------------|---------------------------------------|----------------------------------------------------------|
    | Texto (líneas)            | `Files.readAllLines(Path)`          | `Files.write(Path, List<String>)`     | Carga todo en memoria                                    |
    |                           | `Files.newBufferedReader(Path)`     | `Files.newBufferedWriter(Path)`       | Más eficiente para archivos grandes                      |
    |                           | `Files.readString(Path)` (Java 11+) | `Files.writeString(Path, String)`     | Lectura/escritura completa como bloque                  |
    | Binario                   | `Files.readAllBytes(Path)`          | `Files.write(Path, ByteArray)`        | Lee y escribe bytes puros                               |
    |                           | `Files.newInputStream(Path)`        | `Files.newOutputStream(Path)`         | Flujo de bytes directo                                  |
    | Binario estructurado      | `FileChannel.read(ByteBuffer)`      | `FileChannel.write(ByteBuffer)`       | Usa `FileChannel` para secuencial o aleatorio           |
    |                           | `SeekableByteChannel.read(...)`     | `SeekableByteChannel.write(...)`      | Canal flexible con `.position()`                        |
    |                           | `ByteBuffer.get*()`                 | `ByteBuffer.put*()`                   | Tipos primitivos (`int`, `double`, etc.)                |
    | Imagen                    | `ImageIO.read(Path/File)`           | `ImageIO.write(BufferedImage, formato, File)` | Lectura/escritura directa por fichero (`png`, `jpg`, etc.) |
    |                           | `ImageIO.read(InputStream)`         | `ImageIO.write(BufferedImage, formato, OutputStream)` | Útil si trabajas con streams en lugar de `File` |
    |                           | `Files.readAllBytes(Path)` + `ImageIO.read(ByteArrayInputStream)` | `ImageIO.write(...)` + `Files.write(Path, byte[])` | Alternativa cuando necesitas manipular bytes antes de guardar |
    | Acceso aleatorio          | `FileChannel.position(offset)`      | `FileChannel.position(offset)`        | Permite saltar a cualquier posición del fichero         |
-->


    