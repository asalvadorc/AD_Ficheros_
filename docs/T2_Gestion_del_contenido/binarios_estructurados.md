# 🔹 Ficheros binarios estructurados



!!! warning "Dónde guardar los ejemplos"
    Programa los ejemplos en el proyecto **`Ficheros`**, dentro del paquete **`contenido`**. Los archivos binarios estructurados se leerán y generarán en `documentos`, en la raíz del proyecto.

    Consulta [🧰 Entorno y ubicación de los ejemplos](../00_entorno_y_proyecto.md) si necesitas revisar la estructura completa.

Los ficheros binarios estructurados almacenan la información siguiendo una organización definida por el programador, normalmente mediante registros formados por tipos de datos primitivos (`int`, `double`, `boolean`, etc.).

En Java existen varias APIs para trabajar con este tipo de ficheros. La elección de una u otra dependerá de las necesidades de la aplicación:

- **`DataInputStream` y `DataOutputStream`** son la opción más sencilla cuando los registros se leen o escriben de forma **secuencial**, ya que permiten trabajar directamente con tipos primitivos sin preocuparse por su representación en bytes.

- **`BufferedInputStream` y `BufferedOutputStream`** mejoran el rendimiento al realizar las operaciones mediante un búfer de memoria. Habitualmente se combinan con `DataInputStream` y `DataOutputStream` para reducir el número de accesos al disco.

- **`FileChannel` y `ByteBuffer`** forman parte de la API NIO y ofrecen un mayor control sobre la lectura y escritura de datos. Son especialmente adecuados cuando se necesita acceder a posiciones concretas del fichero, trabajar con grandes volúmenes de información o desarrollar aplicaciones de alto rendimiento.

En este apartado estudiaremos cuándo resulta más conveniente utilizar cada una de estas alternativas y las ventajas que aporta cada una de ellas.

## DataInputStream / DataOutputStream

Cuando hay que guardar tipos primitivos (por ejemplo `Int`, `Double`) de forma secuencial, suele usarse `DataInputStream` y `DataOutputStream`.


??? info "Clases y método de DataInputStream y DataOutputStream"

    | Clase               | Método                          | Tipo de dato           | Descripción                                               |
    |--------------------|----------------------------------|------------------------|-----------------------------------------------------------|
    | DataOutputStream   | `writeInt(int)`                  | Entero (4 bytes)       | Escribe un entero con signo                               |
    |                    | `writeDouble(double)`            | Decimal (8 bytes)      | Escribe un número en coma flotante                        |
    |                    | `writeFloat(float)`              | Decimal (4 bytes)      | Escribe un número float                                   |
    |                    | `writeLong(long)`                | Entero largo (8 bytes) | Escribe un long                                           |
    |                    | `writeBoolean(boolean)`          | Booleano (1 byte)      | Escribe un valor verdadero/falso                          |
    |                    | `writeChar(char)`                | Carácter (2 bytes)     | Escribe un carácter Unicode                               |
    |                    | `writeUTF(String)`               | Cadena UTF-8           | Escribe una cadena precedida por su longitud en 2 bytes   |
    |                    | `writeByte(int)`                 | Byte (1 byte)          | Escribe un solo byte                                      |
    |                    | `writeShort(int)`                | Entero corto (2 bytes) | Escribe un short                                          |
    | DataInputStream    | `readInt()`                      | Entero                 | Lee un entero con signo                                   |
    |                    | `readDouble()`                   | Decimal                | Lee un número double                                      |
    |                    | `readFloat()`                    | Decimal                | Lee un número float                                       |
    |                    | `readLong()`                     | Entero largo           | Lee un long                                               |
    |                    | `readBoolean()`                  | Booleano               | Lee un valor verdadero/falso                              |
    |                    | `readChar()`                     | Carácter               | Lee un carácter Unicode                                   |
    |                    | `readUTF()`                      | Cadena UTF-8           | Lee una cadena UTF-8                                      |
    |                    | `readByte()`                     | Byte                   | Lee un byte                                               |
    |                    | `readShort()`                    | Entero corto           | Lee un short                                              |


---


🖥️ **Ejemplo_binario_estructurado.kt**: Lectura y escritura en ficheros binarios (con tipos primitivos).


Este ejemplo muestra cómo guardar varios datos primitivos en un fichero binario estructurado y recuperarlos después respetando exactamente el mismo orden de escritura.

La finalidad del ejercicio es entender que un fichero binario estructurado organiza la información como una secuencia de datos tipados, y que `DataOutputStream` y `DataInputStream` son herramientas muy adecuadas para escribir y leer esa estructura.

