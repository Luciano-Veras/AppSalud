document.addEventListener("DOMContentLoaded", function() {
  // Función para cargar los horarios disponibles
  function cargarHorariosDisponibles() {
    // Obtener la fecha seleccionada
    var fechaSeleccionada = document.getElementById('fecha').value;

    // Aquí puedes agregar la lógica para cargar dinámicamente los horarios
    // Por ejemplo, podrías tener una lista de horarios predefinidos o hacer una solicitud al servidor

    // Ejemplo de horarios predefinidos
    var horariosDisponibles = ['09:00 AM', '10:00 AM', '11:00 AM', '02:00 PM', '03:00 PM'];

    // Limpiar la lista de horarios actual
    var listaHorarios = document.getElementById('listaHorarios');
    listaHorarios.innerHTML = '';

    // Agregar los horarios a la lista
    for (var i = 0; i < horariosDisponibles.length; i++) {
      var option = document.createElement('option');
      option.text = horariosDisponibles[i];
      listaHorarios.add(option);
    }
  }

  // Función llamada al hacer clic en el botón de confirmación
  function confirmarTurno() {
    // Obtener la fecha y el horario seleccionados
    var fechaSeleccionada = document.getElementById('fecha').value;
    var horarioSeleccionado = document.getElementById('listaHorarios').value;

    // Aquí puedes agregar la lógica para confirmar el turno seleccionado
    // Por ejemplo, podrías enviar esta información al servidor

    // Ejemplo de alerta para demostración
    alert('Turno confirmado para el ' + fechaSeleccionada + ' a las ' + horarioSeleccionado + '. ¡Gracias!');
  }

  // Evento para cargar los horarios cuando se selecciona una fecha
  document.getElementById('fecha').addEventListener('change', cargarHorariosDisponibles);

  // Llama a la función para cargar los horarios iniciales al cargar la página
  cargarHorariosDisponibles();
});