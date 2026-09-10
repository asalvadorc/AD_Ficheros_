# Primera aplicación con ficheros

En la primera aplicación aprendimos que el controlador recibe una petición y llama a un servicio:

```text
Petición:   Cliente → Controlador → Servicio
Respuesta:  Cliente ← Controlador ← Servicio
```

Ahora sustituiremos el saludo por dos operaciones con un fichero de texto. Para comenzar utilizaremos únicamente cuatro anotaciones de Spring:

| Anotación | Para qué sirve |
|---|---|
| `@Service` | Identifica la clase que realiza el trabajo |
| `@RestController` | Identifica la clase que recibe las peticiones |
| `@PostMapping` | Atiende una petición para escribir |
| `@GetMapping` | Atiende una petición para leer |

!!! info "Primero, un ejemplo pequeño"
    En esta primera versión el texto y la ruta serán fijos. Así podremos concentrarnos en la relación entre Spring Boot y `Files`. Más adelante veremos cómo recibir archivos, configurar la carpeta y tratar los errores.

## 1. Crear el servicio

Crea el paquete `service` dentro de `com.example.ficherosapi`. En ese paquete crea el fichero `FileService.kt`:

```kotlin
package com.example.ficherosapi.service

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path

@Service // (1)!
class FileService {

    private val file: Path = Path.of("data", "mensaje.txt") // (2)!

    fun writeMessage() {
        Files.createDirectories(file.parent) // (3)!
        Files.writeString(file, "Hola desde un fichero") // (4)!
    }

    fun readMessage(): String {
        return Files.readString(file) // (5)!
    }
}
```

1. Spring detecta esta clase, crea un objeto de `FileService` y podrá entregárselo al controlador.
2. Representa el fichero `data/mensaje.txt`. La carpeta `data` se creará en la raíz del proyecto.
3. Crea la carpeta `data` si todavía no existe.
4. Escribe el texto en el fichero. Si ya existe, sustituye su contenido.
5. Lee todo el contenido y lo devuelve como un `String`.

El servicio contiene las operaciones con el sistema de ficheros. Todavía no sabe nada de navegadores, URLs o peticiones HTTP.

## 2. Crear el controlador

Crea el paquete `controller` dentro de `com.example.ficherosapi`. En ese paquete crea `FileController.kt`:

```kotlin
package com.example.ficherosapi.controller

import com.example.ficherosapi.service.FileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController // (1)!
class FileController(
    private val fileService: FileService // (2)!
) {

    @PostMapping("/api/file") // (3)!
    fun write(): String {
        fileService.writeMessage() // (4)!
        return "Fichero guardado"
    }

    @GetMapping("/api/file") // (5)!
    fun read(): String {
        return fileService.readMessage() // (6)!
    }
}
```

1. Indica que esta clase recibe peticiones web y devuelve sus resultados en la respuesta.
2. Spring proporciona el servicio que creó anteriormente. No necesitamos escribir `FileService()`.
3. Relaciona una petición `POST /api/file` con la función `write`.
4. El controlador no escribe directamente el fichero: se lo pide al servicio.
5. Relaciona una petición `GET /api/file` con la función `read`.
6. El controlador devuelve al cliente el texto que ha leído el servicio.

## 3. Observar el recorrido

Al escribir el fichero:

```text
POST /api/file → FileController.write()
               → FileService.writeMessage()
               → Files.writeString()
               → data/mensaje.txt
```

Al leerlo:

```text
GET /api/file → FileController.read()
              → FileService.readMessage()
              → Files.readString()
              → respuesta de texto
```

El controlador se ocupa de la comunicación HTTP y el servicio se ocupa del fichero.

## 4. Probar la aplicación

Ejecuta `FicherosApiApplication.kt`. Primero crea el fichero desde PowerShell:

```powershell
Invoke-RestMethod -Method Post http://localhost:8080/api/file
```

La respuesta será:

```text
Fichero guardado
```

Comprueba que se ha creado `data/mensaje.txt` en la raíz del proyecto. Después visita esta dirección en el navegador:

```text
http://localhost:8080/api/file
```

También puedes leerlo desde PowerShell:

```powershell
Invoke-RestMethod http://localhost:8080/api/file
```

La respuesta contendrá el texto guardado:

```text
Hola desde un fichero
```

## 5. Qué hemos aprendido

- Spring recibe una petición y ejecuta la función del controlador asociada a su método y su ruta.
- El controlador delega las operaciones con ficheros en el servicio.
- El servicio continúa utilizando las clases que ya conocemos: `Path` y `Files`.
- Una respuesta puede contener directamente el texto leído de un fichero.

!!! success "Ejemplo básico completado"
    La aplicación ya conecta una petición HTTP con la escritura y lectura de un fichero. No necesitamos introducir todavía configuración externa, subida de archivos ni respuestas HTTP avanzadas.

En la siguiente página ampliaremos el proyecto para recibir un fichero enviado por el cliente, descargarlo y gestionar los posibles errores.
