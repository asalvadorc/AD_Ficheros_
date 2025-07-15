# Acceso aleatorio a ficheros

Además del acceso a ficheros de texto o binarios simples, en programación también es común trabajar con otros tipos de ficheros más especializados, según cómo se estructuran y acceden los datos. Entre ellos destacan:

- **Binarios estructurados**: contienen datos codificados en formato binario siguiendo una estructura fija y conocida (por ejemplo, registros con campos de longitud fija).  
_Ejemplo:_ guardar una lista de personas con nombre (20 bytes) + edad (4 bytes).

- **Acceso aleatorio**: permite leer y escribir en cualquier posición de un fichero, sin necesidad de procesar todo el contenido desde el principio.  
_Ejemplo:_ actualizar solo el campo "saldo" de un cliente en un fichero binario de cuentas.

**Métodos para ficheros binarios estructurados y de acceso aleatorio**{.azul}

Las clases **FileChannel**, **ByteBuffer** y **StandardOpenOption** forman parte de la API **java.nio** y se utilizan juntas para realizar lectura y escritura de archivos **binarios estructurados** y en el **acceso aleatorio a ficheros**.


**FileChannel**{.verde}

Es un canal de E/S que permite leer, escribir, moverse a posiciones específicas en un archivo y trabajar directamente con buffers (ByteBuffer). Es parte del paquete **java.nio**, diseñado para operaciones más rápidas y flexibles que las clases tradicionales como FileInputStream de **java.io**.


        val canal = FileChannel.open(Paths.get("archivo.txt"), StandardOpenOption.READ)

---

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


**ByteBuffer**{.verde}

Se utiliza para leer y escribir datos binarios con control total sobre el formato y la posición, siendo ideal para archivos binarios estructurados, protocolos de red, y sistemas de bajo nivel.


**Métodos de creación**

| Método                           | Descripción                                                                 |
|----------------------------------|-----------------------------------------------------------------------------|
| allocate(capacidad)| Crea un buffer con capacidad fija en memoria (no compartida).              |
| wrap(byteArray)    | Crea un buffer que envuelve un array de bytes existente (memoria compartida). |
| wrap(byteArray, offset, length) | Crea un buffer desde una porción del array existente.            |

---

**Métodos de escritura (`put`)**

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

**Métodos de lectura (`get`)**

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

**Métodos de control del buffer**

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

**StandardOpenOption**{.verde}

Se utiliza para indicar cómo debe abrirse o crearse un archivo al trabajar con **FileChannel** o Files.newOutputStream, Files.newByteChannel, etc.

Se utiliza para indicar si el archivo se va a: Leer (READ), Escribir (WRITE), Crear (CREATE),sobrescribir (TRUNCATE_EXISTING), Añadir al final (APPEND).


        val canal = FileChannel.open(
            Paths.get("archivo.txt"),
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE
            )

**Ejemplo_acceso_posicion.kt**: realiza una operación básica de lectura y escritura de archivo de texto usando FileChannel y ByteBuffer. 

        // Importamos las clases necesarias
        import java.nio.ByteBuffer                      // Para gestionar buffers de bytes
        import java.nio.channels.FileChannel            // Para acceder al archivo como canal
        import java.nio.file.Paths                      // Para crear la ruta del archivo
        import java.nio.file.StandardOpenOption.*       // Para usar opciones como READ, WRITE, CREATE

        fun main() {
            // Creamos una ruta al archivo datos.txt
            val path = Paths.get("documentos/datos.txt")

            // Abrimos el canal con permisos de lectura, escritura y creación
            FileChannel.open(path, READ, WRITE, CREATE).use { canal ->

                // Creamos un buffer con el texto a escribir convertido a bytes
                val buffer = ByteBuffer.wrap("Hola desde Kotlin\n".toByteArray())

                // Establecemos la posición del canal al principio del archivo
                canal.position(0)

                // Escribimos el contenido del buffer en el archivo
                canal.write(buffer)

                // Creamos un nuevo buffer vacío para leer hasta 1024 bytes
                val bufferLectura = ByteBuffer.allocate(1024)

                // Volvemos al principio del archivo para leer desde el inicio
                canal.position(0)

                // Leemos desde el archivo al buffer
                canal.read(bufferLectura)

                // Cambiamos el buffer de modo escritura a modo lectura
                bufferLectura.flip()

                // Convertimos el contenido leído a cadena y lo mostramos
                println(String(bufferLectura.array(), 0, bufferLectura.limit()))
            } // El canal se cierra automáticamente gracias a `use`
        }



