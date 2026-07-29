# 📝 Ejercicio 2: Proyecto Integrador (Parte 2) - Gestión de contenido

## 📋 Enunciado

En este ejercicio vamos a continuar desarrollando el **Explorador Interactivo** que iniciamos en el Tema 1. Ahora que ya sabemos cómo navegar por el sistema de ficheros, vamos a añadirle la capacidad de **crear, leer, modificar y copiar el contenido** de los archivos.


!!!info "Código Base"
    Utiliza como punto de partida el código desarrollado en el Ejercicio 1. Si lo prefieres, puedes comenzar directamente desde la plantilla oficial del proyecto, que incluye la estructura del programa y los retos a completar.
    
    📥 **[Descargar Código Base Oficial (Solucion_T1_Plantilla_T2.kt)](../recursos/Solucion_T1_Base_T2.kt)**

La aplicación debe incorporar las siguientes **nuevas opciones** al menú interactivo:

!!!Tip "📋 Menú ampliado:"
    - 1- Crear un directorio
    - 2- Eliminar un directorio o fichero
    - 3- Ver contenido recursivo de un directorio
    - **4- Crear un archivo de texto**
    - **5- Leer un archivo de texto**
    - **6- Encriptar/Ocultar archivo (Texto a Binario)**
    - **7- Copiar un archivo a otra ruta**
    - 0- Salir

---

## 🛠️ Requisitos técnicos de las nuevas opciones

- **Opción 4 (Crear texto):** Debe pedir al usuario el nombre del archivo y permitirle escribir líneas de texto por consola. Seguirá guardando líneas en el fichero hasta que el usuario escriba la palabra `"FIN"`. Usa `Files.write` o un `BufferedWriter`.
- **Opción 5 (Leer texto):** Pide el nombre de un fichero y muestra su contenido por consola. Debe ser eficiente, utilizando `Files.newBufferedReader()`.
- **Opción 6 (Encriptar):** Lee el contenido de un fichero de texto, invierte los bytes o realiza alguna operación simple (como sumarle 1 al valor de cada byte) y lo guarda en un **fichero binario** con extensión `.bin` (utilizando `Files.readAllBytes` y `Files.write`).
- **Opción 7 (Copiar):** Pide el nombre del archivo original y el nombre del archivo destino, y realiza la copia utilizando `Files.copy`.
- **Gestión de errores:** Todas las operaciones deben estar protegidas con `try/catch`. Si el usuario intenta leer un archivo que no existe, el programa no debe cerrarse de golpe, sino mostrar un mensaje amistoso y volver al menú.

---

📤 Ejemplo de salida esperada


        HOME: C:\Users\as_co

        --------------------------------
        Nombre: aliciatxt
        Tipo: Archivo
        Tamaño: 34 bytes
        Creado: 2026-07-27T16:57:27.3750006Z
        Modificado: 2026-07-27T16:58:15.1796471Z
        Legible: true
        Escribible: true
        --------------------------------
        Nombre: AppData
        Tipo: Directorio
        Tamaño: 0 bytes
        Creado: 2025-07-04T14:35:56.2965815Z
        Modificado: 2025-07-04T14:35:56.5186568Z
        Legible: true
        Escribible: true
        ------

        Información del sistema de archivos
        Tipo: NTFS
        Espacio total: 1022076719104 bytes
        Espacio libre: 768046809088 bytes
        Información del sistema de archivos:
        - Tipo: WindowsFileSystem
        - Total: 1022076719104 bytes
        - Libre: 768046809088 bytes


        ===== MENÚ =====
        1. Crear directorio
        2. Eliminar directorio o fichero
        3. Ver contenido recursivo
        4. Crear archivo de texto
        5. Leer archivo de texto
        6. Encriptar archivo
        7. Copiar archivo
        0. Salir
        Selecciona una opción:

        4
        Nombre del archivo: nuevo.txt
        Escribe líneas (FIN para terminar):
        hola
        adios
        FIN
        Archivo creado correctamente.

## ✅ Rúbrica de evaluación


La calificación del ejercicio se obtendrá sumando la puntuación obtenida en cada uno de los siguientes apartados.

| Reto | Aspectos evaluados | Puntuación máxima |
|-------|--------------------|:-----------------:|
| **Reto 1. Ampliación del menú** | Se incorporan correctamente las nuevas opciones al menú principal y se integran con la estructura del programa. | **0,5** |
| **Reto 2. Creación de archivos de texto** | Se implementa correctamente la creación de un archivo de texto, permitiendo introducir varias líneas desde consola hasta finalizar con la palabra `FIN`. | **2,0** |
| **Reto 3. Lectura de archivos de texto** | Se implementa correctamente la lectura y visualización del contenido de un archivo de texto. | **1,5** |
| **Reto 4. Encriptación de archivos** | Se implementa correctamente la lectura del archivo, la transformación de los datos y la generación del fichero binario resultante. | **2,0** |
| **Reto 5. Copia de archivos** | Se implementa correctamente la copia de un archivo utilizando la API `java.nio.file`. | **1,5** |
| **Gestión robusta de excepciones** | Se gestionan adecuadamente las posibles excepciones mediante bloques `try/catch`. | **1,0** |
| **Comentarios y estructura del código** | El código está correctamente organizado en funciones, incluye comentarios útiles cuando son necesarios y mantiene una estructura clara y legible. | **1,5** |
| | **TOTAL** | **10,0** |


