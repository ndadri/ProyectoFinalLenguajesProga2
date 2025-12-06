<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 05/dic/2025
  Time: 06:48 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Error del Servidor</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
        }

        .error-container {
            text-align: center;
            padding: 2rem;
            max-width: 700px;
        }

        .error-icon {
            font-size: 120px;
            margin-bottom: 2rem;
            animation: shake 0.5s infinite;
        }

        @keyframes shake {
            0%, 100% { transform: rotate(0deg); }
            25% { transform: rotate(-10deg); }
            75% { transform: rotate(10deg); }
        }

        h1 {
            font-size: 72px;
            margin-bottom: 1rem;
            font-weight: 700;
        }

        h2 {
            font-size: 32px;
            margin-bottom: 1rem;
            font-weight: 600;
        }

        p {
            font-size: 18px;
            margin-bottom: 2rem;
            opacity: 0.9;
            line-height: 1.6;
        }

        .error-details {
            background: rgba(255, 255, 255, 0.1);
            padding: 1.5rem;
            border-radius: 12px;
            margin-bottom: 2rem;
            backdrop-filter: blur(10px);
            text-align: left;
            max-height: 300px;
            overflow-y: auto;
        }

        .error-details p {
            margin-bottom: 0.5rem;
            font-size: 13px;
            font-family: monospace;
        }

        .error-details pre {
            background: rgba(0, 0, 0, 0.3);
            padding: 1rem;
            border-radius: 8px;
            overflow-x: auto;
            font-size: 12px;
            line-height: 1.4;
        }

        .btn-group {
            display: flex;
            gap: 1rem;
            justify-content: center;
            flex-wrap: wrap;
        }

        .btn {
            padding: 1rem 2rem;
            border-radius: 12px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
        }

        .btn-primary {
            background: white;
            color: #dc2626;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
        }

        .btn-outline {
            background: transparent;
            color: white;
            border: 2px solid white;
        }

        .btn-outline:hover {
            background: white;
            color: #dc2626;
        }
    </style>
</head>
<body>
<div class="error-container">
    <div class="error-icon">
        <i class="fas fa-server"></i>
    </div>

    <h1>500</h1>
    <h2>Error del Servidor</h2>
    <p>Ups! Algo salió mal en el servidor. Nuestro equipo técnico ha sido notificado.</p>

    <div class="error-details">
        <p><strong>Detalles técnicos:</strong></p>
        <p><strong>URL:</strong> <%= request.getRequestURI() %></p>
        <p><strong>Método:</strong> <%= request.getMethod() %></p>

        <% if (exception != null) { %>
        <p><strong>Excepción:</strong> <%= exception.getClass().getName() %></p>
        <p><strong>Mensaje:</strong> <%= exception.getMessage() %></p>

        <% if (request.getAttribute("javax.servlet.error.message") != null) { %>
        <pre><%= request.getAttribute("javax.servlet.error.message") %></pre>
        <% } %>
        <% } %>
    </div>

    <div class="btn-group">
        <a href="<%= request.getContextPath() %>/dashboard" class="btn btn-primary">
            <i class="fas fa-home"></i> Ir al Dashboard
        </a>
        <a href="javascript:history.back()" class="btn btn-outline">
            <i class="fas fa-arrow-left"></i> Volver Atrás
        </a>
    </div>
</div>
</body>
</html>
