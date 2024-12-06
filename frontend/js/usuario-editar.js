$(document).ready(function () {
    const user = JSON.parse(sessionStorage.getItem('user'));

    if (user) {
        // Cargar los datos existentes del usuario
        $.ajax({
            type: 'GET',
            url: `http://localhost:8080/api/usuarios/${user.id}`,
            dataType: 'json',
            headers: { 'Authorization': `Bearer ${user.token}` },
            success: function (usuario) {
                cargarDatosFormulario(usuario);
            },
            error: function (error) {
                console.error('Error al cargar los datos del usuario: ', error);
            }
        });
    } else {
        window.location.href = '../html/login.html';
    }

    $('#editarUsuarioForm').on('submit', function (e) {
        e.preventDefault();

        // Crear un objeto FormData
        var formData = new FormData();

        // Agregar los datos básicos del formulario
        formData.append('nombre', $('#nombre').val());
        formData.append('apellidos', $('#apellidos').val());
        formData.append('email', $('#email').val());

        // Procesar el avatar
        var avatar = $('#avatar')[0].files[0];
        if (avatar) {
            formData.append('avatar', avatar);
        }

        // Enviar la solicitud PUT para actualizar los datos
        $.ajax({
            type: 'PUT',
            url: `http://localhost:8080/api/usuarios/${user.id}`,
            data: formData,
            processData: false, // Evitar que jQuery procese el FormData
            contentType: false, // Dejar que el navegador establezca el content-type correcto
            headers: {
                'Authorization': `Bearer ${user.token}`
            },
            success: function (response) {
                alert('Perfil actualizado con éxito.');
                window.history.back(); // Volver a la página anterior
            },
            error: function (error) {
                console.error('Error al actualizar el perfil: ', error.status, error.statusText);
                alert('Ocurrió un error al actualizar el perfil.');
            }
        });
    });
});

// Función para cargar los datos del usuario en el formulario
function cargarDatosFormulario(usuario) {
    $('#nombre').val(usuario.nombre);
    $('#apellidos').val(usuario.apellidos);
    $('#email').val(usuario.email);

    // Mostrar el avatar actual
    if (usuario.avatar) {
        var avatarContainer = $('<div>').addClass('mb-3 d-flex align-items-center');
        var avatarImg = $('<img>')
            .attr('src', usuario.avatar)
            .attr('alt', 'Avatar del usuario')
            .addClass('img-thumbnail me-2')
            .css({ width: '100px', height: '100px' });
        var deleteButton = $('<button>')
            .addClass('btn btn-danger btn-sm')
            .text('Eliminar')
            .on('click', function () {
                avatarContainer.remove();
                console.log('Eliminar avatar:', usuario.avatar);
            });

        avatarContainer.append(avatarImg, deleteButton);
        $('#avatar').before(avatarContainer);
    }
}
