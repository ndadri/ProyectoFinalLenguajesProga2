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
        %>

        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-user-md"></i>
                    <%= odontologo != null ? "Editar Odontólogo" : "Nuevo Odontólogo" %>
                </h1>
                <p class="page-subtitle">
                    <%= odontologo != null ? "Modifica la información del odontólogo" : "Completa los datos del nuevo odontólogo" %>
                </p>
            </div>
            <a href="odontologo?action=listar" class="btn btn-outline">
                <i class="fas fa-arrow-left"></i> Volver
            </a>
        </div>

        <div class="card">
            <div class="card-body">
                <form action="odontologo" method="post" id="odontologoForm">
                    <input type="hidden" name="action" value="<%= odontologo != null ? "actualizar" : "guardar" %>">
                    <% if (odontologo != null) { %>
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
                                       value="<%= odontologo != null ? odontologo.getNombres() : "" %>"
                                       required
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="apellidos">Apellidos *</label>
                                <input type="text"
                                       id="apellidos"
                                       name="apellidos"
                                       value="<%= odontologo != null ? odontologo.getApellidos() : "" %>"
                                       required
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="cedula">Cédula *</label>
                                <input type="text"
                                       id="cedula"
                                       name="cedula"
                                       value="<%= odontologo != null ? odontologo.getCedula() : "" %>"
                                       required
                                       maxlength="10"
                                       pattern="[0-9]{10}"
                                       class="form-control"
                                       placeholder="1234567890">
                            </div>

                            <div class="form-group">
                                <label for="fecha_nacimiento">Fecha de Nacimiento</label>
                                <input type="date"
                                       id="fecha_nacimiento"
                                       name="fecha_nacimiento"
                                       value="<%= odontologo != null && odontologo.getFechaNacimiento() != null ? odontologo.getFechaNacimiento() : "" %>"
                                       class="form-control">
                            </div>

                            <div class="form-group">
                                <label for="genero">Género *</label>
                                <select id="genero" name="genero" required class="form-control">
                                    <option value="">Seleccione...</option>
                                    <option value="M" <%= odontologo != null && "M".equals(odontologo.getGenero()) ? "selected" : "" %>>Masculino</option>
                                    <option value="F" <%= odontologo != null && "F".equals(odontologo.getGenero()) ? "selected" : "" %>>Femenino</option>
                                    <option value="Otro" <%= odontologo != null && "Otro".equals(odontologo.getGenero()) ? "selected" : "" %>>Otro</option>
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
                                            <%= odontologo != null && odontologo.getEspecialidadId() == e.getEspecialidadId() ? "selected" : "" %>>
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
                                       value="<%= odontologo != null && odontologo.getNumRegistro() != null ? odontologo.getNumRegistro() : "" %>"
                                       class="form-control"
                                       placeholder="REG-001">
                            </div>
                        </div>
                    </div>

                    <!-- Información de Contacto -->
                    <div class="form-section">
                        <h3 class="form-section-title">
                            <i class="fas fa-address-book"></i> Información de Contacto
                        </h3>

                        <div class="form-grid">
                            <div class="form-group">
                                <label for="telefono">Teléfono Fijo</label>
                                <input type="tel"
                                       id="telefono"
                                       name="telefono"
                                       value="<%= odontologo != null && odontologo.getTelefono() != null ? odontologo.getTelefono() : "" %>"
                                       class="form-control"
                                       placeholder="042345678">
                            </div>

                            <div class="form-group">
                                <label for="celular">Celular</label>
                                <input type="tel"
                                       id="celular"
                                       name="celular"
                                       value="<%= odontologo != null && odontologo.getCelular() != null ? odontologo.getCelular() : "" %>"
                                       class="form-control"
                                       placeholder="0987654321">
                            </div>

                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email"
                                       id="email"
                                       name="email"
                                       value="<%= odontologo != null && odontologo.getEmail() != null ? odontologo.getEmail() : "" %>"
                                       class="form-control"
                                       placeholder="doctor@dentalcare.com">
                            </div>

                            <div class="form-group full-width">
                                <label for="direccion">Dirección</label>
                                <textarea id="direccion"
                                          name="direccion"
                                          rows="2"
                                          class="form-control"><%= odontologo != null && odontologo.getDireccion() != null ? odontologo.getDireccion() : "" %></textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Botones -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i>
                            <%= odontologo != null ? "Actualizar" : "Guardar" %> Odontólogo
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
    // Validación de cédula
    document.getElementById('odontologoForm').addEventListener('submit', function(e) {
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