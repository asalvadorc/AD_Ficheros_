## Acceso al sistema de ficheros. Java.nio 

Durante muchos años se ha utilizado la librería **java.io** para trabajar con ficheros en el mundo Java. Se trata de un **API** muy potente y flexible que nos permite realizar casi cualquier tipo de operación. Sin embargo es una API complicada de entender. **Java.nio** (New IO) es una nueva API disponible desde Java7 que nos permite mejorar el rendimiento, así como simplificar el manejo de muchas operaciones. 

**Java.nio** define interfaces y clases para que la máquina virtual Java tenga acceso a archivos, atributos de archivos y sistemas de archivos. Aunque dicho API comprende numerosas clases, solo existen unas pocas de ellas que sirven de puntos de entrada al API, lo que simplifica considerablemente su manejo.


La interfaz **java.nio.file.Path** representa un path, y las clases que implementen esta interfaz puede utilizarse para localizar ficheros en el sistema de ficheros. Nos permite manejar rutas al estilo GNU/Linux y rutas al estilo Windows dependiendo del SO en el que estemos trabajando.

La clase **java.nio.file.Files** es el otro punto de entrada a la librería de ficheros de Java. Es la que nos permite manejar ficheros reales del disco desde Java.


!!!Tip "Clases para la gesitón de ficheros"
    - **Paths**: Crea objetos Path desde cadenas de texto
    - **Path**: Representa rutas a archivos o directorios
    - **Files**: Permite operaciones sobre archivos usando Path


!!!warning "Ejemplos"
    Los siguientes ejemplos los crearemos en un proyecto nuevo, llamado **Ficheros**.

    En este proyecto crearemos tres paquetes (**sistema**, **contenido** y **formatos**) para organizar los diferentes ejemplos.

    ![Ref](new_project.png)|![Ref](paquetes.png)

    
    
!!!warning "Ejemplos"
    Los siguientes ejemplo se incluirán en el paquete **sistema**.  

### 🔹Paths

La clase **Paths** es una clase de utilidad que proporciona métodos estáticos para crear objetos **Path**, que luego puedes usar con métodos de **Files**.

     
|Método     |Descripción|
|-----------|-----------|
|- get(String first, String... mor e)|	Crea un objeto Path a partir de una o más cadenas.|
|- get(URI uri)|	Crea un Path desde un URI que debe ser del esquema file:///.  |      

!!!Note ""
    El uso de **Paths.get(...)** en Java (o Kotlin) no implica que el archivo o directorio exista. Este método simplemente crea una instancia de Path que representa una ruta en el sistema de archivos, pero no accede al disco ni verifica su existencia.

🖥️ **Ejemplo_get.kt**

        import java.nio.file.Path
        import java.nio.file.Paths

        fun main() {
            val path1: Path = Paths.get("documentos", "archivo.txt")
            val path2: Path = Paths.get("C:", "usuarios", "nombre", "archivo.txt")

            println("Ruta 1: $path1")
            println("Ruta 2: $path2")
        }


🖥️ **Ejemplo_uri.kt**

        import java.net.URI
        import java.nio.file.Path
        import java.nio.file.Paths

        fun main() {
            val uri = URI("file:///C:/usuarios/nombre/archivo.txt")
            val path: Path = Paths.get(uri)

            println("Ruta a partir de URI: $path")
        }


### 🔹Path

La clase **Path** Se utiliza junto con la clase **Files** para realizar operaciones como lectura, escritura, copia, o eliminación de archivos.  
La forma mas sencilla de construir un objeto que cumpla la interfaz **Path** es a partir de la clase **java.nio.file.Paths**, que tiene métodos estáticos que retornan objetos Path a partir de una representación tipo String del path deseado.  
Por supuesto, no es necesario que los ficheros existan de verdad en el disco duro para que se puedan crear los objetos Path correspondientes.

Un objeto Path puede representar una ruta **absoluta**:
    
    - val path = Paths.get("/home/usuario/archivo.txt")
o **relativa**:
    
    - val path = Paths.get("documentos/ejemplo.txt")
    println(path.toAbsolutePath())


 
Las **operaciones** y **métodos** principales que se pueden hacer con Path son:

- Recuperar partes de una ruta
- Eliminar redundancias de una ruta
- Convertir una ruta
- Unir dos rutas
- Crear una ruta relativa a otra dada
- Comparar dos rutas
  

|Método     |Métodos principales|
|-----------|---------------|
|startsWith(Path other)|	Comprueba si la ruta empieza con otra ruta dada.|
|endsWith(Path other)|	Comprueba si la ruta termina con otra ruta dada.|
|getParent()|	Devuelve el Path padre (directorio superior).|
|getRoot()|	Devuelve el componente raíz del Path (por ejemplo, / o C:\).|
|iterator()|	Permite recorrer los directorios y subdirectorios.|
|toString()|	Devuelve la representación de la ruta como un String.|
|toAbsolutePath()|	Devuelve la ruta absoluta de este Path.|