```kotlin
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/binario.dat") // (1)!
    Files.createDirectories(ruta.parent) // (2)!

    DataOutputStream(Files.newOutputStream(ruta)).use { out -> // (3)!
        out.writeInt(42) // (4)!
        out.writeDouble(3.1416) // (5)!
        out.writeUTF("K") // (6)!
    }

    val fis = FileInputStream(ruta.toFile())
    val input = DataInputStream(fis) // (7)!
    val entero = input.readInt() // (8)!
    val decimal = input.readDouble() // (9)!
    val caracter = input.readUTF() // (10)!
    input.close() // (11)!
    fis.close()

    println("📄 Contenido leído:")
    println("  Int: $entero")
    println("  Double: $decimal")
    println("  Char: $caracter")
}
```

1. Define el fichero binario destino.
2. Asegura que la carpeta exista.
3. Abre flujo binario de escritura.
4. Escribe un `Int` en binario.
5. Escribe un `Double` en binario.
6. Escribe una cadena UTF en binario.
7. Abre flujo binario de lectura.
8. Lee el `Int` en el mismo orden de escritura.
9. Lee el `Double` en el mismo orden.
10. Lee la cadena UTF en el mismo orden.
11. Cierra el flujo de lectura.

## BufferedStream

Cuando se trabaja con ficheros binarios estructurados, normalmente estos métodos se combinan con DataInputStream y DataOutputStream,

 🖥️ **Ejemplo_binario_estructurado_buffer.kt**: Lectura y escritura en ficheros binarios utlizando búfer (con tipos primitivos).       

Este ejemplo muestra cómo mejorar la lectura y escritura de un fichero binario estructurado utilizando **flujos con búfer**. Para ello, `BufferedOutputStream` y `BufferedInputStream` se combinan con `DataOutputStream` y `DataInputStream`, consiguiendo una mayor eficiencia al reducir el número de accesos al disco.

La finalidad del ejercicio es comprender que los flujos con búfer no modifican la estructura del fichero binario ni la forma de acceder a los datos, sino que **mejoran el rendimiento** al minimizar los accesos físicos al dispositivo de almacenamiento. Por ello, es habitual combinar `BufferedInputStream` y `BufferedOutputStream` con `DataInputStream` y `DataOutputStream` cuando se trabaja con ficheros binarios estructurados.

```kotlin
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/binario.dat") // (1)!
    Files.createDirectories(ruta.parent) // (2)!

    DataOutputStream( // (3)!
        BufferedOutputStream(
            Files.newOutputStream(ruta)
        )
    ).use { out ->
        out.writeInt(42) // (4)!
        out.writeDouble(3.1416) // (5)!
        out.writeUTF("K") // (6)!
    }

    DataInputStream( // (7)!
        BufferedInputStream(
            Files.newInputStream(ruta)
        )
    ).use { input ->
        val entero = input.readInt() // (8)!
        val decimal = input.readDouble() // (9)!
        val caracter = input.readUTF() // (10)!

        println("📄 Contenido leído:")
        println("Int: $entero")
        println("Double: $decimal")
        println("Cadena: $caracter")
    }
}
```

1. Define ruta del binario.
2. Crea carpeta si falta.
3. Encadena `DataOutputStream` sobre `BufferedOutputStream`.
4. Escribe `Int` en binario.
5. Escribe `Double` en binario.
6. Escribe cadena UTF.
7. Encadena `DataInputStream` sobre `BufferedInputStream`.
8. Lee `Int` respetando orden.
9. Lee `Double` respetando orden.
10. Lee cadena UTF respetando orden.

## FileChannel y ByteBuffer

Cuando hay que guardar tipos primitivos (por ejemplo `Int`, `Double`) de forma compacta, suele usarse `ByteBuffer` y `FileChannel`.

