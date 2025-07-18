# Ficheros de intercambio

En el desarrollo de aplicaciones, es habitual trabajar con datos almacenados o transmitidos en distintos formatos de ficheros. La conversión entre estos formatos permite intercambiar información entre sistemas heterogéneos, integrarse con APIs, facilitar la persistencia de datos o adaptarse a requisitos específicos. 


Los formatos más comunes con los que se trabaja incluyen:

Formato|	Descripción|	Soporte en Kotlin
-------|---------------|----------------------
JSON|	Formato de texto ligero para intercambio de datos estructurados.|	kotlinx.serialization, Gson, Jackson
XML|	Formato estructurado y extensible muy usado en sistemas legados.|	JAXB (via Java), kotlinx-serialization-xml, Simple XML
CSV|	Formato simple de texto para datos tabulares (separados por comas).|	Librerías como OpenCSV, Kotlin CSV
YAML|	Formato legible para humanos, útil para configuración.|	jackson-dataformat-yaml, SnakeYAML
PROTOBUF|	Formato binario eficiente para transmisión entre sistemas.|	kotlinx.serialization-protobuf, Protobuf oficial de Google
AVRO|	Formato binario usado en Big Data (Apache Avro).|	A través de librerías Java
TOML/INI|	Archivos de configuración estructurada.|	Soportado por librerías externas


**¿Cuándo utilizar cada uno de ellos?**{.azul}

- JSON y XML: para APIs REST, configuración, exportación de datos.
- CSV: para importar/exportar datos desde hojas de cálculo.
- YAML / TOML: en archivos de configuración de aplicaciones.
- PROTOBUF / AVRO: en comunicaciones eficientes, microservicios, Big Data.


## Uso de Gradle para trabajar con ficheros CSV, JSON y XML


En este apartado vamos a desarrollar una aplicación en Kotlin que gestione la lectura y escritura de datos utilizando distintos formatos de archivo estructurado: CSV, JSON y XML.

Para facilitar el uso de librerías externas que nos ayuden a trabajar con estos formatos, vamos a utilizar **Gradle** como herramienta de construcción del proyecto. Gradle nos permitirá:

- Gestionar las dependencias necesarias (como OpenCSV o kotlinx.serialization).
- Automatizar el proceso de compilación y ejecución.
- Organizar el proyecto de forma profesional y escalable.

Utilizaremos el archivo **build.gradle.kts** (Kotlin DSL) para declarar las dependencias y configurar el proyecto.

| Formato | Librería             | Propósito principal                                                              |
|---------|----------------------|----------------------------------------------------------------------------------|
| CSV     | OpenCSV              | Lectura y escritura de archivos separados por comas o punto y coma              |
| JSON    | kotlinx.serialization| Conversión entre objetos Kotlin y texto JSON (ligero, multiplataforma, oficial) |
| JSON    | Jackson              | Conversión entre objetos Java/Kotlin y JSON (muy usado en backend Java)         |
| XML     | javax.xml (DOM API)  | Construcción y manipulación manual de documentos XML (bajo nivel, detallado)    |
| XML     | Jackson              | Conversión directa entre objetos y XML (usando anotaciones, más sencillo)       |



En el fichero **build.gradle.kts** se incluirán los plugins y dependencias necesarias:

        plugins {
            kotlin("jvm") version "2.0.20"
            kotlin("plugin.serialization") version "2.0.20"
            application
        }


        repositories {
            mavenCentral()
        }

        dependencies {
            // Kotlin estándar
            implementation(kotlin("stdlib"))

            // Serialización JSON
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

            // OpenCSV para CSV
            implementation("com.opencsv:opencsv:5.9")

            // librería JDOM2
            implementation("org.jdom:jdom2:2.0.6")

            // librerias jackson
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
            implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.0")

        }


## Ficheros CSV  

El formato CSV es un archivo de texto donde los valores están separados por comas u otro delimitador (como punto y coma), muy usado para intercambiar datos entre hojas de cálculo, sistemas contables, etc.

La lectura y escrituara de un archivo CSV se puede hacer de dos maneras:

1. Sin utilizar librerías y mediante la lectura línea a línea + split().
2. Utilizando la librería opencsv.

