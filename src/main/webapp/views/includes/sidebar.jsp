<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 05/dic/2025
  Time: 05:50 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="sidebar">
    <!-- Logo -->
    <div class="sidebar-logo">
        <div class="logo-content">
            <div class="logo-icon">
                <i class="fas fa-tooth"></i>
            </div>
            <span class="logo-text">DentalCare</span>
        </div>
    </div>

    <!-- Menú -->
    <nav class="sidebar-menu">
        <a href="${pageContext.request.contextPath}/dashboard"
           class="menu-item ${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''}">
            <i class="fas fa-home"></i>
            <span>Dashboard</span>
        </a>

        <a href="${pageContext.request.contextPath}/paciente?action=listar"
           class="menu-item ${pageContext.request.requestURI.contains('paciente') ? 'active' : ''}">
            <i class="fas fa-users"></i>
            <span>Pacientes</span>
        </a>

        <a href="${pageContext.request.contextPath}/cita?action=listar"
           class="menu-item ${pageContext.request.requestURI.contains('cita') ? 'active' : ''}">
            <i class="fas fa-calendar-alt"></i>
            <span>Citas</span>
        </a>

        <c:if test="${sessionScope.usuario.tipoId == 1 || sessionScope.usuario.tipoId == 2}">
            <a href="${pageContext.request.contextPath}/consulta?action=listar"
               class="menu-item ${pageContext.request.requestURI.contains('consulta') ? 'active' : ''}">
                <i class="fas fa-stethoscope"></i>
                <span>Consultas</span>
            </a>
        </c:if>

        <c:if test="${sessionScope.usuario.tipoId == 1}">
            <a href="${pageContext.request.contextPath}/odontologo?action=listar"
               class="menu-item ${pageContext.request.requestURI.contains('odontologo') ? 'active' : ''}">
                <i class="fas fa-user-md"></i>
                <span>Odontólogos</span>
            </a>

            <a href="${pageContext.request.contextPath}/tratamiento?action=listar"
               class="menu-item ${pageContext.request.requestURI.contains('tratamiento') ? 'active' : ''}">
                <i class="fas fa-tooth"></i>
                <span>Tratamientos</span>
            </a>

            <a href="${pageContext.request.contextPath}/odontograma?action=listar" class="menu-item">
                <i class="fas fa-tooth"></i>
                <span>Odontogramas</span>
            </a>

            <a href="${pageContext.request.contextPath}/factura?action=listar"
               class="menu-item ${pageContext.request.requestURI.contains('factura') ? 'active' : ''}">
                <i class="fas fa-file-invoice-dollar"></i>
                <span>Facturación</span>
            </a>

            <a href="${pageContext.request.contextPath}/reportes"
               class="menu-item ${pageContext.request.requestURI.contains('reportes') ? 'active' : ''}">
                <i class="fas fa-chart-bar"></i>
                <span>Reportes</span>
            </a>
        </c:if>

        <hr style="margin: 1rem 0; border: none; border-top: 1px solid var(--border-color);">

        <a href="${pageContext.request.contextPath}/configuracion"
           class="menu-item ${pageContext.request.requestURI.contains('configuracion') ? 'active' : ''}">
            <i class="fas fa-cog"></i>
            <span>Configuración</span>
        </a>

        <a href="${pageContext.request.contextPath}/logout" class="menu-item">
            <i class="fas fa-sign-out-alt"></i>
            <span>Cerrar Sesión</span>
        </a>
    </nav>
</div>
