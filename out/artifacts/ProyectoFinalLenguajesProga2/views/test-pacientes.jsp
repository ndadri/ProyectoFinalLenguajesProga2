<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Paciente" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test Pacientes</title>
    <style>
        table { border-collapse: collapse; width: 100%; margin: 20px 0; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
        .error { color: red; font-weight: bold; }
        .success { color: green; font-weight: bold; }
    </style>
</head>
<body>
<h1>Test de Lista de Pacientes</h1>

<h2>Debug Info:</h2>
<p>Atributo 'pacientes' es null? <strong>${pacientes == null}</strong></p>
<p>Lista está vacía? <strong>${empty pacientes}</strong></p>
<p>Cantidad de pacientes: <strong>${pacientes != null ? pacientes.size() : 'NULL'}</strong></p>

<%
    // Debug en Java
    Object pacientesObj = request.getAttribute("pacientes");
    if (pacientesObj != null) {
        List<Paciente> lista = (List<Paciente>) pacientesObj;
        out.println("<p>Debug Java: Lista tiene " + lista.size() + " elementos</p>");

        for (int i = 0; i < lista.size(); i++) {
            Paciente p = lista.get(i);
            out.println("<p>Elemento " + i + ": " + (p == null ? "<span class='error'>NULL</span>" : "<span class='success'>Objeto OK</span>") + "</p>");

            if (p != null) {
                out.println("<ul>");
                out.println("<li>ID: " + p.getPacienteId() + "</li>");
                out.println("<li>Nombres: " + p.getNombres() + "</li>");
                out.println("<li>Apellidos: " + p.getApellidos() + "</li>");
                out.println("<li>Cédula: " + p.getCedula() + "</li>");
                out.println("</ul>");
            }
        }
    } else {
        out.println("<p class='error'>El atributo 'pacientes' es NULL en el request</p>");
    }
%>

<hr>

<h2>Pacientes (usando JSTL):</h2>
<c:choose>
    <c:when test="${not empty pacientes}">
        <table>
            <tr>
                <th>Índice</th>
                <th>Objeto null?</th>
                <th>ID</th>
                <th>Nombres</th>
                <th>Apellidos</th>
                <th>Cédula</th>
            </tr>
            <c:forEach var="p" items="${pacientes}" varStatus="status">
                <tr>
                    <td>${status.index}</td>
                    <td>${p == null ? 'NULL' : 'OK'}</td>
                    <td>${p.pacienteId}</td>
                    <td>${p.nombres}</td>
                    <td>${p.apellidos}</td>
                    <td>${p.cedula}</td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p class="error">No hay pacientes o la lista es null</p>
    </c:otherwise>
</c:choose>

<hr>
<a href="paciente?action=listar">Ir a vista normal</a> |
<a href="dbtest">Ir a diagnóstico completo</a>
</body>
</html>