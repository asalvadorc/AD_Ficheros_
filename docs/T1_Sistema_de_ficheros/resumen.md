---
hide:
  - toc
---

# 📘 Resumen del Tema 1: Sistema de ficheros

| Concepto | Idea clave | Ejemplo o nota |
|----------|------------|----------------|
| Fichero | Es una unidad de almacenamiento persistente para guardar datos. | Se conserva aunque termine la ejecución del programa. |
| Directorio | Es una carpeta que organiza archivos y subcarpetas. | Permite estructurar la información. |
| Ruta (`Path`) | Representa la ubicación de un archivo o carpeta. | Se crea con `Paths.get(...)`. |
| Java NIO | Es la API moderna de Java para trabajar con ficheros y directorios. | Ofrece más flexibilidad que `java.io`. |
| `Paths` | Permite construir objetos `Path` a partir de una cadena de texto. | No comprueba si el archivo existe. |
| `Files` | Realiza operaciones reales sobre archivos y carpetas. | Puede crear, borrar, copiar o listar elementos. |
| Tipos de ficheros | Pueden ser de texto, binarios, código, configuración o bases de datos. | Ejemplos: `.txt`, `.jpg`, `.kt`, `.json`. |
| Persistencia | Los ficheros sirven para guardar datos de forma permanente. | Útil para configuraciones, historial o estado. |
| Lectura/escritura | Permiten leer y guardar información de forma estructurada. | Se puede trabajar con texto o datos binarios. |
| Gestión de errores | Es importante controlar excepciones al usar ficheros. | Se suelen manejar con `IOException`. |

## Operaciones principales con `Files`

| Operación | Qué hace |
|-----------|----------|
| `exists(...)` | Comprueba si un archivo o carpeta existe. |
| `createFile(...)` | Crea un nuevo archivo. |
| `createDirectory(...)` | Crea una carpeta. |
| `delete(...)` | Borra un archivo o carpeta. |
| `copy(...)` | Copia un fichero a otra ubicación. |
| `move(...)` | Mueve o renombra un fichero. |
| `readString(...)` | Lee el contenido completo de un archivo de texto. |
| `writeString(...)` | Escribe contenido en un archivo de texto. |

## Idea final del tema

| Resumen | Explicación |
|---------|-------------|
| Los ficheros permiten guardar información de forma persistente y Java NIO facilita su gestión mediante rutas (`Path`) y operaciones sobre archivos (`Files`). | Esta es la idea central del tema 1. |