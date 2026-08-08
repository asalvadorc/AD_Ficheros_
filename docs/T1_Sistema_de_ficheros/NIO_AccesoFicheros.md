# 🔹 Acceso al sistema de ficheros. Java.nio 

Durante muchos años se ha utilizado la librería **java.io** para trabajar con ficheros en el mundo Java. Se trata de un **API** muy potente y flexible que nos permite realizar casi cualquier tipo de operación. Sin embargo es una API complicada de entender. **Java.nio** (New IO) es una nueva API disponible desde Java7 que nos permite mejorar el rendimiento, así como simplificar el manejo de muchas operaciones. 

**Java.nio** define interfaces y clases para que la máquina virtual Java tenga acceso a archivos, atributos de archivos y sistemas de archivos. Aunque dicho API comprende numerosas clases, solo existen unas pocas de ellas que sirven de puntos de entrada al API, lo que simplifica considerablemente su manejo.


La interfaz **java.nio.file.Path** representa un path, y las clases que implementen esta interfaz puede utilizarse para localizar ficheros en el sistema de ficheros. Nos permite manejar rutas al estilo GNU/Linux y rutas al estilo Windows dependiendo del SO en el que estemos trabajando.

La clase **java.nio.file.Files** es el otro punto de entrada a la librería de ficheros de Java. Es la que nos permite manejar ficheros reales del disco desde Java.


!!!Tip "Clases para la gesitón de ficheros"
    - **Paths**: Crea objetos Path desde cadenas de texto
    - **Path**: Representa rutas a archivos o directorios
    - **Files**: Permite operaciones sobre archivos usando Path


    
## 🔹 Paths

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

- Primero se crea una ruta relativa (`documentos/archivo.txt`).
- Después se crea una ruta con más segmentos que simula una ruta más completa.
- Finalmente, se imprimen ambas rutas para ver cómo Java/Kotlin las representa según el sistema operativo.

La finalidad del ejercicio es entender que `Paths.get(...)` solo construye la ruta en memoria y no comprueba si el archivo existe realmente.

```kotlin
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val path1: Path = Paths.get("documentos", "archivo.txt") // (1)!
    val path2: Path = Paths.get("C:", "usuarios", "nombre", "archivo.txt") // (2)!

    println("Ruta 1: $path1") // (3)!
    println("Ruta 2: $path2") // (4)!
}
```

1. Crea una ruta relativa (`documentos/archivo.txt`) sin acceder al disco.
2. Crea otra ruta a partir de varios segmentos, simulando una ruta más completa.
3. Muestra por pantalla la primera ruta tal como la interpreta el sistema operativo.
4. Muestra por pantalla la segunda ruta para comparar su formato con la anterior.

!!!note "📤 Salida esperada"
        Ruta 1: documentos\archivo.txt
        Ruta 2: C:\usuarios\nombre\archivo.txt


🖥️ **Ejemplo_uri.kt**


Este ejemplo muestra cómo crear un `Path` a partir de una URI de tipo `file:///`.

- Primero se define una URI con formato de archivo local.
- Después se transforma esa URI en un objeto `Path` con `Paths.get(uri)`.
- Finalmente, se imprime la ruta generada.

La finalidad del ejercicio es ver otra forma válida de construir rutas, útil cuando la fuente de datos ya viene en formato URI.

```kotlin
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val uri = URI("file:///C:/usuarios/nombre/archivo.txt") // (1)!
    val path: Path = Paths.get(uri) // (2)!

    println("Ruta a partir de URI: $path") // (3)!
}
```

1. Construye una URI de fichero local en formato estándar.
2. Convierte la URI en un objeto Path usando Paths.get(uri).
3. Imprime la ruta resultante adaptada al formato del sistema operativo.

!!!note "📤 Salida esperada"
        Ruta a partir de URI: C:\usuarios\nombre\archivo.txt


## 🔹 Path

