# Ficheros de imagen

Los ficheros de imagen contienen datos que representan gráficamente una imagen visual (fotografías, ilustraciones, iconos, etc.). A diferencia de los ficheros de texto o binarios crudos, estos archivos tienen estructura interna que depende del formato (como .png, .jpg, .bmp, etc.).


📦 Formatos más comunes

- **.jpg**:	Comprimido con pérdida, ideal para fotos
- **.png**:	Comprimido sin pérdida, soporta transparencia
- **.bmp**:	Sin compresión, ocupa más espacio
- **.gif**:	Admite animaciones simples, limitada a 256 colores
  
En la plataforma Java (y por tanto en Kotlin), **el manejo de imágenes** se hace generalmente usando:

- **ImageIO**: para leer y escribir imágenes
- **BufferedImage**: para acceder y modificar píxeles


| Tipo de fichero           | Lectura                             | Escritura                            | Comentario                                               |
|---------------------------|--------------------------------------|---------------------------------------|----------------------------------------------------------|
| Imagen                 | `ImageIO.read(Path/File)`           | `ImageIO.write(BufferedImage, ...)`   | Usa `javax.imageio.ImageIO`                             |




🖥️ **Ejemplo_generar_imagen.kt:** genera una imagen de ejemplo.

    import java.awt.Color
    import java.awt.image.BufferedImage
    import java.io.File
    import javax.imageio.ImageIO

    fun main() {
        val ancho = 200
        val alto = 100
        val imagen = BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB)

        // Rellenar la imagen con colores
        for (x in 0 until ancho) {
            for (y in 0 until alto) {
                val rojo = (x * 255) / ancho
                val verde = (y * 255) / alto
                val azul = 128
                val color = Color(rojo, verde, azul)
                imagen.setRGB(x, y, color.rgb)
            }
        }

        // Guardar la imagen
        val archivo = File("documentos/imagen_generada.png")
        ImageIO.write(imagen, "png", archivo)
        println("✅ Imagen generada correctamente: ${archivo.absolutePath}")
    }

🖥️ **Ejemplo_invertircolores_imagen.kt:** invierte los colores de la imagen generada en el ejemplo atenerior.

    import java.awt.Color
    import java.awt.image.BufferedImage
    import java.io.File
    import javax.imageio.ImageIO

    fun main() {
        val archivoEntrada = File("documentos/imagen_generada.png")
        val archivoSalida = File("documentos/imagen_salida.png")

        // Leer imagen original
        val imagen: BufferedImage = ImageIO.read(archivoEntrada)

        // Recorrer todos los píxeles
        for (x in 0 until imagen.width) {
            for (y in 0 until imagen.height) {
                val colorOriginal = Color(imagen.getRGB(x, y))
                val colorInvertido = Color(
                    255 - colorOriginal.red,
                    255 - colorOriginal.green,
                    255 - colorOriginal.blue
                )
                imagen.setRGB(x, y, colorInvertido.rgb)
            }
        }

        // Guardar imagen modificada
        ImageIO.write(imagen, "png", archivoSalida)
        println("✅ Imagen guardada como ${archivoSalida.name}")
    }


🖥️ **Ejemplo_img_penyagolosa.kt:** Invierte los colores de una imagen (penyagolosa.png)

    import java.awt.Color
    import java.awt.image.BufferedImage
    import java.nio.file.Files
    import java.nio.file.Path
    import java.nio.file.StandardCopyOption
    import javax.imageio.ImageIO

    fun main() {
        val originalPath = Path.of("documentos/penyagolosa.png")
        val copiaPath = Path.of("documentos/penyagolosa_copia.png")
        val modificadaPath = Path.of("documentos/penyagolosa_modificada.png")

        // 1. Comprobar si la imagen existe
        if (!Files.exists(originalPath)) {
            println("No se encuentra la imagen original: $originalPath")
            return
        }

        // 2. Copiar la imagen con java.nio
        Files.copy(originalPath, copiaPath, StandardCopyOption.REPLACE_EXISTING)
        println("Imagen copiada a: $copiaPath")

        // 3. Leer la imagen como BufferedImage
        val imagen: BufferedImage = ImageIO.read(copiaPath.toFile())

        // 4. Invertir colores
        for (x in 0 until imagen.width) {
            for (y in 0 until imagen.height) {
                val color = Color(imagen.getRGB(x, y))
                val invertido = Color(255 - color.red, 255 - color.green, 255 - color.blue)
                imagen.setRGB(x, y, invertido.rgb)
            }
        }

        // 5. Guardar la imagen modificada
        ImageIO.write(imagen, "png", modificadaPath.toFile())
        println("Imagen modificada guardada como: $modificadaPath")
    }