| Método de OpenCSV                         | ¿Qué hace?                                                     | Ejemplo básico                                                         |
|-------------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------------|
| `CSVReader(FileReader)`                   | Crea un lector de líneas CSV.                                   | `val reader = CSVReader(FileReader("archivo.csv"))`                   |
| `readAll()`                               | Lee todo el contenido como `List<Array<String>>`.               | `val filas = reader.readAll()`                                        |
| `readNext()`                              | Lee una fila (línea) del CSV como `Array<String>`.              | `val fila = reader.readNext()`                                        |
| `CSVWriter(FileWriter)`                   | Crea un escritor CSV.                                           | `val writer = CSVWriter(FileWriter("salida.csv"))`                    |
| `writeNext(Array<String>)`                | Escribe una línea al CSV.                                       | `writer.writeNext(arrayOf("Mario", "35"))`                            |
|



**Ejemplo de lectura y escritura de un archivo CSV sin librerias y con la librería OpenCSV**{.azul}

contenido del archivo:

        Lucía;9
        Carlos;8
        Elena;10




**Ejemplo_CSV_lect_esc.kt**: Sin librerias.

        import java.nio.file.Files
        import java.nio.file.Paths
        import java.nio.file.StandardOpenOption

        fun main() {
            val ruta = Paths.get("documentos/alumnos1.csv")

            // 1. Crear contenido CSV
            val lineas = listOf(
                "Lucía;9",
                "Carlos;8",
                "Elena;10"
            )

            // 2. Escribir el archivo
            Files.write(ruta, lineas, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
            println("Archivo CSV creado: ${ruta.toAbsolutePath()}")

            // 3. Leer y mostrar contenido 
            println("\nContenido del archivo CSV:")
            val lineasLeidas = Files.readAllLines(ruta)

            for (i in lineasLeidas.indices) {
                val linea = lineasLeidas[i]
                val partes = linea.split(";")
                if (partes.size == 2) {
                    val nombre = partes[0]
                    val nota = partes[1]
                    println("Alumno: $nombre, Nota: $nota")
                } else {
                    println("Línea mal formada: $linea")
                }
            }
        }



**Ejempo_OpenCSV_lect_esc.kt**: Con OpenCSV


        import com.opencsv.CSVReaderBuilder
        import com.opencsv.CSVWriterBuilder
        import com.opencsv.CSVParserBuilder
        import java.io.FileReader
        import java.io.FileWriter
        import java.nio.file.Paths

        fun main() {
            val ruta = Paths.get("documentos/alumnos2.csv").toFile()

            // 1. Escribir el archivo CSV con punto y coma como separador
            val escritor = CSVWriterBuilder(FileWriter(ruta))
                .withSeparator(';')
                .build()

            escritor.writeNext(arrayOf("Lucía", "9"))
            escritor.writeNext(arrayOf("Carlos", "8"))
            escritor.writeNext(arrayOf("Elena", "10"))
            escritor.close()

            println("Archivo CSV creado: ${ruta.absolutePath}")

            // 2. Leer el archivo CSV 
            val lector = CSVReaderBuilder(FileReader(ruta))
                .withCSVParser(CSVParserBuilder().withSeparator(';').build())
                .build()

            println("\nContenido del archivo CSV:")
            var fila: Array<String>? = lector.readNext()

            while (fila != null) {
                if (fila.size >= 2) {
                    val nombre = fila[0]
                    val nota = fila[1]
                    println("Alumno: $nombre, Nota: $nota")
                } else {
                    println("Línea mal formada.")
                }
                fila = lector.readNext()
            }

            lector.close()
        }


!!!Note "Nota"
    El archivo CSV generado sin librerías es un archivo de texto plano con el separador **;**, pero sin comillas y sin escape. En cambio, el fichero CSV generado con OpenCSV sigue el estantar CSV (RFC 4180) que incluye encerrar los campos entre comillas dobles, si el campo contiene el separador (como **;** o **,**).


## Ficheros JSON

En muchas aplicaciones modernas, los datos deben almacenarse o intercambiarse en formato JSON (JavaScript Object Notation), un formato ligero y legible ampliamente utilizado en APIs, configuraciones, bases de datos NoSQL y almacenamiento persistente.


**Estructura**{.azul}

La estructura de los ficheros JSON (JavaScript Object Notation) se basa en una sintaxis sencilla y legible para representar datos estructurados. JSON está formado por pares clave-valor y/o listas ordenadas de valores. Ejemplo:

        {
            "alumno": {
                "nombre": "María",
                "edad": 20,
                "activo": true,
                "notas": [8.5, 9.2, 7.8],
                "direccion": {
                "calle": "Av. del Sol",
                "ciudad": "Valencia",
                "codigoPostal": 46001
                }
            }
        }



**Elementos principales**{.azul}

- **Objeto:**	Conjunto de pares clave-valor, delimitado por {}:	{ "nombre": "María" }
- **Array:**	Lista ordenada de valores, delimitada por []:	[8, 5, 9.2, 7.8]
- **Clave:**	Siempre entre comillas dobles: "edad"
- **Valor:**	Puede ser: string, número, booleano, null, objeto o array:	"Valencia", 20, true, etc.

**Librerías**{.azul}

En Kotlin, existen varias librerías que permiten trabajar con ficheros JSON de forma sencilla:


| Librería             | Lenguaje base | Uso recomendado                          | Multiplataforma | Notas destacadas                                  |
|----------------------|----------------|-----------|------------------|--------------------------------------------------|
| `kotlinx.serialization` | Kotlin         | Kotlin puro y Kotlin Multiplatform       | ✅ Sí             | Ligera, rápida y con soporte oficial de JetBrains |
| `Jackson`            | Java           | Proyectos Java/Kotlin con Spring Boot     | ❌ No             | Muy flexible y poderosa                          |
| `Gson`               | Java           | Aplicaciones Android o proyectos simples  | ❌ No             | Fácil de usar, pero más lenta y menos segura     |
| `org.json`           | Java           | Scripts rápidos o aprendizaje             | ❌ No             | Acceso directo a claves sin clases de datos      |



Cuando trabajamos con ficheros JSON en Kotlin, existen dos formas de acceder a los datos: tratarlos como texto plano o estructuras genéricas, o convertirlos directamente en objetos Kotlin. Aunque la primera opción es posible y útil en ciertos casos, trabajar sin conversión implica mayor esfuerzo manual, riesgo de errores en los nombres de claves y ausencia de validación de tipos. En cambio, **kotlinx.serialization** ofrece una solución nativa, segura y eficaz que permite mapear directamente estructuras JSON en clases de datos (data class) mediante la anotación **@Serializable**.

Esto proporciona importantes **ventajas**:  

✔️ Validación automática de la estructura del JSON.  
✔️ Conversión directa entre JSON y objetos Kotlin.  
✔️ Código más limpio y mantenible.  
✔️ Mayor seguridad de tipos, detectando errores en tiempo de compilación.  

**kotlinx.serialization**{.azul}

kotlinx.serialization es la librería oficial de serialización de Kotlin, desarrollada por JetBrains, que permite convertir objetos Kotlin a y desde diferentes formatos como JSON, ProtoBuf, CBOR, XML (experimental), entre otros.


Como ya vimos en el apartado anterior, **la serialización** es el proceso de convertir los datos utilizados por una aplicación a un formato que pueda transferirse por red o almacenarse en una base de datos o archivo. A su vez, la deserialización es el proceso inverso: leer datos de una fuente externa y convertirlos en un objeto de tiempo de ejecución.


!!!Note "Nota"
    Todas las bibliotecas de serialización de Kotlin pertenecen al grupo **org.jetbrains.kotlinx:grupo**. Sus nombres empiezan con _kotlinx-serialization-_ y tienen sufijos que reflejan el formato de serialización:  
    
    - org.jetbrains.kotlinx:kotlinx-serialization-json
    

| Método de kotlinx.serialization         | ¿Qué hace?                                                   | Ejemplo básico                                                   |
|----------------------------------------|---------------------------------------------------------------|------------------------------------------------------------------|
| `Json.encodeToString(objeto)`          | Convierte un objeto Kotlin a una cadena JSON.                | `Json.encodeToString(persona)`                                  |
| `Json.encodeToString(serializer, obj)` | Igual que el anterior pero especificando el serializador.    | `Json.encodeToString(Persona.serializer(), persona)`            |
| `Json.decodeFromString(json)`          | Convierte una cadena JSON a un objeto Kotlin.                | `Json.decodeFromString<Persona>(json)`                          |
| `Json.decodeFromString(serializer, s)` | Igual que el anterior pero con el serializador explícito.    | `Json.decodeFromString(Persona.serializer(), json)`             |
| `Json.encodeToJsonElement(objeto)`     | Convierte un objeto a un árbol `JsonElement`.                | `val elem = Json.encodeToJsonElement(persona)`                  |
| `Json.decodeFromJsonElement(elem)`     | Convierte un `JsonElement` a objeto Kotlin.                  | `val persona = Json.decodeFromJsonElement<Persona>(elem)`       |
| `Json.parseToJsonElement(string)`      | Parsea una cadena JSON a un árbol `JsonElement` sin mapear.  | `val elem = Json.parseToJsonElement(json)`                      |



**Requisitos para usar kotlinx.serialization en Gradle (JSON)**{.azul}

- Activar el plugin de Kotlin serialization en **build.gradle.kts**

        plugins {
            kotlin("jvm") version "2.0.20"
            kotlin("plugin.serialization") version "2.0.20"
            application
        }

        dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
        }

       

