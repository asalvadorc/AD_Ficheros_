# Primera aplicación con Spring Boot

En esta primera aplicación construiremos el código **por etapas**. Los primeros ejemplos son deliberadamente sencillos y sirven para observar el recorrido de una petición HTTP.

```text
Cliente → Controlador → Respuesta
```

## 1. Crear el proyecto

Abre [Spring Initializr](https://start.spring.io/) y selecciona:

| Opción | Valor |
|---|---|
| Project | Gradle - Kotlin |
| Language | Kotlin |
| Packaging | Jar |
| Java | 17 o la versión indicada por el profesor |
| Group | `com.example` |
| Artifact | `ficheros-api` |

Añade estas dependencias:

- **Spring Web**: permite crear controladores y endpoints HTTP.
- **Validation**: permite validar los datos recibidos.

Pulsa **Generate**, descomprime el proyecto y ábrelo desde IntelliJ IDEA mediante su fichero `build.gradle.kts`.

!!! warning "Utiliza el proyecto generado"
    No copies números de versión de otros proyectos. Spring Initializr genera una combinación compatible de Spring Boot, Kotlin, Java y Gradle.

## 2. Reconocer la estructura

```text
ficheros-api/
├── build.gradle.kts
├── gradlew
├── gradlew.bat
└── src/
    ├── main/
    │   ├── kotlin/com/example/ficherosapi/
    │   │   └── FicherosApiApplication.kt
    │   └── resources/
    │       └── application.properties
    └── test/
```

- `build.gradle.kts`: dependencias y configuración del proyecto.
- `src/main/kotlin`: código Kotlin de la aplicación.
- `src/main/resources`: configuración y recursos.
- `src/test`: pruebas automáticas.

La clase principal tendrá un aspecto similar a este:

```kotlin
package com.example.ficherosapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication // (1)!
class FicherosApiApplication

fun main(args: Array<String>) {
    runApplication<FicherosApiApplication>(*args) // (2)!
}
```

1. Indica que esta es la clase principal de una aplicación Spring Boot.
2. Crea el contexto de Spring e inicia el servidor web.

## 3. Crear el primer controlador

Crea el paquete `controller` y, dentro de él, el fichero `SaludoController.kt`:

En esta primera versión devolvemos el saludo directamente para observar qué ocurre cuando se visita una URL. **Todavía no es la organización definitiva del código.**

```kotlin
package com.example.ficherosapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController // (1)!
class SaludoController {

    @GetMapping("/api/saludos") // (2)!
    fun saludar(): String {
        return "Hola desde Spring Boot" // (3)!
    }
}
```

1. Spring detecta esta clase y la utiliza para atender peticiones web.
2. Esta función se ejecuta al recibir una petición `GET` en `/api/saludos`. Utilizamos el prefijo `/api` para identificar los endpoints de la API y el plural `saludos` para nombrar el recurso.
3. El valor devuelto se convierte en el cuerpo de la respuesta HTTP.

Ejecuta `FicherosApiApplication.kt` y abre:

```text
http://localhost:8080/api/saludos
```

También puedes usar PowerShell:

```powershell
Invoke-RestMethod http://localhost:8080/api/saludos
```

## 4. Recibir un dato en la URL

Podemos declarar una parte variable de la ruta mediante `@PathVariable`:

Por ahora continuamos trabajando únicamente en el controlador para concentrarnos en cómo llega el dato desde la URL.

```kotlin
import org.springframework.web.bind.annotation.PathVariable

@GetMapping("/api/saludos/{nombre}") // (1)!
fun saludarA(
    @PathVariable nombre: String // (2)!
): String {
    return "Hola, $nombre"
}
```

1. `{nombre}` señala la parte de la ruta que puede variar.
2. `@PathVariable` copia el valor de la URL en el parámetro `nombre`.

La petición `GET /api/saludos/Ana` devuelve `Hola, Ana`.

## 5. Devolver un objeto como JSON

Hasta ahora la función devolvía un `String`, por lo que el cliente recibía únicamente texto:

```text
Hola, Ana
```

Comenzamos así porque es la respuesta más sencilla posible: permite comprobar el recorrido entre el navegador y el controlador sin introducir todavía nuevos conceptos.

Sin embargo, una API suele necesitar devolver varios datos relacionados. En lugar de fabricar manualmente una cadena, podemos devolver un objeto y dejar que Spring lo convierta a **JSON**.

| Respuesta | Ventaja | Limitación |
|---|---|---|
| Texto | Es fácil de crear y visualizar | No separa los datos |
| JSON | Es estructurado y fácil de procesar | Requiere definir un modelo |

Por ejemplo, en JSON podemos enviar por separado el mensaje y su longitud. Si posteriormente necesitamos añadir la fecha o el idioma, incorporamos otra propiedad sin tener que interpretar una frase.

Crea el paquete `model` dentro de `com.example.ficherosapi`. Dentro de él, crea el fichero `SaludoResponse.kt`:


```kotlin
package com.example.ficherosapi.model

data class SaludoResponse( // (1)!
    val mensaje: String,
    val longitud: Int
)
```

1. Esta clase define la estructura de la respuesta: tendrá los campos `mensaje` y `longitud`.

Ahora modifica el controlador para que devuelva un objeto `SaludoResponse`:

```kotlin
import com.example.ficherosapi.model.SaludoResponse

@GetMapping("/api/saludos/{nombre}")
fun obtenerSaludo(@PathVariable nombre: String): SaludoResponse {
    val mensaje = "Hola, $nombre"

    return SaludoResponse(mensaje, mensaje.length) // (1)!
}
```

1. Spring convierte automáticamente este objeto Kotlin a JSON.

Spring convierte automáticamente el objeto a JSON:

```json
{
  "mensaje": "Hola, Ana",
  "longitud": 9
}
```

## 6. Añadir un servicio

Ya conocemos el recorrido de la petición, `@PathVariable` y la conversión a JSON. Ahora reorganizaremos el código: el controlador no debería contener las reglas del programa, por lo que trasladaremos la creación del saludo a un servicio.

Al separar la lógica, distinguimos el recorrido de la petición y el de la respuesta:

```text
Petición:   Cliente → Controlador → Servicio
Respuesta:  Cliente ← Controlador ← Servicio
```

El controlador recibe los datos de la petición y se los entrega al servicio. El servicio realiza el trabajo y devuelve el resultado al controlador, que lo envía al cliente.

Crea el paquete `service` dentro de `com.example.ficherosapi`. Dentro del nuevo paquete, crea el fichero `SaludoService.kt`:

```kotlin
package com.example.ficherosapi.service

import com.example.ficherosapi.model.SaludoResponse
import org.springframework.stereotype.Service

@Service // (1)!
class SaludoService {
    fun crearSaludo(nombre: String): SaludoResponse { // (2)!
        val mensaje = "Hola, $nombre"
        return SaludoResponse(mensaje, mensaje.length)
    }
}
```

1. `@Service` indica que esta clase contiene lógica de la aplicación. Spring creará y administrará automáticamente un objeto de esta clase.
2. La creación del mensaje y de la respuesta ya no se realiza en el controlador: ahora es responsabilidad del servicio.

Ahora abre `controller/SaludoController.kt` y sustituye su contenido por el siguiente. El controlador solicitará el servicio mediante su constructor:

```kotlin
package com.example.ficherosapi.controller

import com.example.ficherosapi.model.SaludoResponse
import com.example.ficherosapi.service.SaludoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SaludoController(
    private val saludoService: SaludoService // (1)!
) {

    @GetMapping("/api/saludos/{nombre}")
    fun saludarA(@PathVariable nombre: String): SaludoResponse {
        return saludoService.crearSaludo(nombre) // (2)!
    }
}
```

1. No utilizamos `SaludoService()` para crear el objeto. Spring proporciona al controlador el servicio que ha creado previamente. Esto es la **inyección de dependencias**.
2. El controlador recibe la petición, pero delega la lógica en el servicio.

La estructura del código será ahora:

```text
src/main/kotlin/com/example/ficherosapi/
├── FicherosApiApplication.kt
├── controller/
│   └── SaludoController.kt
├── model/
│   └── SaludoResponse.kt
└── service/
    └── SaludoService.kt
```

!!! success "Primera aplicación completada"
    Esta es la versión que debemos conservar. El cliente llama al controlador, el controlador recoge el nombre y llama al servicio, el servicio crea el saludo y Spring devuelve la respuesta.

    Los controladores se ocupan de HTTP; los servicios contienen la lógica de la aplicación. En el próximo ejemplo, `FileController` recibirá las peticiones y `FileService` realizará las operaciones con ficheros.

## Problemas habituales

| Problema | Posible causa |
|---|---|
| El puerto 8080 está ocupado | Otra aplicación sigue ejecutándose |
| Aparece un error 404 | La ruta escrita no coincide con `@GetMapping` |
| Spring no encuentra una clase | Está fuera del paquete raíz `com.example.ficherosapi` |
| Gradle no descarga dependencias | No hay conexión o IntelliJ está en modo sin conexión |
