# Programación Orientada a Componentes (POC)

La programación orientada a componentes (POC) es un paradigma que se centra en el diseño y construcción de aplicaciones mediante componentes reutilizables e independientes, que encapsulan tanto datos como lógica de negocio y se comunican entre sí a través de interfaces bien definidas.

Se trata de clases o módulos que encapsulan la lectura, escritura, conversión o gestión de datos en distintos formatos como CSV, JSON o XML.

En el contexto de la gesión de ficheros, un componente puede ser una clase con una única responsabilidad clara y una interfaz definida. Por ejemplo:

- LectorCSV: componente que sabe leer archivos .csv
- EscritorJSON: componente que convierte objetos a JSON y los guarda
- RepositorioAlumnos: componente que encapsula acceso a datos almacenados en un fichero, sea cual sea el formato

Ejemplo 1: Componente RepositorioAlumnosCSV

Encapsula acceso a datos de alumnos en un archivo CSV


        data class Alumno(val nombre: String, val nota: Int)

        class RepositorioAlumnosCSV(private val fichero: String) {

            fun cargar(): List<Alumno> {
                return File(fichero).readLines().mapNotNull { linea ->
                    val partes = linea.split(";")
                    if (partes.size == 2) Alumno(partes[0], partes[1].toInt()) else null
                }
            }

            fun guardar(alumnos: List<Alumno>) {
                File(fichero).printWriter().use { writer ->
                    alumnos.forEach { writer.println("${it.nombre};${it.nota}") }
                }
            }
        }

**Componente reutilizable para cualquier lista de alumnos CSV.**

Ejemplo 2: Componente GestorJSON

Leer y escribir objetos Kotlin desde/hacia ficheros .json con kotlinx.serialization

        import kotlinx.serialization.*
        import kotlinx.serialization.json.*
        import java.io.File

        @Serializable
        data class Producto(val nombre: String, val precio: Double)

        class GestorJSON<T>(private val serializer: KSerializer<T>) {

            fun leer(path: String): T {
                val contenido = File(path).readText()
                return Json.decodeFromString(serializer, contenido)
            }

            fun guardar(objeto: T, path: String) {
                val json = Json.encodeToString(serializer, objeto)
                File(path).writeText(json)
            }
        }


    val gestor = GestorJSON(Producto.serializer())
    gestor.guardar(Producto("Teclado", 29.99), "producto.json")


 Ejemplo 3: Componente ConversorCSVaJSON

 Transformar ficheros CSV a JSON (ideal como utilitario o servicio)


    import kotlinx.serialization.*
    import kotlinx.serialization.json.*
    import java.io.File

    @Serializable
    data class Alumno(val nombre: String, val nota: Int)

        class ConversorCSVaJSON {

            fun convertir(csvPath: String, jsonPath: String) {
                val alumnos = File(csvPath).readLines().mapNotNull {
                    val partes = it.split(";")
                    if (partes.size == 2) Alumno(partes[0], partes[1].toInt()) else null
                }
                val json = Json.encodeToString(alumnos)
                File(jsonPath).writeText(json)
            }
        }

Ejemplo 4: Componente de persistencia XML

Guardar una lista de objetos en un archivo XML (usando JDOM2)

        import org.jdom2.*
        import org.jdom2.output.XMLOutputter
        import org.jdom2.output.Format
        import java.io.File

        data class Alumno(val nombre: String, val nota: Int)

        class EscritorXML {

            fun guardar(alumnos: List<Alumno>, path: String) {
                val raiz = Element("alumnos")
                alumnos.forEach {
                    val alumnoElem = Element("alumno")
                    alumnoElem.addContent(Element("nombre").setText(it.nombre))
                    alumnoElem.addContent(Element("nota").setText(it.nota.toString()))
                    raiz.addContent(alumnoElem)
                }

                val doc = Document(raiz)
                XMLOutputter().apply {
                    format = Format.getPrettyFormat()
                    output(doc, File(path).outputStream())
                }
            }
        }



Supongamos una aplicación que:

- Lee alumnos desde un CSV
- Les aplica una transformación (por ejemplo, sumar +1 a todas las notas)
- Guarda los resultados en JSON


        fun main() {
            val repoCSV = RepositorioAlumnosCSV("alumnos.csv")
            val alumnos = repoCSV.cargar().map { it.copy(nota = it.nota + 1) }

            val gestorJSON = GestorJSON(ListSerializer(Alumno.serializer()))
            gestorJSON.guardar(alumnos, "alumnos.json")
        }
