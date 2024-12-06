$(document).ready(function () {
    $('#formularioRegistro').submit(function (event) {
        event.preventDefault();

        let data = {
            nombre: $('#nombre').val(),
            apellidos: $('#apellidos').val(),
            email: $('#email').val(),
            role: $('input[name="role"]:checked').val(),
            password: $('#password').val(),
        };
        let confirmpassword = $('#password-confirmation').val();

        // Validaciones
        const emailPattern = /^\w+([.-_+]?\w+)*@\w+([.-]?\w+)*(\.\w{2,10})+$/;
        const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.])[A-Za-z\d@$!%*?&.]{8,}$/;

        if (!emailPattern.test(data.email)) {
            alert('El correo electrónico no es válido.');
            return;
        }

        if (!passwordPattern.test(data.password)) {
            alert('La contraseña debe tener al menos 8 caracteres, mayúsculas, minúsculas, números y símbolos.');
            return;
        }

        if (data.password !== confirmpassword) {
            alert('Las contraseñas no coinciden.');
            return;
        }

        console.log('Datos enviados:', data);

        // Hacer la solicitud AJAX para registrar al usuario
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/auth/register',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                console.log('Registro response:', response);

                let user = {
                    token: response.accessToken,
                    id: response.id,
                    email: response.email,
                    nombre: response.nombre,
                    apellidos: response.apellidos,
                    role: response.role,
                    rutaAvatar: response.rutaAvatar
                }
                
                sessionStorage.setItem('user', JSON.stringify(user));
                if (user.role === "GERENTE") {
                    window.location.href = "../html/sidreria-crear.html";
                } else {
                    window.location.href = "../html/index.html";
                }


            },
            error: function (xhr, status, error) {
                console.error('Error en el registro:', xhr.responseText);
                alert('Error al registrar usuario: ' + (xhr.responseText || 'No se pudo realizar el registro.'));
            }
        });
    });
});
