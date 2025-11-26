<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 17/nov/2025
  Time: 09:02 p. m.
  To change this template use File | Settings | File Templates.
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
        <th>Nombre</th>
        <th>Apellido</th>
        <th>Telefono</th>
        <th>Email</th>
    </tr>
    </thead>

    <tbody>
    <!-- Aquí el servlet imprimirá los pacientes -->
    </tbody>
</table>

</body>
</html>

