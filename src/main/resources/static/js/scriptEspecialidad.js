function mostrarEspecialidades(boton, letra) {
    // Obtén la sección donde mostrarás las especialidades
    var especialidadesContainer = document.getElementById('especialidades-container');
    const contieneLista = document.getElementById('contenedor-lista');
    // Limpia el contenido existente
    contieneLista.innerHTML = '';

    // Crea una lista desordenada para las especialidades
    var listaEspecialidades = document.createElement('ul');
    listaEspecialidades.id = 'lista-especialidades';

    // Aquí podrías tener un array o datos de especialidades que empiezan con la letra seleccionada
    // En este ejemplo, utilizo un array simple como ejemplo
    var especialidades = obtenerEspecialidadesPorLetra(letra);

    // Añade elementos de lista para cada especialidad a la lista
    especialidades.forEach(function (especialidad) {
      var listItem = document.createElement('li');
      var link = document.createElement('a');
      link.textContent = especialidad;

      // Agrega un evento de clic a cada enlace para mostrar la descripción
      link.addEventListener('click', function () {
        mostrarDescripcionEspecialidad(especialidad);
          mostrarDetalles(especialidad)
      });

      listItem.appendChild(link);
      listaEspecialidades.appendChild(listItem);
    });



    // Añade la lista de especialidades al contenedor
    contieneLista.appendChild(listaEspecialidades);

    // Agrega la clase 'centrado' al contenedor
    especialidadesContainer.classList.add('centrado');

    // Cambia el color del botón clickeado
    resetearColoresBotones();
    boton.style.backgroundColor = '#3cd2cb'; // Puedes cambiar 'red' por el color que desees
  }

  function resetearColoresBotones() {
    // Resetea el color de todos los botones a su estado original
    var botones = document.querySelectorAll('#lista-botones button');
    botones.forEach(function (boton) {
      boton.style.backgroundColor = ''; // Puedes cambiar '' por el color original de los botones
    });
  }

  // Esta función debería ser llamada al inicio para establecer el color original de los botones
  resetearColoresBotones();

var especialidadSeleccionada;  // Declara la variable global
function mostrarDescripcionEspecialidad(especialidad) {
    especialidadSeleccionada = especialidad;
    // Puedes personalizar esta función con descripciones específicas para cada especialidad
    var descripcion = obtenerDescripcionEspecialidad(especialidad);

    // Obtén el elemento del párrafo y actualiza su contenido
    var descripcionEspecialidad = document.getElementById('descripcion-especialidad');
    descripcionEspecialidad.textContent = descripcion;
}
function mostrarDetalles(especialidad) {
    // Muestra la descripción de la especialidad
    document.getElementById("descripcion-especialidad").textContent = obtenerDescripcionEspecialidad(especialidad);

    // Muestra el botón "NUESTROS MEDICOS"
    document.getElementById("btnMostrarMedicos").style.display = "block";
    //enviarEspecialidadAlBackend(especialidad);
}

function mostrarMedicos() {

    // Muestra la lista de médicos asociados a la especialidad
    document.getElementById("contenedor-medicos").style.display = "block";
    enviarEspecialidadAlBackend(especialidadSeleccionada);
}
function enviarEspecialidadAlBackend(especialidad) {
    fetch('/profesional/buscar_especialidad', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'termino=' + encodeURIComponent(especialidad),
    })
        .then(response => response.text()) // Convertir la respuesta a texto
        .then(data => {
            // Actualizar el contenido de la página con la respuesta del servidor
            document.body.innerHTML = data;
        })
        .catch(error => {
            console.error('Error al enviar la especialidad al backend:', error);
        });
}







