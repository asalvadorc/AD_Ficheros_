
# Ficheros de intercambio

En el desarrollo de aplicaciones, es habitual trabajar con datos almacenados o transmitidos en distintos formatos de ficheros. La conversión entre estos formatos permite intercambiar información entre sistemas heterogéneos, integrarse con APIs, facilitar la persistencia de datos o adaptarse a requisitos específicos. 

**¿Cuándo utilizar cada uno de ellos?**

- **JSON** y **XML**: para APIs REST, configuración, exportación de datos.
- **CSV**: para importar/exportar datos desde hojas de cálculo.

Estos formatos no solo permiten guardar la información de forma más organizada y legible, sino que requieren utilizar **librerías específicas** para leer y escribir.  


En los siguientes apartados veremos cómo trabajar con cada uno de estos formatos, qué librerías se utilizan en Kotlin para manipularlos, y cómo adaptar nuestras clases y funciones para poder persistir y recuperar objetos fácilmente desde cada tipo de fichero.

1. **CSV**: empieza con lectura y escritura manual para entender los registros y delimitadores; después compara `OpenCSV` con `kotlin-csv`.
2. **JSON**: trabaja primero con `kotlinx.serialization`, la opción natural en Kotlin, y después con `Jackson`, habitual en proyectos Java y backend.
3. **XML**: utiliza `JDOM2` para comprender y manipular la estructura del documento, y termina con `Jackson XML` para convertir objetos directamente.

<div class="bloque-preparacion" markdown>

## 🛠️ Preparación del proyecto

Los ejemplos de CSV, JSON y XML comparten la configuración Gradle y el mismo modelo de datos. Esta preparación se realiza una sola vez y se reutiliza durante todo el tema.

!!! warning "Dónde guardar los ejemplos"
    <span class="setup-tag setup-tag-ide">SETUP_IDE</span> <span class="setup-tag setup-tag-carpetas">SETUP_CARPETAS</span>

    Todos los ejemplos de esta página utilizan **dependencias externas**, por lo que deben realizarse en el proyecto **`Ficheros_Gradle`**:

    - Guarda los archivos Kotlin (`.kt`) dentro de `src/main/kotlin`, en la raíz o en un paquete que elijas y mantengas durante todo el tema.
    - Guarda los archivos de datos (`.csv`, `.json` y `.xml`) en la carpeta `documentos`, situada en la raíz del proyecto.
    - Mantén el archivo `build.gradle.kts` en la raíz de `Ficheros_Gradle`.

    Si necesitas revisar la estructura completa, consulta [🧰 Entorno y ubicación de los ejemplos](../00_entorno_y_proyecto.md).

**Configuración del proyecto con Gradle**{.azul}

En este apartado vamos a desarrollar una aplicación en Kotlin que gestione la lectura y escritura de datos utilizando distintos formatos de archivo estructurado: CSV, JSON y XML.

Para facilitar **el uso de librerías externas** que nos ayuden a trabajar con estos formatos, vamos a utilizar **Gradle** como herramienta de construcción del proyecto. Gradle nos permitirá:

- Gestionar las dependencias necesarias.
- Automatizar el proceso de compilación y ejecución.
- Organizar el proyecto de forma profesional y escalable.

**Dependencias que utilizaremos**:

| Formato | Librería             | Propósito principal                                                              |
|---------|----------------------|----------------------------------------------------------------------------------|
| CSV     | OpenCSV, Kotlin-CSV  | Lectura y escritura de archivos separados por comas o punto y coma              |
| JSON    | kotlinx.serialization| Conversión entre objetos Kotlin y texto JSON (ligero, multiplataforma, oficial) |
| JSON    | Jackson              | Conversión entre objetos Java/Kotlin y JSON (muy usado en backend Java)         |
| XML     | JDOM2                | Construcción y manipulación explícita de documentos XML                         |
| XML     | Jackson XML          | Conversión directa entre objetos y XML mediante anotaciones                     |


En el fichero **build.gradle.kts** se incluirán los plugins y dependencias necesarias:

```kotlin

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

    //Kotlin-CSV
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.1")


    // librería JDOM2
    implementation("org.jdom:jdom2:2.0.6")

    // librerias jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.0")

}

```

**Modelado de datos con `data class`**{.azul}

Aunque este tipo de ficheros están formados por texto, los programas no deberían trabajar directamente con texto, sino con datos estructurados.

Por este motivo, toda la lectura y escritura de ficheros de intercambio se realizará **mediante objetos**, y no manipulando directamente cadenas de texto.
Cuando leemos un fichero CSV, JSON o XML, leemos texto, pero el objetivo final es obtener información con significado. Para representar correctamente esa información dentro del programa, utilizaremos **data class**, que nos permiten modelar los datos de forma clara y segura.

Define qué información tiene un objeto y de qué tipo es cada dato. El data class de una línea CSV se representaría así:

| CSV | Data Class |
|--------|---------|
| nombre;nota<br>Lucía;28 | data class Alumno(<br>&nbsp;&nbsp;&nbsp;val nombre: String,<br>&nbsp;&nbsp;&nbsp;val nota: Int<br>) |

</div>

---



