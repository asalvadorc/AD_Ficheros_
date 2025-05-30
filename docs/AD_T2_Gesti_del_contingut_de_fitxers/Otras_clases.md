# 🔹 Otros tipos de acceso

Además del acceso a ficheros de texto o binarios simples, en programación también es común trabajar con otros tipos de ficheros más especializados, según cómo se estructuran y acceden los datos. Entre ellos destacan:

- **Binarios estructurados**: contienen datos codificados en formato binario siguiendo una estructura fija y conocida (por ejemplo, registros con campos de longitud fija).
 🧪 Ejemplo típico: guardar una lista de personas con nombre (20 bytes) + edad (4 bytes).

- **Acceso aleatorio**: permite leer y escribir en cualquier posición de un fichero, sin necesidad de procesar todo el contenido desde el principio.  
 🧪 Ejemplo típico: actualizar solo el campo "saldo" de un cliente en un fichero binario de cuentas.

- **Ficheros de imagen**:representan datos visuales y requieren herramientas específicas para interpretarlos como imágenes, no solo como bytes.  
  🧪 Ejemplo típico: invertir los colores de una imagen .png o escribir texto sobre ella.

## 🔹 Binarios estructurados y acceso aleatorio 

Las clases **FileChannel**, **ByteBuffer** y **StandardOpenOption** forman parte de la API **java.nio** y se utilizan juntas para realizar lectura y escritura de archivos **binarios estructurados** y en el **acceso aleatorio a ficheros**.


**FileChannel**{.azul}

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


**ByteBuffer**{.azul}

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

**StandardOpenOption**{.azul}

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


<!--
🧪 **Ejemplo:** escribir y leer una frase en un archivo de texto (mensaje.txt)

        import java.nio.file.*
        import java.nio.ByteBuffer
        import java.nio.channels.FileChannel

        fun main() {
            val ruta = Paths.get("mensaje.txt")
            val mensaje = "Hola mundo desde ByteBuffer"

            // ✏️ Escritura con FileChannel y ByteBuffer
            FileChannel.open(ruta, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { canal ->
                val buffer = ByteBuffer.wrap(mensaje.toByteArray()) // para convertir una cadena en un buffer de bytes.
                canal.write(buffer)
            }

            // 📖 Lectura con FileChannel y ByteBuffer
            FileChannel.open(ruta, StandardOpenOption.READ).use { canal ->        
                val buffer = ByteBuffer.allocate(1024) // para preparar espacio para leer
                canal.read(buffer)
                buffer.flip()  // Cambia a modo lectura
                //Lee los datos del archivo al buffer y los imprime carácter a carácter.
                while (buffer.hasRemaining()) {
                    print(buffer.get().toInt().toChar())
                }
            }
        }
-->
### 🔹 Ficheros binarios estructurados

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

### 🔹 Acceso aleatorio a ficheros

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
                      

## 🔹 Ficheros de imagen

Los ficheros de imagen contienen datos que representan gráficamente una imagen visual (fotografías, ilustraciones, iconos, etc.). A diferencia de los ficheros de texto o binarios crudos, estos archivos tienen estructura interna que depende del formato (como .png, .jpg, .bmp, etc.).


📦 Formatos más comunes

- **.jpg**:	Comprimido con pérdida, ideal para fotos
- **.png**:	Comprimido sin pérdida, soporta transparencia
- **.bmp**:	Sin compresión, ocupa más espacio
- **.gif**:	Admite animaciones simples, limitada a 256 colores
  
En la plataforma Java (y por tanto en Kotlin), **el manejo de imágenes** se hace generalmente usando:

- **ImageIO**: para leer y escribir imágenes
- **BufferedImage**: para acceder y modificar píxeles


**Ejemplo_generar_imagen.kt:** genera una imagen de ejemplo.

    import java.awt.Color
    import java.awt.image.BufferedImage
    import java.io.File
    import javax.imageio.ImageIO

    fun main() {
        val ancho = 200
        val alto = 100
        val imagen = BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB)

        // Rellenar la imagen con colores
        for (x in 0 until ancho) {
            for (y in 0 until alto) {
                val rojo = (x * 255) / ancho
                val verde = (y * 255) / alto
                val azul = 128
                val color = Color(rojo, verde, azul)
                imagen.setRGB(x, y, color.rgb)
            }
        }

        // Guardar la imagen
        val archivo = File("documentos/imagen_generada.png")
        ImageIO.write(imagen, "png", archivo)
        println("✅ Imagen generada correctamente: ${archivo.absolutePath}")
    }

**Ejemplo_invertircolores_imagen.kt:** invierte los colores de la imagen generada en el ejemplo atenerior.

    import java.awt.Color
    import java.awt.image.BufferedImage
    import java.io.File
    import javax.imageio.ImageIO

    fun main() {
        val archivoEntrada = File("documentos/imagen_generada.png")
        val archivoSalida = File("documentos/imagen_salida.png")

        // Leer imagen original
        val imagen: BufferedImage = ImageIO.read(archivoEntrada)

        // Recorrer todos los píxeles
        for (x in 0 until imagen.width) {
            for (y in 0 until imagen.height) {
                val colorOriginal = Color(imagen.getRGB(x, y))
                val colorInvertido = Color(
                    255 - colorOriginal.red,
                    255 - colorOriginal.green,
                    255 - colorOriginal.blue
                )
                imagen.setRGB(x, y, colorInvertido.rgb)
            }
        }

        // Guardar imagen modificada
        ImageIO.write(imagen, "png", archivoSalida)
        println("✅ Imagen guardada como ${archivoSalida.name}")
    }


**Ejemplo_img_penyagolosa.kt:** Invierte los colores de una imagen (penyagolosa.png)

    import java.awt.Color
    import java.awt.image.BufferedImage
    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.StandardCopyOption
    import javax.imageio.ImageIO

    fun main() {
        val originalPath = Path.of("documentos/penyagolosa.png")
        val copiaPath = Path.of("documentos/penyagolosa_copia.png")
        val modificadaPath = Path.of("documentos/penyagolosa_modificada.png")

        // 1. Comprobar si la imagen existe
        if (!Files.exists(originalPath)) {
            println("No se encuentra la imagen original: $originalPath")
            return
        }

        // 2. Copiar la imagen con java.nio
        Files.copy(originalPath, copiaPath, StandardCopyOption.REPLACE_EXISTING)
        println("Imagen copiada a: $copiaPath")

        // 3. Leer la imagen como BufferedImage
        val imagen: BufferedImage = ImageIO.read(copiaPath.toFile())

        // 4. Invertir colores
        for (x in 0 until imagen.width) {
            for (y in 0 until imagen.height) {
                val color = Color(imagen.getRGB(x, y))
                val invertido = Color(255 - color.red, 255 - color.green, 255 - color.blue)
                imagen.setRGB(x, y, invertido.rgb)
            }
        }

        // 5. Guardar la imagen modificada
        ImageIO.write(imagen, "png", modificadaPath.toFile())
        println("Imagen modificada guardada como: $modificadaPath")
    }


