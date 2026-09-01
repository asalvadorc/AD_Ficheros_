# 📋 Ficheros JSON

!!! warning "Dónde guardar los ejemplos"
    Antes de continuar, realiza la [🛠️ Preparación del proyecto](ficheros_intercambio.md) (configuración de Gradle y modelado de datos), común para los ejemplos de CSV, JSON y XML.

En muchas aplicaciones modernas, los datos deben almacenarse o intercambiarse en formato JSON (JavaScript Object Notation), un formato ligero y legible ampliamente utilizado en APIs, configuraciones, bases de datos NoSQL y almacenamiento persistente.


**Estructura**{.azul}

La estructura de los ficheros **JSON** (JavaScript Object Notation) se basa en una sintaxis sencilla y legible para representar datos estructurados. **JSON** está formado por **pares clave-valor** y/o listas ordenadas de valores.  
**Ejemplo**: Información sobre un alumno.

```json        
    "alumno": {
        "nombre": "María",
        "edad": 20,
        "activo": true,
        "notas": [8.5, 9.2, 7.8],
        "direccion": {
        "calle": "Av. del Sol",
        "ciudad": "Valencia",
        "codigoPostal": 46001}
        }
```        


**Elementos principales**{.azul}

- **Objeto:**	Conjunto de pares clave-valor, delimitado por {}: "alumno"
- **Array:**	Lista ordenada de valores, delimitada por []: "notas"
- **Clave:**	Siempre entre comillas dobles: "nombre"
- **Valor:**	Puede ser: string, número, booleano, null, objeto o array:	"María", "Valencia", 20, true, etc.

## Resumen del bloque JSON

En **Kotlin**, existen varias librerías que permiten trabajar con ficheros JSON de forma sencilla:


| Librería             | Lenguaje base | Uso recomendado                          | Multiplataforma | Notas destacadas                                  |
|----------------------|----------------|-----------|------------------|--------------------------------------------------|
| `kotlinx.serialization` | Kotlin         | Kotlin puro y Kotlin Multiplatform       | ✅ Sí             | Ligera, rápida y con soporte oficial de JetBrains |
| `Jackson`            | Java           | Proyectos Java/Kotlin con Spring Boot     | ❌ No             | Muy flexible y poderosa                          |
| `Gson`               | Java           | Aplicaciones Android o proyectos simples  | ❌ No             | Fácil de usar, pero más lenta y menos segura     |
| `org.json`           | Java           | Scripts rápidos o aprendizaje             | ❌ No             | Acceso directo a claves sin clases de datos      |


Cuando trabajamos con ficheros **JSON en Kotlin**, existen dos formas de acceder a los datos, tratarlos como texto plano o estructuras genéricas, o convertirlos directamente en objetos Kotlin. Aunque la primera opción es posible y útil en ciertos casos, trabajar sin conversión implica mayor esfuerzo manual, riesgo de errores en los nombres de claves y ausencia de validación de tipos. Las librerías **kotlinx.serialization** y **Jackson** nos permiten convertir los ficheros JSON a objetos y viceversa.

Esto proporciona importantes **ventajas**:  

✔️ Validación automática de la estructura del JSON.  
✔️ Conversión directa entre JSON y objetos Kotlin.  
✔️ Código más limpio y mantenible.  
✔️ Mayor seguridad de tipos, detectando errores en tiempo de compilación.  

## Resumen de ejemplos (JSON) {.azul}