⚠️ Asegúrate de usar una versión compatible con tu Kotlin. Por ejemplo, 1.6.3 funciona bien con Kotlin 2.0.20.        

- Anota tus clases con **@Serializable**
  
        import kotlinx.serialization.Serializable

        @Serializable
        data class Alumno(val nombre: String, val edad: Int)

!!!Note "Nota"
    En Kotlin, las **data class** están diseñadas para modelar datos puros. La palabra clave **data** no es obligatoria para la serialización, pero se usa por buena práctica y para obtener funcionalidades adicionales que son muy útiles, especialmente cuando trabajas con objetos de datos, como toString(), equals(), hashCode(), copy()..     
    


- Utiliza el objeto Json para **serializar/deserializar**      

Para serializar una instancia de esta clase llamamos a **Json.encodeToString()** y para
deserializar llamamos a **Json.decodeFromString()**.

        import kotlinx.serialization.json.Json
        import kotlinx.serialization.encodeToString
        import kotlinx.serialization.decodeFromString

        fun main() {
            val alumno = Alumno("María", 22)

            val json = Json.encodeToString(alumno)
            println("JSON: $json")

            val obj = Json.decodeFromString<Alumno>(json)
            println("Objeto: $obj")
        }


 También puedes serializar colecciones de objetos, como listas, en una sola llamada:

        val dataList = listOf(Data(42, "str"), Data(12, "test"))
        val jsonList = Json.encodeToString(dataList)

