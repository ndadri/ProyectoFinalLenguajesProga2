<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 10:23 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Odontologo" %>
<%@ page import="models.Especialidad" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("odontologo") != null ? "Editar" : "Nuevo" %> Odontólogo - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="includes/header.jsp" />

    <div class="container">
        <%
            Odontologo odontologo = (Odontologo) request.getAttribute("odontologo");
            @SuppressWarnings("unchecked")
            List<Especialidad> especialidades = (List<Especialidad>) request.getAttribute("especialidades");

            boolean esEdicion = (odontologo != null);
            String fechaNacDefault = "";

            if (esEdicion && odontologo.getFechaNacimiento() != null) {
                fechaNacDefault = odontologo.getFechaNacimiento().toString();
            }
        %>

        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-user-md"></i>
                    <%= esEdicion ? "Editar Odontólogo" : "Nuevo Odontólogo" %>
                </h1>
                <p class="page-subtitle">
                    <%= esEdicion ? "Modifica los datos del odontólogo" : "Completa la información del nuevo odontólogo" %>
                </p>
            </div>
            <a href="odontologo?action=listar" class="btn btn-outline">
                <i class="fas fa-arrow-left"></i> Volver
            </a>
        </div>

        <div class="card">
            <div class="card-body">
                <form action="odontologo" method="post" id="odontologoForm">
                    <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "guardar" %>">
                    <% if (esEdicion) { %>
                    <input type="hidden" name="odontologo_id" value="<%= odontologo.getOdontologoId() %>">
                    <% } %>

                    <!-- Datos Personales -->
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
                                       value="<%= esEdicion ? odontologo.getNombres() : "" %>"
                                       required
                                       class="form-control"
                                       placeholder="Juan Carlos">
                            </div>

                            <div class="form-group">
                                <label for="apellidos">Apellidos *</label>
                                <input type="text"
                                       id="apellidos"
                                       name="apellidos"
                                       value="<%= esEdicion ? odontologo.getApellidos() : "" %>"
                                       required
                                       class="form-control"
                                       placeholder="García Pérez">
                            </div>

                            <div class="form-group">
                                <label for="cedula">Cédula *</label>
                                <input type="text"
                                       id="cedula"
                                       name="cedula"
                                       value="<%= esEdicion ? odontologo.getCedula() : "" %>"
                                       required
                                       pattern="[0-9]{10}"
                                       class="form-control"
                                       placeholder="1234567890"
                                       maxlength="10">
                                <small class="form-text">10 dígitos</small>
                            </div>

                            <div class="form-group">
                                <label for="fecha_nacimiento">Fecha de Nacimiento *</label>
                                <input type="date"
                                       id="fecha_nacimiento"
                                       name="fecha_nacimiento"
                                       value="<%= fechaNacDefault %>"
                                       required
                                       max="<%= LocalDate.now().minusYears(18).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) %>"
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="genero">Género *</label>
                                <select id="genero" name="genero" required class="form-control">
                                    <option value="">Seleccione...</option>
                                    <option value="M" <%= esEdicion && "M".equals(odontologo.getGenero()) ? "selected" : "" %>>Masculino</option>
                                    <option value="F" <%= esEdicion && "F".equals(odontologo.getGenero()) ? "selected" : "" %>>Femenino</option>
                                    <option value="Otro" <%= esEdicion && "Otro".equals(odontologo.getGenero()) ? "selected" : "" %>>Otro</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- Información Profesional -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-graduation-cap"></i> Información Profesional
                        </h3>

                        <div class="form-grid">
                            <div class="form-group">
                                <label for="especialidad_id">Especialidad *</label>
                                <select id="especialidad_id" name="especialidad_id" required class="form-control">
                                    <option value="">Seleccione una especialidad...</option>
                                    <%
                                        if (especialidades != null) {
                                            for (Especialidad e : especialidades) {
                                    %>
                                    <option value="<%= e.getEspecialidadId() %>"
                                            <%= esEdicion && odontologo.getEspecialidadId() == e.getEspecialidadId() ? "selected" : "" %>>
                                        <%= e.getNombre() %>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="num_registro">Número de Registro Profesional</label>
                                <input type="text"
                                       id="num_registro"
                                       name="num_registro"
                                       value="<%= esEdicion && odontologo.getNumRegistro() != null ? odontologo.getNumRegistro() : "" %>"
                                       class="form-control"
                                       placeholder="REG-001">
                            </div>
                        </div>
                    </div>

                    <!-- Credenciales de Acceso - SOLO AL CREAR -->
                    <% if (!esEdicion) { %>
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-user-lock"></i> Credenciales de Acceso
                        </h3>
                        <p class="form-help-text" style="color: #6B7280; margin-bottom: 1rem;">
                            <i class="fas fa-info-circle"></i>
                            Estas credenciales se usarán para que el odontólogo acceda al sistema
                        </p>

                        <div class="form-grid">
                            <div class="form-group">
                                <label for="usuario">Usuario *</label>
                                <input type="text"
                                       id="usuario"
                                       name="usuario"
                                       class="form-control"
                                       placeholder="ej: dr.garcia"
                                       required
                                       pattern="[a-zA-Z0-9._-]+"
                                       title="Solo letras, números, puntos, guiones y guiones bajos">
                                <small class="form-text">Sin espacios ni caracteres especiales</small>
                            </div>

                            <div class="form-group">
                                <label for="password">Contraseña *</label>
                                <input type="password"
                                       id="password"
                                       name="password"
                                       class="form-control"
                                       placeholder="Mínimo 6 caracteres"
                                       required
                                       minlength="6">
                                <small class="form-text">Mínimo 6 caracteres</small>
                            </div>

                            <div class="form-group">
                                <label for="email">Email *</label>
                                <input type="email"
                                       id="email"
                                       name="email"
                                       class="form-control"
                                       placeholder="correo@ejemplo.com"
                                       required>
                            </div>
                        </div>
                    </div>
                    <% } %>

                    <!-- Información de Contacto -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-phone"></i> Información de Contacto
                        </h3>

                        <div class="form-grid">
                            <div class="form-group">
                                <label for="telefono">Teléfono</label>
                                <input type="tel"
                                       id="telefono"
                                       name="telefono"
                                       value="<%= esEdicion && odontologo.getTelefono() != null ? odontologo.getTelefono() : "" %>"
                                       class="form-control"
                                       placeholder="02-234-5678"
                                       pattern="[0-9-]+"
                                       maxlength="20">
                            </div>

                            <div class="form-group">
                                <label for="celular">Celular</label>
                                <input type="tel"
                                       id="celular"
                                       name="celular"
                                       value="<%= esEdicion && odontologo.getCelular() != null ? odontologo.getCelular() : "" %>"
                                       class="form-control"
                                       placeholder="0987654321"
                                       pattern="[0-9]+"
                                       maxlength="20">
                            </div>

                            <% if (esEdicion) { %>
                            <div class="form-group">
                                <label for="email_contacto">Email</label>
                                <input type="email"
                                       id="email_contacto"
                                       name="email"
                                       value="<%= odontologo.getEmail() != null ? odontologo.getEmail() : "" %>"
                                       class="form-control"
                                       placeholder="correo@ejemplo.com">
                            </div>
                            <% } %>

                            <div class="form-group full-width">
                                <label for="direccion">Dirección</label>
                                <textarea id="direccion"
                                          name="direccion"
                                          rows="2"
                                          class="form-control"
                                          placeholder="Dirección completa..."><%= esEdicion && odontologo.getDireccion() != null ? odontologo.getDireccion() : "" %></textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Botones -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i>
                            <%= esEdicion ? "Actualizar" : "Guardar" %> Odontólogo
                        </button>
                        <a href="odontologo?action=listar" class="btn btn-outline">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    // Validación de cédula ecuatoriana
    document.getElementById('odontologoForm').addEventListener('submit', function(e) {
        const cedula = document.getElementById('cedula').value;

        if (cedula.length !== 10) {
            e.preventDefault();
            alert('La cédula debe tener exactamente 10 dígitos');
            return false;
        }
    });
</script>
</body>
</html>