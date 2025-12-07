<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 07/dic/2025
  Time: 12:52 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Odontograma - DentalCare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .odontograma-container {
            background: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .cuadrante {
            margin-bottom: 2rem;
        }

        .cuadrante-label {
            text-align: center;
            font-weight: 600;
            color: #374151;
            margin-bottom: 1rem;
            font-size: 1.1rem;
        }

        .dientes-row {
            display: flex;
            justify-content: center;
            gap: 8px;
            margin-bottom: 0.5rem;
        }

        .diente {
            width: 60px;
            height: 60px;
            border: 2px solid #D1D5DB;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.2s;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            position: relative;
        }

        .diente:hover {
            transform: scale(1.1);
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }

        .diente-numero {
            font-size: 0.75rem;
            font-weight: 600;
            color: #6B7280;
        }

        .diente-icon {
            font-size: 1.5rem;
        }

        /* Estados de dientes */
        .estado-Sano {
            background: #D1FAE5;
            border-color: #10B981;
            color: #065F46;
        }

        .estado-Caries {
            background: #FEE2E2;
            border-color: #EF4444;
            color: #991B1B;
        }

        .estado-Obturado {
            background: #DBEAFE;
            border-color: #3B82F6;
            color: #1E40AF;
        }

        .estado-Endodoncia {
            background: #EDE9FE;
            border-color: #8B5CF6;
            color: #5B21B6;
        }

        .estado-Corona {
            background: #FEF3C7;
            border-color: #F59E0B;
            color: #92400E;
        }

        .estado-Extraccion {
            background: #F3F4F6;
            border-color: #6B7280;
            color: #374151;
        }

        .estado-Ausente {
            background: #F9FAFB;
            border-color: #E5E7EB;
            color: #9CA3AF;
            opacity: 0.5;
        }

        .estado-Implante {
            background: #CCFBF1;
            border-color: #14B8A6;
            color: #115E59;
        }

        .leyenda {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
            margin-top: 2rem;
            padding: 1rem;
            background: #F9FAFB;
            border-radius: 8px;
        }

        .leyenda-item {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .leyenda-color {
            width: 24px;
            height: 24px;
            border-radius: 4px;
            border: 2px solid;
        }

        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            align-items: center;
            justify-content: center;
        }

        .modal.active {
            display: flex;
        }

        .modal-content {
            background: white;
            padding: 2rem;
            border-radius: 12px;
            max-width: 500px;
            width: 90%;
            max-height: 90vh;
            overflow-y: auto;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .modal-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #1F2937;
        }

        .close-modal {
            background: none;
            border: none;
            font-size: 1.5rem;
            cursor: pointer;
            color: #6B7280;
        }

        .estado-option {
            padding: 0.75rem;
            margin-bottom: 0.5rem;
            border: 2px solid #E5E7EB;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.2s;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .estado-option:hover {
            border-color: #3B82F6;
            background: #EFF6FF;
        }

        .estado-option.selected {
            border-color: #3B82F6;
            background: #DBEAFE;
        }
    </style>
</head>
<body>
<div class="container">
    <%
        Odontograma odontograma = (Odontograma) request.getAttribute("odontograma");
        List<Diente> dientes = (List<Diente>) request.getAttribute("dientes");

        // Crear mapa de dientes para acceso rápido
        Map<String, Diente> mapaDientes = new HashMap<>();
        if (dientes != null) {
            for (Diente d : dientes) {
                mapaDientes.put(d.getNumeroDiente(), d);
            }
        }
    %>

    <div class="page-header">
        <div>
            <h1 class="page-title">
                <i class="fas fa-tooth"></i> Odontograma - <%= odontograma.getPacienteNombre() %>
            </h1>
            <p class="page-subtitle">
                Cédula: <%= odontograma.getPacienteCedula() %> |
                Fecha: <%= odontograma.getFechaFormateada() %>
            </p>
        </div>
        <a href="odontograma?action=listar" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Volver
        </a>
    </div>

    <div class="odontograma-container">
        <h3 style="text-align: center; color: #374151; margin-bottom: 2rem;">
            <i class="fas fa-teeth"></i> Dentadura Permanente (32 dientes)
        </h3>

        <!-- ARCADA SUPERIOR -->
        <div style="border: 2px solid #E5E7EB; border-radius: 12px; padding: 1.5rem; margin-bottom: 2rem;">
            <!-- Cuadrante 1 (Superior Derecho) -->
            <div class="cuadrante">
                <div class="cuadrante-label">Cuadrante 1 - Superior Derecho</div>
                <div class="dientes-row">
                    <%
                        String[] cuadrante1 = {"18", "17", "16", "15", "14", "13", "12", "11"};
                        for (String num : cuadrante1) {
                            Diente d = mapaDientes.get(num);
                            String estado = d != null ? d.getEstado() : "Sano";
                    %>
                    <div class="diente estado-<%= estado %>" onclick="editarDiente('<%= num %>', '<%= estado %>')">
                        <div class="diente-numero"><%= num %></div>
                        <div class="diente-icon"><i class="fas fa-tooth"></i></div>
                    </div>
                    <% } %>
                </div>
            </div>

            <!-- Cuadrante 2 (Superior Izquierdo) -->
            <div class="cuadrante">
                <div class="cuadrante-label">Cuadrante 2 - Superior Izquierdo</div>
                <div class="dientes-row">
                    <%
                        String[] cuadrante2 = {"21", "22", "23", "24", "25", "26", "27", "28"};
                        for (String num : cuadrante2) {
                            Diente d = mapaDientes.get(num);
                            String estado = d != null ? d.getEstado() : "Sano";
                    %>
                    <div class="diente estado-<%= estado %>" onclick="editarDiente('<%= num %>', '<%= estado %>')">
                        <div class="diente-numero"><%= num %></div>
                        <div class="diente-icon"><i class="fas fa-tooth"></i></div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>

        <!-- ARCADA INFERIOR -->
        <div style="border: 2px solid #E5E7EB; border-radius: 12px; padding: 1.5rem;">
            <!-- Cuadrante 4 (Inferior Derecho) -->
            <div class="cuadrante">
                <div class="cuadrante-label">Cuadrante 4 - Inferior Derecho</div>
                <div class="dientes-row">
                    <%
                        String[] cuadrante4 = {"48", "47", "46", "45", "44", "43", "42", "41"};
                        for (String num : cuadrante4) {
                            Diente d = mapaDientes.get(num);
                            String estado = d != null ? d.getEstado() : "Sano";
                    %>
                    <div class="diente estado-<%= estado %>" onclick="editarDiente('<%= num %>', '<%= estado %>')">
                        <div class="diente-numero"><%= num %></div>
                        <div class="diente-icon"><i class="fas fa-tooth"></i></div>
                    </div>
                    <% } %>
                </div>
            </div>

            <!-- Cuadrante 3 (Inferior Izquierdo) -->
            <div class="cuadrante">
                <div class="cuadrante-label">Cuadrante 3 - Inferior Izquierdo</div>
                <div class="dientes-row">
                    <%
                        String[] cuadrante3 = {"31", "32", "33", "34", "35", "36", "37", "38"};
                        for (String num : cuadrante3) {
                            Diente d = mapaDientes.get(num);
                            String estado = d != null ? d.getEstado() : "Sano";
                    %>
                    <div class="diente estado-<%= estado %>" onclick="editarDiente('<%= num %>', '<%= estado %>')">
                        <div class="diente-numero"><%= num %></div>
                        <div class="diente-icon"><i class="fas fa-tooth"></i></div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>

        <!-- LEYENDA -->
        <div class="leyenda">
            <div class="leyenda-item">
                <div class="leyenda-color estado-Sano"></div>
                <span>Sano</span>
            </div>
            <div class="leyenda-item">
                <div class="leyenda-color estado-Caries"></div>
                <span>Caries</span>
            </div>
            <div class="leyenda-item">
                <div class="leyenda-color estado-Obturado"></div>
                <span>Obturado</span>
            </div>
            <div class="leyenda-item">
                <div class="leyenda-color estado-Endodoncia"></div>
                <span>Endodoncia</span>
            </div>
            <div class="leyenda-item">
                <div class="leyenda-color estado-Corona"></div>
                <span>Corona</span>
            </div>
            <div class="leyenda-item">
                <div class="leyenda-color estado-Extraccion"></div>
                <span>Extracción</span>
            </div>
            <div class="leyenda-item">
                <div class="leyenda-color estado-Ausente"></div>
                <span>Ausente</span>
            </div>
            <div class="leyenda-item">
                <div class="leyenda-color estado-Implante"></div>
                <span>Implante</span>
            </div>
        </div>
    </div>

    <!-- Observaciones -->
    <div class="card" style="margin-top: 2rem;">
        <div class="card-body">
            <h3 style="margin-bottom: 1rem; color: #374151;">
                <i class="fas fa-clipboard"></i> Observaciones Generales
            </h3>
            <form action="odontograma" method="post">
                <input type="hidden" name="action" value="actualizar">
                <input type="hidden" name="odontograma_id" value="<%= odontograma.getOdontogramaId() %>">
                <div class="form-group">
                    <textarea name="observaciones" class="form-control" rows="4"
                              placeholder="Observaciones generales sobre el estado dental del paciente"><%= odontograma.getObservaciones() != null ? odontograma.getObservaciones() : "" %></textarea>
                </div>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Guardar Observaciones
                </button>
            </form>
        </div>
    </div>
</div>

<!-- MODAL EDITAR DIENTE -->
<div id="modalDiente" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3 class="modal-title">
                <i class="fas fa-tooth"></i> Editar Diente <span id="modal-numero-diente"></span>
            </h3>
            <button class="close-modal" onclick="cerrarModal()">
                <i class="fas fa-times"></i>
            </button>
        </div>

        <div>
            <label style="font-weight: 600; margin-bottom: 1rem; display: block;">Estado del Diente:</label>

            <div class="estado-option" data-estado="Sano">
                <div class="leyenda-color estado-Sano"></div>
                <div>
                    <strong>Sano</strong>
                    <p style="margin: 0; font-size: 0.875rem; color: #6B7280;">Sin problemas detectados</p>
                </div>
            </div>

            <div class="estado-option" data-estado="Caries">
                <div class="leyenda-color estado-Caries"></div>
                <div>
                    <strong>Caries</strong>
                    <p style="margin: 0; font-size: 0.875rem; color: #6B7280;">Presenta caries dental</p>
                </div>
            </div>

            <div class="estado-option" data-estado="Obturado">
                <div class="leyenda-color estado-Obturado"></div>
                <div>
                    <strong>Obturado</strong>
                    <p style="margin: 0; font-size: 0.875rem; color: #6B7280;">Tiene obturación/relleno</p>
                </div>
            </div>

            <div class="estado-option" data-estado="Endodoncia">
                <div class="leyenda-color estado-Endodoncia"></div>
                <div>
                    <strong>Endodoncia</strong>
                    <p style="margin: 0; font-size: 0.875rem; color: #6B7280;">Tratamiento de conducto</p>
                </div>
            </div>

            <div class="estado-option" data-estado="Corona">
                <div class="leyenda-color estado-Corona"></div>
                <div>
                    <strong>Corona</strong>
                    <p style="margin: 0; font-size: 0.875rem; color: #6B7280;">Tiene corona dental</p>
                </div>
            </div>

            <div class="estado-option" data-estado="Extraccion">
                <div class="leyenda-color estado-Extraccion"></div>
                <div>
                    <strong>Extracción</strong>
                    <p style="margin: 0; font-size: 0.875rem; color: #6B7280;">Programado para extracción</p>
                </div>
            </div>

            <div class="estado-option" data-estado="Ausente">
                <div class="leyenda-color estado-Ausente"></div>
                <div>
                    <strong>Ausente</strong>
                    <p style="margin: 0; font-size: 0.875rem; color: #6B7280;">Diente faltante</p>
                </div>
            </div>

            <div class="estado-option" data-estado="Implante">
                <div class="leyenda-color estado-Implante"></div>
                <div>
                    <strong>Implante</strong>
                    <p style="margin: 0; font-size: 0.875rem; color: #6B7280;">Implante dental</p>
                </div>
            </div>

            <div class="form-group" style="margin-top: 1.5rem;">
                <label for="observaciones-diente">Observaciones del diente:</label>
                <textarea id="observaciones-diente" class="form-control" rows="3"
                          placeholder="Notas adicionales sobre este diente"></textarea>
            </div>

            <div style="display: flex; gap: 1rem; margin-top: 1.5rem;">
                <button class="btn btn-primary" onclick="guardarDiente()">
                    <i class="fas fa-save"></i> Guardar
                </button>
                <button class="btn btn-secondary" onclick="cerrarModal()">
                    <i class="fas fa-times"></i> Cancelar
                </button>
            </div>
        </div>
    </div>
</div>

<script>
    let dienteActual = null;
    let estadoSeleccionado = null;

    function editarDiente(numeroDiente, estadoActual) {
        dienteActual = numeroDiente;
        estadoSeleccionado = estadoActual;

        document.getElementById('modal-numero-diente').textContent = numeroDiente;
        document.getElementById('modalDiente').classList.add('active');

        // Marcar el estado actual
        document.querySelectorAll('.estado-option').forEach(option => {
            option.classList.remove('selected');
            if (option.dataset.estado === estadoActual) {
                option.classList.add('selected');
            }
        });
    }

    function cerrarModal() {
        document.getElementById('modalDiente').classList.remove('active');
        dienteActual = null;
        estadoSeleccionado = null;
    }

    // Click en opciones de estado
    document.querySelectorAll('.estado-option').forEach(option => {
        option.addEventListener('click', function() {
            document.querySelectorAll('.estado-option').forEach(opt => opt.classList.remove('selected'));
            this.classList.add('selected');
            estadoSeleccionado = this.dataset.estado;
        });
    });

    function guardarDiente() {
        if (!dienteActual || !estadoSeleccionado) {
            alert('Debe seleccionar un estado para el diente');
            return;
        }

        const observaciones = document.getElementById('observaciones-diente').value;

        console.log('[GUARDAR] Enviando:', {
            odontograma_id: '<%= odontograma.getOdontogramaId() %>',
            numero_diente: dienteActual,
            estado: estadoSeleccionado,
            observaciones: observaciones
        });

        // Crear parámetros URL-encoded
        const params = new URLSearchParams();
        params.append('action', 'guardar_diente');
        params.append('odontograma_id', '<%= odontograma.getOdontogramaId() %>');
        params.append('numero_diente', dienteActual);
        params.append('estado', estadoSeleccionado);
        params.append('observaciones', observaciones);

        fetch('odontograma', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params.toString()
        })
            .then(response => {
                console.log('[GUARDAR] Status:', response.status);
                return response.text();
            })
            .then(text => {
                console.log('[GUARDAR] Respuesta:', text);
                try {
                    const data = JSON.parse(text);
                    if (data.success) {
                        console.log('[GUARDAR] Éxito, recargando...');
                        cerrarModal();
                        location.reload();
                    } else {
                        alert('Error: ' + data.message);
                    }
                } catch (e) {
                    console.error('[GUARDAR] Error parsing JSON:', e);
                    console.log('[GUARDAR] Texto recibido:', text.substring(0, 200));
                    alert('Error en la respuesta del servidor');
                }
            })
            .catch(error => {
                console.error('[GUARDAR] Error de red:', error);
                alert('Error de conexión');
            });
    }

    // Cerrar modal al hacer click fuera
    document.getElementById('modalDiente').addEventListener('click', function(e) {
        if (e.target === this) {
            cerrarModal();
        }
    });
</script>
</body>
</html>
