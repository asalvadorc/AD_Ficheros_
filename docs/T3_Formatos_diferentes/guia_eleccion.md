# 🧭 Guía de elección: ¿qué librería usar?

Cuando empezamos a trabajar con ficheros en Kotlin, una de las primeras dudas es:  
**¿qué librería o clase tengo que usar?**

Esta guía responde a esa pregunta en dos pasos: primero según el **tipo de fichero**, y después según el **contexto del proyecto**.

---

## 📂 Paso 1: ¿Qué tipo de fichero tienes?

**Elige el tipo de fichero que necesitas gestionar:**{.azul}

| Tipo de fichero | ¿Qué librería usar? | Paquete / Dependencia Gradle |
|---|---|---|
| 📄 **Texto plano** (`.txt`) | API estándar `Files` de Java NIO | _Incluida en el JDK. Sin dependencias externas._ |
| 📊 **CSV** (`.csv`) | `OpenCSV` o `Kotlin-CSV` | `com.opencsv:opencsv:5.9` / `com.github.doyaaaaaken:kotlin-csv-jvm:1.9.1` |
| 🔵 **JSON** (`.json`) | `kotlinx.serialization` o `Jackson` | `org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3` / `com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0` |
| 🌐 **XML** (`.xml`) | `JDOM2` o `Jackson` (con módulo XML) | `org.jdom:jdom2:2.0.6` / `com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.0` |
| 🖼️ **Imagen** (`.jpg`, `.png`) | `ImageIO` de Java | _Incluida en el JDK. Sin dependencias externas._ |
| 🧱 **Binario no estructurado** (`.bin`, bytes puros) | API estándar `Files` de Java NIO | _Incluida en el JDK. Sin dependencias externas._ |
| ⚙️ **Binario estructurado** | `FileChannel` + `ByteBuffer` de Java NIO | _Incluida en el JDK. Sin dependencias externas._ |
| 📦 **Objeto serializado** (`.ser`) | `ObjectOutputStream` / `ObjectInputStream` | _Incluida en el JDK. Sin dependencias externas._ |


!!!tip "Consejo"
    Si el fichero **no tiene estructura interna definida** (texto línea a línea, bytes puros, imágenes), la respuesta siempre es usar la **API estándar de Java NIO** (`java.nio.file.Files`). No necesitas ninguna dependencia externa.

---

## 🔍 Paso 2: Tengo CSV, JSON o XML. ¿Cuál de las librerías del formato elijo?

### 📊 Para CSV: ¿OpenCSV o Kotlin-CSV?

| Criterio | OpenCSV | Kotlin-CSV |
|---|---|---|
| **Lenguaje base** | Java (usa `java.io.*`) | Kotlin (DSL idiomático) |
| **Facilidad en Kotlin** | ⭐⭐⭐ (funciona, algo verboso) | ⭐⭐⭐⭐⭐ (natural y conciso) |
| **Leer con cabecera** | Manual (saltar línea 0) | `readAllWithHeader()` directo |
| **Estándar CSV (RFC 4180)** | ✅ Sí (comillas, escape...) | ✅ Sí |
| **Madurez y documentación** | Alta (lleva muchos años) | Media-Alta |
| **Recomendado para...** | Proyectos mixtos Java+Kotlin | Proyectos 100% Kotlin |

!!!note "En resumen (CSV)"
    - ¿Proyecto nuevo en Kotlin puro? → **Kotlin-CSV**
    - ¿Necesitas compatibilidad con código Java existente? → **OpenCSV**

---

### 🔵 Para JSON: ¿kotlinx.serialization o Jackson?

| Criterio | `kotlinx.serialization` | Jackson |
|---|---|---|
| **Creado por** | JetBrains (oficial Kotlin) | FasterXML (ecosistema Java) |
| **Peso de la librería** | 🟢 Ligera | 🟡 Más pesada |
| **Facilidad de uso** | ⭐⭐⭐⭐⭐ (anotación `@Serializable`) | ⭐⭐⭐⭐ (requiere `ObjectMapper`) |
| **Multiplataforma** | ✅ Sí (JVM, JS, Native) | ❌ Solo JVM |
| **Soporte XML** | ❌ No (experimental) | ✅ Sí (con módulo extra) |
| **Soporte CSV, YAML** | ❌ No | ✅ Sí (con módulos extra) |
| **Integración con Spring Boot** | 🟡 Posible, pero requiere config. | ✅ Integración automática |
| **Recomendado para...** | Proyectos Kotlin puros / Multiplatform | Backend Java/Spring o múltiples formatos |

