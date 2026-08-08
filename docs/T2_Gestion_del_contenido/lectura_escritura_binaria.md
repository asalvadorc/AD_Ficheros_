---
hide:
  - toc
---

# Lectura y escritura de ficheros binarios

Los ficheros binarios guardan informacion como bytes en bruto. Pueden contener numeros, imagenes, audio, video o estructuras de datos compactas.

A diferencia del texto, su contenido no suele ser legible directamente en un editor de texto.

Existen varias formas de trabajar con ficheros binarios en Kotlin y Java. La elección de una u otra depende del tipo de fichero y de las operaciones que se deseen realizar.

Para **ficheros binarios estructurados**, en los que los datos siguen un formato conocido (por ejemplo, un `Int` seguido de un `Double`), es habitual utilizar clases como `DataInputStream` y `DataOutputStream`, o bien la API NIO mediante `FileChannel` y `ByteBuffer`.

Por otro lado, cuando se trabaja con **ficheros binarios no estructurados** (imágenes, PDF, ZIP, archivos de audio o vídeo, etc.), lo más común es manipularlos como una secuencia de bytes utilizando `BufferedInputStream`, `BufferedOutputStream`, `Files.readAllBytes()` o `FileChannel`, sin necesidad de interpretar su contenido interno.

La siguiente tabla resume los principales métodos disponibles para trabajar con ficheros binarios y las situaciones en las que resulta más recomendable utilizar cada uno de ellos.

???+ info "Métodos principales para binario"

    | Método | Tipo de acceso | Uso recomendado |
    |---------|----------------|-----------------|
    | `Files.readAllBytes()` | Carga el fichero completo en memoria | Ficheros pequeños o medianos. Muy útil para encriptación, cálculo de hashes o manipulación sencilla de imágenes, PDF o ZIP. |
    | `DataInputStream` / `DataOutputStream` | Acceso secuencial | Lectura y escritura de **ficheros binarios estructurados** compuestos por tipos primitivos (`int`, `double`, `boolean`, etc.). |
    | `BufferedInputStream` / `BufferedOutputStream` | Acceso secuencial por bloques | Lectura y escritura eficiente de cualquier fichero binario sin cargarlo completamente en memoria. |
    | `FileChannel` + `ByteBuffer` | Acceso por bloques o por posiciones | Ficheros grandes, acceso aleatorio y aplicaciones de alto rendimiento. Adecuado tanto para ficheros binarios estructurados como no estructurados. |
    | `ImageIO.read(File)` / `ImageIO.read(InputStream)` | Lectura de imagen binaria | Carga imágenes (`png`, `jpg`, `bmp`, etc.) en un `BufferedImage` para poder procesarlas. |
    | `ImageIO.write(BufferedImage, formato, File)` / `ImageIO.write(BufferedImage, formato, OutputStream)` | Escritura de imagen binaria | Guarda la imagen en disco o en stream en el formato indicado (`png`, `jpg`, etc.). |





