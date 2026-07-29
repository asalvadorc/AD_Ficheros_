# 📋 Chuleta de examen — Acceso a Datos: Ficheros

!!!warning "Uso autorizado"
    Este documento puede consultarse durante el examen. **Solo contiene firmas de métodos.** Saber qué método usar y cómo aplicarlo sigue siendo responsabilidad del alumno.

---

## 1 · Kotlin-CSV

```kotlin
// Importaciones necesarias
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

// Lectura — devuelve List<Map<String, String>>  (clave = nombre de columna)
csvReader().readAllWithHeader(File("ruta.csv"))

// Lectura con separador personalizado
csvReader { delimiter = ';' }.readAllWithHeader(File("ruta.csv"))

// Lectura sin cabecera — devuelve List<List<String>>
csvReader().readAll(File("ruta.csv"))

// Escritura fila a fila
csvWriter().open("ruta.csv") {
    writeRow(listOf("col1", "col2"))   // cabecera
    writeRow(listOf("valor1", "valor2"))
}

// Acceder a un valor de una fila leída con cabecera
val valor: String? = fila["nombreColumna"]
```

---

## 2 · kotlinx.serialization (JSON)

```kotlin
// Importaciones necesarias
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// Anotar la clase (obligatorio)
@Serializable
data class MiClase(val campo: String, val numero: Int)

// Objeto → String JSON
val jsonString: String = Json.encodeToString(objeto)

// Lista → String JSON
val jsonString: String = Json.encodeToString(lista)

// JSON con formato legible (prettyPrint)
val jsonString: String = Json { prettyPrint = true }.encodeToString(objeto)

// String JSON → Objeto
val objeto: MiClase = Json.decodeFromString<MiClase>(jsonString)

// String JSON → Lista
val lista: List<MiClase> = Json.decodeFromString<List<MiClase>>(jsonString)
```

---

## 3 · java.nio.file.Files (API estándar)

```kotlin
// Importaciones necesarias
import java.nio.file.Files
import java.nio.file.Paths

val ruta = Paths.get("carpeta/fichero.txt")

// Crear directorio (y padres) si no existe — usar SIEMPRE antes de escribir
Files.createDirectories(ruta.parent)

// Escribir texto completo
Files.writeString(ruta, "contenido")

// Leer texto completo
val contenido: String = Files.readString(ruta)

// Leer línea a línea (ficheros grandes)
Files.newBufferedReader(ruta).use { reader ->
    reader.lineSequence().forEach { linea -> println(linea) }
}

// Comprobar si un fichero existe
Files.exists(ruta)   // → Boolean
```

---

## 4 · Estructuras básicas de Kotlin (recordatorio)

```kotlin
// Lista mutable
val lista = mutableListOf<Tipo>()
lista.add(elemento)

// Mapa mutable
val mapa = mutableMapOf<String, MutableList<Tipo>>()
if (mapa.containsKey(clave)) {
    mapa[clave]!!.add(elemento)
} else {
    mapa[clave] = mutableListOf(elemento)
}

// Recorrer un mapa
for ((clave, valor) in mapa) { ... }

// Gestión de excepciones
try {
    // operación de fichero
} catch (e: IOException) {
    println("Error de E/S: ${e.message}")
} catch (e: Exception) {
    println("Error inesperado: ${e.message}")
}
```
