# Ficheros de acceso aleatorio

Hasta el momento todos los accesos que hemos hecho a los archivos, tanto binarios como de carácter, han sido secuenciales. Esto significa que siempre empezamos por el principio del archivo hasta que llegamos a la información que queremos, o en la mayor parte de los casos hasta el final de archivo.

Pero, ¿y si queremos únicamente una determinada información? Afortunadamente hay otra forma de acceder, otro tipo de acceso. Se llama acceso directo o aleatorio, porque permitirá ir directamente a una posición determinada del archivo.

Cuando se necesita mayor control, eficiencia y rendimiento en el acceso a ficheros, especialmente en operaciones binarias o de acceso aleatorio, el enfoque tradicional con la clase **RandomAccessFile** de **Java.io** puede quedarse corto. Para estos casos, Java ofrece una solución moderna a través del paquete **java.nio.file** combinado con **FileChannel** y **ByteBuffer**. 

Las clases **FileChannel**, **ByteBuffer** y **StandardOpenOption** forman parte de la API **java.nio** y se utilizan juntas para realizar lectura y escritura de archivos **binarios estructurados** y en el **acceso aleatorio a ficheros**.

**Métodos habituales de FileChannel para el acceso aleatorio a ficheros**{.azul}

| Método                   | Función principal                                         |
|--------------------------|-----------------------------------------------------------|
| position()             |  Devuelve la posición actual del puntero en el archivo     |
| position(long)         |  Establece una posición exacta para lectura/escritura      |
| truncate(long)         | Recorta o amplía el tamaño del archivo                    |
| size()                 |  Devuelve el tamaño total actual del archivo  




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

