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
