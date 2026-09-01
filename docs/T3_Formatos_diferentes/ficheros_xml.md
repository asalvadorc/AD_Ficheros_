# 🗂️ Ficheros XML

!!! warning "Dónde guardar los ejemplos"
    Antes de continuar, realiza la [🛠️ Preparación del proyecto](ficheros_intercambio.md) (configuración de Gradle y modelado de datos), común para los ejemplos de CSV, JSON y XML.

Un fichero **XML** (eXtensible Markup Language) es un formato de texto estructurado diseñado para almacenar y transportar datos de forma legible tanto para humanos como para máquinas.

Tiene una estructura jerárquica basada en etiquetas, similar al HTML, pero orientada al contenido de datos, no a la presentación. **XML** permite guardar objetos o estructuras de datos en un archivo de texto legible.

**Estructura**{.azul}
```xml
   <?xml version="1.0" encoding="UTF-8"?>
    <raiz>                             <!-- Elemento raíz obligatorio -->
        <elemento>
            <subelemento>valor</subelemento>
        </elemento>
        <otroElemento atributo="valor" />
    </raiz>
```

Para trabajar con XML en **Kotlin** podemos utilizar una API de árbol como **JDOM2**, que permite crear, leer y modificar su estructura, o una librería de mapeo como **Jackson XML**, que convierte directamente entre XML y objetos Kotlin.

## Resumen de ejemplos (XML) {.azul}

