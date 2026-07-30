# 🔹 Ficheros de acceso aleatorio

Hasta el momento todos los accesos que hemos hecho a los archivos, tanto binarios como de texto, han sido secuenciales. Esto significa que siempre empezamos por el principio del archivo hasta que llegamos a la información que queremos, o en la mayor parte de los casos hasta el final de archivo.

Pero, ¿y si queremos únicamente una determinada información? Afortunadamente hay otra forma de acceder, otro tipo de acceso. Se llama acceso **directo o aleatorio**, porque permitirá ir directamente a una posición determinada del archivo.

Cuando se necesita mayor control, eficiencia y rendimiento en el acceso a ficheros, especialmente en operaciones binarias o de acceso aleatorio, el enfoque tradicional con la clase **RandomAccessFile** de **Java.io** puede quedarse corto. Para estos casos, Java ofrece una solución moderna a través del paquete **java.nio.file** combinado con **FileChannel** y **ByteBuffer**. 

Las clases **FileChannel**, **ByteBuffer** y **StandardOpenOption** forman parte de la API **java.nio** y se utilizan juntas para realizar lectura y escritura de archivos **binarios** y en el **acceso aleatorio a ficheros**.

**FileChannel**{.azul}

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


**ByteBuffer**{.azul}

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

**StandardOpenOption**{.azul}

Se utiliza para indicar cómo debe abrirse o crearse un archivo al trabajar con **FileChannel** o Files.newOutputStream, Files.newByteChannel, etc.

Se utiliza para indicar si el archivo se va a: **Leer (READ)**, **Escribir (WRITE)**, **Crear (CREATE)**, **sobrescribir (TRUNCATE_EXISTING)**, **Añadir al final (APPEND)**.


        val canal = FileChannel.open(
            Paths.get("archivo.txt"),
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE
            )

      

🖥️ **Ejemplo_acceso_posicion.kt**: realiza una operación básica de lectura y escritura de archivo de texto usando **FileChannel** y **ByteBuffer**. 


Este ejemplo muestra cómo usar `FileChannel` y `ByteBuffer` para escribir y leer un archivo controlando de forma explícita la posición dentro del fichero.

- Primero se crea la ruta del archivo y se abre un canal con permisos de lectura, escritura y creación.
- Después se prepara un `ByteBuffer` con el texto que se quiere guardar en el archivo.
- A continuación se sitúa el canal en la posición `0` mediante `canal.position(0)` para empezar a escribir desde el inicio.
- Una vez escrito el contenido, se crea otro `ByteBuffer` vacío para almacenar los datos leídos.
- Luego se vuelve a colocar el canal al principio del fichero para hacer la lectura desde el byte inicial.
- Tras leer el contenido, se llama a `flip()` para preparar el buffer para su lectura.
- Finalmente, los bytes almacenados en el buffer se convierten en una cadena y se muestran por pantalla.

La finalidad del ejercicio es comprender cómo `FileChannel` permite controlar el punto exacto del archivo en el que se lee o escribe, algo fundamental en el acceso aleatorio a ficheros.

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


🖥️ **Ejemplo_acceso_aleatorio.kt** : acceso directo a posiciones en un archivo con **FileChannel** y **ByteBuffer**.


Este ejemplo muestra un caso más claro de acceso aleatorio: escribir contenido en distintas posiciones del archivo sin necesidad de recorrerlo secuencialmente desde el principio hasta el final.

- Primero se crea la ruta del archivo y se abre un `FileChannel` con permisos de lectura, escritura y creación.
- Después se escribe el texto `Inicio` en la posición `0`, es decir, al comienzo del archivo.
- A continuación se cambia la posición del canal a `20` con `canal.position(20)` para escribir otro texto en ese punto exacto del fichero.
- Esta operación deja un espacio intermedio entre ambos fragmentos, que al leer el archivo puede aparecer como bytes o caracteres nulos.
- Luego se reserva un `ByteBuffer` para cargar el contenido completo del archivo desde el inicio.
- Tras la lectura, se usa `flip()` para preparar el buffer y convertir su contenido en una cadena de texto.
- Finalmente, se muestra por pantalla el resultado para observar cómo el segundo texto ha sido escrito en una posición concreta del archivo.

La finalidad del ejercicio es entender que el acceso aleatorio permite saltar directamente a un byte determinado del fichero y leer o escribir allí sin depender de un recorrido secuencial completo.

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