function obtenerDescripcionEspecialidad(especialidad) {
    // Puedes expandir esta función con descripciones específicas para cada especialidad
    switch (especialidad) {
        case 'Alergia e Inmunología':
            return 'El Servicio de Alergia e Inmunología se encarga de la prevención, diagnóstico y tratamiento de las patologías del sistema inmunológico.'
        case 'Anestesiología':
            return 'Se ocupa del proceso quirúrgico completo, teniendo su razón de ser en la atención al paciente quirúrgico en cada etapa de este proceso: desde el momento anterior al ingreso, con la valoración pre-anestésica del riesgo quirúrgico, hasta el postoperatorio inmediato en el que el paciente será trasladado a la sala de hospitalización.'
        case 'Cardiología':
            return 'La cardiología es la rama de la medicina que se encarga del estudio, diagnóstico y tratamiento de las enfermedades del corazón y del aparato circulatorio.';
        case 'Cirugía Cardíaca':
            return 'El Servicio de Cirugía Cardíaca de Sanatorio Vitalis se ocupa del tratamiento de diferentes patologías cardíacas que requieren una solución quirúrgica.'
        case 'Cirugía General':
            return 'Es que es una especialidad médica que se ocupa de la realización de intervenciones quirúrgicas para el tratamiento de diversas enfermedades y condiciones, abarcando un amplio rango de procedimientos en áreas como abdomen, piel, tejidos blandos y procedimientos mínimamente invasivos.'
        case 'Clínica Médica':
            return ' Clínica Médica se enfoca en el diagnóstico y tratamiento de enfermedades en adultos.'
        case 'Cirugía de Tórax':
            return 'Cirugía de Tórax trata condiciones quirúrgicas en la cavidad torácica, incluyendo pulmones y corazón.'
        case 'Diabetes':
            return 'Diabetes es una condición que afecta el manejo del azúcar en la sangre.'
        case 'Endocrinología':
            return 'La Endocrinología se centra en el estudio y tratamiento de desequilibrios hormonales en el cuerpo.'
        case 'Diagnóstico por Imágenes':
            return 'El Diagnóstico por Imágenes es una especialidad médica que utiliza tecnologías de imagen, como radiografías y resonancias magnéticas, para visualizar estructuras internas del cuerpo y diagnosticar diversas condiciones médicas.'
        case 'Geriatría y Gerontología':
            return 'Se dedican al cuidado de la salud de las personas mayores. La Geriatría se enfoca en el tratamiento de enfermedades en esta población, mientras que la Gerontología aborda el envejecimiento desde una perspectiva más amplia, incluyendo aspectos sociales y psicológicos.'
        case 'Ginecología y Obstetricia':
            return 'Se centra en la salud femenina, abordando problemas ginecológicos, mientras que Obstetricia se ocupa del cuidado durante el embarazo y el parto. En conjunto, Ginecología y Obstetricia cubren la salud reproductiva de las mujeres-.'
        case 'Genética Médica Clínica' :
            return 'Genética Médica Clínica se especializa en el diagnóstico y tratamiento de enfermedades genéticas.'
        case 'Hematología':
            return 'Hematología se dedica al estudio y tratamiento de enfermedades de la sangre.'
        case 'Infectología':
            return 'Infectología trata enfermedades infecciosas, causadas por virus, bacterias u otros agentes patógenos.'
        case 'Kinesioterapia Respiratoria':
            return 'Kinesioterapia Respiratoria se enfoca en la rehabilitación y mejora de la función respiratoria a través de ejercicios y técnicas kinésicas.'
        case 'Laboratorio Bioquímico Central':
            return 'Laboratorio Bioquímico Central realiza análisis y pruebas de laboratorio para evaluar parámetros bioquímicos en muestras biológicas.'
        case 'Medicina Transfusional':
            return 'Medicina Transfusional se ocupa de la transfusión de componentes sanguíneos para el tratamiento de diversas condiciones médicas.'
        case 'Medicina Interna Hospitalaria':
            return 'Medicina Interna Hospitalaria se especializa en el diagnóstico y tratamiento de enfermedades complejas en pacientes hospitalizados.'
        case 'Nefrología' :
             return 'Nefrología trata enfermedades relacionadas con los riñones.'
        case 'Neurocirugía' :
            return 'Neurocirugía realiza intervenciones quirúrgicas en el sistema nervioso para tratar diversas condiciones.'
        case 'Neurología':
            return 'Neurología se enfoca en el diagnóstico y tratamiento de trastornos del sistema nervioso.'
        case 'Neumonología':
            return 'Neumonología trata enfermedades respiratorias y del sistema pulmonar.'
        case 'Otorrinolaringología':
            return 'Otorrinolaringología aborda trastornos del oído, nariz, garganta y áreas relacionadas en cabeza y cuello.'
        case 'Oftalmología':
            return 'Oftalmología se especializa en la salud visual y tratamientos oculares.'
        case 'Pediatría':
            return 'Pediatría se dedica al cuidado de la salud de los niños y adolescentes.'
        case 'Oncología Clínica':
            return 'Oncología Clínica trata y gestiona el tratamiento de pacientes con cáncer.'
        case 'Radioterapia':
            return 'Radioterapia utiliza radiación para tratar enfermedades, especialmente cáncer.'
        case 'Salud Mental':
            return 'Salud Mental aborda el bienestar emocional y psicológico de las personas.'
        case 'Terapia Intensiva ':
            return 'Terapia Intensiva se especializa en el cuidado de pacientes críticamente enfermos.'
        case 'Trasplantes':
            return 'Trasplantes realiza procedimientos de trasplante de órganos para salvar vidas.'
        case 'Traumatología y Ortopedia':
            return 'Traumatología y Ortopedia se enfoca en el tratamiento de lesiones y problemas musculoesqueléticos.'
        case 'Trabajo Social':
            return 'Trabajo Social en el ámbito médico aborda aspectos sociales y emocionales relacionados con la salud.'
        case 'Urología':
            return 'Urología trata trastornos del sistema urinario y del sistema reproductor masculino.'
        default:
            return 'Descripción no disponible.';
    }
}

function obtenerEspecialidadesPorLetra(letra) {
    switch (letra) {
        case 'A-B':
            return ['Alergia e Inmunología', 'Anestesiología'];
        case 'C':
            return ['Cardiología', 'Cirugía Cardíaca', 'Cirugía General', 'Clínica Médica', 'Cirugía de Tórax'];
        case 'D-E':
            return ['Diabetes', 'Endocrinología', 'Diagnóstico por Imágenes'];
        case 'F-G':
            return ['Geriatría y Gerontología', 'Ginecología y Obstetricia', 'Genética Médica Clínica'];
        case 'H-I-K':
            return ['Hematología', 'Infectología', 'Kinesioterapia Respiratoria'];
        case 'L-M':
            return ['Laboratorio Bioquímico Central', 'Medicina Transfusional', 'Medicina Interna Hospitalaria'];
        case 'N':
            return ['Nefrología', 'Neurocirugía', 'Neurología', 'Neumonología'];
        case 'O-P':
            return [ 'Otorrinolaringología', 'Oftalmología', 'Pediatría', 'Oncología Clínica'];
        case 'R-S':
            return ['Radioterapia', 'Salud Mental'];
        case 'T-U':
            return ['Terapia Intensiva ', 'Trasplantes', 'Traumatología y Ortopedia', 'Trabajo Social', 'Urología']
        default:
            return [];
    }
}