- [JDOM2: objeto a XML](#xml-objeto-a-xml)
- [JDOM2: XML a objeto](#xml-a-objeto)
- [Jackson XML: serialización/deserialización](#xml-jackson)

| Librería | Forma de trabajo | Nivel de control | Recomendado para |
|----------|------------------|------------------|-----------------|
| JDOM2 | Manipulación de elementos, atributos y documentos | Alto | Comprender XML o modificar su estructura de manera precisa |
| Jackson XML | Conversión directa entre XML y objetos Kotlin | Medio | Leer y escribir modelos de datos con menos código |

## Librería: JDOM2

**JDOM2** es una librería ligera y fácil de usar para trabajar con **XML** de forma manual y controlada, ideal cuando no necesitas solo convertir directamente a objetos, sino manipular el contenido de manera estructurada.


Clase|	¿Para qué sirve?
-----|-------------------
SAXBuilder|	Analiza (parsea) un archivo XML y devuelve un Document.
Document|	Representa todo el documento XML.
Element|	Representa una etiqueta (nodo) del XML.
Attribute|	Representa un atributo dentro de una etiqueta.
XMLOutputter|	Convierte el árbol de elementos en texto XML.


??? info "Métodos comunes de JDOM2 para manipular XML"

    | Método                             | Clase            | Descripción                                                                 |
    |------------------------------------|------------------|-----------------------------------------------------------------------------|
    | `Element(String name)`             | `Element`        | Crea un nuevo elemento XML con el nombre especificado                      |
    | `addContent(Element child)`        | `Element`        | Añade un elemento hijo al elemento actual                                  |
    | `addContent(String text)`          | `Element`        | Añade texto al contenido del elemento                                      |
    | `setText(String text)`             | `Element`        | Establece el texto del elemento                                            |
    | `getText()`                        | `Element`        | Obtiene el texto del elemento                                              |
    | `getChild(String name)`            | `Element`        | Obtiene el primer hijo con ese nombre                                      |
    | `getChildren(String name)`         | `Element`        | Obtiene todos los hijos con ese nombre                                     |
    | `getChildren()`                    | `Element`        | Obtiene todos los hijos del elemento                                       |
    | `setAttribute(String, String)`     | `Element`        | Establece un atributo del elemento                                         |
    | `getAttributeValue(String)`        | `Element`        | Obtiene el valor de un atributo                                            |
    | `Document(Element root)`           | `Document`       | Crea un documento XML con el elemento raíz dado                            |
    | `getRootElement()`                 | `Document`       | Obtiene el elemento raíz del documento                                     |
    | `SAXBuilder().build(File)`         | `SAXBuilder`     | Carga un documento XML desde un archivo                                    |
    | `XMLOutputter().output(Document, OutputStream)` | `XMLOutputter` | Escribe el documento XML en una salida (archivo, consola, etc.)            |
    | `setFormat(Format.prettyFormat())` | `XMLOutputter`   | Establece un formato bonito con sangrías                                   |

<!--
**Ejemplo de lectura y escritura de un archivo xml con JDOM2**{.azul}:

- Dependencias en **build.gradle.kts**

        dependencies {
            implementation("org.jdom:jdom2:2.0.6")
        }

- Guarda el siguiente fichero **alumnos.xml** en la carpeta **documentos**:


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


🖥️ **Ejemplo_XML_Dom.kt**: Lectura de alumnos.xml


        import org.jdom2.input.SAXBuilder
        import org.jdom2.Document
        import org.jdom2.Element
        import org.jdom2.output.Format
        import org.jdom2.output.XMLOutputter
        import java.io.File

        fun lecturaXML_Dom() {
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


        fun escrituraXML_Dom() {
            // Crear elementos
            val raiz = Element("alumnos.xml")

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
            salida.output(documento, File("documentos/alumnos_nuevo.xml").outputStream())

            println("Archivo XML creado con éxito.")
        }

        fun main(){
            lecturaXML_Dom()
            escrituraXML_Dom()
        }

-->


<!-- **Ejemplo que convierte el archivo alumnos.xml en un objeto y viceversa**{.azul}:-->


JDOM2 no realiza serialización automática de objetos Kotlin, se necesita mapear manualmente entre objetos (data class) y elementos XML. A continuación lo veremos con un ejemplo:

**Ejemplo de lectura y escritura de un archivo xml con JDOM2**{.azul}:

Vamos a generar el siguiente archivo XML utilizando dos ejemplos, uno se encargará de crear el documento XML, y otro de leer su contenido desde el fichero generado.

```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <alumnos>
        <alumno>
            <nombre>Lucía</nombre>
            <nota>8</nota>
        </alumno>
        <alumno>
            <nombre>Carlos</nombre>
            <nota>6</nota>
        </alumno>
        <alumno>
            <nombre>Elena</nombre>
            <nota>10</nota>
        </alumno>
        </alumnos>
```        

- Dependencias en **build.gradle.kts**

```kotlin
    dependencies {
        implementation("org.jdom:jdom2:2.0.6")
    }
```

- Reutilizamos la **clase Alumno** creada en los ejemplos anteriores:
```kotlin
        data class Alumno(val nombre: String, val nota: Int)
```

<a id="xml-objeto-a-xml"></a>

**🖥️ Ejemplo_Objeto_a_XML.kt**

Leemos el objeto `Alumno` y lo convertimos en el fichero XML `alumnos.xml`.


Este ejemplo muestra cómo convertir una colección de objetos `Alumno` en un documento XML usando JDOM2, construyendo manualmente la estructura del fichero.


La finalidad del ejercicio es entender cómo pasar de objetos Kotlin a un fichero XML creando manualmente las etiquetas y la jerarquía del documento.

```kotlin
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
        Alumno("Elena", 10)
    )

    // Crear elemento raíz <alumnos>
    val raiz = Element("alumnos") // (1)!

    // Añadir cada alumno como <alumno>
    for (alumno in alumnos) {
        val alumnoElement = Element("alumno") // (2)!
        alumnoElement.addContent(Element("nombre").setText(alumno.nombre)) // (3)!
        alumnoElement.addContent(Element("nota").setText(alumno.nota.toString())) // (4)!
        raiz.addContent(alumnoElement) // (5)!
    }

    // Crear el documento XML
    val documento = Document(raiz) // (6)!

    // Escribir en archivo con formato bonito
    val salida = XMLOutputter() // (7)!
    salida.format = Format.getPrettyFormat() // (8)!
    salida.output(documento, File("documentos/alumnos.xml").outputStream()) // (9)!

    println("Archivo XML creado con éxito.")
}
```

1. Crea el elemento raiz del documento XML.
2. Crea cada nodo `<alumno>` para un objeto de la lista.
3. Inserta el nombre como nodo hijo.
4. Inserta la nota como nodo hijo.
5. Añade el alumno al elemento raiz.
6. Construye el documento XML completo.
7. Prepara el escritor XML.
8. Activa el formato legible con sangrias.
9. Escribe el documento en el fichero XML.

<a id="xml-a-objeto"></a>

**🖥️ Ejemplo_XML_a_Objeto.kt**

Leemos el archivo `alumnos.xml` y lo convertimos en un objeto.


Este ejemplo realiza el proceso inverso al anterior: leer un fichero XML existente y transformar su contenido en objetos Kotlin de tipo `Alumno`.

La finalidad del ejercicio es entender cómo leer la estructura de un documento XML y mapear manualmente sus elementos a objetos del programa.

```kotlin
import org.jdom2.input.SAXBuilder
import java.io.File


fun main() {

    //Crea una lista mutable de tipo Alumno.
    val alumnos = mutableListOf<Alumno>() 

    val archivo = File("documentos/alumnos.xml") // (1)!
    val builder = SAXBuilder() // (2)!
    val documento = builder.build(archivo) // (3)!
    val raiz = documento.rootElement // (4)!

    val listaAlumnos = raiz.getChildren("alumno") // (5)!

    //Por cada nodo <alumno> del XML, crea un objeto Alumno con sus atributos.
    for (elemento in listaAlumnos) {
        val nombre = elemento.getChildText("nombre") // (6)!
        val nota = elemento.getChildText("nota").toIntOrNull() ?: 0 // (7)!
        alumnos.add(Alumno(nombre, nota))  // (8)!
    }

    // Mostrar los objetos
    alumnos.forEach { println(it) }
}
```

1. Localiza el archivo XML de entrada.
2. Crea el analizador `SAXBuilder`.
3. Lee el archivo XML y genera un documento en memoria.
4. Obtiene el elemento raiz del documento.
5. Recupera todos los nodos `<alumno>`.
6. Lee el texto del nodo `<nombre>`.
7. Convierte la nota a entero de forma segura.
8. Construye el objeto `Alumno` a partir del nodo leido.


## Librería: Jackson XML

JDOM2 no realiza serialización automática de objetos Kotlin, pero se puede recurrir a librerías como **Jackson** o **kotlinx.serialization**.

A diferencia de la librería **kotlinx.serialization-josn**, para ficheros **JSON**, que es es la librería oficial de serialización de Kotlin, la librería **kotlinx.serialization-xml**, para ficheros **XML**, no es oficial (aún experimental) y está mantenidad por terceros, por lo que no es una buena elección.

Utilizaremos, por tanto, la librería **Jackson** para realizar la serialización automática de objetos Kotlin, la cual también permite soporte completo para XML y JSON. Es decir, puede serializar y deserializar ambos formatos usando las mismas clases.


??? info "Métodos comunes de Jackson para XML (XmlMapper)"

    | Método                                       | Clase        | Descripción                                                                 |
    |----------------------------------------------|--------------|-----------------------------------------------------------------------------|
    | `readValue(File, Class<T>)`                  | `XmlMapper`  | Lee un archivo XML y lo convierte en un objeto Kotlin/Java                 |
    | `readValue(String, Class<T>)`                | `XmlMapper`  | Lee un String XML y lo convierte en un objeto                              |
    | `writeValue(File, Object)`                   | `XmlMapper`  | Escribe un objeto como XML en un archivo                                   |
    | `writeValueAsString(Object)`                 | `XmlMapper`  | Convierte un objeto en una cadena XML                                      |
    | `writeValueAsBytes(Object)`                  | `XmlMapper`  | Convierte un objeto en un array de bytes XML                               |
    | `registerModule(Module)`                     | `ObjectMapper` / `XmlMapper` | Registra un módulo como `KotlinModule` o `JavaTimeModule`        |
    | `enable(SerializationFeature)`               | `XmlMapper`  | Activa una opción de serialización (por ejemplo, indentado)                |
    | `disable(DeserializationFeature)`            | `XmlMapper`  | Desactiva una opción de deserialización                                    |
    | `configure(MapperFeature, boolean)`          | `XmlMapper`  | Configura opciones generales del mapeo                                     |
    | `setDefaultPrettyPrinter(...)`               | `XmlMapper`  | Establece un formateador personalizado                                     |


**Ejemplo de lectura y escritura del fichero alumnos.xml  con Jackson XML**{.azul}


- Dependencias en **build.gradle.kts**:

```kotlin
        dependencies {
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
            implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.0")
        }
```

Siguiendo con el ejemplo **alumnos.xml**:

```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <alumnos>
        <alumno>
            <nombre>Lucía</nombre>
            <nota>8</nota>
        </alumno>
        <alumno>
            <nombre>Carlos</nombre>
            <nota>6</nota>
        </alumno>
        <alumno>
            <nombre>Elena</nombre>
            <nota>10</nota>
        </alumno>
        </alumnos>
```        

## Clase contenedora

En XML siempre hay un único elemento raíz. Jackson necesita una clase que represente ese nodo raíz.
En nuestro ejemplo ese elemento raíz es: `<alumnos>`

Creamos la clase contenedora **ListaAlumnos**, que actúa como puente entre el XML y Kotlin. Esta clase da nombre al nodo raiz `<alumnos>` y explica como mapear los elementos repetidos `<alumno>`.

    data class ListaAlumnos(
                @JacksonXmlElementWrapper(useWrapping = false) // No añade un <alumnoList>, usa directamente <alumno>
                @JacksonXmlProperty(localName = "alumno") // Cada elemento se llama <alumno>
                val alumno: List<Alumno> = emptyList()
            )

<a id="xml-jackson"></a>

**🖥️ Ejemplo_XML_Jackson.kt**


Este ejemplo muestra cómo usar Jackson XML para serializar y deserializar automáticamente una colección de objetos Kotlin, sin tener que construir o recorrer manualmente el árbol XML como ocurría con JDOM2.

La finalidad del ejercicio es comprobar cómo Jackson permite convertir entre objetos Kotlin y XML de forma automática, siempre que exista una clase que represente correctamente la estructura del documento.

```kotlin
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

fun escribirXml() {
    val xmlMapper = XmlMapper().registerModule(KotlinModule.Builder().build()) // (1)!

    val lista = ListaAlumnos( // (2)!
        listOf(
            Alumno("Lucía", 8),
            Alumno("Carlos", 6),
            Alumno("María", 10)
        )
    )

    val archivo = File("documentos/alumnos.xml") // (3)!
    xmlMapper.writerWithDefaultPrettyPrinter().writeValue(archivo, lista) // (4)!

    println("XML escrito correctamente en: ${archivo.absolutePath}")
}


fun leerXml() {
    val xmlMapper = XmlMapper().registerKotlinModule() // (5)!

    val archivo = File("documentos/alumnos.xml") // (6)!

    //val lista = xmlMapper.readValue(archivo, ListaAlumnos::class.java)
    val lista=xmlMapper.readValue<ListaAlumnos>(archivo) // (7)!

    println("Lectura correcta:")

    lista.alumno.forEach { // (8)!
        println("${it.nombre} tiene un ${it.nota}")
    }
}

fun main() {   
    escribirXml()
    leerXml()
}
```

1. Crea el `XmlMapper` para serializar y deserializar XML.
2. Construye el contenedor `ListaAlumnos` con la lista de objetos.
3. Define el archivo XML de salida.
4. Escribe el XML con formato legible.
5. Crea de nuevo el `XmlMapper` para leer el archivo.
6. Abre el archivo XML de entrada.
7. Reconstruye el contenedor `ListaAlumnos` desde el XML.
8. Recorre la lista recuperada para usar los objetos leidos.

       



!!!question "🧠 Comprueba tu comprensión"
    1. Si en un proyecto Kotlin puro necesitas leer un CSV con cabecera y recorrer filas de forma expresiva, ¿qué librería elegirías entre OpenCSV y Kotlin-CSV?
    2. Si trabajas en un backend con Spring y necesitas JSON y XML en el mismo proyecto, ¿qué librería suele ser la opción más práctica?
    3. ¿Por qué en este tema se insiste en transformar el contenido del fichero a objetos (`data class`) antes de convertir entre formatos?

    ??? success "Ver respuestas"
        1. **Kotlin-CSV**, porque está diseñada para Kotlin y ofrece un estilo más idiomático (`readAllWithHeader`, DSL, secuencias).
        2. **Jackson**, porque soporta ambos formatos de forma unificada y se integra muy bien en ecosistemas Java/Spring.
        3. Porque el objeto actúa como representación intermedia tipada y desacopla el formato de entrada del formato de salida.

!!!question "🧠 Caso práctico rápido"
    Tienes un `alumnos.csv` y necesitas generar `alumnos.json` sin perder estructura.
    ¿Cuál es el flujo correcto de trabajo?

    ??? success "Ver respuesta"
        El flujo recomendado es:

        1. Leer el CSV.
        2. Convertir cada fila en objetos `Alumno`.
        3. Serializar esa lista de objetos a JSON.

        Es decir: **CSV → objetos Kotlin → JSON**.

!!!question "🧠 Errores frecuentes"
    ¿Qué problema puede aparecer si intentas convertir directamente texto entre formatos (por ejemplo, CSV a JSON) sin mapear primero a objetos?

    ??? success "Ver respuesta"
        Suelen aparecer errores de interpretación de tipos y estructura:

        - Números tratados como texto.
        - Campos ausentes o desordenados.
        - Dificultad para validar datos.

        Mapear a `data class` primero evita estos problemas y hace el código más mantenible.
