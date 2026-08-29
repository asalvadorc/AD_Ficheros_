---
hide:
  - toc
---

# Introducción

En el desarrollo de software es habitual utilizar formatos estándar para almacenar e intercambiar datos entre aplicaciones, sistemas o lenguajes de programación. Entre los más utilizados se encuentran **JSON**, **XML** y **CSV**.

Estos formatos se emplean principalmente para:

La comunicación entre aplicaciones, por ejemplo mediante APIs REST.
La importación y exportación de datos entre sistemas.
El almacenamiento estructurado de información.

A diferencia de los ficheros de texto plano o binarios, JSON, XML y CSV tienen una estructura interna definida, por lo que su contenido se procesa mediante librerías específicas que permiten analizar o parsear los datos.

Cada formato está orientado a diferentes necesidades:

**CSV**: sencillo y eficiente para datos tabulares.  
**JSON**: ligero y adecuado para datos estructurados y servicios web.  
**XML**: más estructurado y extensible, útil en sistemas complejos y cuando se requiere validación.  

Antes de trabajar con estos formatos es importante comprender la **serialización**, proceso que permite transformar un objeto en memoria en una representación que pueda almacenarse o transmitirse, y la deserialización, que realiza el proceso inverso.

Este concepto se desarrolla en el siguiente apartado.


## 🛠️ Ubicación de los ejemplos

!!!warning ""
    <span class="setup-tag setup-tag-ide">SETUP_IDE</span> <span class="setup-tag setup-tag-paquetes">SETUP_PAQUETES</span> Los ejemplos del **Tema 3** se reparten entre dos proyectos: 

    - **Sin librerías externas** (ej. ObjectOutputStream): usa el **proyecto** `Ficheros`, **paquete** `formatos`.
    - **Con dependencias externas** (ej. JSON, CSV, XML): usa el **proyecto** `Ficheros_Gradle`.   
    
    Si tienes dudas sobre la preparación del entorno o la ubicación de los ejemplos, consulta [🧰 Entorno y Ubicación de los Ejemplos](../00_entorno_y_proyecto.md), disponible en el menú `Inicio`.




