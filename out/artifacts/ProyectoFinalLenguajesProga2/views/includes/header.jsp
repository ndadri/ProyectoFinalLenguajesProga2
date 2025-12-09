<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 05/dic/2025
  Time: 05:51 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Include: includes/header.jsp
Propósito: Cabecera de la aplicación que muestra información del usuario (nombre, rol) y control para mostrar/ocultar el sidebar en dispositivos móviles.
Variables/atributos esperados en session:
- sessionScope.usuario.usuario (nombre de usuario)
- sessionScope.usuario.tipoUsuarioNombre (nombre del rol)
Comportamiento JS:
- #toggleSidebar alterna la clase 'active' en el elemento .sidebar (útil en móviles).
-->
<header class="header">
    <div class="header-left">
        <button class="btn-icon" id="toggleSidebar" style="display: none;">
            <i class="fas fa-bars"></i>
        </button>
    </div>

    <div class="header-user">
        <div class="user-info" style="text-align: right; margin-right: 1rem;">
            <div style="font-weight: 600; font-size: 14px; color: var(--text-dark);">
                ${sessionScope.usuario.usuario != null ? sessionScope.usuario.usuario : 'Usuario'}
            </div>
            <div style="font-size: 12px; color: var(--text-gray);">
                ${sessionScope.usuario.tipoUsuarioNombre != null ? sessionScope.usuario.tipoUsuarioNombre : 'Rol'}
            </div>
        </div>
        <div class="user-avatar">
            <c:choose>
                <c:when test="${not empty sessionScope.usuario.usuario}">
                    ${sessionScope.usuario.usuario.substring(0, 1).toUpperCase()}
                </c:when>
                <c:otherwise>
                    U
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>

<script>
    // Toggle sidebar en móvil
    document.getElementById('toggleSidebar')?.addEventListener('click', function() {
        document.querySelector('.sidebar').classList.toggle('active');
    });
</script>