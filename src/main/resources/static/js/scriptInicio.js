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

    const sec1 = document.querySelector("#sec-1");

    contenido.scrollIntoView({ behavior: 'smooth' });
    sec1.style.display = "flex";

})

const boton2 = document.querySelector("#boton2");
boton2.addEventListener("click",function(){
    limpiarSeccion();

    const sec2 = document.querySelector("#sec-2");
    
    contenido.scrollIntoView({ behavior: 'smooth' });
    sec2.style.display = "flex";

});

const boton3 = document.querySelector("#boton3");
boton3.addEventListener("click",function(){
    limpiarSeccion();

    const sec3 = document.querySelector("#sec-3");
    
    contenido.scrollIntoView({ behavior: 'smooth' });
    sec3.style.display = "block";

});

const boton4 = document.querySelector("#boton4");
boton4.addEventListener("click",function(){
    limpiarSeccion();

    const sec4 = document.querySelector("#sec-4");
    
    contenido.scrollIntoView({ behavior: 'smooth' });
    sec4.style.display = "block";

});

function abrirModal(idTurno,idProf){

    console.log(idTurno);
    console.log(idProf);

    const modalForm = document.querySelector("#form-modal");
    const spanTurno = document.querySelector("#spanTurno");
    const modal = document.querySelector(".modal-turnos");
    const cerrarModal = document.querySelector(".cerrar-modal");

    modalForm.setAttribute('action',`/turno/modificar_turno/${idTurno}/${idProf}`);

    spanTurno.innerHTML = `${idTurno}`;
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




const daysTag = document.querySelector(".days"),
    currentDate = document.querySelector(".current-date"),
    prevNextIcon = document.querySelectorAll(".icons span");
// getting new date, current year and month
let date = new Date(),
    currYear = date.getFullYear(),
    currMonth = date.getMonth();
// storing full name of all months in array
const months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
    "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
const renderCalendar = () => {
    let firstDayofMonth = new Date(currYear, currMonth, 1).getDay(), // getting first day of month
        lastDateofMonth = new Date(currYear, currMonth + 1, 0).getDate(), // getting last date of month
        lastDayofMonth = new Date(currYear, currMonth, lastDateofMonth).getDay(), // getting last day of month
        lastDateofLastMonth = new Date(currYear, currMonth, 0).getDate(); // getting last date of previous month

    let liTag = "";

    for (let i = firstDayofMonth; i > 0; i--) { // creating li of previous month last days
        liTag += `<li class="inactive">${lastDateofLastMonth - i + 1}</li>`;
    }

    for (let i = 1; i <= lastDateofMonth; i++) { // creating li of all days of current month
        // adding active class to li if the current day, month, and year matched
        let isToday = i === date.getDate() && currMonth === new Date().getMonth()
            && currYear === new Date().getFullYear() ? "actual" : "";
        liTag += `<li class="${isToday}" onclick="selectDate(this)">${i}</li>`;
    }

    for (let i = lastDayofMonth; i < 6; i++) { // creating li of next month first days
        liTag += `<li class="inactive">${i - lastDayofMonth + 1}</li>`
    }

    const prevIcon = document.querySelector('#prev');
    const fechaAct = new Date();
    prevIcon.style.display = currMonth === fechaAct.getMonth() ? "none" : "block";

    currentDate.innerText = `${months[currMonth]} ${currYear}`; // passing current mon and yr as currentDate text
    daysTag.innerHTML = liTag;

    const inputHoy = document.querySelector('#hoy');
    inputHoy.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" fill="#1D7874" class="bi bi-circle-fill mb-1" viewBox="0 0 16 16">
    <circle cx="8" cy="8" r="8"/>
  </svg> Fecha de hoy: ${fechaAct.getDate()}/${fechaAct.getMonth() + 1}/${fechaAct.getFullYear()}`;

}

prevNextIcon.forEach(icon => { // getting prev and next icons
    icon.addEventListener("click", () => { // adding click event on both icons
        // if clicked icon is previous icon then decrement current month by 1 else increment it by 1
        currMonth = icon.id === "prev" ? currMonth - 1 : currMonth + 1;
        if (currMonth < 0 || currMonth > 11) { // if current month is less than 0 or greater than 11
            // creating a new date of current year & month and pass it as date value
            date = new Date(currYear, currMonth, new Date().getDate());
            currYear = date.getFullYear(); // updating current year with new date year
            currMonth = date.getMonth(); // updating current month with new date month
        } else {
            date = new Date(); // pass the current date as date value
        }

        renderCalendar(); // calling renderCalendar function
    });
});

renderCalendar();

function selectDate(diaElegido) {

    const fechaActual = new Date().setHours(0, 0, 0, 0);
    const fechaElegida = new Date(currYear, currMonth, diaElegido.textContent);

    if (fechaElegida < fechaActual) {
        alert('No se puede elegir una fecha anterior a la fecha actual.');
    } else if (fechaElegida >= fechaActual) {

        const dias = document.querySelectorAll('.days li');
        const inputSelec = document.querySelector('#selec');
        const inputFecha = document.querySelector('#fecha-seleccionada');

        if (diaElegido.classList.contains('active')) {
            diaElegido.classList.remove('active');
            inputFecha.value = '';
            inputSelec.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" fill="#a068b6" class="bi bi-circle-fill mb-1" viewBox="0 0 16 16">
            <circle cx="8" cy="8" r="8"/>
            </svg> Fecha seleccionada: `;
        } else {

            dias.forEach(dia => {
                dia.classList.remove('active');
            });
            diaElegido.classList.add('active');
            let dia = diaElegido.textContent.toString().padStart(2,"0");
            let mes = (currMonth + 1).toString().padStart(2,"0");
            console.log("mes"+mes)
            let fecha = `${currYear}/${mes}/${dia}`;

            inputSelec.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" fill="#a068b6" class="bi bi-circle-fill mb-1" viewBox="0 0 16 16">
            <circle cx="8" cy="8" r="8"/>
            </svg> Fecha seleccionada: ${dia}/${mes}/${currYear}`;
            inputFecha.value = fecha;
        }

    }
    msjConfirmacion();
}

const horarios = document.querySelectorAll('.item');

horarios.forEach(function (horario) {
    horario.addEventListener('click', function () {

        let horarioSeleccionado = '';

        if (horario.classList.contains('seleccionado')) {
            horario.classList.remove('seleccionado');
            document.querySelector('#horario-seleccionado').value = horarioSeleccionado;
        } else {
            horarios.forEach(horario => { horario.classList.remove('seleccionado') });
            horario.classList.add('seleccionado');
            horarioSeleccionado = horario.textContent;
            document.querySelector('#horario-seleccionado').value = horarioSeleccionado;
        }

        msjConfirmacion();
        console.log(document.querySelector('#horario-seleccionado').value);
    });
});

function msjConfirmacion() {
    const inputFecha = document.querySelector('#fecha-seleccionada');
    const inputHora = document.querySelector('#horario-seleccionado');
    const msjTurno = document.querySelector('.msj-turno');
    const confirmacionContainer = document.querySelector('.confirmacion-container');

    if (inputFecha.value !== '' && inputHora.value !== '') {
        confirmacionContainer.classList.add('mostrar');
        msjTurno.innerHTML = `Turno a crear: ${inputFecha.value}, a las ${inputHora.value} hs.`;
    } else {
        confirmacionContainer.classList.remove('mostrar');
        msjTurno.innerHTML = '';
    }
}