- [kotlinx.serialization: objeto JSON](#json-kserialization)
- [Jackson: objeto JSON](#json-jackson)
- [Jackson: lista JSON](#json-lista-jackson)

## Librería: kotlinx.serialization

**kotlinx.serialization** es la librería oficial de serialización de Kotlin, desarrollada por JetBrains, que permite convertir objetos Kotlin a y desde diferentes formatos como JSON, ProtoBuf, CBOR, XML (experimental), entre otros.


Como ya vimos en el apartado anterior, **la serialización** es el proceso de convertir los datos utilizados por una aplicación a un formato que pueda transferirse por red o almacenarse en una base de datos o archivo. A su vez, la deserialización es el proceso inverso: leer datos de una fuente externa y convertirlos en un objeto de tiempo de ejecución.


!!!Note "Nota"
    Todas las bibliotecas de serialización de Kotlin pertenecen al grupo **org.jetbrains.kotlinx:grupo**. Sus nombres empiezan con _kotlinx-serialization-_ y tienen sufijos que reflejan el formato de serialización: **org.jetbrains.kotlinx:kotlinx-serialization-json**


??? info "Clases y funciones clave de kotlinx.serialization.json"

    | Clase / Función                | Tipo       | Descripción                                                                 |
    |-------------------------------|------------|-----------------------------------------------------------------------------|
    | `Json`                        | Clase      | Punto de entrada principal para serializar y deserializar en JSON          |
    | `JsonObject`                  | Clase      | Representa un objeto JSON `{}` como un `Map<String, JsonElement>`          |
    | `JsonArray`                   | Clase      | Representa un array JSON `[]`, como una lista de `JsonElement`             |
    | `JsonElement`                 | Clase      | Superclase abstracta para cualquier valor JSON                             |
    | `JsonPrimitive`               | Clase      | Representa valores primitivos JSON (string, número, booleano, null)        |
    | `JsonNull`                    | Objeto     | Representa el valor `null` en JSON                                         |
    | `JsonLiteral`                 | Clase      | Subtipo de `JsonPrimitive` que representa valores literales (string/num)   |
    | `JsonObjectBuilder`           | Clase DSL  | Permite construir objetos JSON usando `buildJsonObject { ... }`           |
    | `JsonArrayBuilder`            | Clase DSL  | Permite construir arrays JSON usando `buildJsonArray { ... }`             |
    | `buildJsonObject { ... }`     | Función    | Crea un `JsonObject` de forma declarativa                                 |
    | `buildJsonArray { ... }`      | Función    | Crea un `JsonArray` de forma declarativa                                  |
    | `parseToJsonElement(...)`     | Función    | Convierte un `String` en `JsonElement` (analiza el JSON sin clase)        |
    | `encodeToJsonElement(...)`    | Función    | Convierte un objeto Kotlin en `JsonElement` usando un `Json`              |
    | `decodeFromJsonElement(...)`  | Función    | Convierte un `JsonElement` a un objeto Kotlin                             |
    | `jsonPrimitive`               | Propiedad  | Accede al valor primitivo dentro de un `JsonElement`                      |
    | `jsonObject`                  | Propiedad  | Convierte un `JsonElement` a `JsonObject` (si es compatible)              |
    | `jsonArray`                   | Propiedad  | Convierte un `JsonElement` a `JsonArray` (si es compatible)               |


??? info "Métodos principales de kotlinx.serialization"

    Son funciones generales que no están dentro del paquete .json, pero que se usan muy a menudo en la serialización en Kotlin.

    | Método de kotlinx.serialization         | ¿Qué hace?                                                   | Ejemplo básico                                                   |
    |--------------------------------------------------------------|---------------------------------------------------------------|------------------------------------------------------------------|
    | `Json.encodeToString(objeto)`          | Convierte un objeto Kotlin a una cadena JSON.                | `Json.encodeToString(persona)`                                  |
    | `Json.encodeToString(serializer, obj)` | Igual que el anterior pero especificando el serializador.    | `Json.encodeToString(Persona.serializer(), persona)`            |
    | `Json.decodeFromString(json)`          | Convierte una cadena JSON a un objeto Kotlin.                | `Json.decodeFromString<Persona>(json)`                          |
    | `Json.decodeFromString(serializer, s)` | Igual que el anterior pero con el serializador explícito.    | `Json.decodeFromString(Persona.serializer(), json)`             |
    | `Json.encodeToJsonElement(objeto)`     | Convierte un objeto a un árbol `JsonElement`.                | `val elem = Json.encodeToJsonElement(persona)`                  |
    | `Json.decodeFromJsonElement(elem)`     | Convierte un `JsonElement` a objeto Kotlin.                  | `val persona = Json.decodeFromJsonElement<Persona>(elem)`       |
    | `Json.parseToJsonElement(string)`      | Parsea una cadena JSON a un árbol `JsonElement` sin mapear.  | `val elem = Json.parseToJsonElement(json)`                      |


**Requisitos para usar kotlinx.serialization en Gradle (JSON)**{.azul}

- Activar el plugin de Kotlin serialization en **build.gradle.kts**

```kotlin
    plugins {
        kotlin("jvm") version "2.0.20"
        kotlin("plugin.serialization") version "2.0.20"
        application
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    }
```
       

⚠️ Asegúrate de usar una versión compatible con tu Kotlin. Por ejemplo, 1.6.3 funciona bien con Kotlin 2.0.20.        

- Anota tus clases con **@Serializable**

```kotlin  
    import kotlinx.serialization.Serializable

    @Serializable
    data class Objeto(val clave1: String, val clave2: Int)
```        

!!!Note "Nota"
    En Kotlin, las **data class** están diseñadas para modelar datos puros. La palabra clave **data** no es obligatoria para la serialización, pero se usa por buena práctica y para obtener funcionalidades adicionales que son muy útiles, especialmente cuando trabajas con objetos de datos, como toString(), equals(), hashCode(), copy()..     
    


- Utiliza el objeto Json para **serializar/deserializar**      

Para serializar una instancia de esta clase llamamos a **Json.encodeToString()** y para
deserializar llamamos a **Json.decodeFromString()**.

```kotlin
        import kotlinx.serialization.json.Json
        import kotlinx.serialization.encodeToString
        import kotlinx.serialization.decodeFromString

        fun main() {
            val obj = Objeto("María", 22)

            val json = Json.encodeToString(obj)
            println("JSON: $json")

            val obj = Json.decodeFromString<Objeto>(json)
            println("Objeto: $obj")
        }
```

 También puedes serializar **colecciones de objetos**, como listas, en una sola llamada:

```kotlin
        val dataList = listOf(Objeto("María", 22), Objeto("Carlos", 30), Objeto("Ana", 18))
        val jsonList = Json.encodeToString(dataList)
```
El resultado sería:
```json
    [
        {"nombre":"Lucía","edad":28},
        {"nombre":"Carlos","edad":30},
        {"nombre":"Elena","edad":18}
    ]
```

**Ejemplo de lectura y escritura de un archivo json**{.azul}

**Estructura del proyecto:**


        Ficheros_Gradle/            
        ├── documentos/
        |       └──persona.json
        └── src/
            └── main/
                └── kotlin/
                    └── Ejemplos/
                            └──Ejemplo_JSON_KSerialization.kt 
                            └──Persona.kt


1- El fichero json que vamos a generar será el que tenga el siguiente contenido. Es un objeto JSON que contiene dos atributos, nombre y edad, representando una persona.:
```json
        {
        "nombre": "Lucía",
        "edad": 28
        }
```
2- Crea la **Data Class** **Persona.kt** con la misma estructura del archivo fuera del programa de ejemplo para poder reutilizarla desde cualquier otro main.  
Anota la clase como **serializable**.   
   
```kotlin    
    import kotlinx.serialization.Serializable

    @Serializable
    data class Persona(val nombre: String, val edad: Int)
```

<a id="json-kserialization"></a>

**🖥️ Ejemplo_JSON_KSerialization.kt**


Este ejemplo muestra el ciclo completo de trabajo con JSON usando `kotlinx.serialization`: convertir un objeto Kotlin en texto JSON, guardarlo en un archivo y volver a leerlo para reconstruir el objeto.

La finalidad del ejercicio es entender cómo una `data class` anotada con `@Serializable` puede convertirse fácilmente a JSON y recuperarse después sin tener que procesar manualmente cada campo.

```kotlin
    import kotlinx.serialization.encodeToString
    import kotlinx.serialization.json.*
    import java.nio.file.Files
    import java.nio.file.Paths
    import java.io.IOException
    import java.nio.file.Files.readString

    fun escribirJSON() {

        val ruta = Paths.get("documentos/persona.json")
        val persona = Persona("Lucía", 28)
        try {

            // Convertir a String con formato bonito
            val jsonString = Json { prettyPrint = true }.encodeToString(persona) // (1)!

            // Crear carpeta si no existe
            Files.createDirectories(ruta.parent) // (2)!

            // Escribir JSON en archivo
            Files.writeString(ruta, jsonString) // (3)!

            println("Archivo JSON creado en: ${ruta.toAbsolutePath()}")
            println("Contenido:\n$jsonString")

        } catch (e: IOException) {
            println("⚠️ Error de entrada/salida: ${e.message}")
        } catch (e: Exception) {
            println("⚠️ Error inesperado: ${e.message}")
        }
    }


    fun leerJSON(){
        val rutaEntrada = Paths.get("documentos/persona.json")

        // --- Lectura segura ---
        try {
            if (!Files.exists(rutaEntrada)) { // (4)!
                println("El archivo no existe: $rutaEntrada")
                return
            }

            val contenidoJson = readString(rutaEntrada) // (5)!
            val persona = Json.decodeFromString<Persona>(contenidoJson) // (6)!
            println("Lectura correcta: $persona")


        } catch (e: IOException) {
            println("Error de E/S: ${e.message}")
        }
    }


fun main() {
    escribirJSON()
    leerJSON()

}
```

1. Serializa el objeto `Persona` a texto JSON legible.
2. Crea la carpeta de destino antes de escribir el archivo.
3. Escribe el JSON generado en disco.
4. Comprueba que el archivo existe antes de intentar leerlo.
5. Lee el contenido JSON como texto plano.
6. Deserializa el texto y reconstruye el objeto `Persona`.
<!--
**JSON sin depender de una clase de datos**{.azul}

**🖥️ Ejemplo_JSONObject.kt**


Cuando queremos construir el JSON "a mano", sin depender de la serialización automática de la clase, utilizaremos el método **buildJsonObject**, la cual permite no tener una clase serializable y modificar campos dinámicamente.


       import kotlinx.serialization.json.*
        import java.io.IOException
        import java.nio.file.Files
        import java.nio.file.Paths


        fun escribirObjeto() {
            val ruta = Paths.get("documentos/persona_nueva.json")

            try {
                // Construir el JSON manualmente
                val jsonObject = buildJsonObject {
                    put("nombre", "Mario")
                    put("edad", 35)
                }

                // Convertirlo a String con formato bonito
                val jsonString = Json { prettyPrint = true }.encodeToString(JsonObject.serializer(), jsonObject)

                // Crear carpeta si no existe
                Files.createDirectories(ruta.parent)

                // Escribir JSON en el archivo
                Files.writeString(ruta, jsonString)

                println("Archivo JSON creado en: ${ruta.toAbsolutePath()}")
                println("Contenido:\n$jsonString")

            } catch (e: IOException) {
                println("Error de entrada/salida: ${e.message}")
            } catch (e: Exception) {
                println("Error inesperado: ${e.message}")
            }
        }

        fun leerObjeto() {
            val ruta = Paths.get("documentos/persona_nueva.json")

            if (!Files.exists(ruta)) {
                println("El archivo no existe.")
                return
            }

            try {
                val contenido = Files.readString(ruta)
                val json = Json { ignoreUnknownKeys = true }
                val jsonElement = json.parseToJsonElement(contenido)
                val jsonObject = jsonElement.jsonObject

                // Acceso a los campos manualmente
                val nombre = jsonObject["nombre"]?.jsonPrimitive?.content
                val edad = jsonObject["edad"]?.jsonPrimitive?.int

                println("Nombre leído manualmente: $nombre")
                println("Edad leída manualmente: $edad")

            } catch (e: Exception) {
                println("Error al leer o procesar el archivo: ${e.message}")
            }
        }


        fun main(){

            escribirObjeto()
            leerObjeto()
        }

-->

## Librería: Jackson JSON

**Jackson** es la librería más usada en Java para JSON. Muchos frameworks Java lo usan por defecto (Spring Boot, Micronaut, Quarkus, etc.). Conocerlo permite trabajar con APIs externas, backends y entornos mixtos (Java + Kotlin). 

Mientras que **kotlinx.serialization** está centrado en JSON y formatos binarios (CBOR, ProtoBuf...), **Jackson** también soporta XML, YAML y CSV de forma unificada. Además, si necesitas convertir entre formatos (XML ↔ JSON), Jackson es ideal. Por eso, es importante conocer ambas librerías para entender tanto proyectos Kotlin puros y modernos (kotlinx.serialization) como proyectos empresariales con Jackson.


**Clases esenciales para trabajar con JSON usando Jackson**{.azul}

Clase / interfaz|	Para qué sirve
----------------|------------------
ObjectMapper|	La clase principal para leer y escribir JSON
File (de java.io)|	Representa el archivo físico JSON
Tu data class en Kotlin|	Define la estructura del objeto a leer o escribir

??? info "Métodos principales de Jackson para JSON"

    | Método Jackson                        | ¿Qué hace?                                                     | Ejemplo básico                                                   |
    |--------------------------------------|-----------------------------------------------------------------|------------------------------------------------------------------|
    | `readValue(String, Class)`           | Convierte una cadena JSON a un objeto Kotlin o Java.           | `mapper.readValue(json, Persona::class.java)`                   |
    | `readValue(File, Class)`             | Convierte un archivo JSON a un objeto.                         | `mapper.readValue(File("persona.json"), Persona::class.java)`   |
    | `readTree(String)`                   | Lee un JSON como árbol (`JsonNode`) sin mapear a clase.        | `val node = mapper.readTree(json)`                              |
    | `writeValue(File, Object)`           | Escribe un objeto como JSON en un archivo.                     | `mapper.writeValue(File("salida.json"), persona)`               |
    | `writeValueAsString(Object)`        | Convierte un objeto en una cadena JSON.                        | `val json = mapper.writeValueAsString(persona)`                 |
    | `writeValueAsBytes(Object)`         | Convierte un objeto en un array de bytes JSON.                 | `val bytes = mapper.writeValueAsBytes(persona)`                 |
    | `writerWithDefaultPrettyPrinter()`  | Devuelve un escritor que formatea (indentado) el JSON.         | `mapper.writerWithDefaultPrettyPrinter().writeValue(...)`       |


**Anotaciones en Jackson**{.azul}

Las anotaciones en Jackson (como @JsonIgnoreProperties, @JsonProperty, etc.) no siempre son necesarias, pero se usan para resolver problemas comunes al serializar o deserializar objetos. 


Caso|	¿Anotación necesaria?
----|-------------------------
JSON coincide exactamente con la data class|	❌ No
JSON tiene campos extra	|✅ Sí (@JsonIgnoreProperties)
Nombres distintos en JSON|	✅ Sí (@JsonProperty)
Quieres ocultar campos|	✅ Sí (@JsonIgnore)


Anotación|	¿Para qué sirve?
---------|--------------------
@JsonProperty("x")|	Mapear nombres distintos entre JSON y la clase
@JsonIgnore|	Excluir una propiedad al serializar/deserializar
@JsonIgnoreProperties(ignoreUnknown = true)|	Evitar errores por campos JSON no mapeados
@JsonInclude(...)|	Excluir valores nulos o vacíos en el JSON


**Ejemplo de lectura y escritura con Jackson**{.azul}


Dependencia Gradle:

        dependencies {
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
        }

       
!!!Note "Nota"
    ⚠️ Con la librería Jackson no es obligatorio usar la anotación **@Serializable**.
    Jackson puede trabajar directamente con clases normales de Kotlin.

    En este caso, seguiremos utilizando la **data class Persona**, que ya está anotada con **@Serializable** porque se usa también en ejemplos con **kotlinx.serialization**.  
    👉 Esta anotación no afecta a Jackson ni provoca errores, por lo que la misma clase se puede reutilizar sin problemas en ambos casos.

         import kotlinx.serialization.Serializable

            @Serializable
            data class Persona(val nombre: String, val edad: Int)
            

<a id="json-jackson"></a>

**🖥️ Ejemplo_JSON_jackson.kt**


Este ejemplo muestra cómo leer y escribir archivos JSON con la librería Jackson, que permite convertir objetos Kotlin a JSON y viceversa de forma muy directa.

La finalidad del ejercicio es comprobar cómo Jackson simplifica la conversión entre objetos Kotlin y JSON sin necesidad de procesar manualmente el texto del archivo.

```kotlin
        
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File


fun escribirJson() {

    val mapper = jacksonObjectMapper() // (1)!

    val persona = Persona("Mario", 35)
    val archivo = File("documentos/persona.json")

    mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, persona) // (2)!

    println("JSON generado correctamente en: ${archivo.absolutePath}")
}
fun leerJson() {

    val mapper = jacksonObjectMapper() // (3)!
    val archivo = File("documentos/persona.json")

    val persona = mapper.readValue<Persona>(archivo) // (4)!
    println("Lectura correcta: ${persona.nombre} tiene ${persona.edad} años.")
}

fun main() {
    escribirJson()
    leerJson()
}
```

1. Crea el `ObjectMapper` de Jackson.
2. Escribe el objeto `Persona` como JSON en el archivo.
3. Crea de nuevo el `ObjectMapper` para leer el fichero.
4. Lee el JSON del archivo y reconstruye el objeto `Persona`.


       


**Ejemplo de lectura y escritura de un array (lista) de elementos con Jackson**{.azul}        

!!!Note "Fichero JSON compuesto por una lista de elementos"
    Si el fichero **JSON** contiene un *array* (`[...]`), es decir, una **lista de objetos**, entonces debemos indicar explícitamente que queremos leer un `List<Objeto>`.  
   
```json 
    [ 
        {
        "nombre" : "Lucía",
        "edad" : 28
        }, {
        "nombre" : "Pepe",
        "edad" : 30
        }, {
        "nombre" : "Ana",
        "edad" : 50
        }, {
        "nombre" : "Juan",
        "edad" : 12
        } 
    ]
```

<a id="json-lista-jackson"></a>

**🖥️ Ejemplo_listaJSON_jackson.kt**


Este ejemplo amplía el caso anterior para trabajar no con un único objeto, sino con una lista completa de objetos `Persona` almacenada en un archivo JSON.

La finalidad del ejercicio es entender que Jackson no solo puede serializar objetos individuales, sino también colecciones completas, facilitando el intercambio de listas de datos en formato JSON.

```kotlin
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File


fun escribirListaJson() {

    val mapper = jacksonObjectMapper() // (1)!

    val personas = listOf(
        Persona("Lucía", 28),
        Persona("Pepe", 30),
        Persona("Ana", 50),
        Persona("Juan", 12)
    )
    val archivo = File("documentos/lista_personas.json")

    mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, personas) // (2)!

    println("JSON generado correctamente en: ${archivo.absolutePath}")
    }

    fun leerListaJson() {

        val mapper = jacksonObjectMapper() // (3)!
        val archivo = File("documentos/lista_personas.json")

        val lista=mapper.readValue<List<Persona>>(archivo) // (4)!


        for (p in lista) {
            println("${p.nombre} tiene ${p.edad} años.")
        }

    }

fun main() {
            escribirListaJson()
            leerListaJson()
}    

```    

1. Crea el `ObjectMapper` para trabajar con la lista.
2. Serializa la lista completa en JSON.
3. Crea el `ObjectMapper` para la lectura.
4. Lee el array JSON y lo reconstruye como `List<Persona>`.
