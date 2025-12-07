<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 05/dic/2025
  Time: 05:56 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${paciente != null ? 'Editar' : 'Nuevo'} Paciente - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<!-- Incluir menú lateral -->
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <!-- Header -->
    <jsp:include page="includes/header.jsp" />

    <!-- Contenido Principal -->
    <div class="container">
        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-user-${paciente != null ? 'edit' : 'plus'}"></i>
                    ${paciente != null ? 'Editar Paciente' : 'Nuevo Paciente'}
                </h1>
                <p class="page-subtitle">
                    ${paciente != null ? 'Modifica la información del paciente' : 'Completa los datos del nuevo paciente'}
                </p>
            </div>
            <a href="paciente?action=listar" class="btn btn-outline">
                <i class="fas fa-arrow-left"></i> Volver
            </a>
        </div>

        <!-- Formulario -->
        <div class="card">
            <div class="card-body">
                <form action="paciente" method="post" id="pacienteForm">
                    <input type="hidden" name="action" value="${paciente != null ? 'actualizar' : 'guardar'}">
                    <c:if test="${paciente != null}">
                        <input type="hidden" name="paciente_id" value="${paciente.pacienteId}">
                    </c:if>

                    <!-- Sección: Datos Personales -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-user"></i> Datos Personales
                        </h3>

                        <div class="form-grid">
                            <div class="form-group">
                                <label for="nombres">Nombres *</label>
                                <input type="text"
                                       id="nombres"
                                       name="nombres"
                                       value="${paciente.nombres}"
                                       required
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="apellidos">Apellidos *</label>
                                <input type="text"
                                       id="apellidos"
                                       name="apellidos"
                                       value="${paciente.apellidos}"
                                       required
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="cedula">Cédula *</label>
                                <input type="text"
                                       id="cedula"
                                       name="cedula"
                                       value="${paciente.cedula}"
                                       required
                                       maxlength="10"
                                       pattern="[0-9]{10}"
                                       class="form-control"
                                       placeholder="1234567890">
                            </div>

                            <div class="form-group">
                                <label for="fecha_nacimiento">Fecha de Nacimiento *</label>
                                <input type="date"
                                       id="fecha_nacimiento"
                                       name="fecha_nacimiento"
                                       value="${paciente.fechaNacimiento}"
                                       required
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="genero">Género *</label>
                                <select id="genero" name="genero" required class="form-control">
                                    <option value="">Seleccione...</option>
                                    <option value="M" ${paciente.genero == 'M' ? 'selected' : ''}>Masculino</option>
                                    <option value="F" ${paciente.genero == 'F' ? 'selected' : ''}>Femenino</option>
                                    <option value="Otro" ${paciente.genero == 'Otro' ? 'selected' : ''}>Otro</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="ocupacion">Ocupación</label>
                                <input type="text"
                                       id="ocupacion"
                                       name="ocupacion"
                                       value="${paciente.ocupacion}"
                                       class="form-control">
                            </div>
                        </div>
                    </div>

                    <!-- Sección: Contacto -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-address-book"></i> Información de Contacto
                        </h3>

                        <div class="form-grid">
                            <div class="form-group">
                                <label for="telefono">Teléfono</label>
                                <input type="tel"
                                       id="telefono"
                                       name="telefono"
                                       value="${paciente.telefono}"
                                       class="form-control"
                                       placeholder="0987654321">
                            </div>

                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email"
                                       id="email"
                                       name="email"
                                       value="${paciente.email}"
                                       class="form-control"
                                       placeholder="ejemplo@mail.com">
                            </div>

                            <div class="form-group full-width">
                                <label for="direccion">Dirección</label>
                                <textarea id="direccion"
                                          name="direccion"
                                          rows="2"
                                          class="form-control">${paciente.direccion}</textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Sección: Contacto de Emergencia -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-phone-volume"></i> Contacto de Emergencia
                        </h3>

                        <div class="form-grid">
                            <div class="form-group">
                                <label for="contacto_emergencia">Nombre Completo</label>
                                <input type="text"
                                       id="contacto_emergencia"
                                       name="contacto_emergencia"
                                       value="${paciente.contactoEmergencia}"
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="telefono_emergencia">Teléfono</label>
                                <input type="tel"
                                       id="telefono_emergencia"
                                       name="telefono_emergencia"
                                       value="${paciente.telefonoEmergencia}"
                                       class="form-control">
                            </div>
                        </div>
                    </div>

                    <!-- Sección: Historial Médico -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-notes-medical"></i> Historial Médico
                        </h3>

                        <div class="form-grid">
                            <div class="form-group full-width">
                                <label for="alergias">Alergias</label>
                                <textarea id="alergias"
                                          name="alergias"
                                          rows="2"
                                          class="form-control"
                                          placeholder="Alergias a medicamentos, anestesia, etc.">${paciente.alergias}</textarea>
                            </div>

                            <div class="form-group full-width">
                                <label for="enfermedades_sistemicas">Enfermedades Sistémicas</label>
                                <textarea id="enfermedades_sistemicas"
                                          name="enfermedades_sistemicas"
                                          rows="2"
                                          class="form-control"
                                          placeholder="Diabetes, hipertensión, etc.">${paciente.enfermedadesSistemicas}</textarea>
                            </div>

                            <div class="form-group full-width">
                                <label for="medicamentos_actuales">Medicamentos Actuales</label>
                                <textarea id="medicamentos_actuales"
                                          name="medicamentos_actuales"
                                          rows="2"
                                          class="form-control">${paciente.medicamentosActuales}</textarea>
                            </div>

                            <div class="form-group" id="embarazada-container" style="display: none;">
                                <label class="checkbox-label">
                                    <input type="checkbox"
                                           id="embarazada"
                                           name="embarazada"
                                           value="true"
                                    ${paciente.embarazada ? 'checked' : ''}>
                                    <span>¿Está embarazada?</span>
                                </label>
                            </div>

                            <div class="form-group full-width">
                                <label for="notas">Notas Adicionales</label>
                                <textarea id="notas"
                                          name="notas"
                                          rows="3"
                                          class="form-control">${paciente.notas}</textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Botones -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i>
                            ${paciente != null ? 'Actualizar' : 'Guardar'} Paciente
                        </button>
                        <a href="paciente?action=listar" class="btn btn-outline">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Mostrar campo de embarazo solo si el género es Femenino
    const generoSelect = document.getElementById('genero');
    const embarazadaContainer = document.getElementById('embarazada-container');

    function toggleEmbarazada() {
        if (generoSelect.value === 'F') {
            embarazadaContainer.style.display = 'block';
        } else {
            embarazadaContainer.style.display = 'none';
            document.getElementById('embarazada').checked = false;
        }
    }

    generoSelect.addEventListener('change', toggleEmbarazada);
    toggleEmbarazada(); // Ejecutar al cargar

    // Validación básica del formulario
    document.getElementById('pacienteForm').addEventListener('submit', function(e) {
        const cedula = document.getElementById('cedula').value;
        if (cedula.length !== 10 || !/^\d+$/.test(cedula)) {
            e.preventDefault();
            alert('La cédula debe contener exactamente 10 dígitos numéricos');
            return false;
        }
    });
</script>
</body>
</html>