La clase **Path** Se utiliza junto con la clase **Files** para realizar operaciones como lectura, escritura, copia, o eliminación de archivos.  
La forma mas sencilla de construir un objeto que cumpla la interfaz **Path** es a partir de la clase **java.nio.file.Paths**, que tiene métodos estáticos que retornan objetos Path a partir de una representación tipo String del path deseado.  
Por supuesto, no es necesario que los ficheros existan de verdad en el disco duro para que se puedan crear los objetos Path correspondientes.

Un objeto Path puede representarse de dos formas:

- **Ruta absoluta**   

        val path = Paths.get("/home/usuario/archivo.txt")   

- **Ruta relativa**   
     
```kotlin
val path = Paths.get("documentos/ejemplo.txt") // (1)!
println(path.toAbsolutePath()) // (2)!



```

1. Ejecuta $snippet.
2. Ejecuta $snippet.
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

- Primero se crea un `Path` base y se muestran sus propiedades principales (`fileName`, `parent`, `root`, etc.).
- Después se combinan rutas con `resolve(...)` y se calcula una ruta relativa con `relativize(...)`.
- También se normaliza una ruta con `normalize()` para eliminar segmentos redundantes.
- Finalmente, se comprueba si una ruta empieza o termina con determinados valores.

La finalidad del ejercicio es dominar las operaciones de manipulación de rutas antes de realizar operaciones reales de lectura o escritura con `Files`.

```kotlin
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val path: Path = Paths.get("documentos/ejemplo.txt") // (1)!

    println("toString(): ${path}") // (2)!
    println("toAbsolutePath(): ${path.toAbsolutePath()}") // (3)!
    println("getFileName(): ${path.fileName}") // (4)!
    println("getParent(): ${path.parent}") // (5)!
    println("getRoot(): ${path.root}") // (6)!

    val otroPath: Path = Paths.get("imagenes/foto.png") // (7)!
    println("resolve(): ${path.resolve(otroPath)}") // (8)!

    val relativo: Path = path.relativize(Paths.get("documentos/otroArchivo.txt")) // (9)!
    println("relativize(): $relativo") // (10)!

    val rutaNormalizada: Path = Paths.get("carpeta/../archivo.txt").normalize() // (11)!
    println("normalize(): $rutaNormalizada") // (12)!

    println("startsWith(\"documentos\"): ${path.startsWith("documentos")}") // (13)!
    println("endsWith(\"ejemplo.txt\"): ${path.endsWith("ejemplo.txt")}") // (14)!
}
```

1. Crea la ruta base sobre la que se harán todas las pruebas.
2. Muestra la representación textual directa del `Path`.
3. Convierte la ruta a absoluta según el entorno de ejecución.
4. Obtiene el nombre final del fichero.
5. Obtiene el directorio padre.
6. Consulta la raíz de la ruta (si existe).
7. Crea otra ruta para combinarla con la ruta base.
8. Une ambas rutas con `resolve(...)`.
9. Calcula la ruta relativa entre dos rutas del mismo contexto.
10. Muestra el resultado de `relativize(...)`.
11. Normaliza una ruta eliminando segmentos redundantes.
12. Muestra la ruta normalizada.
13. Comprueba si la ruta comienza por `documentos`.
14. Comprueba si la ruta termina en `ejemplo.txt`.

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
        


## 🔹 Files

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

- Primero se crea un `Path` al archivo que se quiere comprobar.
- Después se consulta su existencia con `Files.exists(...)`.
- A continuación se revisan permisos de lectura, escritura y ejecución.

La finalidad del ejercicio es realizar una validación previa antes de operar sobre un fichero, evitando errores por acceso no permitido.

```kotlin
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

fun main() {
    val path: Path = Paths.get("documentos/ejemplo.txt") // (1)!

    println("path = $path") // (2)!
    println("exists = ${Files.exists(path)}") // (3)!
    println("readable = ${Files.isReadable(path)}") // (4)!
    println("writable = ${Files.isWritable(path)}") // (5)!
    println("executable = ${Files.isExecutable(path)}") // (6)!
}
```

