# 🔹 Acceso al sistema de ficheros. Java.nio 

!!! warning "Dónde guardar los ejemplos"
    Programa los ejemplos en el proyecto **`Ficheros`**, dentro del paquete **`sistema`**. Los archivos utilizados por el código deben guardarse en las carpetas `documentos` o `documentos2`, situadas en la raíz del proyecto.

    Consulta [🧰 Entorno y ubicación de los ejemplos](../00_entorno_y_proyecto.md) si necesitas revisar la estructura completa.

Durante muchos años se ha utilizado la librería **java.io** para trabajar con ficheros en el mundo Java. Se trata de un **API** muy potente y flexible que nos permite realizar casi cualquier tipo de operación. Sin embargo es una API complicada de entender. **Java.nio** (New IO) es una nueva API disponible desde Java7 que nos permite mejorar el rendimiento, así como simplificar el manejo de muchas operaciones. 

**Java.nio** define interfaces y clases para que la máquina virtual Java tenga acceso a archivos, atributos de archivos y sistemas de archivos. Aunque dicho API comprende numerosas clases, solo existen unas pocas de ellas que sirven de puntos de entrada al API, lo que simplifica considerablemente su manejo.


La interfaz **java.nio.file.Path** representa un path, y las clases que implementen esta interfaz puede utilizarse para localizar ficheros en el sistema de ficheros. Nos permite manejar rutas al estilo GNU/Linux y rutas al estilo Windows dependiendo del SO en el que estemos trabajando.

La clase **java.nio.file.Files** es el otro punto de entrada a la librería de ficheros de Java. Es la que nos permite manejar ficheros reales del disco desde Java.


!!!Tip "Clases para la gesitón de ficheros"
    - **Paths**: Crea objetos Path desde cadenas de texto
    - **Path**: Representa rutas a archivos o directorios
    - **Files**: Permite operaciones sobre archivos usando Path


    
## Paths

La clase **Paths** es una clase de utilidad que proporciona métodos estáticos para crear objetos **Path**, que luego puedes usar con métodos de **Files**.

??? info "Métodos principales de Paths"
    |Método     |Descripción|
    |-----------|-----------|
    | **get(String first, String... )**|	Crea un objeto Path a partir de una o más cadenas.|
    | **get(URI uri)**|	Crea un Path desde un URI que debe ser del esquema file:///.  |      

!!!Note ""
    El uso de **Paths.get(...)** en Java (o Kotlin) no implica que el archivo o directorio exista. Este método simplemente crea una instancia de Path que representa una ruta en el sistema de archivos, pero no accede al disco ni verifica su existencia.

🖥️ **Ejemplo_get.kt**


Este ejemplo muestra cómo construir rutas con `Paths.get(...)` a partir de uno o varios segmentos de texto.

La finalidad del ejercicio es entender que `Paths.get(...)` solo construye la ruta en memoria y no comprueba si el archivo existe realmente.

```kotlin
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val path1: Path = Paths.get("documentos", "archivo.txt") // (1)!
    val path2: Path = Paths.get("C:", "usuarios", "nombre", "archivo.txt") // (2)!

    println("Ruta 1: $path1")
    println("Ruta 2: $path2")
}
```

1. Crea una ruta relativa (`documentos/archivo.txt`) sin acceder al disco.
2. Crea otra ruta a partir de varios segmentos, simulando una ruta más completa.

!!!note "📤 Salida esperada"
        Ruta 1: documentos\archivo.txt
        Ruta 2: C:\usuarios\nombre\archivo.txt


🖥️ **Ejemplo_uri.kt**


Este ejemplo muestra cómo crear un `Path` a partir de una URI de tipo `file:///`.

La finalidad del ejercicio es ver otra forma válida de construir rutas, útil cuando la fuente de datos ya viene en formato URI.

```kotlin
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val uri = URI("file:///C:/usuarios/nombre/archivo.txt") // (1)!
    val path: Path = Paths.get(uri) // (2)!

    println("Ruta a partir de URI: $path")
}
```

1. Construye una URI de fichero local en formato estándar.
2. Convierte la URI en un objeto Path usando Paths.get(uri).