🖥️ **Ejemplo_Path.kt**

        import java.nio.file.Path
        import java.nio.file.Paths

        fun main() {
            val path: Path = Paths.get("documentos/ejemplo.txt")

            println("toString(): ${path}")
            println("toAbsolutePath(): ${path.toAbsolutePath()}")
            println("getFileName(): ${path.fileName}")
            println("getParent(): ${path.parent}")
            println("getRoot(): ${path.root}")

            val otroPath: Path = Paths.get("imagenes/foto.png")
            println("resolve(): ${path.resolve(otroPath)}")

            val relativo: Path = path.relativize(Paths.get("documentos/otroArchivo.txt"))
            println("relativize(): $relativo")

            val rutaNormalizada: Path = Paths.get("carpeta/../archivo.txt").normalize()
            println("normalize(): $rutaNormalizada")

            println("startsWith(\"documentos\"): ${path.startsWith("documentos")}")
            println("endsWith(\"ejemplo.txt\"): ${path.endsWith("ejemplo.txt")}")
        }
        


### 🔹Files

La clase **Files** es el otro punto de entrada a la librería de ficheros de Java. Es la que nos permite manejar ficheros reales del disco desde Java.  
Esta clase tiene métodos estáticos para el manejo de ficheros, los métodos de la clase **Files** trabajan sobre objetos **Path**.  

Las **operaciones** y **métodos** principales a realizar con Files son:

  - Verificación de existencia y accesibilidad
  - Borrar un archivo o directorio
  - Copiar un archivo o directorio
  - Mover un archivo o directorio


|Método     |Métodos principales|
|-----------|---------------|
|Verificación|	exists(), isDirectory(), isRegularFile(), isReadable()|
|Crear|	createFile(), createDirectory(), createDirectories()|
|Eliminar|	delete(), deleteIfExists()|
|Mover|	move(origen, destino)|
|Copiar|	copy(origen, destino)|
|Listar|	list(), walk()|
|Atributos|	size(), getLastModifiedTime(), getOwner(), getAttribute()|


🖥️ **Ejemplo_permisos.kt**: existencia y comprobación de permisos

        import java.nio.file.Path
        import java.nio.file.Paths
        import java.nio.file.Files

        fun main() {
            val path: Path = Paths.get("documentos/ejemplo.txt")

            println("path = $path")
            println("exists = ${Files.exists(path)}")
            println("readable = ${Files.isReadable(path)}")
            println("writable = ${Files.isWritable(path)}")
            println("executable = ${Files.isExecutable(path)}")
        }    

  
🖥️ **Ejemplo_creardirectorio.kt**: crear un directorio


        import java.nio.file.Path
        import java.nio.file.Paths
        import java.nio.file.Files
        import java.nio.file.FileAlreadyExistsException
        import java.io.IOException

        fun main() {
            val path: Path = Paths.get("documentos")

            try {
                val newDir = Files.createDirectory(path)
                println("Directorio creado en: $newDir")
            } catch (e: FileAlreadyExistsException) {
                println("El directorio ya existe: $path")
            } catch (e: IOException) {
                println("Error de entrada/salida: ${e.message}")
                e.printStackTrace()
            }
        }

🖥️ **Ejemplo_borrardirectorio.kt**: elimina un directorio

        import java.nio.file.Files
        import java.nio.file.Path
        import java.nio.file.Paths

        fun main() {
            val directorio: Path = Paths.get("c:/mi_directorio")

            // Si ya existe, lo eliminamos
            if (Files.exists(directorio)) {
                println("El directorio ya existe. Borrándolo...")
                Files.delete(directorio)
            }

        }


El método  **delete(Path)** borra el fichero o directorio o lanza una excepción si el borrado falla. El siguiente ejemplo muestra como capturar y gestionar las excepciones que pueden producirse en el borrado. Si el fichero o directorio no existe, la excepción que se produce es  **NoSuchFileException**. Los sucesivos **cath** permiten determinar por  que ha fallado el borrado:

        import java.nio.file.*
        import java.io.IOException

            fun main() {
                val path = Paths.get("c:/mi_directorio")
                try {
                    Files.delete(path)
                } catch (e: NoSuchFileException) {
                    System.err.printf("%s: no such file or directory%n", path)
                } catch (e: DirectoryNotEmptyException) {
                    System.err.printf("%s not empty%n", path)
                } catch (e: IOException) {
                    // Problemas de permisos u otros errores de E/S
                    System.err.println("Error: ${e.message}")
                }
            }


!!!Note ""
    El metodo **deleteIfExists(Path)** tambien borra el fichero o directorio, pero no lanza ningun error en caso de que el fichero o directorio no exista.



🖥️ **Ejemplo_copiardirectorio.kt**: copiar directorios

Se puede copiar un archivo o directorio usando el método copy(Path, Path, CopyOption...). La copia falla si el archivo de destino existe, a menos que se especifique la opción REPLACE_EXISTING. 

