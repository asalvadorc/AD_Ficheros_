---
hide:
  - toc
---

# 🔹 Conversión entre formatos de ficheros

!!!Tip "Importante"
    Para convertir un fichero de un formato a otro, primero debo deserializarlo o interpretarlo convirtiéndolo a objetos en memoria, y después generar el nuevo formato a partir de esos objetos. El proceso correcto es siempre:  
    **Formato de entrada → Objeto → Formato de salida.**

Cada formato (CSV, JSON, XML, binario) organiza la información de forma distinta:

- CSV → datos planos por filas/columnas
- JSON → datos jerárquicos
- XML → etiquetas anidadas
- Binario → datos codificados


En la siguiente tabla se resumen las conversiones más habituales entre formatos de ficheros trabajadas en el módulo.
Para cada conversión se indican varias herramientas posibles, pero las **opciones marcadas en negrita** representan la recomendación principal, ya que son las más sencillas.

Las herramientas no destacadas en negrita son alternativas válidas, que pueden utilizarse en otros contextos o como ampliación, pero no son las más recomendadas como primera opción.


| Conversión                       | Herramientas recomendadas                                                            | Proceso resumido                                                   |
| -------------------------------- | ------------------------------------------------------------------------------------ | ------------------------------------------------------------------ |
| CSV → JSON                       | **readAllLines + split** / KotlinCSV / OpenCSV + **Jackson** / kotlinx.serialization | Leer CSV → mapear a objetos → serializar a JSON                    |
| JSON → CSV                       | **Jackson** / kotlinx.serialization + **KotlinCSV** / OpenCSV                        | Deserializar JSON a objetos → escribir filas CSV                   |
| CSV → XML                        | **readAllLines + split** / KotlinCSV / OpenCSV + **Jackson (XmlMapper)**             | Leer CSV → mapear a objetos → serializar a XML                     |
| XML → CSV                        | **Jackson (XmlMapper)** + **KotlinCSV** / OpenCSV                                    | Leer XML → mapear a objetos → escribir filas CSV                   |
| JSON → XML                       | **Jackson (ObjectMapper, XmlMapper)**                                                | Convertir JSON a objeto → serializar a XML                         |
| XML → JSON                       | **Jackson (XmlMapper, ObjectMapper)**                                                | Leer XML como objeto → serializar a JSON                           |
| Texto → JSON / XML               | **Files.readAllLines()** + **Jackson** / kotlinx.serialization                       | Leer texto → interpretar líneas → mapear a estructura → serializar |
| Texto → binario                  | **Files.readAllLines() / Files.readString() + Files.write()**                        | Leer texto → convertir a bytes (UTF-8) → guardar en binario        |
| Texto → binario estructurado     | **Files.readAllLines() / Files.readString() + DataOutputStream**                     | Leer texto → escribir campos con tipo fijo                         |
| Binario estructurado → JSON      | **DataInputStream + Jackson** / kotlinx.serialization                                | Leer datos binarios → construir objetos → serializar a JSON        |
| JSON → binario estructurado      | **Jackson** / kotlinx.serialization + **DataOutputStream**                           | Deserializar JSON → escribir datos con tipo fijo                   |
| Objeto → binario (serialización) | **ObjectOutputStream (Serializable)**                                                | Serializar objetos completos a binario                             |
| Binario → objeto                 | **ObjectInputStream**                                                                | Deserializar objetos binarios                                      |
| PNG → JPG (imagen)               | **ImageIO.read() + ImageIO.write()**                                                 | Leer imagen → guardar en otro formato                              |

### 🔹 Ficheros de partida para los ejemplos {.azul}


!!!danger "Ficheros para trabajar en las conversiones"
    En los siguientes ejemplos trabajaremos con tres archivos en diferentes formatos: **CSV**, **XML** y **JSON**. Estos archivos contienen información similar, representada con distinta estructura y sintaxis según el formato. Los utilizaremos como base para realizar ejercicios de conversión entre formatos.

**alumnos.csv**

```csv
nombre;nota
Lucía;28
Carlos;8
Elena;10
```

**persona.xml**

```xml
<Persona>
    <nombre>Lucía</nombre>
    <edad>28</edad>
</Persona>
```

**persona.json**

```json
{
    "nombre": "Lucía",
    "edad": 28
}
```

