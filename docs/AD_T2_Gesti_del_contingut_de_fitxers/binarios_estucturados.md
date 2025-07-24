# Ficheros binarios estructurados

Cuando necesitamos almacenar datos en formato binario, por ejemplo, registros que combinan números enteros, decimales, caracteres, etc., debemos utilizar una forma de escritura que conserve fielmente su representación interna. Tradicionalmente, esto se hacía con clases como **DataInputStream** y **DataOutputStream**, ambos parte del paquete **java.io**, que permiten leer y escribir tipos primitivos directamente. Sin embargo, en aplicaciones modernas donde se requiere un mayor control sobre el formato binario, mejor eficiencia y rendimiento, y acceso aleatorio o directo a posiciones concretas del archivo, la solución recomendada es utilizar **FileChannel** junto con **ByteBuffer**, ambos parte del paquete **java.nio**.



**FileChannel**{.azul}

Es un canal de E/S que permite leer, escribir, moverse a posiciones específicas en un archivo y trabajar directamente con buffers (ByteBuffer). Es parte del paquete **java.nio**, diseñado para operaciones más rápidas y flexibles que las clases tradicionales como FileInputStream de **java.io**.


| Tipo de fichero           | Lectura                             | Escritura                            | Comentario                                               |
|---------------------------|--------------------------------------|---------------------------------------|----------------------------------------------------------|
| Binario estructurado   | `FileChannel.read(ByteBuffer)`      | `FileChannel.write(ByteBuffer)`       | Usa `FileChannel` para secuencial o aleatorio


        val canal = FileChannel.open(Paths.get("archivo.txt"), StandardOpenOption.READ)

**Métodos de FileChanel para binarios estructurados **{.verde}

| Método                    | Descripción                                                                 |
|---------------------------|------------------------------------------------------------------------------|
| read(ByteBuffer)        | Lee datos desde el canal al buffer.                                         |
| write(ByteBuffer)       | Escribe datos del buffer al canal.                                          |
| position() / position(n) | Obtiene o establece la posición del cursor de lectura/escritura.        |
| size()                  | Tamaño total del archivo.                                                   |
| truncate(long)          | Corta el archivo al tamaño indicado.                                        |
| force(true)             | Fuerza a que los cambios se escriban en disco (útil para persistencia).     |
| map(...)                | Mapea el archivo a memoria (memoria mapeada, muy eficiente).                |
| close()                 | Cierra el canal.                                                            |
                                        |


**ByteBuffer**{.azul}

Se utiliza para leer y escribir datos binarios con control total sobre el formato y la posición, siendo ideal para archivos binarios estructurados, protocolos de red, y sistemas de bajo nivel.


**Métodos de creación**{.verde}

| Método                           | Descripción                                                                 |
|----------------------------------|-----------------------------------------------------------------------------|
| allocate(capacidad)| Crea un buffer con capacidad fija en memoria (no compartida).              |
| wrap(byteArray)    | Crea un buffer que envuelve un array de bytes existente (memoria compartida). |
| wrap(byteArray, offset, length) | Crea un buffer desde una porción del array existente.            |

---

**Métodos de escritura (`put`)**{.verde}

| Método                        | Descripción                                      |
|-------------------------------|--------------------------------------------------|
| put(byte)                   | Escribe un byte en la posición actual.          |
| putInt(int)                 | Escribe un valor `int`.                         |
| putDouble(double)           | Escribe un valor `double`.                      |
| putFloat(float)             | Escribe un valor `float`.                       |
| putChar(char)               | Escribe un carácter (`char`, 2 bytes).          |
| putShort(short)             | Escribe un valor `short`.                       |
| putLong(long)               | Escribe un valor `long`.                        |
| put(byte[], offset, length)` | Escribe una porción de un array de bytes.       |

---

**Métodos de lectura (`get`)**{.verde}

| Método                        | Descripción                                      |
|-------------------------------|--------------------------------------------------|
| get()                       | Lee un byte desde la posición actual.           |
| getInt()                    | Lee un valor `int`.                             |
| getDouble()                 | Lee un valor `double`.                          |
| getFloat()                  | Lee un valor `float`.                           |
| getChar()                   | Lee un carácter (`char`).                       |
| getShort()                  | Lee un valor `short`.                           |
| getLong()                   | Lee un valor `long`.                            |
| get(byte[], offset, length)` | Lee una porción del buffer a un array.          |

---

**Métodos de control del buffer**{.verde}

| Método           | Descripción                                                                 |
|------------------|-----------------------------------------------------------------------------|
| position()     | Devuelve la posición actual del cursor.                                     |
| position(int)  | Establece la posición del cursor.                                           |
| limit()        | Devuelve el límite del buffer.                                              |
| limit(int)     | Establece un nuevo límite.                                                  |
| capacity()     | Devuelve la capacidad total del buffer.                                     |
| clear()        | Limpia el buffer: posición a 0, límite al máximo (sin borrar contenido).    |
| flip()         | Prepara el buffer para lectura después de escribir.                         |
| rewind()       | Posición a 0 para releer desde el inicio.                                   |
| remaining    | Indica cuántos elementos quedan por procesar.                               |
| hasRemaining() | `true` si aún queda contenido por leer o escribir.                          |


🖥️ **Ejemplo_binario_estructurado.kt**: Lectura y escritura en ficheros binarios (con tipos primitivos).

        import java.nio.ByteBuffer
        import java.nio.channels.FileChannel
        import java.nio.file.Paths
        import java.nio.file.StandardOpenOption.*

        fun main() {
            val ruta = Paths.get("documentos/binario.dat")

            // Escritura binaria
            FileChannel.open(ruta, WRITE, CREATE, TRUNCATE_EXISTING).use { canal ->
                val buffer = ByteBuffer.allocate(20)
                buffer.putInt(42)            // int (4 bytes)
                buffer.putDouble(3.1416)     // double (8 bytes)
                buffer.putChar('K')          // char (2 bytes)
                buffer.flip()               // prepara el buffer para ser leído
                canal.write(buffer)
            }
            println("Fichero binario escrito.")

            // Lectura binaria
            FileChannel.open(ruta, READ).use { canal ->
                val buffer = ByteBuffer.allocate(20)
                canal.read(buffer)
                buffer.flip()
                val entero = buffer.int
                val decimal = buffer.double
                val caracter = buffer.char
                println("Contenido leído:\n  Int: $entero\n  Double: $decimal\n  Char: $caracter")
            }
        }
!!!Note "Nota"
    Siempre que quieras leer datos que acabas de escribir en un buffer utliza **flip()**. Sin flip(), la posición estaría al final y no podrías leer nada útil.