1. Define la ruta del fichero que se quiere comprobar.
2. Muestra la ruta construida.
3. Verifica si el fichero existe.
4. Comprueba permiso de lectura.
5. Comprueba permiso de escritura.
6. Comprueba permiso de ejecución.

!!!note "📤 Salida esperada"
        path = documentos\ejemplo.txt
        exists = true
        readable = true
        writable = true
        executable = true

  
🖥️ **Ejemplo_creardirectorio.kt**: crear un directorio


Este ejemplo muestra cómo crear un directorio con NIO y gestionar los errores habituales.

- Primero se define la ruta del directorio a crear.
- Después se intenta crear con `Files.createDirectory(...)`.
- Si ya existe, se captura `FileAlreadyExistsException`.
- Si ocurre otro problema de entrada/salida, se captura `IOException`.

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
        println("Directorio creado en: $newDir") // (3)!
    } catch (e: FileAlreadyExistsException) { // (4)!
        println("El directorio ya existe: $path") // (5)!
    } catch (e: IOException) { // (6)!
        println("Error de entrada/salida: ${e.message}") // (7)!
        e.printStackTrace() // (8)!
    }
}
```

1. Define la ruta del directorio a crear.
2. Intenta crear el directorio en disco.
3. Informa que la creación fue correcta.
4. Captura el caso en que la carpeta ya existe.
5. Muestra un mensaje específico para ese caso.
6. Captura otros errores de entrada/salida.
7. Muestra el detalle del error.
8. Imprime la traza para depuración.

!!!note "📤 Salida esperada"
        Directorio creado en: documentos

🖥️ **Ejemplo_borrardirectorio.kt**: elimina un directorio


Este ejemplo elimina un directorio solo si existe previamente.

- Primero se construye la ruta al directorio.
- Después se comprueba su existencia con `Files.exists(...)`.
- Si existe, se elimina con `Files.delete(...)`.

La finalidad del ejercicio es introducir un patrón seguro de borrado, evitando llamar a `delete` sobre rutas inexistentes.

```kotlin
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun main() {
    val directorio: Path = Paths.get("c:/mi_directorio") // (1)!

    if (Files.exists(directorio)) { // (2)!
        println("El directorio ya existe. Borrándolo...") // (3)!
        Files.delete(directorio) // (4)!
    }
}
```

1. Define la ruta del directorio a eliminar.
2. Comprueba si existe antes de borrar.
3. Informa que se va a realizar el borrado.
4. Elimina el directorio.

**Gestión de errores y validaciones**{.azul}

El método  **delete(Path)** borra el fichero o directorio o lanza una excepción si el borrado falla. El siguiente ejemplo muestra como capturar y gestionar las excepciones que pueden producirse en el borrado. Si el fichero o directorio no existe, la excepción que se produce es  **NoSuchFileException**. Los sucesivos **cath** permiten determinar por  que ha fallado el borrado:

```kotlin
import java.nio.file.*
import java.io.IOException

fun main() {
    val path = Paths.get("c:/mi_directorio") // (1)!
    try {
        Files.delete(path) // (2)!
    } catch (e: NoSuchFileException) { // (3)!
        System.err.printf("%s: no such file or directory%n", path) // (4)!
    } catch (e: DirectoryNotEmptyException) { // (5)!
        System.err.printf("%s not empty%n", path) // (6)!
    } catch (e: IOException) { // (7)!
        System.err.println("Error: ${e.message}") // (8)!
    }
}
```

1. Define la ruta que se quiere borrar.
2. Intenta eliminarla directamente con `Files.delete(...)`.
3. Captura el error cuando la ruta no existe.
4. Informa de que no se encontró el fichero o directorio.
5. Captura el error cuando el directorio no está vacío.
6. Informa de que hay contenido pendiente por borrar.
7. Captura cualquier otro error de E/S.
8. Muestra el detalle del fallo general.


!!!Warning ""
    El metodo **deleteIfExists(Path)** tambien borra el fichero o directorio, pero no lanza ningun error en caso de que el fichero o directorio no exista.


🖥️ **Ejemplo_copiardirectorio.kt**: copiar directorios


Este ejemplo muestra cómo copiar un directorio usando `Files.copy(...)`.

- Primero se definen ruta origen y ruta destino.
- Después se intenta copiar el directorio.
- Si el destino ya existe, se captura la excepción correspondiente.
- También se indica la opción `REPLACE_EXISTING` para sobrescribir si se necesita.

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
        println("Copia realizada con éxito.") // (4)!
    } catch (e: FileAlreadyExistsException) { // (5)!
        println("El fichero o directorio ya existe en el destino.") // (6)!
    } catch (e: IOException) { // (7)!
        println("Error al copiar: ${e.message}") // (8)!
        e.printStackTrace() // (9)!
    }
}
```

