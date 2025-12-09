<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 10:44 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Consulta" %>
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
            Boolean historialPaciente = (Boolean) request.getAttribute("historialPaciente");
        %>

        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-stethoscope"></i>
                    <%= historialPaciente != null ? "Historial Clínico" : "Consultas Médicas" %>
                </h1>
                <p class="page-subtitle">
                    <%= historialPaciente != null ? "Historial completo del paciente" : "Registro de consultas odontológicas" %>
                </p>
            </div>
            <div style="display: flex; gap: 0.5rem;">
                <% if (historialPaciente != null) { %>
                <a href="consulta?action=listar" class="btn btn-outline">
                    <i class="fas fa-arrow-left"></i> Volver
                </a>
                <% } %>
                <a href="consulta?action=nuevo" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Nueva Consulta
                </a>
            </div>
        </div>

        <!-- Mensajes -->
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

        <!-- Tabla de consultas -->
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
                    <p>Comienza registrando la primera consulta</p>
                    <a href="consulta?action=nuevo" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Registrar Consulta
                    </a>
                </div>
                <%
                } else {
                %>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Fecha</th>
                            <th>Paciente</th>
                            <th>Odontólogo</th>
                            <th>Motivo</th>
                            <th>Diagnóstico</th>
                            <th>Dientes</th>
                            <th>Seguimiento</th>
                            <th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Consulta c : consultas) {
                        %>
                        <tr>
                            <td><strong>#<%= c.getConsultaId() %></strong></td>
                            <td><%= c.getFechaConsulta() != null ? sdf.format(c.getFechaConsulta()) : "-" %></td>
                            <td>
                                <%= c.getPacienteNombre() %>
                                <br><small style="color: #6B7280;"><%= c.getPacienteCedula() %></small>
                            </td>
                            <td><%= c.getOdontologoNombre() %></td>
                            <td>
                                <%= c.getMotivoConsulta() != null && c.getMotivoConsulta().length() > 50
                                        ? c.getMotivoConsulta().substring(0, 50) + "..."
                                        : c.getMotivoConsulta() %>
                            </td>
                            <td>
                                <%= c.getDiagnostico() != null && c.getDiagnostico().length() > 50
                                        ? c.getDiagnostico().substring(0, 50) + "..."
                                        : c.getDiagnostico() %>
                            </td>
                            <td><%= c.getDientesTratados() != null ? c.getDientesTratados() : "-" %></td>
                            <td>
                                <% if (c.isRequiereSeguimiento()) { %>
                                <span class="badge badge-warning">
                                                        <i class="fas fa-exclamation-triangle"></i> Sí
                                                    </span>
                                <% } else { %>
                                <span class="badge badge-success">No</span>
                                <% } %>
                            </td>
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
                        Mostrando <strong><%= consultas.size() %></strong> consulta(s)
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
