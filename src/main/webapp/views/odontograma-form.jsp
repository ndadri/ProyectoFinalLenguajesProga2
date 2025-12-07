<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 07/dic/2025
  Time: 12:50 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Paciente" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuevo Odontograma - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="container">
    <%
        List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
    %>

    <div class="page-header">
        <div>
            <h1 class="page-title">
                <i class="fas fa-plus"></i> Nuevo Odontograma
            </h1>
            <p class="page-subtitle">Crea un nuevo registro dental para un paciente</p>
        </div>
        <a href="odontograma?action=listar" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver
        </a>
    </div>

    <div class="card">
        <div class="card-body">
            <form action="odontograma" method="post">
                <input type="hidden" name="action" value="crear">

                <div class="form-group">
                    <label for="paciente_id">Paciente *</label>
                    <select id="paciente_id" name="paciente_id" class="form-control" required>
                        <option value="">Seleccione un paciente</option>
                        <% for (Paciente p : pacientes) { %>
                        <option value="<%= p.getPacienteId() %>">
                            <%= p.getNombres() %> <%= p.getApellidos() %> - <%= p.getCedula() %>
                        </option>
                        <% } %>
                    </select>
                    <small class="form-text">Selecciona el paciente para el cual se creará el odontograma</small>
                </div>

                <div class="form-group">
                    <label for="observaciones">Observaciones Iniciales</label>
                    <textarea id="observaciones" name="observaciones" class="form-control" rows="4"
                              placeholder="Observaciones generales del estado dental del paciente"></textarea>
                    <small class="form-text">Puedes agregar notas sobre el estado general de la dentadura</small>
                </div>

                <div style="background: #EFF6FF; padding: 1rem; border-radius: 8px; margin: 1.5rem 0;">
                    <div style="display: flex; gap: 0.5rem; align-items: center; color: #1E40AF;">
                        <i class="fas fa-info-circle"></i>
                        <strong>Información:</strong>
                    </div>
                    <p style="margin: 0.5rem 0 0 0; color: #1E40AF; font-size: 0.9rem;">
                        Al crear el odontograma, se inicializarán automáticamente los 32 dientes permanentes
                        con estado "Sano". Luego podrás editar cada diente individualmente.
                    </p>
                </div>

                <div style="display: flex; gap: 1rem; margin-top: 2rem;">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Crear Odontograma
                    </button>
                    <a href="odontograma?action=listar" class="btn btn-secondary">
                        <i class="fas fa-times"></i> Cancelar
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
