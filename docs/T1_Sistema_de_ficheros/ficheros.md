---
hide:
  - toc
---

# 🔹 Introducción

Un **fichero** es una unidad de almacenamiento de datos en un sistema informático. Se trata de un conjunto de información organizada y almacenada en un dispositivo de almacenamiento (secuencia de bytes), como un disco duro, una memoria USB o un servidor en la nube.
A los datos que se guardan en ficheros se llaman datos persistentes, porque persisten más allá de la ejecución de la aplicación que los trata.

=== "Características"

    - **Nombre**: Cada fichero tiene un nombre único dentro de su directorio.
    - **Extensión**: Indica habitualmente su tipo (`.txt`, `.jpg`, `.pdf`, `.dat`...).
    - **Ubicación**: Se organizan en carpetas o directorios dentro del sistema de archivos.
    - **Contenido**: Puede ser texto, imágenes, vídeos, código fuente, datos, etc.
    - **Permisos de acceso**: Permiten controlar la lectura, escritura y ejecución.

=== "Tipos de ficheros"

    - **Texto**: `.txt`, `.csv`, `.json`, `.xml`.
    - **Binarios**: `.exe`, `.jpg`, `.mp3`, `.dat`.
    - **Código fuente**: `.java`, `.kt`, `.py`.
    - **Configuración**: `.ini`, `.conf`, `.properties`.
    - **Bases de datos**: `.db`, `.sql`.

=== "Usos en persistencia"

    El uso de ficheros para la persistencia de datos es una alternativa sencilla y eficiente cuando no se requiere una base de datos completa:
  
    - Guardar **ajustes de una aplicación**.
    - Mantener **registros de eventos o errores**.
    - Almacenar **información de usuario o del estado de la aplicación**.
    - Facilitar el **intercambio de datos entre programas**.
    - Permitir el funcionamiento **sin conexión o sin una base de datos centralizada**.


## 🛠️ Ubicación de los ejemplos

!!!warning ""
    <span class="setup-tag setup-tag-ide">SETUP_IDE</span> <span class="setup-tag setup-tag-paquetes">SETUP_PAQUETES</span> Todos los ejemplos de código del **Tema 1** se deben programar en el **proyecto** `Ficheros`, dentro del **paquete** `sistema`. 
       
    Si tienes dudas sobre la preparación del entorno o la ubicación de los ejemplos, consulta [🧰 Entorno y Ubicación de los Ejemplos](../00_entorno_y_proyecto.md), disponible en el menú `Inicio`.