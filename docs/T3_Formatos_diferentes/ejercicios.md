# 📝 Ejercicio 2: Conversión y recuperación de una ficha en múltiples formatos



## 📋 Enunciado

Se pide crear un programa que trabaje con una ficha de usuario (`Ficha`: nombre, edad, biografía y preferencias). La información de esta ficha se deberá guardar en **varios formatos distintos** y, posteriormente, leerse y mostrarse por consola.

---

**🔧 Parte 1: Guardar**{.azul}

Crea un objeto **Ficha** como el siguiente:


        val ficha = Ficha(
            nombre = "Ana",
            edad = 30,
            biografia = "Ingeniera de software apasionada por la IA y la educación.",
            preferencias = Preferencias(
                lenguaje = "Kotlin",
                editor = "IntelliJ",
                tema = "oscuro"
            )
        )


Implementa funciones que guarden la información de este objeto en los siguientes formatos, dentro de una carpeta con su nombre (ej. **fichas/ficha_ana**):

1. `biografia.txt` – como texto plano.
2. `general.csv` – en formato CSV.
3. `notas.dat` – binario simple (solo la biografía como bytes).
4. `registro.bin` – binario estructurado (nombre, edad, biografía).
5. `preferencias.json` – en JSON (solo las preferencias).
6. `ficha.xml` – como XML completo.
7. `ficha.obj` – como objeto.

---

**📥 Parte 2: Leer**{.azul}

Implementa funciones para **leer** cada uno de los ficheros anteriores y mostrar la información por consola, asegurándote de que los datos son correctos y se han conservado adecuadamente en todos los formatos.

---

## 🛠️ Requisitos técnicos

* Utiliza `Path`, `Files`, `StandardOpenOption` y otras clases de `java.nio.file`.
* Mantén el código limpio, estructurado y bien comentado.
* Todos los archivos deben almacenarse dentro de `fichas/ficha_<nombre>`.

---

📤 **Ejemplo de salida esperada**

```
    📄 Biografía:
    Ingeniera de software apasionada por la IA y la educación.

    📄 Ficha CSV:
    Ana, 30, Ingeniera de software apasionada por la IA y la educación., Kotlin, IntelliJ, oscuro

    📦 Binario simple:
    Ingeniera de software apasionada por la IA y la educación.

    📦 Binario estructurado:
    Nombre: Ana
    Edad: 30
    Biografía: Ingeniera de software apasionada por la IA y la educación.

    🟨 JSON (preferencias):
    lenguaje → Kotlin
    editor → IntelliJ
    tema → oscuro

    📘 Ficha XML:
    Nombre: Ana
    Edad: 30
    Biografía: Ingeniera de software apasionada por la IA y la educación.
    Preferencias:
    Lenguaje: Kotlin
    Editor: IntelliJ
    Tema: oscuro

    🟨 Objeto:
    Ficha(nombre=Ana, edad=30, biografia=Ingeniera de software apasionada por la IA y la educación., preferencias=Preferencias(lenguaje=Kotlin, editor=IntelliJ, tema=oscuro))

```        

## ✅ Rúbrica de evaluación

| Criterio                                                              | Puntos |
| --------------------------------------------------------------------- | ------ |
| Se crea correctamente la estructura de carpetas                       | 1      |
| Se genera correctamente el archivo de biografía (`.txt`)              | 1      |
| Se genera correctamente el archivo `CSV` manual                       | 1      |
| Se genera correctamente el binario simple (`.dat`)                    | 1      |
| Se genera correctamente el binario estructurado (`.bin`)              | 1      |
| Se genera correctamente el archivo `JSON` con Jackson                 | 1      |
| Se genera correctamente el archivo `XML` con Jackson                  | 1      |
| Se genera correctamente el archivo serializado (`.obj`)               | 1      |
| Se implementan correctamente las funciones de lectura y visualización | 1      |
| Código estructurado, funcional y con buen estilo                      | 1      |
| **Total**                                                             | **10** |

