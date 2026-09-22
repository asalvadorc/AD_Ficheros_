# 🗺️ Guía de estudio

El contenido está dividido en **4 temas**. Sigue el orden indicado: cada tema parte de los conocimientos del anterior.

**Tema 1 — Sistema de ficheros**

Entenderás qué es un fichero, cómo se organiza el sistema de archivos y cómo acceder a él desde Kotlin.

| Orden | Página | Descripción |
|-------|--------|-------------|
| 1 | [Ficheros](T1_Sistema_de_ficheros/ficheros.md) | Qué es un fichero y para qué se usa |
| 2 | [Acceso al sistema. Java.nio](T1_Sistema_de_ficheros/NIO_AccesoFicheros.md) | Clases Path, Files, FileSystem... |
| 3 | [Ejercicio obligatorio 1](T1_Sistema_de_ficheros/exercicis.md) | Explorador interactivo del directorio personal |

---

**Tema 2 — Manejo de ficheros**

Aprenderás a leer y escribir ficheros de texto, binarios, imágenes y a hacer acceso aleatorio.

| Orden | Página | Descripción |
|-------|--------|-------------|
| 1 | [Introducción y clases](T2_Gestion_del_contenido/resumen.md) | Tabla resumen de todas las clases |
| 2 | [Formas de acceso](T2_Gestion_del_contenido/formas_acceso.md) | Acceso secuencial vs. aleatorio |
| 3 | [Ficheros de texto](T2_Gestion_del_contenido/lectura_escritura_texto.md) | Leer y escribir ficheros de texto (.txt) |
| 4 | [Ficheros binarios](T2_Gestion_del_contenido/lectura_escritura_binaria.md) | Introducción a los ficheros binarios |
| 5 | [Binarios no estructurados](T2_Gestion_del_contenido/binarios_no_estructurados.md) | Secuencia de bytes sin formato interno |
| 6 | [Binarios estructurados](T2_Gestion_del_contenido/binarios_estructurados.md) | Tipos primitivos con DataStream |
| 7 | [Imágenes](T2_Gestion_del_contenido/ficheros_imagen.md) | Leer, copiar y modificar imágenes |
| 8 | [Acceso aleatorio](T2_Gestion_del_contenido/acceso_aleatorio.md) | FileChannel y ByteBuffer |
| 9 | Ejercicio obligatorio 2 *(próximamente)* | Gestión completa de ficheros |

---

**Tema 3 — Ficheros de diferentes formatos**

Trabajarás con formatos de intercambio de datos (JSON, XML, CSV) usando librerías externas.

| Orden | Página | Descripción |
|-------|--------|-------------|
| 1 | [Introducción](T3_Formatos_diferentes/intro.md) | JSON, XML, CSV: cuándo usar cada uno |
| 2 | [Serialización de Objetos](T3_Formatos_diferentes/seriaci_dobjectes.md) | Convertir objetos a bytes y viceversa |
| 3 | [Ficheros de intercambio](T3_Formatos_diferentes/ficheros_intercambio.md) | CSV, JSON y XML con librerías |
| 4 | [Conversión entre formatos](T3_Formatos_diferentes/conversion.md) | De un formato a otro |
| 5 | [Ejercicio obligatorio 3](T3_Formatos_diferentes/ejercicios.md) | Aplicación con múltiples formatos |

---

**Tema 4 — Spring Boot y componentes de acceso a datos**

Aprenderás a integrar las operaciones con ficheros en una aplicación web mediante Spring Boot, Spring MVC, Tomcat y una API REST.

| Orden | Página | Descripción |
|-------|--------|-------------|
| 1 | [Introducción a Spring Boot](T4_Spring_Boot/intro.md) | Aplicaciones web, Tomcat, Spring MVC y Spring Boot |
| 2 | [Primera aplicación](T4_Spring_Boot/primera_aplicacion.md) | Crear una aplicación desde Spring Initializr |
| 3 | [Primera aplicación con ficheros](T4_Spring_Boot/ficheros.md) | Leer y escribir ficheros mediante una API REST |
| 4 | [Gestión de ficheros](T4_Spring_Boot/gestion_ficheros.md) | Crear, leer, listar y subir ficheros |
| 5 | [Texto y listado](T4_Spring_Boot/texto_y_listado.md) | Trabajar con texto y listados de ficheros |
| 6 | [Conversión CSV a JSON](T4_Spring_Boot/conversion.md) | Convertir datos mediante un servicio web |
| 7 | [Ejercicio obligatorio 4](T4_Spring_Boot/ejercicios.md) | Diseñar una API de gestión de apuntes |

---

## 💡 Consejos para el estudio en semipresencial

!!!tip "Cómo aprovechar este material"
    - **Lee primero la teoría** de cada página antes de intentar ejecutar el código.
    - **Ejecuta todos los ejemplos** en IntelliJ: la práctica es esencial para afianzar los conceptos.
    - Antes de pasar a la siguiente página, asegúrate de que entiendes el ejemplo anterior: compara tu salida con la salida esperada que aparece en cada ejemplo.
    - **Los ejercicios obligatorios** son la parte más importante: aplican todo lo aprendido en cada bloque.


---
