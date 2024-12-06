$(document).ready(() => {
    const btnMiCuenta = document.getElementById('btn-micuenta');
    const btnMiSidreria = document.getElementById('btn-misidreria');
    const btnLogin = document.getElementById('btn-login');
    const btnSignUp = document.getElementById('btn-signup');
    const btnCerrarSesion = document.getElementById('btn-logout'); 
  
    // Verificar si hay un usuario en sessionStorage
    const user = JSON.parse(sessionStorage.getItem('user'));
  
    if (user) {
      // Ocultar botones de Login y Registro
      btnLogin.style.display = 'none';
      btnSignUp.style.display = 'none';
  
      // Mostrar botones de Mi Cuenta y Cerrar Sesión
      btnMiCuenta.hidden = false;
      btnCerrarSesion.hidden = false;
  
      // Si el rol del usuario es "GERENTE", mostrar también el botón "Mi Sidrería"
      if (user.role === 'GERENTE') {
        btnMiSidreria.hidden = false;
      }
    } else {
      // Usuario no está autenticado, mostrar Login y Registro
      btnLogin.style.display = 'inline-block';
      btnSignUp.style.display = 'inline-block';
  
      // Ocultar otros botones
      btnMiCuenta.hidden = true;
      btnMiSidreria.hidden = true;
      btnCerrarSesion.hidden = true;
    }
  
    // Evento para cerrar sesión
    btnCerrarSesion?.addEventListener('click', () => {
      sessionStorage.removeItem('user'); 
      sessionStorage.removeItem('miSidreria'); 
      window.location.href = '../html/index.html';
    });

})
