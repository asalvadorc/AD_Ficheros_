# Entender las lambdas en Kotlin

En los ejemplos de ficheros aparecerán instrucciones como estas:

```kotlin
elementos.forEach { elemento -> println(elemento) }
```

```kotlin
lector.use { reader ->
    println(reader.readLine())
}
```

Las llaves contienen una **lambda**: una función sin nombre que pasamos a otra función. En este apartado aprenderás a leer esa estructura paso a paso y a relacionarla con los bucles y las funciones que ya conoces.

!!! info "Dónde practicar"
    Guarda cada ejemplo completo en un archivo `.kt` diferente, dentro del paquete `sistema` del proyecto **`Ficheros`**. Ejecuta el `main` del ejemplo que estés estudiando. Los fragmentos que no incluyen `main` sirven para comparar formas equivalentes de escribir una instrucción.

## 1. De un bucle conocido a una lambda

Supongamos que queremos mostrar tres nombres de fichero. Con un bucle `for`:

```kotlin
package sistema

fun main() {
    val nombres = listOf("tema1.txt", "repaso.txt", "foto.png")

    for (nombre in nombres) {
        println(nombre)
    }
}
```

Podemos obtener la misma salida con `forEach`:

```kotlin
package sistema

fun main() {
    val nombres = listOf("tema1.txt", "repaso.txt", "foto.png")

    nombres.forEach { nombre ->
        println(nombre)
    }
}
```

En ambos casos se muestra:

```text
tema1.txt
repaso.txt
foto.png
```

La función `forEach` recorre la lista y ejecuta el bloque una vez por cada elemento. En cada ejecución entrega al bloque un nombre distinto.

| Parte | Qué significa en este ejemplo |
|---|---|
| `nombres` | La lista sobre la que trabajamos. |
| `.forEach` | La función que ejecutará el bloque por cada elemento. |
| `{ ... }` | La lambda que le pasamos: el código que debe ejecutar. |
| `nombre` | El parámetro que recibe el elemento de esa ejecución. |
| `->` | Separa el parámetro de las instrucciones del bloque. |
| `println(nombre)` | La instrucción que se ejecuta con ese elemento. |

Puedes leerlo así: **«Por cada elemento de `nombres`, recíbelo como `nombre` y muéstralo»**.

## 2. Qué significa la flecha `->`

Una lambda con un parámetro tiene esta forma:

```text
{ parámetro ->
    instrucciones que utilizan ese parámetro
}
```

La flecha **no asigna un valor ni es un bucle**. Indica dónde terminan los parámetros y dónde empieza el cuerpo de la función.

El nombre del parámetro lo eliges tú. Estas dos instrucciones hacen lo mismo:

```kotlin
nombres.forEach { nombre -> println(nombre) }
nombres.forEach { elemento -> println(elemento) }
```

`nombre` y `elemento` no son palabras reservadas. Dentro de cada bloque representan el elemento que `forEach` entrega en esa ejecución.

La sintaxis de lambda tampoco determina cuántas veces se ejecuta el bloque: **eso depende de la función que lo recibe**. `forEach` lo ejecuta por cada elemento; `use` lo ejecuta una vez con el recurso.

## 3. Por qué el bloque aparece fuera de los paréntesis

Normalmente pasamos los argumentos de una función entre paréntesis:

```kotlin
println("Hola")
```

Un argumento también puede ser una función. En el caso de `forEach`, esa función es la lambda:

```kotlin
nombres.forEach({ nombre ->
    println(nombre)
})
```

En Kotlin, si el último argumento es una lambda, podemos escribirla **fuera de los paréntesis**. Si es el único argumento, podemos omitir los paréntesis:

```kotlin
nombres.forEach { nombre ->
    println(nombre)
}
```

Ambas formas llaman a la misma función y le pasan el mismo bloque de código. En los ejemplos utilizaremos habitualmente la segunda.

## 4. Qué es `it`

Cuando la lambda recibe un solo parámetro y Kotlin puede deducirlo, podemos omitir su declaración y usar el nombre implícito **`it`**:

