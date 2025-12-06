<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 11:40 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
        <a href="tratamiento?action=nuevo" class="btn btn-primary">
            <i class="fas fa-plus"></i> Nuevo Tratamiento
        </a>
    </div>

    <!-- MENSAJE DE ALERTA -->
    <c:if test="${not empty sessionScope.mensaje}">
        <div class="alert alert-${sessionScope.tipoMensaje}">
                ${sessionScope.mensaje}
        </div>
        <c:remove var="mensaje" scope="session"/>
        <c:remove var="tipoMensaje" scope="session"/>
    </c:if>

    <!-- TABLA DE TRATAMIENTOS -->
    <table class="table table-striped table-hover">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Código</th>
            <th>Nombre</th>
            <th>Categoría</th>
            <th>Precio Base</th>
            <th>Duración (min)</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="t" items="${tratamientos}">
            <tr>
                <td>${t.id}</td>
                <td>${t.codigo}</td>
                <td>${t.nombre}</td>
                <td>${t.categoria}</td>
                <td>$ ${t.precio_base}</td>
                <td>${t.duracionMin}</td>
                <td>
                        <span class="badge bg-${t.estado == 1 ? 'success' : 'secondary'}">
                                ${t.estado == 1 ? 'Activo' : 'Inactivo'}
                        </span>
                </td>
                <td>
                    <a href="tratamiento?action=editar&id=${t.id}" class="btn btn-warning btn-sm">
                        <i class="fas fa-edit"></i>
                    </a>
                    <a href="tratamiento?action=eliminar&id=${t.id}"
                       class="btn btn-danger btn-sm"
                       onclick="return confirm('¿Seguro de eliminar?');">
                        <i class="fas fa-trash"></i>
                    </a>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty tratamientos}">
            <tr>
                <td colspan="8" class="text-center text-muted">No hay tratamientos registrados</td>
            </tr>
        </c:if>

        </tbody>
    </table>
</div>

</body>
</html>
