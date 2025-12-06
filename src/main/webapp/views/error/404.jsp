<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 05/dic/2025
  Time: 06:47 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Página no encontrada</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
        }

        .error-container {
            text-align: center;
            padding: 2rem;
            max-width: 600px;
        }

        .error-icon {
            font-size: 120px;
            margin-bottom: 2rem;
            animation: bounce 2s infinite;
        }

        @keyframes bounce {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-20px); }
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
        }

        .error-details p {
            margin-bottom: 0.5rem;
            font-size: 14px;
            font-family: monospace;
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
            color: #667eea;
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
            color: #667eea;
        }
    </style>
</head>
<body>
<div class="error-container">
    <div class="error-icon">
        <i class="fas fa-exclamation-triangle"></i>
    </div>

    <h1>404</h1>
    <h2>Página no encontrada</h2>
    <p>Lo sentimos, la página que buscas no existe o ha sido movida.</p>

    <div class="error-details">
        <p><strong>URL solicitada:</strong> <%= request.getRequestURI() %></p>
        <p><strong>Método:</strong> <%= request.getMethod() %></p>
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
