# 🔹 Conversión entre formatos de ficheros en Kotlin/Java

| Conversión                     | Herramientas recomendadas                                   | Proceso resumido                                                                 |
|-------------------------------|--------------------------------------------------------------|----------------------------------------------------------------------------------|
| **CSV → JSON**                | OpenCSV + kotlinx.serialization / Jackson                           | Leer CSV → mapear a objetos → serializar con `Json.encodeToString`              |
| **JSON → CSV**                | kotlinx.serialization / Jackson + OpenCSV                             | Deserializar JSON a objetos → escribir filas CSV                                |
| **CSV → XML**                 | OpenCSV + DOM (`DocumentBuilderFactory`)                    | Leer CSV → construir documento XML nodo a nodo                                  |
| **JSON → XML**                | Jackson (`ObjectMapper`, `XmlMapper`)                        | Convertir JSON a objeto → serializar con `XmlMapper.writeValueAsString()`       |
| **XML → JSON**                | Jackson (`XmlMapper`, `ObjectMapper`)                        | Leer XML como objeto → serializar como JSON                                     |
| **Texto plano → JSON/XML**    | `Files.readLines()` + kotlinx.serialization o manual         | Interpretar el texto → mapear a estructura → serializar                         |
| **JSON → binario estructurado**| kotlinx.serialization + `DataOutputStream`                  | Deserializar JSON → escribir datos con tipo fijo (enteros, strings, etc.)       |
| **Binario estructurado → JSON**| `DataInputStream` o `ByteBuffer` + kotlinx.serialization    | Leer bytes → construir objetos → serializar a JSON                              |
| **Texto → binario**           | `OutputStream`, `DataOutputStream`                          | Codificar texto (ej. UTF-8) o campos → guardar en binario                       |
| **Binario → texto legible**   | `InputStream`, `ByteBuffer`, interpretación personalizada     | Leer bytes → convertir a texto interpretando la estructura                      |
| **PNG → JPG (imagen)**        | `ImageIO.read()` + `ImageIO.write(..., "jpg", archivo)`     | Leer imagen → guardar con otro formato                                          |
| **Imagen → binario base64**   | `ImageIO.read()` + `Base64.getEncoder().encodeToString()`    | Convertir imagen a bytes → codificarlos como texto                              |
| **Imagen → texto (OCR)**      | Tesseract OCR + librería externa (`Tess4J`)                  | Procesar imagen → extraer texto con reconocimiento óptico de caracteres         |

## 🔹 Ejemplos de Conversión

!!!warning "Ejemplos"
    Los siguientes ejemplos tamién se incluirán en el proyecto **Ficheros_Gradle**.

En los siguientes ejemplos trabajaremos con tres archivos en diferentes formatos: **CSV**, **XML** y **JSON**. Estos archivos contienen información similar, representada con distinta estructura y sintaxis según el formato. Los utilizaremos como base para realizar ejercicios de conversión entre formatos.

Contenido de los archivos:


| **alumnos.csv** | **persona.xml** | **persona.json** |
|-----------------|-----------------|------------------|
| nombre;nota<br>Lucía;9<br>Carlos;8<br>Elena;10 | &lt;Persona&gt;<br>&nbsp;&nbsp;&lt;nombre&gt;Lucía&lt;/nombre&gt;<br>&nbsp;&nbsp;&lt;edad&gt;28&lt;/edad&gt;<br>&lt;/Persona&gt; | {<br>&nbsp;&nbsp;"nombre" : "Lucía",<br>&nbsp;&nbsp;"edad" : 28<br>} |


### **CSV a JSON**{.azul}

En estos ejemplos utilizamos **Jackson**, pero se podría  utilizar también **Kotlinx.serialization**.

🖥️ **Ejemplo_convertir_csv_a_json.kt**

        import com.opencsv.CSVReaderBuilder
        import com.opencsv.CSVParserBuilder
        import com.fasterxml.jackson.databind.ObjectMapper
        import com.fasterxml.jackson.module.kotlin.KotlinModule
        import java.io.File
        import java.io.FileReader

        data class Alumno(val nombre: String, val nota: Int)

        fun main() {
            val rutaCSV = "documentos/alumnos.csv"
            val rutaJSON = "documentos/alumnos.json"

            val reader = CSVReaderBuilder(FileReader(rutaCSV))
                .withCSVParser(CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1)
                .build()

            val registros = reader.readAll()
            val alumnos = mutableListOf<Alumno>()

            for (campos in registros) {
                val nombre = campos[0]
                val nota = campos[1].toInt()
                val alumno = Alumno(nombre, nota)
                alumnos.add(alumno)
            }

            reader.close()

            val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
            mapper.writerWithDefaultPrettyPrinter().writeValue(File(rutaJSON), alumnos)

            println("✅ Conversión CSV → JSON completada: $rutaJSON")
        }
