// Añade las importaciones necesarias para trabajar con:
//
// - JSON.
// - XML.
// - Lectura del fichero de configuración.


import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

// ==========================================================
// Reto 1
// ==========================================================

// Crea las clases de datos que representarán:
//
// - La configuración.
// - La información de cada archivo.
// - La estructura necesaria para generar el XML.


//data class Configuracion(
    //***aquí va el código de data class***
//)

//data class ArchivoInfo(
    //***aquí va el código de data class***
//)

//data class Reporte(
    //***aquí va el código de data class***
//)


// ==========================================================
// Reto 2
// ==========================================================
// Implementa una función que:
//
// - Busque el fichero config.json.
// - Si existe, lea su contenido.
// - Si no existe, permita utilizar la configuración por defecto.

//fun cargarConfiguracion(): Configuracion? {

    //***aquí va el código de la función***
//}



fun main() {

// ==========================================================
// Reto 3
// ==========================================================
// Sustituye el directorio Home por el directorio indicado
// en el fichero de configuración.
//
// Además, incorpora la posibilidad de mostrar
// o no los archivos ocultos.

    val home = Paths.get(System.getProperty("user.home"))

    var opcion: Int

        do {

            mostrarHome(home)


            // ==========================================================
            // Reto 4
            // ==========================================================
            // Añade una nueva opción al menú que permita
            // exportar un reporte del directorio actual.

            println(
                """
            
            ===== MENÚ =====
            1. Crear directorio
            2. Eliminar directorio o fichero
            3. Ver contenido recursivo
            4. Crear archivo de texto
            5. Leer archivo de texto
            6. Encriptar archivo
            7. Copiar archivo
            0. Salir
            Selecciona una opción:
            """.trimIndent()
            )

            opcion = readln().toInt()

            when (opcion) {

                1 -> crearDirectorio(home)
                2 -> eliminar(home)
                3 -> verRecursivo(home)
                4 -> crearTexto(home)
                5 -> leerTexto(home)
                6 -> encriptar(home)
                7 -> copiarArchivo(home)
                }

        } while (opcion != 0)

    }

// ==========================================================
// Reto 5
// ==========================================================
// Modifica esta función para que pueda mostrar
// u ocultar los archivos ocultos según el valor
// indicado en la configuración.

fun mostrarHome(home: Path) {

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

// ==========================================================
// Reto 6
// ==========================================================
// Implementa una función que coordine todo el proceso:
//
// 1. Obtener la información del directorio.
// 2. Generar un CSV.
// 3. Leer nuevamente el CSV.
// 4. Generar un JSON.
// 5. Generar un XML.

//fun exportarReporte(
        //***código de la función***
//}

// ==========================================================
// Reto 7
// ==========================================================
// Implementa una función que obtenga la información
// de los archivos del directorio y la almacene en memoria.

//fun obtenerArchivos(
    //***aquí va el código de la función***
//}

// ==========================================================
// Reto 8
// ==========================================================
// Implementa una función que lea el fichero CSV.
//fun leerCSV(
    //***aquí va el código de la función***
//}

// ==========================================================
// Reto 9
// ==========================================================
// Implementa una función que genere el fichero CSV.

//fun exportarCSV(
    //***aquí va el código de la función***

//}

// ==========================================================
// Reto 10
// ==========================================================
// Implementa una función que genere el fichero JSON
// utilizando la información obtenida del CSV.

//fun exportarJSON(
    //***aquí va el código de la función***

//}

// ==========================================================
// Reto 11
// ==========================================================
// Implementa una función que genere el fichero XML
// utilizando la información obtenida del CSV.

//fun exportarXML(
    //***aquí va el código de la función***

//}