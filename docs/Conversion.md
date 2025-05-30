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


## Hoja de ejercicios para el aula
   
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

