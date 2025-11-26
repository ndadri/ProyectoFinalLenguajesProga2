<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 17/nov/2025
  Time: 09:02 p. m.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Cita" %>
<%
    List<Cita> listaCitas = (List<Cita>) request.getAttribute("listaCitas");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Citas Programadas</title>
</head>
<body>

<h2>Citas</h2>

<table border="1">
    <thead>
    <tr>
        <th>ID Cita</th>
        <th>ID Paciente</th>
        <th>ID Médico</th>
        <th>Fecha y Hora</th>
        <th>Motivo</th>
        <th>Estado</th>
    </tr>
    </thead>

    <tbody>
    <%
        if (listaCitas != null) {
            for (Cita cita : listaCitas) {
    %>
    <tr>
        <td><%= cita.getId() %></td>
        <td><%= cita.getPacienteId() %></td>
        <td><%= cita.getMedicoId() %></td>
        <td><%= cita.getFechaHora() %></td>
        <td><%= cita.getMotivo() %></td>
        <td><%= cita.getEstado() %></td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

</body>
</html>