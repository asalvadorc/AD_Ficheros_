# 🔹 Clases y métodos sobre ficheros. Java.nio


En el desarrollo de aplicaciones es común tener que leer y escribir datos almacenados en archivos. Según el tipo de contenido del fichero (texto, binario, imagen, estructurado...), se utilizan clases y métodos distintos para acceder a ellos de forma eficiente y segura.

**Kotlin** se apoya en las bibliotecas de Java (**java.io** y **java.nio.file**) para realizar estas operaciones, permitiendo un control detallado tanto para acceso secuencial como aleatorio.

- **java.io**: API tradicional basada en flujos de bytes o caracteres.

- **java.nio** (New I/O): API moderna basada en canales y buffers, introducida para mejorar el rendimiento y la flexibilidad.

En el desarrollo actual, especialmente en aplicaciones que manejan datos estructurados, binarios o grandes volúmenes de información, es preferible utilizar **java.nio**, por lo que será la API que utilizaremos en los ejemplos y ejercicios.  

La siguiente tabla resume las clases más utilizadas para trabajar con los distintos tipos de ficheros, indicando las opciones recomendadas para lectura y escritura, y proporcionando notas aclaratorias sobre su uso típico.

**Resumen de clases y métodos para el acceso a ficheros en Kotlin (Java NIO)**{.verde}


| Tipo de fichero           | Lectura                             | Escritura                            | Comentario                                               |
|---------------------------|-------------------------------------|---------------------------------------|----------------------------------------------------------|
| Texto (líneas)            | Files.readAllLines(Path)          | Files.write(Path, List<String>)     | Carga todo en memoria                                    |
|                           | Files.newBufferedReader(Path)     | Files.newBufferedWriter(Path)       | Más eficiente para archivos grandes                      |
|                           | Files.readString(Path) (Java 11+) | Files.writeString(Path, String)     | Lectura/escritura completa como bloque                  |
| Binario                   | Files.readAllBytes(Path)          | Files.write(Path, ByteArray)        | Lee y escribe bytes puros                               |
|                           | Files.newInputStream(Path)        | Files.newOutputStream(Path)         | Flujo de bytes directo                                  |
| Binario estructurado      | FileChannel.read(ByteBuffer)      | FileChannel.write(ByteBuffer)       | Usa `FileChannel` para secuencial o aleatorio           |
|                           | SeekableByteChannel.read(...)     | SeekableByteChannel.write(...)      | Canal flexible con `.position()`                        |
|                           | ByteBuffer.get*()                 | ByteBuffer.put*()                   | Tipos primitivos (`int`, `double`, etc.)                |
| Imagen                    | ImageIO.read(Path/File)           | ImageIO.write(BufferedImage, ...)   | Usa `javax.imageio.ImageIO`                             |
| Acceso aleatorio          | FileChannel.position(offset)      | FileChannel.position(offset)        | Permite saltar a cualquier posición del fichero         |




