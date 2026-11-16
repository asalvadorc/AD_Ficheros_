(() => {
    const normalizar = (texto) => texto.normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .toLowerCase().replace(/^[^a-z0-9]+/, "").trim();
    const clave = /^(nombre|codigo|metodo|funcion|clase|propiedad|anotacion|parametro|campo|herramienta|palabra clave|operacion|configuracion|sentencia|comando|api|tipo de dato|tipo$|que devuelve|devuelve|tipo devuelto|tipo de retorno|retorno|usa)(\b|s\b)/;
    const descripcion = /^(descripcion|criterio|aspecto|situacion|si necesitas|necesidad|uso|objetivo|para que|cuando)(\b|s\b)/;

    function prepararTablas() {
        // Esta página mantiene la distribución de tablas anterior al ajuste.
        const titulo = document.querySelector(".md-content__inner h1");
        if (titulo && normalizar(titulo.textContent).startsWith("conversion entre formatos de ficheros")) return;
        document.querySelectorAll(".md-typeset table").forEach((tabla) => {
            if (tabla.hasAttribute("data-tabla-legible") || tabla.closest(".image-pair-table")) return;
            const cabecera = tabla.tHead?.rows[0];
            if (!cabecera || Array.from(cabecera.cells).some((celda) => celda.colSpan !== 1)) return;
            const titulos = Array.from(cabecera.cells, (celda) => normalizar(celda.textContent));
            const indices = new Set();
            titulos.forEach((titulo, indice) => {
                if (clave.test(titulo)) indices.add(indice);
            });
            // La primera columna se conserva, salvo que sea una descripción.
            if (!descripcion.test(titulos[0])) indices.add(0);
            tabla.setAttribute("data-tabla-legible", "true");
            Array.from(tabla.rows).forEach((fila) => {
                let columna = 0;
                Array.from(fila.cells).forEach((celda) => {
                    if (celda.colSpan === 1 && indices.has(columna)) celda.classList.add("tabla-col-clave");
                    columna += celda.colSpan;
                });
            });
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", prepararTablas, { once: true });
    } else {
        prepararTablas();
    }
    // MkDocs Material reutiliza el documento al navegar entre temas.
    if (typeof document$ !== "undefined") document$.subscribe(prepararTablas);
})();
