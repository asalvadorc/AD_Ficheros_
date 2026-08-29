# AD - Acceso a Datos - Ficheros

Material docente sobre acceso y gestión de ficheros con Kotlin, publicado como sitio web mediante MkDocs Material.

## Preparar el entorno

Es necesario tener instalado Python 3.12. Desde la carpeta del proyecto, crea un entorno virtual e instala las versiones indicadas en `requirements.txt`:

```powershell
python -m venv .venv
.venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt
```

El entorno virtual evita que las dependencias de este proyecto interfieran con otros programas de Python.

## Ver el sitio en el ordenador

```powershell
mkdocs serve
```

Después, abre la dirección que muestra el terminal, normalmente `http://127.0.0.1:8000`.

## Comprobar el sitio

Antes de subir cambios, se puede ejecutar:

```powershell
mkdocs build --strict
```

Esta comprobación detecta problemas de configuración, páginas inexistentes y enlaces internos incorrectos. La carpeta generada `site/` no se guarda en Git.

## Publicación

Al hacer `push` a las ramas `main` o `master`, GitHub Actions realiza automáticamente estos pasos:

1. Instala las versiones de MkDocs definidas en `requirements.txt`.
2. Valida el sitio en modo estricto.
3. Si la validación termina correctamente, publica la web en GitHub Pages.

Si hay un error, esa versión no se publica y el detalle puede consultarse en la pestaña **Actions** del repositorio de GitHub. No es necesario utilizar pull requests para este flujo.

Los ejercicios 2 y 3 están preparados en el repositorio, pero permanecen deshabilitados en la navegación hasta que estén listos para publicarse.
