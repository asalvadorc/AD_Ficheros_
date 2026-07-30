# 🔹 Gestión segura de recursos

La gestión de errores en operaciones de entrada/salida es clave para evitar fallos en tiempo de ejecución y mejorar la robustez de las aplicaciones.

- **`try-catch`** permite capturar errores previsibles (archivo no encontrado, permisos, formato inválido, etc.).
- **`use`** garantiza el cierre automático de recursos (streams, readers, writers), incluso si ocurre una excepción.

**Recomendaciones didácticas**{.azul}

- Capturar excepciones específicas siempre que sea posible (por ejemplo, `IOException`).
- Evitar `catch (Exception)` como única estrategia; usarlo solo como respaldo.
- Mostrar mensajes de error claros para facilitar depuración y mantenimiento.
- Validar rutas y existencia de ficheros antes de operar cuando sea necesario.

**Ejemplo mínimo en Kotlin**

		import java.io.IOException
		import java.nio.file.Files
		import java.nio.file.Paths

		fun main() {
			val ruta = Paths.get("documentos/datos.txt")

			try {
				Files.newBufferedReader(ruta).use { reader ->
					reader.forEachLine { println(it) }
				}
			} catch (e: IOException) {
				println("Error de entrada/salida: ${e.message}")
			}
		}

En este ejemplo, `use` cierra automáticamente el `BufferedReader`, y `try-catch` controla posibles errores de lectura.  
