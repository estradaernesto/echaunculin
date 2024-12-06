$(document).ready(function () {
    let urlParams = new URLSearchParams(window.location.search);
    let idSidreria = urlParams.get('id');

    if (idSidreria) {
        // Obtener detalles de la sidrería y sus reseñas
        $.ajax({
            type: 'GET',
            url: `http://localhost:8080/api/sidrerias/${idSidreria}`,
            dataType: 'json',
            success: response => {
                mostrarDetallesSidreria(response.sidreriaDTO, response.reviews);
            },
            error: error => {
                console.error('Error en la petición AJAX: ', error.status, error.statusText);
                $("#sidreria").text('Error en la petición AJAX: ' + error);
            }
        });
    } else {
        $("#sidreria").text("ID de sidrería no válido.");
    }
});

function mostrarDetallesSidreria(sidreria, reviews) {
    let contenedor = $("#sidreria");

    // Detalles de la sidrería
    let nombre = $('<h1>').text(sidreria.nombre);
    let ubicacion = $('<p>').html('<strong>Ubicación:</strong> ' + sidreria.ubicacion);
    
    // Valoración de la sidrería
    let valoracion = $('<p>').html('<strong>Valoración:</strong> ');
    let estrellasSidreria = generarEstrellas(sidreria.valoracion); // Generar estrellas
    valoracion.append(estrellasSidreria);

    let precio = $('<p>').html('<strong>Precio por comensal:</strong> €' + sidreria.precioComensal);
    let escanciado = $('<p>').html(sidreria.esEscanciado ? 'Servicio de escanciado' : 'No ofrece escanciado');

    // Imágenes
    let imagenesContainer = $('<div>').addClass('row mb-4');
    sidreria.imagenes.forEach(imagenUrl => {
        let col = $('<div>').addClass('col-12 col-sm-6 col-md-4 mb-4');
        let imagen = $('<img>')
            .attr('src', 'http://localhost:8080/'+imagenUrl)
            .attr('alt', 'Imagen de ' + sidreria.nombre)
            .addClass('img-fluid rounded');
        col.append(imagen);
        imagenesContainer.append(col);
    });

    // Menú
    let menu = $('<a>')
        .attr('href', sidreria.carta)
        .attr('target', '_blank')
        .text('Ver carta')
        .addClass('btn btn-primary mt-4');

    // Contenedor de reseñas
    let reviewsContainer = $('<div>').addClass('reviews-container mt-5 mb-5');
    let reviewsH2 = $('<h2>').text('Reseñas:');
    let btnComentar = $('<button>')
        .addClass('btn btn-primary mb-4 mt-3')
        .click(function () {
            clickOnCrearReview(sidreria.idSidreria);
        })
        .text('Añadir reseña');
    reviewsContainer.append(reviewsH2, btnComentar);

    if (reviews && reviews.length > 0) {
        let rowReviews = $('<div>').addClass('row');

        reviews.forEach(review => {
            let colReview = $('<div>').addClass('col-12 col-md-6 mb-4 d-flex');
            let reviewElement = $('<article>')
                .addClass('review-item p-3 border rounded w-100');

            // Usuario y avatar
            let userContainer = $('<div>').addClass('d-flex align-items-center mb-3');
            let avatar = $('<img>')
                .attr('src', 'http://localhost:8080/'+review.rutaAvatar) 
                .attr('alt', 'Avatar de ' + review.nombreUsuario)
                .addClass('review-avatar rounded-circle me-2');
            let nombreUsuario = $('<h3>')
                .addClass('mb-0')
                .text(`${review.nombreUsuario} ${review.apellidosUsuario}`);
            userContainer.append(avatar, nombreUsuario);

            // Contenido de la reseña
            let reviewTitulo = $('<h4>').addClass('mt-3').text(review.titulo);
            
            // Valoración de la reseña
            let reviewValoracion = $('<p>');
            let estrellasReview = generarEstrellas(review.valoracion); // Generar estrellas
            reviewValoracion.append(estrellasReview);

            let reviewTexto = $('<p>').text(review.contenido);

            // Respuesta
            let respuestaContainer = $('<div>');
            if (review.respuesta) {
                let respuestaTexto = $('<p>')
                    .addClass('respuesta-texto bg-light p-2 rounded')
                    .text('Respuesta: ' + review.respuesta);
                respuestaContainer.append(respuestaTexto);
            }

            reviewElement.append(userContainer, reviewTitulo, reviewValoracion, reviewTexto, respuestaContainer);
            colReview.append(reviewElement);
            rowReviews.append(colReview);
        });

        reviewsContainer.append(rowReviews);
    } else {
        reviewsContainer.append($('<p>').text('No hay reseñas para esta sidrería.'));
    }

    // Añadir elementos al contenedor principal
    contenedor.append(nombre, ubicacion, valoracion, precio, escanciado, imagenesContainer, menu, reviewsContainer);
}


function generarEstrellas(valoracion) {
    let estrellasContainer = $('<span>');
    let rating = Math.round(valoracion * 2) / 2; // Redondear al 0.5 más cercano
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
    return estrellasContainer;
}

function clickOnCrearReview(idSidreria) {
    if (sessionStorage.getItem('user')) {
        window.location.href = '../html/review-crear.html?id=' + idSidreria;
    } else {
        window.location.href = '../html/login.html';
    }
}
