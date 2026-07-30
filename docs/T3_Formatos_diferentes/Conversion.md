# 🔹 Conversión entre formatos de ficheros en Kotlin/Java

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


## 🔹 Ejemplos de conversión

!!!warning "Ejemplos"
    Los siguientes ejemplos también se incluirán en el proyecto **Ficheros_Gradle**.

En los siguientes ejemplos trabajaremos con tres archivos en diferentes formatos: **CSV**, **XML** y **JSON**. Estos archivos contienen información similar, representada con distinta estructura y sintaxis según el formato. Los utilizaremos como base para realizar ejercicios de conversión entre formatos.

Contenido de los archivos:


| **alumnos.csv** | **persona.xml** | **persona.json** |
|-----------------|-----------------|------------------|
| nombre;nota<br>Lucía;28<br>Carlos;8<br>Elena;10 | &lt;Persona&gt;<br>&nbsp;&nbsp;&lt;nombre&gt;Lucía&lt;/nombre&gt;<br>&nbsp;&nbsp;&lt;edad&gt;28&lt;/edad&gt;<br>&lt;/Persona&gt; | {<br>&nbsp;&nbsp;"nombre" : "Lucía",<br>&nbsp;&nbsp;"edad" : 28<br>} |

!!!Tip "Data Class"
    Al trabajar con ficheros de intercambio como CSV, JSON o XML, es habitual encontrarnos con datos estructurados formados por distintos campos. Para poder manejar estos datos de forma cómoda y segura en Kotlin, es recomendable representarlos mediante **data class**, que permiten modelar la información con tipos y nombres claros. Una vez los datos están representados como objetos, el formato original del fichero deja de ser relevante. Esta idea será fundamental en el siguiente apartado, donde se utilizarán los **data class** como elemento intermedio para transformar la información entre distintos formatos de fichero, como CSV, JSON, XML o binario.  
    
    📌 **Nota:** Las clases **Alumno** y **Persona** ya las creamos en los ejemplos sobre ficheros de intercambio y las volveremos a utilizar en los siguientes ejemplos.

    **Alumno.kt** 

        data class Alumno(
                        val nombre: String,
                        val nota: Int
                    )    
    **Persona.kt** 

        @Serializable
        data class Persona(
            val nombre: String, val edad: Int
            )   


| Alumno | Persona |
|--------|---------|
| data class Alumno(<br>&nbsp;&nbsp;&nbsp;val nombre: String,<br>&nbsp;&nbsp;&nbsp;val nota: Int<br>) | data class Persona(<br>&nbsp;&nbsp;&nbsp;val nombre: String,<br>&nbsp;&nbsp;&nbsp;val edad: Int<br>) |


### 🔹 CSV <-> JSON {.azul}

En estos ejemplos utilizamos la librería **Jackson**, pero se podría  utilizar también **Kotlinx.serialization**.

**Resumen de ejemplos**{.azul}