??? info  "Clases y métodos de ByteBuffer y FileChannel"

    | Clase | Método | Descripción |
    |--------|--------|-------------|
    | `ByteBuffer` | `allocate(int capacity)` | Crea un búfer con la capacidad indicada en bytes. |
    | `ByteBuffer` | `putInt(int value)` | Escribe un valor de tipo `int` (4 bytes). |
    | `ByteBuffer` | `putDouble(double value)` | Escribe un valor de tipo `double` (8 bytes). |
    | `ByteBuffer` | `putChar(char value)` | Escribe un valor de tipo `char` (2 bytes). |
    | `ByteBuffer` | `getInt()` | Lee un valor de tipo `int` del búfer. |
    | `ByteBuffer` | `getDouble()` | Lee un valor de tipo `double` del búfer. |
    | `ByteBuffer` | `getChar()` | Lee un valor de tipo `char` del búfer. |
    | `ByteBuffer` | `flip()` | Prepara el búfer para leer los datos previamente escritos en él. |
    | `ByteBuffer` | `clear()` | Vacía el búfer y lo prepara para una nueva escritura. |
    | `FileChannel` | `open(Path, OpenOption...)` | Abre un canal asociado a un fichero. |
    | `FileChannel` | `read(ByteBuffer)` | Lee datos del fichero y los almacena en un `ByteBuffer`. |
    | `FileChannel` | `write(ByteBuffer)` | Escribe en el fichero el contenido de un `ByteBuffer`. |
    | `FileChannel` | `position(long)` | Establece la posición del canal, permitiendo el acceso aleatorio al fichero. |
    | `FileChannel` | `close()` | Cierra el canal y libera los recursos asociados. |

🖥️ **Ejemplo_binario_estructurado_compacto.kt**: Lectura y escritura en ficheros binarios utlizando búfer (con tipos primitivos).

Este ejemplo muestra cómo leer y escribir un fichero binario estructurado utilizando la API **NIO** de Java mediante `FileChannel` y `ByteBuffer`. Esta combinación ofrece un mayor control sobre la gestión de los datos y resulta especialmente adecuada para aplicaciones que requieren un alto rendimiento o acceso directo a posiciones concretas del fichero.

La finalidad del ejercicio es comprender cómo `FileChannel` y `ByteBuffer` permiten trabajar directamente con los bytes de un fichero binario estructurado. Aunque requieren una gestión más explícita del búfer que `DataInputStream` y `DataOutputStream`, ofrecen un mayor control sobre la lectura y escritura de datos y constituyen la base de la API NIO para aplicaciones que necesitan un acceso más eficiente o flexible a los ficheros.

```kotlin
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

fun main() {
    val ruta = Path.of("documentos/registro.bin") // (1)!

    FileChannel.open( // (2)!
        ruta,
        StandardOpenOption.CREATE,
        StandardOpenOption.WRITE,
        StandardOpenOption.TRUNCATE_EXISTING
    ).use { canal ->
        val buffer = ByteBuffer.allocate(Int.SIZE_BYTES + Double.SIZE_BYTES) // (3)!
        buffer.putInt(42) // (4)!
        buffer.putDouble(19.95) // (5)!
        buffer.flip() // (6)!
        canal.write(buffer) // (7)!
    }

    FileChannel.open(ruta, StandardOpenOption.READ).use { canal -> // (8)!
        val buffer = ByteBuffer.allocate(Int.SIZE_BYTES + Double.SIZE_BYTES) // (9)!
        canal.read(buffer) // (10)!
        buffer.flip() // (11)!
        val id = buffer.int // (12)!
        val precio = buffer.double // (13)!
        println("id=$id, precio=$precio")
    }
}
```

1. Define la ruta del registro binario.
2. Abre canal de escritura con creacion/sobrescritura.
3. Reserva bytes exactos para `Int + Double`.
4. Inserta el `Int` en el buffer.
5. Inserta el `Double` en el buffer.
6. Cambia el buffer a modo lectura.
7. Escribe los bytes al fichero.
8. Abre canal en modo lectura.
9. Reserva buffer de lectura del mismo tamaño.
10. Lee bytes del fichero al buffer.
11. Prepara buffer para extraer datos.
12. Recupera el `Int`.
13. Recupera el `Double`.

!!!question "🧠 ¿Cuándo utilizar cada método?"
    Si tienes que decidir entre `DataInputStream`/`DataOutputStream` y `ByteBuffer`/`FileChannel`, ¿en qué casos conviene usar cada opción?

    ??? success "Ver respuesta"
        | Método | ¿Cuándo utilizarlo? |
        |--------|----------------------|
        | `DataInputStream` / `DataOutputStream` | Cuando el fichero contiene registros formados por tipos de datos (`Int`, `Double`, `Boolean`, `UTF`, etc.) y el acceso es secuencial. Normalmente se combinan con `BufferedInputStream` y `BufferedOutputStream`. |
        | `ByteBuffer` / `FileChannel` | Cuando se necesita mayor control sobre la lectura y escritura de datos, acceso aleatorio al fichero o mejor rendimiento mediante la API NIO. |
