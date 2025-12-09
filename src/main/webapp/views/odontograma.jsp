<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 07/dic/2025
  Time: 12:49 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.*" %>
<%@ page import="java.util.List" %>
<%
    Usuario usuarioOdonto = (Usuario) session.getAttribute("usuario");
    if (usuarioOdonto == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!-- Vista: odontograma.jsp
Propósito: Listado de odontogramas por paciente y gestión (crear, ver, eliminar).
Variables/atributos esperados:
- pacientes (List<models.Paciente>)
- odontogramas (List<models.Odontograma>)
- pacienteIdFiltro (Integer) (opcional)
Secciones principales:
1) Filtro por paciente.
2) Tabla/listado de odontogramas.
3) Acciones: ver, eliminar.
-->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Odontogramas - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<!-- SIDEBAR -->
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <!-- HEADER -->
    <jsp:include page="includes/header.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-tooth"></i> Odontogramas
                </h1>
                <p class="page-subtitle">Registros dentales de los pacientes</p>
            </div>
            <a href="odontograma?action=nuevo" class="btn btn-primary">
                <i class="fas fa-plus"></i> Nuevo Odontograma
            </a>
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

        <%
            List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
            Integer pacienteIdFiltro = (Integer) request.getAttribute("pacienteIdFiltro");
        %>

        <div class="filter-bar">
            <form method="get" action="odontograma" style="display: flex; gap: 1rem; align-items: center;">
                <input type="hidden" name="action" value="listar">
                <label style="font-weight: 600;">Filtrar por paciente:</label>
                <select name="paciente_id" class="form-control" style="max-width: 300px;" onchange="this.form.submit()">
                    <option value="">Todos los pacientes</option>
                    <% for (Paciente p : pacientes) { %>
                    <option value="<%= p.getPacienteId() %>"
                            <%= pacienteIdFiltro != null && pacienteIdFiltro == p.getPacienteId() ? "selected" : "" %>>
                        <%= p.getNombres() %> <%= p.getApellidos() %> - <%= p.getCedula() %>
                    </option>
                    <% } %>
                </select>
                <% if (pacienteIdFiltro != null) { %>
                <a href="odontograma?action=listar" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Limpiar filtro
                </a>
                <% } %>
            </form>
        </div>

        <div class="card">
            <div class="card-body">
                <%
                    @SuppressWarnings("unchecked")
                    List<Odontograma> odontogramas = (List<Odontograma>) request.getAttribute("odontogramas");
                    if (odontogramas == null || odontogramas.isEmpty()) {
                %>
                <div class="empty-state">
                    <i class="fas fa-tooth" style="font-size: 4rem; color: #D1D5DB;"></i>
                    <h3>No hay odontogramas registrados</h3>
                    <p>Comienza creando el primer odontograma</p>
                    <a href="odontograma?action=nuevo" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Nuevo Odontograma
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
                            <th>Paciente</th>
                            <th>Cédula</th>
                            <th>Fecha Creación</th>
                            <th>Observaciones</th>
                            <th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Odontograma o : odontogramas) {
                        %>
                        <tr>
                            <td><strong>#<%= o.getOdontogramaId() %></strong></td>
                            <td><%= o.getPacienteNombre() %></td>
                            <td><%= o.getPacienteCedula() %></td>
                            <td><%= o.getFechaFormateada() %></td>
                            <td>
                                <% if (o.getObservaciones() != null && !o.getObservaciones().isEmpty()) { %>
                                <%= o.getObservaciones().length() > 50 ? o.getObservaciones().substring(0, 50) + "..." : o.getObservaciones() %>
                                <% } else { %>
                                <span style="color: #9CA3AF;">Sin observaciones</span>
                                <% } %>
                            </td>
                            <td>
                                <div class="actions">
                                    <a href="odontograma?action=ver&id=<%= o.getOdontogramaId() %>"
                                       class="btn-icon" style="background: #DBEAFE; color: #1E40AF;"
                                       title="Ver odontograma">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <button onclick="confirmarEliminacion(<%= o.getOdontogramaId() %>, '<%= o.getPacienteNombre() %>')"
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
                    <p class="table-info">Mostrando <strong><%= odontogramas.size() %></strong> odontograma(s)</p>
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmarEliminacion(id, paciente) {
        if (confirm('¿Está seguro que desea eliminar el odontograma del paciente ' + paciente + '?\n\nEsta acción eliminará todos los registros dentales asociados y no se puede deshacer.')) {
            window.location.href = 'odontograma?action=eliminar&id=' + id;
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