### 🔹 ficheros de acceso aleatorio

Hasta el momento todos los accesos que hemos hecho a los archivos, tanto binarios como de carácter, han sido secuenciales. Esto significa que siempre empezamos por el principio del archivo hasta que llegamos a la información que queremos, o en la mayor parte de los casos hasta el final de archivo.

Pero, ¿y si queremos únicamente una determinada información? Afortunadamente hay otra forma de acceder, otro tipo de acceso. Se llama acceso directo o aleatorio, porque permitirá ir directamente a una posición determinada del archivo.

Cuando se necesita mayor control, eficiencia y rendimiento en el acceso a ficheros, especialmente en operaciones binarias o de acceso aleatorio, el enfoque tradicional con la clase **RandomAccessFile** de **Java.io** puede quedarse corto. Para estos casos, Java ofrece una solución moderna a través del paquete **java.nio.file** combinado con **FileChannel** y **ByteBuffer**. 

**Métodos habituales de FileChannel para el acceso aleatorio a ficheros**{.azul}

| Método                   | Función principal                                         |
|--------------------------|-----------------------------------------------------------|
| position()             |  Devuelve la posición actual del puntero en el archivo     |
| position(long)         |  Establece una posición exacta para lectura/escritura      |
| truncate(long)         | Recorta o amplía el tamaño del archivo                    |
| size()                 |  Devuelve el tamaño total actual del archivo  


**Ejemplo_acceso_aleatorio.kt** : acceso directo a posiciones en un archivo con FileChannel y ByteBuffer

        import java.nio.ByteBuffer
        import java.nio.channels.FileChannel
        import java.nio.file.Paths
        import java.nio.file.StandardOpenOption.*

        fun main() {
            val path = Paths.get("documentos/datos.txt")

            // Abrimos el canal con permisos de lectura y escritura
            FileChannel.open(path, READ, WRITE, CREATE).use { canal ->

                // Escribimos "Inicio\n" en la posición 0 del archivo
                canal.position(0)
                val inicio = ByteBuffer.wrap("Inicio\n".toByteArray())
                canal.write(inicio)

                // Escribimos "Texto en posición 20\n" en la posición 20 del archivo
                canal.position(20)
                val texto = ByteBuffer.wrap("Texto en posición 20\n".toByteArray())
                canal.write(texto)

                // Leemos el contenido completo desde el inicio (posición 0)
                val bufferLectura = ByteBuffer.allocate(1024)
                canal.position(0)
                canal.read(bufferLectura)

                // Preparamos el buffer para lectura e imprimimos el contenido
                bufferLectura.flip()
                val contenido = String(bufferLectura.array(), 0, bufferLectura.limit())
                println("Contenido leído del archivo:\n$contenido")
            }
        }
                      
### 🔹 Ficheros estructurados

Cuando necesitamos almacenar datos en formato binario, por ejemplo, registros que combinan números enteros, decimales, caracteres, etc., debemos utilizar una forma de escritura que conserve fielmente su representación interna. Tradicionalmente, esto se hacía con clases como **DataInputStream** y **DataOutputStream**, ambos parte del paquete **java.io**, que permiten leer y escribir tipos primitivos directamente. Sin embargo, en aplicaciones modernas donde se requiere un mayor control sobre el formato binario, mejor eficiencia y rendimiento, y acceso aleatorio o directo a posiciones concretas del archivo, la solución recomendada es utilizar **FileChannel** junto con **ByteBuffer**, ambos parte del paquete **java.nio**.



**Ejemplo_binario_estructurado.kt**: Lectura y escritura en ficheros binarios (con tipos primitivos).

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

