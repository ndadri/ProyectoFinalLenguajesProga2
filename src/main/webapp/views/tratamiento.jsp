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
    if (usuarioTratamiento == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!-- Vista: tratamiento.jsp
Propósito: Listado de tratamientos odontológicos y gestión (crear, editar, eliminar).
Variables/atributos esperados:
- tratamientos (List<models.TratamientoOdontologico>)
- sessionScope.mensaje, sessionScope.tipoMensaje (opcional) para notificaciones
Secciones principales:
1) Cabecera con botón para nuevo tratamiento.
2) Mensaje de alerta (opcional).
3) Tabla de tratamientos con acciones (editar, eliminar).
Notas:
- Esta vista usa JSTL tags (c:forEach, c:if), asegurarse de que el taglib esté disponible en el proyecto.
-->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tratamientos - DentalCare</title>
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
                    <i class="fas fa-tooth"></i> Gestión de Tratamientos
                </h1>
                <p class="page-subtitle">Catálogo de tratamientos dentales</p>
            </div>
            <% if (usuarioTratamiento.tieneAccesoTotal()) { %>
            <a href="tratamiento?action=nuevo" class="btn btn-primary">
                <i class="fas fa-plus"></i> Nuevo Tratamiento
            </a>
            <% } else { %>
            <span class="badge" style="background: #6B7280; color: white; font-size: 1rem; padding: 0.5rem 1rem;">
                <i class="fas fa-eye"></i> Solo Lectura
            </span>
            <% } %>
        </div>

        <!-- MENSAJE DE ALERTA -->
        <c:if test="${not empty sessionScope.mensaje}">
            <div class="alert alert-${sessionScope.tipoMensaje}">
                <i class="fas fa-<%= "success".equals(session.getAttribute("tipoMensaje")) ? "check-circle" : "exclamation-circle" %>"></i>
                    ${sessionScope.mensaje}
                <button class="close-alert" onclick="this.parentElement.style.display='none'">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <c:remove var="mensaje" scope="session"/>
            <c:remove var="tipoMensaje" scope="session"/>
        </c:if>

        <!-- TABLA DE TRATAMIENTOS -->
        <div class="card">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table">
                        <thead>
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
                                <td><strong>#${t.tratamientoId}</strong></td>
                                <td><span class="badge" style="background: #3B82F6; color: white;">${t.codigo}</span></td>
                                <td>${t.nombre}</td>
                                <td>
                                    <span class="badge" style="background: #06B6D4; color: white;">${t.categoria}</span>
                                </td>
                                <td><strong>$${t.precioBase}</strong></td>
                                <td>${t.duracionAproximada} min</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${t.activo == 1}">
                                            <span class="badge badge-success">Activo</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge" style="background: #6B7280; color: white;">Inactivo</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <% if (usuarioTratamiento.tieneAccesoTotal()) { %>
                                    <div class="actions">
                                        <a href="tratamiento?action=editar&id=${t.tratamientoId}"
                                           class="btn-icon btn-edit"
                                           title="Editar">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <button onclick="confirmarEliminacion(${t.tratamientoId}, '${t.nombre}')"
                                                class="btn-icon btn-delete"
                                                title="Eliminar">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                    <% } else { %>
                                    <span style="color: #6B7280; font-size: 0.875rem;">
                                        <i class="fas fa-eye"></i> Solo lectura
                                    </span>
                                    <% } %>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty tratamientos}">
                            <tr>
                                <td colspan="8">
                                    <div class="empty-state">
                                        <i class="fas fa-tooth" style="font-size: 4rem; color: #D1D5DB;"></i>
                                        <h3>No hay tratamientos registrados</h3>
                                        <p>Comienza creando el primer tratamiento</p>
                                        <% if (usuarioTratamiento.tieneAccesoTotal()) { %>
                                        <a href="tratamiento?action=nuevo" class="btn btn-primary">
                                            <i class="fas fa-plus"></i> Nuevo Tratamiento
                                        </a>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>

                <c:if test="${not empty tratamientos}">
                    <div class="table-footer">
                        <p class="table-info">
                            <i class="fas fa-info-circle"></i>
                            Total de tratamientos: <strong>${tratamientos.size()}</strong>
                        </p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmarEliminacion(id, nombre) {
        if (confirm('¿Está seguro de eliminar el tratamiento "' + nombre + '"?\n\nEsta acción no se puede deshacer.')) {
            window.location.href = 'tratamiento?action=eliminar&id=' + id;
        }
    }

    // Auto-hide alerts
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