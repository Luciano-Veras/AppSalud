# Plataforma de Servicios de Salud

## Descripción

Esta aplicación es una plataforma de servicios de salud diseñada para facilitar la interacción entre profesionales de la salud y pacientes. Permite la gestión de perfiles, la reserva de turnos, la documentación de historias clínicas, y más.

## Entidades Principales

### Usuario

- **Atributos:**
  - DNI
  - Contraseña
  - Correo electrónico
  - Activo
  - Rol
  - Fecha de alta
  - Fecha de nacimiento
  - Imagen
  - Teléfono
  - Nombre
  - Apellido
  - Sexo
  - Paciente
  - Profesional

### Turno

- **Atributos:**
  - Fecha del turno
  - Especialidad
  - Motivo de consulta
  - Atendido
  - Cancelado
  - Paciente
  - Profesional

### Profesional

- **Atributos:**
  - Especialidad
  - Turnos disponibles
  - Matrícula
  - Calificaciones
  - Usuario

### Paciente

- **Atributos:**
  - Número de obra social
  - Turnos
  - Grupo familiar
  - Paciente titular
  - Miembros
  - Usuario
  - Calificaciones

### Historia Clínica

- **Atributos:**
  - Fecha de visita
  - Diagnóstico
  - Tratamiento
  - Medicación
  - Indicaciones
  - Estudios
  - Observaciones
  - Paciente
  - Profesional

### Grupo Familiar

- **Atributos:**
  - Parentesco
  - Paciente titular
  - Miembros

### Calificación

- **Atributos:**
  - Puntuación
  - Comentario
  - Fecha de calificación
  - Paciente
  - Profesional

## Funcionalidades Principales

- **Registro y creación de perfiles:**
  - Tanto para profesionales de la salud como para pacientes.
- **Gestión de horarios y turnos:**
  - Diseñada específicamente para profesionales de la salud.
- **Búsqueda y filtrado intuitivo:**
  - Por especialidad y calificación.
- **Creación de fichas de pacientes:**
  - Registro meticuloso de historias clínicas por parte de los profesionales.
- **Interfaz amigable:**
  - Facilita la agendación y gestión de citas médicas.

## Roles de Usuario

- **Administrador:**
  - Puede dar de baja, dar de alta y establecer roles de usuario.

## Ejemplos de Uso java
```java
// Dar de baja un usuario
Administrador.darBajaUsuario(usuario);

// Dar de alta un usuario
Administrador.darAltaUsuario(usuario);
    
// Establecer el rol de un usuario
Administrador.establecerROlUsuario(usuario, "PROFESIONAL");
```

## Estado del Proyecto

Esta versión se encuentra en fase "alpha", por lo que no se recomienda su implementación en entornos de producción. Estamos emocionados de recibir comentarios y sugerencias valiosas de la comunidad de desarrolladores y usuarios.


## Contribución

¡Apreciamos la colaboración de cada miembro del equipo! Cada línea de código, función implementada y decisión de diseño contribuyen al progreso general de la plataforma. Si deseas contribuir, sigue estas [pautas de contribución](https://github.com/nachxg/webAppServiciosSalud/edit/developer/README.md).

