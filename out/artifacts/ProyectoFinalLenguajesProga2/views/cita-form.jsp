<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 10:35 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Cita" %>
<%@ page import="models.Paciente" %>
<%@ page import="models.Odontologo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("cita") != null ? "Editar" : "Nueva" %> Cita - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="includes/header.jsp" />

    <div class="container">
        <%
            Cita cita = (Cita) request.getAttribute("cita");
            @SuppressWarnings("unchecked")
            List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
            @SuppressWarnings("unchecked")
            List<Odontologo> odontologos = (List<Odontologo>) request.getAttribute("odontologos");

            // Valores por defecto
            String fechaDefault = "";
            String horaDefault = "";

            if (cita != null && cita.getFechaHora() != null) {
                LocalDateTime ldt = cita.getFechaHora().toLocalDateTime();
                fechaDefault = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                horaDefault = ldt.format(DateTimeFormatter.ofPattern("HH:mm"));
            }
        %>

        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-calendar-plus"></i>
                    <%= cita != null ? "Editar Cita" : "Agendar Nueva Cita" %>
                </h1>
                <p class="page-subtitle">
                    <%= cita != null ? "Modifica los datos de la cita" : "Completa la información para agendar la cita" %>
                </p>
            </div>
            <a href="cita?action=listar" class="btn btn-outline">
                <i class="fas fa-arrow-left"></i> Volver
            </a>
        </div>

        <div class="card">
            <div class="card-body">
                <form action="cita" method="post" id="citaForm">
                    <input type="hidden" name="action" value="<%= cita != null ? "actualizar" : "guardar" %>">
                    <% if (cita != null) { %>
                    <input type="hidden" name="cita_id" value="<%= cita.getCitaId() %>">
                    <% } %>

                    <!-- Información de la Cita -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-calendar-alt"></i> Información de la Cita
                        </h3>

                        <div class="form-grid">
                            <div class="form-group">
                                <label for="paciente_id">Paciente *</label>
                                <select id="paciente_id" name="paciente_id" required class="form-control">
                                    <option value="">Seleccione un paciente...</option>
                                    <%
                                        if (pacientes != null) {
                                            for (Paciente p : pacientes) {
                                    %>
                                    <option value="<%= p.getPacienteId() %>"
                                            <%= cita != null && cita.getPacienteId() == p.getPacienteId() ? "selected" : "" %>>
                                        <%= p.getNombreCompleto() %> - <%= p.getCedula() %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="odontologo_id">Odontólogo *</label>
                                <select id="odontologo_id" name="odontologo_id" required class="form-control">
                                    <option value="">Seleccione un odontólogo...</option>
                                    <%
                                        if (odontologos != null) {
                                            for (Odontologo o : odontologos) {
                                    %>
                                    <option value="<%= o.getOdontologoId() %>"
                                            <%= cita != null && cita.getOdontologoId() == o.getOdontologoId() ? "selected" : "" %>>
                                        Dr(a). <%= o.getNombres() %> <%= o.getApellidos() %> - <%= o.getEspecialidadNombre() %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="fecha">Fecha *</label>
                                <input type="date"
                                       id="fecha"
                                       name="fecha"
                                       value="<%= fechaDefault %>"
                                       required
                                       min="<%= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) %>"
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="hora">Hora *</label>
                                <input type="time"
                                       id="hora"
                                       name="hora"
                                       value="<%= horaDefault %>"
                                       required
                                       min="08:00"
                                       max="18:00"
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="duracion_minutos">Duración (minutos) *</label>
                                <select id="duracion_minutos" name="duracion_minutos" required class="form-control">
                                    <option value="15" <%= cita != null && cita.getDuracionMinutos() == 15 ? "selected" : "" %>>15 min</option>
                                    <option value="30" <%= cita == null || cita.getDuracionMinutos() == 30 ? "selected" : "" %>>30 min</option>
                                    <option value="45" <%= cita != null && cita.getDuracionMinutos() == 45 ? "selected" : "" %>>45 min</option>
                                    <option value="60" <%= cita != null && cita.getDuracionMinutos() == 60 ? "selected" : "" %>>1 hora</option>
                                    <option value="90" <%= cita != null && cita.getDuracionMinutos() == 90 ? "selected" : "" %>>1.5 horas</option>
                                    <option value="120" <%= cita != null && cita.getDuracionMinutos() == 120 ? "selected" : "" %>>2 horas</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="tipo_cita">Tipo de Cita *</label>
                                <select id="tipo_cita" name="tipo_cita" required class="form-control">
                                    <option value="Primera_Vez" <%= cita == null || "Primera_Vez".equals(cita.getTipoCita()) ? "selected" : "" %>>Primera Vez</option>
                                    <option value="Control" <%= cita != null && "Control".equals(cita.getTipoCita()) ? "selected" : "" %>>Control</option>
                                    <option value="Emergencia" <%= cita != null && "Emergencia".equals(cita.getTipoCita()) ? "selected" : "" %>>Emergencia</option>
                                    <option value="Seguimiento" <%= cita != null && "Seguimiento".equals(cita.getTipoCita()) ? "selected" : "" %>>Seguimiento</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="estado">Estado *</label>
                                <select id="estado" name="estado" required class="form-control">
                                    <option value="Programada" <%= cita == null || "Programada".equals(cita.getEstado()) ? "selected" : "" %>>Programada</option>
                                    <option value="Confirmada" <%= cita != null && "Confirmada".equals(cita.getEstado()) ? "selected" : "" %>>Confirmada</option>
                                    <option value="En_Curso" <%= cita != null && "En_Curso".equals(cita.getEstado()) ? "selected" : "" %>>En Curso</option>
                                    <option value="Completada" <%= cita != null && "Completada".equals(cita.getEstado()) ? "selected" : "" %>>Completada</option>
                                    <option value="Cancelada" <%= cita != null && "Cancelada".equals(cita.getEstado()) ? "selected" : "" %>>Cancelada</option>
                                    <option value="No_Asistio" <%= cita != null && "No_Asistio".equals(cita.getEstado()) ? "selected" : "" %>>No Asistió</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="consultorio">Consultorio</label>
                                <input type="text"
                                       id="consultorio"
                                       name="consultorio"
                                       value="<%= cita != null && cita.getConsultorio() != null ? cita.getConsultorio() : "" %>"
                                       class="form-control"
                                       placeholder="Consultorio 1">
                            </div>

                            <div class="form-group full-width">
                                <label for="motivo">Motivo de la Consulta</label>
                                <textarea id="motivo"
                                          name="motivo"
                                          rows="2"
                                          class="form-control"
                                          placeholder="Describe el motivo de la cita..."><%= cita != null && cita.getMotivo() != null ? cita.getMotivo() : "" %></textarea>
                            </div>

                            <div class="form-group full-width">
                                <label for="observaciones">Observaciones</label>
                                <textarea id="observaciones"
                                          name="observaciones"
                                          rows="3"
                                          class="form-control"
                                          placeholder="Observaciones adicionales..."><%= cita != null && cita.getObservaciones() != null ? cita.getObservaciones() : "" %></textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Botones -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i>
                            <%= cita != null ? "Actualizar" : "Agendar" %> Cita
                        </button>
                        <a href="cita?action=listar" class="btn btn-outline">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Validación de horario
    document.getElementById('citaForm').addEventListener('submit', function(e) {
        const hora = document.getElementById('hora').value;
        const [horas, minutos] = hora.split(':').map(Number);

        if (horas < 8 || horas >= 18) {
            e.preventDefault();
            alert('El horario de atención es de 8:00 AM a 6:00 PM');
            return false;
        }
    });
</script>
</body>
</html>