**Ejemplo de lectura y escritura de un archivo json**{.azul}

**Estructura del proyecto:**


        tu-proyecto/            
        ├── documentos/
        |       └──persona.json
        |       └──persona_nueva.json
        └── src/
            └── main/
                └── kotlin/
                    └── ejemplos/
                            └──Ejemplo_esc_JSON.kt 
                                └── main()
                            └──Ejemplo_lect_JSON.kt 
                                └── main()    
                            └── Persona.kt


1- Copia el siguiente fichero **persona.json** en la raiz del proyecto/documentos:

        {
        "nombre": "Lucía",
        "edad": 28
        }

2- Crea la clase **Persona.kt** con la misma estructura del archivo **persona.json**. Este es el objeto en Java que se corresponde con el archivo json y que se utiliza en la serialización.


        import kotlinx.serialization.Serializable

        @Serializable
        data class Persona(val nombre: String, val edad: Int)




**Ejemplo_JSON_lect.kt**

        import kotlinx.serialization.json.*
        import java.nio.file.Files
        import java.nio.file.Paths
        import java.io.IOException
        import java.nio.file.Files.readString


        fun main() {
            val rutaEntrada = Paths.get("documentos/persona.json")

            // --- Lectura segura ---
            try {
                if (!Files.exists(rutaEntrada)) {
                    println("El archivo no existe: $rutaEntrada")
                    return
                }

                val contenidoJson = readString(rutaEntrada)
                val persona = Json.decodeFromString<Persona>(contenidoJson)
                println("Lectura correcta: $persona")


            } catch (e: IOException) {
                println("Error de E/S: ${e.message}")
            }
        }




**Ejemplo_JSON_esc.kt**


El programa siguiente crea el fichero **persona_nueva.json**  con el siguiente contenido:    

        {
        "nombre": "Mario",
        "edad": 35
        }        
