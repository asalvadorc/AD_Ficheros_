# 📝 Ejercicio 1: Explorador interactivo del directorio personal


## 📋 Enunciado

!!!info "Proyecto Integrador"
    Este ejercicio es la **Parte 1** de un proyecto que irás construyendo a lo largo de todo el módulo. Escribe código limpio y ordenado, ¡porque en los siguientes temas añadirás más funcionalidades a este mismo programa!

Desarrollar un programa en **Kotlin** que permita **explorar y manipular el contenido del directorio personal del usuario (home)** utilizando la API de `java.nio.file`.

El programa debe:

1. Obtener la ruta del directorio personal del usuario mediante:

        val homePath = Paths.get(System.getProperty("user.home"))

2. Mostrar los **primeros 5 elementos (archivos o carpetas)** visibles dentro del home (excluyendo ocultos), mostrando:

   * Nombre
   * Tipo (archivo o directorio)
   * Tamaño en bytes
   * Fecha de creación y última modificación
   * Permisos: legible y escribible

3. Mostrar información sobre el **sistema de archivos**:

   * Tipo del sistema
   * Espacio total y libre en bytes

4. Presentar un **menú interactivo** con las siguientes opciones:

!!!Tip "📋 Menú de acciones:"   
         1- Crear un directorio en el home
         2- Eliminar un directorio del home
         3- Ver contenido recursivo de un directorio
         0- Salir
      

---

## 🛠️ Requisitos técnicos

* El contenido del `home` debe actualizarse y mostrarse tras cada operación del menú.
* El programa debe ignorar todos los archivos o carpetas cuyo nombre comience por `.`
* La opción 3 debe explorar el directorio de forma recursiva usando `Files.walk()`
* Se debe utilizar `try/catch` para capturar errores como directorios inexistentes, problemas de permisos, etc.
* La salida debe ser clara, bien estructurada y comprensible.

---

📤 **Ejemplo de salida esperada**

     
      Exploración del home (sin ocultos): C:\Users\as_co

      Nombre: aliciatxt
      Tipo: Archivo
      Tamaño: 34 bytes
      Creado: 2026-07-27T16:57:27.3750006Z
      Modificado: 2026-07-27T16:58:15.1796471Z
      Legible: true
      Escribible: true

      Nombre: AppData
      Tipo: Directorio
      Tamaño: 0 bytes
      Creado: 2025-07-04T14:35:56.2965815Z
      Modificado: 2025-07-04T14:35:56.5186568Z
      Legible: true
      Escribible: true

      ---
      Información del sistema de archivos:
      - Tipo: WindowsFileSystem
      - Total: 1022076719104 bytes
      - Libre: 768391630848 bytes

      ===== MENÚ =====
      1. Crear un directorio en el home
      2. Eliminar un directorio del home
      3. Ver contenido recursivo de un directorio
      0. Salir
      Selecciona una opción: 

---


## ✅ Rúbrica de evaluación

La calificación del ejercicio se obtendrá sumando la puntuación obtenida en cada uno de los siguientes apartados.

| Apartado | Aspectos evaluados | Puntuación máxima |
|-----------|--------------------|:-----------------:|
| **1. Visualización del directorio Home** | Se muestra correctamente el contenido del directorio Home y la información básica de cada archivo o directorio. | **2,0** |
| **2. Información del sistema de archivos** | Se obtiene y muestra correctamente la información del sistema de archivos utilizando la API NIO.2. | **1,0** |
| **3. Creación de directorios** | La opción permite crear correctamente un nuevo directorio y gestiona posibles errores. | **2,0** |
| **4. Eliminación de archivos o directorios** | La opción elimina correctamente archivos o directorios y controla las excepciones que puedan producirse. | **2,0** |
| **5. Visualización recursiva** | Se muestra correctamente el contenido de un directorio de forma recursiva utilizando `Files.walk()`. | **2,0** |
| **6. Organización y calidad del código** | El código está correctamente estructurado en funciones, utiliza nombres adecuados, mantiene una buena legibilidad y gestiona correctamente las excepciones. | **1,0** |
| | **TOTAL** | **10,0** |



