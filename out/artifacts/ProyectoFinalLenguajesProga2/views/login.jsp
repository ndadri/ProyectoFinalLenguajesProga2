<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 05/dic/2025
  Time: 05:57 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Vista: login.jsp
Propósito: Formulario de inicio de sesión para el sistema DentalCare.
Variables/atributos esperados en request/session:
- error (opcional): Mensaje de error para mostrar si las credenciales son inválidas.
Secciones principales:
1) Estilos embebidos y assets (Font Awesome).
2) Panel izquierdo informativo (branding y descripción).
3) Panel derecho con el formulario de login (usuario, password).
4) Bloque <c:if> que muestra mensajes de error si existen.
    Notas de implementación:
    - El formulario realiza POST a la ruta 'login'.
    - No se hacen cambios funcionales, sólo documentación para facilitar mantenimiento.
    -->
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - DentalCare</title>
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
            padding: 2rem;
        }

        .login-container {
            background: white;
            border-radius: 24px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
            display: flex;
            max-width: 900px;
            width: 100%;
        }

        .login-left {
            flex: 1;
            padding: 3rem;
            background: linear-gradient(135deg, #3B82F6, #A855F7);
            color: white;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .login-logo {
            display: flex;
            align-items: center;
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .logo-icon {
            width: 60px;
            height: 60px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
        }

        .login-left h1 {
            font-size: 32px;
            margin-bottom: 1rem;
        }

        .login-left p {
            opacity: 0.9;
            line-height: 1.6;
        }

        .login-right {
            flex: 1;
            padding: 3rem;
        }

        .login-form-title {
            font-size: 28px;
            font-weight: 700;
            color: #1F2937;
            margin-bottom: 0.5rem;
        }

        .login-form-subtitle {
            color: #6B7280;
            margin-bottom: 2rem;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            font-weight: 500;
            color: #374151;
            margin-bottom: 0.5rem;
        }

        .input-group {
            position: relative;
        }

        .input-group i {
            position: absolute;
            left: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: #9CA3AF;
        }

        .form-control {
            width: 100%;
            padding: 0.875rem 1rem 0.875rem 2.75rem;
            border: 2px solid #E5E7EB;
            border-radius: 12px;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            outline: none;
            border-color: #3B82F6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }

        .btn-login {
            width: 100%;
            padding: 0.875rem;
            background: linear-gradient(135deg, #3B82F6, #A855F7);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 1rem;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(59, 130, 246, 0.3);
        }

        .alert-error {
            background: #FEE2E2;
            color: #991B1B;
            padding: 1rem;
            border-radius: 12px;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .divider {
            text-align: center;
            margin: 1.5rem 0;
            color: #9CA3AF;
            font-size: 14px;
        }

        @media (max-width: 768px) {
            .login-container {
                flex-direction: column;
            }

            .login-left {
                padding: 2rem;
            }

            .login-right {
                padding: 2rem;
            }
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="login-left">
        <div class="login-logo">
            <div class="logo-icon">
                <i class="fas fa-tooth"></i>
            </div>
            <h1>DentalCare</h1>
        </div>
        <h2>Sistema de Gestión Odontológica</h2>
        <p>Administra pacientes, citas, consultas y tratamientos dentales de manera eficiente y profesional.</p>
    </div>

    <div class="login-right">
        <h2 class="login-form-title">Iniciar Sesión</h2>
        <p class="login-form-subtitle">Ingresa tus credenciales para acceder</p>
        <!--
        <c:if test="${not empty error}">
            <div class="alert-error">
                <i class="fas fa-exclamation-circle"></i>
                    ${error}
            </div>
        </c:if>
    -->

        <form action="login" method="post">
            <div class="form-group">
                <label for="usuario">Usuario</label>
                <div class="input-group">
                    <i class="fas fa-user"></i>
                    <input type="text"
                           id="usuario"
                           name="usuario"
                           class="form-control"
                           placeholder="Ingresa tu usuario"
                           required
                           autofocus>
                </div>
            </div>

            <div class="form-group">
                <label for="password">Contraseña</label>
                <div class="input-group">
                    <i class="fas fa-lock"></i>
                    <input type="password"
                           id="password"
                           name="password"
                           class="form-control"
                           placeholder="Ingresa tu contraseña"
                           required>
                </div>
            </div>

            <button type="submit" class="btn-login">
                <i class="fas fa-sign-in-alt"></i> Iniciar Sesión
            </button>
        </form>

        <div class="divider">
            <p style="margin-top: 1.5rem; font-size: 13px;">
                ¿Olvidaste tu contraseña?
                <a href="#" style="color: #3B82F6; text-decoration: none; font-weight: 600;">Recuperar</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>
