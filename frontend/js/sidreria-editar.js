$(document).ready(function () {
    const user = JSON.parse(sessionStorage.getItem('user'));

    if (user) {
        // Cargar los datos existentes de la sidrería
        $.ajax({
            type: 'GET',
            url: `http://localhost:8080/api/sidrerias/gerente/${user.id}`,
            dataType: 'json',
            headers: { 'Authorization': `Bearer ${user.token}` },
            success: function (sidreria) {
                cargarDatosFormulario(sidreria);
            },
            error: function (error) {
                console.error('Error al cargar la sidrería: ', error);
            }
        });

    } else {
        window.location.href = '../html/login.html';
    }

    $('#editarSidreriaForm').on('submit', function (e) {
        e.preventDefault();

        // Crear un objeto FormData
        var formData = new FormData();

        // Agregar datos del formulario
        let sidreria = {
            nombre: $('#nombre').val(),
            ubicacion: $('#ubicacion').val(),
            precioComensal: parseFloat($('#precioComensal').val()),
            esEscanciado: $('input[name="escanciado"]:checked').val() === "true"
        };

        formData.append("sidreria", JSON.stringify(sidreria));

        // Procesar imágenes nuevas
        let nuevasimg = $('#nuevasImagenes')[0].files;
        console.log(nuevasimg);
        if (nuevasimg.length > 0) {
            for (let i = 0; i < nuevasimg.length; i++) {
                formData.append('imagenes', nuevasimg[i]);
            }
        }

        // // Procesar el nuevo menú (carta)
        // var nuevaCarta = $('#carta')[0].files[0];
        // if (nuevaCarta) {
        //     formData.append('carta', nuevaCarta);
        // }

        // Enviar la solicitud PUT para actualizar los datos
        $.ajax({
            type: 'PUT',
            url: `http://localhost:8080/api/sidrerias/gerente/${user.id}`,
            data: formData,
            processData: false, 
            contentType: false, 
            headers: {
                'Authorization': `Bearer ${user.token}`
            },
            success: function (response) {
                alert('Sidrería actualizada con éxito.');
                window.history.back(); // Volver a la página anterior
            },
            error: function (error) {
                console.error('Error al actualizar la sidrería: ', error.status, error.statusText);
                alert('Ocurrió un error al actualizar la sidrería.');
            }
        });
    });
});

// Función para cargar los datos de la sidrería en el formulario
function cargarDatosFormulario(sidreria) {
    $('#nombre').val(sidreria.sidreriaDTO.nombre);
    $('#ubicacion').val(sidreria.sidreriaDTO.ubicacion);
    $('#precioComensal').val(sidreria.sidreriaDTO.precioComensal);

    // Preseleccionar la opción de escanciado
    if (sidreria.sidreriaDTO.esEscanciado) {
        $('#escanciadoSi').prop('checked', true);
    } else {
        $('#escanciadoNo').prop('checked', true);
    }

    // Mostrar las imágenes actuales
    var imagenesActuales = $('#imagenesActuales');
    sidreria.sidreriaDTO.imagenes.forEach((imagen, index) => {
        var imgContainer = $('<div>').addClass('mb-2 d-flex align-items-center');
        var img = $('<img>')
            .attr('src', 'http://localhost:8080/'+imagen)
            .attr('alt', 'Imagen de la sidrería')
            .addClass('img-thumbnail me-2')
            .css({ width: '100px', height: '100px' });
        var deleteButton = $('<button>')
            .addClass('btn btn-danger btn-sm')
            .text('Eliminar')
            .on('click', function () {
                imgContainer.remove();
                console.log('Eliminar imagen:', imagen);
            });

        imgContainer.append(img, deleteButton);
        imagenesActuales.append(imgContainer);
    });

    // Mostrar la carta actual (PDF)
    var cartaActual = $('#menuActual');
    if (sidreria.sidreriaDTO.carta) {
        var cartaLink = $('<a>')
            .attr('href', sidreria.sidreriaDTO.carta)
            .attr('target', '_blank')
            .text('Ver carta actual');
        var deleteButton = $('<button>')
            .addClass('btn btn-danger btn-sm ms-2')
            .text('Eliminar')
            .on('click', function () {
                cartaActual.empty();
                console.log('Eliminar carta:', sidreria.sidreriaDTO.carta);
            });

        cartaActual.append(cartaLink, deleteButton);
    } else {
        cartaActual.append($('<p>').text('No hay un menú disponible.'));
    }
}
