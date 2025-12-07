<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 10:34 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Cita" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Citas - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .filter-bar {
            background: white;
            padding: 1rem 1.5rem;
            border-radius: 12px;
            margin-bottom: 1.5rem;
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
            align-items: center;
        }

        .filter-btn {
            padding: 0.5rem 1rem;
            border-radius: 8px;
            border: none;
            background: #F3F4F6;
            color: #6B7280;
            cursor: pointer;
            transition: all 0.3s;
            font-weight: 500;
            text-decoration: none;
            display: inline-block;
        }

        .filter-btn:hover {
            background: #E5E7EB;
        }

        .filter-btn.active {
            background: linear-gradient(135deg, #3B82F6, #A855F7);
            color: white;
        }

        .cita-actions {
            display: flex;
            gap: 0.5rem;
            flex-wrap: wrap;
        }

        .btn-sm {
            padding: 0.4rem 0.8rem;
            font-size: 12px;
            border-radius: 6px;
        }
    </style>
</head>
<body>
<jsp:include page="includes/sidebar.jsp" />

<div class="main-content">
    <jsp:include page="includes/header.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <i class="fas fa-calendar-alt"></i> Gestión de Citas
                </h1>
                <p class="page-subtitle">Administra las citas de la clínica</p>
            </div>
            <a href="cita?action=nuevo" class="btn btn-primary">
                <i class="fas fa-plus"></i> Nueva Cita
            </a>
        </div>

        <!-- Mensajes -->
        <%
            String mensaje = (String) session.getAttribute("mensaje");
            String tipoMensaje = (String) session.getAttribute("tipoMensaje");

            if (mensaje != null) {
        %>
        <div class="alert alert-<%= tipoMensaje %>">
            <i class="fas fa-<%= "success".equals(tipoMensaje) ? "check-circle" : "exclamation-circle" %>"></i>
            <%= mensaje %>
            <button class="close-alert" onclick="this.parentElement.style.display='none'">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <%
                session.removeAttribute("mensaje");
                session.removeAttribute("tipoMensaje");
            }
        %>

        <!-- Filtros -->
        <%
            String estadoFiltro = (String) request.getAttribute("estadoFiltro");
            Boolean soloHoy = (Boolean) request.getAttribute("soloHoy");
        %>

        <div class="filter-bar">
            <span style="font-weight: 600; color: #374151;">Filtrar por:</span>
            <a href="cita?action=listar" class="filter-btn <%= estadoFiltro == null && soloHoy == null ? "active" : "" %>">
                <i class="fas fa-list"></i> Todas
            </a>
            <a href="cita?action=hoy" class="filter-btn <%= soloHoy != null ? "active" : "" %>">
                <i class="fas fa-calendar-day"></i> Hoy
            </a>
            <a href="cita?action=listar&estado=Programada" class="filter-btn <%= "Programada".equals(estadoFiltro) ? "active" : "" %>">
                <i class="fas fa-clock"></i> Programadas
            </a>
            <a href="cita?action=listar&estado=Confirmada" class="filter-btn <%= "Confirmada".equals(estadoFiltro) ? "active" : "" %>">
                <i class="fas fa-check"></i> Confirmadas
            </a>
            <a href="cita?action=listar&estado=Completada" class="filter-btn <%= "Completada".equals(estadoFiltro) ? "active" : "" %>">
                <i class="fas fa-check-double"></i> Completadas
            </a>
            <a href="cita?action=listar&estado=Cancelada" class="filter-btn <%= "Cancelada".equals(estadoFiltro) ? "active" : "" %>">
                <i class="fas fa-times"></i> Canceladas
            </a>
        </div>

        <!-- Tabla de citas -->
        <div class="card">
            <div class="card-body">
                <%
                    @SuppressWarnings("unchecked")
                    List<Cita> citas = (List<Cita>) request.getAttribute("citas");

                    if (citas == null || citas.isEmpty()) {
                %>
                <div class="empty-state">
                    <i class="fas fa-calendar-times"></i>
                    <h3>No hay citas registradas</h3>
                    <p>Comienza agendando la primera cita</p>
                    <a href="cita?action=nuevo" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Agendar Cita
                    </a>
                </div>
                <%
                } else {
                %>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Fecha</th>
                            <th>Hora</th>
                            <th>Paciente</th>
                            <th>Teléfono</th>
                            <th>Odontólogo</th>
                            <th>Motivo</th>
                            <th>Tipo</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            for (Cita c : citas) {
                        %>
                        <tr>
                            <td><%= c.getCitaId() %></td>
                            <td><%= c.getFechaFormateada() %></td>
                            <td><strong><%= c.getHoraFormateada() %></strong></td>
                            <td><%= c.getPacienteNombre() %></td>
                            <td><%= c.getPacienteTelefono() != null ? c.getPacienteTelefono() : "-" %></td>
                            <td><%= c.getOdontologoNombre() %></td>
                            <td><%= c.getMotivo() != null ? c.getMotivo() : "-" %></td>
                            <td><span class="badge badge-info"><%= c.getTipoCita() %></span></td>
                            <td>
                                                <span class="badge badge-<%= c.getEstadoColor() %>">
                                                    <%= c.getEstado() %>
                                                </span>
                            </td>
                            <td>
                                <div class="cita-actions">
                                    <% if ("Programada".equals(c.getEstado())) { %>
                                    <a href="cita?action=confirmar&id=<%= c.getCitaId() %>"
                                       class="btn btn-primary btn-sm"
                                       title="Confirmar">
                                        <i class="fas fa-check"></i>
                                    </a>
                                    <% } %>

                                    <% if ("Confirmada".equals(c.getEstado()) || "Programada".equals(c.getEstado())) { %>
                                    <a href="cita?action=completar&id=<%= c.getCitaId() %>"
                                       class="btn btn-secondary btn-sm"
                                       title="Completar">
                                        <i class="fas fa-check-double"></i>
                                    </a>
                                    <% } %>

                                    <a href="cita?action=editar&id=<%= c.getCitaId() %>"
                                       class="btn-icon btn-edit"
                                       title="Editar">
                                        <i class="fas fa-edit"></i>
                                    </a>

                                    <% if (!"Cancelada".equals(c.getEstado()) && !"Completada".equals(c.getEstado())) { %>
                                    <button onclick="cancelarCita(<%= c.getCitaId() %>)"
                                            class="btn-icon"
                                            style="background: #FEF3C7; color: #92400E;"
                                            title="Cancelar">
                                        <i class="fas fa-ban"></i>
                                    </button>
                                    <% } %>

                                    <button onclick="confirmarEliminacion(<%= c.getCitaId() %>, '<%= c.getPacienteNombre() %>')"
                                            class="btn-icon btn-delete"
                                            title="Eliminar">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>

                <div class="table-footer">
                    <p class="table-info">
                        Mostrando <strong><%= citas.size() %></strong> cita(s)
                    </p>
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmarEliminacion(id, paciente) {
        if (confirm('¿Está seguro que desea eliminar la cita de ' + paciente + '?')) {
            window.location.href = 'cita?action=eliminar&id=' + id;
        }
    }

    function cancelarCita(id) {
        if (confirm('¿Está seguro que desea cancelar esta cita?')) {
            window.location.href = 'cita?action=cancelar&id=' + id;
        }
    }

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