!!!note "📤 Salida esperada"
        Ruta a partir de URI: C:\usuarios\nombre\archivo.txt


## Path

La clase **Path** Se utiliza junto con la clase **Files** para realizar operaciones como lectura, escritura, copia, o eliminación de archivos.  
La forma mas sencilla de construir un objeto que cumpla la interfaz **Path** es a partir de la clase **java.nio.file.Paths**, que tiene métodos estáticos que retornan objetos Path a partir de una representación tipo String del path deseado.  
Por supuesto, no es necesario que los ficheros existan de verdad en el disco duro para que se puedan crear los objetos Path correspondientes.

Un objeto Path puede representarse de dos formas:

- **Ruta absoluta**   

        val path = Paths.get("/home/usuario/archivo.txt")   

- **Ruta relativa**   
     
```kotlin
val path = Paths.get("documentos/ejemplo.txt") // (1)!
println(path.toAbsolutePath())



```

1. Construye una ruta relativa con `Paths.get(...)`.
Las **operaciones** y **métodos** principales que se pueden hacer con Path son:

??? info "Operaciones y métodos principales de Path"
    | **Método**                | **Qué devuelve**          | **Descripción**                                                                 |
    |-------------------------- |---------------------------|---------------------------------------------------------------------------------|
    | .startsWith(Path other)   | `Boolean`                 | Devuelve `true` si el path empieza por el path dado.                           |
    | .endsWith(Path other)     | `Boolean`                 | Devuelve `true` si el path termina con el path dado.                           |
    | .getParent()              | `Path?`                   | Devuelve el path padre (superior) o `null` si no tiene.                        |
    | .getRoot()                | `Path?`                   | Devuelve el componente raíz (`/`, `C:\`, etc.) o `null` si no existe.          |
    | .iterator()               | `Iterator<Path>`          | Permite iterar sobre cada parte del path (carpetas y nombre final).            |
    | .toString()               | `String`                  | Devuelve el path como texto.                                                   |
    | .toAbsolutePath()         | `Path`                    | Devuelve el path completo desde la raíz del sistema.                           |
    | .resolve(Path/String)     | `Path`                    | Une dos partes de un path de forma correcta, manejando barras automáticamente. |
    | .toFile()                 | `java.io.File`            | Convierte el `Path` en un `File` de la API tradicional de Java (`java.io`).    |


🖥️ **Ejemplo_Path.kt**


Este ejemplo recorre los métodos más usados de `Path` para inspeccionar y transformar rutas.

La finalidad del ejercicio es dominar las operaciones de manipulación de rutas antes de realizar operaciones reales de lectura o escritura con `Files`.

```kotlin
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val path: Path = Paths.get("documentos/ejemplo.txt") // (1)!

    val textoPath = path.toString() // (2)!
    val absoluto = path.toAbsolutePath() // (3)!
    val nombreFichero = path.fileName // (4)!
    val padre = path.parent // (5)!
    val raiz = path.root // (6)!

    println("toString(): ${textoPath}")
    println("toAbsolutePath(): ${absoluto}")
    println("getFileName(): ${nombreFichero}")
    println("getParent(): ${padre}")
    println("getRoot(): ${raiz}")

    val otroPath: Path = Paths.get("imagenes/foto.png") // (7)!
    val rutaResuelta = path.resolve(otroPath) // (8)!
    println("resolve(): ${rutaResuelta}")

    val relativo: Path = path.relativize(Paths.get("documentos/otroArchivo.txt")) // (9)!
    println("relativize(): $relativo")

    val rutaNormalizada: Path = Paths.get("carpeta/../archivo.txt").normalize() // (10)!
    println("normalize(): $rutaNormalizada")

    val empiezaPorDocumentos = path.startsWith("documentos") // (11)!
    val terminaEnEjemplo = path.endsWith("ejemplo.txt") // (12)!
    println("startsWith(\"documentos\"): ${empiezaPorDocumentos}")
    println("endsWith(\"ejemplo.txt\"): ${terminaEnEjemplo}")
}
```

1. Crea la ruta base sobre la que se harán todas las pruebas.
2. Muestra la representación textual directa del `Path`.
3. Convierte la ruta a absoluta según el entorno de ejecución.
4. Obtiene el nombre final del fichero.
5. Obtiene el directorio padre.
6. Consulta la raíz de la ruta (si existe).
7. Crea otra ruta para combinarla con la ruta base.
8. Combina rutas con `resolve(...)`.
9. Calcula una ruta relativa con `relativize(...)`.
10. se normaliza una ruta con `normalize()` para eliminar segmentos redundantes.
11. Comprueba si la ruta comienza por `documentos`.
12. Comprueba si la ruta termina en `ejemplo.txt`.

!!!note "📤 Salida esperada"
        toString(): documentos\ejemplo.txt
        toAbsolutePath(): C:\Ruta\Al\Proyecto\documentos\ejemplo.txt
        getFileName(): ejemplo.txt
        getParent(): documentos
        getRoot(): null
        resolve(): documentos\ejemplo.txt\imagenes\foto.png
        relativize(): ..\otroArchivo.txt
        normalize(): archivo.txt
        startsWith("documentos"): true
        endsWith("ejemplo.txt"): true
        


## Files

La clase **Files** es el otro punto de entrada a la librería de ficheros de Java. Es la que nos permite manejar ficheros reales del disco desde Java.  
Esta clase tiene métodos estáticos para el manejo de ficheros, los métodos de la clase **Files** trabajan sobre objetos **Path**. Muchos de estos métodos devuelven **streams**, lo que permite procesar archivos y directorios de forma eficiente y elegante. 

En Java (y también en Kotlin), un **Stream** es una secuencia de elementos que permite realizar operaciones funcionales (como map, filter, forEach, etc.) sobre datos de forma eficiente y fluida, sin necesidad de estructuras intermedias ni bucles explícitos. Algunos método de **Files** utilizan o devuelven **Streams**.


Las **operaciones** y **métodos** principales a realizar con Files son:

??? info "Operaciones y métodos principales de Files"
    | Método                            | Qué devuelve           | Descripción                                            |
    |-----------------------------------|------------------------|--------------------------------------------------------|
    | list(Path)                        | `Stream<Path>`         | Lista contenido directo (no recursivo) del directorio. |
    | .walk(Path)                       | `Stream<Path>`         | Recorre directorios de forma recursiva.                |
    | .find(...  )                      | `Stream<Path>`         | Busca elementos que cumplan una condición.             |
    | .lines(Path)                      | `Stream<String>`       | Devuelve las líneas de un archivo de texto.            |
    | .exists(Path)                     | `Boolean`              | Verifica si el archivo existe.                         |
    | .isDirectory(Path)                | `Boolean`              | Verifica si es un directorio.                          |
    | .isRegularFile(Path)              | `Boolean`              | Verifica si es un archivo normal.                      |
    | .isReadable(Path)                 | `Boolean`              | Verifica si se puede leer.                             |
    | .createFile(Path)                 | `Path`                 | Crea un archivo vacío.                                 |
    | .createDirectory(Path)            | `Path`                 | Crea un directorio.                                    |
    | .createDirectories(Path)          | `Path`                 | Crea directorios y subdirectorios necesarios.          |
    | .delete(Path)                     | `void`                 | Elimina un archivo o directorio.                       |
    | .deleteIfExists(Path)             | `Boolean`              | Elimina si existe.                                     |
    | .move(Path, Path)                 | `Path`                 | Mueve un archivo o directorio.                         |
    | .copy(Path, Path)                 | `Path`                 | Copia un archivo o directorio.                         |
    | .size(Path)                       | `Long`                 | Tamaño del archivo.                                    |
    | .getLastModifiedTime(Path)        | `FileTime`             | Última modificación.                                   |
    | .getOwner(Path)                   | `UserPrincipal`        | Devuelve el propietario.                               |
    | .getAttribute(Path, String)       | `Object`               | Devuelve un atributo específico.                       |




🖥️ **Ejemplo_permisos.kt**: existencia y comprobación de permisos


Este ejemplo verifica si una ruta existe y qué permisos básicos tiene asociados.

La finalidad del ejercicio es realizar una validación previa antes de operar sobre un fichero, evitando errores por acceso no permitido.

```kotlin
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

