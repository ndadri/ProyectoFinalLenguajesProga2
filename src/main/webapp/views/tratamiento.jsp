<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 11:40 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    Usuario usuarioTratamiento = (Usuario) session.getAttribute("usuario");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Tratamientos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">DentalCare</a>
    </div>
</nav>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="fas fa-tooth"></i> Gestión de Tratamientos</h2>
        <% if (usuarioTratamiento.tieneAccesoTotal()) { %>
        <a href="tratamiento?action=nuevo" class="btn btn-primary">
            <i class="fas fa-plus"></i> Nuevo Tratamiento
        </a>
        <% } else { %>
        <span class="badge bg-secondary" style="font-size: 1rem; padding: 0.5rem 1rem;">
            <i class="fas fa-eye"></i> Solo Lectura
        </span>
        <% } %>
    </div>

    <!-- MENSAJE DE ALERTA -->
    <c:if test="${not empty sessionScope.mensaje}">
        <div class="alert alert-${sessionScope.tipoMensaje} alert-dismissible fade show" role="alert">
                ${sessionScope.mensaje}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="mensaje" scope="session"/>
        <c:remove var="tipoMensaje" scope="session"/>
    </c:if>

    <!-- TABLA DE TRATAMIENTOS -->
    <div class="card shadow-sm">
        <div class="card-body">
            <table class="table table-striped table-hover mb-0">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Código</th>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Precio Base</th>
                    <th>Duración</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="t" items="${tratamientos}">
                    <tr>
                        <td>${t.tratamientoId}</td>
                        <td><strong>${t.codigo}</strong></td>
                        <td>${t.nombre}</td>
                        <td>
                            <span class="badge bg-info">${t.categoria}</span>
                        </td>
                        <td>$${t.precioBase}</td>
                        <td>${t.duracionAproximada} min</td>
                        <td>
                            <c:choose>
                                <c:when test="${t.activo == 1}">
                                    <span class="badge bg-success">Activo</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Inactivo</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <% if (usuarioTratamiento.tieneAccesoTotal()) { %>
                            <div class="btn-group" role="group">
                                <a href="tratamiento?action=editar&id=${t.tratamientoId}"
                                   class="btn btn-warning btn-sm"
                                   title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="tratamiento?action=eliminar&id=${t.tratamientoId}"
                                   class="btn btn-danger btn-sm"
                                   title="Eliminar"
                                   onclick="return confirm('¿Está seguro de eliminar el tratamiento \"${t.nombre}\"?\n\nEsta acción no se puede deshacer.');">
                                <i class="fas fa-trash"></i>
                                </a>
                            </div>
                            <% } else { %>
                            <span class="text-muted">
                                <i class="fas fa-eye"></i> Solo lectura
                            </span>
                            <% } %>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty tratamientos}">
                    <tr>
                        <td colspan="8" class="text-center text-muted py-4">
                            <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                            <p class="mb-0">No hay tratamientos registrados</p>
                        </td>
                    </tr>
                </c:if>

                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-3 text-muted">
        <small>
            <i class="fas fa-info-circle"></i>
            Total de tratamientos: <strong>${tratamientos.size()}</strong>
        </small>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>