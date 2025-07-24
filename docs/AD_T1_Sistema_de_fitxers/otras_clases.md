# FileSystem, BasicFileAttributes y FileStore
 
En la biblioteca **java.nio** podemos encontrar otras clases que complementan y amplían lo que se puede hacer con **java.nio.file.Path**.

## FileSystem

El concepto de **FileSystem** define un **sistema de ficheros completo**. Mientras que por otro lado el concepto de **Path** hace referencia a un **directorio, fichero o link** que tengamos dentro de nuestro sistema de ficheros. 

| Método                 | Descripción                                                                 |
|------------------------|------------------------------------------------------------------------------|
| getSeparator()       | Devuelve el separador de nombres de ruta (por ejemplo, / o \ \).         |
| getRootDirectories() | Devuelve un iterable con los directorios raíz del sistema de archivos.     |
| getFileStores()      | Devuelve un iterable con las particiones o volúmenes montados.             |
| getPath(...)         | Crea una instancia de Path a partir de una cadena de texto.              |
| provider()           | Devuelve el proveedor de sistema de archivos asociado (por ejemplo, UnixFileSystemProvider). |
| getDefault()          | Devuelve el sistema de ficheros por defecto del entorno en ejecución.|

---

Esto:

    val fileSystem = FileSystems.getDefault()
    val path = fileSystem.getPath("C:\\Users\\alumno\\documento.txt")

Es equivalente a usar:

    val path = Paths.get("C:\\Users\\alumno\\documento.txt")

Pero usando FileSystems.getDefault() puedes:

- Cambiar de sistema de ficheros si lo necesitas (por ejemplo, ZIP o virtuales).

- Obtener características del sistema.

🖥️ **Ejemplo_FileSystem.kt**: obtener el nombre de un fichero así como la carpeta padre en la que se encuentra ubicado.

        import java.nio.file.FileSystems
        import java.nio.file.Path

        fun main() {
            val sistemaFicheros = FileSystems.getDefault()
            val rutaFichero: Path = sistemaFicheros.getPath("documentos/destino/ejemplo3.txt")

            println(rutaFichero.fileName)
            println(rutaFichero.parent.fileName)

            val rutaDirectorio: Path = sistemaFicheros.getPath("documentos/destino")
            val it = rutaDirectorio.iterator()

            while (it.hasNext()) {
                println(it.next().fileName)
            }
        }


## BasicFileAttributes  

BasicFileAttributes permite obtener **información detallada sobre archivos y directorios**, como fecha de creación, tamaño, etc.


|Método	|Descripción|
|--------|-----------|
|creationTime()	|Devuelve la fecha de creación.|
|lastModifiedTime()	|Última modificación del archivo.|
|size()	|Tamaño en bytes.|
|isDirectory()	|Verifica si es un directorio.|
|isRegularFile()	|Verifica si es un archivo normal.|

🖥️ **Ejemplo_BasicFileAttributes.kt**:  leer los atributos básicos de un archivo o directorio.

    import java.nio.file.Files
    import java.nio.file.Paths
    import java.nio.file.attribute.BasicFileAttributes

    fun main() {
        val path = Paths.get("documentos")

        if (Files.exists(path)) {
            val attr = Files.readAttributes(path, BasicFileAttributes::class.java)
            println("Creación: ${attr.creationTime()}")
            println("Último acceso: ${attr.lastAccessTime()}")
            println("Es un directorio: ${attr.isDirectory}")
            println("Tamaño del archivo: ${attr.size()} bytes")
        }
    }

## FileStore (Información del Disco)

FileStore permite obtener **información sobre el sistema de archivos**, como el espacio disponible.

Método	|Descripción|
|--------|-----------|
|name()|	Nombre del volumen|
|type()|	Tipo de sistema de archivos|
|getTotalSpace()|	Espacio total|
|getUsableSpace()|	Espacio disponible para el usuario|
|supportsFileAttributeView()|	Si soporta atributos como POSIX o DOS|

🖥️ **Ejemplo_FileStore.kt**: obtener información del almacenamiento físico.

    import java.nio.file.FileStore
    import java.nio.file.Files
    import java.nio.file.Paths

    fun main() {
        val path = Paths.get("/")
        val fileStore: FileStore = Files.getFileStore(path)

        println("Sistema de archivos: ${fileStore.type()}")
        println("Espacio total: ${fileStore.totalSpace / (1024 * 1024)} MB")
        println("Espacio disponible: ${fileStore.usableSpace / (1024 * 1024)} MB")
    }

!!!Note "Nota"
    Funciona en Windows y Linux, aunque Files.getFileStore(Paths.get("/")) podría requerir ajustes en Windows para seleccionar una unidad específica (C:\, D:\, etc.).    



