$(document).ready(function () {
    const user = JSON.parse(sessionStorage.getItem('user'));

    if (user) {
        mostrarDetallesUsuario(user);
    } else {
        window.location.href = '../html/index.html';
    }
});

function mostrarDetallesUsuario(user) {
    let contenedor = $("#mi-cuenta");

    // SECCIÓN DATOS USUARIO
    let userContainer = $('<div>').addClass('d-flex justify-content-between align-items-center mb-4');

    let avatar = $('<img>')
        .attr('src', 'http://localhost:8080/' + user.rutaAvatar)
        .attr('alt', 'Avatar de ' + user.nombre)
        .addClass('rounded-circle me-3')
        .css({ width: '70px', height: '70px' });

    let bienvenidaH1 = $('<h1>').text(user.nombre + " " + user.apellidos);

    // Botón Editar con alineación a la derecha
    let btnEditar = $('<button>')
        .text("Editar")
        .addClass('btn btn-primary ms-auto')  // `ms-auto` para mover el botón a la derecha
        .click(function () {
            editarDatosUsuario(user);
        });

    // Añadir el avatar, el nombre y el botón de editar al contenedor
    userContainer.append(avatar, bienvenidaH1, btnEditar);

    // SECCIÓN DE FAVORITOS
    let favoritosSection = $('<section>').addClass('mb-5');
    let favoritosH2 = $('<h2>').text('Mis favoritos:');
    favoritosSection.append(favoritosH2);

    let favoritosContainer = $('<div>').addClass('row');

    $.ajax({
        type: 'GET',
        url: `http://localhost:8080/api/sidrerias/usuario/${user.id}`,
        dataType: 'json',
        headers: {
            'Authorization': 'Bearer ' + user.token
        },
        success: sidreriasResponse => {
            let favoritas = sidreriasResponse.filter(s => s.favorita == true);
            favoritas.forEach(function (fav) {
                let col = $('<div>').addClass('col-12 col-sm-6 col-md-4 mb-4');

                let favoritoItem = $('<article>')
                    .addClass('p-3 border rounded h-100 d-flex flex-column justify-content-between favoritos-item')
                    .attr('data-id', fav.idSidreria)
                    .click(function () {
                        clickOnSidreria(fav.idSidreria);
                    });

                let nombre = $('<h3>').addClass('mb-3').text(fav.nombre);
                let ubicacion = $('<p>').html('<strong>Ubicación:</strong> ' + fav.ubicacion);

                // Valoración con estrellas
                let valoracion = $('<p>').html('<strong>Valoración:</strong> ');
                let estrellasFavorito = generarEstrellas(fav.valoracion);
                valoracion.append(estrellasFavorito);

                let primeraImagen = $('<img>')
                    .attr('src', 'http://localhost:8080/' + fav.imagenes[0])
                    .attr('alt', 'Imagen de ' + fav.nombre)
                    .addClass('img-fluid rounded');

                favoritoItem.append(primeraImagen, nombre, ubicacion, valoracion);
                col.append(favoritoItem);
                favoritosContainer.append(col);
            });
        },
        error: error => {
            console.error('Error en la petición AJAX: ', error.status, error.statusText);
        }
    });

    favoritosSection.append(favoritosContainer);

    // SECCIÓN DE REVIEWS
    let reviewsSection = $('<section>').addClass('mb-5');
    let reviewsH2 = $('<h2>').text('Mis reseñas:');
    reviewsSection.append(reviewsH2);

    let reviewsContainer = $('<div>').addClass('row');

    $.ajax({
        type: 'GET',
        url: `http://localhost:8080/api/reviews/usuario/${user.id}`,
        dataType: 'json',
        success: reviews => {
            console.log(reviews);
            if (reviews != undefined) {
                reviews.forEach(function (review) {
                    let col = $('<div>').addClass('col-12 col-md-6 mb-4');

                    let reviewItem = $('<article>')
                        .addClass('p-3 border rounded h-100 d-flex flex-column reviews-item');

                    let sidreriaNombre = $('<h3>').addClass('mb-3').text(review?.nombreSidreria || 'Sidrería desconocida');
                    let reviewTitulo = $('<h4>').addClass('mb-2').text(review.titulo);

                    // Valoración con estrellas
                    let reviewValoracion = $('<p>');
                    let estrellasReview = generarEstrellas(review.valoracion);
                    reviewValoracion.append(estrellasReview);

                    let reviewTexto = $('<p>').text(review.contenido);
                    let reviewFecha = $('<p>').text(review.fechaPublicacion);

                    reviewItem.append(sidreriaNombre, reviewTitulo, reviewValoracion, reviewTexto, reviewFecha);

                    if (review.respuesta) {
                        // Si la reseña tiene una respuesta, se crea un recuadro con la respuesta
                        let respuestaContainer = $('<div>')
                            .addClass('respuesta-texto  p-2 rounded')
                            .text('Respuesta de la sidrería: ' + review.respuesta);

                        reviewItem.append(respuestaContainer);

                    } else {
                        let btnContainer = $('<div>').addClass('d-flex align-items-center mb-4');
                        let btnEditar = $('<button>')
                            .text('Editar')
                            .addClass('btn btn-secondary me-2 mt-3')
                            .click(function () {
                                editarReview(review.id);
                            });

                        let btnEliminar = $('<button>')
                            .text('Eliminar')
                            .addClass('btn btn-danger mt-3')
                            .click(function () {
                                eliminarReview(review.id);
                            });
                        btnContainer.append(btnEditar, btnEliminar);
                        reviewItem.append(btnContainer);
                    }

                    col.append(reviewItem);
                    reviewsContainer.append(col);
                });
            } else {
                reviewsContainer.append(
                    $('<div>').addClass('col-12').append(
                        $('<p>').text('No has realizado reseñas aún.')
                    )
                );
            }
        },
        error: error => {
            console.error('Error al cargar las reseñas: ', error);
        }
    });

    reviewsSection.append(reviewsContainer);

    // Agregar al contenedor principal
    contenedor.append(userContainer, favoritosSection, reviewsSection);
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

function clickOnSidreria(idSidreria) {
    window.location.href = '../html/sidreria.html?id=' + idSidreria;
}

function editarReview(idreview, idSidreria) {
    window.location.href = '../html/review-editar.html?idReview=' + idreview;
}

function eliminarReview(idreview) {
    if (confirm('¿Estás seguro de que quieres eliminar este review?')) {
        $.ajax({
            type: 'POST',
            url: '../json/reviews_eliminar.php',
            data: JSON.stringify({ id: idreview }),
            contentType: 'application/json',
            success: function () {
                alert('Review eliminado con éxito.');
                location.reload();
            },
            error: function (error) {
                console.error('Error al eliminar el review:', error);
                alert('No se pudo eliminar el review.');
            }
        });
    }
}

function editarDatosUsuario(user) {
    window.location.href = '../html/usuario-editar.html';
}