Se puede copiar directorios aunque, los archivos dentro del directorio no se copian, por lo que el nuevo directorio está vacío incluso cuando el directorio original contiene archivos.

       import java.io.IOException
        import java.nio.file.FileAlreadyExistsException
        import java.nio.file.Files
        import java.nio.file.Path
        import java.nio.file.Paths
        // import java.nio.file.StandardCopyOption  // si se desea sobrescribir

        fun main() {
            val sourcePath: Path = Paths.get("documentos")
            val destinationPath: Path = Paths.get("documentos/destino")

            try {
                Files.copy(sourcePath, destinationPath)
                // Para sobrescribir si ya existe, descomenta la siguiente línea:
                // Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING)

                println("Copia realizada con éxito.")
            } catch (e: FileAlreadyExistsException) {
                println("El fichero o directorio ya existe en el destino.")
            } catch (e: IOException) {
                println("Error al copiar: ${e.message}")
                e.printStackTrace()
            }
        }

🖥️ **Ejemplo_copiarficheros.kt**: copiar ficheros

        import java.io.IOException
        import java.nio.file.FileAlreadyExistsException
        import java.nio.file.Files
        import java.nio.file.Path
        import java.nio.file.Paths
        import java.nio.file.StandardCopyOption

        fun main() {
            val sourcePath: Path = Paths.get("documentos/ejemplo.txt")
            val destinationPath: Path = Paths.get("documentos/ejemplo_copia.txt")

            try {
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING)
                println("Archivo copiado correctamente a: $destinationPath")
            } catch (e: FileAlreadyExistsException) {
                println("El archivo destino ya existe.")
            } catch (e: IOException) {
                println(" Error al copiar el archivo: ${e.message}")
                e.printStackTrace()
            }
        }



🖥️ **Ejemplo_moverficheros.kt**: mover ficheros y directorios cambiando el nombre.

        import java.io.IOException
        import java.nio.file.FileAlreadyExistsException
        import java.nio.file.Files
        import java.nio.file.Path
        import java.nio.file.Paths
        import java.nio.file.StandardCopyOption

        fun main() {
            val sourcePath: Path = Paths.get("documentos/ejemplo.txt")
            val destinationPath: Path = Paths.get("documentos2/ejemplo2.txt")

            try {
                Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING)
                println("Archivo movido/renombrado correctamente a: $destinationPath")
            } catch (e: FileAlreadyExistsException) {
                println("El archivo destino ya existe.")
            } catch (e: IOException) {
                println("Error al mover el archivo: ${e.message}")
                e.printStackTrace()
            }
        }



El siguiente ejemplo recorre la estructura home en tu sistema, indicando los permisos de cada archivo y directorio: 

🖥️ **Ejemplo_SistemaFicheros.kt**

        import java.nio.file.*
        import java.nio.file.attribute.BasicFileAttributes
        import java.util.Scanner

        fun main() {
            val scanner = Scanner(System.`in`)
            var currentPath: Path = Paths.get(System.getProperty("user.home"))

            while (true) {
                println("\n Directorio actual: $currentPath")
                try {
                    val paths = Files.list(currentPath).toList()
                    paths.forEachIndexed { index, path ->
                        val attrs = Files.readAttributes(path, BasicFileAttributes::class.java)
                        val tipo = when {
                            attrs.isDirectory -> "[DIR]"
                            attrs.isRegularFile -> "[FILE]"
                            else -> "[OTRO]"
                        }

                        val permisos = listOfNotNull(
                            if (Files.isReadable(path)) "r" else null,
                            if (Files.isWritable(path)) "w" else null,
                            if (Files.isExecutable(path)) "x" else null
                        ).joinToString("")

                        val size = if (attrs.isRegularFile) "${attrs.size()} bytes" else ""

                        println("$index. $tipo ${path.fileName} [$permisos] $size")
                    }

                    println("\nOpciones:")
                    println(" - Número: acceder a subdirectorio")
                    println(" - `..`: subir al directorio padre")
                    println(" - `salir`: finalizar el programa")
                    print("Opción: ")

                    when (val input = scanner.nextLine()) {
                        "salir" -> {
                            println("Saliendo del explorador.")
                            return
                        }
                        ".." -> {
                            currentPath = currentPath.parent ?: currentPath
                        }
                        else -> {
                            val index = input.toIntOrNull()
                            if (index != null && index in paths.indices) {
                                val selected = paths[index]
                                if (Files.isDirectory(selected)) {
                                    currentPath = selected
                                } else {
                                    println("No es un directorio.")
                                }
                            } else {
                                println("Entrada no válida.")
                            }
                        }
                    }

                } catch (e: Exception) {
                    println("Error al acceder al directorio: ${e.message}")
                }
            }
        }




### 🔹FileSystem

En la biblioteca **java.nio** podemos encontrar otras clases que complementan y amplían lo que se puede hacer con **java.nio.file.Path**.

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


### 🔹BasicFileAttributes  

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

### 🔹FileStore

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



        
Llicenciat sota la  [Llicència Creative Commons Reconeixement NoComercial
CompartirIgual 2.5](http://creativecommons.org/licenses/by-nc-sa/2.5/)