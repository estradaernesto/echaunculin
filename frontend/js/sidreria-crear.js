$(document).ready(function () {
    const user = JSON.parse(sessionStorage.getItem('user'));
    console.log('Usuario en sessionStorage:', user);

    if (user) {
        $('#crearSidreriaForm').on('submit', function (e) {
            e.preventDefault();

            // Crear un objeto FormData
            let formData = new FormData();

            // Agregar datos del formulario
            let sidreria = {
                nombre: $('#nombre').val(),
                ubicacion: $('#ubicacion').val(),
                precioComensal: parseFloat($('#precioComensal').val()),
                esEscanciado: $('input[name="escanciado"]:checked').val() === "true"
            };

            formData.append("sidreria", JSON.stringify(sidreria));


            // Agregar imágenes
            let nuevasimg = $('#nuevasImagenes')[0].files;
            if (nuevasimg.length > 0) {
                for (let i = 0; i < nuevasimg.length; i++) {
                    formData.append('imagenes', nuevasimg[i]);
                }
            }

            // // Agregar carta (PDF)
            // let carta = $('#carta')[0].files[0];
            // if (carta) {
            //     formData.append('carta', carta);
            // }

            // Enviar la solicitud AJAX
            $.ajax({
                type: 'POST',
                url: `http://localhost:8080/api/sidrerias/gerente/${user.id}`,
                processData: false, 
                contentType: false, 
                data: formData,
                headers: {
                    'Authorization': 'Bearer ' + user.token
                },
                success: response => {
                    alert('Sidrería creada con éxito.');
                    window.location.href = '../html/index.html';
                },
                error: error => {
                    console.error('Error al crear la sidrería: ', error.status, error.statusText);
                    alert('Ocurrió un error al crear la sidrería.');
                }
            });
        });

    } else {
        window.location.href = "../html/index.html";
    }
});

