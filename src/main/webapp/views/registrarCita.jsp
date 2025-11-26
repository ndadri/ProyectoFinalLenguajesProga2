<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 17/nov/2025
  Time: 09:42 p. m.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar Cita</title>
</head>
<body>

<h2>Nueva Cita</h2>

<form action="registrarCita" method="POST">
    <label>ID Paciente:</label><br>
    <input type="number" name="paciente_id" required><br><br>

    <label>ID Médico:</label><br>
    <input type="number" name="medico_id" required><br><br>

    <label>Fecha y Hora:</label><br>
    <input type="datetime-local" name="fecha_hora" required><br><br>

    <label>Motivo:</label><br>
    <textarea name="motivo" required></textarea><br><br>

    <button type="submit">Crear Cita</button>
</form>

</body>
</html>