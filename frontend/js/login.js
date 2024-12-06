$(document).ready(() => {
    $('#formularioLogin').submit(e => {
        e.preventDefault();
        var email = $('#email').val();
        var password = $('#password').val();
        var emailPattern = /^\w+([.-_+]?\w+)*@\w+([.-]?\w+)*(\.\w{2,10})+$/;

        // Validar que se ingresen valores para usuario y contraseña y que cumplan con las expresiones regulares
        if (email.trim() === '') {
            alert('Por favor introduce el email');
            return;
        } else if (!emailPattern.test(email)) {
            alert('El email no es válido');
            return;
        }

        if (password.trim() === '') {
            alert('Por favor introduce la contraseña.');
            return;
        } 

    
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/auth/login', 
            contentType: 'application/json',
            data: JSON.stringify({ email, password }), 
            success: response => {
                if (response.accessToken) {
                    const user = {
                        token: response.accessToken,
                        id: response.id,
                        email: response.email,
                        nombre: response.nombre,
                        apellidos: response.apellidos,
                        role: response.role,
                        rutaAvatar: response.rutaAvatar
                    }
                    sessionStorage.setItem('user', JSON.stringify(user));
                    window.location.href = '../html/index.html';
                } else {
                    alert('Error en las credenciales.');
                }
            },
            error: error => {
                console.error('Error en la petición:', error);
                alert('Hubo un error al intentar iniciar sesión.');
            }
        });
        
    });
});
