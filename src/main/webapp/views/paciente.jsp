<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Paciente" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Pacientes - DentalCare</title>
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
                    <i class="fas fa-users"></i> Gestión de Pacientes
                </h1>
                <p class="page-subtitle">Administra la información de tus pacientes</p>
            </div>
            <a href="paciente?action=nuevo" class="btn btn-primary">
                <i class="fas fa-plus"></i> Nuevo Paciente
            </a>
        </div>

        <!-- Mensajes de notificación -->
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

        <!-- Barra de búsqueda -->
        <div class="search-bar-container">
            <form action="paciente" method="get" class="search-form">
                <input type="hidden" name="action" value="buscar">
                <div class="search-input-group">
                    <i class="fas fa-search"></i>
                    <input type="text"
                           name="termino"
                           placeholder="Buscar por nombre, apellido o cédula..."
                           value="<%= request.getParameter("termino") != null ? request.getParameter("termino") : "" %>"
                           class="search-input">
                </div>
                <button type="submit" class="btn btn-secondary">Buscar</button>
                <% if (request.getParameter("termino") != null && !request.getParameter("termino").isEmpty()) { %>
                <a href="paciente?action=listar" class="btn btn-outline">
                    <i class="fas fa-times"></i> Limpiar
                </a>
                <% } %>
            </form>
        </div>

        <!-- Tabla de pacientes -->
        <div class="card">
            <div class="card-body">
                <%
                    @SuppressWarnings("unchecked")
                    List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    if (pacientes == null || pacientes.isEmpty()) {
                %>
                <div class="empty-state">
                    <i class="fas fa-user-slash"></i>
                    <h3>No hay pacientes registrados</h3>
                    <p>Comienza agregando tu primer paciente</p>
                    <a href="paciente?action=nuevo" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Agregar Paciente
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
                            <th>Nombres</th>
                            <th>Apellidos</th>
                            <th>Cédula</th>
                            <th>Edad</th>
                            <th>Teléfono</th>
                            <th>Email</th>
                            <th>Última Visita</th>
                            <th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Paciente p : pacientes) {
                        %>
                        <tr>
                            <td><%= p.getPacienteId() %></td>
                            <td><%= p.getNombres() %></td>
                            <td><%= p.getApellidos() %></td>
                            <td><%= p.getCedula() %></td>
                            <td><%= p.getEdad() %> años</td>
                            <td><%= p.getTelefono() != null ? p.getTelefono() : "-" %></td>
                            <td><%= p.getEmail() != null ? p.getEmail() : "-" %></td>
                            <td>
                                <% if (p.getUltimaVisita() != null) { %>
                                <%= sdf.format(p.getUltimaVisita()) %>
                                <% } else { %>
                                <span class="badge badge-warning">Sin visitas</span>
                                <% } %>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="paciente?action=editar&id=<%= p.getPacienteId() %>"
                                       class="btn-icon btn-edit"
                                       title="Editar">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <button onclick="confirmarEliminacion(<%= p.getPacienteId() %>, '<%= p.getNombreCompleto() %>')"
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

                <!-- Información de registros -->
                <div class="table-footer">
                    <p class="table-info">
                        Mostrando <strong><%= pacientes.size() %></strong> paciente(s)
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
    function confirmarEliminacion(id, nombre) {
        if (confirm('¿Está seguro que desea eliminar al paciente ' + nombre + '?')) {
            window.location.href = 'paciente?action=eliminar&id=' + id;
        }
    }

    // Auto-ocultar alertas después de 5 segundos
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
