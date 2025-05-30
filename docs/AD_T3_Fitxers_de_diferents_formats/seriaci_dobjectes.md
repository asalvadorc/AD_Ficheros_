
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
    Kotlin no proporciona ninguna librería adicional para serializar objetos java.  Utiliza exactamente el mismo sistema de serialización binaria de Java, ya que es 100 % compatible.

**Clases y herramientas que se utilizan**{.azul}

|Herramienta|	Uso principal|
|------------|--------------|
|java.io.Serializable|	Marca que un objeto es serializable|
|ObjectOutputStream|	Serializa y escribe un objeto|
|ObjectInputStream|	Lee un objeto serializado|
|transient|	Excluye atributos de la serialización|

!!!Note "Nota"
    La serialización en java sigue necesitando usar las clases de java.io (ObjectOutputStream, ObjectInputStream) porque java.nio no proporciona soporte directo para serialización de objetos.  
    Con java.nio.file.Files y Paths puedes usar Files para escribir directamente un ByteArray generado con la serialización tradicional.

Los Streams **ObjectInputStream** y **ObjectOutputStream** añaden a cualquier otro Stream la capacidad de seriar cualquier objeto Serializable. El stream de salida dispondrá del método **writeObject** y el stream de entrada, el método de lectura **readObject**.

El método **readObject** sólo permite recuperar instancias que sean de la
misma clase que la que se guardó. De lo contrario, se lanzaría una
excepción de tipos **ClassCastExeception**. Además, es necesario que la aplicación disponga del código compilado de la clase; si no fuera así, la excepción lanzada sería
**ClassNotFoundException**.

Los pasos para serializar un objeto java (kotlin) son los siguientes:

1. **Persona.kt**: Crear una clase serializable

        import java.io.Serializable

        class Persona(val nombre: String, val edad: Int) : Serializable

2. **Ejemplo_guardar_persona.kt**: Serializar un objeto a un archivo 

        import java.io.FileOutputStream
        import java.io.ObjectOutputStream

        fun main() {
            val persona = Persona("Ana", 30)

            ObjectOutputStream(FileOutputStream("documentos/persona.obj")).use { salida ->
                salida.writeObject(persona)
            }

            println("Objeto serializado correctamente.")
        }

3. **Ejemplo_leer_persona.kt**: Deserializar un objeto desde un archivo

        import java.io.FileInputStream
        import java.io.ObjectInputStream

        fun main() {
            val persona = ObjectInputStream(FileInputStream("documentos/persona.obj")).use { entrada ->
                entrada.readObject() as Persona
            }

            println("Objeto deserializado:")
            println("Nombre: ${persona.nombre}, Edad: ${persona.edad}")
        }

Si hay atributos que no quieres guardar, usa el modificador @Transient:

    data class Usuario(
        val nombre: String,
        @Transient val clave: String
    ) : Serializable

!!!Tip "Recomendación: serialVersionUID"
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


<!--
La tècnica de la **seriació** és segurament la més senzilla de totes, però
també a la vegada la més problemàtica. Java, i per tant també Kotlin, disposa
d’un sistema genèric de seriació de qualsevol objecte, un sistema **recursiu**
que es repeteix per cada objecte contingut a la instància que s’està seriant.
Aquest procés para en arribar als tipus primitius, els quals es guarden com
una sèrie de bytes. A banda dels tipus primitius, Java serialitza també molta
informació addicional o metadades específiques de cada classe (el nom de les
classe, els noms dels atributs i molta més informació addicional). Gràcies a
les metadades es fa possible automatitzar la seriació de forma genèrica **amb
garanties de recuperar un objecte tal com es va guardar**.

Lamentablement, **aquest és un procediment específic de Java**. És a dir, no
és possible recuperar els objectes seriats des de Java utilitzant un altre
llenguatge. D’altra banda, el fet de guardar metadades pot arribar a comportar
també problemes, encara que utilitzem sempre el llenguatge Java. La
modificació d’una classe pot fer variar les seues metadades. Aquestes
variacions poden donar problemes de recuperació d’instàncies que hagen estat
guardades amb algunes versions anteriors a la modificació, impedint que
l’objecte puga ser recuperat.

Aquestes consideracions fa que no siga pràctica aquesta tècnica per guardar
objectes de forma més o menys permanent. En canvi, la seua senzillesa la fa
una perfecta candidata per a l’emmagatzematge temporal, per exemple dins de la
mateixa sessió.

Per a que un objecte puga ser seriat cal que la seua classe i tot el seu
contingut implementen la interfície **Serializable**. Es tracta d’una
interfície sense mètodes, perquè l’únic objectiu de la interfície és actuar de
marcador per indicar a la màquina virtual quines classes es poden seriar i
quines no.

Totes les classes equivalents als tipus bàsics ja implementen Serializable.
També implementen aquesta interfície la classe String i tots els contenidors i
els objectes Array. La seriació de col·leccions depèn en últim terme dels
elements continguts. Si aquestos són seriables, la col·lecció també ho serà.

En cas que la classe de l’objecte que s’intente seriar, o les d’algun dels
objectes que continga, no implementaren la interfície Serializable, es
llançaria una excepció de tipus **NotSerializableException** , impedint
l’emmagatzematge.

Els Streams **ObjectInputStream** i **ObjectOutputStream** són decoradors que
afegeixen a qualsevol altre Stream la capacitat de seriar qualsevol objecte
Serializable. El stream d'eixida disposarà del mètode **writeObject**. i el
stream d’entrada, el mètode de lectura **readObject**.

El mètode **readObject** només permet recuperar instàncies que siguen de la
mateixa classe que la que es va guardar. En cas contrari, es llançaria una
excepció de tipus **ClassCastExeception**. A més, cal que l’aplicació dispose
del codi compilat de la classe; si no fóra així, l’excepció llançada seria
**ClassNotFoundException**.

