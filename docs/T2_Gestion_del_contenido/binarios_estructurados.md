# 🔹Ficheros binarios estructurados

Aunque **java.nio.file** es la API moderna para trabajar con rutas y archivos, las clases **DataOutputStream** y **DataInputStream** de **java.io** siguen siendo la opción más adecuada para escribir y leer **binario estructurado**.
Son más simples, seguras, portables y claras para representar estructuras secuenciales como registros.

En contextos donde se requiera rendimiento avanzado o acceso aleatorio, puede usarse **FileChannel** y **ByteBuffer**, aunque su complejidad las hace menos recomendables.

**Clases y método de  DataInputStream y DataOutputStream**{.verde}


| Clase               | Método                          | Tipo de dato           | Descripción                                               |
|--------------------|----------------------------------|------------------------|-----------------------------------------------------------|
| DataOutputStream   | `writeInt(int)`                  | Entero (4 bytes)       | Escribe un entero con signo                               |
|                    | `writeDouble(double)`            | Decimal (8 bytes)      | Escribe un número en coma flotante                        |
|                    | `writeFloat(float)`              | Decimal (4 bytes)      | Escribe un número float                                   |
|                    | `writeLong(long)`                | Entero largo (8 bytes) | Escribe un long                                           |
|                    | `writeBoolean(boolean)`          | Booleano (1 byte)      | Escribe un valor verdadero/falso                          |
|                    | `writeChar(char)`                | Carácter (2 bytes)     | Escribe un carácter Unicode                               |
|                    | `writeUTF(String)`               | Cadena UTF-8           | Escribe una cadena precedida por su longitud en 2 bytes   |
|                    | `writeByte(int)`                 | Byte (1 byte)          | Escribe un solo byte                                      |
|                    | `writeShort(int)`                | Entero corto (2 bytes) | Escribe un short                                          |
| DataInputStream    | `readInt()`                      | Entero                 | Lee un entero con signo                                   |
|                    | `readDouble()`                   | Decimal                | Lee un número double                                      |
|                    | `readFloat()`                    | Decimal                | Lee un número float                                       |
|                    | `readLong()`                     | Entero largo           | Lee un long                                               |
|                    | `readBoolean()`                  | Booleano               | Lee un valor verdadero/falso                              |
|                    | `readChar()`                     | Carácter               | Lee un carácter Unicode                                   |
|                    | `readUTF()`                      | Cadena UTF-8           | Lee una cadena UTF-8                                      |
|                    | `readByte()`                     | Byte                   | Lee un byte                                               |
|                    | `readShort()`                    | Entero corto           | Lee un short                                              |


🖥️ **Ejemplo_binario_estructurado.kt**: Lectura y escritura en ficheros binarios (con tipos primitivos).


Este ejemplo muestra cómo guardar varios datos primitivos en un fichero binario estructurado y recuperarlos después respetando exactamente el mismo orden de escritura.

- Primero se define la ruta del archivo y se crea la carpeta contenedora si no existe.
- Después se abre un `DataOutputStream`, que permite escribir datos primitivos en formato binario.
- A continuación se escriben varios valores de tipos diferentes, como un entero, un número decimal y una cadena corta, todos seguidos dentro del mismo fichero.
- Luego se abre un `DataInputStream` para leer esos datos almacenados.
- La lectura debe hacerse en el mismo orden en que se escribieron; de lo contrario, los valores obtenidos serían incorrectos o se produciría un error.
- Finalmente, los datos leídos se muestran por pantalla para comprobar que el contenido recuperado coincide con el contenido original.

La finalidad del ejercicio es entender que un fichero binario estructurado organiza la información como una secuencia de datos tipados, y que `DataOutputStream` y `DataInputStream` son herramientas muy adecuadas para escribir y leer esa estructura.

        import java.io.DataInputStream
        import java.io.DataOutputStream
        import java.io.FileInputStream
        import java.io.FileOutputStream
        import java.nio.file.Files
        import java.nio.file.Paths

        fun main() {
            val ruta = Paths.get("documentos/binario.dat")
            Files.createDirectories(ruta.parent)

            // Escritura binaria
            val out= DataOutputStream(Files.newOutputStream(ruta)).use { out -> //devuelve OutputStream
            out.writeInt(42)         // int (4 bytes)
            out.writeDouble(3.1416)  // double (8 bytes)
            out.writeUTF("K")       // char (2 bytes)
            }

            println("✅ Fichero binario escrito con DataOutputStream (sin lambda).")

            // Lectura binaria
            val fis = FileInputStream(ruta.toFile())
            val input = DataInputStream(fis)
            val entero = input.readInt()
            val decimal = input.readDouble()
            val caracter = input.readUTF()
            input.close()
            fis.close()

            println("📄 Contenido leído:")
            println("  Int: $entero")
            println("  Double: $decimal")
            println("  Char: $caracter")
        }