1. Define la ruta de origen del directorio.
2. Define la ruta de destino para la copia.
3. Ejecuta la copia del directorio.
4. Informa de copia completada.
5. Captura la colisión cuando el destino ya existe.
6. Muestra el mensaje específico de colisión.
7. Captura otros errores de E/S.
8. Muestra el detalle del error.
9. Imprime la traza para diagnóstico.

🖥️ **Ejemplo_copiarficheros.kt**: copiar ficheros


Este ejemplo copia un fichero concreto y permite sobrescribir el destino si ya existe.

- Primero se definen el archivo origen y el archivo destino.
- Después se ejecuta la copia con `StandardCopyOption.REPLACE_EXISTING`.
- Si hay conflictos o errores de E/S, se gestionan mediante excepciones.

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
        println("Archivo copiado correctamente a: $destinationPath") // (4)!
    } catch (e: FileAlreadyExistsException) { // (5)!
        println("El archivo destino ya existe.") // (6)!
    } catch (e: IOException) { // (7)!
        println(" Error al copiar el archivo: ${e.message}") // (8)!
        e.printStackTrace() // (9)!
    }
}
```

1. Define la ruta del archivo origen.
2. Define la ruta del archivo de destino.
3. Copia el archivo permitiendo sobrescribir si ya existe.
4. Informa de que la copia se realizó correctamente.
5. Captura el caso en que el destino ya existe.
6. Muestra el aviso de colisión.
7. Captura otros errores de E/S.
8. Muestra el mensaje del error.
9. Imprime la traza de depuración.

!!!note "📤 Salida esperada"
        Archivo copiado correctamente a: documentos\ejemplo_copia.txt


🖥️ **Ejemplo_moverficheros.kt**: mover ficheros y directorios cambiando el nombre.


Este ejemplo mueve un archivo de ubicación y, al mismo tiempo, cambia su nombre.

- Primero se define la ruta de origen y la de destino final.
- Después se usa `Files.move(...)` para realizar el traslado.
- La opción `REPLACE_EXISTING` permite reemplazar si el destino ya existe.
- Se gestionan excepciones para informar de posibles errores.

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
        println("Archivo movido/renombrado correctamente a: $destinationPath") // (4)!
    } catch (e: FileAlreadyExistsException) { // (5)!
        println("El archivo destino ya existe.") // (6)!
    } catch (e: IOException) { // (7)!
        println("Error al mover el archivo: ${e.message}") // (8)!
        e.printStackTrace() // (9)!
    }
}
```

1. Define la ruta de origen del archivo.
2. Define la nueva ruta (ubicación y nombre final).
3. Mueve el archivo y reemplaza destino si existe.
4. Informa del movimiento/renombrado correcto.
5. Captura conflicto por destino existente.
6. Muestra aviso de conflicto.
7. Captura otros errores de E/S.
8. Muestra el detalle del error.
9. Imprime la traza para depurar.

!!!note "📤 Salida esperada"
        Archivo movido/renombrado correctamente a: documentos2\ejemplo2.txt


El siguiente ejemplo recorre la estructura home en tu sistema, indicando los permisos de cada archivo y directorio: 

🖥️ **Ejemplo_SistemaFicheros.kt**


