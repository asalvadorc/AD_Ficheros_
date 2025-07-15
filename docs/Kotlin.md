
# Kotlin

En el módulo de Programación, de 1º de DAM, hemos trabajado principalmente con Java, un lenguaje robusto, orientado a objetos y ampliamente utilizado en el desarrollo de aplicaciones empresariales. Gracias a Java, hemos aprendido conceptos clave como clases, objetos, herencia, interfaces y estructuras de control.

A partir de ahora, en el módulo de Acceso a Datos (AD), vamos a continuar aplicando estos mismos conceptos, pero dando un paso más allá y utilizando **Kotlin**, un lenguaje moderno, conciso y seguro que funciona sobre la máquina virtual de Java (JVM). Fue desarrollado por JetBrains (los creadores de IntelliJ IDEA) y es 100% interoperable con Java.

Los principales usos de Kotlin son:

- Aplicaciones Android 📱 (mayoría actuales)
- Aplicaciones de escritorio (con Swing, JavaFX o Compose Desktop)
- Backends web (con frameworks como Ktor o Spring)
- Desarrollo multiplataforma (Kotlin Multiplatform)


## Gestión de excepciones

Una **excepción** es un error que ocurre en tiempo de ejecución y que interrumpe el flujo normal del programa (por ejemplo: dividir por cero, leer un archivo que no existe, etc.).

La gestión de excepciones implica detectar, manejar y recuperarse de errores en el código.
Kotlin utiliza el mismo modelo de excepciones que Java, pero con algunas diferencias importantes. En Kotlin hay dos formas comunes de gestionar excepciones:

**1. try-catch → Captura y manejo explícito de errores**{.azul}

Se utiliza cuando queremos atrapar y manejar una excepción que puede producirse en un bloque de código.

**Sintaxis**

        try {
            //  Código que puede fallar
        } catch (e1: IOException) {
            // Código para manejar el error
        } catch (e2: Exception) {
            // Código para manejar el error
        } finally {
            // opcional. Finalización y liberación de recursos
        }


**Ejemplo**

        fun main() {
        try {
            val resultado = 10 / 0
            println("Resultado: $resultado")
        } catch (e: ArithmeticException) {
            println("Error: no se puede dividir entre cero.")
        } finally {
            println("Fin del bloque try-catch.")
        }
    }

**2. use → Gestión automática de recursos**{.azul}

Se utiliza con objetos que implementan la interfaz Closeable o AutoCloseable (como archivos, streams, lectores, sockets...). Es el equivalente de **try-with-resources** en Java.

Con **use**, Kotlin abre, utiliza y cierra automáticamente el recurso (como un archivo), incluso si ocurre una excepción.


        recurso.use {
            // Aquí usas el recurso
        }

    - recurso es un objeto como BufferedReader, FileWriter, InputStream, etc.
    
    
 **Ejemplo 1:** Leer archivo con BufferedReader

        import java.io.BufferedReader
        import java.io.FileReader

        fun main() {
            BufferedReader(FileReader("archivo.txt")).use { reader ->
                val linea = reader.readLine()
                println(linea)
            }
        }

    - Cuando el bloque use { ... } termina, el reader se cierra automáticamente.

**Ejemplo 2:** Escribir archivo con FileWriter

        import java.io.FileWriter

        fun main() {
            FileWriter("archivo.txt").use {
                it.write("Hola, Kotlin\n")
            }
        }

Internamente, **use** hace lo mismo que esto:

            val writer = FileWriter("archivo.txt")
            try {
                writer.write("Texto")
            } finally {
                writer.close()
            }

**Ejemplo 3:** Ejemplo con BufferedReader. **Use** se encarga de cerrar el BufferedReader automáticamente, aunque haya un error.

    import java.io.File

    fun main() {
        val archivo = File("datos.txt")

        try {
            archivo.bufferedReader().use { reader ->
                println("Primera línea: ${reader.readLine()}")
            }
        } catch (e: Exception) {
            println("Error al leer el archivo: ${e.message}")
        }
    }



## Funciones lambda

Kotlin permite trabajar de forma concisa con **funciones lambda**, utilizando la palabra clave **it** para referirse al parámetro implícito cuando hay solo uno.

Las funciones lambda son funciones definidas sin un nombre, utilizadas para operaciones simples y rápidas, mejorando la legibilidad del código y el rendimiento.

**Una lambda** es una expresión de función que se puede usar como valor. Kotlin la define con la siguiente sintaxis:

    { parámetro1, parámetro2 -> cuerpo de la función }

    - Los parámetros van antes del ->.
    - El cuerpo (lo que hace la función) va después del ->.
  

🧪 Ejemplo básico:

        val saludar = { nombre: String -> println("Hola, $nombre") }

        saludar("Ana")

    - Aquí saludar es una variable que almacena una lambda.
    - La lambda toma un String y muestra un saludo.


**Kotlin** permite pasar **lambdas como argumentos** de funciones de orden superior:

        val lista = listOf(1, 2, 3, 4)

        lista.forEach { println(it) }

    - { println(it) } es una lambda que se ejecuta para cada elemento de la lista.
    - it es el valor del elemento actual (ver it en el apartado siguiente).
  
- Lambdas sin parámetros


        val decirHola = { println("Hola") }
        decirHola()

- Lambdas con múltiples parámetros 

        val sumar = { a: Int, b: Int -> a + b }
        println(sumar(3, 4)) // Imprime 7

- Comparación con funciones tradicionales

| Función tradicional                          | Lambda equivalente                                      |
|----------------------------------------------|----------------------------------------------------------|
| `fun saludar(nombre: String) { ... }`        | `val saludar = { nombre: String -> ... }`              |
| `return a + b` dentro de una función         | `val suma = { a: Int, b: Int -> a + b }`               |
| `fun cuadrado(x: Int): Int = x * x`          | `val cuadrado = { x: Int -> x * x }`                   |
| `fun sinParametros() = println("Hola")`      | `val sinParametros = { println("Hola") }`    



**it**{.azul}

En Kotlin, **it** es el nombre implícito que se le da **al único parámetro de una lambda** cuando no se le asigna un nombre explícito. Se usa cuando:

  - Una lambda tiene solo un parámetro.
  - No se necesita nombrar el parámetro explícitamente.
  - Se quiere un código más conciso.

Ejemplo

        val lista = listOf("Ana", "Luis", "Marta")

        lista.forEach { println(it) } // 'it' representa cada elemento de la lista
       

Es equivalente a:

        lista.forEach { nombre -> println(nombre) }



¿Cuándo no se puede usar it?

- Si la lambda tiene más de un parámetro, debes nombrarlos explícitamente.

- Si quieres mejorar la legibilidad, puedes usar nombres explícitos aunque haya solo uno.

!!!Note "Nota"
    **it** es específico de Kotlin. En Java no existe como palabra clave ni como nombre implícito de parámetros.



