---
hide:
  - toc
---

# 🔹 Ficheros de acceso aleatorio

Hasta el momento todos los accesos que hemos hecho a los archivos, tanto binarios como de texto, han sido secuenciales. Esto significa que siempre empezamos por el principio del archivo hasta que llegamos a la información que queremos, o en la mayor parte de los casos hasta el final de archivo.

Pero, ¿y si queremos únicamente una determinada información? Afortunadamente hay otra forma de acceder, otro tipo de acceso. Se llama acceso **directo o aleatorio**, porque permitirá ir directamente a una posición determinada del archivo.

Cuando se necesita mayor control, eficiencia y rendimiento en el acceso a ficheros, especialmente en operaciones binarias o de acceso aleatorio, el enfoque tradicional con la clase **RandomAccessFile** de **Java.io** puede quedarse corto. Para estos casos, Java ofrece una solución moderna a través del paquete **java.nio.file** combinado con **FileChannel** y **ByteBuffer**. 

Las clases **FileChannel**, **ByteBuffer** y **StandardOpenOption** forman parte de la API **java.nio** y se utilizan juntas para realizar lectura y escritura de archivos **binarios** y en el **acceso aleatorio a ficheros**.

## 🔹 FileChannel

| Tipo de fichero           | Lectura                             | Escritura                            | Comentario                                               |
|---------------------------|--------------------------------------|---------------------------------------|----------------------------------------------------------|
| Acceso aleatorio       | `FileChannel.position(offset)`      | `FileChannel.position(offset)`        | Permite saltar a cualquier posición del fichero         |


??? info "Métodos habituales de FileChannel para el acceso aleatorio a ficheros"

    | Método                   | Función principal                                         |
    |--------------------------|-----------------------------------------------------------|
    | position()             |  Devuelve la posición actual del puntero en el archivo     |
    | position(long)         |  Establece una posición exacta para lectura/escritura      |
    | truncate(long)         | Recorta o amplía el tamaño del archivo                    |
    | size()                 |  Devuelve el tamaño total actual del archivo              |


## 🔹 ByteBuffer

ByteBuffer se utiliza en archivos de acceso aleatorio porque permite leer y escribir bloques binarios de datos en posiciones específicas del archivo de forma eficiente y controlada.


??? info "Métodos de creación de ByteBuffer"

    | Método                           | Descripción                                                                 |
    |----------------------------------|-----------------------------------------------------------------------------|
    | allocate(capacidad) | Crea un buffer con capacidad fija en memoria (no compartida).              |
    | wrap(byteArray)    | Crea un buffer que envuelve un array de bytes existente (memoria compartida). |
    | wrap(byteArray, offset, length) | Crea un buffer desde una porción del array existente.            |

---

??? info "Métodos de escritura (`put`) de ByteBuffer"

    | Método                        | Descripción                                      |
    |-------------------------------|--------------------------------------------------|
    | put(byte)                   | Escribe un byte en la posición actual.          |
    | putInt(int)                 | Escribe un valor `int`.                         |
    | putDouble(double)           | Escribe un valor `double`.                      |
    | putFloat(float)             | Escribe un valor `float`.                       |
    | putChar(char)               | Escribe un carácter (`char`, 2 bytes).          |
    | putShort(short)             | Escribe un valor `short`.                       |
    | putLong(long)               | Escribe un valor `long`.                        |
    | put(byte[], offset, length) | Escribe una porción de un array de bytes.       |

---

??? info "Métodos de lectura (`get`) de ByteBuffer"

    | Método                        | Descripción                                      |
    |-------------------------------|--------------------------------------------------|
    | get()                       | Lee un byte desde la posición actual.           |
    | getInt()                    | Lee un valor `int`.                             |
    | getDouble()                 | Lee un valor `double`.                          |
    | getFloat()                  | Lee un valor `float`.                           |
    | getChar()                   | Lee un carácter (`char`).                       |
    | getShort()                  | Lee un valor `short`.                           |
    | getLong()                   | Lee un valor `long`.                            |
    | get(byte[], offset, length) | Lee una porción del buffer a un array.          |

---

??? info "Métodos de control de ByteBuffer"

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
    | remaining()    | Indica cuántos elementos quedan por procesar.                               |
    | hasRemaining() | `true` si aún queda contenido por leer o escribir.                          |

## 🔹StandardOpenOption

Se utiliza para indicar cómo debe abrirse o crearse un archivo al trabajar con **FileChannel** o Files.newOutputStream, Files.newByteChannel, etc.

