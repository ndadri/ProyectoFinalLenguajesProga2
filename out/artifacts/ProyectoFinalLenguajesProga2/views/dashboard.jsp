<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Usuario" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
    if (usuarioActual == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }

        .stat-card {
            background: white;
            border-radius: 16px;
            padding: 1.5rem;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            display: flex;
            align-items: center;
            gap: 1rem;
            transition: transform 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }

        .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            color: white;
        }

        .stat-icon.blue {
            background: linear-gradient(135deg, #3B82F6, #60A5FA);
        }

        .stat-icon.purple {
            background: linear-gradient(135deg, #A855F7, #C084FC);
        }

        .stat-icon.green {
            background: linear-gradient(135deg, #10B981, #34D399);
        }

        .stat-icon.orange {
            background: linear-gradient(135deg, #F97316, #FB923C);
        }

        .stat-content h3 {
            color: #6B7280;
            font-size: 14px;
            font-weight: 500;
            margin-bottom: 0.25rem;
        }

        .stat-content p {
            font-size: 28px;
            font-weight: 700;
            color: #1F2937;
        }

        .welcome-banner {
            background: linear-gradient(135deg, #3B82F6, #A855F7);
            color: white;
            padding: 2rem;
            border-radius: 16px;
            margin-bottom: 2rem;
            position: relative;
        }

        .welcome-banner h1 {
            font-size: 32px;
            margin-bottom: 0.5rem;
        }

        .user-info-box {
            position: absolute;
            top: 1.5rem;
            right: 1.5rem;
            display: flex;
            align-items: center;
            gap: 1rem;
            background: rgba(255, 255, 255, 0.95);
            padding: 0.75rem 1.5rem;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .user-info-box .user-details {
            text-align: right;
        }

        .user-info-box .user-name {
            font-weight: 600;
            color: #1F2937;
            font-size: 0.95rem;
        }

        .user-info-box .user-role {
            font-size: 0.8rem;
            color: #6B7280;
        }

        .user-info-box .user-avatar {
            width: 45px;
            height: 45px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 600;
            font-size: 18px;
        }

        .user-info-box .logout-btn {
            color: #EF4444;
            text-decoration: none;
            font-size: 1.2rem;
            margin-left: 0.5rem;
            transition: transform 0.2s;
        }

        .user-info-box .logout-btn:hover {
            transform: scale(1.1);
        }

        .recent-section {
            background: white;
            border-radius: 16px;
            padding: 1.5rem;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .recent-section h2 {
            font-size: 20px;
            font-weight: 700;
            margin-bottom: 1rem;
            color: #1F2937;
        }

        .cita-item {
            padding: 1rem;
            border-bottom: 1px solid #E5E7EB;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .cita-item:last-child {
            border-bottom: none;
        }

        .cita-info h4 {
            font-size: 15px;
            font-weight: 600;
            color: #1F2937;
            margin-bottom: 0.25rem;
        }

        .cita-info p {
            font-size: 13px;
            color: #6B7280;
        }

        @media (max-width: 768px) {
            .user-info-box {
                position: static;
                margin-bottom: 1rem;
                justify-content: center;
            }

            .welcome-banner {
                text-align: center;
            }
        }
    </style>
</head>
<body>
<!-- Incluir menú lateral -->
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <!-- Header -->
    <jsp:include page="includes/header.jsp" />

    <!-- Contenido Principal -->
    <div class="container">
        <!-- Banner de bienvenida con info de usuario -->
        <div class="welcome-banner">
            <!-- Info del usuario -->
            <div class="user-info-box">
                <div class="user-details">
                    <div class="user-name"><%= usuarioActual.getUsuario() %></div>
                    <div class="user-role"><%= usuarioActual.getTipoUsuarioNombre() %></div>
                </div>
                <div class="user-avatar">
                    <%= usuarioActual.getUsuario().substring(0, 1).toUpperCase() %>
                </div>
                <a href="logout" class="logout-btn" title="Cerrar sesión">
                    <i class="fas fa-sign-out-alt"></i>
                </a>
            </div>

            <h1><i class="fas fa-hand-sparkles"></i> ¡Bienvenido de vuelta!</h1>
            <p>Aquí está un resumen de tu clínica dental hoy</p>
        </div>

        <!-- Estadísticas -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon blue">
                    <i class="fas fa-users"></i>
                </div>
                <div class="stat-content">
                    <h3>Total Pacientes</h3>
                    <p>${totalPacientes != null ? totalPacientes : 0}</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon purple">
                    <i class="fas fa-calendar-day"></i>
                </div>
                <div class="stat-content">
                    <h3>Citas Hoy</h3>
                    <p>${citasHoy != null ? citasHoy : 0}</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon green">
                    <i class="fas fa-calendar-check"></i>
                </div>
                <div class="stat-content">
                    <h3>Citas Pendientes</h3>
                    <p>${citasPendientes != null ? citasPendientes : 0}</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon orange">
                    <i class="fas fa-tooth"></i>
                </div>
                <div class="stat-content">
                    <h3>Tratamientos</h3>
                    <p>24</p>
                </div>
            </div>
        </div>

        <!-- Acciones rápidas -->
        <div class="card" style="margin-bottom: 2rem;">
            <div class="card-body">
                <h2 style="font-size: 20px; font-weight: 700; margin-bottom: 1rem; color: #1F2937;">
                    <i class="fas fa-bolt"></i> Acciones Rápidas
                </h2>
                <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                    <% if (usuarioActual.tieneAccesoTotal()) { %>
                    <a href="paciente?action=nuevo" class="btn btn-primary">
                        <i class="fas fa-user-plus"></i> Nuevo Paciente
                    </a>
                    <% } %>
                    <a href="cita?action=nuevo" class="btn btn-primary">
                        <i class="fas fa-calendar-plus"></i> Agendar Cita
                    </a>
                    <a href="consulta?action=nuevo" class="btn btn-secondary">
                        <i class="fas fa-stethoscope"></i> Nueva Consulta
                    </a>
                </div>
            </div>
        </div>

        <!-- Citas recientes -->
        <div class="recent-section">
            <h2><i class="fas fa-history"></i> Citas Recientes</h2>

            <c:choose>
                <c:when test="${empty citasRecientes}">
                    <div class="empty-state" style="padding: 2rem;">
                        <i class="fas fa-calendar-times"></i>
                        <h3>No hay citas recientes</h3>
                        <p>Las citas aparecerán aquí cuando se agenden</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="cita" items="${citasRecientes}">
                        <div class="cita-item">
                            <div class="cita-info">
                                <h4>${cita.pacienteNombre}</h4>
                                <p>
                                    <i class="fas fa-user-md"></i> ${cita.odontologoNombre} |
                                    <i class="fas fa-clock"></i> <fmt:formatDate value="${cita.fechaHora}" pattern="dd/MM/yyyy HH:mm"/>
                                </p>
                            </div>
                            <span class="badge badge-${cita.estado == 'Completada' ? 'success' : 'warning'}">
                                    ${cita.estado}
                            </span>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>