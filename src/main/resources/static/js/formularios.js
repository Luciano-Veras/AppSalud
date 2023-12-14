document.addEventListener(function () {
  
    const dni = document.getElementById('dni-registro');
    const fecha = document.getElementById('fecha-registro');
    const matricula = document.getElementById('matricula-registro');
    const mensaje = document.getElementById('warnings');

    dni.addEventListener('input', validarDNI);
    fecha.addEventListener('input', validarFecha);
    matricula.addEventListener('input', validarMatricula);

    function validarDNI() {
        if (dni.value.length > 8) {
            mostrarAdvertencia("El DNI no puede tener más de 8 dígitos");
            dni.value = "";
        } else {
            ocultarAdvertencia();
        }
    }

    function validarFecha() {
        const fechaValue = new Date(fecha.value);
        const fechaActual = new Date();

        if (fechaValue > fechaActual) {
            mostrarAdvertencia("La fecha de nacimiento es incorrecta");
            fecha.value = "";
        } else {
            ocultarAdvertencia();
        }
    }

    function validarMatricula() {
       
        const matriculasIngresadas = [];

        const  matriculaValor = matricula.value;

   
    if (matriculasIngresadas.includes(matriculaValor)) {

        mensaje.textContent = "La matricula ya fue ingresada previamente";
        matricula.value = "";

    } else {

        mensaje.textContent = "";
        matriculasIngresadas.push(matriculaValor);
    }
        if (matriculaExiste(matricula.value)) {
            mostrarAdvertencia("La matrícula ya está en uso");
            matricula.value = "";
        } else {
            ocultarAdvertencia();
        }
    }

    function mostrarAdvertencia(mensaje) {
        mensaje.textContent = mensaje;
    }

    function ocultarAdvertencia() {
        mensaje.textContent = "";
    }

    function matriculaExiste(matricula) {
        return false;
    }
});