# 🔹 Ficheros binarios estructurados



Los ficheros binarios estructurados almacenan la información siguiendo una organización definida por el programador, normalmente mediante registros formados por tipos de datos primitivos (`int`, `double`, `boolean`, etc.).

En Java existen varias APIs para trabajar con este tipo de ficheros. La elección de una u otra dependerá de las necesidades de la aplicación:

- **`DataInputStream` y `DataOutputStream`** son la opción más sencilla cuando los registros se leen o escriben de forma **secuencial**, ya que permiten trabajar directamente con tipos primitivos sin preocuparse por su representación en bytes.

- **`BufferedInputStream` y `BufferedOutputStream`** mejoran el rendimiento al realizar las operaciones mediante un búfer de memoria. Habitualmente se combinan con `DataInputStream` y `DataOutputStream` para reducir el número de accesos al disco.

- **`FileChannel` y `ByteBuffer`** forman parte de la API NIO y ofrecen un mayor control sobre la lectura y escritura de datos. Son especialmente adecuados cuando se necesita acceder a posiciones concretas del fichero, trabajar con grandes volúmenes de información o desarrollar aplicaciones de alto rendimiento.

En este apartado estudiaremos cuándo resulta más conveniente utilizar cada una de estas alternativas y las ventajas que aporta cada una de ellas.

**DataInputStream y DataOutputStream**{.azul}

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

- Primero se define la ruta del archivo y se crea la carpeta contenedora si no existe.
- Después se abre un `DataOutputStream`, que permite escribir datos primitivos en formato binario.
- A continuación se escriben varios valores de tipos diferentes, como un entero, un número decimal y una cadena corta, todos seguidos dentro del mismo fichero.
- Luego se abre un `DataInputStream` para leer esos datos almacenados.
- La lectura debe hacerse en el mismo orden en que se escribieron; de lo contrario, los valores obtenidos serían incorrectos o se produciría un error.
- Finalmente, los datos leídos se muestran por pantalla para comprobar que el contenido recuperado coincide con el contenido original.

La finalidad del ejercicio es entender que un fichero binario estructurado organiza la información como una secuencia de datos tipados, y que `DataOutputStream` y `DataInputStream` son herramientas muy adecuadas para escribir y leer esa estructura.

        import java.io.DataInputStream
        import java.io.DataOutputStream
        import java.io.FileInputStream
        import java.io.FileOutputStream
        import java.nio.file.Files
        import java.nio.file.Paths

        fun main() {
            val ruta = Paths.get("documentos/binario.dat")
            Files.createDirectories(ruta.parent)

            // Escritura binaria
            val out= DataOutputStream(Files.newOutputStream(ruta)).use { out -> //devuelve OutputStream
            out.writeInt(42)         // int (4 bytes)
            out.writeDouble(3.1416)  // double (8 bytes)
            out.writeUTF("K")       // char (2 bytes)
            }

            println("✅ Fichero binario escrito con DataOutputStream (sin lambda).")

            // Lectura binaria
            val fis = FileInputStream(ruta.toFile())
            val input = DataInputStream(fis)
            val entero = input.readInt()
            val decimal = input.readDouble()
            val caracter = input.readUTF()
            input.close()
            fis.close()

            println("📄 Contenido leído:")
            println("  Int: $entero")
            println("  Double: $decimal")
            println("  Char: $caracter")
        }

**BufferedInputStream/BufferedOutputStream**{.azul}

Cuando se trabaja con ficheros binarios estructurados, normalmente estos métodos se combinan con DataInputStream y DataOutputStream,

 🖥️ **Ejemplo_binario_estructurado_buffer.kt**: Lectura y escritura en ficheros binarios utlizando búfer (con tipos primitivos).       

Este ejemplo muestra cómo mejorar la lectura y escritura de un fichero binario estructurado utilizando **flujos con búfer**. Para ello, `BufferedOutputStream` y `BufferedInputStream` se combinan con `DataOutputStream` y `DataInputStream`, consiguiendo una mayor eficiencia al reducir el número de accesos al disco.

- Primero se define la ruta del archivo y se crea la carpeta contenedora si no existe.
- Después se abre un `DataOutputStream` sobre un `BufferedOutputStream`, lo que permite escribir datos primitivos en formato binario utilizando un búfer de memoria.
- A continuación se escriben varios valores de distintos tipos, como un entero, un número decimal y una cadena, almacenándolos de forma secuencial en el fichero.
- Luego se abre un `DataInputStream` sobre un `BufferedInputStream` para leer el contenido del fichero de forma eficiente.
- La lectura debe realizarse en el mismo orden en que se escribieron los datos; de lo contrario, los valores recuperados serán incorrectos o se producirá una excepción.
- Finalmente, los datos leídos se muestran por pantalla para comprobar que la información recuperada coincide con la información almacenada.