- [Ejemplo_convertir_csv_a_json.kt](#ejemplo-convertir-csv-a-json): convierte un CSV en JSON mediante una lista de objetos `Alumno`.


- [Ejemplo_convertir_json_a_csv.kt](#ejemplo-convertir-json-a-csv): convierte un JSON con una lista de alumnos en un fichero CSV.


!!!Note ""
    El intermediario entre el CSV y el JSON es **la lista de objetos alumnos** (de tipo Alumno)


**Alumno.kt**   (Clase ya creada)

    

<a id="ejemplo-convertir-csv-a-json"></a>

🖥️ **Ejemplo_convertir_csv_a_json.kt**


Este ejemplo muestra cómo convertir un fichero CSV en un archivo JSON tomando como paso intermedio una lista de objetos `Alumno`.

- Primero se definen las rutas del archivo CSV de entrada y del archivo JSON de salida.
- Después se abre el CSV con `CSVReaderBuilder`, configurando el separador `;` y omitiendo la cabecera.
- A continuación se leen todos los registros del fichero.
- Luego se recorre cada fila del CSV y se extraen sus campos para construir un objeto `Alumno`.
- Cada objeto creado se añade a una lista, que actuará como representación estructurada de todo el contenido del CSV.
- Una vez cerrada la lectura, se crea un `ObjectMapper` de Jackson.
- Finalmente, esa lista de objetos se escribe en un archivo JSON con `writeValue(...)`, generando una salida legible y estructurada.

La finalidad del ejercicio es entender que la conversión entre formatos no se hace directamente de CSV a JSON, sino pasando antes por objetos Kotlin que representan los datos de forma tipada.


        import com.opencsv.CSVReaderBuilder
        import com.opencsv.CSVParserBuilder
        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import java.io.File
        import java.io.FileReader


        fun main() {
            val rutaCSV = "documentos/alumnos.csv"
            val rutaJSON = "documentos/alumnos.json"

            val reader = CSVReaderBuilder(FileReader(rutaCSV))
                .withCSVParser(CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1)
                .build()

            val registros = reader.readAll()

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

            
            val mapper = jacksonObjectMapper()
            mapper.writerWithDefaultPrettyPrinter().writeValue(File(rutaJSON), alumnos)

            println("✅ Conversión CSV → JSON completada: $rutaJSON")
        }


<a id="ejemplo-convertir-json-a-csv"></a>

🖥️ **Ejemplo_convertir_json_a_csv.kt**        


Este ejemplo realiza la conversión inversa: parte de un archivo JSON con una lista de alumnos y genera a partir de él un fichero CSV.

- Primero se definen las rutas del archivo JSON de entrada y del archivo CSV de salida.
- Después se crea un `ObjectMapper` de Jackson para leer el contenido del JSON.
- A continuación se deserializa el archivo en una lista de objetos `Alumno` con `readValue(...)`.
- Luego se abre un `CSVWriter`, configurado para usar `;` como separador.
- Se escribe primero la cabecera del archivo CSV.
- Después se recorre la lista de alumnos y se escribe cada objeto como una fila, convirtiendo sus propiedades a texto.
- Finalmente, el resultado es un archivo CSV que puede abrirse en una hoja de cálculo o reutilizarse en otros programas.

La finalidad del ejercicio es entender que, igual que en la conversión anterior, el paso clave entre formatos vuelve a ser una colección de objetos Kotlin que representa los datos de forma estructurada.

        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.opencsv.CSVWriter
        import java.io.File
        import java.io.FileWriter


        fun main() {
            val rutaJson = "documentos/alumnos.json"
            val rutaCsv = "documentos/alumnos.csv"

            // 1. Leer JSON
            val mapper = jacksonObjectMapper()
            val alumnos: List<Alumno> = mapper.readValue(File(rutaJson))

            // 2. Escribir CSV
            val writer = CSVWriter(FileWriter(rutaCsv), ';', CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)

            // Cabecera
            writer.writeNext(arrayOf("nombre", "nota"))

            // Cuerpo
            for (alumno in alumnos) {
                writer.writeNext(arrayOf(alumno.nombre, alumno.nota.toString()))
            }

            writer.close()

            println("✅ Conversión JSON → CSV completada: $rutaCsv")
        }


### 🔹 JSON <-> XML {.azul}

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

- Primero se crean dos mapeadores: `ObjectMapper` para trabajar con JSON y `XmlMapper` para generar XML.
- Después se lee el archivo JSON de entrada y se deserializa en un objeto `Persona`.
- Ese objeto actúa como puente entre ambos formatos, ya que contiene la información de forma estructurada y tipada.
- A continuación se utiliza `XmlMapper` para escribir el objeto en un archivo XML.
- La opción `writerWithDefaultPrettyPrinter()` permite que el resultado se genere con un formato más legible.
- Finalmente, se muestra un mensaje indicando que la conversión se ha completado correctamente.

La finalidad del ejercicio es entender que la conversión de JSON a XML no se hace directamente entre textos, sino reconstruyendo primero un objeto en memoria y generando después el nuevo formato a partir de él.

        import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.fasterxml.jackson.module.kotlin.registerKotlinModule
        import java.io.File

        fun convertirJsonAXml(jsonPath: String, xmlPath: String) {
            val xmlMapper = XmlMapper().registerKotlinModule()
            val jsonMapper = jacksonObjectMapper()

            //Leer JSON y convertirlo a un objeto Persona
            val persona = jsonMapper.readValue<Persona>(File(jsonPath))

            // Escribir el objeto Persona en formato XML
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(File(xmlPath), persona)

            println("Conversión JSON → XML completada")
        }

        fun main() {
            convertirJsonAXml("documentos/persona.json", "documentos/persona.xml")


        }

!!!Note "Fichero JSON compuesto por una lista de elementos"
    Si el fichero **JSON** contiene un *array* (`[...]`), es decir, una **lista de objetos**, entonces debemos indicar explícitamente que queremos leer un `List<Objeto>`. 

    **lista_personas_jackson.json** 

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

    <a id="ejemplo-convertir-listajson-a-xml"></a>

🖥️ **Ejemplo_convertir_listajson_a_xml.kt**


Este ejemplo amplía la conversión anterior para trabajar con un archivo JSON que contiene una lista de objetos `Persona` y generar a partir de ella un XML.

- Primero se crean un `ObjectMapper` para JSON y un `XmlMapper` para XML.
- Después se lee el archivo JSON de entrada y se deserializa en una lista de objetos `Persona`.
- Esa lista actúa como estructura intermedia en memoria, igual que ocurría en las conversiones anteriores.
- A continuación se escribe directamente la lista en un archivo XML con `writeValue(...)`.
- Finalmente, se muestra un mensaje indicando que la conversión ha terminado.

La finalidad del ejercicio es comprobar que Jackson también puede serializar colecciones completas, aunque el XML generado automáticamente no siempre tendrá la estructura más adecuada.

        import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import java.io.File
        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.registerKotlinModule


        fun convertirListaJsonAXml(jsonPath: String, xmlPath: String) {
            
            val jsonMapper = jacksonObjectMapper()
            val xmlMapper = XmlMapper().registerKotlinModule()

            // Leer JSON y convertirlo a lista de objetos Persona
            val personas: List<Persona> = jsonMapper.readValue(File(jsonPath))

            // Escribir el XML
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(File(xmlPath), personas)

            println("Conversión JSON → XML completada")
        }

        fun main() {

            convertirListaJsonAXml("documentos/lista_personas.json", "documentos/lista_personas.xml")

        }


El contenido del fichero xml convertido sería el siguiente:

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

⚠️ Esto no es correcto porque, aunque el XML se puede leer, cuando un XML representa una lista, siempre debe tener un elemento raíz con significado.

!!!Tip "Clase contenedora"
    Cuando se convierte una lista de JSON a XML, es recomendable utilizar un **data class** para modelar los datos y una **clase contenedora auxiliar** para representar el nodo raíz del XML.  

📌 Creamos la Clase contenedora **ListaPersonas** en el **paquete Ejemplos**, fuera de los programas, para poder reutilizarla:

        import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

        data class ListaPersonas(
            @JacksonXmlElementWrapper(useWrapping = false)
            val persona: List<Persona>
        )        


<a id="ejemplo-convertir-listajson-a-xml-nodo"></a>

🖥️ **Ejemplo_convertir_listajson_a_xml_nodo.kt**


Este ejemplo corrige el problema del caso anterior utilizando una clase contenedora para que el XML resultante tenga un nodo raíz con significado.

- Primero se leen los datos del archivo JSON y se convierten en una lista de objetos `Persona`.
- Después esa lista se envuelve en un objeto `ListaPersonas`, que representa el nodo raíz del XML.
- A continuación se utiliza `XmlMapper` para serializar ese objeto contenedor.
- Gracias a esta clase auxiliar, los elementos quedan agrupados bajo una etiqueta raíz coherente.
- Finalmente, se guarda el resultado en un archivo XML con una estructura más correcta y reutilizable.

La finalidad del ejercicio es entender que, cuando un XML representa una colección, suele ser necesario crear explícitamente una clase contenedora que modele el nodo raíz del documento.

        import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.fasterxml.jackson.module.kotlin.registerKotlinModule
        import java.io.File

        fun convertirListaJsonAXml_nodo(jsonPath: String, xmlPath: String) {
            val jsonMapper = jacksonObjectMapper()
            val xmlMapper = XmlMapper().registerKotlinModule()

            // Leer JSON y convertirlo a lista de objetos Persona
            val personas: List<Persona> = jsonMapper.readValue(File(jsonPath))

            // Envolver la lista en la clase contenedora para XML
            val listaPersonas = ListaPersonas(personas)

            // Escribir el XML
            xmlMapper.writerWithDefaultPrettyPrinter()
                .writeValue(File(xmlPath), listaPersonas)

            println("✅ Conversión JSON → XML completada")
        }

        fun main() {
            convertirListaJsonAXml_nodo(
                "documentos/lista_personas.json",
                "documentos/lista_personas_nodo.xml"
            )
        }


Ahora la conversión si que es correcta:

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

    <a id="ejemplo-convertir-xml-a-json"></a>

🖥️ **Ejemplo_convertir_xml_a_json.kt**


Este ejemplo realiza la conversión inversa al primero de esta sección: parte de un archivo XML y genera un fichero JSON a partir de un objeto `Persona` intermedio.

- Primero se crean un `XmlMapper` para leer XML y un `ObjectMapper` para escribir JSON.
- Después se abre el archivo XML de entrada y se deserializa en un objeto `Persona`.
- Ese objeto vuelve a actuar como representación estructurada en memoria de los datos.
- A continuación se utiliza Jackson para escribir ese objeto en un archivo JSON con formato legible.
- Finalmente, se muestra un mensaje indicando que la conversión ha finalizado correctamente.

La finalidad del ejercicio es comprobar que la misma idea se mantiene en sentido inverso: leer un formato, reconstruir un objeto y generar después el nuevo formato de salida.


       import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.fasterxml.jackson.module.kotlin.registerKotlinModule
        import java.io.File

        fun convertirXmlAJson(xmlPath: String, jsonPath: String) {
            val xmlMapper = XmlMapper().registerKotlinModule()
            val jsonMapper = jacksonObjectMapper()


            val persona = xmlMapper.readValue<Persona>(File(xmlPath))
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(File(jsonPath), persona)

            println("Conversión XML → JSON completada")
        }

        fun main() {

            convertirXmlAJson("documentos/persona.xml", "documentos/persona.json")

        }


!!!Note "Fichero XML compuesto por una lista de elementos"
    Si el fichero **XML** contiene una **lista de objetos**, entonces debemos indicar explícitamente que queremos leer un `List<Objeto>`.  

    **lista_personas.xml**

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

    <a id="ejemplo-convertir-listaxml-a-json"></a>

🖥️ **Ejemplo_convertir_listaxml_a_json.kt**


Este ejemplo convierte un XML que contiene una lista de personas en un archivo JSON con un array de objetos.

- Primero se crean los mapeadores necesarios para XML y JSON.
- Después se lee el archivo XML de entrada y se deserializa directamente en una lista de objetos `Persona`.
- Esa lista representa en memoria toda la información recuperada del XML.
- A continuación se escribe la lista en formato JSON con `writerWithDefaultPrettyPrinter()` para obtener una salida más legible.
- Finalmente, se guarda el resultado en el archivo de salida y se informa de que la conversión ha terminado.

La finalidad del ejercicio es ver cómo una colección leída desde XML puede serializarse después como un array JSON sin procesar manualmente cada elemento.

        import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.fasterxml.jackson.module.kotlin.registerKotlinModule
        import java.io.File

        fun convertirListaXmlAJson(xmlPath: String, jsonPath: String) {
            val jsonMapper = jacksonObjectMapper()
            val xmlMapper = XmlMapper().registerKotlinModule()

            val personas: List<Persona> = xmlMapper.readValue(File(xmlPath))
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(File(jsonPath), personas)

            println("Conversión XML → JSON completada")
        }

        fun main() {

            convertirListaXmlAJson("documentos/lista_personas.xml", "documentos/lista_personas.json")
        }

!!!Tip "Clase contenedora"
    Cuando el XML contiene un nodo raíz que agrupa varios elementos, es conveniente utilizar una **clase contenedora auxiliar** para mapear correctamente la estructura del documento.

    **lista_personas_nodo.xml**

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


📌 Utilizaremos la clase contenedora **ListapPersona** creada anteriormente.

<a id="ejemplo-convertir-listaxml-a-json-nodo"></a>

🖥️ **Ejemplo_convertir_listaxml_a_json_nodo.kt**


Este ejemplo parte de un XML con un nodo raíz significativo y usa una clase contenedora para convertir correctamente su contenido en un array JSON.

- Primero se crea un `XmlMapper` para leer el XML y un `ObjectMapper` para escribir el JSON.
- Después se deserializa el archivo XML en un objeto `ListaPersonas`, que representa el nodo raíz del documento.
- A continuación se extrae de ese contenedor la lista de objetos `Persona`.
- Luego esa lista se serializa a JSON como un array de elementos.
- Finalmente, se escribe el resultado en el archivo de salida con formato legible.

La finalidad del ejercicio es entender cómo una clase contenedora permite mapear correctamente la estructura jerárquica del XML antes de convertirla en una lista JSON.


        import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.fasterxml.jackson.module.kotlin.registerKotlinModule
        import java.io.File
        import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

        fun convertirListaXmlAJsonNodo(xmlPath: String, jsonPath: String) {
            val jsonMapper = jacksonObjectMapper()
            val xmlMapper = XmlMapper().registerKotlinModule()

            // Lee el XML y lo convierte a un objeto de tipo ListaPersonas
            // Aquí utilizamos la clase contenedora para representar el nodo raíz del XML
            val lista: ListaPersonas = xmlMapper.readValue(File(xmlPath)) //Aquí utilizamos la clase contendora

            // Extrae la lista de objetos Persona desde la clase contenedora
            val personas = lista.persona

            // Escribe la lista de personas en formato JSON
            // El JSON generado será un array de objetos
            jsonMapper.writerWithDefaultPrettyPrinter()
                .writeValue(File(jsonPath), personas)

            println("Conversión XML → JSON completada")
        }

        fun main() {

            convertirListaXmlAJsonNodo("documentos/lista_personas_nodo.xml", "documentos/lista_personas.json")
        }

### 🔹 JSON <-> Binario estructurado {.azul}

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

- Primero se definen las rutas del archivo JSON de entrada y del fichero binario de salida.
- Después se lee el contenido del JSON como texto y se deserializa en un objeto `Persona`.
- A continuación se crea la carpeta de destino si no existe.
- Luego se abre un `DataOutputStream` para escribir los datos en formato binario estructurado.
- El nombre se guarda con `writeUTF(...)` y la edad con `writeInt(...)`, respetando un orden fijo.
- Finalmente, el ejemplo vuelve a leer el fichero binario para comprobar que los datos almacenados se recuperan correctamente.

La finalidad del ejercicio es entender cómo pasar de un formato textual como JSON a un formato binario tipado, escribiendo cada campo con el método adecuado.

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
            val contenido = File(rutaJson).readText()
            val persona = Json.decodeFromString<Persona>(contenido)

            // Crear carpeta si no existe
            Files.createDirectories(Paths.get(rutaBin).parent)

            // Escribir como binario estructurado
            val salida = DataOutputStream(FileOutputStream(rutaBin))
            salida.writeUTF(persona.nombre)  // Guarda string como UTF con longitud
            salida.writeInt(persona.edad)    // Guarda entero (4 bytes)
            salida.close()

            println("✅ .Persona guardada como binario estructurado en: $rutaBin")

            //Leer el binario estructurado
            val entrada = DataInputStream(FileInputStream(rutaBin))
            val nombre = entrada.readUTF()
            val edad = entrada.readInt()
            entrada.close()

            println("📄 Persona leída del binario:")
            println("Nombre: $nombre, Edad: $edad")

        }

!!!Note "Fichero JSON compuesto por una lista de elementos"
    Si el fichero **JSON** contiene un *array* (`[...]`), es decir, una **lista de objetos**, necesitas iterar sobre la lista al escribir y al leer.

<a id="ejemplo-convertir-listajson-a-binario"></a>

🖥️ **Ejemplo_convertir_listajson_a_binario.kt**        


Este ejemplo amplía el caso anterior para convertir una lista de personas en un fichero binario estructurado.

- Primero se lee el archivo JSON y se deserializa en una lista de objetos `Persona`.
- Después se crea la carpeta de destino si todavía no existe.
- A continuación se abre un `DataOutputStream` para escribir los datos en binario.
- Primero se guarda el número de elementos de la lista con `writeInt(...)`.
- Luego se recorren todas las personas y se escriben sus campos en el mismo orden para cada registro.
- Finalmente, el ejemplo vuelve a leer el fichero binario para comprobar que todos los datos se han guardado y recuperado correctamente.

La finalidad del ejercicio es entender que, cuando se trabaja con colecciones en binario estructurado, hay que almacenar también cuántos elementos contiene la lista antes de escribir sus datos.

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
            val contenido = File(rutaJson).readText()
            val personas = Json.decodeFromString<List<Persona>>(contenido)

            // Crear carpeta si no existe
            Files.createDirectories(Paths.get(rutaBin).parent)

            // Escribir como binario estructurado
            val salida = DataOutputStream(FileOutputStream(rutaBin))
            salida.writeInt(personas.size) // Guardar el tamaño de la lista
            for (persona in personas) {
                salida.writeUTF(persona.nombre)
                salida.writeInt(persona.edad)
            }
            salida.close()

            println("✅ Lista de personas guardada como binario estructurado en: $rutaBin")

            // Leer el binario estructurado
            val entrada = DataInputStream(FileInputStream(rutaBin))
            val cantidad = entrada.readInt() // Leer el tamaño de la lista
            for (i in 0 until cantidad) {
                val nombre = entrada.readUTF()
                val edad = entrada.readInt()

                println("Nombre: $nombre, Edad: $edad")
            }
            entrada.close()
        }