!!!note "En resumen (JSON)"
    - ¿Proyecto Kotlin puro o multiplataforma? → **kotlinx.serialization**
    - ¿Proyecto con Spring Boot o necesitas también XML/YAML? → **Jackson**

---

### 🌐 Para XML: ¿JDOM2 o Jackson?

| Criterio | JDOM2 | Jackson (módulo XML) |
|---|---|---|
| **Estilo de acceso** | Bajo nivel: nodos, atributos manualmente | Alto nivel: mapeo directo a data class |
| **Control sobre el XML** | ⭐⭐⭐⭐⭐ Total | ⭐⭐⭐ Depende de anotaciones |
| **Cantidad de código** | 🔴 Más código, más verboso | 🟢 Menos código |
| **Curva de aprendizaje** | 🟡 Media (hay que conocer la API DOM) | 🟢 Baja si ya sabes Jackson JSON |
| **Ideal cuando...** | Necesitas leer/modificar partes concretas del XML | Tienes clases Kotlin y quieres persistirlas en XML |

!!!note "En resumen (XML)"
    - ¿Necesitas control total sobre la estructura XML? → **JDOM2**
    - ¿Tienes clases Kotlin y solo quieres guardar/leer objetos? → **Jackson con módulo XML**

---

## 🗺️ Resumen visual: árbol de decisión

```
¿Qué tipo de fichero necesitas manejar?
│
├── Texto plano (.txt)         → java.nio.file.Files (sin dependencias)
│
├── Imagen (.jpg, .png...)     → javax.imageio.ImageIO (sin dependencias)
│
├── Binario no estructurado (.bin) → Files.readAllBytes / Files.write (sin dependencias)
│
├── Binario estructurado / acceso aleatorio → FileChannel + ByteBuffer (sin dependencias)
│
├── Objeto serializado (.ser)  → ObjectOutputStream / ObjectInputStream
│
├── CSV (.csv)
│   ├── Proyecto Kotlin puro   → Kotlin-CSV
│   └── Compatibilidad Java    → OpenCSV
│
├── JSON (.json)
│   ├── Kotlin puro / multiplatform → kotlinx.serialization
│   └── Spring Boot / múltiples formatos → Jackson
│
└── XML (.xml)
    ├── Control total sobre nodos → JDOM2
    └── Mapeo de objetos Kotlin → Jackson + jackson-dataformat-xml
```

---

## ⚡ Tabla resumen rápida

| Necesidad concreta | Librería | ¿Dependencia Gradle necesaria? |
|---|---|---|
| Leer/escribir texto línea a línea | `Files` (NIO) | ❌ No |
| Leer/escribir binario no estructurado (bytes puros) | `Files.readAllBytes` / `Files.write` | ❌ No |
| Leer/escribir bytes | `Files` (NIO) | ❌ No |
| Binario estructurado (campos tipados) | `FileChannel` + `ByteBuffer` | ❌ No |
| Acceso aleatorio en fichero binario | `FileChannel` | ❌ No |
| Leer/escribir imagen | `ImageIO` | ❌ No |
| Serializar un objeto Java | `ObjectOutputStream` | ❌ No |
| CSV en proyecto Kotlin moderno | `kotlin-csv` | ✅ Sí |
| CSV con compatibilidad Java | `OpenCSV` | ✅ Sí |
| JSON en proyecto Kotlin puro | `kotlinx.serialization` | ✅ Sí |
| JSON en Spring Boot / backend Java | `Jackson` | ✅ Sí |
| XML con control manual de nodos | `JDOM2` | ✅ Sí |
| XML mapeado desde clases Kotlin | `Jackson` + módulo XML | ✅ Sí |
| JSON **y** XML en el mismo proyecto | `Jackson` | ✅ Sí (un solo `ObjectMapper`) |

---

!!!warning "Recuerda siempre"
    Antes de añadir una dependencia externa, pregúntate:  
    **¿La API estándar de Java es suficiente para lo que necesito?**  
    Si la respuesta es sí, úsala. Menos dependencias = proyecto más simple y portable.
