Formas de acceso a un fichero


 Existen dos formas principales de acceder a un fichero:

  1. Acceso secuencial

- Los datos se procesan en orden, desde el principio hasta el final del fichero.
- Es el más común y sencillo.
- Se usa cuando se desea leer todo el contenido o recorrer registro por registro.

Ejemplos: lectura de un archivo de texto línea por línea, o de un fichero binario estructurado registro a registro. 


 2. Acceso aleatorio (directo)

- Permite saltar a una posición concreta del fichero sin necesidad de leer lo anterior.
- Es útil cuando los registros tienen un tamaño fijo y se necesita eficiencia (por ejemplo, ir directamente al registro 100).
- Requiere técnicas más avanzadas como el uso de FileChannel, SeekableByteChannel o RandomAccessFile.


En función del tipo de contenido (texto plano, binario sin estructura, binario estructurado, imagen, etc.), se utilizan diferentes clases y métodos, que se resumen en la siguiente tabla:
