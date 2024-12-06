$(document).ready(function () {
    const user = JSON.parse(sessionStorage.getItem('user'));

    if (user) {
        $.ajax({
            type: 'GET',
            url: `http://localhost:8080/api/sidrerias/gerente/${user.id}`,
            dataType: 'json',
            headers: { 'Authorization': `Bearer ${user.token}` },
            success: function (miSidreria) {
                mostrarDetallesMiSidreria(miSidreria, user);
                console.log(miSidreria);
            },
            error: function (error) {
                console.error('Error al cargar la sidrería: ', error);
            }
        });
    }else{
        window.location.href('../html/login.html');
    }
});

function mostrarDetallesMiSidreria(sidreria, user) {
    let contenedor = $("#mi-sidreria");

    let headerContainer = $('<div>')
        .addClass('d-flex justify-content-between align-items-center mb-4');

    let bienvenidaH1 = $('<h1>').text(sidreria.sidreriaDTO.nombre);

    let btnEditar = $('<button>')
        .text("Editar")
        .addClass('btn btn-primary')
        .click(function () {
            editarDatosSidreria(sidreria);
        });

    headerContainer.append(bienvenidaH1, btnEditar);

    let datosSidreria = $('<section>');
    let ubicacion = $('<p>').html('<strong>Ubicación:</strong> ' + sidreria.sidreriaDTO.ubicacion);
    let valoracion = $('<p>').html('<strong>Valoración:</strong> ' + sidreria.sidreriaDTO.valoracion);
    let precio = $('<p>').html('<strong>Precio por comensal:</strong> ' + sidreria.sidreriaDTO.precioComensal + "€");
    let escanciado = $('<p>').html(sidreria.sidreriaDTO.esEscanciado ? 'Servicio de escanciado' : 'No ofrece escanciado');

    let imagenesContainer = $('<div>').addClass('row');
    if (sidreria.sidreriaDTO.imagenes) {
        sidreria.sidreriaDTO.imagenes.forEach(imagenUrl => {
            let col = $('<div>').addClass('col-12 col-sm-4');
            let imagen = $('<img>')
                .attr('src', 'http://localhost:8080/'+imagenUrl)
                .attr('alt', 'Imagen de ' + sidreria.sidreriaDTO.nombre)
                .addClass('img-fluid');
            col.append(imagen);
            imagenesContainer.append(col);
        });
    }

    let carta = $('<a>')
        .attr('href', sidreria.sidreriaDTO.carta)
        .attr('target', '_blank')
        .text('Ver carta')
        .addClass('btn btn-primary mt-4');

    datosSidreria.append(ubicacion, valoracion, precio, escanciado, imagenesContainer, carta);

    let comentariosContainer = $('<section>').addClass('comentarios mt-5 mb-5');
    let tituloComentarios = $('<h2>').text('Reseñas');
    let comentariosRow = $('<div>').addClass('row');

    comentariosContainer.append(tituloComentarios, comentariosRow);

    sidreria.reviews.forEach(comentario => {
        let col = $('<div>').addClass('col-12 col-md-6 mb-4 d-flex');
        let comentarioElement = $('<article>')
            .addClass('comentario-item p-3 border rounded w-100 d-flex flex-column justify-content-between')
            .attr('data-id-comentario', comentario.id); // Añadir el ID como un atributo

        let userContainer = $('<div>').addClass('d-flex align-items-center mb-3');
        let avatar = $('<img>')
            .attr('src', 'http://localhost:8080/'+ comentario.rutaAvatar)
            .attr('alt', 'Avatar de ' + comentario.nombreUsuario)
            .addClass('comentario-avatar rounded-circle me-2');

        let nombreUsuario = $('<h3>')
            .addClass('mb-0')
            .text(comentario.nombreUsuario + ' ' + comentario.apellidosUsuario);

        userContainer.append(avatar, nombreUsuario);

        let comentarioTitulo = $('<h4>').addClass('mt-3').text(comentario.titulo);
        let comentarioValoracion = $('<p>').text('Valoración: ' + comentario.valoracion + ' ⭐');
        let comentarioTexto = $('<p>').text(comentario.contenido);

        let respuestaContainer = $('<div>');

        if (comentario.respuesta) {
            let respuestaTexto = $('<p>')
                .addClass('respuesta-texto bg-light p-2 rounded')
                .text('Respuesta: ' + comentario.respuesta);

            let btnEditarRespuesta = $('<button>')
                .addClass('btn btn-secondary me-2')
                .text('Editar respuesta')
                .click(function () {
                    editarRespuesta(comentario.id, comentario.respuesta, user); // Pasar el `user` aquí
                });

            let btnEliminarRespuesta = $('<button>')
                .addClass('btn btn-danger')
                .text('Eliminar respuesta')
                .click(function () {
                    eliminarRespuesta(comentario.id, user);
                });

            respuestaContainer.append(respuestaTexto, btnEditarRespuesta, btnEliminarRespuesta);
        } else {
            let respuestaForm = $('<form>')
                .addClass('respuesta-form mt-3')
                .on('submit', function (e) {
                    e.preventDefault();
                    enviarRespuesta(comentario.id, $(this).find('textarea').val(), user);
                });

            let respuestaTextarea = $('<textarea>')
                .addClass('form-control mb-2')
                .attr('placeholder', 'Escribe tu respuesta...')
                .attr('required', true);

            let respuestaButton = $('<button>')
                .addClass('btn btn-primary')
                .text('Responder');

            respuestaForm.append(respuestaTextarea, respuestaButton);
            respuestaContainer.append(respuestaForm);
        }

        comentarioElement.append(
            userContainer,
            comentarioTitulo,
            comentarioValoracion,
            comentarioTexto,
            respuestaContainer
        );

        col.append(comentarioElement);
        comentariosRow.append(col);
    });

    contenedor.append(headerContainer, datosSidreria, comentariosContainer);
}




