import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes


fun main() {

    val homePath = Paths.get(System.getProperty("user.home"))

    var opcion: Int

    do {

        mostrarContenidoHome(homePath)
        mostrarInfoSistema(homePath)

        // ==========================================================
        // Reto 1
        // ==========================================================
        // Añade las nuevas opciones al menu

        println(
            """
            
             ===== MENÚ =====
            1. Crear directorio
            2. Eliminar directorio o fichero
            3. Ver contenido recursivo
            0. Salir
            Selecciona una opción:
            """.trimIndent()
        )

        opcion = readln().toInt()

        when (opcion) {

            1 -> crearDirectorio(homePath)
            2 -> eliminar(homePath)
            3 -> verRecursivo(homePath)


        }

    } while (opcion != 0)

}

fun mostrarContenidoHome(home: Path) {

    println("\nHOME: $home\n")

    val lista = Files.list(home).toList()

    var contador = 0

    for (archivo in lista) {

        if (!archivo.fileName.toString().startsWith(".")) {

            val atributos = Files.readAttributes(
                archivo,
                BasicFileAttributes::class.java
            )

            println("--------------------------------")
            println("Nombre: ${archivo.fileName}")

            if (atributos.isDirectory) {
                println("Tipo: Directorio")
            } else {
                println("Tipo: Archivo")
            }

            println("Tamaño: ${atributos.size()} bytes")
            println("Creado: ${atributos.creationTime()}")
            println("Modificado: ${atributos.lastModifiedTime()}")
            println("Legible: ${Files.isReadable(archivo)}")
            println("Escribible: ${Files.isWritable(archivo)}")

            contador++

            if (contador == 5) {
                break
            }
        }
    }

    val sistema = Files.getFileStore(home)

    println("\nInformación del sistema de archivos")
    println("Tipo: ${sistema.type()}")
    println("Espacio total: ${sistema.totalSpace} bytes")
    println("Espacio libre: ${sistema.usableSpace} bytes")
}

fun mostrarInfoSistema(homePath: Path) {
    val fileStore = Files.getFileStore(homePath)
    val fileSystem = FileSystems.getDefault()
    println("Información del sistema de archivos:")
    println("- Tipo: ${fileSystem.javaClass.simpleName}")
    println("- Total: ${fileStore.totalSpace} bytes")
    println("- Libre: ${fileStore.usableSpace} bytes\n")
}

fun crearDirectorio(home: Path) {

    try {

        print("Nombre del directorio: ")
        val nombre = readln()

        Files.createDirectory(home.resolve(nombre))

        println("Directorio creado correctamente.")

    } catch (e: Exception) {

        println("Error: ${e.message}")

    }

}

fun eliminar(home: Path) {

    try {

        print("Nombre del fichero o directorio: ")
        val nombre = readln()

        Files.delete(home.resolve(nombre))

        println("Eliminado correctamente.")

    } catch (e: Exception) {

        println("Error: ${e.message}")

    }

}

fun verRecursivo(home: Path) {

    try {

        print("Nombre del directorio: ")
        val ruta = home.resolve(readln())

        Files.walk(ruta).forEach {
            println(it)
        }

    } catch (e: Exception) {

        println("Error: ${e.message}")

    }

}

// ==========================================================
// Reto 2
// ==========================================================
// Implementa una función que cree un archivo de texto y permitirle escribir líneas de texto por consola.

fun crearTexto(home: Path) {

    try {

        print("Nombre del archivo: ")
        val fichero = home.resolve(readln())

        val escritor = Files.newBufferedWriter(fichero)

        println("Escribe líneas (FIN para terminar):")

        while (true) {

            val linea = readln()

            if (linea == "FIN") {
                break
            }

            escritor.write(linea)
            escritor.newLine()

        }

        escritor.close()

        println("Archivo creado correctamente.")

    } catch (e: Exception) {

        println("Error: ${e.message}")

    }

}
// ==========================================================
// Reto 3
// ==========================================================
// Implementa una función que lea un archivo de texto por consola.

fun leerTexto(home: Path) {

    try {

        print("Nombre del archivo: ")
        val fichero = home.resolve(readln())

        val lector = Files.newBufferedReader(fichero)

        var linea = lector.readLine()

        while (linea != null) {

            println(linea)

            linea = lector.readLine()

        }

        lector.close()

    } catch (e: Exception) {

        println("Error: ${e.message}")

    }

}
// ==========================================================
// Reto 4
// ==========================================================
// Implementa una función que ecripte un archivo de texto y lo guarda en un fichero binario

fun encriptar(home: Path) {

    try {

        print("Archivo origen: ")
        val origen = home.resolve(readln())

        val datos = Files.readAllBytes(origen)

        for (i in datos.indices) {
            datos[i] = (datos[i] + 1).toByte()
        }

        val destino = home.resolve("${origen.fileName}.bin")

        Files.write(destino, datos)

        println("Archivo binario creado correctamente.")

    } catch (e: Exception) {

        println("Error: ${e.message}")

    }

}
// ==========================================================
// Reto 5
// ==========================================================
// Implementa una función que copie un archivo en otro.


fun copiarArchivo(home: Path) {

    try {

        print("Archivo origen: ")
        val origen = home.resolve(readln())

        print("Archivo destino: ")
        val destino = home.resolve(readln())

        Files.copy(
            origen,
            destino,
            StandardCopyOption.REPLACE_EXISTING
        )

        println("Archivo copiado correctamente.")

    } catch (e: Exception) {

        println("Error: ${e.message}")

    }

}