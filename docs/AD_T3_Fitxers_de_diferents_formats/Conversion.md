# 🔹 Conversión entre formatos de ficheros en Kotlin/Java

| Conversión                     | Herramientas recomendadas                                   | Proceso resumido                                                                 |
|-------------------------------|--------------------------------------------------------------|----------------------------------------------------------------------------------|
| **CSV → JSON**                | OpenCSV + kotlinx.serialization                              | Leer CSV → mapear a objetos → serializar con `Json.encodeToString`              |
| **JSON → CSV**                | kotlinx.serialization + OpenCSV                             | Deserializar JSON a objetos → escribir filas CSV                                |
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


alumnos.csv

    nombre;nota
    Lucía;9
    Carlos;8
    Elena;10

persona.xml

    <Persona>
        <nombre>Lucía</nombre>
        <edad>28</edad>
    </Persona>


perosna.json

    {
    "nombre" : "Lucía",
    "edad" : 28
    }


**CSV a JSON**{.azul}

**Ejemplo_convertir_csv_a_json.kt**

        import com.opencsv.CSVReaderBuilder
        import com.opencsv.CSVParserBuilder
        import com.fasterxml.jackson.databind.ObjectMapper
        import com.fasterxml.jackson.module.kotlin.KotlinModule
        import java.io.File
        import java.io.FileReader



        fun main() {
            val rutaCSV = "documentos/alumnos.csv"
            val rutaJSON = "documentos/alumnos.json"

            val reader = CSVReaderBuilder(FileReader(rutaCSV))
                .withCSVParser(CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1)
                .build()

            val alumnos = reader.readAll().map { campos ->
                Alumno(
                    nombre = campos[0],
                    nota = campos[1].toInt()
                )
            }

            reader.close()

            val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
            mapper.writerWithDefaultPrettyPrinter().writeValue(File(rutaJSON), alumnos)

            println("✅ Conversión CSV → JSON completada: $rutaJSON")
        }

**JSON a CSV**{.azul}

**Ejemplo_convertir_json_a_csv.kt**        

        import com.fasterxml.jackson.databind.ObjectMapper
        import com.fasterxml.jackson.module.kotlin.KotlinModule
        import com.fasterxml.jackson.module.kotlin.readValue
        import com.opencsv.CSVWriter
        import java.io.File
        import java.io.FileWriter


        fun main() {
            val rutaJson = "documentos/alumnos.json"
            val rutaCsv = "documentos/alumnos_convertido.csv"

            // 1. Leer JSON
            val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
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




**JSON a XML**{.azul}

**Ejemplo_convertir_json_a_xml.kt**

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

**XML a JSON**{.azul}

**Ejemplo_convertir_xml_a_json.kt**



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




**Hoja de ejercicios para el aula**{.azul}
   
🧪 Ejercicios prácticos por tipo de conversión
📝 Nivel básico
Leer un archivo CSV con nombre y edad, y convertirlo a JSON usando kotlinx.serialization.

Leer un archivo de texto plano línea a línea y transformarlo en una lista de objetos Persona(nombre, edad), luego convertirlo a JSON.

🧱 Nivel intermedio
Convertir un archivo JSON a CSV generando un nuevo archivo con formato delimitado por ;.

Leer un archivo de imagen PNG y guardarlo como JPG usando ImageIO.

Invertir los colores de una imagen .png y guardar el resultado como .png.

🧩 Nivel avanzado
Leer un archivo binario estructurado que almacene nombres y edades (string + int), y convertirlo a JSON.

Escribir un archivo binario estructurado a partir de un JSON con personas, usando DataOutputStream.

🧠 Extra (OCR)
Utilizar OCR (Tesseract) para extraer texto de una imagen escaneada y guardar el resultado en un archivo .txt.

