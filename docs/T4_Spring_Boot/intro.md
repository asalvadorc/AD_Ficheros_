---
hide:
  - toc
---

# Introducción a Spring Boot

Hasta ahora nuestros programas se ejecutaban desde una función `main`, leían datos del teclado y trabajaban con ficheros del ordenador. En esta unidad convertiremos esas operaciones en una **aplicación web**.

El objetivo no es aprender todo Spring, sino entender lo necesario para publicar nuestras operaciones con ficheros mediante una API REST.

## ¿Qué es una aplicación web?

En una aplicación web intervienen normalmente dos programas:

1. El **cliente** envía una petición. Puede ser un navegador, una aplicación móvil o una herramienta como Postman.
2. El **servidor** recibe la petición, ejecuta una operación y devuelve una respuesta.

Por ejemplo:

```text
GET http://localhost:8080/saludo
                 │
                 ▼
        Aplicación Spring Boot
                 │
                 ▼
        Hola desde Spring Boot
```

`localhost` representa nuestro propio ordenador y `8080` es el puerto en el que, por defecto, escucha la aplicación.

## ¿Qué es Spring Boot?

**Spring Framework** proporciona herramientas para crear aplicaciones Java y Kotlin. **Spring Boot** simplifica su configuración y permite iniciar un servidor web desde una función `main`.

Spring Boot se encarga, entre otras cosas, de:

- iniciar el servidor web;
- recibir peticiones HTTP;
- convertir objetos Kotlin a JSON y viceversa;
- crear y conectar los objetos que necesita la aplicación;
- centralizar la configuración.

!!! info "No es un servidor externo"
    Para estos ejemplos no instalaremos Apache Tomcat por separado. Spring Boot incluye y configura el servidor necesario dentro del proyecto.

## Las capas de nuestra aplicación

Separaremos el programa en tres responsabilidades:

| Capa | Responsabilidad | Ejemplo |
|---|---|---|
| Controlador | Recibir peticiones y devolver respuestas | `FileController` |
| Servicio | Aplicar las reglas de la aplicación | `FileService` |
| Sistema de ficheros | Guardar y recuperar los datos | `Files`, `Path` |

```text
Petición HTTP → Controller → Service → Fichero
Respuesta HTTP ← Controller ← Service ← Fichero
```

Esta separación permite cambiar la interfaz sin reescribir las operaciones con ficheros.

## Vocabulario mínimo

### Endpoint

Es una operación disponible en una URL concreta. Un endpoint se identifica mediante una ruta y un método HTTP.

| Método | Uso habitual | Ejemplo |
|---|---|---|
| `GET` | Consultar | Obtener la lista de ficheros |
| `POST` | Crear o enviar datos | Subir un fichero |
| `PUT` | Sustituir o actualizar | Cambiar el contenido |
| `DELETE` | Eliminar | Borrar un fichero |

### JSON

Es un formato de texto utilizado para intercambiar datos:

```json
{
  "nombre": "informe.txt",
  "tamano": 128
}
```

### Anotación

Una anotación comienza por `@` y proporciona información a Spring:

```kotlin
@RestController
class SaludoController
```

No ejecutamos manualmente el controlador. Spring detecta la anotación, crea el objeto y lo utiliza cuando llega una petición apropiada.

### Inyección de dependencias

Si un controlador necesita un servicio, lo declara en su constructor:

```kotlin
@RestController
class FileController(
    private val fileService: FileService
)
```

Spring crea ambos objetos y entrega el servicio al controlador. Esto recibe el nombre de **inyección de dependencias**.

## Qué vamos a construir

Al terminar tendremos una API con estas operaciones:

```text
GET    /api/files              Lista los ficheros
POST   /api/files              Sube un fichero
GET    /api/files/{nombre}     Descarga un fichero
DELETE /api/files/{nombre}     Elimina un fichero
```

Antes de trabajar con ficheros crearemos una aplicación mínima para comprender cómo se recibe una petición.

## Recursos oficiales

- [Tutorial de Spring Boot con Kotlin](https://spring.io/guides/tutorials/spring-boot-kotlin/)
- [Guía de Spring para subir ficheros](https://spring.io/guides/gs/uploading-files/)