<!--
🖥️ **Ejemplo_convertir_csv_a_json_kotlinx.kt** utilizando Kotlinx.serialization

        import com.opencsv.CSVParserBuilder
        import com.opencsv.CSVReaderBuilder
        import kotlinx.serialization.*
        import kotlinx.serialization.json.*
        import java.io.File
        import java.io.FileReader
        import java.nio.file.Files
        import java.nio.file.Paths

        @Serializable
        data class Alumno(val nombre: String, val nota: Int)

        fun main() {
            val rutaCSV = "documentos/alumnos.csv"
            val rutaJSON = "documentos/alumnos.json"

            val reader = CSVReaderBuilder(FileReader(rutaCSV))
                .withCSVParser(CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1) // saltar cabecera
                .build()

            val alumnos = mutableListOf<Alumno>()

            var fila = reader.readNext()
            while (fila != null) {
                if (fila.size >= 2) {
                    val nombre = fila[0]
                    val nota = fila[1].toIntOrNull() ?: -1
                    alumnos.add(Alumno(nombre, nota))
                }
                fila = reader.readNext()
            }

            reader.close()

            // Serializar a JSON
            val jsonString = Json { prettyPrint = true }.encodeToString(alumnos)

            // Guardar el JSON
            Files.createDirectories(Paths.get(rutaJSON).parent)
            File(rutaJSON).writeText(jsonString)

            println("✅ Conversión CSV → JSON completada: $rutaJSON")
            println("📄 Contenido generado:\n$jsonString")
        }

-->

### **JSON a CSV**{.azul}

🖥️ **Ejemplo_convertir_json_a_csv.kt**        

        import com.fasterxml.jackson.databind.ObjectMapper
        import com.fasterxml.jackson.module.kotlin.KotlinModule
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.opencsv.CSVWriterBuilder
        import com.opencsv.ICSVWriter
        import java.io.File
        import java.io.FileWriter

        data class Alumno(val nombre: String, val nota: Int)

        fun main() {
            val rutaJSON = "documentos/alumnos.json"
            val rutaCSV = "documentos/alumnos_convertido.csv"

            val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
            val alumnos: List<Alumno> = mapper.readValue(File(rutaJSON))

            val writer = CSVWriterBuilder(FileWriter(rutaCSV))
                .withSeparator(';')
                .build()

            // Escribir cabecera
            writer.writeNext(arrayOf("nombre", "nota"))

            // Escribir cada alumno (sin usar lambda)
            for (alumno in alumnos) {
                val fila = arrayOf(alumno.nombre, alumno.nota.toString())
                writer.writeNext(fila)
            }

            writer.close()

            println("✅ Conversión JSON → CSV completada: $rutaCSV")
        }



### **JSON a XML**{.azul}

En estos ejemplos utilizamos **Jackson**.


🖥️ Ejemplo_convertir_json_a_xml.kt

        import com.fasterxml.jackson.databind.ObjectMapper
        import com.fasterxml.jackson.dataformat.xml.XmlMapper
        import com.fasterxml.jackson.module.kotlin.KotlinModule
        import java.io.File

        fun convertirJsonAXml(jsonPath: String, xmlPath: String) {
            val jsonMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
            val xmlMapper = XmlMapper().registerModule(KotlinModule.Builder().build())

            val persona = jsonMapper.readValue(File(jsonPath), Persona::class.java)
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(File(xmlPath), persona)

            println("Conversión JSON → XML completada")
        }


        fun main() {
            convertirJsonAXml("documentos/persona.json", "documentos/persona_generada.xml")

        }

### **XML a JSON**{.azul}

🖥️ **Ejemplo_convertir_xml_a_json.kt**



    import com.fasterxml.jackson.databind.ObjectMapper
    import com.fasterxml.jackson.dataformat.xml.XmlMapper
    import com.fasterxml.jackson.module.kotlin.KotlinModule
    import java.io.File

    fun convertirXmlAJson(xmlPath: String, jsonPath: String) {
        val xmlMapper = XmlMapper().registerModule(KotlinModule.Builder().build())
        val jsonMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

        val persona = xmlMapper.readValue(File(xmlPath), Persona::class.java)
        jsonMapper.writerWithDefaultPrettyPrinter().writeValue(File(jsonPath), persona)

        println("Conversión XML → JSON completada")
    }


    fun main() {
        
        convertirXmlAJson("documentos/persona.xml", "documentos/persona_generada.json")
    }


### **JSON a Binario estructurado**{.azul}

En estos ejemplos utilizamos **kotlinx.serialization**.

🖥️ **Ejemplo_convertir_json_a_binario.kt**

        import kotlinx.serialization.decodeFromString
        import kotlinx.serialization.json.Json
        import java.io.DataOutputStream
        import java.io.File
        import java.io.FileOutputStream
        import java.nio.file.Files
        import java.nio.file.Paths

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

            println("✅ Persona guardada como binario estructurado en: $rutaBin")
        }

    
       
Leer el binario estructurado:

        import java.io.DataInputStream
        import java.io.FileInputStream

        fun leerBinario(ruta: String) {
            val entrada = DataInputStream(FileInputStream(ruta))
            val nombre = entrada.readUTF()
            val edad = entrada.readInt()
            entrada.close()

            println("📄 Persona leída del binario:")
            println("Nombre: $nombre, Edad: $edad")
        }



### **Binario estructurado a JSON**{.azul}

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



      