__


        import kotlinx.serialization.json.*
        import java.io.IOException
        import java.nio.file.Files
        import java.nio.file.Paths

        fun main() {
            val ruta = Paths.get("documentos/persona_nueva.json")

            try {
                // Crear el JSON directamente desde los datos
                val json = buildJsonObject {
                    put("nombre", "Mario")
                    put("edad", 35)
                }

                // Convertir a String con formato bonito
                val jsonString = Json { prettyPrint = true }.encodeToString(JsonObject.serializer(), json)

                // Crear carpeta si no existe
                Files.createDirectories(ruta.parent)

                // Escribir JSON en archivo
                Files.writeString(ruta, jsonString)

                println("Archivo JSON creado en: ${ruta.toAbsolutePath()}")
                println("Contenido:\n$jsonString")

            } catch (e: IOException) {
                println("Error de entrada/salida: ${e.message}")
            } catch (e: Exception) {
                println("Error inesperado: ${e.message}")
            }
        }


**JSON y Jackson**{.azul}

**kotlinx.serialization** es la librería oficial de serialización de Kotlin, pero **Jackson** es la librería más usada en Java para JSON. Muchos frameworks Java lo usan por defecto (Spring Boot, Micronaut, Quarkus, etc.). Conocerlo permite trabajar con APIs externas, backends y entornos mixtos (Java + Kotlin). 

Mientras que **kotlinx.serialization** está centrado en JSON y formatos binarios (CBOR, ProtoBuf...), **Jackson** también soporta XML, YAML, CSV de forma unificada, además, si necesitas convertir entre formatos (XML ↔ JSON), Jackson es ideal, por lo que es importante cononcer ambas librerías para entender los proyectos Kotlin puros y modernos (kotlinx.serialization) y también los proyectos reales empresariales con Jackson.

| Método Jackson                        | ¿Qué hace?                                                     | Ejemplo básico                                                   |
|--------------------------------------|-----------------------------------------------------------------|------------------------------------------------------------------|
| `readValue(String, Class)`           | Convierte una cadena JSON a un objeto Kotlin o Java.           | `mapper.readValue(json, Persona::class.java)`                   |
| `readValue(File, Class)`             | Convierte un archivo JSON a un objeto.                         | `mapper.readValue(File("persona.json"), Persona::class.java)`   |
| `readTree(String)`                   | Lee un JSON como árbol (`JsonNode`) sin mapear a clase.        | `val node = mapper.readTree(json)`                              |
| `writeValue(File, Object)`           | Escribe un objeto como JSON en un archivo.                     | `mapper.writeValue(File("salida.json"), persona)`               |
| `writeValueAsString(Object)`        | Convierte un objeto en una cadena JSON.                        | `val json = mapper.writeValueAsString(persona)`                 |
| `writeValueAsBytes(Object)`         | Convierte un objeto en un array de bytes JSON.                 | `val bytes = mapper.writeValueAsBytes(persona)`                 |
| `writerWithDefaultPrettyPrinter()`  | Devuelve un escritor que formatea (indentado) el JSON.         | `mapper.writerWithDefaultPrettyPrinter().writeValue(...)`       |



**Ejemplo de lectura y escritura con Jackson**{.azul}


Dependencia Gradle:

        dependencies {
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
        }


**Ejemplo_JSON_jackson.kt**

        import com.fasterxml.jackson.annotation.JsonProperty
        import com.fasterxml.jackson.databind.ObjectMapper
        import com.fasterxml.jackson.module.kotlin.KotlinModule
        import java.io.File

        data class PersonaJackson(
            @JsonProperty("nombre")
            val nombre: String = "",

            @JsonProperty("edad")
            val edad: Int = 0
        )


        //lectura de persona.json
        fun leerJson() {
            val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
            val archivo = File("documentos/persona.json")

            val persona = mapper.readValue(archivo, PersonaJackson::class.java)
            println("Lectura correcta: ${persona.nombre} tiene ${persona.edad} años.")
        }

        //escritura de persona.json
        fun escribirJson() {
            val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

            val persona = Persona("Mario", 35)
            val archivo = File("documentos/persona_generada_jackson.json")

            mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, persona)

            println("JSON generado correctamente en: ${archivo.absolutePath}")
        }

        fun main() {
            leerJson()
            escribirJson()
        }


## Ficheros XML

Un fichero **XML** (eXtensible Markup Language) es un formato de texto estructurado diseñado para almacenar y transportar datos de forma legible tanto para humanos como para máquinas.

Tiene una estructura jerárquica basada en etiquetas, similar al HTML, pero orientada al contenido de datos, no a la presentación. **XML** permite guardar objetos o estructuras de datos en un archivo de texto legible.

