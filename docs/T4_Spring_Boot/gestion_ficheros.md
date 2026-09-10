# Subir y listar ficheros

En el ejemplo anterior, la aplicación escribía siempre el mismo texto en el mismo fichero. Ahora permitiremos que el cliente elija un archivo de su ordenador y lo envíe a la aplicación.

Avanzaremos solo un paso:

```text
Ejemplo anterior:  Spring escribe un texto fijo
Ejemplo actual:    El cliente envía un fichero a Spring
```

Mantendremos la misma organización:

```text
Cliente → FileController → FileService → carpeta data
```

## 1. El nuevo concepto: MultipartFile

Los formularios y clientes HTTP envían los archivos utilizando el formato `multipart/form-data`. Spring representa el archivo recibido mediante la clase `MultipartFile`.

Un `MultipartFile` permite consultar:

- el nombre original con `originalFilename`;
- si está vacío con `isEmpty`;
- sus bytes mediante `inputStream`.

!!! info "No es todavía el fichero definitivo"
    `MultipartFile` representa el archivo recibido en la petición. Para conservarlo después de finalizar la petición debemos copiar su contenido a nuestra carpeta `data`.

Solo añadiremos una anotación nueva:

| Anotación | Para qué sirve |
|---|---|
| `@RequestParam("file")` | Obtiene de la petición el archivo llamado `file` |

## 2. Modificar el servicio

Abre `service/FileService.kt` y sustituye su contenido:

```kotlin
package com.example.ficherosapi.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service
class FileService {

    private val folder: Path = Path.of("data") // (1)!

    init {
        Files.createDirectories(folder) // (2)!
    }

    fun save(file: MultipartFile): String {
        if (file.isEmpty) {
            return "El fichero está vacío" // (3)!
        }

        val name = Path.of(file.originalFilename ?: "file")
            .fileName
            .toString() // (4)!

        val destination = folder.resolve(name) // (5)!

        file.inputStream.use { input ->
            Files.copy(
                input,
                destination,
                StandardCopyOption.REPLACE_EXISTING
            ) // (6)!
        }

        return "Fichero guardado: $name"
    }

    fun list(): List<String> {
        return Files.list(folder).use { paths ->
            paths
                .filter { Files.isRegularFile(it) }
                .map { it.fileName.toString() }
                .sorted()
                .toList() // (7)!
        }
    }
}
```

1. Representa la carpeta `data` en la que guardaremos los archivos.
2. El bloque `init` se ejecuta al crear el servicio y crea la carpeta si todavía no existe. Es código Kotlin, no una anotación de Spring.
3. Comprobamos que el cliente no haya enviado un fichero vacío.
4. Obtenemos solamente el nombre, sin las carpetas que pudiera incluir el cliente.
5. Construimos la ruta del fichero dentro de `data`.
6. Copiamos los bytes recibidos. `use` cierra el flujo automáticamente y `REPLACE_EXISTING` permite sustituir un fichero con el mismo nombre.
7. Obtenemos únicamente los ficheros regulares, extraemos sus nombres, los ordenamos y creamos una lista.

El servicio sigue utilizando las mismas herramientas estudiadas anteriormente: `Path`, `Files`, flujos y `use`. La única clase nueva es `MultipartFile`, que sirve de puente entre la petición web y el flujo de entrada.

## 3. Modificar el controlador

Abre `controller/FileController.kt` y sustituye su contenido:

```kotlin
package com.example.ficherosapi.controller

import com.example.ficherosapi.service.FileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileController(
    private val fileService: FileService
) {

    @PostMapping("/api/files")
    fun upload(
        @RequestParam("file") file: MultipartFile // (1)!
    ): String {
        return fileService.save(file) // (2)!
    }

    @GetMapping("/api/files")
    fun list(): List<String> {
        return fileService.list() // (3)!
    }
}
```

1. Busca en la petición la parte llamada `file` y la convierte en un `MultipartFile`.
2. El controlador entrega el archivo al servicio y devuelve el mensaje obtenido.
3. Spring convierte automáticamente la lista de nombres a JSON.

El controlador es pequeño porque no contiene operaciones con `Path` o `Files`. Su responsabilidad es comunicar HTTP con el servicio.

## 4. Probar la subida

Arranca la aplicación. Después abre PowerShell en una carpeta que contenga un archivo llamado `ejemplo.txt` y ejecuta:

```powershell
curl.exe -F "file=@ejemplo.txt" http://localhost:8080/api/files
```

- `-F` crea una petición de formulario.
- `file` coincide con el nombre escrito en `@RequestParam("file")`.
- `@ejemplo.txt` indica a `curl.exe` que debe enviar el contenido del archivo.

La respuesta será similar a:

```text
Fichero guardado: ejemplo.txt
```

Comprueba que el archivo aparece en la carpeta `data` del proyecto.

## 5. Probar el listado

Abre en el navegador:

```text
http://localhost:8080/api/files
```

También puedes utilizar PowerShell:

```powershell
Invoke-RestMethod http://localhost:8080/api/files
```

Spring convierte la lista de Kotlin en una respuesta JSON:

```json
[
  "ejemplo.txt",
  "notas.txt"
]
```

## 6. Comparar los dos ejemplos

| Operación | Primer ejemplo | Segundo ejemplo |
|---|---|---|
| Contenido | Texto fijo | Archivo elegido por el cliente |
| Escritura | `Files.writeString` | `Files.copy` |
| Entrada | No recibe datos | Recibe un `MultipartFile` |
| Salida | Texto | Texto o lista JSON |
| Anotación nueva | Ninguna | `@RequestParam` |

!!! success "Segundo paso completado"
    La aplicación ya recibe un fichero real, lo guarda mediante las clases de acceso a datos y devuelve la lista de archivos almacenados. Para conseguirlo solo hemos necesitado incorporar `MultipartFile` y `@RequestParam`.
