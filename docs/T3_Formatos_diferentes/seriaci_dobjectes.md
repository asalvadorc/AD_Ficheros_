
# 3.- Serialización de Objetos

La serialización de objetos en Java es el proceso de convertir un objeto java en una secuencia de bytes, con el fin de guardarlo en un archivo o base de datos.
  
La deserialización es el proceso inverso: leer esos bytes y reconstruir el objeto.

Para que un objeto pueda ser seriado es necesario que su clase y todo su
contenido implementan la interfaz **Serializable**. Se trata de una
interfaz sin métodos, porque el único objetivo de la interfaz es actuar de
marcador para indicar en la máquina virtual qué clases se pueden seriar y
cuáles no.

Todas las clases equivalentes a los tipos básicos ya implementan serializable.
También implementan esta interfaz la clase String y todos los contenedores y
los objetos Array. La seriación de colecciones depende en último término de los
elementos contenidos. Si éstos son seriables, la colección también lo será.

En caso de que la clase del objeto que se intente seriar, o las de alguno de los
objetos que contenga, no implementaran la interfaz Serializable, se
lanzaría una excepción de tipos **NotSerializableException** , impidiendo
el almacenamiento.

!!!Note ""
    **Kotlin** no proporciona ninguna librería adicional para serializar objetos java.  Utiliza exactamente el mismo sistema de serialización binaria de Java, ya que es 100% compatible.

**Clases y herramientas que se utilizan**{.verde}

|Herramienta|	Uso principal|
|------------|--------------|
|java.io.Serializable|	Marca que un objeto es serializable|
|ObjectOutputStream|	Serializa y escribe un objeto|
|ObjectInputStream|	Lee un objeto serializado|
|transient|	Excluye atributos de la serialización|
|ReadObject| Lee y reconstruye un objeto binario|
|WriteObject| Guarda un objeto como binario|

!!!Note "Nota"
    La serialización en java sigue necesitando usar las clases de **java.io** (ObjectOutputStream, ObjectInputStream) porque java.nio no proporciona soporte directo para serialización de objetos.  
    Con java.nio.file.Files y Paths puedes usar Files para escribir directamente un ByteArray generado con la serialización tradicional.

Los Streams **ObjectInputStream** y **ObjectOutputStream** añaden a cualquier otro Stream la capacidad de seriar cualquier objeto Serializable. El stream de salida dispondrá del método **writeObject** y el stream de entrada, el método de lectura **readObject**.

El método **readObject** sólo permite recuperar instancias que sean de la
misma clase que la que se guardó. De lo contrario, se lanzaría una
excepción de tipos **ClassCastExeception**. Además, es necesario que la aplicación disponga del código compilado de la clase; si no fuera así, la excepción lanzada sería
**ClassNotFoundException**.

!!!warning "Ejemplos"
    Los ejemplos de esta parte se incluirán en el paquete **formatos**, dentro de nuestro proyecto **Ficheros**. 

    ![Ref](paquetes.png)

Los pasos para serializar un objeto java (kotlin) son los siguientes:

🖥️ 1. **Persona.kt**: Crear una clase serializable

        import java.io.Serializable

        class Persona(val nombre: String, val edad: Int) : Serializable

🖥️ 2. **Ejemplo_guardar_persona.kt**: Serializar un objeto a un archivo 

        import java.io.ObjectOutputStream
        import java.nio.file.Files
        import java.nio.file.Paths


        fun main() {

            val persona = Persona("Alicia", 30)

            val path = Paths.get("documentos/persona.obj")
            val objectOut = ObjectOutputStream(Files.newOutputStream(path))
            objectOut.writeObject(persona)
            objectOut.close()

   

            println("Objeto serializado correctamente.")
        }
           


🖥️ 3. **Ejemplo_leer_persona.kt**: Deserializar un objeto desde un archivo

        import java.io.ObjectInputStream
        import java.nio.file.Paths
        import java.nio.file.Files
            
        fun main() {
            val path = Paths.get("documentos/persona.obj")
            val objectIn = ObjectInputStream(Files.newInputStream(path))
            val persona= objectIn.readObject() as Persona
            objectIn.close()

            
            println("Objeto deserializado:")
            println("Nombre: ${persona.nombre}, Edad: ${persona.edad}")
        }


Si hay atributos que no quieres guardar, usa el modificador **@Transient**:

    class Usuario(
        val nombre: String,
        @Transient val clave: String
    ) : Serializable

!!!Tip "Recomendación: serialVersionUID"
    serialVersionUID se utiliza para garantizar la compatibilidad de una clase serializable durante la deserialización, evitando errores tanto cuando se realizan cambios en la definición de la clase como cuando el mismo código se ejecuta en distintos entornos o versiones de Java.
    En realidad, en el archivo se guarda, además del nombre del paquete y el nombre de la clase, el identificador de la clase: el **serialVersionUID**, para poder identificar unívocamente la clase. Esto nos puede dar problemas si intentamos compartir la información entre nosotros, ya que perfectamente puede generar UID diferentes. Para evitarlo, podríamos definir nosotros este serialVersionUID, y entonces no habrá problemas para compartir. Incluso serviría para poder compartir el archivo de objetos entre Kotlin y Java.


Se recomienda declarar la constante **serialVersionUID** de tipo long
para asegurar la compatibilidad de los objetos en distintos sistemas.

La clase **persona.kt** quedaría así:

      import java.io.Serializable

      class Persona(val nombre: String, val edad: Int) : Serializable
       {
            companion object {
                private const val serialVersionUID: Long = 1
            }
       }        



Llicenciat sota la  [Llicència Creative Commons Reconeixement NoComercial
CompartirIgual 2.5](http://creativecommons.org/licenses/by-nc-sa/2.5/)