**Estructura**{.azul}

   <?xml version="1.0" encoding="UTF-8"?>
    <raiz>                             <!-- Elemento raíz obligatorio -->
        <elemento>
            <subelemento>valor</subelemento>
        </elemento>
        <otroElemento atributo="valor" />
    </raiz>




La mejor forma de trabajar con XML en **Kotlin** es utlizar la librería **DOM** (Document Object Model) o **JDOM2**, que permiten crear, leer y modificar fácilmente estructuras XML.

- DOM	-> Estándar Java, sin dependencias.	Requiere muchas líneas de código para hacer algo relativamente simple. 
- JDOM2 ->	API más amigable para desarrolladores.	Requiere añadir una librería externa.

**Ficheros XML y JDOM2**{.azul}

JDOM2 es una librería Java (Kotlin) que permite leer, crear, modificar y guardar archivos XML de forma sencilla y orientada a objetos.



Clase|	¿Para qué sirve?
-----|-------------------
SAXBuilder|	Analiza (parsea) un archivo XML y devuelve un Document.
Document|	Representa todo el documento XML.
Element|	Representa una etiqueta (nodo) del XML.
Attribute|	Representa un atributo dentro de una etiqueta.
XMLOutputter|	Convierte el árbol de elementos en texto XML.


**Ejemplo de lectura y escritura de un archivo xml con JDOM2**{.azul}:

Dependencias en Gradle:

        dependencies {
            implementation("org.jdom:jdom2:2.0.6")
        }

- Guarda el siguiente fichero **alumnos.xml** en la raiz del proyecto/documentos/alumnos.xml:


        <?xml version="1.0" encoding="UTF-8"?>
        <alumnos>
            <alumno>
                <nombre>Ana</nombre>
                <nota>9</nota>
            </alumno>
            <alumno>
                <nombre>Pedro</nombre>
                <nota>7</nota>
            </alumno>
        </alumnos>


- **Ejemplo_XML_lect.kt**: Lectura de alumnos.xml

        import org.jdom2.input.SAXBuilder
        import java.io.File

        fun main() {
            val archivo = File("documentos/alumnos.xml")
            val builder = SAXBuilder()
            val documento = builder.build(archivo)
            val raiz = documento.rootElement
            val listaAlumnos = raiz.getChildren("alumno")

            for (alumno in listaAlumnos) {
                val nombre = alumno.getChildText("nombre")
                val nota = alumno.getChildText("nota")
                println("Alumno: $nombre, Nota: $nota")
            }
        }

- **Ejemplo_XML_esc.kt**: Escritura en alumnos_nuevo.xml

        import org.jdom2.Document
        import org.jdom2.Element
        import org.jdom2.output.Format
        import org.jdom2.output.XMLOutputter
        import java.io.File

        fun main() {
            // Crear elementos
            val raiz = Element("alumnos")

            val alumno1 = Element("alumno")
            alumno1.addContent(Element("nombre").setText("Lucía"))
            alumno1.addContent(Element("nota").setText("8"))

            val alumno2 = Element("alumno")
            alumno2.addContent(Element("nombre").setText("Carlos"))
            alumno2.addContent(Element("nota").setText("6"))

            // Añadir alumnos a la raíz
            raiz.addContent(alumno1)
            raiz.addContent(alumno2)

            // Crear documento y escribirlo
            val documento = Document(raiz)
            val salida = XMLOutputter()
            salida.format = Format.getPrettyFormat()
            salida.output(documento, File("documentos/nuevo_alumnos.xml").outputStream())

            println("Archivo XML creado con éxito.")
        }


**Ejemplo que convierte el archivo alumnos.xml en un objeto y viceversa**{.azul}:

JDOM2 no realiza serialización automática de objetos Kotlin, se necesita mapear manualmente entre objetos (data class) y elementos XML.


- Primero crearemos la clase Alumnos:

        data class Alumno(val nombre: String, val nota: Int)


