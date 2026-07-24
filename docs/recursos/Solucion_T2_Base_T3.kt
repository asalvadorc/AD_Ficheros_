import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    var currentPath = Paths.get(System.getProperty("user.home"))

    while (true) {
        println("\n========================================")
        println("📁 DIRECTORIO ACTUAL: $currentPath")
        println("========================================")
        
        try {
            // Mostrar los primeros 5 elementos visibles
            val paths = Files.list(currentPath).filter { !it.fileName.toString().startsWith(".") }.limit(5).toList()
            paths.forEachIndexed { index, path ->
                val attrs = Files.readAttributes(path, BasicFileAttributes::class.java)
                val tipo = if (attrs.isDirectory) "[DIR]" else "[FILE]"
                val permisos = listOfNotNull(
                    if (Files.isReadable(path)) "r" else null,
                    if (Files.isWritable(path)) "w" else null
                ).joinToString("")
                val size = if (attrs.isRegularFile) "${attrs.size()} bytes" else ""
                
                println("  - $tipo ${path.fileName} [$permisos] $size | Creado: ${attrs.creationTime()}")
            }
            
            // Mostrar información del sistema de archivos
            val fileStore = Files.getFileStore(currentPath)
            println("\n💾 Info del Sistema (${fileStore.type()}): Libre ${fileStore.usableSpace / (1024*1024)} MB / Total ${fileStore.totalSpace / (1024*1024)} MB")

            println("\n📋 MENÚ DE ACCIONES:")
            println("1- Crear un directorio")
            println("2- Eliminar un directorio o fichero")
            println("3- Ver contenido recursivo de un directorio")
            println("4- Crear un archivo de texto")
            println("5- Leer un archivo de texto")
            println("6- Encriptar/Ocultar archivo (Texto a Binario)")
            println("7- Copiar un archivo a otra ruta")
            println("0- Salir")
            print("\nElige una opción: ")

            when (scanner.nextLine()) {
                "1" -> {
                    print("Nombre del nuevo directorio: ")
                    val dirName = scanner.nextLine()
                    Files.createDirectory(currentPath.resolve(dirName))
                    println("✅ Directorio creado.")
                }
                "2" -> {
                    print("Nombre a eliminar: ")
                    val target = scanner.nextLine()
                    val targetPath = currentPath.resolve(target)
                    Files.deleteIfExists(targetPath)
                    println("✅ Eliminado (si existía y estaba vacío).")
                }
                "3" -> {
                    print("Nombre del subdirectorio a explorar (deja en blanco para el actual): ")
                    val target = scanner.nextLine()
                    val explorePath = if (target.isBlank()) currentPath else currentPath.resolve(target)
                    if (Files.isDirectory(explorePath)) {
                        Files.walk(explorePath).forEach { println(it) }
                    } else {
                        println("❌ No es un directorio válido.")
                    }
                }
                "4" -> {
                    print("Nombre del nuevo archivo de texto: ")
                    val fileName = scanner.nextLine()
                    val filePath = currentPath.resolve(fileName)
                    Files.newBufferedWriter(filePath).use { writer ->
                        println("Escribe el contenido (escribe 'FIN' en una nueva línea para terminar):")
                        while(true) {
                            val line = scanner.nextLine()
                            if (line == "FIN") break
                            writer.write(line)
                            writer.newLine()
                        }
                    }
                    println("✅ Archivo creado.")
                }
                "5" -> {
                    print("Nombre del archivo de texto a leer: ")
                    val fileName = scanner.nextLine()
                    val filePath = currentPath.resolve(fileName)
                    if (Files.isRegularFile(filePath)) {
                        println("\n--- Contenido de $fileName ---")
                        Files.newBufferedReader(filePath).use { reader ->
                            reader.forEachLine { println(it) }
                        }
                        println("-------------------------------")
                    } else {
                        println("❌ El archivo no existe.")
                    }
                }
                "6" -> {
                    print("Nombre del archivo de texto a ocultar: ")
                    val source = scanner.nextLine()
                    print("Nombre del nuevo archivo binario (.bin): ")
                    val target = scanner.nextLine()
                    
                    val sourcePath = currentPath.resolve(source)
                    val targetPath = currentPath.resolve(target)
                    
                    if (Files.isRegularFile(sourcePath)) {
                        val bytes = Files.readAllBytes(sourcePath)
                        // Simple encriptación: sumar 1 a cada byte
                        val encrypted = ByteArray(bytes.size) { i -> (bytes[i] + 1).toByte() }
                        Files.write(targetPath, encrypted)
                        println("✅ Archivo encriptado a binario.")
                    } else {
                        println("❌ El archivo original no existe.")
                    }
                }
                "7" -> {
                    print("Nombre del archivo a copiar: ")
                    val source = scanner.nextLine()
                    print("Ruta o nombre de destino: ")
                    val target = scanner.nextLine()
                    
                    val sourcePath = currentPath.resolve(source)
                    val targetPath = Paths.get(target).let { if (it.isAbsolute) it else currentPath.resolve(it) }
                    
                    if (Files.exists(sourcePath)) {
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
                        println("✅ Archivo copiado.")
                    } else {
                        println("❌ El archivo origen no existe.")
                    }
                }
                "0" -> {
                    println("👋 ¡Hasta pronto!")
                    return
                }
                else -> println("❌ Opción no válida.")
            }
        } catch (e: Exception) {
            println("❌ Error: ${e.message}")
        }
    }
}
