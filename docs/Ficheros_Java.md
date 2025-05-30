# Ficheros y Java (Kotlin)
Un **fichero** es una unidad de almacenamiento de datos en un sistema informático. Se trata de un conjunto de información organizada y almacenada en un dispositivo de almacenamiento (secuencia de bytes), como un disco duro, una memoria USB o un servidor en la nube.
A los datos que se guardan en ficheros se llaman datos persistentes, porque persisten más allá de la ejecución de la aplicación que los trata.

**Características de un fichero**{.azul}

- **Nombre**: Cada fichero tiene un nombre único dentro de su directorio.
- **Extensión**: Muchos ficheros tienen una extensión que indica su tipo (por ejemplo, .txt para texto, .jpg para imágenes, .pdf para documentos, .dat para binarios...).
- **Ubicación**: Se encuentran organizados en carpetas o directorios dentro del sistema de archivos.
- **Contenido**: Puede ser texto, imágenes, vídeos, código fuente, bases de datos, etc.
- **Permisos de acceso**: Se pueden configurar para permitir o restringir la lectura, escritura o ejecución a determinados usuarios o programas.

**Tipos de ficheros**{.azul}  

- **Ficheros de texto**: Contienen datos en formato legible por humanos (.txt, .csv, .json, .xml).
- **Ficheros binarios**: Almacenan información en un formato no legible directamente (.exe, .jpg, .mp3, .dat).
- **Ficheros de código fuente**: Contienen instrucciones escritas en lenguajes de programación (.java, .kt, .py).
- **Ficheros de configuración**: Almacenan parámetros de configuración de programas (.ini, .conf, .properties).
- **Ficheros de bases de datos**: Se utilizan para almacenar grandes volúmenes de datos estructurados (.db, .sql).

**Principales usos de la persistencia de datos en ficheros**{.azul}

El uso de ficheros para la persistencia de datos es una alternativa sencilla y eficiente cuando no se requiere una base de datos completa:

- Guardar ajustes de una aplicación en archivos de configuración (.properties, .ini, .json).
- Mantener un historial de eventos o errores en un sistema (.log).
- Guardar información de usuario o estado de la aplicación sin necesidad de una base de datos (.csv, .json).
- Comunicación entre programas mediante archivos JSON o XML.
- Aplicaciones que funcionan sin internet o sin una base de datos centralizada.


!!!Note "Kotlin"
    **Kotlin** es un lenguaje de programación moderno, conciso y seguro, desarrollado por JetBrains, la misma empresa que creó IntelliJ IDEA. Está diseñado para interoperar totalmente con Java.  Ambos lenguajes comparten las mismas APIs para la gestión del sistema de archivos, principalmente las de **java.io** y **java.nio.file**, sin embargo, Kotlin ofrece algunas diferencias clave en estilo, sintaxis y características de lenguaje que mejoran la experiencia, por lo que, **en este curso, utilizaremos la sintaxis de Kotlin**. 

**Librerías java.io y java.nio**{.azul}

A las operaciones que constituyen un flujo de información del programa con el exterior, se les conoce como Entrada/Salida (E/S).

En Kotlin (y Java), la gestión de ficheros se apoya en los paquetes estándar de la plataforma Java. Kotlin no tiene su propia API de ficheros independiente, sino que utiliza las siguientes librerías estándar de Java: **java.io** y **java.nio**.


✅ **java.io (E/S clásica)**

        Características:

        - API tradicional desde Java 1.0.
        - Clases basadas en streams (secuenciales).
        - Funciona bien para archivos pequeños y lectura/escritura básica.

        Clases:

        - File, FileInputStream, FileOutputStream
        - BufferedReader, BufferedWriter
        - ObjectInputStream, ObjectOutputStream

!!!Note ""
    Aún se usa, sobre todo en ejemplos básicos, pero ha sido superada por alternativas más modernas.

✅ **java.nio (NIO.2)**

        Características:

        - Introducida en Java 7 como parte de NIO.2 (New I/O).
        - Permite trabajar con rutas (Path), lectura y escritura moderna, y acceso a atributos de ficheros.
        - Es la forma recomendada actualmente para trabajar con archivos.

        Clases:

        - Path, Paths
        - Files (con métodos estáticos como readAllLines, write, newBufferedReader, etc.)
        - DirectoryStream, FileVisitor, WatchService (para recorrer o monitorizar el sistema de archivos)


!!!Note "Importante"
    ✔ En este curso utilizaremos la librería **java.nio**, ya que es la recomendada para la mayoría de los usos modernos de E/S de archivos.
