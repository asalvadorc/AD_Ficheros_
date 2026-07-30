# 📘 Guía resumen: Lectura y escritura de distintos formatos de ficheros en Kotlin

## Tipos de ficheros y métodos de acceso

| Tipo de fichero         | Lectura                             | Escritura                           | Comentario                                      |
|-------------------------|--------------------------------------|--------------------------------------|-------------------------------------------------|
| **Texto (líneas)**      | `Files.readAllLines`                | `Files.write(Path, List<String>)`    | Carga todo en memoria                          |
|                         | `Files.newBufferedReader`           | `Files.newBufferedWriter`            | Más eficiente para archivos grandes            |
|                         | `Files.readString` (Java 11+)       | `Files.writeString`                  | Lectura/escritura completa como bloque         |
| **Binario**             | `Files.readAllBytes`                | `Files.write(Path, ByteArray)`       | Lee y escribe bytes puros                      |
|                         | `Files.newInputStream`              | `Files.newOutputStream`              | Flujo de bytes directo                         |
| **Binario estructurado**| `FileChannel.read(ByteBuffer)`      | `FileChannel.write(ByteBuffer)`      | Acceso secuencial o aleatorio con `ByteBuffer` |
|                         | `SeekableByteChannel.read(...)`     | `SeekableByteChannel.write(...)`     | Más flexible (se puede posicionar)             |
|                         | `ByteBuffer.get*()`                 | `ByteBuffer.put*()`                  | Tipos primitivos                               |
| **Acceso aleatorio**    | `FileChannel.position(offset)`      | `FileChannel.position(offset)`       | Permite saltar a posiciones concretas          |
| **Imagen**              | `ImageIO.read(Path/File)`           | `ImageIO.write(BufferedImage, ...)`  | Usa `javax.imageio.ImageIO`                    |

---

## Serialización de objetos

| Herramienta               | Uso principal                         | Notas clave                                     |
|---------------------------|----------------------------------------|-------------------------------------------------|
| `Serializable` (Java)     | Marca una clase como serializable      | Necesario para `ObjectOutputStream`            |
| `ObjectOutputStream`      | Serializa y guarda un objeto           | Solo con clases `Serializable`                 |
| `ObjectInputStream`       | Lee un objeto serializado              | Funciona solo con objetos serializados en Java |
| `transient`               | Evita serializar ciertos atributos     | Útil para datos temporales o sensibles         |
| `writeObject/readObject`  | Métodos opcionales para personalizar   | Control sobre la serialización binaria         |

---


## Conversión de formatos

| Formato | Librería                   | Uso principal                                           |
|---------|----------------------------|----------------------------------------------------------|
| CSV     | OpenCSV, Kotlin-CSV        | Lectura y escritura de archivos CSV                     |
| JSON    | `kotlinx.serialization`    | Ligero, multiplataforma, integrado con Kotlin           |
| JSON    | Jackson                    | Muy usado en backend Java y con Spring Boot             |
| XML     | DOM API (`javax.xml`)      | Bajo nivel, control manual de nodos y estructura        |
| XML     | Jackson                    | Conversión directa entre objetos y XML con anotaciones  |

---

## Métodos de `OpenCSV` (CSV)

**Lectura con OpenCSV**{.verde}

| Clase / Método          | ¿Qué hace?                                                              | Ejemplo básico                                         |
| ----------------------- | ----------------------------------------------------------------------- | ------------------------------------------------------ |
| `CSVReader(FileReader)` | Crea un lector de archivos CSV.                                         | `val reader = CSVReader(FileReader("archivo.csv"))`    |
| `readAll()`             | Lee todo el contenido como `List<Array<String>>`.                       | `val filas = reader.readAll()`                         |
| `readNext()`            | Lee una fila como `Array<String>`.                                      | `val fila = reader.readNext()`                         |
| `close()`               | Cierra el lector.                                                       | `reader.close()`                                       |
| `CSVReaderBuilder(...)` | Permite configurar el lector: separador, comillas, salto de línea, etc. | `CSVReaderBuilder(FileReader(...)).withSeparator(';')` |
| `withSkipLines(n)`      | Omite las primeras `n` líneas (útil para saltar cabeceras).             | `withSkipLines(1)`                                     |
| `build()`               | Construye el lector configurado.                                        | `build()`                                              |

**Escritura con OpenCSV**{.verde}


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

## Métodos de `kotlinx.serialization`

| Método                               | ¿Qué hace?                                      | Ejemplo básico                                    |
|--------------------------------------|--------------------------------------------------|--------------------------------------------------|
| `Json.encodeToString(obj)`           | Objeto → JSON como `String`                     | `Json.encodeToString(persona)`                  |
| `Json.decodeFromString<T>(json)`     | JSON como `String` → Objeto Kotlin              | `Json.decodeFromString<Persona>(json)`          |
| `Json.encodeToJsonElement(obj)`      | Objeto → `JsonElement` (estructura de árbol)    | `val elem = Json.encodeToJsonElement(persona)`  |
| `Json.decodeFromJsonElement(elem)`   | `JsonElement` → Objeto Kotlin                   | `val persona = Json.decodeFromJsonElement<Persona>(elem)` |
| `Json.parseToJsonElement(string)`    | Cadena JSON → árbol `JsonElement` sin mapear    | `val elem = Json.parseToJsonElement(json)`      |

---

## Métodos de Jackson

| Método                         | ¿Qué hace?                                      | Ejemplo                                          |
|--------------------------------|--------------------------------------------------|--------------------------------------------------|
| `readValue(String, Class)`     | JSON → objeto                                   | `mapper.readValue(json, Persona::class.java)`   |
| `readValue(File, Class)`       | Archivo JSON → objeto                           | `mapper.readValue(File("persona.json"), ...)`   |
| `readTree(String)`             | JSON → `JsonNode` (sin mapeo a clase)           | `val node = mapper.readTree(json)`              |
| `writeValue(File, Object)`     | Objeto → archivo JSON                           | `mapper.writeValue(File("salida.json"), persona)` |
| `writeValueAsString(Object)`   | Objeto → cadena JSON                            | `val json = mapper.writeValueAsString(persona)` |
| `writeValueAsBytes(Object)`    | Objeto → bytes JSON                             | `val bytes = mapper.writeValueAsBytes(persona)` |
| `writerWithDefaultPrettyPrinter()` | Formatea JSON de salida                      | `.writerWithDefaultPrettyPrinter().writeValue(...)` |

---

## Clases útiles para XML con JDOM

| Clase         | Función                                           |
|---------------|--------------------------------------------------|
| `SAXBuilder`  | Lee y convierte un archivo XML en `Document`     |
| `Document`    | Representa todo el documento XML                 |
| `Element`     | Representa una etiqueta o nodo XML               |
| `Attribute`   | Atributo de un `Element`                         |
| `XMLOutputter`| Convierte el documento XML a texto               |

---

## Recomendaciones según situación

| Situación                           | Mejor opción             | Motivo principal                             |
|------------------------------------|---------------------------|----------------------------------------------|
| Solo necesitas JSON                | `kotlinx.serialization`  | Ligero, rápido, oficial para Kotlin          |
| Necesitas JSON + XML (complejo)    | Jackson                   | Más completo y flexible para ambos formatos  |
| Proyecto en Java o Spring Boot     | Jackson                   | Integración fluida                           |
| Proyecto Multiplataforma Kotlin    | `kotlinx.serialization`  | Compatible con JS, JVM y Native              |
