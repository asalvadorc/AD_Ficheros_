## Acceso al sistema de ficheros

Hemos usado durante muchos años **java.io** para trabajar con ficheros en el mundo Java. Se trata de un **API** muy potente y flexible que nos permite realizar casi cualquier tipo de operación. Sin embargo es una API complicada de entender. Java **NIO** (New IO) es una nueva API disponible desde Java7 que nos permite mejorar el rendimiento, así como simplificar el manejo de muchas operaciones. 

**Java.nio** define interfaces y clases para que la máquina virtual Java tenga acceso a archivos, atributos de archivos y sistemas de archivos. Aunque dicho API comprende numerosas clases, solo existen unas pocas de ellas que sirven de puntos de entrada al API, lo que simplifica considerablemente su manejo.


La interfaz **java.nio.file.Path** representa un path, y las clases que implementen esta interfaz puede utilizarse para localizar ficheros en el sistema de ficheros. Nos permite manejar rutas al estilo GNU/Linux y rutas al estilo Windows dependiendo del SO en el que estemos trabajando.

La clase **java.nio.file.Files** es el otro punto de entrada a la librería de ficheros de Java. Es la que nos permite manejar ficheros reales del disco desde Java.

!!!Tip "Claes para la gesitón de ficheros"
    - **Paths**: Crea objetos Path desde cadenas de texto
    - **Path**: Representa rutas a archivos o directorios
    - **Files**: Permite operaciones sobre archivos usando Path

### **Paths**{.azul}

La clase **Paths** es una clase de utilidad que proporciona métodos estáticos para crear objetos Path.
Paths no representa una ruta, solo sirve para construir instancias de Path de forma sencilla. Paths tiene un único propósito, devolver un objeto Path, que luego puedes usar con métodos de Files.

     
**Métodos más importantes de Paths:**

|Método     |Descripción|
|-----------|-----------|
|get(String first, String... mor e)|	Crea un objeto Path a partir de una o más cadenas.|
|get(URI uri)|	Crea un Path desde un URI que debe ser del esquema file:///.  |      


**Ejemplo_get.kt**

        import java.nio.file.Path
        import java.nio.file.Paths

        fun main() {
            val path1: Path = Paths.get("documentos", "archivo.txt")
            val path2: Path = Paths.get("C:", "usuarios", "nombre", "archivo.txt")

            println("Ruta 1: $path1")
            println("Ruta 2: $path2")
        }


**Ejemplo_uri.kt**

        import java.net.URI
        import java.nio.file.Path
        import java.nio.file.Paths

        fun main() {
            val uri = URI("file:///C:/usuarios/nombre/archivo.txt")
            val path: Path = Paths.get(uri)

            println("Ruta a partir de URI: $path")
        }


### **Path**{.azul}

La clase **Path** Se utiliza junto con la clase **Files** para realizar operaciones como lectura, escritura, copia, o eliminación de archivos.  
La forma mas sencilla de construir un objeto que cumpla la interfaz Path es a partir de la clase java.nio.file.Paths, que tiene métodos estáticos que retornan objetos Path a partir de una representación tipo String del path deseado.  
Por supuesto, no es necesario que los ficheros existan de verdad en el disco duro para que se puedan crear los objetos Path correspondientes

Un objeto Path puede representar una ruta **absoluta** (/home/usuario/archivo.txt) o **relativa** (documentos/archivo.txt).

**ejemplo:** Devuelve la ruta relativa 

    val path = Paths.get("documentos/ejemplo.txt")
    println(path.toAbsolutePath())

**ejemplo:** Devuelve la ruta absoluta

    val path = Paths.get("/home/usuario/archivo.txt")

**Operaciones con Path**

- Recuperar partes de una ruta
- Eliminar redundancias de una ruta
- Convertir una ruta
- Unir dos rutas
- Crear una ruta relativa a otra dada
- Comparar dos rutas
  
**Principales métodos de Path:**

