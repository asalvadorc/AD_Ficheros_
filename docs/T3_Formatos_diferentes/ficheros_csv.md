# 📄 Ficheros CSV

!!! warning "Dónde guardar los ejemplos"
    Antes de continuar, realiza la [🛠️ Preparación del proyecto](ficheros_intercambio.md) (configuración de Gradle y modelado de datos), común para los ejemplos de CSV, JSON y XML.

El formato CSV es un archivo de texto donde los valores están separados por comas u otro delimitador (como punto y coma), muy usado para intercambiar datos entre hojas de cálculo, sistemas contables, etc.

La lectura y escritura de un archivo CSV se puede hacer de tres formas:

| Enfoque | Dependencia | Nivel de automatización | Recomendado para |
|---------|-------------|-------------------------|-----------------|
| Sin librerías (`Files` + `split`) | Ninguna | Bajo | Comprender el formato y procesar casos muy sencillos |
| OpenCSV | `com.opencsv:opencsv` | Alto | Proyectos Java/Kotlin que necesitan una biblioteca consolidada |
| kotlin-csv | `com.github.doyaaaaaken:kotlin-csv-jvm` | Alto | Proyectos Kotlin que buscan una API más expresiva e idiomática |

## Resumen de ejemplos (CSV) {.azul}

- [Sin librerías: lectura línea a línea + split()](#csv-sin-librerias)
- [Con OpenCSV](#csv-opencsv)
- [Con Kotlin-CSV](#csv-kotlincsv)



En este bloque de contenidos vamos a trabajar con distintos programas de ejemplo y en todos estos casos, los datos representan siempre el mismo tipo de información: alumnos.


!!!Tip "Alumnos.csv"
    nombre:nota
    Lucía;9  
    Carlos;8  
    Elena;10  
    
    
La **data class Alumno** correspondiente será:

```kotlin
data class Alumno(
    val nombre: String,
    val nota: Int
)
```

📌 Esta clase la crearemos fuera de los programas de ejemplo para poder reutilizarla desde cualquier otro `main`.


## Enfoque: sin librerías

<a id="csv-sin-librerias"></a>

**🖥️ Ejemplo_CSV_lect_esc.kt**


Este ejemplo muestra la forma más básica de trabajar con un fichero CSV: escribirlo como texto y después leerlo línea a línea para convertir cada registro en un objeto `Alumno`.

La finalidad del ejercicio es entender cómo funciona internamente un CSV y cómo transformar su contenido textual en objetos Kotlin, incluso sin utilizar librerías externas.

```kotlin
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption


fun main() {
    val ruta = Paths.get("documentos/alumnos.csv")

    // 1. Crear contenido CSV (con cabecera)
    val lineas = listOf(
        "nombre;nota",     // cabecera
        "Lucía;9",
        "Carlos;8",
        "Elena;10"
    )

    // 2. Escribir el archivo
    Files.write( // (1)!
        ruta,
        lineas,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING
    )

    println("Archivo CSV creado: ${ruta.toAbsolutePath()}")

    // 3. Leer el archivo y convertir a objetos Alumno
    val lineasLeidas = Files.readAllLines(ruta) // (2)!
    val alumnos = mutableListOf<Alumno>()

    for (i in lineasLeidas.indices) {

        // Saltamos la cabecera (línea 0)
        if (i == 0) continue

        val linea = lineasLeidas[i]
        val partes = linea.split(";") // (5)!

        if (partes.size == 2) {
            val nombre = partes[0]
            val nota = partes[1].toInt() // (3)!

            val alumno = Alumno(nombre, nota) // (4)!
            alumnos.add(alumno)
        } else {
            println("Línea mal formada: $linea")
        }
    }

    // 4. Usar los objetos
    println("\nListado de alumnos:")
    for (alumno in alumnos) {
        println("Alumno: ${alumno.nombre}, Nota: ${alumno.nota}")
    }
}
```

1. Escribe la lista de lineas en el fichero CSV.
2. Lee el contenido completo del CSV como lineas de texto.
3. Convierte el valor textual de la nota a entero.
4. Construye el objeto `Alumno` a partir de los datos leidos.
5. Cada línea de datos se divide con split(";") para separar el nombre y la nota.


## Librería: OpenCSV

!!!Note "Nota"
    **OpenCSV** fue desarrollado antes de que **java.nio.file.Path** se introdujera en Java 7, y sus métodos aún usan la API antigua **(java.io.*)**, como FileReader y FileWriter.

??? info "Lectura con OpenCSV"

    | Clase / Método          | ¿Qué hace?                                                              | Ejemplo básico                                         |
    | ----------------------- | ----------------------------------------------------------------------- | ------------------------------------------------------ |
    | `CSVReader(FileReader)` | Crea un lector de archivos CSV.                                         | `val reader = CSVReader(FileReader("archivo.csv"))`    |
    | `readAll()`             | Lee todo el contenido como `List<Array<String>>`.                       | `val filas = reader.readAll()`                         |
    | `readNext()`            | Lee una fila como `Array<String>`.                                      | `val fila = reader.readNext()`                         |
    | `close()`               | Cierra el lector.                                                       | `reader.close()`                                       |
    | `CSVReaderBuilder(...)` | Permite configurar el lector: separador, comillas, salto de línea, etc. | `CSVReaderBuilder(FileReader(...)).withSeparator(';')` |
    | `withSkipLines(n)`      | Omite las primeras `n` líneas (útil para saltar cabeceras).             | `withSkipLines(1)`                                     |
    | `build()`               | Construye el lector configurado.                                        | `build()`                                              |

??? info "Escritura con OpenCSV"

    | Clase / Método                  | ¿Qué hace?                                                          | Ejemplo básico                                         |
    | ------------------------------- | ------------------------------------------------------------------- | ------------------------------------------------------ |
    | `CSVWriter(FileWriter)`         | Crea un escritor CSV básico.                                        | `val writer = CSVWriter(FileWriter("archivo.csv"))`    |
    | `writeNext(Array<String>)`      | Escribe una línea al CSV.                                           | `writer.writeNext(arrayOf("Ana", "30"))`               |
    | `writeAll(List<Array<String>>)` | Escribe múltiples filas al CSV.                                     | `writer.writeAll(listaFilas)`                          |
    | `flush()`                       | Fuerza la escritura del buffer.                                     | `writer.flush()`                                       |
    | `close()`                       | Cierra el escritor.                                                 | `writer.close()`                                       |
    | `CSVWriterBuilder(...)`         | Permite configurar el escritor: delimitador, comillas, escape, etc. | `CSVWriterBuilder(FileWriter(...)).withSeparator(';')` |
    | `withQuoteChar(c)`              | Define el carácter de comillas (por defecto es `"`).                | `withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)`          |
    | `withEscapeChar(c)`             | Define el carácter de escape (por defecto `\`).                     | `withEscapeChar('\\')`                                 |
    | `withLineEnd(e)`                | Define el carácter de fin de línea.                                 | `withLineEnd("\n")`                                    |
    | `build()`                       | Construye el escritor configurado.                                  | `build()`                                              |

---
<a id="csv-opencsv"></a>


**🖥️ Ejemplo_OpenCSV_lect_esc.kt**

Este ejemplo realiza la misma tarea que el anterior, pero utilizando la librería `OpenCSV`, que simplifica la lectura y escritura de archivos CSV.

La finalidad del ejercicio es comprobar cómo una librería especializada reduce el trabajo manual y hace más cómoda la manipulación de archivos CSV.



```kotlin
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos/alumnos.csv").toString()

    val writer = CSVWriter(FileWriter(ruta)) // (1)! 

    writer.writeNext(arrayOf("nombre", "nota")) // (2)! 
    writer.writeNext(arrayOf("Lucía", "9"))
    writer.writeNext(arrayOf("Carlos", "8"))
    writer.writeNext(arrayOf("Elena", "10"))
    writer.close()

    println("Archivo CSV creado: $ruta")

    val alumnos = mutableListOf<Alumno>()

    val reader = CSVReader(FileReader(ruta)) // (3)! 
    val filas = reader.readAll() // (4)! 

    for (i in filas.indices) {

        // Saltamos la cabecera
        if (i == 0) continue

        val fila = filas[i]

        if (fila.size == 2) {
            val nombre = fila[0]
            val nota = fila[1].toInt() // (5)! 

            alumnos.add(Alumno(nombre, nota)) // (6)! 
        } else {
            println("Línea mal formada: ${fila.joinToString(";")}")
        }
    }

    reader.close()

    println("\nListado de alumnos:")
    for (alumno in alumnos) {
        println("Alumno: ${alumno.nombre}, Nota: ${alumno.nota}")
    }
}
```

1. Crea el escritor CSV para generar el fichero.
2. Escribe la cabecera del archivo.
3. Crea el lector CSV para recuperar el fichero generado.
4. Lee todas las filas en memoria.
5. Convierte la cadena de la nota a entero.
6. Crea el objeto `Alumno` a partir de la fila leida.

!!!Note "Nota"
    El archivo CSV generado sin librerías es un archivo de texto plano con el separador **;**, pero sin comillas y sin escape. En cambio, el fichero CSV generado con OpenCSV sigue el estantar CSV (RFC 4180) que incluye encerrar los campos entre comillas dobles, si el campo contiene el separador (como **;** o **,**).

## Librería: kotlin-csv

!!!Note "Nota"
    la librería **kotlin-csv** también utiliza **java.io.File** para muchas de sus operaciones principales, aunque de una forma un poco más moderna y flexible que **OpenCSV**.


Tradicionalmente, en entornos Java se ha utilizado la librería OpenCSV para leer y escribir este tipo de archivos, debido a su potencia y versatilidad, sin embargo, cuando desarrollamos en Kotlin, existen alternativas más modernas y adaptadas al lenguaje. Una de ellas es kotlin-csv, una librería ligera y expresiva diseñada específicamente para aprovechar las ventajas de Kotlin, como las expresiones lambda, la sintaxis DSL y el trabajo con secuencias (sequences) y corrutinas.   


!!!Tip ""
    **csvWriter** y **csvReader** no son clases, sino funciones DSL propias de Kotlin

??? info "Métodos habituales de kotlin-csv"

    | Tipo        | Método                        | Ejemplo mínimo |
    |-------------|-------------------------------|----------------|
    | **Lectura** | `readAll(File)`               | `val filas = csvReader().readAll(File("alumnos.csv"))` |
    |             | `readAllWithHeader(File)`     | `val datos = csvReader().readAllWithHeader(File("alumnos.csv"))` |
    |             | `open { readAllAsSequence() }`| `csvReader().open("alumnos.csv") { readAllAsSequence().forEach { println(it) } }` |
    | **Escritura**| `writeAll(data, File)`       | `csvWriter().writeAll(listOf(listOf("Lucía", "9")), File("salida.csv"))` |
    |             | `writeRow(row, File)`         | `csvWriter().writeRow(listOf("Carlos", "8"), File("salida.csv"))` |
    |             | `writeAllWithHeader(data, File)` | `csvWriter().writeAllWithHeader(listOf(mapOf("nombre" to "Elena", "nota" to "10")), File("salida.csv"))` |
    | **Configuración** | `delimiter`, `quoteChar`, etc. | `csvReader { delimiter = ';' }` |


<a id="csv-kotlincsv"></a>

**🖥️ Ejemplo_KotlinCSV_lect_esc.kt**

Este ejemplo muestra cómo trabajar con archivos CSV usando la librería `kotlin-csv`, una alternativa más idiomática para Kotlin que permite leer y escribir de forma más expresiva.

La finalidad del ejercicio es comprobar cómo una librería orientada a Kotlin simplifica el trabajo con CSV y facilita la conversión entre texto estructurado y objetos del programa.

```kotlin
    import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
    import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
    import java.io.File
    import java.nio.file.Paths


    fun main() {
        val ruta = Paths.get("documentos/alumnos.csv").toString()

        // Escribir con cabecera
        csvWriter().open(ruta) { // (1)!
            writeRow(listOf("nombre", "nota"))  // (2)!
            writeRow(listOf("Lucía", "9"))
            writeRow(listOf("Carlos", "8"))
            writeRow(listOf("Elena", "10"))
        }

        // Leer con cabecera (como Map)
        val filas: List<Map<String, String>> = csvReader().readAllWithHeader(File(ruta)) // (3)!

        val alumnos = filas.mapNotNull { fila -> // (4)!
            val nombre = fila["nombre"]
            val notaStr = fila["nota"]
            if (nombre != null && notaStr != null) Alumno(nombre, notaStr.toInt()) else null // (5)!
        }

        println("Listado de alumnos:")
        alumnos.forEach { println("Alumno: ${it.nombre}, Nota: ${it.nota}") }
    }
```

1. Abre el escritor DSL de kotlin-csv para generar el fichero.
2. Escribe la cabecera como una fila.
3. Lee el CSV interpretando la cabecera.
4. Recorre las filas leidas para transformarlas.
5. Crea cada objeto `Alumno` cuando existen los campos necesarios.