Se utiliza para indicar si el archivo se va a: **Leer (READ)**, **Escribir (WRITE)**, **Crear (CREATE)**, **sobrescribir (TRUNCATE_EXISTING)**, **Añadir al final (APPEND)**.


        val canal = FileChannel.open(
            Paths.get("archivo.txt"),
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE
            )

      

🖥️ **Ejemplo_acceso_posicion.kt**: realiza una operación básica de lectura y escritura de archivo de texto usando **FileChannel** y **ByteBuffer**. 


Este ejemplo muestra cómo usar `FileChannel` y `ByteBuffer` para escribir y leer un archivo controlando de forma explícita la posición dentro del fichero.

La finalidad del ejercicio es comprender cómo `FileChannel` permite controlar el punto exacto del archivo en el que se lee o escribe, algo fundamental en el acceso aleatorio a ficheros.

```kotlin
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.*

fun main() {
    val path = Paths.get("documentos/datos.txt") // (1)!

    FileChannel.open(path, READ, WRITE, CREATE).use { canal -> // (2)!
        val buffer = ByteBuffer.wrap("Hola desde Kotlin\n".toByteArray()) // (3)!

        canal.position(0) // (4)!
        canal.write(buffer) // (5)!

        val bufferLectura = ByteBuffer.allocate(1024) // (6)!
        canal.position(0) // (7)!
        canal.read(bufferLectura) // (8)!

        bufferLectura.flip() // (9)!
        println(String(bufferLectura.array(), 0, bufferLectura.limit()))
    }
}
```

1. Crea la ruta del fichero a trabajar.
2. Abre el `FileChannel` con lectura, escritura y creacion.
3. Carga en memoria los bytes a escribir.
4. Situa el puntero del canal en el byte inicial.
5. Escribe el contenido del buffer en el fichero.
6. Reserva un buffer para la lectura.
7. Recoloca el canal al inicio para leer.
8. Lee bytes desde el fichero al buffer.
9. Prepara el buffer para extraer sus datos.


🖥️ **Ejemplo_acceso_aleatorio.kt** : acceso directo a posiciones en un archivo con **FileChannel** y **ByteBuffer**.


Este ejemplo muestra un caso más claro de acceso aleatorio: escribir contenido en distintas posiciones del archivo sin necesidad de recorrerlo secuencialmente desde el principio hasta el final.

La finalidad del ejercicio es entender que el acceso aleatorio permite saltar directamente a un byte determinado del fichero y leer o escribir allí sin depender de un recorrido secuencial completo.

```kotlin
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.*

fun main() {
    val path = Paths.get("documentos/datos.txt") // (1)!

    FileChannel.open(path, READ, WRITE, CREATE).use { canal -> // (2)!
        canal.position(0) // (3)!
        val inicio = ByteBuffer.wrap("Inicio\n".toByteArray()) // (4)!
        canal.write(inicio) // (5)!

        canal.position(20) // (6)!
        val texto = ByteBuffer.wrap("Texto en posición 20\n".toByteArray()) // (7)!
        canal.write(texto) // (8)!

        val bufferLectura = ByteBuffer.allocate(1024) // (9)!
        canal.position(0) // (10)!
        canal.read(bufferLectura) // (11)!

        bufferLectura.flip() // (12)!
        val contenido = String(bufferLectura.array(), 0, bufferLectura.limit())
        println("Contenido leído del archivo:\n$contenido")
    }
}
```

1. Define la ruta del fichero.
2. Abre el `FileChannel` para acceso aleatorio.
3. Situa el puntero en el inicio.
4. Prepara el primer bloque de bytes.
5. Escribe el primer bloque.
6. Salta directamente al byte 20.
7. Prepara el segundo bloque de bytes.
8. Escribe el segundo bloque en esa posicion.
9. Reserva buffer para lectura completa.
10. Vuelve al inicio del archivo.
11. Lee contenido del canal al buffer.
12. Cambia el buffer a modo lectura.

!!!note "📤 Salida esperada"
        Contenido leído del archivo:
        Inicio
        [caracteres nulos...]Texto en posición 20

---

!!!question "🧠 Comprueba tu comprensión"
    1. Antes de realizar una operación de lectura con `canal.read(bufferLectura)`, ¿qué método del canal debes usar si quieres leer desde el byte 50?
    2. Después de llenar un `ByteBuffer` con datos leídos del archivo, ¿qué método es obligatorio llamar en el buffer ANTES de extraer sus datos para mostrarlos?

    ??? success "Ver respuestas"
        1. Debes usar `canal.position(50)`. Este método mueve el puntero de lectura/escritura a la posición exacta indicada.
        2. Debes usar el método `flip()`. Este método prepara el buffer para lectura, situando el puntero interno en la posición 0 para que puedas leer exactamente lo que se acaba de descargar del archivo.