Este ejemplo implementa un explorador de ficheros en consola para navegar por directorios y ver atributos básicos.

- Primero establece como punto inicial el directorio personal del usuario.
- Después lista los elementos del directorio actual y muestra tipo, permisos y tamaño.
- Permite navegar a subdirectorios por índice, subir con `..` o salir con `salir`.
- Controla entradas inválidas y errores de acceso con manejo de excepciones.

La finalidad del ejercicio es integrar en un único programa varias operaciones de NIO: listado, atributos, permisos y navegación por rutas.

```kotlin
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`) // (1)!
    var currentPath: Path = Paths.get(System.getProperty("user.home")) // (2)!

    while (true) { // (3)!
        println("\n Directorio actual: $currentPath") // (4)!
        try {
            val paths = Files.list(currentPath).toList() // (5)!
            paths.forEachIndexed { index, path -> // (6)!
                val attrs = Files.readAttributes(path, BasicFileAttributes::class.java) // (7)!
                val tipo = when { // (8)!
                    attrs.isDirectory -> "[DIR]"
                    attrs.isRegularFile -> "[FILE]"
                    else -> "[OTRO]"
                }

                val permisos = listOfNotNull( // (9)!
                    if (Files.isReadable(path)) "r" else null,
                    if (Files.isWritable(path)) "w" else null,
                    if (Files.isExecutable(path)) "x" else null
                ).joinToString("")

                val size = if (attrs.isRegularFile) "${attrs.size()} bytes" else "" // (10)!

                println("$index. $tipo ${path.fileName} [$permisos] $size") // (11)!
            }

            println("\nOpciones:")
            println(" - Número: acceder a subdirectorio")
            println(" - `..`: subir al directorio padre")
            println(" - `salir`: finalizar el programa")
            print("Opción: ")

            when (val input = scanner.nextLine()) { // (12)!
                "salir" -> {
                    println("Saliendo del explorador.") // (13)!
                    return
                }
                ".." -> {
                    currentPath = currentPath.parent ?: currentPath // (14)!
                }
                else -> {
                    val index = input.toIntOrNull() // (15)!
                    if (index != null && index in paths.indices) { // (16)!
                        val selected = paths[index] // (17)!
                        if (Files.isDirectory(selected)) { // (18)!
                            currentPath = selected // (19)!
                        } else {
                            println("No es un directorio.") // (20)!
                        }
                    } else {
                        println("Entrada no válida.") // (21)!
                    }
                }
            }

        } catch (e: Exception) { // (22)!
            println("Error al acceder al directorio: ${e.message}") // (23)!
        }
    }
}
```

1. Crea el lector de entrada por consola.
2. Inicializa la navegación en el directorio personal del usuario.
3. Mantiene el explorador en ejecución continua.
4. Muestra el directorio en el que estás situado.
5. Lista el contenido directo de la carpeta actual.
6. Recorre cada elemento junto con su índice.
7. Lee atributos básicos del elemento.
8. Clasifica cada entrada como directorio, fichero u otro tipo.
9. Construye la cadena de permisos `rwx` disponibles.
10. Calcula tamaño solo para archivos regulares.
11. Imprime una fila con índice, tipo, nombre, permisos y tamaño.
12. Lee y evalúa la opción escrita por el usuario.
13. Finaliza el programa cuando se escribe `salir`.
14. Sube al directorio padre si existe.
15. Intenta convertir la entrada a número de índice.
16. Valida que el índice sea correcto.
17. Obtiene el elemento seleccionado.
18. Comprueba que la selección sea un directorio.
19. Entra en el subdirectorio seleccionado.
20. Informa cuando se intenta abrir un archivo.
21. Informa cuando la entrada no es válida.
22. Captura errores de acceso o lectura.
23. Muestra el mensaje del error capturado.


## 🔹 FileSystem

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
val fileSystem = FileSystems.getDefault() // (1)!
val path = fileSystem.getPath("C:\\Users\\alumno\\documento.txt") // (2)!

```