fun main() {
    val path: Path = Paths.get("documentos/ejemplo.txt") // (1)!
    val existe = Files.exists(path) // (2)!
    val legible = Files.isReadable(path) // (3)!
    val escribible = Files.isWritable(path) // (4)!
    val ejecutable = Files.isExecutable(path) // (5)!

    println("path = $path")
    println("exists = ${existe}")
    println("readable = ${legible}")
    println("writable = ${escribible}")
    println("executable = ${ejecutable}")
}
```

1. Define la ruta del fichero que se quiere comprobar.
2. Verifica si el fichero existe.
3. Comprueba permiso de lectura.
4. Comprueba permiso de escritura.
5. Comprueba permiso de ejecución.

!!!note "📤 Salida esperada"
        path = documentos\ejemplo.txt
        exists = true
        readable = true
        writable = true
        executable = true

  
🖥️ **Ejemplo_creardirectorio.kt**: crear un directorio


Este ejemplo muestra cómo crear un directorio con NIO y gestionar los errores habituales.

La finalidad del ejercicio es aprender a combinar operaciones de creación con manejo explícito de excepciones.

```kotlin
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.FileAlreadyExistsException
import java.io.IOException

fun main() {
    val path: Path = Paths.get("documentos") // (1)!

    try {
        val newDir = Files.createDirectory(path) // (2)!
        println("Directorio creado en: $newDir")
    } catch (e: FileAlreadyExistsException) { // (3)!
        println("El directorio ya existe: $path")
    } catch (e: IOException) { // (4)!
        println("Error de entrada/salida: ${e.message}")
        e.printStackTrace()
    }
}
```

1. Define la ruta del directorio a crear.
2. Intenta crear el directorio en disco.
3. Captura el caso en que la carpeta ya existe.
4. Captura otros errores de entrada/salida.

!!!note "📤 Salida esperada"
        Directorio creado en: documentos

🖥️ **Ejemplo_borrardirectorio.kt**: elimina un directorio


Este ejemplo elimina un directorio solo si existe previamente.

La finalidad del ejercicio es introducir un patrón seguro de borrado, evitando llamar a `delete` sobre rutas inexistentes.

```kotlin
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val directorio: Path = Paths.get("c:/mi_directorio") // (1)!

    if (Files.exists(directorio)) { // (2)!
        println("El directorio ya existe. Borrándolo...")
        Files.delete(directorio) // (3)!
    }
}
```

1. Define la ruta del directorio a eliminar.
2. Comprueba si existe antes de borrar.
3. Elimina el directorio.

El método  **delete(Path)** borra el fichero o directorio o lanza una excepción si el borrado falla. El siguiente ejemplo muestra como capturar y gestionar las excepciones que pueden producirse en el borrado. Si el fichero o directorio no existe, la excepción que se produce es  **NoSuchFileException**. Los sucesivos **cath** permiten determinar por  que ha fallado el borrado:

```kotlin
import java.nio.file.*
import java.io.IOException

