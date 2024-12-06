$(document).ready(() => {
    let user;
    try {
        user = JSON.parse(sessionStorage.getItem('user'));
        console.log('Usuario en sessionStorage:', user);
    } catch (e) {
        console.error('Error al parsear el usuario de sessionStorage:', e);
        user = null;
    }

    // Define la URL según si el usuario está loggeado o no
    let url = user && user.id
        ? `http://localhost:8080/api/sidrerias/usuario/${user.id}`
        : 'http://localhost:8080/api/sidrerias/';

    console.log(url);
    $.ajax({
        type: 'GET',
        url: url,
        dataType: 'json',
        headers: user && user.token ? { 'Authorization': `Bearer ${user.token}` } : {}, // Agregar token si existe
        success: response => {
            mostrarSidrerias(response, user);
            console.log(JSON.stringify(response));
        },
        error: error => {
            console.error('Error en la petición AJAX: ', error.status, error.statusText);
            console.log(JSON.stringify(error, null, 2));
            $("#sidrerias").text('Error en la petición AJAX: ' + error);
        }
    });
});


function mostrarSidrerias(data, user) {
    let contenedor = $('#sidrerias');
    let row = $('<div>').addClass('row');

    data.forEach(function (sidreria) {
        let col = $('<div>').addClass('col-12 col-sm-6 col-md-4 mb-4');

        // Contenedor principal de la tarjeta
        let sidreriaSection = $('<section>')
            .addClass('sidreria-item border rounded p-3 d-flex flex-column justify-content-between h-100');

        // Botón de favorito
        let btnFavorito = $('<button>')
            .addClass('btn btn-light btn-favorito position-absolute top-0 end-0 m-2 p-0 rounded-circle')
            .attr('data-sidreria-id', sidreria.idSidreria)
            .toggleClass('favorito', sidreria.favorita)
            .html(
                sidreria.favorita
                    ? '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-heart-fill" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"/></svg>'
                    : '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-heart" viewBox="0 0 16 16"><path d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z"/></svg>'
            )
            .click(function (e) {
                e.stopPropagation(); // Evitar que el clic se propague al contenedor
                clickOnGuardarFavorito(sidreria.idSidreria, user, sidreria.favorita, btnFavorito);
            });

        // Contenido de la tarjeta
        let dataSidreria = $('<div>')
            .click(function () {
                clickOnSidreria(sidreria.idSidreria);
            });

        let nombre = $('<h2>')
            .addClass('h5 mb-3 text-truncate')
            .text(sidreria.nombre);

        let ubicacion = $('<p>')
            .html('<strong>Ubicación:</strong> ' + sidreria.ubicacion)
            .addClass('mb-2 small text-muted');

        // Estrellas de valoración
        let valoracion = $('<p>').addClass('mb-2 small text-muted');
        let estrellasContainer = $('<span>');

        let rating = Math.round(sidreria.valoracion * 2) / 2; // Redondear al 0.5 más cercano
        for (let i = 1; i <= 5; i++) {
            if (rating >= i) {
                estrellasContainer.append('<span class="fa fa-star checked"></span>');
            } else if (rating >= i - 0.5) {
                estrellasContainer.append('<span class="fa fa-star-half-o checked"></span>');
                rating = 0; // Asegurarse de que las siguientes estrellas sean vacías
            } else {
                estrellasContainer.append('<span class="fa fa-star"></span>');
            }
        }
        valoracion.append(estrellasContainer);

        let escanciado = $('<p>')
            .html(sidreria.escanciado ? 'Servicio de escanciado' : '')
            .addClass('mb-2 small text-muted');

        let primeraImagen = $('<img>')
            .attr('src', 'http://localhost:8080/'+sidreria.imagenes[0])
            .attr('alt', 'Imagen de ' + sidreria.nombre)
            .addClass('img-fluid mb-3 rounded');

        // Agregar elementos al contenedor principal de la tarjeta
        dataSidreria.append(primeraImagen, nombre, ubicacion, valoracion, escanciado);
        sidreriaSection.append(btnFavorito, dataSidreria);

        // Añadir la sidrería al contenedor de la columna
        col.append(sidreriaSection);

        // Añadir la columna a la fila
        row.append(col);
    });

    // Añadir la fila completa al contenedor principal
    contenedor.append(row);
}



function clickOnSidreria(idSidreria) {
    window.location.href = '../html/sidreria.html?id=' + idSidreria;
}

function clickOnGuardarFavorito(idSidreria, user, esFavorita, btnFavorito) {
    if (user && user.token) { // Verifica que el usuario y el token existan
        const data = {
            idSidreria: idSidreria,
            idUsuario: user.id,
            isAltaFavorito: !esFavorita // Alterna el estado
        };

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/sidrerias/favorito',
            contentType: 'application/json',
            data: JSON.stringify(data),
            headers: { 'Authorization': `Bearer ${user.token}` }, // Incluye el token de autorización
            success: response => {
                // Actualizar el estado del botón en la interfaz
                if (!esFavorita) {
                    btnFavorito.addClass('favorito').html('<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-heart-fill" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M8 1.314C12.438-3.248 23.534 4.735 8 15-7.534 4.736 3.562-3.248 8 1.314z"/></svg>');
                    location.reload();
                } else {
                    btnFavorito.removeClass('favorito').html('<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-heart" viewBox="0 0 16 16"><path d="m8 2.748-.717-.737C5.6.281 2.514.878 1.4 3.053c-.523 1.023-.641 2.5.314 4.385.92 1.815 2.834 3.989 6.286 6.357 3.452-2.368 5.365-4.542 6.286-6.357.955-1.886.838-3.362.314-4.385C13.486.878 10.4.28 8.717 2.01zM8 15C-7.333 4.868 3.279-3.04 7.824 1.143q.09.083.176.171a3 3 0 0 1 .176-.17C12.72-3.042 23.333 4.867 8 15z"/></svg>');
                }
            },
            error: error => {
                console.error('Error al cambiar estado de favorito: ', error.status, error.statusText);
                alert('No se ha podido cambiar el estado de favorito.');
            }
        });
    } else {
        // Redirigir al usuario a la página de inicio de sesión
        window.location.href = '../html/login.html';
    }
}