!!!Note ""
    El mismo ejermplo pero utilizando la libreía **Jackson** en lugar de Kotlinx.serialization

<a id="ejemplo-convertir-listajson-a-binario-jackson"></a>

🖥️ **Ejemplo_convertir_listajson_a_binario_Jackson.kt**         


Este ejemplo realiza la misma conversión que el anterior, pero usando Jackson para leer el archivo JSON en lugar de `kotlinx.serialization`.

- Primero se crea un `ObjectMapper` y se deserializa el archivo JSON en una lista de objetos `Persona`.
- Después se prepara la carpeta de salida si no existe.
- A continuación se abre un `DataOutputStream` para escribir la lista en formato binario estructurado.
- Se guarda primero el tamaño de la colección y luego se escriben, para cada persona, el nombre y la edad en el orden establecido.
- Finalmente, se vuelve a leer el fichero binario para verificar que la conversión se ha realizado correctamente.

La finalidad del ejercicio es comparar dos formas de obtener los mismos objetos desde JSON y comprobar que, una vez creados en memoria, el proceso de escritura binaria es exactamente el mismo.

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
            val mapper = jacksonObjectMapper()
            val personas: List<Persona> = mapper.readValue(File(rutaJson))

            // Crear carpeta si no existe
            Files.createDirectories(Paths.get(rutaBin).parent)

            // Escribir como binario estructurado
            val salida = DataOutputStream(FileOutputStream(rutaBin))
            salida.writeInt(personas.size) // Guardar el tamaño de la lista
            for (persona in personas) {
                salida.writeUTF(persona.nombre)
                salida.writeInt(persona.edad)
            }
            salida.close()

            println("✅ Lista de personas guardada como binario estructurado en: $rutaBin")

            // Leer el binario estructurado
            val entrada = DataInputStream(FileInputStream(rutaBin))
            val cantidad = entrada.readInt() // Leer el tamaño de la lista
            for (i in 0 until cantidad) {
                val nombre = entrada.readUTF()
                val edad = entrada.readInt()

                println("Nombre: $nombre, Edad: $edad")
            }
            entrada.close()
        }