fun main() {
    val path = Paths.get("c:/mi_directorio") // (1)!
    try {
        Files.delete(path) // (2)!
    } catch (e: NoSuchFileException) { // (3)!
        System.err.printf("%s: no such file or directory%n", path)
    } catch (e: DirectoryNotEmptyException) { // (4)!
        System.err.printf("%s not empty%n", path)
    } catch (e: IOException) { // (5)!
        System.err.println("Error: ${e.message}")
    }
}
```

1. Define la ruta que se quiere borrar.
2. Intenta eliminarla directamente con `Files.delete(...)`.
3. Captura el error cuando la ruta no existe.
4. Captura el error cuando el directorio no está vacío.
5. Captura cualquier otro error de E/S.


!!!Warning ""
    El metodo **deleteIfExists(Path)** tambien borra el fichero o directorio, pero no lanza ningun error en caso de que el fichero o directorio no exista.


🖥️ **Ejemplo_copiardirectorio.kt**: copiar directorios


Este ejemplo muestra cómo copiar un directorio usando `Files.copy(...)`.

La finalidad del ejercicio es entender que, al copiar directorios, se crea la carpeta destino pero no se copian automáticamente sus contenidos internos.

Se puede copiar un archivo o directorio usando el método copy(Path, Path, CopyOption...). La copia falla si el archivo de destino existe, a menos que se especifique la opción REPLACE_EXISTING. 

Se puede copiar directorios aunque, los archivos dentro del directorio no se copian, por lo que el nuevo directorio está vacío incluso cuando el directorio original contiene archivos.

```kotlin
import java.io.IOException
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val sourcePath: Path = Paths.get("documentos") // (1)!
    val destinationPath: Path = Paths.get("documentos/destino") // (2)!

    try {
        Files.copy(sourcePath, destinationPath) // (3)!
        println("Copia realizada con éxito.")
    } catch (e: FileAlreadyExistsException) { // (4)!
        println("El fichero o directorio ya existe en el destino.")
    } catch (e: IOException) { // (5)!
        println("Error al copiar: ${e.message}")
        e.printStackTrace()
    }
}
```

1. Define la ruta de origen del directorio.
2. Define la ruta de destino para la copia.
3. Ejecuta la copia del directorio.
4. Captura la colisión cuando el destino ya existe.
5. Captura otros errores de E/S.

🖥️ **Ejemplo_copiarficheros.kt**: copiar ficheros


Este ejemplo copia un fichero concreto y permite sobrescribir el destino si ya existe.

La finalidad del ejercicio es practicar la copia de archivos con control de colisiones en el destino.

```kotlin
import java.io.IOException
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

