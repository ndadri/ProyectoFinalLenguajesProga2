<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 08/dic/2025
  Time: 07:04 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<%
    Usuario usuarioConfig = (Usuario) session.getAttribute("usuario");
    if (usuarioConfig == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Configuración - DentalCare</title>
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
                    <i class="fas fa-cog"></i> Configuración
                </h1>
                <p class="page-subtitle">Administra tu cuenta y preferencias</p>
            </div>
        </div>

        <!-- Información del Usuario -->
        <div class="card" style="margin-bottom: 2rem;">
            <div class="card-body">
                <h3 style="margin-bottom: 1rem;">
                    <i class="fas fa-user"></i> Información de la Cuenta
                </h3>
                <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 1rem;">
                    <div>
                        <strong>Usuario:</strong>
                        <p><%= usuarioConfig.getUsuario() %></p>
                    </div>
                    <div>
                        <strong>Email:</strong>
                        <p><%= usuarioConfig.getEmail() %></p>
                    </div>
                    <div>
                        <strong>Tipo de Usuario:</strong>
                        <p><%= usuarioConfig.getTipoUsuarioNombre() %></p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Cambiar Contraseña -->
        <div class="card">
            <div class="card-body">
                <h3 style="margin-bottom: 1rem;">
                    <i class="fas fa-key"></i> Cambiar Contraseña
                </h3>

                <% if (request.getAttribute("mensaje") != null) { %>
                <div class="alert alert-<%= request.getAttribute("tipoMensaje") %>" style="margin-bottom: 1rem;">
                    <%= request.getAttribute("mensaje") %>
                </div>
                <% } %>

                <form action="configuracion" method="post" style="max-width: 500px;">
                    <input type="hidden" name="action" value="cambiar_password">

                    <div class="form-group">
                        <label for="password_actual">Contraseña Actual *</label>
                        <input type="password"
                               id="password_actual"
                               name="password_actual"
                               class="form-control"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="password_nueva">Nueva Contraseña *</label>
                        <input type="password"
                               id="password_nueva"
                               name="password_nueva"
                               class="form-control"
                               minlength="6"
                               required>
                        <small class="form-text">Mínimo 6 caracteres</small>
                    </div>

                    <div class="form-group">
                        <label for="password_confirmar">Confirmar Nueva Contraseña *</label>
                        <input type="password"
                               id="password_confirmar"
                               name="password_confirmar"
                               class="form-control"
                               minlength="6"
                               required>
                    </div>

                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Cambiar Contraseña
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Validar que las contraseñas coincidan
    document.querySelector('form').addEventListener('submit', function(e) {
        const nueva = document.getElementById('password_nueva').value;
        const confirmar = document.getElementById('password_confirmar').value;

        if (nueva !== confirmar) {
            e.preventDefault();
            alert('Las contraseñas no coinciden');
            return false;
        }
    });
</script>
</body>
</html>