```kotlin
// Con un nombre explícito:
nombres.forEach { nombre -> println(nombre) }

// Con el nombre implícito:
nombres.forEach { println(it) }
```

En la segunda versión, `it` representa el nombre del fichero que se está procesando. Al omitir la declaración del parámetro también desaparece `->`.

!!! tip "Para empezar"
    Utiliza nombres explícitos como `nombre`, `ruta` o `reader`. Cuando comprendas el bloque, podrás reconocer la versión abreviada con `it`. En bloques anidados los nombres explícitos ayudan a distinguir a qué objeto se refiere cada instrucción.

## 5. Lambdas que devuelven un resultado: `filter` y `map`

Una lambda puede producir un resultado. En los siguientes ejemplos, **la última expresión del bloque es su resultado**, sin escribir `return`.

### `filter`: decidir qué elementos conservar

```kotlin
package sistema

fun main() {
    val nombres = listOf("tema1.txt", "repaso.txt", "foto.png")

    val textos = nombres.filter { nombre ->
        nombre.endsWith(".txt")
    }

    println(textos)
}
```

Salida:

```text
[tema1.txt, repaso.txt]
```

La lambda devuelve `true` o `false` para cada nombre. `filter` construye una lista con los elementos que cumplen la condición. La lista `nombres` no cambia.

La versión abreviada es:

```kotlin
val textos = nombres.filter { it.endsWith(".txt") }
```

### `map`: transformar cada elemento

```kotlin
val longitudes = nombres.map { nombre ->
    nombre.length
}
println(longitudes)
```

Usando la misma lista de nombres, muestra:

```text
[9, 10, 8]
```

La lambda calcula un número para cada nombre. `map` construye una nueva lista con esos resultados, sin modificar los nombres originales.

| Función | Qué hace con el bloque |
|---|---|
| `forEach` | Lo ejecuta para cada elemento, por ejemplo para mostrarlo. |
| `filter` | Utiliza su resultado booleano para seleccionar elementos. |
| `map` | Utiliza su resultado para transformar cada elemento. |

## 6. Entender `use { reader -> ... }`

Además de listas, trabajaremos con **recursos que hay que cerrar**, como lectores de ficheros, flujos de directorios y conexiones a bases de datos.

Observa este ejemplo. Antes de ejecutarlo, crea la carpeta `documentos` en la raíz del proyecto y guarda dentro `archivo.txt` en UTF-8, con dos líneas:

```text
Primera línea
Segunda línea
```

```kotlin
package sistema

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val ruta = Paths.get("documentos", "archivo.txt")
    val lectorConBuffer = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)

    lectorConBuffer.use { reader ->
        val linea = reader.readLine()
        println(linea)
    }
}
```

Salida:

```text
Primera línea
```

Desglosemos las operaciones:

1. `Files.newBufferedReader(...)` abre el lector. La ruta es relativa al directorio de trabajo; al ejecutar desde la raíz del proyecto, busca allí `documentos/archivo.txt`.
2. `.use` recibe la lambda y la ejecuta con ese lector.
3. `reader` es el nombre que damos al lector dentro del bloque. Hace referencia al mismo objeto que `lectorConBuffer`; no crea otro lector.
4. `readLine()` lee una línea y avanza en el fichero. Solo se llama una vez, por eso no se muestra la segunda línea.
5. Al terminar el bloque, `use` cierra el lector. También lo cierra si se produce una excepción dentro del bloque.

Puedes leer la estructura así: **«Usa este lector, llámalo `reader` dentro del bloque, ejecuta estas instrucciones y ciérralo al terminar»**.

!!! note "Qué hace cada cosa"
    La lambda contiene las instrucciones de lectura. El cierre automático lo proporciona la función **`use`**, no las llaves ni la flecha. Una lambda de `forEach` no cierra recursos automáticamente. `use` tampoco sustituye a `try/catch`: cierra el recurso, pero los errores siguen propagándose si no los capturamos.

### Comparación con el cierre manual