fun main() {
    val sourcePath: Path = Paths.get("documentos/ejemplo.txt") // (1)!
    val destinationPath: Path = Paths.get("documentos/ejemplo_copia.txt") // (2)!

    try {
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING) // (3)!
        println("Archivo copiado correctamente a: $destinationPath")
    } catch (e: FileAlreadyExistsException) { // (4)!
        println("El archivo destino ya existe.")
    } catch (e: IOException) { // (5)!
        println(" Error al copiar el archivo: ${e.message}")
        e.printStackTrace()
    }
}
```

1. Define la ruta del archivo origen.
2. Define la ruta del archivo de destino.
3. Copia el archivo permitiendo sobrescribir si ya existe.
4. Captura el caso en que el destino ya existe.
5. Captura otros errores de E/S.

!!!note "📤 Salida esperada"
        Archivo copiado correctamente a: documentos\ejemplo_copia.txt


🖥️ **Ejemplo_moverficheros.kt**: mover ficheros y directorios cambiando el nombre.


Este ejemplo mueve un archivo de ubicación y, al mismo tiempo, cambia su nombre.

La finalidad del ejercicio es mostrar que mover y renombrar son una misma operación cuando cambia la ruta de destino.

```kotlin
import java.io.IOException
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

fun main() {
    val sourcePath: Path = Paths.get("documentos/ejemplo.txt") // (1)!
    val destinationPath: Path = Paths.get("documentos2/ejemplo2.txt") // (2)!

    try {
        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING) // (3)!
        println("Archivo movido/renombrado correctamente a: $destinationPath")
    } catch (e: FileAlreadyExistsException) { // (4)!
        println("El archivo destino ya existe.")
    } catch (e: IOException) { // (5)!
        println("Error al mover el archivo: ${e.message}")
        e.printStackTrace()
    }
}
```

1. Define la ruta de origen del archivo.
2. Define la nueva ruta (ubicación y nombre final).
3. Mueve el archivo y reemplaza destino si existe.
4. Captura conflicto por destino existente.
5. Captura otros errores de E/S.

!!!note "📤 Salida esperada"
        Archivo movido/renombrado correctamente a: documentos2\ejemplo2.txt


El siguiente ejemplo recorre la estructura home en tu sistema, indicando los permisos de cada archivo y directorio: 

🖥️ **Ejemplo_SistemaFicheros.kt**


Este ejemplo implementa un explorador de ficheros en consola para navegar por directorios y ver atributos básicos.

La finalidad del ejercicio es integrar en un único programa varias operaciones de NIO: listado, atributos, permisos y navegación por rutas.

```kotlin
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    var currentPath: Path = Paths.get(System.getProperty("user.home")) // (1)!

    while (true) {
        println("\n Directorio actual: $currentPath")
        try {
            val paths = Files.list(currentPath).toList() // (2)!
            paths.forEachIndexed { index, path ->
                val attrs = Files.readAttributes(path, BasicFileAttributes::class.java) // (3)!
                val tipo = when {
                    attrs.isDirectory -> "[DIR]"
                    attrs.isRegularFile -> "[FILE]"
                    else -> "[OTRO]"
                }

                val permisos = listOfNotNull( // (4)!
                    if (Files.isReadable(path)) "r" else null,
                    if (Files.isWritable(path)) "w" else null,
                    if (Files.isExecutable(path)) "x" else null
                ).joinToString("")

                val size = if (attrs.isRegularFile) "${attrs.size()} bytes" else "" // (5)!

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
                    currentPath = currentPath.parent ?: currentPath // (6)!
                }
                else -> {
                    val index = input.toIntOrNull()
                    if (index != null && index in paths.indices) {
                        val selected = paths[index]
                        if (Files.isDirectory(selected)) { // (7)!
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
```

1. Inicializa la navegación en el directorio personal del usuario.
2. Lista el contenido directo de la carpeta actual.
3. Lee atributos básicos de cada elemento.
4. Construye la cadena de permisos `rwx` disponibles.
5. Calcula tamaño solo para archivos regulares.
6. Sube al directorio padre si existe.
7. Comprueba que la selección sea un directorio antes de navegar.


## FileSystem

En la biblioteca **java.nio** podemos encontrar otras clases que complementan y amplían lo que se puede hacer con **java.nio.file.Path**.

El concepto de **FileSystem** define un **sistema de ficheros completo**. Mientras que por otro lado el concepto de **Path** hace referencia a un **directorio, fichero o link** que tengamos dentro de nuestro sistema de ficheros. 

??? info "Métodos principales de FileSystem"
    | Método                  | Qué devuelve          | Descripción                                                                 |
    |-------------------------|-----------------------|------------------------------------------------------------------------------|
    | .getDefault()           | `FileSystem`          | Devuelve el sistema de ficheros por defecto del entorno en ejecución.       |
    | .getSeparator()         | `String`              | Devuelve el separador de nombres de ruta (por ejemplo, `/` o `\`).          |
    | .getRootDirectories()   | `Iterable<Path>`      | Devuelve los directorios raíz del sistema (ej: `/`, `C:\`).                 |
    | .getFileStores()        | `Iterable<FileStore>` | Devuelve las particiones o volúmenes montados en el sistema.                |
    | .getPath(...)           | `Path`                | Crea una instancia de `Path` a partir de cadenas de texto.                  |
    | .provider()             | `FileSystemProvider`  | Devuelve el proveedor del sistema de archivos (ej. `UnixFileSystemProvider`).|

---

Esto:

```kotlin
val fileSystem = FileSystems.getDefault() 
val path = fileSystem.getPath("C:\\Users\\alumno\\documento.txt") 

```

Es equivalente a usar:

```kotlin
val path = Paths.get("C:\\Users\\alumno\\documento.txt") 

```


🖥️ **Ejemplo_FileSystem.kt**: obtener el nombre de un fichero así como la carpeta padre en la que se encuentra ubicado.


Este ejemplo muestra el uso de `FileSystems.getDefault()` para construir rutas y recorrer sus segmentos.

La finalidad del ejercicio es comprender la relación entre `FileSystem` y `Path`, y cuándo puede interesar trabajar desde el sistema de archivos explícitamente.

```kotlin
import java.nio.file.FileSystems
import java.nio.file.Path

fun main() {
    val sistemaFicheros = FileSystems.getDefault() // (1)!
    val rutaFichero: Path = sistemaFicheros.getPath("documentos/destino/ejemplo3.txt") // (2)!

    println(rutaFichero.fileName)
    println(rutaFichero.parent.fileName)

    val rutaDirectorio: Path = sistemaFicheros.getPath("documentos/destino") // (3)!
    val it = rutaDirectorio.iterator() // (4)!

    while (it.hasNext()) {
        println(it.next().fileName)
    }
}
```

1. Obtiene el sistema de archivos por defecto del sistema operativo.
2. Construye la ruta del fichero desde ese `FileSystem`.
3. Construye la ruta del directorio a recorrer.
4. Crea un iterador para sus segmentos.


## BasicFileAttributes

BasicFileAttributes permite obtener **información detallada sobre archivos y directorios**, como fecha de creación, tamaño, etc.

Para poder utilizar un objeto de tipo **BasicFileAttributes**, primero es necesario llamar al método **readAttributes**:

!!!Note ""
        val attr = Files.readAttributes(path, BasicFileAttributes::class.java)

    - Este método pertenece a la clase **Files** y se encarga de leer los atributos asociados al archivo o directorio indicado por **path**.
    - **BasicFileAttributes::class.java:** indica que queremos obtener los atributos básicos definidos en esa clase.
    - El resultado (**attr**) es un objeto del tipo BasicFileAttributes.

??? info "Métodos principales de BasicFileAttributes"
    | Método             | Descripción                                      | Devuelve                |
    |--------------------|--------------------------------------------------|--------------------------|
    | creationTime()     | Devuelve la fecha de creación del archivo.       | `FileTime`              |
    | lastModifiedTime() | Devuelve la última fecha de modificación.        | `FileTime`              |
    | size()             | Devuelve el tamaño del archivo en bytes.         | `Long`                  |
    | isDirectory()      | Verifica si el `Path` representa un directorio.  | `Boolean`               |
    | isRegularFile()    | Verifica si es un archivo regular (no directorio). | `Boolean`             |


🖥️ **Ejemplo_BasicFileAttributes.kt**:  leer los atributos básicos de un archivo o directorio.


Este ejemplo recupera metadatos básicos de una ruta con `BasicFileAttributes`.

La finalidad del ejercicio es aprender a obtener información técnica del sistema de archivos sin abrir directamente el contenido del fichero.

```kotlin
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

fun main() {
    val path = Paths.get("documentos") // (1)!

    if (Files.exists(path)) { // (2)!
        val attr = Files.readAttributes(path, BasicFileAttributes::class.java) // (3)!
        println("Creación: ${attr.creationTime()}")
        println("Último acceso: ${attr.lastAccessTime()}")
        println("Es un directorio: ${attr.isDirectory}")
        println("Tamaño del archivo: ${attr.size()} bytes")
    }
}
```

1. Define la ruta del recurso a inspeccionar.
2. Verifica su existencia antes de leer atributos.
3. Obtiene los atributos básicos del fichero/directorio.

!!!note "📤 Salida esperada"
        Creación: 2024-10-01T12:00:00Z
        Último acceso: 2024-10-01T12:00:00Z
        Es un directorio: true
        Tamaño del archivo: 4096 bytes

## FileStore

FileStore permite obtener **información sobre el sistema de archivos**, como el espacio disponible.

No se puede instanciar un FileStore directamente. Para usarlo, necesitamos obtenerlo desde un Path (archivo o directorio)

        val Store = Files.getFileStore(path)


??? info "Métodos principales de FileStore"
    | Método                          | Descripción                                                       | Devuelve       |
    |---------------------------------|-------------------------------------------------------------------|----------------|
    | name()                          | Nombre del volumen o unidad lógica.                              | `String`       |
    | type()                          | Tipo de sistema de archivos (por ejemplo, `ext4`, `NTFS`, etc.). | `String`       |
    | getTotalSpace()                 | Espacio total disponible en el volumen (en bytes).               | `Long`         |
    | getUsableSpace()                | Espacio disponible para el usuario (en bytes).                   | `Long`         |
    | supportsFileAttributeView(...) | Verifica si el volumen soporta ciertos atributos como POSIX o DOS. | `Boolean`    |


🖥️ **Ejemplo_FileStore.kt**: obtener información del almacenamiento físico.


Este ejemplo consulta información del volumen o partición donde se encuentra una ruta.

La finalidad del ejercicio es conocer cómo acceder a métricas de almacenamiento del sistema desde Java NIO.

```kotlin
import java.nio.file.FileStore
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val path = Paths.get("/") // (1)!
    val fileStore: FileStore = Files.getFileStore(path) // (2)!

    println("Sistema de archivos: ${fileStore.type()}")
    println("Espacio total: ${fileStore.totalSpace / (1024 * 1024)} MB")
    println("Espacio disponible: ${fileStore.usableSpace / (1024 * 1024)} MB")
}
```

1. Define una ruta base para identificar el volumen.
2. Obtiene el `FileStore` asociado a esa ruta.

!!!note "📤 Salida esperada"
        Sistema de archivos: NTFS
        Espacio total: 476938 MB
        Espacio disponible: 154320 MB

!!!Note "Nota"
    Funciona en Windows y Linux, aunque `Files.getFileStore(Paths.get("/"))` podría requerir ajustes en Windows para seleccionar una unidad específica (`C:\`, `D:\`, etc.).
**EjemploCompleto_File.kt** :El siguiente ejemplo utiliza todas estas funciones para mostrar información sobre el sistema de ficheros.

```kotlin
import java.io.File
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileStore
import java.nio.file.FileSystems

fun main() {
    println(" Raíces del sistema:")
    File.listRoots().forEach { raiz -> // (1)!
        println("- ${raiz.absolutePath}")
    }

    println("\n Sistemas de archivos detectados:")
    val fileSystem: FileSystem = FileSystems.getDefault() // (2)!
    fileSystem.fileStores.forEach { store: FileStore -> // (3)!
        println("Unidad: ${store.name()} (${store.type()})")
        println("Total: ${store.totalSpace / 1024 / 1024} MB")
        println("Libre: ${store.usableSpace / 1024 / 1024} MB")
    }

    val path: Path = Paths.get("datos.txt") // (4)!

    if (Files.exists(path)) { // (5)!
        println("\n Atributos del fichero '${path.fileName}':")
        val attrs: BasicFileAttributes = Files.readAttributes(path, BasicFileAttributes::class.java) // (6)!

        println("Creación: ${attrs.creationTime()}")
        println("Último acceso: ${attrs.lastAccessTime()}")
        println("Última modificación: ${attrs.lastModifiedTime()}")
        println("Tamaño: ${attrs.size()} bytes")
        println("¿Es directorio?: ${attrs.isDirectory}")
        println("¿Es archivo normal?: ${attrs.isRegularFile}")
    } else {
        println("\n El fichero 'datos.txt' no existe en la raíz del proyecto.")
    }
}
```

1. Recorre todas las raíces disponibles (unidades o puntos de montaje).
2. Obtiene el `FileSystem` por defecto.
3. Recorre cada `FileStore` del sistema.
4. Define la ruta del fichero a analizar.
5. Comprueba si el fichero existe.
6. Lee los atributos básicos del fichero.

!!!note "📤 Salida esperada"
         Raíces del sistema:
        - C:\
        - D:\

         Sistemas de archivos detectados:
        Unidad: OS (NTFS)
        Total: 476938 MB
        Libre: 154320 MB

         Atributos del fichero 'datos.txt':
        Creación: 2024-10-01T12:00:00Z
        Último acceso: 2024-10-01T12:00:00Z
        Última modificación: 2024-10-01T12:00:00Z
        Tamaño: 4096 bytes
        ¿Es directorio?: false
        ¿Es archivo normal?: true
                    
---

!!!question "🧠 Comprueba tu comprensión"
    1. ¿Cuál es la diferencia principal entre `Path` y `FileStore`?
    2. Si necesitas crear un directorio y también sus carpetas padre en caso de que no existan, ¿qué método usarías?
    3. Si instancias un objeto con `Paths.get("C:/no_existe.txt")`, ¿se lanza alguna excepción indicando que el archivo no está en el disco?

    ??? success "Ver respuestas"
        1. `Path` representa una ruta (un archivo o directorio), mientras que `FileStore` representa el volumen o partición física donde se almacena (ej. el disco C:).
        2. Usaría `Files.createDirectories(path)` (en plural).
        3. **No**. Crear un objeto `Path` solo construye la representación en memoria. El disco no se toca hasta que usas un método de la clase `Files` sobre ese `Path`.

