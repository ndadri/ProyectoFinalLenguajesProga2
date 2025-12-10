<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 10:44 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Consulta" %>
<%@ page import="models.Paciente" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!-- Vista: consulta.jsp
Propósito: Listado de consultas médicas/odontológicas y posible vista de historial por paciente.
Variables/atributos esperados:
- consultas (List<models.Consulta>)
- historialPaciente (Boolean) (opcional)
-->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultas Médicas - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="includes/header.jsp" />

    <div class="container">
        <%
            // Recuperamos datos
            Boolean historialPaciente = (Boolean) request.getAttribute("historialPaciente");
            Paciente paciente = (Paciente) request.getAttribute("paciente"); // Recuperamos al paciente
        %>

        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-stethoscope"></i>
                    <% if (historialPaciente != null && paciente != null) { %>
                    Historial: <%= paciente.getNombres() %> <%= paciente.getApellidos() %>
                    <% } else { %>
                    Consultas Médicas
                    <% } %>
                </h1>
                <p class="page-subtitle">
                    <%= historialPaciente != null ? "Expediente clínico detallado" : "Registro general de consultas odontológicas" %>
                </p>
            </div>
            <div style="display: flex; gap: 0.5rem;">
                <% if (historialPaciente != null) { %>
                <a href="consulta?action=listar" class="btn btn-outline">
                    <i class="fas fa-arrow-left"></i> Volver a Lista
                </a>
                <a href="consulta?action=nuevo&paciente_id=<%= paciente.getPacienteId() %>" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Nueva Consulta para <%= paciente.getNombres() %>
                </a>
                <% } else { %>
                <a href="consulta?action=nuevo" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Nueva Consulta
                </a>
                <% } %>
            </div>
        </div>

        <%
            String mensaje = (String) session.getAttribute("mensaje");
            String tipoMensaje = (String) session.getAttribute("tipoMensaje");

            if (mensaje != null) {
        %>
        <div class="alert alert-<%= tipoMensaje %>">
            <i class="fas fa-<%= "success".equals(tipoMensaje) ? "check-circle" : "exclamation-circle" %>"></i>
            <%= mensaje %>
            <button class="close-alert" onclick="this.parentElement.style.display='none'">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <%
                session.removeAttribute("mensaje");
                session.removeAttribute("tipoMensaje");
            }
        %>

        <% if (historialPaciente != null && paciente != null) { %>
        <div class="card" style="margin-bottom: 20px; border-left: 5px solid #2563EB;">
            <div class="card-body">
                <div style="display: flex; flex-wrap: wrap; justify-content: space-between; gap: 20px;">

                    <div style="flex: 1; min-width: 200px;">
                        <h3 style="margin-top: 0; color: #1F2937; font-size: 1.1rem;">Datos Personales</h3>
                        <p style="margin: 5px 0; color: #4B5563;"><strong>Cédula:</strong> <%= paciente.getCedula() %></p>
                        <p style="margin: 5px 0; color: #4B5563;"><strong>Edad:</strong> <%= paciente.getEdad() %> años</p>
                        <p style="margin: 5px 0; color: #4B5563;"><strong>Género:</strong> <%= paciente.getGenero() %></p>
                    </div>

                    <div style="flex: 1; min-width: 200px;">
                        <h3 style="margin-top: 0; color: #1F2937; font-size: 1.1rem;">Contacto</h3>
                        <p style="margin: 5px 0; color: #4B5563;"><i class="fas fa-phone"></i> <%= paciente.getTelefono() %></p>
                        <p style="margin: 5px 0; color: #4B5563;"><i class="fas fa-map-marker-alt"></i> <%= paciente.getDireccion() != null ? paciente.getDireccion() : "Sin dirección" %></p>
                    </div>

                    <div style="flex: 1; min-width: 250px; background-color: #FEF2F2; padding: 10px; border-radius: 8px; border: 1px solid #FECACA;">
                        <h3 style="margin-top: 0; color: #991B1B; font-size: 1.1rem;"><i class="fas fa-exclamation-circle"></i> Alertas Médicas</h3>
                        <p style="margin: 5px 0; color: #7F1D1D;">
                            <strong>Alergias:</strong>
                            <%= (paciente.getAlergias() != null && !paciente.getAlergias().isEmpty()) ? paciente.getAlergias() : "Ninguna registrada" %>
                        </p>
                        <p style="margin: 5px 0; color: #7F1D1D;">
                            <strong>Enf. Sistémicas:</strong>
                            <%= (paciente.getEnfermedadesSistemicas() != null && !paciente.getEnfermedadesSistemicas().isEmpty()) ? paciente.getEnfermedadesSistemicas() : "Ninguna" %>
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
        <div class="card">
            <div class="card-body">
                <%
                    @SuppressWarnings("unchecked")
                    List<Consulta> consultas = (List<Consulta>) request.getAttribute("consultas");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    if (consultas == null || consultas.isEmpty()) {
                %>
                <div class="empty-state">
                    <i class="fas fa-notes-medical"></i>
                    <h3>No hay consultas registradas</h3>
                    <p>No se encontraron registros en el historial.</p>
                    <% if (historialPaciente != null && paciente != null) { %>
                    <a href="consulta?action=nuevo&paciente_id=<%= paciente.getPacienteId() %>" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Crear Primera Consulta
                    </a>
                    <% } else { %>
                    <a href="consulta?action=nuevo" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Registrar Consulta
                    </a>
                    <% } %>
                </div>
                <%
                } else {
                %>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Fecha</th> <% if (historialPaciente == null) { %>
                            <th>Paciente</th> <% } %>
                            <th>Odontólogo</th>
                            <th>Motivo / Diagnóstico</th> <th>Tratamiento</th>
                            <th>Dientes</th>
                            <th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Consulta c : consultas) {
                        %>
                        <tr>
                            <td>
                                <strong><%= c.getFechaConsulta() != null ? sdf.format(c.getFechaConsulta()) : "-" %></strong>
                                <% if (c.isRequiereSeguimiento()) { %>
                                <br><span class="badge badge-warning" style="font-size: 0.7rem;">Requiere Control</span>
                                <% } %>
                            </td>

                            <% if (historialPaciente == null) { %>
                            <td>
                                <a href="consulta?action=historial&paciente_id=<%= c.getPacienteId() %>" style="font-weight: bold; color: #2563EB; text-decoration: none;">
                                    <%= c.getPacienteNombre() %>
                                </a>
                                <br><small style="color: #6B7280;"><%= c.getPacienteCedula() %></small>
                            </td>
                            <% } %>

                            <td><%= c.getOdontologoNombre() %></td>

                            <td>
                                <span style="display:block; font-weight:500;">M: <%= c.getMotivoConsulta() %></span>
                                <span style="display:block; color: #DC2626; font-size: 0.9em;">Dx: <%= c.getDiagnostico() %></span>
                            </td>

                            <td><%= c.getTratamiento() %></td>

                            <td><%= c.getDientesTratados() != null ? c.getDientesTratados() : "-" %></td>

                            <td>
                                <div class="action-buttons">
                                    <a href="consulta?action=ver&id=<%= c.getConsultaId() %>"
                                       class="btn-icon"
                                       style="background: #DBEAFE; color: #1E40AF;"
                                       title="Ver Detalles">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <a href="consulta?action=editar&id=<%= c.getConsultaId() %>"
                                       class="btn-icon btn-edit"
                                       title="Editar">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <button onclick="confirmarEliminacion(<%= c.getConsultaId() %>)"
                                            class="btn-icon btn-delete"
                                            title="Eliminar">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>

                <div class="table-footer">
                    <p class="table-info">
                        Mostrando <strong><%= consultas.size() %></strong> registros
                    </p>
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmarEliminacion(id) {
        if (confirm('¿Está seguro que desea eliminar esta consulta?\nEsta acción no se puede deshacer.')) {
            window.location.href = 'consulta?action=eliminar&id=' + id;
        }
    }

    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        });
    }, 5000);
</script>
</body>
</html>
