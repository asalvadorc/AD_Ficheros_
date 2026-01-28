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





## 🔹 Ejemplos de Conversión

!!!warning "Ejemplos"
    Los siguientes ejemplos tamién se incluirán en el proyecto **Ficheros_Gradle**.

En los siguientes ejemplos trabajaremos con tres archivos en diferentes formatos: **CSV**, **XML** y **JSON**. Estos archivos contienen información similar, representada con distinta estructura y sintaxis según el formato. Los utilizaremos como base para realizar ejercicios de conversión entre formatos.

Contenido de los archivos:


| **alumnos.csv** | **persona.xml** | **persona.json** |
|-----------------|-----------------|------------------|
| nombre;nota<br>Lucía;28<br>Carlos;8<br>Elena;10 | &lt;Persona&gt;<br>&nbsp;&nbsp;&lt;nombre&gt;Lucía&lt;/nombre&gt;<br>&nbsp;&nbsp;&lt;edad&gt;28&lt;/edad&gt;<br>&lt;/Persona&gt; | {<br>&nbsp;&nbsp;"nombre" : "Lucía",<br>&nbsp;&nbsp;"edad" : 28<br>} |


### **CSV <-> JSON**{.azul}

En estos ejemplos utilizamos la librería **Jackson**, pero se podría  utilizar también **Kotlinx.serialization**.

!!!Note ""
    El intermediario entre el CSV y el JSON es **la lista de objetos alumnos** (de tipo Alumno)

🖥️ **Ejemplo_convertir_csv_a_json.kt**


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


🖥️ **Ejemplo_convertir_json_a_csv.kt**        

        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.opencsv.CSVWriter
        import java.io.File
        import java.io.FileWriter


        fun main() {
            val rutaJson = "documentos/alumnos.json"
            val rutaCsv = "documentos/alumnos_convertido.csv"

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




### **JSON <-> XML**{.azul}

!!!Note ""
    En estos ejemplos utilizamos **Jackson**, en ambas conversiones, y por lo tanto también utiliza un objeto intermediario (**persona**), aunque de forma más implícita.

🖥️ **Ejemplo_convertir_json_a_xml.kt**

        import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.fasterxml.jackson.module.kotlin.registerKotlinModule
        import java.io.File

        fun convertirJsonAXml(jsonPath: String, xmlPath: String) {
            val xmlMapper = XmlMapper().registerKotlinModule()
            val jsonMapper = jacksonObjectMapper()


            val persona = jsonMapper.readValue<Persona>(File(jsonPath))
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(File(xmlPath), persona)

            println("Conversión JSON → XML completada")
        }

        fun main() {
            convertirJsonAXml("documentos/persona.json", "documentos/persona_convertida.xml")


        }

!!!Note "Fichero JSON compuesto por una lista de elementos"
    Si el fichero **JSON** contiene un *array* (`[...]`), es decir, una **lista de objetos**, entonces debemos indicar explícitamente que queremos leer un `List<Objeto>`. 

    **lista_personas_jackson.json** 

        [ {
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
        } ]

🖥️ **Ejemplo_convertir_listajson_a_xml.kt**

        import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.readValue
        import java.io.File
        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import com.fasterxml.jackson.module.kotlin.registerKotlinModule


        fun convertirListaJsonAXml(jsonPath: String, xmlPath: String) {
            
            val jsonMapper = jacksonObjectMapper()
            val xmlMapper = XmlMapper().registerKotlinModule()


            val personas: List<Persona> = jsonMapper.readValue(File(jsonPath))
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(File(xmlPath), personas)

            println("Conversión JSON → XML completada")
        }

        fun main() {

            convertirListaJsonAXml("documentos/lista_personas_jackson.json", "documentos/lista_personas_jackson.xml")


        }


🖥️ **Ejemplo_convertir_xml_a_json.kt**


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

            convertirXmlAJson("documentos/persona.xml", "documentos/persona_convertida.json")

        }


!!!Note "Fichero XML compuesto por una lista de elementos"
    Si el fichero **XML** contiene una **lista de objetos**, entonces debemos indicar explícitamente que queremos leer un `List<Objeto>`.  

    **lista_personas_jackson.xml**

        <Personas>
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
        </Personas>

🖥️ **Ejemplo_convertir_listaxml_a_json.kt**

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

            convertirListaXmlAJson("documentos/lista_personas_jackson.xml", "documentos/lista_personas_convertida.json")
        }



### **JSON <-> Binario estructurado**{.azul}

En estos ejemplos utilizamos **kotlinx.serialization**.

!!!Note ""
    El objeto **persona** (instancia de la clase Persona) es el intermediario entre el archivo JSON y el archivo binario.

🖥️ **Ejemplo_convertir_json_a_binario.kt**

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

🖥️ **Ejemplo_convertir_listajson_a_binario.kt**        

        import kotlinx.serialization.json.Json
        import java.io.DataOutputStream
        import java.io.File
        import java.io.FileOutputStream
        import java.nio.file.Files
        import java.nio.file.Paths
        import java.io.DataInputStream
        import java.io.FileInputStream

        fun main() {
            val rutaJson = "documentos/lista_personas_jackson.json"
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

🖥️ **Ejemplo_convertir_listajson_a_binario_Jackson.kt**         

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
            val rutaJson = "documentos/lista_personas_jackson.json"
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





🖥️ **Ejemplo_convertir_binario_a_json.kt**

        import kotlinx.serialization.encodeToString
        import kotlinx.serialization.json.Json
        import java.io.DataInputStream
        import java.io.File
        import java.io.FileInputStream
        import java.nio.file.Files
        import java.nio.file.Paths

        fun main() {
            val rutaBin = "documentos/persona.dat"
            val rutaJson = "documentos/persona_convertidaBinario.json"

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

🖥️ **Ejemplo_convertir_listabinario_a_json.kt**

            import kotlinx.serialization.encodeToString
            import kotlinx.serialization.json.Json
            import java.io.DataInputStream
            import java.io.File
            import java.io.FileInputStream
            import java.nio.file.Files
            import java.nio.file.Paths

            fun main() {
                val rutaBin = "documentos/lista_personas.dat"
                val rutaJson = "documentos/lista_personas_convertidaBinario.json"

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


🖥️ **Ejemplo_convertir_listabinario_a_json_Jackson.kt**

        import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
        import java.io.DataInputStream
        import java.io.File
        import java.io.FileInputStream
        import java.nio.file.Files
        import java.nio.file.Paths

        fun main() {
            val rutaBin = "documentos/lista_personas.dat"
            val rutaJson = "documentos/lista_personas_convertidaBinario.json"

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