1. Ejecuta $snippet.
2. Ejecuta $snippet.
Es equivalente a usar:

```kotlin
val path = Paths.get("C:\\Users\\alumno\\documento.txt") // (1)!

```

1. Ejecuta $snippet.
Pero usando FileSystems.getDefault() puedes:

- Cambiar de sistema de ficheros si lo necesitas (por ejemplo, ZIP o virtuales).

- Obtener características del sistema.

🖥️ **Ejemplo_FileSystem.kt**: obtener el nombre de un fichero así como la carpeta padre en la que se encuentra ubicado.


Este ejemplo muestra el uso de `FileSystems.getDefault()` para construir rutas y recorrer sus segmentos.

- Primero se obtiene el sistema de archivos por defecto.
- Después se crea una ruta de fichero y se muestran su nombre y el de su carpeta padre.
- A continuación se crea una ruta de directorio y se recorre parte por parte con su iterador.

La finalidad del ejercicio es comprender la relación entre `FileSystem` y `Path`, y cuándo puede interesar trabajar desde el sistema de archivos explícitamente.

```kotlin
import java.nio.file.FileSystems
import java.nio.file.Path

fun main() {
    val sistemaFicheros = FileSystems.getDefault() // (1)!
    val rutaFichero: Path = sistemaFicheros.getPath("documentos/destino/ejemplo3.txt") // (2)!

    println(rutaFichero.fileName) // (3)!
    println(rutaFichero.parent.fileName) // (4)!

    val rutaDirectorio: Path = sistemaFicheros.getPath("documentos/destino") // (5)!
    val it = rutaDirectorio.iterator() // (6)!

    while (it.hasNext()) { // (7)!
        println(it.next().fileName) // (8)!
    }
}
```

1. Obtiene el sistema de archivos por defecto del sistema operativo.
2. Construye la ruta del fichero desde ese `FileSystem`.
3. Muestra el nombre del fichero.
4. Muestra el nombre de la carpeta padre.
5. Construye la ruta del directorio a recorrer.
6. Crea un iterador para sus segmentos.
7. Recorre cada parte de la ruta.
8. Imprime el nombre de cada segmento.


## 🔹 BasicFileAttributes

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

- Primero se define la ruta a consultar.
- Después se comprueba si existe para evitar errores.
- Si existe, se leen sus atributos con `Files.readAttributes(...)`.
- Finalmente, se muestran datos como fecha de creación, último acceso, tipo y tamaño.

La finalidad del ejercicio es aprender a obtener información técnica del sistema de archivos sin abrir directamente el contenido del fichero.

```kotlin
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

fun main() {
    val path = Paths.get("documentos") // (1)!

    if (Files.exists(path)) { // (2)!
        val attr = Files.readAttributes(path, BasicFileAttributes::class.java) // (3)!
        println("Creación: ${attr.creationTime()}") // (4)!
        println("Último acceso: ${attr.lastAccessTime()}") // (5)!
        println("Es un directorio: ${attr.isDirectory}") // (6)!
        println("Tamaño del archivo: ${attr.size()} bytes") // (7)!
    }
}
```

1. Define la ruta del recurso a inspeccionar.
2. Verifica su existencia antes de leer atributos.
3. Obtiene los atributos básicos del fichero/directorio.
4. Muestra la fecha de creación.
5. Muestra la fecha del último acceso.
6. Indica si la ruta corresponde a un directorio.
7. Muestra el tamaño en bytes.

!!!note "📤 Salida esperada"
        Creación: 2024-10-01T12:00:00Z
        Último acceso: 2024-10-01T12:00:00Z
        Es un directorio: true
        Tamaño del archivo: 4096 bytes

## 🔹 FileStore

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

- Primero se define una ruta base del sistema.
- Después se obtiene su `FileStore` con `Files.getFileStore(...)`.
- Finalmente, se muestran tipo de sistema de archivos y espacio total/disponible.

