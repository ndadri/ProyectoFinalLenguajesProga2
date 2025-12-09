<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 10:46 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Consulta" %>
<%@ page import="models.Paciente" %>
<%@ page import="models.Odontologo" %>
<%@ page import="java.util.List" %>
<!-- Vista: consulta-form.jsp
Propósito: Formulario para registrar o editar una consulta clínica.
Variables/atributos esperados:
- consulta (models.Consulta) (opcional) -> modo edición si está presente.
- pacientes (List<models.Paciente>)
- odontologos (List<models.Odontologo>)
Validaciones destacadas:
- Campos obligatorios: paciente, odontólogo, motivo, diagnóstico, tratamiento.
- El campo paciente puede estar deshabilitado en edición (se incluye hidden con el id).
Notas:
- Envía POST al servlet 'consulta' con action 'guardar' o 'actualizar'.
-->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("consulta") != null ? "Editar" : "Nueva" %> Consulta - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="includes/header.jsp" />

    <div class="container">
        <%
            Consulta consulta = (Consulta) request.getAttribute("consulta");
            @SuppressWarnings("unchecked")
            List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
            @SuppressWarnings("unchecked")
            List<Odontologo> odontologos = (List<Odontologo>) request.getAttribute("odontologos");
        %>

        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-stethoscope"></i>
                    <%= consulta != null ? "Editar Consulta" : "Registrar Nueva Consulta" %>
                </h1>
                <p class="page-subtitle">
                    <%= consulta != null ? "Modifica los datos de la consulta" : "Completa el historial clínico del paciente" %>
                </p>
            </div>
            <a href="consulta?action=listar" class="btn btn-outline">
                <i class="fas fa-arrow-left"></i> Volver
            </a>
        </div>

        <div class="card">
            <div class="card-body">
                <form action="consulta" method="post" id="consultaForm">
                    <input type="hidden" name="action" value="<%= consulta != null ? "actualizar" : "guardar" %>">
                    <% if (consulta != null) { %>
                    <input type="hidden" name="consulta_id" value="<%= consulta.getConsultaId() %>">
                    <% } %>

                    <!-- Información del Paciente y Odontólogo -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-user-injured"></i> Información del Paciente
                        </h3>

                        <div class="form-grid">
                            <div class="form-group">
                                <label for="paciente_id">Paciente *</label>
                                <select id="paciente_id" name="paciente_id" required class="form-control"
                                        <%= consulta != null ? "disabled" : "" %>>
                                    <option value="">Seleccione un paciente...</option>
                                    <%
                                        if (pacientes != null) {
                                            for (Paciente p : pacientes) {
                                    %>
                                    <option value="<%= p.getPacienteId() %>"
                                            <%= consulta != null && consulta.getPacienteId() == p.getPacienteId() ? "selected" : "" %>>
                                        <%= p.getNombreCompleto() %> - <%= p.getCedula() %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                                <% if (consulta != null) { %>
                                <input type="hidden" name="paciente_id" value="<%= consulta.getPacienteId() %>">
                                <% } %>
                            </div>

                            <div class="form-group">
                                <label for="odontologo_id">Odontólogo Tratante *</label>
                                <select id="odontologo_id" name="odontologo_id" required class="form-control">
                                    <option value="">Seleccione un odontólogo...</option>
                                    <%
                                        if (odontologos != null) {
                                            for (Odontologo o : odontologos) {
                                    %>
                                    <option value="<%= o.getOdontologoId() %>"
                                            <%= consulta != null && consulta.getOdontologoId() == o.getOdontologoId() ? "selected" : "" %>>
                                        Dr(a). <%= o.getNombres() %> <%= o.getApellidos() %> - <%= o.getEspecialidadNombre() %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- Motivo y Síntomas -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-clipboard-list"></i> Motivo de Consulta y Síntomas
                        </h3>

                        <div class="form-grid">
                            <div class="form-group full-width">
                                <label for="motivo_consulta">Motivo de la Consulta *</label>
                                <textarea id="motivo_consulta"
                                          name="motivo_consulta"
                                          rows="3"
                                          required
                                          class="form-control"
                                          placeholder="¿Por qué acude el paciente a consulta?"
                                ><%= consulta != null && consulta.getMotivoConsulta() != null ? consulta.getMotivoConsulta() : "" %></textarea>
                            </div>

                            <div class="form-group full-width">
                                <label for="sintomas">Síntomas Presentados</label>
                                <textarea id="sintomas"
                                          name="sintomas"
                                          rows="3"
                                          class="form-control"
                                          placeholder="Describe los síntomas que presenta el paciente..."
                                ><%= consulta != null && consulta.getSintomas() != null ? consulta.getSintomas() : "" %></textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Diagnóstico -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-diagnoses"></i> Diagnóstico
                        </h3>

                        <div class="form-grid">
                            <div class="form-group full-width">
                                <label for="diagnostico">Diagnóstico *</label>
                                <textarea id="diagnostico"
                                          name="diagnostico"
                                          rows="4"
                                          required
                                          class="form-control"
                                          placeholder="Diagnóstico odontológico detallado..."
                                ><%= consulta != null && consulta.getDiagnostico() != null ? consulta.getDiagnostico() : "" %></textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Tratamiento -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-tooth"></i> Tratamiento Realizado
                        </h3>

                        <div class="form-grid">
                            <div class="form-group full-width">
                                <label for="tratamiento">Tratamiento *</label>
                                <textarea id="tratamiento"
                                          name="tratamiento"
                                          rows="4"
                                          required
                                          class="form-control"
                                          placeholder="Describe el tratamiento realizado..."
                                ><%= consulta != null && consulta.getTratamiento() != null ? consulta.getTratamiento() : "" %></textarea>
                            </div>

                            <div class="form-group">
                                <label for="dientes_tratados">Dientes Tratados</label>
                                <input type="text"
                                       id="dientes_tratados"
                                       name="dientes_tratados"
                                       value="<%= consulta != null && consulta.getDientesTratados() != null ? consulta.getDientesTratados() : "" %>"
                                       class="form-control"
                                       placeholder="Ej: 11, 12, 21">
                                <small style="color: #6B7280;">Usa numeración FDI separada por comas</small>
                            </div>

                            <div class="form-group">
                                <label for="procedimientos">Procedimientos</label>
                                <input type="text"
                                       id="procedimientos"
                                       name="procedimientos"
                                       value="<%= consulta != null && consulta.getProcedimientos() != null ? consulta.getProcedimientos() : "" %>"
                                       class="form-control"
                                       placeholder="Ej: Limpieza, Obturación">
                            </div>
                        </div>
                    </div>

                    <!-- Observaciones y Pronóstico -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-notes-medical"></i> Observaciones y Seguimiento
                        </h3>

                        <div class="form-grid">
                            <div class="form-group full-width">
                                <label for="observaciones">Observaciones</label>
                                <textarea id="observaciones"
                                          name="observaciones"
                                          rows="3"
                                          class="form-control"
                                          placeholder="Observaciones adicionales..."
                                ><%= consulta != null && consulta.getObservaciones() != null ? consulta.getObservaciones() : "" %></textarea>
                            </div>

                            <div class="form-group">
                                <label for="pronostico">Pronóstico</label>
                                <select id="pronostico" name="pronostico" class="form-control">
                                    <option value="">Seleccione...</option>
                                    <option value="Excelente" <%= consulta != null && "Excelente".equals(consulta.getPronostico()) ? "selected" : "" %>>Excelente</option>
                                    <option value="Bueno" <%= consulta != null && "Bueno".equals(consulta.getPronostico()) ? "selected" : "" %>>Bueno</option>
                                    <option value="Regular" <%= consulta != null && "Regular".equals(consulta.getPronostico()) ? "selected" : "" %>>Regular</option>
                                    <option value="Reservado" <%= consulta != null && "Reservado".equals(consulta.getPronostico()) ? "selected" : "" %>>Reservado</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="proxima_cita">Próxima Cita</label>
                                <input type="date"
                                       id="proxima_cita"
                                       name="proxima_cita"
                                       value="<%= consulta != null && consulta.getProximaCita() != null ? consulta.getProximaCita() : "" %>"
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label class="checkbox-label">
                                    <input type="checkbox"
                                           id="requiere_seguimiento"
                                           name="requiere_seguimiento"
                                        <%= consulta != null && consulta.isRequiereSeguimiento() ? "checked" : "" %>>
                                    <span>Requiere Seguimiento</span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <!-- Botones -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i>
                            <%= consulta != null ? "Actualizar" : "Guardar" %> Consulta
                        </button>
                        <a href="consulta?action=listar" class="btn btn-outline">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Validación del formulario
    document.getElementById('consultaForm').addEventListener('submit', function(e) {
        const motivo = document.getElementById('motivo_consulta').value.trim();
        const diagnostico = document.getElementById('diagnostico').value.trim();
        const tratamiento = document.getElementById('tratamiento').value.trim();

        if (!motivo || !diagnostico || !tratamiento) {
            e.preventDefault();
            alert('Por favor, completa todos los campos obligatorios');
            return false;
        }
    });
</script>
</body>
</html>