<a id="ejemplo-convertir-binario-a-json"></a>

🖥️ **Ejemplo_convertir_binario_a_json.kt**


Este ejemplo muestra la conversión inversa: leer un fichero binario estructurado, reconstruir un objeto `Persona` y generar a partir de él un archivo JSON.

- Primero se abre el archivo binario con `DataInputStream`.
- Después se leen sus campos en el mismo orden en que fueron escritos: primero el nombre y después la edad.
- A continuación se crea un objeto `Persona` con los datos recuperados.
- Luego se convierte ese objeto a una cadena JSON con `Json.encodeToString(...)` y formato legible.
- Finalmente, se crea la carpeta de destino si hace falta y se escribe el JSON resultante en un archivo.

La finalidad del ejercicio es entender que para recuperar información desde un binario estructurado hay que leer cada dato respetando exactamente el orden y tipo con el que se almacenó.

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
            val entrada = DataInputStream(FileInputStream(rutaBin))
            val nombre = entrada.readUTF()
            val edad = entrada.readInt()
            entrada.close()

            // Crear objeto
            val persona = Persona(nombre, edad)

            // Convertir a JSON con pretty print
            val json = Json { prettyPrint = true }.encodeToString(persona)

            // Crear carpeta si no existe
            Files.createDirectories(Paths.get(rutaJson).parent)

            // Escribir JSON en archivo
            File(rutaJson).writeText(json)

            println("✅ Binario estructurado convertido a JSON:")
            println(json)
        }


