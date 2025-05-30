# 📋 Métodos recomendados según tipo de archivo en Kotlin/Java

| Tipo de archivo        | Métodos/Librerías recomendadas                                             | Observaciones clave                                                                 |
|------------------------|------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| 📝 Texto plano         | `Files.readAllLines()`, `Files.readString()`, `Files.writeString()`        | Ideal para archivos pequeños. Usa `BufferedReader` para lectura línea a línea.     |
| 🧱 Binario (crudo)      | `FileInputStream`, `FileOutputStream`, `Files.readAllBytes()`              | Lectura/escritura de datos binarios sin estructura (imágenes, audio, etc.).        |
| 🧩 Binario estructurado | `FileChannel`, `ByteBuffer`, `RandomAccessFile`                            | Permite acceso aleatorio, eficiente para ficheros con estructura fija (e.g. registros). |
| 🖼 Imagen               | `ImageIO.read()`, `BufferedImage`, `ImageIO.write()`                        | Permite modificar píxeles, crear gráficos, exportar en formatos como PNG, JPG.     |
| 📑 CSV                 | OpenCSV (`CSVReader`, `CSVWriter`)                                          | Maneja delimitadores, comillas, cabeceras. Requiere dependencia externa.           |
| 🧾 JSON                | `kotlinx.serialization.json.Json`                                           | Muy integrado con Kotlin. Usa `@Serializable`. Requiere plugin y dependencia.      |
| 🗂 XML                 | `DocumentBuilderFactory`, `TransformerFactory` (Java estándar)              | Verboso. Se puede usar JAXB o `Jackson XML` como alternativas modernas.            |
