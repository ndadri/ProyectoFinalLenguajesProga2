<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 17/nov/2025
  Time: 09:02 p. m.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Paciente" %>
<%
    List<Paciente> pacientes = (List<Paciente>) request.getAttribute("pacientes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Pacientes</title>
</head>
<body>

<h2>Pacientes Registrados</h2>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Nombres</th>
        <th>Apellidos</th>
        <th>Teléfono</th>
        <th>Email</th>
    </tr>
    </thead>

    <tbody>
    <%
        if (pacientes != null) {
            for (Paciente paciente : pacientes) {
    %>
    <tr>
        <td><%= paciente.getId() %></td>
        <td><%= paciente.getNombres() %></td>
        <td><%= paciente.getApellidos() %></td>
        <td><%= paciente.getTelefono() %></td>
        <td><%= paciente.getEmail() %></td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

</body>
</html>
