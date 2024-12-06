$(document).ready(function () {
    const user = JSON.parse(sessionStorage.getItem('user'));
    let urlParams = new URLSearchParams(window.location.search);
    let idReview = urlParams.get('idReview');

    if (!idReview) {
        $("#editarReviewForm").text('No se puede editar la reseña porque no se encontró un ID válido.');
        return;
    }

    if (user) {
        $.ajax({
            type: 'GET',
            url: `http://localhost:8080/api/reviews/usuario/${user.id}`,
            dataType: 'json',
            success: reviews => {
                // Filtrar la reseña por idReview
                const review = reviews.find(r => r.id === parseInt(idReview));

                if (review) {
                    // Rellenar los campos del formulario
                    $('#titulo').val(review.titulo);
                    $('#valoracion').val(review.valoracion);
                    $('#comentario').val(review.contenido);
                } else {
                    $("#editarReviewForm").text('No se encontró la reseña especificada.');
                }
            },
            error: error => {
                console.error('Error al cargar las reseñas: ', error);
                $("#editarReviewForm").text('Hubo un error al cargar los datos de la reseña.');
            }
        });
    }

    // Manejar el envío del formulario
    $('#editarComentarioForm').on('submit', function (e) {
        e.preventDefault();

        // Crear el objeto JSON con los datos actualizados
        const reviewData = {
            id: idReview,
            idUsuario: user.id,
            titulo: $('#titulo').val(),
            valoracion: parseInt($('#valoracion').val()),
            contenido: $('#comentario').val()
        };

        // Realizar la solicitud AJAX para actualizar la reseña
        $.ajax({
            type: 'PUT',
            url: `http://localhost:8080/api/reviews/${idReview}`,
            contentType: 'application/json',
            data: JSON.stringify(reviewData),
            headers: { 'Authorization': `Bearer ${user.token}` },
            success: response => {
                window.history.back();
            },
            error: error => {
                console.error('Error al actualizar la reseña: ', error.status, error.statusText);
                alert('Ocurrió un error al actualizar la reseña.');
            }
        });
    });
});
