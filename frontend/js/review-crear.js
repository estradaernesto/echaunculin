$(document).ready(function () {
    const user = JSON.parse(sessionStorage.getItem('user'));
    const urlParams = new URLSearchParams(window.location.search);
    const idSidreria = urlParams.get('id');
    
    if (user) {
        if (!idSidreria) {
            $("#crearComentarioForm").text('No se puede crear la reseña');
            return;
        }

        $('#crearComentarioForm').on('submit', function (e) {
            e.preventDefault();

            let comentarioData = {
                idUsuario: user.id,
                idSidreria: idSidreria,
                titulo: $('#titulo').val(),
                valoracion: parseInt($('#valoracion').val()),
                contenido: $('#comentario').val(),
                fechaPublicacion: new Date()
            };

            console.log(comentarioData);

            $.ajax({
                type: 'POST',
                url: `http://localhost:8080/api/reviews`,
                contentType: 'application/json',
                data: JSON.stringify(comentarioData),
                headers: { 'Authorization': `Bearer ${user.token}` },
                success: response => {
                    window.location.replace('../html/mi-sidreria.html?' + idSidreria);
                },
                error: error => {
                    console.error('Error al publicar el comentario: ', error.status, error.statusText);
                    alert('Ocurrió un error al publicar el comentario.');
                }
            });
        });
    } else {
        window.location.href = '../html/login.html';
    }
});
