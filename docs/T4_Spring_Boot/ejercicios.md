# 📝 Ejercicio 4: API de gestión de apuntes

!!! info "Estructura del proyecto"
    Continúa en el proyecto Spring Boot `ficheros-api` desarrollado durante el tema. Dentro del paquete principal `com.example.ficherosapi`, organiza las nuevas clases en los paquetes `model`, `service` y `controller`.

## 📋 Enunciado

Crea una **API de gestión de apuntes** que permita guardar, leer, listar y subir ficheros de texto mediante peticiones HTTP. Los apuntes se almacenarán en una carpeta llamada `apuntes` en el ordenador servidor. El cliente enviará los datos mediante Postman y recibirá una respuesta de la API.

!!! info "Código base"
    Utiliza los ejemplos de [crear, leer y listar texto](texto_y_listado.md) y [subir un fichero existente](gestion_ficheros.md) como referencia. Conserva las operaciones anteriores y añade las de apuntes con sus propias clases y rutas.

!!! tip "📋 Operaciones de la API"
    | Método | Ruta | Operación |
    |---|---|---|
    | `POST` | `/api/apuntes` | Crear o sustituir un apunte con datos JSON |
    | `GET` | `/api/apuntes?nombre=repaso.txt` | Leer el apunte indicado |
    | `GET` | `/api/apuntes/listado` | Listar los nombres de los apuntes |
    | `POST` | `/api/apuntes/upload` | Subir un fichero de texto existente |
    | `POST` | `/api/apuntes/importar-csv` | Importar varios apuntes desde un CSV |
    | `GET` | `/api/apuntes/xml?nombre=repaso.txt` | Devolver un apunte en formato XML |
    | `POST` / `GET` | `/api/apuntes/imagen` | Subir y mostrar una imagen |

## Objetivo

- Reutilizar las operaciones con ficheros en una aplicación web.
- Separar la recepción de peticiones, las operaciones y la representación de los datos.
- Enviar JSON y archivos desde un cliente HTTP.
- Comprobar que los datos permanecen guardados en el servidor.

## 🛠️ Requisitos técnicos

### Organización y almacenamiento

La estructura de las nuevas clases será:

```text
src/main/kotlin/com/example/ficherosapi/
├── controller/
│   └── ApunteController.kt
├── model/
│   └── CrearApunteRequest.kt
└── service/
    └── ApunteService.kt
```

