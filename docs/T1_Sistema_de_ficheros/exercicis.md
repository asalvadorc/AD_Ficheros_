# 📝 Ejercicio: Explorador interactivo del directorio personal


## 📋 Enunciado

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

```
📁 Exploración del home: /home/alumno

🔹 Nombre: Documentos
   Tipo: Directorio
   Tamaño: 4096 bytes
   Creado: 2024-10-01T12:30:00
   Modificado: 2025-08-15T09:00:00
   Legible: true
   Escribible: true

📊 Información del sistema de archivos:
- Tipo: LinuxFileSystem
- Total: 500000000000 bytes
- Libre: 123456789000 bytes

📋 Menú de acciones:
1. Crear un directorio en el home
2. Eliminar un directorio del home
3. Ver contenido recursivo de un directorio
0. Salir
```

---


## ✅ Rúbrica de evaluación


| Criterio                         | Puntuación máxima |
|----------------------------------|-------------------|
| Exploración y atributos del home | 1 |
| Menú funcional y navegación clara| 2 |
| Creación de directorios          | 1 |
| Eliminación de directorios       | 1 |
| Recorrido recursivo completo     | 2 |
| Control de errores               | 1 |
| Claridad y organización del código | 1 |
| Comentarios y estilo del código  | 1 |
| **Total**                        | **10** |


