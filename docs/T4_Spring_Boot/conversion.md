# Convertir un fichero CSV a JSON

En el ejemplo anterior aprendimos a recibir un fichero mediante **MultipartFile**. Ahora utilizaremos su contenido para realizar una conversión sencilla:

```text
productos.csv → MultipartFile → List<Producto> → JSON
```

No necesitamos aprender nuevas anotaciones. Spring convertirá automáticamente la lista de objetos Kotlin a JSON.

!!! info "CSV sencillo"
    Este ejemplo utiliza un CSV educativo separado por punto y coma. No contempla campos que contengan el propio separador. Para ficheros CSV más complejos utilizaríamos una librería específica.

## 1. El fichero de entrada

Descarga [productos.csv](../recursos/productos.csv). Su contenido comienza así:

```csv
nombre;categoria;precio;stock
Teclado;Periféricos;45.99;10
Monitor 28";Pantallas;299.00;3
Ratón inalámbrico;Periféricos;25.50;0
```

La primera línea es la cabecera. Cada una de las líneas siguientes representa un producto.

## 2. Crear el modelo

Crea el paquete **model** y, dentro de él, el fichero **Producto.kt**:

```kotlin
package com.example.ficherosapi.model

data class Producto( // (1)!
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val stock: Int
)
```

1. Cada propiedad corresponde a una columna del CSV. Spring utilizará sus nombres como claves del JSON.

## 3. Crear el servicio de conversión

En el paquete **service**, crea **ConversionService.kt**:

```kotlin
package com.example.ficherosapi.service

import com.example.ficherosapi.model.Producto
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ConversionService {

    fun csvToProducts(file: MultipartFile): List<Producto> {
        return file.inputStream
            .bufferedReader(Charsets.UTF_8)
            .useLines { lines -> // (1)!
                lines
                    .drop(1) // (2)!
                    .filter { it.isNotBlank() }
                    .map { line -> toProduct(line) }
                    .toList()
            }
    }

    private fun toProduct(line: String): Producto {
        val fields = line.split(";") // (3)!

        require(fields.size == 4) {
            "Línea CSV incorrecta: $line"
        }

        return Producto(
            nombre = fields[0].trim(),
            categoria = fields[1].trim(),
            precio = fields[2].trim().toDouble(),
            stock = fields[3].trim().toInt()
        ) // (4)!
    }
}
```

1. Lee el archivo como texto UTF-8. **useLines** cierra automáticamente el lector al terminar.
2. Omite la primera línea porque contiene los nombres de las columnas.
3. Divide cada línea utilizando el punto y coma como separador.
4. Convierte los textos de precio y stock a sus tipos correspondientes y crea un objeto **Producto**.

El servicio realiza la conversión de CSV a objetos Kotlin. Todavía no genera manualmente el JSON.

## 4. Crear el controlador

En el paquete **controller**, crea **ConversionController.kt**:

```kotlin
package com.example.ficherosapi.controller

import com.example.ficherosapi.model.Producto
import com.example.ficherosapi.service.ConversionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class ConversionController(
    private val conversionService: ConversionService
) {

    @PostMapping("/api/conversions/csv-json")
    fun csvToJson(
        @RequestParam("file") file: MultipartFile
    ): List<Producto> {
        return conversionService.csvToProducts(file) // (1)!
    }
}
```

1. El controlador devuelve una lista de productos. Spring detecta los objetos y transforma automáticamente la lista a JSON.

El reparto de responsabilidades queda así:

```text
Controlador        Recibe el MultipartFile y devuelve la respuesta
Servicio           Lee y convierte las líneas
Modelo Producto    Representa cada fila
Spring Boot        Convierte List<Producto> a JSON
```

## 5. Probar la conversión

Arranca la aplicación y ejecuta desde la carpeta que contiene **productos.csv**:

```powershell
curl.exe -F "file=@productos.csv" http://localhost:8080/api/conversions/csv-json
```

La respuesta será un JSON similar a este:

```json
[
  {
    "nombre": "Teclado",
    "categoria": "Periféricos",
    "precio": 45.99,
    "stock": 10
  },
  {
    "nombre": "Monitor 28\"",
    "categoria": "Pantallas",
    "precio": 299.0,
    "stock": 3
  }
]
```

## 6. Qué hemos aprendido

- El fichero llega al controlador como **MultipartFile**.
- El servicio utiliza un lector de texto y separa las columnas.
- Cada fila se transforma en un objeto **Producto**.
- Spring convierte la lista de objetos a JSON sin construir cadenas manualmente.

!!! success "Conversión completada"
    Hemos conectado contenidos ya conocidos —lectura de texto, CSV y objetos Kotlin— con una respuesta JSON de Spring Boot sin añadir nuevas anotaciones.
