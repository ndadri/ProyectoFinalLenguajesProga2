<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 10:23 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Odontologo" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Odontólogos - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="includes/header.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-user-md"></i> Gestión de Odontólogos
                </h1>
                <p class="page-subtitle">Administra el personal odontológico de la clínica</p>
            </div>
            <a href="odontologo?action=nuevo" class="btn btn-primary">
                <i class="fas fa-plus"></i> Nuevo Odontólogo
            </a>
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

        <!-- Tabla de odontólogos -->
        <div class="card">
            <div class="card-body">
                <%
                    @SuppressWarnings("unchecked")
                    List<Odontologo> odontologos = (List<Odontologo>) request.getAttribute("odontologos");

                    if (odontologos == null || odontologos.isEmpty()) {
                %>
                <div class="empty-state">
                    <i class="fas fa-user-md-slash"></i>
                    <h3>No hay odontólogos registrados</h3>
                    <p>Comienza agregando el primer odontólogo</p>
                    <a href="odontologo?action=nuevo" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Agregar Odontólogo
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
                            <th>Especialidad</th>
                            <th>Cédula</th>
                            <th>Registro Prof.</th>
                            <th>Celular</th>
                            <th>Email</th>
                            <th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Odontologo o : odontologos) {
                        %>
                        <tr>
                            <td><%= o.getOdontologoId() %></td>
                            <td><%= o.getNombres() %></td>
                            <td><%= o.getApellidos() %></td>
                            <td>
                                                <span class="badge badge-info">
                                                    <%= o.getEspecialidadNombre() %>
                                                </span>
                            </td>
                            <td><%= o.getCedula() %></td>
                            <td><%= o.getNumRegistro() != null ? o.getNumRegistro() : "-" %></td>
                            <td><%= o.getCelular() != null ? o.getCelular() : "-" %></td>
                            <td><%= o.getEmail() != null ? o.getEmail() : "-" %></td>
                            <td>
                                <div class="action-buttons">
                                    <a href="odontologo?action=editar&id=<%= o.getOdontologoId() %>"
                                       class="btn-icon btn-edit"
                                       title="Editar">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <button onclick="confirmarEliminacion(<%= o.getOdontologoId() %>, '<%= o.getNombreCompleto() %>')"
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
                        Mostrando <strong><%= odontologos.size() %></strong> odontólogo(s)
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
        if (confirm('¿Está seguro que desea eliminar a ' + nombre + '?')) {
            window.location.href = 'odontologo?action=eliminar&id=' + id;
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
