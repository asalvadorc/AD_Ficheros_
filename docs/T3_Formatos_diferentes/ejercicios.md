# 📝 Ejercicio 3: Proyecto Integrador (Parte 3) - Formatos de intercambio

## 📋 Enunciado

¡Llegamos a la fase final de nuestro **Explorador Interactivo**! Hasta ahora, nuestro programa explora carpetas y lee/escribe archivos. En esta última parte, vamos a dotarlo de capacidades "profesionales": **configuración externa** y **exportación de reportes** usando JSON, XML y CSV.

!!!info "Código Base"
    Debes utilizar como punto de partida el código del Ejercicio 2. Si te quedaste atascado, descarga el siguiente código base oficial para poder completar esta última parte del proyecto.

    📥 **[Descargar Código Base Oficial (Solucion_T2_Base_T3.kt)](../recursos/Solucion_T2_Base_T3.kt)**

Vamos a implementar dos grandes novedades en la aplicación:

**1. Carga de Configuración (Al iniciar la app)**
Antes, el explorador siempre arrancaba en el directorio `home`. Ahora, al arrancar, el programa debe buscar un fichero llamado `config.json` en la raíz del proyecto. Este fichero tendrá preferencias del usuario:
```json
{
  "directorio_inicial": "C:/Usuarios/Ana/Documentos",
  "mostrar_archivos_ocultos": false
}
```
Si el archivo `config.json` existe, el explorador iniciará en esa ruta y respetará la opción de ocultos. Si el archivo no existe, arrancará en el `home` por defecto.

**2. Exportación de directorios (Nueva opción en el menú)**
Añadimos una última opción al menú:

!!!Tip "📋 Menú final:"   
         ...
         **8- Exportar reporte del directorio actual**
         0- Salir

Al pulsar 8, el programa leerá todos los archivos y carpetas del directorio donde nos encontremos (nombre, tipo y tamaño) y generará **tres ficheros de reporte** en ese mismo directorio:
- `reporte.csv`
- `reporte.json`
- `reporte.xml`

---

## 🛠️ Requisitos técnicos

* Puedes utilizar las librerías externas que prefieras (Jackson, Gson, kotlinx.serialization para JSON; DOM o librerías externas para XML). Recuerda usar **Gradle** para gestionar las dependencias.
* **CSV:** No necesitas librería obligatoria, puedes generarlo concatenando *Strings* separados por comas, o usar *Apache Commons CSV*.
* **JSON:** El archivo `reporte.json` debe ser un array de objetos, donde cada objeto representa un archivo/carpeta con sus propiedades.
* El código debe estar modularizado. Crea una clase o función específica para la exportación de cada formato.

---

## ✅ Rúbrica de evaluación

| Criterio                                  | Puntuación máxima |
|-------------------------------------------|-------------------|
| Parte 2 integrada y funcionando           | 1                 |
| Lectura de preferencias desde `config.json` | 2                 |
| **Opción 8**: Generación correcta del CSV | 2                 |
| **Opción 8**: Generación correcta del JSON | 2                 |
| **Opción 8**: Generación correcta del XML | 2                 |
| Modularidad y uso correcto de dependencias | 1                 |
| **Total**                                 | **10**            |
