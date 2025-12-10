<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 09/dic/2025
  Time: 09:25 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inventario de Medicamentos</title>
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
                <h1 class="page-title"><i class="fas fa-pills"></i> Medicamentos</h1>
                <p class="page-subtitle">Gestión de inventario farmacéutico</p>
        <div class="card">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Presentación</th>
                            <th>Concentración</th>
                            <th>Stock</th>
                            <th>Precio</th>
                            <th>Descripción</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="m" items="${medicamentos}">
                            <tr>
                                <td style="font-weight: bold; color: #2563EB;">${m.nombre}</td>
                                <td>${m.presentacion}</td>
                                <td>${m.concentracion}</td>
                                <td>
                                        <span class="badge ${m.stock > 10 ? 'badge-success' : 'badge-warning'}">
                                            ${m.stock} un.
                                        </span>
                                </td>
                                <td>$ ${m.precio}</td>
                                <td style="color: #6B7280; font-size: 0.9em;">${m.descripcion}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