!!!Note "Fichero Binario compuesto por una lista de objetos"
    Si el fichero **Binario** contiene una **lista de objetos**, debes leer el tamaño de la lista y luego leer cada objeto uno por uno, construir la lista y convertirla a JSON.

<a id="ejemplo-convertir-listabinario-a-json"></a>

🖥️ **Ejemplo_convertir_listabinario_a_json.kt**


Este ejemplo convierte un fichero binario estructurado que contiene varias personas en un archivo JSON con una lista de objetos.

- Primero se abre el archivo binario y se lee el número de elementos almacenados.
- Después se recorre ese número de registros y se leen, para cada uno, el nombre y la edad.
- Con esos datos se va construyendo una lista mutable de objetos `Persona`.
- A continuación se serializa toda la lista a JSON con `Json.encodeToString(...)` y formato bonito.
- Finalmente, se crea la carpeta de salida si es necesario y se escribe el contenido JSON en un archivo.

La finalidad del ejercicio es comprobar cómo una secuencia de registros binarios puede reconstruirse como una colección de objetos Kotlin y exportarse después a un formato textual más legible.

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
                val entrada = DataInputStream(FileInputStream(rutaBin))
                val cantidad = entrada.readInt() // Leer el tamaño de la lista
                val personas = mutableListOf<Persona>()
                for (i in 0 until cantidad) {
                    val nombre = entrada.readUTF()
                    val edad = entrada.readInt()
                    personas.add(Persona(nombre, edad))
                }
                entrada.close()

                // Convertir a JSON con pretty print
                val json = Json { prettyPrint = true }.encodeToString(personas)

                // Crear carpeta si no existe
                Files.createDirectories(Paths.get(rutaJson).parent)

                // Escribir JSON en archivo
                File(rutaJson).writeText(json)

                println("✅ Lista de personas convertida a JSON:")
                println(json)
            }