!!!Tip "Data Class"
    Al trabajar con ficheros de intercambio como CSV, JSON o XML, es habitual encontrarnos con datos estructurados formados por distintos campos. Para poder manejar estos datos de forma cómoda y segura en Kotlin, es recomendable representarlos mediante **data class**, que permiten modelar la información con tipos y nombres claros. Una vez los datos están representados como objetos, el formato original del fichero deja de ser relevante. Esta idea será fundamental en el siguiente apartado, donde se utilizarán los **data class** como elemento intermedio para transformar la información entre distintos formatos de fichero, como CSV, JSON, XML o binario.  
    
    📌 **Nota:** Las clases **Alumno** y **Persona** ya las creamos en los ejemplos sobre ficheros de intercambio y las volveremos a utilizar en los siguientes ejemplos.

    **Alumno.kt** 
    ```kotlin
        data class Alumno(
                        val nombre: String,
                        val nota: Int
                    )   
    ```

    **Persona.kt** 
    ```kotlin
        @Serializable
        data class Persona(
            val nombre: String, val edad: Int
            )   
    ```

## 🔹 CSV <-> JSON

En estos ejemplos utilizamos la librería **Jackson**, pero se podría  utilizar también **Kotlinx.serialization**.

**Resumen de ejemplos**{.azul}

