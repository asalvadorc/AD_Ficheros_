---
hide:
  - toc
---
# Resultados de Aprendizaje y Criterios de Evalucaicón.

Esta unidad trabaja dos resultados de aprendizaje relacionados entre sí:

- **RA 1**, centrado en el acceso, la lectura, la escritura y la transformación de información almacenada en ficheros.
- **RA 6**, centrado en la creación de componentes que gestionan esos datos y en su integración en aplicaciones.

El **Tema 4** conecta ambos resultados: utiliza Spring Boot para convertir las operaciones con ficheros del RA 1 en componentes accesibles mediante una API web. Los criterios del RA 6 relacionados con bases de datos y documentos se desarrollarán en los temas posteriores.

## RA1. Desarrolla aplicaciones que gestionan información almacenada en ficheros identificando el campo de aplicación de los mismos y utilizando clases específicas.


**Criterios de evaluación del RA 1**

| Letra | Criterio de evaluación | Tema | Contenido didáctico |
|:---:|---|:---:|---|
| a) | Se han identificado las clases adecuadas para gestionar ficheros y directorios. | T1 | Acceso al sistema de archivos con `File`, `Path` y clases relacionadas. |
| b) | Se han valorado las ventajas e inconvenientes de las formas de acceso a los ficheros. | T2 | Acceso secuencial y acceso aleatorio. |
| c) | Se han utilizado clases para recuperar información de ficheros. | T2 | Lectura de ficheros de texto y otros formatos. |
| d) | Se han utilizado clases para almacenar información en ficheros. | T2 | Escritura y modificación de ficheros. |
| e) | Se han utilizado clases para realizar conversiones entre formatos. | T3 | Conversión entre JSON, XML, CSV y otros formatos. |
| f) | Se han gestionado los errores y las validaciones durante el acceso a ficheros. | T2 | Excepciones de entrada y salida y uso seguro de recursos. |
| g) | Se han probado y documentado las aplicaciones desarrolladas. | T1-T3 | Pruebas, comentarios y documentación. |

## RA 6. Programa componentes de acceso a datos identificando las características que debe poseer un componente y utilizando herramientas de desarrollo

Este resultado de aprendizaje se desarrolla progresivamente en los temas dedicados a ficheros, bases de datos y documentos XML. En el **Tema 4** se aborda la gestión de ficheros mediante componentes de una aplicación web construida con Spring Boot.

**Criterios de evaluación del RA 6**

| Letra | Criterio de evaluación | Tema | Contenido didáctico |
|:---:|---|:---:|---|
| a) | Se han valorado las ventajas e inconvenientes de utilizar programación orientada a componentes. | T4 | Separación entre controlador, servicio y acceso a ficheros. |
| b) | Se han identificado herramientas de desarrollo de componentes. | T4 | Spring, Spring MVC, Spring Boot y Tomcat. |
| c) | Se han programado componentes que gestionan información almacenada en ficheros. | T4 | Componentes para leer, escribir, listar, subir y convertir ficheros. |
| h) | Se han probado y documentado los componentes desarrollados. | T4 | Pruebas de la API con Postman y documentación de sus operaciones. |
| i) | Se han integrado los componentes desarrollados en aplicaciones. | T4 | Integración de la gestión de ficheros en una aplicación web con Spring Boot. |
<!--
**Rúbrica de Evaluación - Acceso y gestión de ficheros en Kotlin**{.azul}

| Criterio de Evaluación                          | Indicador de logro                                                 | Nivel Excelente (10)                      | Nivel Satisfactorio (7)               | Nivel Básico (5)                         | Nivel Insuficiente (0-4)                    |
|--------------------------------------------------|----------------------------------------------------------------------|-------------------------------------------|--------------------------------------|----------------------------------------|-----------------------------------------------|
| **a)** Uso de clases para ficheros y directorios | Utiliza `File`, `Path`, `Files` y `DirectoryStream` correctamente   | Aplica clases con fluidez en distintos contextos | Aplica clases en casos simples       | Utiliza clases parcialmente o con errores | No utiliza clases adecuadamente              |
| **b)** Valora formas de acceso                   | Compara acceso secuencial y aleatorio con criterio                  | Argumenta ventajas/inconvenientes con ejemplos | Describe las diferencias principales | Enumera tipos sin valoración           | No diferencia las formas de acceso           |
| **c)** Recupera información de ficheros         | Lee correctamente contenido de archivos                             | Lee de varios formatos y fuentes           | Lee textos o binarios simples         | Recupera parcialmente                     | No consigue leer datos de ficheros           |
| **d)** Almacena información en ficheros         | Escribe datos en diferentes formatos (JSON, texto, binario)         | Escribe correctamente en formato estructurado | Escribe datos simples correctamente  | Escritura parcial o incompleta            | Escritura incorrecta o inexistente           |
| **e)** Realiza conversiones entre formatos       | Convierte objetos entre distintos formatos                          | Maneja conversión JSON/XML/binario sin errores | Convierte objetos simples           | Solo realiza una conversión parcial       | No realiza ninguna conversión               |
| **f)** Gestiona excepciones                      | Maneja errores de E/S y serialización con `try-catch`, `use`       | Captura excepciones concretas y previene errores | Usa `try-catch` básico               | Manejo limitado o incorrecto              | No gestiona errores ni valida               |
| **g)** Prueba y documenta la aplicación         | Documenta, prueba y explica su funcionamiento                       | Incluye pruebas, comentarios claros y ejemplos | Comenta funciones y prueba básica   | Documentación o pruebas escasas            | No documenta ni prueba su código             |
-->
