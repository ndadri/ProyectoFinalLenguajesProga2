# Sistema de Gestión Odontológica Integral

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue.svg)](https://jakarta.ee/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

Sistema web completo para la gestión integral de clínicas odontológicas, desarrollado con Jakarta EE, MySQL y arquitectura MVC.

---

## Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías-utilizadas)
- [Arquitectura](#-arquitectura-del-sistema)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Base de Datos](#-base-de-datos)
- [Ejecución](#-ejecución)
- [Usuarios de Prueba](#-usuarios-de-prueba)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Módulos del Sistema](#-módulos-del-sistema)
- [API y Endpoints](#-api-y-endpoints)
- [Seguridad](#-seguridad)
- [Troubleshooting](#-troubleshooting)
- [Contribución](#-contribución)
- [Licencia](#-licencia)
- [Contacto](#-contacto)

---

## Características

### Funcionalidades Principales

**Gestión de Pacientes**
- Registro completo con datos demográficos y médicos
- Historial de alergias, enfermedades sistémicas y medicamentos
- Búsqueda avanzada por nombre, apellido o cédula
- Cálculo automático de edad
- Eliminación lógica para preservar historial

**Sistema de Citas**
- Agendamiento con asignación de odontólogo
- Estados: Programada, Confirmada, En Curso, Completada, Cancelada
- Filtrado por estado, fecha y odontólogo
- Control de acceso por rol (odontólogo solo ve sus citas)
- Vista de agenda diaria

**Consultas Médicas**
- Registro completo de historiales clínicos
- Motivo, síntomas, diagnóstico y tratamiento
- Dientes tratados y procedimientos realizados
- Indicador de seguimiento requerido
- Historial cronológico por paciente

**Odontogramas Digitales**
- Representación visual de 32 dientes (numeración FDI)
- 8 estados dentales diferentes con código de colores
- Actualización interactiva con AJAX
- Trazabilidad histórica por paciente
- Observaciones específicas por diente

**Facturación y Pagos**
- Generación automática de números de factura
- Cálculo automático de IVA (15%)
- Gestión de detalles vinculados a catálogo de tratamientos
- Pagos parciales con historial completo
- Actualización automática de estados (Pendiente/Parcial/Pagada)

**Gestión de Odontólogos**
- Registro con especialidades
- Creación automática de usuario vinculado
- Validación de cédula y número de registro único

**Catálogo de Tratamientos**
- Códigos, nombres y descripciones
- Precios base y duración aproximada
- Categorización por tipo de tratamiento
- Indicador de requerimiento de anestesia

**Dashboard Informativo**
- Estadísticas en tiempo real
- Total de pacientes, citas del día, citas pendientes
- Listado de citas recientes
- Personalización según rol de usuario

**Seguridad**
- Autenticación con BCrypt (hash de contraseñas)
- Control de sesiones con Jakarta EE
- Filtro de autorización (AuthFilter)
- Control de acceso por roles (Admin, Recepción, Odontólogo)

---

## Tecnologías Utilizadas

### Backend
- **Java SE 17** (LTS)
- **Jakarta EE 10** (Servlets, JSP, JSTL, Filters)
- **Apache Tomcat 10.1** (Servidor de aplicaciones)
- **MySQL 8.0** (Base de datos relacional)
- **Maven 3.9** (Gestión de dependencias)
- **jBCrypt 0.4** (Hash de contraseñas)

### Frontend
- **HTML5 / CSS3**
- **Bootstrap 5.3** (Framework CSS)
- **JavaScript / jQuery 3.7**
- **Font Awesome 6.0** (Iconos)

### Arquitectura y Patrones
- **Patrón MVC** (Modelo-Vista-Controlador)
- **Patrón DAO** (Data Access Object)
- **Arquitectura en 3 Capas** (Presentación, Lógica, Datos)

---

## Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                      CAPA DE PRESENTACIÓN                   │
│                    (JSP + Bootstrap + JS)                   │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE CONTROL                         │
│              (Servlets + Filters + Session)                 │
│                                                              │
│  AuthFilter → CitaServlet → ConsultaServlet → ...          │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                   CAPA DE DATOS (DAO)                       │
│         (CitaDAO, PacienteDAO, FacturaDAO, ...)            │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    BASE DE DATOS MySQL                      │
│               (15 tablas normalizadas 3NF)                  │
└─────────────────────────────────────────────────────────────┘
```

### Flujo de Peticiones

```
1. Cliente (Browser) → HTTP Request
2. AuthFilter valida sesión
3. Servlet procesa petición
4. Servlet invoca DAO
5. DAO ejecuta query SQL
6. DAO retorna datos
7. Servlet prepara modelo
8. JSP renderiza vista
9. HTTP Response → Cliente
```

---

## Requisitos Previos

### Software Necesario

| Software | Versión Mínima | Versión Recomendada | Descarga |
|----------|----------------|---------------------|----------|
| Java JDK | 17 | 17 LTS | [Oracle](https://www.oracle.com/java/technologies/downloads/) |
| Apache Tomcat | 10.0 | 10.1.x | [Apache](https://tomcat.apache.org/download-10.cgi) |
| MySQL Server | 8.0 | 8.0.x | [MySQL](https://dev.mysql.com/downloads/mysql/) |
| Maven | 3.8 | 3.9.x | [Apache Maven](https://maven.apache.org/download.cgi) |
| Git | 2.30 | Última | [Git](https://git-scm.com/downloads) |

### Herramientas Opcionales (Recomendadas)

- **IDE:** IntelliJ IDEA, Eclipse, NetBeans
- **Cliente MySQL:** MySQL Workbench, DBeaver, phpMyAdmin
- **Navegador:** Google Chrome (recomendado), Firefox

### Requisitos de Hardware

**Mínimos:**
- CPU: Intel Core i3 o equivalente
- RAM: 4 GB
- Disco: 2 GB libres

**Recomendados:**
- CPU: Intel Core i5 o superior
- RAM: 8 GB o más
- Disco: 10 GB libres (para datos de desarrollo)

---

## Instalación

### Opción 1: Clonar desde Git (Recomendado)

```bash
# Clonar el repositorio
git clone https://github.com/ndadri/ProyectoFinalLenguajesProga2

```

### Opción 2: Descargar ZIP

1. Descargue el archivo ZIP del proyecto
2. Extraiga en la ubicación deseada
3. Abra terminal en el directorio extraído

---

## Configuración

### 1. Configurar Base de Datos

#### Paso 1: Crear la Base de Datos

```bash
# Iniciar sesión en MySQL
mysql -u root -p

# Crear base de datos
CREATE DATABASE clinica_dental CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Salir
exit;
```

#### Paso 2: Ejecutar Script de Esquema

```bash
# Importar esquema
mysql -u root -p clinica_dental < database/schema.sql

# Importar datos de prueba (opcional)
mysql -u root -p clinica_dental < database/data_seed.sql
```

**O desde MySQL Workbench:**
1. Abrir MySQL Workbench
2. Conectarse al servidor
3. File → Run SQL Script
4. Seleccionar `database/schema.sql`
5. Ejecutar

### 2. Configurar Credenciales de Base de Datos

Edite el archivo: `src/main/java/utils/ConexionBdd.java`

```java
public class ConexionBdd {
    // MODIFICAR ESTAS LÍNEAS CON SUS CREDENCIALES
    private static final String URL = "jdbc:mysql://localhost:3306/clinica_dental";
    private static final String USER = "root";           // usuario MySQL
    private static final String PASSWORD = "su_password"; // contraseña MySQL
    
}
```

**Configuración por Ambiente (Opcional):**

Para desarrollo vs producción, puede usar variables de entorno:

```java
private static final String URL = System.getenv("DB_URL") != null 
    ? System.getenv("DB_URL") 
    : "jdbc:mysql://localhost:3306/clinica_dental";
```

### 3. Configurar Tomcat

#### En IntelliJ IDEA:

1. Run → Edit Configurations
2. Add New Configuration → Tomcat Server → Local
3. Configure:
   - Name: `Tomcat 10`
   - Application Server: Seleccionar Tomcat 10.1
   - Deployment: Add → Artifact → `clinica-dental:war exploded`
   - Application context: `/clinica-dental`
4. Apply → OK

#### En Eclipse:

1. Window → Preferences → Server → Runtime Environments
2. Add → Apache Tomcat v10.1
3. Seleccionar directorio de instalación de Tomcat
4. Apply

### 4. Compilar el Proyecto

```bash
# Limpiar y compilar
mvn clean install

# Debería ver:
# [INFO] BUILD SUCCESS
# [INFO] Total time: XX s
```

---

## Base de Datos

### Esquema de Base de Datos

El sistema utiliza **15 tablas** normalizadas en tercera forma normal (3NF):

#### Tablas Principales

| Tabla | Descripción | Registros |
|-------|-------------|-----------|
| `paciente` | Información de pacientes | Primaria |
| `odontologo` | Datos de odontólogos | Primaria |
| `especialidad` | Especialidades odontológicas | Catálogo |
| `usuario` | Usuarios del sistema | Autenticación |
| `tipo_usuario` | Tipos de usuarios (roles) | Catálogo |
| `cita` | Citas médicas | Transaccional |
| `consulta` | Consultas médicas | Transaccional |
| `odontograma` | Odontogramas de pacientes | Médica |
| `diente` | Estados de dientes individuales | Médica |
| `factura` | Facturas emitidas | Financiera |
| `detalle_factura` | Líneas de factura | Financiera |
| `historial_pagos` | Pagos realizados | Financiera |
| `tratamiento_odontologico` | Catálogo de tratamientos | Catálogo |

### Diagrama Entidad-Relación

```
paciente (1) ────< (N) cita (N) >──── (1) odontologo
    │                   │                      │
    │                   │                      │
    │               (0-1) consulta             │
    │                                          │
    └─────< (N) factura              especialidad
    │            │
    │            └────< (N) detalle_factura
    │            │
    │            └────< (N) historial_pagos
    │
    └─────< (N) odontograma (1) ────< (32) diente
```

### Scripts SQL Disponibles

```
database/
├── schema.sql           # Esquema completo (CREATE TABLE)
├── data_seed.sql       # Datos de prueba (INSERT)
├── triggers.sql        # Triggers para auditoría (opcional)
└── backup.sql          # Backup de ejemplo
```

---

## Ejecución

### Opción 1: Desde IDE (IntelliJ IDEA)

1. Abrir proyecto en IntelliJ IDEA
2. Esperar a que Maven descargue dependencias
3. Configurar Tomcat (ver sección Configuración)
4. Click en el botón **Run** (o Shift+F10)
5. Esperar a que Tomcat inicie

**Consola debería mostrar:**
```
[INFO] Tomcat started on port(s): 8080
[INFO] Deployment of web application directory [...] has finished
```

6. Abrir navegador en: `http://localhost:8080/clinica-dental`

### Opción 2: Desde Terminal con Maven

```bash
# Limpiar y empaquetar
mvn clean package

# Debería generar: target/clinica-dental.war
```

Luego copiar el WAR a Tomcat:

```bash
# Linux/Mac
cp target/clinica-dental.war $TOMCAT_HOME/webapps/

# Windows
copy target\clinica-dental.war %TOMCAT_HOME%\webapps\
```

Iniciar Tomcat:

```bash
# Linux/Mac
$TOMCAT_HOME/bin/catalina.sh run

# Windows
%TOMCAT_HOME%\bin\catalina.bat run
```

### Opción 3: Deploy Manual en Tomcat

1. Compilar: `mvn clean package`
2. Copiar `target/clinica-dental.war` a `TOMCAT_HOME/webapps/`
3. Iniciar Tomcat
4. Tomcat auto-desplegará el WAR
5. Acceder a: `http://localhost:8080/clinica-dental`

### Verificar Instalación

**Página de Login Correcta:**
```
┌──────────────────────────────────────┐
│   SISTEMA DE GESTIÓN ODONTOLÓGICA    │
├──────────────────────────────────────┤
│                                      │
│   Usuario:  [________________]       │
│   Contraseña: [________________]     │
│                                      │
│   [  INICIAR SESIÓN  ]              │
└──────────────────────────────────────┘
```

---

## Usuarios de Prueba

Una vez ejecutado el script `data_seed.sql`, tendrá estos usuarios:

### Administrador

```
Usuario:    admin
Contraseña: admin123
Rol:        Administrador
Permisos:   Acceso total al sistema
```

### Recepción

```
Usuario:    recepcion
Contraseña: recepcion123
Rol:        Recepción
Permisos:   Gestión de pacientes
            Gestión de citas (todas)
            Facturación completa
            No gestiona usuarios
```

### Odontólogo

```
Usuario:    dr.perez
Contraseña: odontologo123
Rol:        Odontólogo
Permisos:   Solo sus citas
            Registrar consultas
            Gestionar odontogramas
            No accede a facturación
```

### Cambiar Contraseñas

**IMPORTANTE:** Se recomienda cambiar las contraseñas de prueba en producción.

Para cambiar contraseñas manualmente:

```sql
-- Generar hash BCrypt con factor 10
-- Usar herramienta online: https://bcrypt-generator.com/

UPDATE usuario 
SET password = '$2a$10$NUEVO_HASH_AQUI' 
WHERE usuario = 'admin';
```

---

## Estructura del Proyecto

```
sistema-clinica-dental/
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── controllers/          # Servlets (Capa de Control)
│       │   │   ├── CitaServlet.java
│       │   │   ├── ConsultaServlet.java
│       │   │   ├── DashboardServlet.java
│       │   │   ├── FacturaServlet.java
│       │   │   ├── LoginServlet.java
│       │   │   ├── OdontogramaServlet.java
│       │   │   ├── OdontologoServlet.java
│       │   │   ├── PacienteServlet.java
│       │   │   └── TratamientoServlet.java
│       │   │
│       │   ├── dao/                   # Data Access Objects (Capa de Datos)
│       │   │   ├── CitaDAO.java
│       │   │   ├── ConsultaDAO.java
│       │   │   ├── FacturaDAO.java
│       │   │   ├── OdontogramaDAO.java
│       │   │   ├── OdontologoDAO.java
│       │   │   ├── PacienteDAO.java
│       │   │   ├── TratamientoDAO.java
│       │   │   └── UsuarioDAO.java
│       │   │
│       │   ├── models/                # Modelos de Dominio (POJOs)
│       │   │   ├── Cita.java
│       │   │   ├── Consulta.java
│       │   │   ├── DetalleFactura.java
│       │   │   ├── Diente.java
│       │   │   ├── Especialidad.java
│       │   │   ├── Factura.java
│       │   │   ├── HistorialPago.java
│       │   │   ├── Odontograma.java
│       │   │   ├── Odontologo.java
│       │   │   ├── Paciente.java
│       │   │   ├── TratamientoOdontologico.java
│       │   │   └── Usuario.java
│       │   │
│       │   ├── filters/               # Filtros de Seguridad
│       │   │   └── AuthFilter.java
│       │   │
│       │   └── utils/                 # Utilidades
│       │       ├── ConexionBdd.java   # Pool de conexiones
│       │       └── PasswordUtil.java  # BCrypt utilities
│       │
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml            # Descriptor de despliegue
│           │   └── lib/               # JARs de dependencias
│           │
│           ├── views/                 # Vistas JSP
│           │   ├── cita.jsp
│           │   ├── cita-form.jsp
│           │   ├── consulta.jsp
│           │   ├── consulta-form.jsp
│           │   ├── consulta-detalle.jsp
│           │   ├── dashboard.jsp
│           │   ├── facturacion.jsp
│           │   ├── factura-form.jsp
│           │   ├── factura-detalle.jsp
│           │   ├── factura-pago.jsp
│           │   ├── login.jsp
│           │   ├── odontograma.jsp
│           │   ├── odontograma-form.jsp
│           │   ├── odontograma-detalle.jsp
│           │   ├── odontologo.jsp
│           │   ├── odontologo-form.jsp
│           │   ├── paciente.jsp
│           │   ├── paciente-form.jsp
│           │   ├── tratamiento.jsp
│           │   └── tratamiento-form.jsp
│           │
│           ├── estilos/               # CSS
│           │   └── style.css
│           │
│           └── js/                    # JavaScript
│               ├── main.js
│               └── odontograma.js
│
├── database/                          # Scripts SQL
│   ├── schema.sql
│   ├── data_seed.sql
│   └── backup.sql
│
├── docs/                              # Documentación
│   ├── manual-usuario.md
│   ├── manual-tecnico.md
│   └── diagramas/
│
├── pom.xml                            # Maven configuration
├── README.md                          # Este archivo
└── .gitignore                         # Git ignore rules
```

---

## Módulos del Sistema

### 1. Módulo de Autenticación

**Componentes:**
- `LoginServlet.java` - Maneja login y logout
- `AuthFilter.java` - Valida sesiones en cada petición
- `UsuarioDAO.java` - Validación de credenciales con BCrypt

**Flujo:**
1. Usuario ingresa credenciales
2. Servlet valida con DAO
3. DAO verifica hash BCrypt
4. Si es válido, crea sesión
5. Usuario redirigido a dashboard

### 2. Módulo de Pacientes

**Componentes:**
- `PacienteServlet.java` - CRUD de pacientes
- `PacienteDAO.java` - Operaciones de BD
- `Paciente.java` - Modelo de dominio

**Funcionalidades:**
- Registro con 25+ campos
- Validación de cédula única
- Cálculo automático de edad
- Búsqueda por múltiples criterios
- Eliminación lógica

### 3. Módulo de Citas

**Componentes:**
- `CitaServlet.java`
- `CitaDAO.java`
- `Cita.java`

**Características:**
- 6 estados de cita
- Asignación de odontólogo
- Filtrado avanzado
- Control de acceso por rol

### 4. Módulo de Consultas

**Componentes:**
- `ConsultaServlet.java`
- `ConsultaDAO.java`
- `Consulta.java`

**Funcionalidades:**
- Registro completo de historial clínico
- Vinculación opcional con citas
- Historial cronológico por paciente

### 5. Módulo de Odontogramas

**Componentes:**
- `OdontogramaServlet.java`
- `OdontogramaDAO.java`
- `Odontograma.java`, `Diente.java`
- `odontograma.js` - Interactividad AJAX

**Características:**
- 32 dientes (numeración FDI)
- 8 estados con código de colores
- Actualización sin recarga de página
- Trazabilidad histórica

### 6. Módulo de Facturación

**Componentes:**
- `FacturaServlet.java`
- `FacturaDAO.java`
- `Factura.java`, `DetalleFactura.java`, `HistorialPago.java`

**Funcionalidades:**
- Generación automática de números
- Cálculo de IVA al 15%
- Pagos parciales
- Estados automáticos

### 7. Módulo de Tratamientos

**Componentes:**
- `TratamientoServlet.java`
- `TratamientoDAO.java`
- `TratamientoOdontologico.java`

**Funcionalidades:**
- Catálogo de servicios
- Precios base
- Categorización

---

## API y Endpoints

### Endpoints Principales

#### Autenticación

```
GET  /login                    # Mostrar formulario de login
POST /login                    # Procesar login
GET  /logout                   # Cerrar sesión
```

#### Pacientes

```
GET  /paciente?action=listar                # Listar todos
GET  /paciente?action=nuevo                 # Formulario nuevo
POST /paciente?action=guardar              # Guardar nuevo
GET  /paciente?action=editar&id=1          # Formulario editar
POST /paciente?action=actualizar           # Actualizar
GET  /paciente?action=eliminar&id=1        # Eliminar (lógico)
GET  /paciente?action=buscar&termino=juan  # Buscar
```

#### Citas

```
GET  /cita?action=listar                    # Listar todas
GET  /cita?action=hoy                       # Citas de hoy
GET  /cita?action=nuevo                     # Formulario nuevo
POST /cita?action=guardar                   # Guardar nuevo
GET  /cita?action=editar&id=1              # Formulario editar
POST /cita?action=actualizar                # Actualizar
GET  /cita?action=confirmar&id=1           # Confirmar cita
GET  /cita?action=cancelar&id=1            # Cancelar cita
GET  /cita?action=completar&id=1           # Completar cita
GET  /cita?action=eliminar&id=1            # Eliminar
```

#### Consultas

```
GET  /consulta?action=listar                      # Listar todas
GET  /consulta?action=historial&paciente_id=1    # Historial paciente
GET  /consulta?action=nuevo                       # Formulario nuevo
POST /consulta?action=guardar                     # Guardar nuevo
GET  /consulta?action=ver&id=1                    # Ver detalle
GET  /consulta?action=editar&id=1                 # Formulario editar
POST /consulta?action=actualizar                  # Actualizar
GET  /consulta?action=eliminar&id=1               # Eliminar
```

#### Odontogramas

```
GET  /odontograma?action=listar                # Listar todos
GET  /odontograma?action=nuevo                 # Formulario nuevo
POST /odontograma?action=crear                 # Crear nuevo
GET  /odontograma?action=ver&id=1              # Ver detalle
POST /odontograma?action=actualizar            # Actualizar observaciones
POST /odontograma?action=guardar_diente        # Actualizar diente (AJAX)
GET  /odontograma?action=eliminar&id=1         # Eliminar
```

#### Facturación

```
GET  /factura?action=listar                     # Listar todas
GET  /factura?action=listar&estado=Pendiente   # Filtrar por estado
GET  /factura?action=nuevo                      # Formulario nuevo
POST /factura?action=guardar                    # Guardar nuevo
GET  /factura?action=ver&id=1                   # Ver detalle
GET  /factura?action=editar&id=1                # Formulario editar
POST /factura?action=actualizar                 # Actualizar
GET  /factura?action=pagar&id=1                 # Formulario pago
POST /factura?action=registrar_pago             # Registrar pago
GET  /factura?action=eliminar&id=1              # Eliminar
GET  /factura?action=cancelar&id=1              # Cancelar
```

---

## Seguridad

### Medidas Implementadas

#### 1. Autenticación Robusta

**BCrypt para Contraseñas:**
```java
// Hash al crear usuario (factor 10)
String hashed = PasswordUtil.hashPassword("password123");
// Almacenado: $2a$10$...hash_completo...

// Verificación al login
boolean valid = PasswordUtil.checkPassword("password123", hashedFromDB);
```

**Características:**
- Salt automático único por contraseña
- Costo computacional ajustable (factor 10)
- Resistente a rainbow tables
- Tiempo de verificación ~100ms

#### 2. Control de Sesiones

**AuthFilter.java** - Intercepta TODAS las peticiones:

```java
// Valida sesión en cada request
if (session != null && session.getAttribute("usuario") != null) {
    chain.doFilter(request, response); // Permitir
} else {
    response.sendRedirect(loginURI);   // Redirigir a login
}
```

**Configuración de Sesión:**
- Timeout: 1 hora de inactividad
- Cookies HTTP-only
- Invalidación al logout

#### 3. Prevención de SQL Injection

**PreparedStatements en TODOS los DAOs:**

```java
// INSEGURO (concatenación)
String sql = "SELECT * FROM paciente WHERE cedula = '" + cedula + "'";

// SEGURO (parámetros vinculados)
String sql = "SELECT * FROM paciente WHERE cedula = ?";
ps.setString(1, cedula);
```

#### 4. Control de Acceso por Roles

**En cada Servlet:**

```java
Usuario usuario = (Usuario) session.getAttribute("usuario");

if (usuario.isAdmin()) {
    // Acceso completo
} else if (usuario.isOdontologo()) {
    // Solo sus citas y pacientes
} else if (usuario.isRecepcion()) {
    // Gestión administrativa
}
```