La finalidad del ejercicio es comprender que los flujos con búfer no modifican la estructura del fichero binario ni la forma de acceder a los datos, sino que **mejoran el rendimiento** al minimizar los accesos físicos al dispositivo de almacenamiento. Por ello, es habitual combinar `BufferedInputStream` y `BufferedOutputStream` con `DataInputStream` y `DataOutputStream` cuando se trabaja con ficheros binarios estructurados.


        import java.io.BufferedInputStream
        import java.io.BufferedOutputStream
        import java.io.DataInputStream
        import java.io.DataOutputStream
        import java.nio.file.Files
        import java.nio.file.Paths

        fun main() {

            val ruta = Paths.get("documentos/binario.dat")
            Files.createDirectories(ruta.parent)

            // Escritura binaria utilizando un búfer
            DataOutputStream(
                BufferedOutputStream(
                    Files.newOutputStream(ruta)
                )
            ).use { out ->

                out.writeInt(42)
                out.writeDouble(3.1416)
                out.writeUTF("K")
            }

            println("✅ Fichero binario escrito correctamente.")

            // Lectura binaria utilizando un búfer
            DataInputStream(
                BufferedInputStream(
                    Files.newInputStream(ruta)
                )
            ).use { input ->

                val entero = input.readInt()
                val decimal = input.readDouble()
                val caracter = input.readUTF()

                println("📄 Contenido leído:")
                println("Int: $entero")
                println("Double: $decimal")
                println("Cadena: $caracter")
            }
        }

**FileChannel y ByteBuffer**{.azul}       

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

- Primero se define la ruta del fichero binario donde se almacenarán los datos.
- Después se abre un `FileChannel` en modo escritura, creando el fichero si no existe y sobrescribiendo su contenido si ya estaba creado.
- A continuación se reserva un `ByteBuffer` con el tamaño necesario para almacenar un entero y un número decimal (`Int.SIZE_BYTES + Double.SIZE_BYTES`).
- Los valores se escriben en el búfer mediante los métodos `putInt()` y `putDouble()`.
- Antes de escribir el contenido en el fichero se invoca el método `flip()`, que prepara el búfer para pasar del modo escritura al modo lectura.
- El contenido del búfer se escribe en el fichero utilizando el método `write()` del canal.
- Posteriormente se abre un nuevo `FileChannel` en modo lectura y se reserva otro `ByteBuffer` con el mismo tamaño.
- Los datos del fichero se leen en el búfer mediante `read()`, y de nuevo se utiliza `flip()` para preparar el búfer para su lectura.
- Finalmente, los valores se recuperan utilizando los métodos `getInt()` y `getDouble()` (o las propiedades equivalentes `buffer.int` y `buffer.double`) y se muestran por pantalla.

La finalidad del ejercicio es comprender cómo `FileChannel` y `ByteBuffer` permiten trabajar directamente con los bytes de un fichero binario estructurado. Aunque requieren una gestión más explícita del búfer que `DataInputStream` y `DataOutputStream`, ofrecen un mayor control sobre la lectura y escritura de datos y constituyen la base de la API NIO para aplicaciones que necesitan un acceso más eficiente o flexible a los ficheros.



        import java.nio.ByteBuffer
        import java.nio.channels.FileChannel
        import java.nio.file.Path
        import java.nio.file.StandardOpenOption

        fun main() {
            val ruta = Path.of("documentos/registro.bin")

            // Escritura: Int + Double
            FileChannel.open(
                ruta,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
            ).use { canal ->
                val buffer = ByteBuffer.allocate(Int.SIZE_BYTES + Double.SIZE_BYTES)
                buffer.putInt(42)
                buffer.putDouble(19.95)
                buffer.flip()
                canal.write(buffer)
            }

            // Lectura
            FileChannel.open(ruta, StandardOpenOption.READ).use { canal ->
                val buffer = ByteBuffer.allocate(Int.SIZE_BYTES + Double.SIZE_BYTES)
                canal.read(buffer)
                buffer.flip()
                val id = buffer.int
                val precio = buffer.double
                println("id=$id, precio=$precio")
            }
        }

!!!question "🧠 ¿Cuándo utilizar cada método?"
    Si tienes que decidir entre `DataInputStream`/`DataOutputStream` y `ByteBuffer`/`FileChannel`, ¿en qué casos conviene usar cada opción?

    ??? success "Ver respuesta"
        | Método | ¿Cuándo utilizarlo? |
        |--------|----------------------|
        | `DataInputStream` / `DataOutputStream` | Cuando el fichero contiene registros formados por tipos de datos (`Int`, `Double`, `Boolean`, `UTF`, etc.) y el acceso es secuencial. Normalmente se combinan con `BufferedInputStream` y `BufferedOutputStream`. |
        | `ByteBuffer` / `FileChannel` | Cuando se necesita mayor control sobre la lectura y escritura de datos, acceso aleatorio al fichero o mejor rendimiento mediante la API NIO. |