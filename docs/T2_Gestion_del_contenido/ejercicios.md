# 📝 Ejercicio 2: Proyecto Integrador (Parte 2) - Gestión de contenido

## 📋 Enunciado

En este ejercicio vamos a continuar desarrollando el **Explorador Interactivo** que iniciamos en el Tema 1. Ahora que ya sabemos cómo navegar por el sistema de ficheros, vamos a añadirle la capacidad de **crear, leer, modificar y copiar el contenido** de los archivos.

!!!info "Código Base"
    Debes utilizar como punto de partida el código que desarrollaste en el Ejercicio 1. Si no conseguiste terminarlo, puedes descargar la solución base a continuación para continuar desde aquí sin bloquearte.
    
    📥 **[Descargar Código Base Oficial (Solucion_T1_Base_T2.kt)](../recursos/Solucion_T1_Base_T2.kt)**

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

## ✅ Rúbrica de evaluación

| Criterio                                  | Puntuación máxima |
|-------------------------------------------|-------------------|
| Parte 1 integrada y funcionando           | 1                 |
| **Opción 4**: Creación correcta (hasta FIN) | 2                 |
| **Opción 5**: Lectura eficiente (Buffer)  | 1.5               |
| **Opción 6**: Paso de texto a binario     | 2                 |
| **Opción 7**: Copia correcta de archivos  | 1.5               |
| Gestión robusta de excepciones (`try/catch`) | 1                 |
| Comentarios y estructura en funciones     | 1                 |
| **Total**                                 | **10**            |