- **Ejemplo_XML_a_Objeto.kt**: Leemos el archivo alumnos.xml creado en el ejemplo anterior y lo convertimos a objeto.

        import org.jdom2.input.SAXBuilder
        import java.io.File


        fun main() {
            val alumnos = mutableListOf<Alumno>()

            val archivo = File("documentos/alumnos.xml")
            val builder = SAXBuilder()
            val documento = builder.build(archivo)
            val raiz = documento.rootElement

            val listaAlumnos = raiz.getChildren("alumno")

            for (elemento in listaAlumnos) {
                val nombre = elemento.getChildText("nombre")
                val nota = elemento.getChildText("nota").toIntOrNull() ?: 0
                alumnos.add(Alumno(nombre, nota))
            }

            // Mostrar los objetos
            alumnos.forEach { println(it) }
        }

- **Ejemplo_Objeto_a_XML.kt**: Leemos el objeto Alumno y lo convertimos en fichero xml (alumnos_generado.xml).


        import org.jdom2.Document
        import org.jdom2.Element
        import org.jdom2.output.Format
        import org.jdom2.output.XMLOutputter
        import java.io.File


        fun main() {
            // Lista de alumnos
            val alumnos = listOf(
                Alumno("Lucía", 8),
                Alumno("Carlos", 6),
                Alumno("María", 10)
            )

            // Crear elemento raíz <alumnos>
            val raiz = Element("alumnos")

            // Añadir cada alumno como <alumno>
            for (alumno in alumnos) {
                val alumnoElement = Element("alumno")
                alumnoElement.addContent(Element("nombre").setText(alumno.nombre))
                alumnoElement.addContent(Element("nota").setText(alumno.nota.toString()))
                raiz.addContent(alumnoElement)
            }

            // Crear el documento XML
            val documento = Document(raiz)

            // Escribir en archivo con formato bonito
            val salida = XMLOutputter()
            salida.format = Format.getPrettyFormat()
            salida.output(documento, File("documentos/alumnos_generado.xml").outputStream())

            println("Archivo XML creado con éxito.")
        }



**XML y Jackson**{.azul}

JDOM2 no realiza serialización automática de objetos Kotlin, pero se puede recurrir a librerías como **Jackson** o **kotlinx.serialization**.

A diferencia de la librería **kotlinx.serialization-josn**, para ficheros **JSON**, que es es la librería oficial de serialización de Kotlin, la librería **kotlinx.serialization-xml**, para ficheros **XML**, no es oficial (aún experimental) y está mantenidad por terceros, por lo que no es una buena elección.

Utilizaremos, por tanto, la librería **Jackson** para realizar la serialización automática de objetos Kotlin, la cual también permite soporte completo para XML y JSON, es decier, puede serializar y deserializar ambos formatos usando las mismas clases.


Situación|	Mejor opción|	Motivo principal
---------|--------------|-------------------
Solo JSON|	kotlinx.serialization|	Es ligero, idiomático, rápido y bien integrado en Kotlin.
JSON + XML o solo XML complejo|	Jackson|	Más flexible, potente y compatible con XML real.



**Ejemplo de lectura y escritura del fichero alumnos.xml  con Jackson XML**{.azul}


Dependencias en Gradle:

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
        implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.0")
    }

**Ejemplo_XML_Jackson.kt**


        import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
        import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
        import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.KotlinModule
        import java.io.File

        //Clases de datos
        data class AlumnoJckson(
            @JacksonXmlProperty(localName = "nombre")
            val nombre: String = "",

            @JacksonXmlProperty(localName = "nota")
            val nota: Int = 0
        )

        data class ListaAlumnos(
            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "alumno")
            val alumno: List<AlumnoJson> = emptyList()
        )

        //Lectura del XML (alumnos.xml)
        fun leerXml() {
            val xmlMapper = XmlMapper().registerModule(KotlinModule.Builder().build())
            val archivo = File("documentos/alumnos.xml")

            val lista = xmlMapper.readValue(archivo, ListaAlumnos::class.java)

            println("Lectura correcta:")
            lista.alumno.forEach {
                println("${it.nombre} tiene un ${it.nota}")
            }
        }

        Escritura del XML (alumnos_generado.xml)
        fun escribirXml() {
            val xmlMapper = XmlMapper().registerModule(KotlinModule.Builder().build())

            val lista = ListaAlumnos(
                listOf(
                    AlumnoJson("Ana", 9),
                    AlumnoJson("Pedro", 7)
                )
            )

            val archivo = File("documentos/alumnos_generado_Jakcson.xml")
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(archivo, lista)

            println("XML escrito correctamente en: ${archivo.absolutePath}")
        }

        fun main() {
            leerXml()
            escribirXml()
        }