La finalidad del ejercicio es conocer cómo acceder a métricas de almacenamiento del sistema desde Java NIO.

```kotlin
import java.nio.file.FileStore
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val path = Paths.get("/") // (1)!
    val fileStore: FileStore = Files.getFileStore(path) // (2)!

    println("Sistema de archivos: ${fileStore.type()}") // (3)!
    println("Espacio total: ${fileStore.totalSpace / (1024 * 1024)} MB") // (4)!
    println("Espacio disponible: ${fileStore.usableSpace / (1024 * 1024)} MB") // (5)!
}
```

1. Define una ruta base para identificar el volumen.
2. Obtiene el `FileStore` asociado a esa ruta.
3. Muestra el tipo de sistema de archivos.
4. Muestra el espacio total del volumen en MB.
5. Muestra el espacio utilizable en MB.

!!!note "📤 Salida esperada"
        Sistema de archivos: NTFS
        Espacio total: 476938 MB
        Espacio disponible: 154320 MB

!!!Note "Nota"
```kotlin
Funciona en Windows y Linux, aunque Files.getFileStore(Paths.get("/")) podría requerir ajustes en Windows para seleccionar una unidad específica (C:\, D:\, etc.).     // (1)!


```

1. Ejecuta $snippet.
**EjemploCompleto_File.kt** :El siguiente ejemplo utiliza todas estas funciones para mostrar información sobre el sistema de ficheros.

```kotlin
import java.io.File
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileStore
import java.nio.file.FileSystems

fun main() {
    println(" Raíces del sistema:") // (1)!
    File.listRoots().forEach { raiz -> // (2)!
        println("- ${raiz.absolutePath}") // (3)!
    }

    println("\n Sistemas de archivos detectados:") // (4)!
    val fileSystem: FileSystem = FileSystems.getDefault() // (5)!
    fileSystem.fileStores.forEach { store: FileStore -> // (6)!
        println("Unidad: ${store.name()} (${store.type()})") // (7)!
        println("Total: ${store.totalSpace / 1024 / 1024} MB") // (8)!
        println("Libre: ${store.usableSpace / 1024 / 1024} MB") // (9)!
    }

    val path: Path = Paths.get("datos.txt") // (10)!

    if (Files.exists(path)) { // (11)!
        println("\n Atributos del fichero '${path.fileName}':") // (12)!
        val attrs: BasicFileAttributes = Files.readAttributes(path, BasicFileAttributes::class.java) // (13)!

        println("Creación: ${attrs.creationTime()}") // (14)!
        println("Último acceso: ${attrs.lastAccessTime()}") // (15)!
        println("Última modificación: ${attrs.lastModifiedTime()}") // (16)!
        println("Tamaño: ${attrs.size()} bytes") // (17)!
        println("¿Es directorio?: ${attrs.isDirectory}") // (18)!
        println("¿Es archivo normal?: ${attrs.isRegularFile}") // (19)!
    } else {
        println("\n El fichero 'datos.txt' no existe en la raíz del proyecto.") // (20)!
    }
}
```

1. Muestra el encabezado de raíces del sistema.
2. Recorre todas las raíces disponibles (unidades o puntos de montaje).
3. Imprime la ruta absoluta de cada raíz.
4. Muestra el encabezado de sistemas de archivos detectados.
5. Obtiene el `FileSystem` por defecto.
6. Recorre cada `FileStore` del sistema.
7. Muestra nombre y tipo de cada unidad/volumen.
8. Muestra el espacio total del volumen en MB.
9. Muestra el espacio libre utilizable en MB.
10. Define la ruta del fichero a analizar.
11. Comprueba si el fichero existe.
12. Muestra el encabezado de atributos del fichero.
13. Lee los atributos básicos del fichero.
14. Muestra fecha de creación.
15. Muestra fecha de último acceso.
16. Muestra fecha de última modificación.
17. Muestra tamaño en bytes.
18. Indica si la ruta es un directorio.
19. Indica si la ruta es un archivo regular.
20. Informa cuando el fichero no existe.

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