!!!Note ""
    El mismo ejermplo pero utilizando la libreía **Jackson** en lugar de Kotlinx.serialization


<a id="ejemplo-convertir-listabinario-a-json-jackson"></a>

🖥️ **Ejemplo_convertir_listabinario_a_json_Jackson.kt**


Este ejemplo repite la conversión de binario estructurado a JSON, pero utilizando Jackson para generar la salida en lugar de `kotlinx.serialization`.

- Primero se abre el fichero binario y se lee el tamaño de la lista almacenada.
- Después se recorren todos los registros y se reconstruyen los objetos `Persona` uno a uno.
- A continuación se crea un `ObjectMapper` de Jackson.
- Luego se serializa la lista completa de personas a un archivo JSON con formato legible.
- Finalmente, se muestra también por pantalla el contenido generado para comprobar el resultado.

La finalidad del ejercicio es ver que distintas librerías pueden utilizarse para generar el JSON final, mientras que la parte crítica de lectura binaria sigue dependiendo del orden y del tipo de los datos almacenados.

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
            val entrada = DataInputStream(FileInputStream(rutaBin))
            val cantidad = entrada.readInt() // Leer el tamaño de la lista
            val personas = mutableListOf<Persona>()
            for (i in 0 until cantidad) {
                val nombre = entrada.readUTF()
                val edad = entrada.readInt()
                personas.add(Persona(nombre, edad))
            }
            entrada.close()

            // Convertir a JSON con pretty print usando Jackson
            val mapper = jacksonObjectMapper()
            mapper.writerWithDefaultPrettyPrinter().writeValue(File(rutaJson), personas)

            // Crear carpeta si no existe
            Files.createDirectories(Paths.get(rutaJson).parent)

            println("✅ Lista de personas convertida a JSON con Jackson:")
            println(mapper.writeValueAsString(personas))
        }


