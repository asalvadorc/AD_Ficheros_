// ============================================================
//  EXAMEN — Acceso a Datos: Gestión de Ficheros en Kotlin
//  Completa las funciones marcadas con // TODO
//  No modifiques las firmas de las funciones.
// ============================================================

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

// --- Data class proporcionada (no modificar) ---

@Serializable
data class Producto(
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int
)

// ============================================================
// FUNCIÓN 1 (2 puntos)
// Lee el fichero CSV en la ruta indicada usando Kotlin-CSV.
// Ignora la primera fila (cabecera).
// Si el fichero no existe, captura la excepción y devuelve listOf().
// ============================================================
fun leerProductosCSV(rutaCsv: String): List<Producto> {
    // TODO: implementa la función aquí
    return listOf()
}

// ============================================================
// FUNCIÓN 2 (2 puntos)
// Guarda la lista de productos en un fichero JSON.
// Usa kotlinx.serialization con prettyPrint = true.
// Si el directorio de destino no existe, créalo.
// Captura las posibles excepciones de I/O.
// ============================================================
fun guardarComoJSON(productos: List<Producto>, rutaJson: String) {
    // TODO: implementa la función aquí
}

// ============================================================
// FUNCIÓN 3 (1 punto)
// Recibe la lista de productos y devuelve solo los que tienen stock == 0.
// Recorre la lista con un bucle for.
// Si stock == 0, añade el producto a una lista de resultados
// y lo imprime con el formato:
//   ⚠️ Sin stock: <nombre> → <stock> unidades
// Devuelve la lista de productos sin stock.
// ============================================================
fun filtrarSinStock(productos: List<Producto>): List<Producto> {
    // TODO: implementa la función aquí
    return listOf()
}

// ============================================================
// FUNCIÓN 4 (1 punto)
// Agrupa los productos por categoría usando un mutableMapOf<String, MutableList<Producto>>().
// Recorre la lista con un bucle for:
//   - Si la categoría ya existe en el mapa, añade el producto a su lista.
//   - Si no existe, crea una nueva lista con ese producto.
// Después recorre el mapa e imprime por cada categoría:
//   📦 <Categoría> → <N> productos | Precio medio: <X.XX> €
// El precio medio = suma de precios / número de productos.
// ============================================================
fun resumenPorCategoria(productos: List<Producto>) {
    // TODO: implementa la función aquí
}

// --- Main de prueba (no modificar) ---
fun main() {
    val rutaCSV  = "documentos/productos.csv"
    val rutaJSON = "documentos/productos.json"

    println("=== Leyendo CSV ===")
    val productos = leerProductosCSV(rutaCSV)
    println("Productos leídos: ${productos.size}")

    println("\n=== Guardando JSON ===")
    guardarComoJSON(productos, rutaJSON)

    println("\n=== Productos sin stock ===")
    val sinStock = filtrarSinStock(productos)
    println("Total sin stock: ${sinStock.size}")

    println("\n=== Resumen por categoría ===")
    resumenPorCategoria(productos)
}
