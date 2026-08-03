# 📘 Resumen del Tema 2: Manejo de ficheros

## Tipos de ficheros y métodos de acceso

| Tipo de fichero         | Lectura                             | Escritura                           | Comentario                                      |
|-------------------------|--------------------------------------|--------------------------------------|-------------------------------------------------|
| **Texto (líneas)**      | `Files.readAllLines`                | `Files.write(Path, List<String>)`    | Carga todo en memoria                          |
|                         | `Files.newBufferedReader`           | `Files.newBufferedWriter`            | Más eficiente para archivos grandes            |
|                         | `Files.readString` (Java 11+)       | `Files.writeString`                  | Lectura/escritura completa como bloque         |
| **Binario**             | `Files.readAllBytes`                | `Files.write(Path, ByteArray)`       | Lee y escribe bytes puros                      |
|                         | `Files.newInputStream`              | `Files.newOutputStream`              | Flujo de bytes directo                         |
| **Binario estructurado**| `FileChannel.read(ByteBuffer)`      | `FileChannel.write(ByteBuffer)`      | Acceso secuencial o aleatorio con `ByteBuffer` |
|                         | `SeekableByteChannel.read(...)`     | `SeekableByteChannel.write(...)`     | Más flexible (se puede posicionar)             |
|                         | `ByteBuffer.get*()`                 | `ByteBuffer.put*()`                  | Tipos primitivos                               |
| **Acceso aleatorio**    | `FileChannel.position(offset)`      | `FileChannel.position(offset)`       | Permite saltar a posiciones concretas          |
| **Imagen**              | `ImageIO.read(Path/File)`           | `ImageIO.write(BufferedImage, ...)`  | Usa `javax.imageio.ImageIO`                    |