**Exemple**

Ens recolzarem en un exemple utilitzat en els anteriors punts, en els
empleats. Ara anem a suposar que els empleats són objectes, i intentarem
guardar aquestos objectes en un fitxer amb una seriació.

El primer pas serà construir la classe **Empleat** , que contindrà la mateixa
informació que en els altres apartats: número d'empleat, nom, departament,
edat i sou.

   
    package exemples
     

    import java.io.Serializable
    
    class Empleat (var num: Int, var nom: String, var departament: Int, var edat: Int, var sou: Double): Serializable
    

Anem a intentar construir el fitxer de dades amb els objectes guardats. El
flux de dades serà un **ObjectOutputStream** per a poder escriure
(**writeObject**). I observeu com s'ha de recolzar en un OutputStream, que en
aquest cas serà d'un fitxer, és a dir un **FileOutputStream**. A cada iteració
del bucle senzillament construirem un objecte de la classe **Empleat** i
l'escriurem al fitxer. Copieu el següent codi en un fitxer Kotlin anomenat
**Exemple_3_3_1_GuardarObjectes.kt**

        
    import java.io.ObjectOutputStream
    import java.io.FileOutputStream
    
    fun main(args: Array<String>) {
    	val f = ObjectOutputStream(FileOutputStream("Empleats.obj"))
    
    	val noms = arrayOf("Andreu", "Bernat", "Clàudia", "Damià")
    	val departaments = arrayOf(10, 20, 10, 10)
    	val edats = arrayOf(32, 28, 26, 40)
    	val sous = arrayOf(1000.0, 1200.0, 1100.0, 1500.0)
    
    	for (i in 0..3){
    		val e = Empleat (i + 1, noms[i], departaments[i], edats[i], sous[i])
    		f.writeObject(e)
    	}
    
    	f.close();
    }

<u>**Nota**</u>
<div style="background-color: #d6eaf8; color: black; padding: 5px;">
El fitxer creat, <b>Empleats.obj</b> , evidentment no és de text. Tanmateix si
l'obrim amb un editor de text podrem veure alguna cosa.
</div>
<p></p>
![](T3_3_1.png)
<div style="background-color: #d6eaf8; color: black; padding: 5px;">
  - La primera qüestió és que es guarda el nom de la classe amb el nom del paquet davant. <b>exemples.Empleat</b> és realment el nom de la classe creada.
  
  - Es guarden també els noms dels camps. Tot això són les metadades que havíem comentat, i que permeten la recuperació posterior dels objectes guardats
  
  - I després ja podem veure la informació guardada, on identifiquem els noms dels empleats
</div>
<p></p>

Per a llegir el fitxer creat, **Empleats.obj** , utilitzarem el
**ObjectInputStream** per a poder fer **readObject**. S'ha de basar en un
InputStream, que en aquest cas serà un **FileInputStream**.

Lamentablement el mètode **available()** no funcionarà correctament, i no ens
dirà realment els bytes que queden per llegir.

El tractament de final de fitxer el farem capturant l'excepció (l'error)
d'haver arribat al final i intentat llegir encara: **EOFException**. La raó és
que **readObject** no torna null, a no ser que s'haja introduït aquest valor.
Per tant muntem un bucle infinit, però capturant amb **try ... catch**
l'error, que és quan tancarem el Stream. Copieu el següent codi a un fitxer
Kotlin anomenat **Exemple_3_3_2_LlegirObjectes.kt**

    
    package exemples
    
    import java.io.ObjectInputStream
    import java.io.FileInputStream
    import java.io.EOFException
    
    fun main(args: Array<String>) {
        val f = ObjectInputStream(FileInputStream("Empleats.obj"))
    
        try {
            while (true) {
                val e = f.readObject() as Empleat
                println("Número: " + e.num)
                println("Nom: " + e.nom)
                println("Departament: " + e.departament)
                println("Edat: " + e.edat)
                println("Sou: " + e.sou)
                println();
            }
        } catch (eof: EOFException) {
            f.close()
        }
    }

<u>**Nota**</u>
<div style="background-color: #d6eaf8; color: black; padding: 5px;">
En realitat, en el fitxer es guarda, a més del nom del paquet i el nom de la
classe, l'identificador de la classe: el <b>serialVersionUID</b> , per a poder
identificar unívocament la classe. Això ens pot donar problemes si intentem
compartir la informació entre nosaltres, ja que perfectament ens pot generar
UID diferents. Per a evitar-lo podríem definir nosaltres aquest
<b>serialVersionUID</b> , i aleshores no hi haurà problemes per a compartir. Fins
i tot serviria per a poder compartir el fitxer d'objectes entre <b>Kotlin</b> i
<b>Java</b>
</div>
<p></p>
        
    package exemples
    
    import java.io.Serializable
    
    class Empleat (var num: Int,var nom: String,var departament: Int,var edat: Int,var sou: Double): Serializable {
        companion object {
            private const val serialVersionUID: Long = 1
        }
    }
<div style="background-color: #d6eaf8; color: black; padding: 5px;">
Si fem aquest canvi en la classe Empleat haurem de tornar a generar el fitxer
(<b>Exemple_3_3_1_GuardarObjectes.kt</b>) abans de poder consultar-lo
(<b>Exemple_3_3_2_LlegirObjectes.kt</b>), perquè a tots els efectes seria una
classe nova.
</div>
<p></p>
    
-->
Llicenciat sota la  [Llicència Creative Commons Reconeixement NoComercial
CompartirIgual 2.5](http://creativecommons.org/licenses/by-nc-sa/2.5/)