- [Ejemplo_convertir_csv_a_json.kt](#ejemplo-convertir-csv-a-json): convierte un CSV en JSON mediante una lista de objetos `Alumno`.


- [Ejemplo_convertir_json_a_csv.kt](#ejemplo-convertir-json-a-csv): convierte un JSON con una lista de alumnos en un fichero CSV.



<a id="ejemplo-convertir-csv-a-json"></a>

🖥️ **Ejemplo_convertir_csv_a_json.kt**


Este ejemplo muestra cómo convertir un fichero CSV en un archivo JSON tomando como paso intermedio una lista de objetos `Alumno`.

La finalidad del ejercicio es entender que la conversión entre formatos no se hace directamente de CSV a JSON, sino pasando antes por objetos Kotlin que representan los datos de forma tipada.

!!!Note ""
    El intermediario entre el CSV y el JSON es **la lista de objetos alumnos** (de tipo Alumno, que es una clase ya creada)


```kotlin
import com.opencsv.CSVReaderBuilder
import com.opencsv.CSVParserBuilder
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.FileReader


fun main() {
    val rutaCSV = "documentos/alumnos.csv"
    val rutaJSON = "documentos/alumnos.json"

    val reader = CSVReaderBuilder(FileReader(rutaCSV)) // (1)!
        .withCSVParser(CSVParserBuilder().withSeparator(';').build()) // (2)!
        .withSkipLines(1) // (3)!
        .build()

    val registros = reader.readAll() // (4)!

    //lista alumnos para guardar los objetos Alumno
    val alumnos = mutableListOf<Alumno>()

    for (campos in registros) {
        val nombre = campos[0]
        val nota = campos[1].toInt()

        //  Cada línea del CSV se transforma en un objeto
        val alumno = Alumno(nombre, nota)
        //Todos los objetos se guardan en la lista alumnos
        alumnos.add(alumno)
    }

    reader.close()


    val mapper = jacksonObjectMapper() // (5)!
    mapper.writerWithDefaultPrettyPrinter().writeValue(File(rutaJSON), alumnos) // (6)!

    println("✅ Conversión CSV → JSON completada: $rutaJSON")
}


```

1. Inicializa el lector CSV sobre el fichero de entrada.
2. Configura el parser CSV con el separador indicado.
3. Omite la primera linea del CSV (cabecera).
4. Lee todos los registros del CSV en memoria.
5. Crea el mapper de Jackson para la conversion entre objetos y JSON.
6. Escribe el resultado en el fichero de salida con formato legible.
<a id="ejemplo-convertir-json-a-csv"></a>

🖥️ **Ejemplo_convertir_json_a_csv.kt**        


Este ejemplo realiza la conversión inversa: parte de un archivo JSON con una lista de alumnos y genera a partir de él un fichero CSV.

La finalidad del ejercicio es entender que, igual que en la conversión anterior, el paso clave entre formatos vuelve a ser una colección de objetos Kotlin que representa los datos de forma estructurada.

```kotlin
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter


fun main() {
    val rutaJson = "documentos/alumnos.json"
    val rutaCsv = "documentos/alumnos.csv"

    // 1. Leer JSON
    val mapper = jacksonObjectMapper() // (1)!
    val alumnos: List<Alumno> = mapper.readValue(File(rutaJson)) // (2)!

    // 2. Escribir CSV
    val writer = CSVWriter(FileWriter(rutaCsv), ';', CSVWriter.NO_QUOTE_CHARACTER, // (3)!
        CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END) // (4)!

    // Cabecera
    writer.writeNext(arrayOf("nombre", "nota")) // (5)!

    // Cuerpo
    for (alumno in alumnos) {
        writer.writeNext(arrayOf(alumno.nombre, alumno.nota.toString())) // (6)!
    }

    writer.close()

    println("✅ Conversión JSON → CSV completada: $rutaCsv")
}


```
1. Crea el mapper de Jackson para la conversion entre objetos y JSON.
2. Deserializa el JSON en una lista tipada de objetos.
3. Crea el escritor CSV con su configuracion de separador y escape.
4. Completa la configuracion del escritor CSV (escape y fin de linea).
5. Escribe la cabecera del fichero CSV.
6. Escribe una fila CSV con los datos del alumno.

## 🔹 JSON <-> XML

!!!Note ""
    En estos ejemplos utilizamos **Jackson**, en ambas conversiones, y por lo tanto también utiliza un objeto intermediario (**persona**), aunque de forma más implícita.

**Resumen de ejemplos**{.azul}

- [Ejemplo_convertir_json_a_xml.kt](#ejemplo-convertir-json-a-xml): convierte un objeto JSON en un fichero XML.


- [Ejemplo_convertir_listajson_a_xml.kt](#ejemplo-convertir-listajson-a-xml): convierte una lista JSON en XML sin nodo raíz significativo.


- [Ejemplo_convertir_listajson_a_xml_nodo.kt](#ejemplo-convertir-listajson-a-xml-nodo): convierte una lista JSON en XML usando una clase contenedora.


- [Ejemplo_convertir_xml_a_json.kt](#ejemplo-convertir-xml-a-json): convierte un XML simple en un fichero JSON.


- [Ejemplo_convertir_listaxml_a_json.kt](#ejemplo-convertir-listaxml-a-json): convierte un XML con lista en un array JSON.


- [Ejemplo_convertir_listaxml_a_json_nodo.kt](#ejemplo-convertir-listaxml-a-json-nodo): convierte un XML con nodo raíz significativo en un array JSON.


<a id="ejemplo-convertir-json-a-xml"></a>

🖥️ **Ejemplo_convertir_json_a_xml.kt**


Este ejemplo muestra cómo convertir un archivo JSON en un fichero XML utilizando Jackson, pasando internamente por un objeto `Persona` como representación intermedia de los datos.

La finalidad del ejercicio es entender que la conversión de JSON a XML no se hace directamente entre textos, sino reconstruyendo primero un objeto en memoria y generando después el nuevo formato a partir de él.

```kotlin
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

fun convertirJsonAXml(jsonPath: String, xmlPath: String) {
    val xmlMapper = XmlMapper().registerKotlinModule() // (1)!
    val jsonMapper = jacksonObjectMapper() // (2)!

    //Leer JSON y convertirlo a un objeto Persona
    val persona = jsonMapper.readValue<Persona>(File(jsonPath)) // (3)!

    // Escribir el objeto Persona en formato XML
    xmlMapper.writerWithDefaultPrettyPrinter().writeValue(File(xmlPath), persona) // (4)!

    println("Conversión JSON → XML completada")
}

fun main() {
    convertirJsonAXml("documentos/persona.json", "documentos/persona.xml")

}

```

1. Crea el mapper XML y registra soporte para clases Kotlin.
2. Crea el mapper de Jackson para la conversion entre objetos y JSON.
3. Lee el fichero de entrada y lo deserializa al tipo indicado.
4. Escribe el resultado en el fichero de salida con formato legible.
!!!Note "Fichero JSON compuesto por una lista de elementos"
    Si el fichero **JSON** contiene un *array* (`[...]`), es decir, una **lista de objetos**, entonces debemos indicar explícitamente que queremos leer un `List<Objeto>`.

    **lista_personas_jackson.json** 
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

    <a id="ejemplo-convertir-listajson-a-xml"></a>

🖥️ **Ejemplo_convertir_listajson_a_xml.kt**


Este ejemplo amplía la conversión anterior para trabajar con un archivo JSON que contiene una lista de objetos `Persona` y generar a partir de ella un XML.

La finalidad del ejercicio es comprobar que Jackson también puede serializar colecciones completas, aunque el XML generado automáticamente no siempre tendrá la estructura más adecuada.

```kotlin
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule


fun convertirListaJsonAXml(jsonPath: String, xmlPath: String) {

    val jsonMapper = jacksonObjectMapper() // (1)!
    val xmlMapper = XmlMapper().registerKotlinModule() // (2)!

    // Leer JSON y convertirlo a lista de objetos Persona
    val personas: List<Persona> = jsonMapper.readValue(File(jsonPath)) // (3)!

    // Escribir el XML
    xmlMapper.writerWithDefaultPrettyPrinter().writeValue(File(xmlPath), personas) // (4)!

    println("Conversión JSON → XML completada")
}

fun main() {

    convertirListaJsonAXml("documentos/lista_personas.json", "documentos/lista_personas.xml")

}


```

1. Crea el mapper de Jackson para la conversion entre objetos y JSON.
2. Crea el mapper XML y registra soporte para clases Kotlin.
3. Deserializa el JSON en una lista tipada de objetos.
4. Escribe el resultado en el fichero de salida con formato legible.


El contenido del fichero xml convertido sería el siguiente:
```xml
        <ArrayList>
        <item>
            <nombre>Lucía</nombre>
            <edad>28</edad>
        </item>
        <item>
            <nombre>Pepe</nombre>
            <edad>30</edad>
        </item>
        <item>
            <nombre>Ana</nombre>
            <edad>50</edad>
        </item>
        <item>
            <nombre>Juan</nombre>
            <edad>12</edad>
        </item>
        </ArrayList>
```
⚠️ Esto no es correcto porque, aunque el XML se puede leer, cuando un XML representa una lista, siempre debe tener un elemento raíz con significado.

!!!Tip "Clase contenedora"
    Cuando se convierte una lista de JSON a XML, es recomendable utilizar un **data class** para modelar los datos y una **clase contenedora auxiliar** para representar el nodo raíz del XML.  

📌 Creamos la Clase contenedora **ListaPersonas** en el **paquete Ejemplos**, fuera de los programas, para poder reutilizarla:
    ```kotlin
        import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

        data class ListaPersonas(
            @JacksonXmlElementWrapper(useWrapping = false)
            val persona: List<Persona>
        )        
    ```

<a id="ejemplo-convertir-listajson-a-xml-nodo"></a>

🖥️ **Ejemplo_convertir_listajson_a_xml_nodo.kt**


Este ejemplo corrige el problema del caso anterior utilizando una clase contenedora para que el XML resultante tenga un nodo raíz con significado.

La finalidad del ejercicio es entender que, cuando un XML representa una colección, suele ser necesario crear explícitamente una clase contenedora que modele el nodo raíz del documento.

```kotlin
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

fun convertirListaJsonAXml_nodo(jsonPath: String, xmlPath: String) {
    val jsonMapper = jacksonObjectMapper() // (1)!
    val xmlMapper = XmlMapper().registerKotlinModule() // (2)!

    // Leer JSON y convertirlo a lista de objetos Persona
    val personas: List<Persona> = jsonMapper.readValue(File(jsonPath)) // (3)!

    // Envolver la lista en la clase contenedora para XML
    val listaPersonas = ListaPersonas(personas)

    // Escribir el XML
    xmlMapper.writerWithDefaultPrettyPrinter() // (4)!
        .writeValue(File(xmlPath), listaPersonas) // (5)!

    println("✅ Conversión JSON → XML completada")
}

fun main() {
    convertirListaJsonAXml_nodo(
        "documentos/lista_personas.json",
        "documentos/lista_personas_nodo.xml"
    )
}


```

1. Crea el mapper de Jackson para la conversion entre objetos y JSON.
2. Crea el mapper XML y registra soporte para clases Kotlin.
3. Deserializa el JSON en una lista tipada de objetos.
4. Prepara un escritor XML con formato legible.
5. Completa la escritura del contenido convertido en el fichero de salida.

Ahora la conversión si que es correcta:
```xml
        <ListaPersonas>
        <persona>
            <nombre>Lucía</nombre>
            <edad>28</edad>
        </persona>
        <persona>
            <nombre>Pepe</nombre>
            <edad>30</edad>
        </persona>
        <persona>
            <nombre>Ana</nombre>
            <edad>50</edad>
        </persona>
        <persona>
            <nombre>Juan</nombre>
            <edad>12</edad>
        </persona>
        </ListaPersonas>
        
```
    <a id="ejemplo-convertir-xml-a-json"></a>

🖥️ **Ejemplo_convertir_xml_a_json.kt**


Este ejemplo realiza la conversión inversa al primero de esta sección: parte de un archivo XML y genera un fichero JSON a partir de un objeto `Persona` intermedio.

La finalidad del ejercicio es comprobar que la misma idea se mantiene en sentido inverso: leer un formato, reconstruir un objeto y generar después el nuevo formato de salida.


```kotlin
import com.fasterxml.jackson.dataformat.xml.XmlMapper
 import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
 import com.fasterxml.jackson.module.kotlin.readValue
 import com.fasterxml.jackson.module.kotlin.registerKotlinModule
 import java.io.File

 fun convertirXmlAJson(xmlPath: String, jsonPath: String) {
     val xmlMapper = XmlMapper().registerKotlinModule() // (1)!
     val jsonMapper = jacksonObjectMapper() // (2)!


     val persona = xmlMapper.readValue<Persona>(File(xmlPath)) // (3)!
     jsonMapper.writerWithDefaultPrettyPrinter().writeValue(File(jsonPath), persona) // (4)!

     println("Conversión XML → JSON completada")
 }

 fun main() {

     convertirXmlAJson("documentos/persona.xml", "documentos/persona.json")

 }


```

1. Crea el mapper XML y registra soporte para clases Kotlin.
2. Crea el mapper de Jackson para la conversion entre objetos y JSON.
3. Lee el fichero de entrada y lo deserializa al tipo indicado.
4. Escribe el resultado en el fichero de salida con formato legible.

!!!Note "Fichero XML compuesto por una lista de elementos"
    Si el fichero **XML** contiene una **lista de objetos**, entonces debemos indicar explícitamente que queremos leer un `List<Objeto>`.  

    **lista_personas.xml**
    ```xml
            <ArrayList>
            <item>
                <nombre>Lucía</nombre>
                <edad>28</edad>
            </item>
            <item>
                <nombre>Pepe</nombre>
                <edad>30</edad>
            </item>
            <item>
                <nombre>Ana</nombre>
                <edad>50</edad>
            </item>
            <item>
                <nombre>Juan</nombre>
                <edad>12</edad>
            </item>
            </ArrayList>
    ```
    <a id="ejemplo-convertir-listaxml-a-json"></a>

🖥️ **Ejemplo_convertir_listaxml_a_json.kt**


Este ejemplo convierte un XML que contiene una lista de personas en un archivo JSON con un array de objetos.

La finalidad del ejercicio es ver cómo una colección leída desde XML puede serializarse después como un array JSON sin procesar manualmente cada elemento.

```kotlin
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

fun convertirListaXmlAJson(xmlPath: String, jsonPath: String) {
    val jsonMapper = jacksonObjectMapper() // (1)!
    val xmlMapper = XmlMapper().registerKotlinModule() // (2)!

    val personas: List<Persona> = xmlMapper.readValue(File(xmlPath)) // (3)!
    jsonMapper.writerWithDefaultPrettyPrinter().writeValue(File(jsonPath), personas) // (4)!

    println("Conversión XML → JSON completada")
}

fun main() {

    convertirListaXmlAJson("documentos/lista_personas.xml", "documentos/lista_personas.json")
}

```

1. Crea el mapper de Jackson para la conversion entre objetos y JSON.
2. Crea el mapper XML y registra soporte para clases Kotlin.
3. Deserializa el XML en una lista tipada de objetos.
4. Escribe el resultado en el fichero de salida con formato legible.

!!!Tip "Clase contenedora"
    Cuando el XML contiene un nodo raíz que agrupa varios elementos, es conveniente utilizar una **clase contenedora auxiliar** para mapear correctamente la estructura del documento.
    
    **lista_personas_nodo.xml**

    ```xml
        <ListaPersonas>
            <persona>
                <nombre>Lucía</nombre>
                <edad>28</edad>
            </persona>
            <persona>
                <nombre>Pepe</nombre>
                <edad>30</edad>
            </persona>
            <persona>
                <nombre>Ana</nombre>
                <edad>50</edad>
            </persona>
            <persona>
                <nombre>Juan</nombre>
                <edad>12</edad>
            </persona>
            </ListaPersonas>
    ```

📌 Utilizaremos la clase contenedora **ListapPersona** creada anteriormente.

<a id="ejemplo-convertir-listaxml-a-json-nodo"></a>

🖥️ **Ejemplo_convertir_listaxml_a_json_nodo.kt**


Este ejemplo parte de un XML con un nodo raíz significativo y usa una clase contenedora para convertir correctamente su contenido en un array JSON.

La finalidad del ejercicio es entender cómo una clase contenedora permite mapear correctamente la estructura jerárquica del XML antes de convertirla en una lista JSON.


```kotlin
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

fun convertirListaXmlAJsonNodo(xmlPath: String, jsonPath: String) {
    val jsonMapper = jacksonObjectMapper() // (1)!
    val xmlMapper = XmlMapper().registerKotlinModule() // (2)!

    // Lee el XML y lo convierte a un objeto de tipo ListaPersonas
    // Aquí utilizamos la clase contenedora para representar el nodo raíz del XML
    val lista: ListaPersonas = xmlMapper.readValue(File(xmlPath)) //Aquí utilizamos la clase contendora // (3)!

    // Extrae la lista de objetos Persona desde la clase contenedora
    val personas = lista.persona

    // Escribe la lista de personas en formato JSON
    // El JSON generado será un array de objetos
    jsonMapper.writerWithDefaultPrettyPrinter()
        .writeValue(File(jsonPath), personas) // (4)!

    println("Conversión XML → JSON completada")
}

fun main() {

    convertirListaXmlAJsonNodo("documentos/lista_personas_nodo.xml", "documentos/lista_personas.json")
}

```

1. Crea el mapper de Jackson para la conversion entre objetos y JSON.
2. Crea el mapper XML y registra soporte para clases Kotlin.
3. Deserializa el XML al contenedor ListaPersonas para poder acceder a su lista interna.
4. Completa la escritura del contenido convertido en el fichero de salida.
## 🔹 JSON <-> Binario estructurado

En estos ejemplos utilizamos **kotlinx.serialization**.

**Resumen de ejemplos**{.azul}

- [Ejemplo_convertir_json_a_binario.kt](#ejemplo-convertir-json-a-binario): convierte un objeto JSON en binario estructurado.


- [Ejemplo_convertir_listajson_a_binario.kt](#ejemplo-convertir-listajson-a-binario): convierte una lista JSON en binario estructurado.


- [Ejemplo_convertir_listajson_a_binario_Jackson.kt](#ejemplo-convertir-listajson-a-binario-jackson): realiza la misma conversión anterior leyendo el JSON con Jackson.


- [Ejemplo_convertir_binario_a_json.kt](#ejemplo-convertir-binario-a-json): convierte un binario estructurado en un archivo JSON.


- [Ejemplo_convertir_listabinario_a_json.kt](#ejemplo-convertir-listabinario-a-json): convierte una lista binaria estructurada en JSON con `kotlinx.serialization`.


- [Ejemplo_convertir_listabinario_a_json_Jackson.kt](#ejemplo-convertir-listabinario-a-json-jackson): convierte una lista binaria estructurada en JSON usando Jackson.


!!!Note ""
    El objeto **persona** (instancia de la clase Persona) es el intermediario entre el archivo JSON y el archivo binario.

<a id="ejemplo-convertir-json-a-binario"></a>

🖥️ **Ejemplo_convertir_json_a_binario.kt**


Este ejemplo muestra cómo convertir un archivo JSON en un fichero binario estructurado utilizando un objeto `Persona` como paso intermedio.

La finalidad del ejercicio es entender cómo pasar de un formato textual como JSON a un formato binario tipado, escribiendo cada campo con el método adecuado.

```kotlin
import kotlinx.serialization.json.Json
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.io.DataInputStream
import java.io.FileInputStream

fun main() {
    val rutaJson = "documentos/persona.json"
    val rutaBin = "documentos/persona.dat"

    // Leer JSON
    val contenido = File(rutaJson).readText() // (1)!
    val persona = Json.decodeFromString<Persona>(contenido) // (2)!

    // Crear carpeta si no existe
    Files.createDirectories(Paths.get(rutaBin).parent) // (3)!

    // Escribir como binario estructurado
    val salida = DataOutputStream(FileOutputStream(rutaBin)) // (4)!
    salida.writeUTF(persona.nombre)  // Guarda string como UTF con longitud // (5)!
    salida.writeInt(persona.edad)    // Guarda entero (4 bytes) // (6)!
    salida.close()

    println("✅ .Persona guardada como binario estructurado en: $rutaBin")

    //Leer el binario estructurado
    val entrada = DataInputStream(FileInputStream(rutaBin)) // (7)!
    val nombre = entrada.readUTF() // (8)!
    val edad = entrada.readInt() // (9)!
    entrada.close()

    println("📄 Persona leída del binario:")
    println("Nombre: $nombre, Edad: $edad")

}

```

1. Lee el contenido textual del fichero de entrada.
2. Deserializa el JSON de entrada al tipo Kotlin correspondiente.
3. Asegura que exista la carpeta de salida antes de escribir el fichero.
4. Abre un flujo de salida para escribir binario estructurado.
5. Escribe el campo de texto en binario usando UTF.
6. Escribe un valor entero en binario respetando el orden del formato.
7. Abre un flujo de entrada para leer el binario estructurado.
8. Lee un campo de texto UTF desde el binario.
9. Lee un valor entero desde el binario.

!!!Note "Fichero JSON compuesto por una lista de elementos"
    Si el fichero **JSON** contiene un *array* (`[...]`), es decir, una **lista de objetos**, necesitas iterar sobre la lista al escribir y al leer.

<a id="ejemplo-convertir-listajson-a-binario"></a>

🖥️ **Ejemplo_convertir_listajson_a_binario.kt**        


Este ejemplo amplía el caso anterior para convertir una lista de personas en un fichero binario estructurado.

La finalidad del ejercicio es entender que, cuando se trabaja con colecciones en binario estructurado, hay que almacenar también cuántos elementos contiene la lista antes de escribir sus datos.

```kotlin
import kotlinx.serialization.json.Json
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.io.DataInputStream
import java.io.FileInputStream

fun main() {
    val rutaJson = "documentos/lista_personas.json"
    val rutaBin = "documentos/lista_personas.dat"

    // Leer JSON
    val contenido = File(rutaJson).readText() // (1)!
    val personas = Json.decodeFromString<List<Persona>>(contenido) // (2)!

    // Crear carpeta si no existe
    Files.createDirectories(Paths.get(rutaBin).parent) // (3)!

    // Escribir como binario estructurado
    val salida = DataOutputStream(FileOutputStream(rutaBin)) // (4)!
    salida.writeInt(personas.size) // Guardar el tamaño de la lista // (5)!
    for (persona in personas) {
        salida.writeUTF(persona.nombre) // (6)!
        salida.writeInt(persona.edad) // (7)!
    }
    salida.close()

    println("✅ Lista de personas guardada como binario estructurado en: $rutaBin")

    // Leer el binario estructurado
    val entrada = DataInputStream(FileInputStream(rutaBin)) // (8)!
    val cantidad = entrada.readInt() // Leer el tamaño de la lista // (9)!
    for (i in 0 until cantidad) {
        val nombre = entrada.readUTF() // (10)!
        val edad = entrada.readInt() // (11)!

        println("Nombre: $nombre, Edad: $edad")
    }
    entrada.close()
}

```

1. Lee el contenido textual del fichero de entrada.
2. Deserializa el JSON de entrada a una lista tipada de Persona.
3. Asegura que exista la carpeta de salida antes de escribir el fichero.
4. Abre un flujo de salida para escribir binario estructurado.
5. Escribe un valor entero en binario respetando el orden del formato.
6. Escribe el campo de texto en binario usando UTF.
7. Escribe un valor entero en binario respetando el orden del formato.
8. Abre un flujo de entrada para leer el binario estructurado.
9. Lee un valor entero desde el binario.
10. Lee un campo de texto UTF desde el binario.
11. Lee un valor entero desde el binario.
!!!Note ""
    El mismo ejermplo pero utilizando la libreía **Jackson** en lugar de Kotlinx.serialization

<a id="ejemplo-convertir-listajson-a-binario-jackson"></a>

🖥️ **Ejemplo_convertir_listajson_a_binario_Jackson.kt**         


Este ejemplo realiza la misma conversión que el anterior, pero usando Jackson para leer el archivo JSON en lugar de `kotlinx.serialization`.

La finalidad del ejercicio es comparar dos formas de obtener los mismos objetos desde JSON y comprobar que, una vez creados en memoria, el proceso de escritura binaria es exactamente el mismo.

```kotlin
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.io.DataInputStream
import java.io.FileInputStream

fun main() {
    val rutaJson = "documentos/lista_personas.json"
    val rutaBin = "documentos/lista_personas.dat"

    // Leer JSON con Jackson
    val mapper = jacksonObjectMapper() // (1)!
    val personas: List<Persona> = mapper.readValue(File(rutaJson)) // (2)!

    // Crear carpeta si no existe
    Files.createDirectories(Paths.get(rutaBin).parent) // (3)!

    // Escribir como binario estructurado
    val salida = DataOutputStream(FileOutputStream(rutaBin)) // (4)!
    salida.writeInt(personas.size) // Guardar el tamaño de la lista // (5)!
    for (persona in personas) {
        salida.writeUTF(persona.nombre) // (6)!
        salida.writeInt(persona.edad) // (7)!
    }
    salida.close()

    println("✅ Lista de personas guardada como binario estructurado en: $rutaBin")

    // Leer el binario estructurado
    val entrada = DataInputStream(FileInputStream(rutaBin)) // (8)!
    val cantidad = entrada.readInt() // Leer el tamaño de la lista // (9)!
    for (i in 0 until cantidad) {
        val nombre = entrada.readUTF() // (10)!
        val edad = entrada.readInt() // (11)!

        println("Nombre: $nombre, Edad: $edad")
    }
    entrada.close()
}


```

1. Crea el mapper de Jackson para la conversion entre objetos y JSON.
2. Deserializa el JSON en una lista tipada de objetos.
3. Asegura que exista la carpeta de salida antes de escribir el fichero.
4. Abre un flujo de salida para escribir binario estructurado.
5. Escribe un valor entero en binario respetando el orden del formato.
6. Escribe el campo de texto en binario usando UTF.
7. Escribe un valor entero en binario respetando el orden del formato.
8. Abre un flujo de entrada para leer el binario estructurado.
9. Lee un valor entero desde el binario.
10. Lee un campo de texto UTF desde el binario.
11. Lee un valor entero desde el binario.
<a id="ejemplo-convertir-binario-a-json"></a>

🖥️ **Ejemplo_convertir_binario_a_json.kt**


Este ejemplo muestra la conversión inversa: leer un fichero binario estructurado, reconstruir un objeto `Persona` y generar a partir de él un archivo JSON.

La finalidad del ejercicio es entender que para recuperar información desde un binario estructurado hay que leer cada dato respetando exactamente el orden y tipo con el que se almacenó.

```kotlin
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val rutaBin = "documentos/persona.dat"
    val rutaJson = "documentos/persona.json"

    // Leer binario estructurado
    val entrada = DataInputStream(FileInputStream(rutaBin)) // (1)!
    val nombre = entrada.readUTF() // (2)!
    val edad = entrada.readInt() // (3)!
    entrada.close()

    // Crear objeto
    val persona = Persona(nombre, edad)

    // Convertir a JSON con pretty print
    val json = Json { prettyPrint = true }.encodeToString(persona) // (4)!

    // Crear carpeta si no existe
    Files.createDirectories(Paths.get(rutaJson).parent) // (5)!

    // Escribir JSON en archivo
    File(rutaJson).writeText(json) // (6)!

    println("✅ Binario estructurado convertido a JSON:")
    println(json)
}


```

1. Abre un flujo de entrada para leer el binario estructurado.
2. Lee un campo de texto UTF desde el binario.
3. Lee un valor entero desde el binario.
4. Serializa el objeto o lista a JSON con formato legible.
5. Asegura que exista la carpeta de salida antes de escribir el fichero.
6. Guarda el JSON generado en el fichero de salida.
!!!Note "Fichero Binario compuesto por una lista de objetos"
    Si el fichero **Binario** contiene una **lista de objetos**, debes leer el tamaño de la lista y luego leer cada objeto uno por uno, construir la lista y convertirla a JSON.

<a id="ejemplo-convertir-listabinario-a-json"></a>

🖥️ **Ejemplo_convertir_listabinario_a_json.kt**


Este ejemplo convierte un fichero binario estructurado que contiene varias personas en un archivo JSON con una lista de objetos.

La finalidad del ejercicio es comprobar cómo una secuencia de registros binarios puede reconstruirse como una colección de objetos Kotlin y exportarse después a un formato textual más legible.

```kotlin
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val rutaBin = "documentos/lista_personas.dat"
    val rutaJson = "documentos/lista_personas.json"

    // Leer binario estructurado
    val entrada = DataInputStream(FileInputStream(rutaBin)) // (1)!
    val cantidad = entrada.readInt() // Leer el tamaño de la lista // (2)!
    val personas = mutableListOf<Persona>()
    for (i in 0 until cantidad) {
        val nombre = entrada.readUTF() // (3)!
        val edad = entrada.readInt() // (4)!
        personas.add(Persona(nombre, edad))
    }
    entrada.close()

    // Convertir a JSON con pretty print
    val json = Json { prettyPrint = true }.encodeToString(personas) // (5)!

    // Crear carpeta si no existe
    Files.createDirectories(Paths.get(rutaJson).parent) // (6)!

    // Escribir JSON en archivo
    File(rutaJson).writeText(json) // (7)!

    println("✅ Lista de personas convertida a JSON:")
    println(json)
}

```

1. Abre un flujo de entrada para leer el binario estructurado.
2. Lee un valor entero desde el binario.
3. Lee un campo de texto UTF desde el binario.
4. Lee un valor entero desde el binario.
5. Serializa el objeto o lista a JSON con formato legible.
6. Asegura que exista la carpeta de salida antes de escribir el fichero.
7. Guarda el JSON generado en el fichero de salida.
!!!Note ""
    El mismo ejermplo pero utilizando la libreía **Jackson** en lugar de Kotlinx.serialization


<a id="ejemplo-convertir-listabinario-a-json-jackson"></a>

🖥️ **Ejemplo_convertir_listabinario_a_json_Jackson.kt**


Este ejemplo repite la conversión de binario estructurado a JSON, pero utilizando Jackson para generar la salida en lugar de `kotlinx.serialization`.

La finalidad del ejercicio es ver que distintas librerías pueden utilizarse para generar el JSON final, mientras que la parte crítica de lectura binaria sigue dependiendo del orden y del tipo de los datos almacenados.

```kotlin
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val rutaBin = "documentos/lista_personas.dat"
    val rutaJson = "documentos/lista_personas.json"

    // Leer binario estructurado
    val entrada = DataInputStream(FileInputStream(rutaBin)) // (1)!
    val cantidad = entrada.readInt() // Leer el tamaño de la lista // (2)!
    val personas = mutableListOf<Persona>()
    for (i in 0 until cantidad) {
        val nombre = entrada.readUTF() // (3)!
        val edad = entrada.readInt() // (4)!
        personas.add(Persona(nombre, edad))
    }
    entrada.close()

    // Convertir a JSON con pretty print usando Jackson
    val mapper = jacksonObjectMapper() // (5)!
    mapper.writerWithDefaultPrettyPrinter().writeValue(File(rutaJson), personas) // (6)!

    // Crear carpeta si no existe
    Files.createDirectories(Paths.get(rutaJson).parent) // (7)!

    println("✅ Lista de personas convertida a JSON con Jackson:")
    println(mapper.writeValueAsString(personas))
}



```

1. Abre un flujo de entrada para leer el binario estructurado.
2. Lee un valor entero desde el binario.
3. Lee un campo de texto UTF desde el binario.
4. Lee un valor entero desde el binario.
5. Crea el mapper de Jackson para la conversion entre objetos y JSON.
6. Escribe el resultado en el fichero de salida con formato legible.
7. Asegura que exista la carpeta de salida antes de escribir el fichero.
