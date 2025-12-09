<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    Usuario usuarioMenu = (Usuario) session.getAttribute("usuario");
    if (usuarioMenu == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

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
        <!-- Dashboard - TODOS -->
        <a href="${pageContext.request.contextPath}/dashboard"
           class="menu-item ${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''}">
            <i class="fas fa-home"></i>
            <span>Dashboard</span>
        </a>

        <!-- Pacientes - SOLO Admin y Recepción -->
        <% if (usuarioMenu.tieneAccesoTotal()) { %>
        <a href="${pageContext.request.contextPath}/paciente?action=listar"
           class="menu-item ${pageContext.request.requestURI.contains('paciente') ? 'active' : ''}">
            <i class="fas fa-users"></i>
            <span>Pacientes</span>
        </a>
        <% } %>

        <!-- Citas - TODOS (filtrado por rol) -->
        <a href="${pageContext.request.contextPath}/cita?action=listar"
           class="menu-item ${pageContext.request.requestURI.contains('cita') ? 'active' : ''}">
            <i class="fas fa-calendar-alt"></i>
            <span>Citas</span>
        </a>

        <!-- Consultas - TODOS (filtrado por rol) -->
        <a href="${pageContext.request.contextPath}/consulta?action=listar"
           class="menu-item ${pageContext.request.requestURI.contains('consulta') ? 'active' : ''}">
            <i class="fas fa-stethoscope"></i>
            <span>Consultas</span>
        </a>

        <!-- Odontólogos - SOLO Admin y Recepción -->
        <% if (usuarioMenu.tieneAccesoTotal()) { %>
        <a href="${pageContext.request.contextPath}/odontologo?action=listar"
           class="menu-item ${pageContext.request.requestURI.contains('odontologo') ? 'active' : ''}">
            <i class="fas fa-user-md"></i>
            <span>Odontólogos</span>
        </a>
        <% } %>

        <!-- Tratamientos - TODOS (solo lectura para odontólogos) -->
        <a href="${pageContext.request.contextPath}/tratamiento?action=listar"
           class="menu-item ${pageContext.request.requestURI.contains('tratamiento') ? 'active' : ''}">
            <i class="fas fa-tooth"></i>
            <span>Tratamientos</span>
        </a>

        <!-- Odontogramas - TODOS (filtrado por rol) -->
        <a href="${pageContext.request.contextPath}/odontograma?action=listar"
           class="menu-item ${pageContext.request.requestURI.contains('odontograma') ? 'active' : ''}">
            <i class="fas fa-teeth"></i>
            <span>Odontogramas</span>
        </a>

        <!-- Facturación - SOLO Admin y Recepción -->
        <% if (usuarioMenu.tieneAccesoTotal()) { %>
        <a href="${pageContext.request.contextPath}/factura?action=listar"
           class="menu-item ${pageContext.request.requestURI.contains('factura') ? 'active' : ''}">
            <i class="fas fa-file-invoice-dollar"></i>
            <span>Facturación</span>
        </a>
        <% } %>

        <!-- Reportes - SOLO Admin y Recepción -->
        <% if (usuarioMenu.tieneAccesoTotal()) { %>
        <a href="${pageContext.request.contextPath}/reportes"
           class="menu-item ${pageContext.request.requestURI.contains('reportes') ? 'active' : ''}">
            <i class="fas fa-chart-bar"></i>
            <span>Reportes</span>
        </a>
        <% } %>

        <hr style="margin: 1rem 0; border: none; border-top: 1px solid var(--border-color);">

        <!-- Configuración - SOLO Admin -->
        <% if (usuarioMenu.isAdmin()) { %>
        <a href="${pageContext.request.contextPath}/configuracion"
           class="menu-item ${pageContext.request.requestURI.contains('configuracion') ? 'active' : ''}">
            <i class="fas fa-cog"></i>
            <span>Configuración</span>
        </a>
        <% } %>

        <!-- Logout - TODOS -->
        <a href="${pageContext.request.contextPath}/logout" class="menu-item">
            <i class="fas fa-sign-out-alt"></i>
            <span>Cerrar Sesión</span>
        </a>
    </nav>
</div>