|Método     |Métodos principales|
|-----------|---------------|
|startsWith(Path other)|	Comprueba si la ruta empieza con otra ruta dada.|
|endsWith(Path other)|	Comprueba si la ruta termina con otra ruta dada.|
|getParent()|	Devuelve el Path padre (directorio superior).|
|getRoot()|	Devuelve el componente raíz del Path (por ejemplo, / o C:\).|
|iterator()|	Permite recorrer los directorios y subdirectorios.|
|toString()|	Devuelve la representación de la ruta como un String.|
|toAbsolutePath()|	Devuelve la ruta absoluta de este Path.|

**Ejemplo_Path.kt**

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
        


### **Files**{.azul}

La clase **Files** es el otro punto de entrada a la librería de ficheros de Java. Es la que nos permite manejar ficheros reales del disco desde Java.  
Esta clase tiene métodos estáticos para el manejo de ficheros, los métodos de la clase **Files** trabajan sobre objetos **Path**.  
Las operaciones principales a realizar con archivos y directorios son:

  - Verificación de existencia y accesibilidad
  - Borrar un archivo o directorio
  - Copiar un archivo o directorio
  - Mover un archivo o directorio

**Principales métodos de Files:**

|Método     |Métodos principales|
|-----------|---------------|
|Verificación|	exists(), isDirectory(), isRegularFile(), isReadable()|
|Crear|	createFile(), createDirectory(), createDirectories()|
|Eliminar|	delete(), deleteIfExists()|
|Mover|	move(origen, destino)|
|Copiar|	copy(origen, destino)|
|Listar|	list(), walk()|
|Atributos|	size(), getLastModifiedTime(), getOwner(), getAttribute()|


**Ejemplo_permisos.kt**: existencia y comprobación de permisos

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

  
**Ejemplo_directorios.kt**: creación y borrado de directorios

        import java.nio.file.Path
        import java.nio.file.Paths
        import java.nio.file.Files
        import java.io.IOException

        fun main() {
            val path: Path = Paths.get("documentos/ejemplo.txt")

            try {
                if (Files.exists(path)) {
                    Files.delete(path)
                    println("Archivo eliminado: $path")
                } else {
                    Files.createFile(path)
                    println("Archivo creado: $path")
                }
            } catch (e: IOException) {
                System.err.println("Error: ${e.message}")
                System.exit(1)
            }
        }

El método  **delete(Path)** borra el fichero o directorio o lanza una excepción si el borrado falla. El siguiente ejemplo muestra como capturar y gestionar las excepciones que pueden producirse en el borrado. Si el fichero o directorio no existe, la excepción que se produce es  **NoSuchFileException**. Los sucesivos **cath** permiten determinar por  que ha fallado el borrado:

       import java.nio.file.*
       import java.io.IOException

        fun main() {
            val path = Paths.get("documentos/ejemplo.txt")
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


El metodo **deleteIfExists(Path)** tambien borra el fichero o directorio, pero no lanza ningun error en caso de que el fichero o directorio no exista.

**Ejemplo_creardirectorio.kt**: crear un directorio


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


**Ejemplo_copiardirectorio.kt**: copiar directorios

Se puede copiar un archivo o directorio usando el método copy(Path, Path, CopyOption...). La copia falla si el archivo de destino existe, a menos que se especifique la opción REPLACE_EXISTING. 

Se puede copiar directorios  Aunque, los archivos dentro del directorio no se copian, por lo que el nuevo directorio está vacío incluso cuando el directorio original contiene archivos.

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

**Ejemplo_copiarficheros.kt**: copiar ficheros

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



**Ejemplo_moverficheros.kt**: mover ficheros y directorios cambiando el nombre.

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





**Ejemplo_SistemaFicheros.kt**

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

        
Llicenciat sota la  [Llicència Creative Commons Reconeixement NoComercial
CompartirIgual 2.5](http://creativecommons.org/licenses/by-nc-sa/2.5/)