Para relacionarlo con una estructura conocida, el comportamiento esencial del bloque anterior se puede expresar así:

```kotlin
val reader = lectorConBuffer

try {
    val linea = reader.readLine()
    println(linea)
} finally {
    reader.close()
}
```

Son **alternativas**, no dos bloques que debas ejecutar uno después del otro con el mismo lector. `use` se encarga del cierre y también gestiona el caso de que se produzca un error al cerrar mientras ya se estaba propagando otro error.

También puedes reconocer estas formas equivalentes:

```kotlin
lectorConBuffer.use({ reader -> println(reader.readLine()) })
lectorConBuffer.use { reader -> println(reader.readLine()) }
lectorConBuffer.use { println(it.readLine()) }
```

Cada línea muestra una alternativa. Tras ejecutar una de ellas, el lector queda cerrado y no se debe reutilizar para seguir leyendo.

## 7. Aplicarlo al listado de un directorio

`Files.list` devuelve un flujo de rutas que también hay que cerrar. La carpeta `documentos` debe existir:

```kotlin
package sistema

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val carpeta = Paths.get("documentos")

    Files.list(carpeta).use { rutas ->
        rutas.forEach { ruta ->
            println(ruta.fileName)
        }
    }
}
```

Aquí hay **dos lambdas**:

- `rutas ->`: recibe una vez el flujo que entrega `Files.list`. `use` lo cierra al terminar.
- `ruta ->`: recibe cada una de las rutas al recorrer el flujo. `forEach` ejecuta este bloque por cada elemento.

El bloque de `forEach` termina antes que el de `use`. Primero se muestran los nombres y, cuando acaba el recorrido, se cierra el flujo. No se garantiza el orden de los nombres en este listado.

Más adelante reconocerás la misma estructura en JDBC: `use { conn -> ... }` recibe una conexión, `use { stmt -> ... }` una sentencia y `use { rs -> ... }` un resultado. Los nombres cambian, pero la forma de leer el bloque es la misma.

## 8. Comprueba tu comprensión

### A. Cambiar el nombre del parámetro

```kotlin
listOf("a.txt", "b.txt").forEach { fichero ->
    println(fichero)
}
```

¿Cambiaría el resultado si sustituyes `fichero` por `nombre` en los dos lugares?

??? success "Ver respuesta"
    No. Es el nombre local del parámetro y puedes elegirlo. Se siguen mostrando `a.txt` y `b.txt`, en ese orden.

### B. Reconocer `it` y el resultado de la lambda

```kotlin
val resultado = listOf("a.txt", "foto.png", "b.txt")
    .filter { it.endsWith(".txt") }
println(resultado)
```

¿Qué representa `it` y qué se muestra?

??? success "Ver respuesta"
    `it` representa cada nombre que se comprueba. La condición devuelve un booleano y se muestra `[a.txt, b.txt]`. No se modifican los nombres ni se borra ningún fichero: se filtra una lista en memoria.

### C. Contar las lecturas

Un lector recién abierto contiene las líneas `A`, `B` y `C`:

```kotlin
lector.use { reader ->
    println(reader.readLine())
    println(reader.readLine())
}
```

¿Qué líneas se muestran? ¿Queda abierto el lector para leer `C` después del bloque?

??? success "Ver respuesta"
    Se muestran `A` y `B`, una por cada llamada a `readLine()`. El lector se cierra al terminar `use`, aunque no se haya leído todo el fichero. Para leer `C` con ese mismo lector habría que hacerlo dentro del bloque, antes de cerrarlo.

## Referencias

- [Kotlin: sintaxis de lambdas, último argumento e `it`](https://kotlinlang.org/docs/lambdas.html#lambda-expression-syntax).
- [Kotlin: función `use` y cierre de recursos](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.io/use.html).

Continúa con [Acceso al sistema de ficheros: Java NIO](NIO_AccesoFicheros.md). En el tema 2 volveremos sobre la lectura de ficheros y el [manejo de errores y recursos](../T2_Gestion_del_contenido/gestion_segura_recursos.md).
