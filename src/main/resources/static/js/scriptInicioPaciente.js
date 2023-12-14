const contPerfil = document.getElementById("contenedor-perfil");
const icono = document.getElementById("icono");
const desplegable = document.getElementById("desplegable");

let estadoFlecha = false;

contPerfil.addEventListener("click", function () {

    if (!estadoFlecha) {
        contPerfil.classList.add("link-click");
        icono.classList.add("rotar");
        desplegable.style.maxHeight = "20%";
        desplegable.style.visibility = "visible";
        desplegable.style.opacity = "1";
        estadoFlecha = true;
    } else {
        contPerfil.classList.remove("link-click");
        icono.classList.remove("rotar");
        desplegable.style.maxHeight = "0";
        desplegable.style.visibility = "hidden";
        desplegable.style.opacity = "0";
        estadoFlecha = false;
    }

});

const inputContainer = document.querySelector(".consulta-container");
const input = document.createElement('input');
const boton = document.createElement('button');
boton.setAttribute('type','submit');
boton.innerHTML = "Reservar turno";

function obtenerSeleccion(value) {
    let especialidadElegida = value.toString();
    console.log(especialidadElegida);

    var contenedorListas = document.getElementById('contenedor-listas');
    var listasUL = contenedorListas.getElementsByTagName('ul');
    var arrayListas = Array.from(listasUL);

    var listaDeTurnos = arrayListas.map(function (ulElemento) {
        var elementosLi = ulElemento.getElementsByTagName('li');
        var arrayLi = Array.from(elementosLi);

        return {
            id: arrayLi[0].textContent,
            especialidad: arrayLi[1].textContent,
            fechaTurno: arrayLi[2].textContent,
            profesionalACargo: arrayLi[3].textContent,
            cancelado: arrayLi[4].textContent,
            atendido: arrayLi[5].textContent
        };
    });

    // Busca el elemento select existente
    let selectTurnosDisponibles = document.querySelector('.select-turnos-disponibles');

    // Si existe, elimina sus opciones para agregar las nuevas
    if (selectTurnosDisponibles) {
        selectTurnosDisponibles.innerHTML = '';
    } else {
        selectTurnosDisponibles = document.createElement('select');
        let opSelected = document.createElement('option');
        opSelected.selected = true;
        opSelected.text = "Elija una opción";
        opSelected.value = "";
        selectTurnosDisponibles.add(opSelected);
        selectTurnosDisponibles.setAttribute('name', 'idTurno');
        selectTurnosDisponibles.setAttribute('onchange', 'desplegarInput(value)');
        selectTurnosDisponibles.classList.add('select-turnos-disponibles');
    }

    listaDeTurnos.forEach(turno => {
        if (turno.especialidad === especialidadElegida && turno.atendido === 'false' && turno.cancelado === 'false') {
            var opcion = document.createElement('option');
            opcion.value = parseInt(turno.id);

            let [mitadFecha, mitadHora] = turno.fechaTurno.split('T');
            let [anio, mes, dia] = mitadFecha.split('-');
            let fechaFormat = `${dia}/${mes}/${anio}, ${mitadHora}`;

            opcion.text = `Dr. ${turno.profesionalACargo.toUpperCase()}. Día ${fechaFormat} hs.`;
            selectTurnosDisponibles.add(opcion);
        }
    });

    console.log(selectTurnosDisponibles.length);

    let containerSelectTurnos = document.querySelector('.select-turnos-container');
    const sinTurnos = document.querySelector('.sin-turnos');
    // Agrega el select al DOM si tiene opciones, o elimínalo si ya existe
    if (selectTurnosDisponibles.length > 1) {
        sinTurnos.style.display = "none";
        if (!document.querySelector('.select-turnos-disponibles')) {
            containerSelectTurnos.classList.add("mostrar-container");
            containerSelectTurnos.appendChild(selectTurnosDisponibles);
        }

    } else {
        // Elimina el select si no hay opciones
        sinTurnos.style.display = "block";
        if (document.querySelector('.select-turnos-disponibles')) {
            document.querySelector('.select-turnos-disponibles').remove();
            containerSelectTurnos.classList.remove("mostrar-container");
            inputContainer.classList.remove("mostrar-container");
            boton.remove();
            input.remove();
        }
    }
}

const botonContainer = document.querySelector('.boton-container');
const formularioTomarTurno = document.querySelector('.form-tomar-turno')

function desplegarInput(value) {
    if(value != ""){
        inputContainer.appendChild(input);
        input.setAttribute('name','motivoConsulta');
        input.setAttribute('id','motivo-consulta');
        input.required = "true";
        inputContainer.classList.add("mostrar-container");
        botonContainer.appendChild(boton);
        botonContainer.style.opacity = 1;
        let idPaciente = document.querySelector('#idPaciente').textContent;
        formularioTomarTurno.setAttribute('action',`/paciente/tomarTurno/${idPaciente}/${value}/`);
    } else {
        inputContainer.removeChild(input);
        inputContainer.classList.remove("mostrar-container");
        botonContainer.style.opacity = 0;
        document.querySelector('.boton-container').removeChild(boton);
    }
}

const contenido = document.querySelector(".contenido");

function limpiarSeccion() {

    const tarjetaContenido = document.querySelector(".tarjeta-contenido");
    const secciones = Array.from(tarjetaContenido.children);

    secciones.forEach(seccion => {
        seccion.style.display = "none";
    });

    const parrafoClick = document.querySelector("#parrafo-click");

    if(parrafoClick.classList.contains("d-block")){
        parrafoClick.classList.replace("d-block","d-none");
    }

}

const boton1 = document.querySelector("#boton1");
boton1.addEventListener("click",function(){

    limpiarSeccion();

    const sec1 = document.querySelector("#sec1");

    contenido.scrollIntoView({ behavior: 'smooth' });
    sec1.style.display = "flex";

})

const boton2 = document.querySelector("#boton2");
boton2.addEventListener("click",function(){
    limpiarSeccion();

    const sec2 = document.querySelector("#sec2");

    contenido.scrollIntoView({ behavior: 'smooth' });
    sec2.style.display = "flex";

});

const boton3 = document.querySelector("#boton3");
boton3.addEventListener("click",function(){
    limpiarSeccion();

    const sec3 = document.querySelector("#sec3");

    contenido.scrollIntoView({ behavior: 'smooth' });
    sec3.style.display = "flex";

});

function abrirModal(){

    const modal = document.querySelector(".modal-familiar");
    const cerrarModal = document.querySelector(".cerrar-modal");

    modal.style.display = "block";

    cerrarModal.addEventListener("click", function(){
        modal.style.display = "none";
    });

    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
}