# Comprensión y uso de use, it y lambdas en Kotlin

En Kotlin, la gestión eficiente de recursos (como archivos, conexiones, streams...) se realiza habitualmente con el método **use**, que garantiza el cierre automático del recurso una vez finalizada su utilización, sin necesidad de escribir bloques **try-finally**.

Además, Kotlin permite trabajar de forma concisa con **funciones lambda**, utilizando la palabra clave **it** para referirse al parámetro implícito cuando hay solo uno.

# lambda

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



# it

En Kotlin, **it** es el nombre implícito que se le da **al único parámetro de una lambda** cuando no se le asigna un nombre explícito, para que no tengas que declararlo tú.

Se usa cuando:

  - Una lambda tiene solo un parámetro.
  - No necesitas nombrar el parámetro explícitamente.
  - Quieres un código más conciso.

Ejemplo simple

        val lista = listOf("Ana", "Luis", "Marta")

        lista.forEach { println(it) } // 'it' representa cada elemento de la lista
       

Es equivalente a:

        lista.forEach { nombre -> println(nombre) }



¿Cuándo no se puede usar it?

- Si la lambda tiene más de un parámetro, debes nombrarlos explícitamente.

- Si quieres mejorar la legibilidad, puedes usar nombres explícitos aunque haya solo uno.

!!!Note "Nota"
    **it** es específico de Kotlin. En Java no existe como palabra clave ni como nombre implícito de parámetros.



# Use

La función **use**  es una función de extensión en Kotlin que se aplica a cualquier objeto que implemente la interfaz Closeable o AutoCloseable (como archivos, streams, lectores, sockets...).

**Use** permite ejecutar un bloque de código sobre un recurso (como un archivo), y se asegura de cerrarlo automáticamente cuando termina el bloque, incluso si ocurre una excepción. Es la alternativa Kotlin al **try-with-resources** de Java.

        recurso.use {
            // Aquí usas el recurso
        }


    - recurso es un objeto como BufferedReader, FileWriter, InputStream, etc.
    - Dentro del bloque, puedes acceder al recurso como it o dándole un nombre explícito.
    
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

Internamente, use hace lo mismo que esto:

    val writer = FileWriter("archivo.txt")
    try {
        writer.write("Texto")
    } finally {
        writer.close()
    }