- `CrearApunteRequest` representará los campos `nombre` y `contenido`, ambos de tipo `String`.
- El controlador recibirá las peticiones y llamará al servicio. Las operaciones de lectura, escritura y listado estarán en el servicio.
- Utiliza `Path` y `Files`. Crea automáticamente la carpeta `apuntes` si no existe, en el directorio de trabajo del servidor. Al ejecutar desde la raíz del proyecto, aparecerá allí.
- Acepta nombres simples terminados en `.txt`, como `repaso.txt`. Rechaza nombres vacíos, `.` y `..`, separadores de carpetas (`/` y `\`) y dos puntos (`:`). Puedes adaptar `resolveName` de los ejemplos y añadir la comprobación de la extensión.
- Trabaja con texto UTF-8 y ficheros de prueba normales. Cierra los flujos mediante `use` cuando corresponda.
- Los datos se recibirán mediante HTTP: estas operaciones no pedirán información con `readln()` ni usarán `println()` como respuesta al cliente.

### Reto 1. Crear o sustituir un apunte

`POST /api/apuntes` recibirá un JSON con el nombre del fichero y su contenido. Guardará el texto en `apuntes` y devolverá una confirmación con el nombre.

Si ya existe un apunte con ese nombre, se sustituirá su contenido. El texto puede contener varias líneas; no se utiliza la palabra `FIN`.

### Reto 2. Leer un apunte

`GET /api/apuntes?nombre=repaso.txt` devolverá como texto el contenido del fichero indicado. El nombre se recibirá mediante `@RequestParam`.

### Reto 3. Listar los apuntes

`GET /api/apuntes/listado` devolverá una lista JSON con los nombres de los ficheros de `apuntes`, ordenados alfabéticamente.

**Solo aparecerán ficheros normales terminados en `.txt`**: no se incluirán carpetas ni archivos de otro tipo. Si no hay apuntes, devolverá `[]`.

### Reto 4. Subir un apunte existente

`POST /api/apuntes/upload` recibirá un fichero mediante `MultipartFile`, en un campo de formulario llamado `file`.

Guardará una copia en `apuntes`, conservando el nombre recibido y aplicando las comprobaciones indicadas. Rechaza archivos vacíos. Si ya existe uno con ese nombre, sustituye su contenido. Devuelve una confirmación con el nombre guardado.

!!! info "Alcance de los errores"
    Las comprobaciones básicas de nombres y archivos vacíos son obligatorias y pueden realizarse con `require`, como en los ejemplos. En esta práctica no se exige personalizar los mensajes ni los códigos HTTP de error. Las pruebas de funcionamiento utilizarán datos válidos y crearán los ficheros antes de leerlos.

## 📤 Ejemplo de peticiones y respuestas

Arranca la aplicación y utiliza Postman en el mismo ordenador. Las URLs comenzarán por `http://localhost:8080`.

### Crear y leer

En una petición **POST** a `/api/apuntes`, selecciona **Body → raw → JSON** y envía:

```json
{
  "nombre": "repaso.txt",
  "contenido": "Repasar lectura de ficheros.\nPracticar peticiones HTTP."
}
```

Respuesta esperada:

```text
Apunte guardado: repaso.txt
```

Envía después un **GET** a `/api/apuntes?nombre=repaso.txt`, sin cuerpo. Deberías recibir:

```text
Repasar lectura de ficheros.
Practicar peticiones HTTP.
```

Cambia el contenido del JSON, repite el POST y vuelve a leer. Comprueba que recibes el texto nuevo sin modificar Kotlin ni reiniciar la aplicación.

### Subir y listar

Crea en el ordenador cliente un archivo UTF-8 llamado `clase.txt` con algún texto. En Postman:

1. Crea una petición **POST** a `/api/apuntes/upload`.
2. Selecciona **Body → form-data**.
3. Añade una clave `file`, cambia su tipo a **File** y selecciona `clase.txt`.
4. Pulsa **Send**. Deja que Postman genere la cabecera de este formulario; no reutilices una cabecera manual `Content-Type: application/json`.

Debes recibir `Apunte guardado: clase.txt`. Comprueba su contenido mediante **GET** a `/api/apuntes?nombre=clase.txt`.

Si estos son los únicos apuntes, **GET** a `/api/apuntes/listado`, sin cuerpo, devolverá:

```json
["clase.txt", "repaso.txt"]
```

Para comprobar el filtro del listado, crea manualmente en la carpeta `apuntes` del servidor un fichero `ignorar.csv` y una carpeta `carpeta.txt`. Ninguno debe aparecer en la respuesta.

### Reto 5. Importar apuntes desde CSV

Añade `POST /api/apuntes/importar-csv` para recibir, en el campo `file`, un CSV UTF-8 con la cabecera `nombre,contenido`:

```csv
nombre,contenido
tema1.txt,Repasar las rutas de los ficheros.
tema2.txt,Practicar la lectura de texto.
```

Lee el CSV recibido y crea un apunte por cada fila, reutilizando el servicio de escritura y sus comprobaciones. Para este reto utilizaremos campos sin comas, comillas ni saltos de línea internos. Si un apunte ya existe, sustituye su contenido.

Devuelve una lista JSON con los nombres importados:

```json
["tema1.txt", "tema2.txt"]
```

Este reto sirve para practicar la lectura de CSV y forma parte de la evaluación del ejercicio.

### Reto 6. Convertir un apunte a XML

Este reto relaciona la API con los formatos estudiados anteriormente.

Añade `GET /api/apuntes/xml?nombre=repaso.txt`. La API leerá el apunte y devolverá una respuesta XML con esta estructura:

```xml
<apunte>
  <nombre>repaso.txt</nombre>
  <contenido>Repasar lectura de ficheros.</contenido>
</apunte>
```

Puedes crear una clase de respuesta, utilizar `XmlMapper` de Jackson o construir el XML con las herramientas que ya conoces. La conversión debe realizarse a partir del contenido del apunte guardado en `apuntes`.

### Reto 7. Subir y mostrar una imagen

Añade una operación para imágenes:

| Método | Ruta | Operación |
|---|---|---|
| `POST` | `/api/apuntes/imagen` | Recibe una imagen mediante `MultipartFile` y la guarda en `apuntes` |
| `GET` | `/api/apuntes/imagen?nombre=foto.png` | Devuelve los bytes de la imagen para que el navegador pueda mostrarla |

La subida debe aceptar únicamente extensiones como `.png`, `.jpg` o `.jpeg`, rechazar archivos vacíos y conservar el nombre validado. Para la lectura, el controlador puede devolver `ByteArray` junto con el tipo MIME adecuado mediante `ResponseEntity`.

Por ejemplo, una respuesta correcta debe indicar `image/png` para una imagen PNG. El navegador podrá mostrarla si visitas la URL GET directamente. No conviertas la imagen a texto ni la leas con `Files.readString`: las imágenes son ficheros binarios.

Para probarlo, selecciona una imagen en Postman mediante **Body → form-data → file** y, después de subirla, abre la URL GET en el navegador.

## Antes de entregar

- [ ] La aplicación arranca y crea `apuntes` si la carpeta no existe.
- [ ] El modelo, el controlador y el servicio están separados.
- [ ] Puedo crear y leer un apunte con varias líneas.
- [ ] Un segundo POST con el mismo nombre sustituye el contenido.
- [ ] El listado devuelve solo ficheros `.txt`, ordenados por nombre.
- [ ] Una carpeta sin apuntes devuelve `[]`.
- [ ] Puedo subir un archivo y leer su contenido desde la API.
- [ ] Se rechazan los nombres no admitidos y los archivos subidos vacíos.
- [ ] Los apuntes siguen disponibles después de reiniciar la aplicación.
- [ ] Las operaciones anteriores del proyecto siguen disponibles.
- [ ] La colección de Postman contiene las operaciones de los siete retos.

## 📦 Entrega


1. **El proyecto Spring Boot** comprimido en un arhivo **.zip** con el código fuente y los archivos de Gradle necesarios para abrirlo y ejecutarlo. No incluyas las carpetas `.gradle` ni `build`.
2. **Una colección de Postman** exportada con las peticiones de los siete retos y los datos de ejemplo. Incluye `clase.txt` y una imagen de prueba por separado: al importar la colección en otro ordenador habrá que volver a seleccionarlos para las subidas.
3. **Un vídeo breve** en el que expliques la estructura del programa y muestres su ejecución desde Postman. Deben verse al menos la organización en `model`, `service` y `controller`, el arranque de la aplicación y algunas peticiones de los retos.

<!--
3. Un documento breve con evidencias de creación, lectura, sustitución, subida y filtrado del listado, y las respuestas a estas preguntas:
    - ¿En qué ordenador se guardan los apuntes si el cliente y el servidor están en equipos distintos?
    - ¿Qué hace el controlador y qué hace el servicio?
    - ¿Por qué escribir la URL en la barra del navegador no ejecuta el POST?

-->

## ✅ Rúbrica de evaluación

La calificación se obtiene sumando los siguientes apartados:

| Reto | Aspectos evaluados | Puntuación máxima |
|---|---|:---:|
| **Organización del proyecto** | Modelo, controlador y servicio separados; responsabilidades bien distribuidas y código legible. | **1,0** |
| **Creación de apuntes** | Recibe JSON, guarda texto con varias líneas y sustituye el contenido cuando corresponde. | **1,5** |
| **Lectura de apuntes** | Recibe el nombre en la URL y devuelve el contenido del fichero solicitado. | **1,0** |
| **Listado de apuntes** | Devuelve JSON ordenado, filtra ficheros `.txt`, excluye carpetas y admite un listado vacío. | **1,0** |
| **Subida de apuntes** | Recibe el campo `file`, copia el archivo y permite leerlo después. | **1,0** |
| **Importación CSV** | Lee el CSV recibido, crea o sustituye los apuntes y devuelve sus nombres. | **1,25** |
| **Conversión a XML** | Lee un apunte y genera una respuesta XML con su nombre y contenido. | **1,0** |
| **Gestión de imágenes** | Sube, valida, almacena y devuelve imágenes con el tipo MIME correcto. | **1,0** |
| **Almacenamiento y comprobaciones** | Crea la carpeta, conserva los datos, comprueba nombres y archivos vacíos y cierra los recursos. | **0,75** |
| **Pruebas y comprensión** | Entrega la colección, las evidencias y un vídeo breve; explica correctamente el papel del cliente, del controlador y del servicio. | **0,5** |
| | **TOTAL** | **10,0** |
