# 📝 Ejercicio: Explorador interactivo del directorio personal


## 📋 Enunciado

Desarrollar un programa en **Kotlin** que permita **explorar y manipular el contenido del directorio personal del usuario (home)** utilizando la API de `java.nio.file`. El ejercicio tiene como finalidad practicar:

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

   * Tipo del sistema (`FileSystem`)
   * Espacio total y libre en bytes (`FileStore`)

4. Presentar un **menú interactivo** con las siguientes opciones:

   ```
   📋 Menú de acciones:
   1. Crear un directorio en el home
   2. Eliminar un directorio del home
   3. Ver contenido recursivo de un directorio
   0. Salir
   ```

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


## 📊 Rúbrica de evaluación

| Criterio                             | Excelente (2) | Bien (1) | Insuficiente (0) |
| ------------------------------------ | ------------- | -------- | ---------------- |
| Exploración y atributos del home     |               |          |                  |
| Menú funcional y navegación clara    |               |          |                  |
| Creación de directorios              |               |          |                  |
| Eliminación de directorios           |               |          |                  |
| Recorrido recursivo completo         |               |          |                  |
| Control de errores                   |               |          |                  |
| Claridad y organización del código   |               |          |                  |
| Comentarios y estilo del código      |               |          |                  |