function enviarRespuesta(idReview, respuesta, user) {
    let respuestaData = {
        id: idReview,
        respuesta: respuesta
    };

    $.ajax({
        type: 'PUT',
        url: `http://localhost:8080/api/reviews/${idReview}`,
        contentType: 'application/json',
        data: JSON.stringify(respuestaData),
        headers: { 'Authorization': `Bearer ${user.token}` },
        success: function () {
            location.reload();
        },
        error: function (error) {
            console.error('Error al enviar la respuesta: ', error.status, error.statusText);
            alert('Error al enviar la respuesta. Inténtalo de nuevo.');
        }
    });
}

function editarRespuesta(idComentario, respuestaActual, user) {
    // Selecciona el contenedor del comentario basado en el ID
    let comentarioContainer = $(`[data-id-comentario="${idComentario}"]`);
    let respuestaContainer = comentarioContainer.find('.respuesta-texto').parent(); // Encuentra el contenedor de la respuesta

    // Limpia el contenedor para preparar el área editable
    respuestaContainer.empty();

    // Crea un formulario para editar la respuesta
    let respuestaForm = $('<form>')
        .addClass('respuesta-form mt-3')
        .on('submit', function (e) {
            e.preventDefault();
            let nuevaRespuesta = $(this).find('textarea').val();
            enviarRespuesta(idComentario, nuevaRespuesta, user); // Pasa el `user` al enviar la respuesta
        });

    let respuestaTextarea = $('<textarea>')
        .addClass('form-control mb-2')
        .val(respuestaActual) // Prellena con la respuesta actual
        .attr('required', true);

    let btnGuardar = $('<button>')
        .addClass('btn btn-primary me-2')
        .text('Guardar');

    let btnCancelar = $('<button>')
        .addClass('btn btn-secondary')
        .text('Cancelar')
        .click(function (e) {
            e.preventDefault();
            cancelarEdicionRespuesta(respuestaContainer, respuestaActual, idComentario, user);
        });

    // Agrega los elementos al formulario y luego al contenedor
    respuestaForm.append(respuestaTextarea, btnGuardar, btnCancelar);
    respuestaContainer.append(respuestaForm);
}

function cancelarEdicionRespuesta(respuestaContainer, respuestaActual, idComentario, user) {
    respuestaContainer.empty(); // Limpia el contenedor
    let respuestaTexto = $('<p>')
        .addClass('respuesta-texto bg-light p-2 rounded')
        .text('Respuesta: ' + respuestaActual);

    let btnEditarRespuesta = $('<button>')
        .addClass('btn btn-secondary me-2')
        .text('Editar respuesta')
        .click(function () {
            editarRespuesta(idComentario, respuestaActual, user); // Pasa el `user` aquí
        });

    let btnEliminarRespuesta = $('<button>')
        .addClass('btn btn-danger')
        .text('Eliminar respuesta')
        .click(function () {
            eliminarRespuesta(idComentario);
        });

    respuestaContainer.append(respuestaTexto, btnEditarRespuesta, btnEliminarRespuesta);
}


function eliminarRespuesta(idComentario, user) {
    if (confirm('¿Estás seguro de que quieres eliminar esta respuesta?')) {
        let respuestaData = {
            id: idComentario,
            respuesta: ''
        };

        $.ajax({
            type: 'PUT', 
            url: `http://localhost:8080/api/reviews/${idComentario}`, 
            contentType: 'application/json',
            data: JSON.stringify(respuestaData),
            headers: { 'Authorization': `Bearer ${user.token}` }, 
            success: function () {
                location.reload(); 
            },
            error: function (error) {
                console.error('Error al eliminar la respuesta:', error.status, error.statusText);
                alert('Error al eliminar la respuesta. Inténtalo de nuevo.');
            }
        });
    }
}


function editarDatosSidreria(sidreria) {
    window.location.href = '../html/sidreria-editar.html';
}
