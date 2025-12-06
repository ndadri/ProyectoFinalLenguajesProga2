<%--
  Created by IntelliJ IDEA.
  User: adria
  Date: 06/dic/2025
  Time: 11:41 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Formulario Tratamiento</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow" style="max-width: 800px; margin: auto;">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0">${tratamiento != null ? 'Editar' : 'Nuevo'} Tratamiento</h4>
        </div>
        <div class="card-body">
            <form action="tratamiento" method="post">
                <input type="hidden" name="id" value="${tratamiento.id}">

                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Código</label>
                        <input type="text" name="codigo" class="form-control"
                               value="${tratamiento.codigo}" required placeholder="Ej: LIM001">
                    </div>
                    <div class="col-md-8 mb-3">
                        <label class="form-label">Nombre</label>
                        <input type="text" name="nombre" class="form-control"
                               value="${tratamiento.nombre}" required>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Categoría</label>
                        <select name="categoria" class="form-select" required>
                            <option value="">Seleccione...</option>
                            <option value="Preventivo" ${tratamiento.categoria == 'Preventivo' ? 'selected' : ''}>Preventivo</option>
                            <option value="Restaurador" ${tratamiento.categoria == 'Restaurador' ? 'selected' : ''}>Restaurador</option>
                            <option value="Endodoncia" ${tratamiento.categoria == 'Endodoncia' ? 'selected' : ''}>Endodoncia</option>
                            <option value="Cirugia" ${tratamiento.categoria == 'Cirugia' ? 'selected' : ''}>Cirugía</option>
                            <option value="Protesis" ${tratamiento.categoria == 'Protesis' ? 'selected' : ''}>Prótesis</option>
                            <option value="Estetico" ${tratamiento.categoria == 'Estetico' ? 'selected' : ''}>Estético</option>
                            <option value="Ortodoncia" ${tratamiento.categoria == 'Ortodoncia' ? 'selected' : ''}>Ortodoncia</option>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label">Duración (min)</label>
                        <input type="number" name="duracion" class="form-control"
                               value="${tratamiento.duracionMin}" required>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label class="form-label">Costo ($)</label>
                        <input type="number" step="0.01" name="costo" class="form-control"
                               value="${tratamiento.costo}" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label">Descripción</label>
                    <textarea name="descripcion" class="form-control" rows="2">${tratamiento.descripcion}</textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">Estado</label>
                    <select name="estado" class="form-select">
                        <option value="1" ${tratamiento == null || tratamiento.estado == 1 ? 'selected' : ''}>Activo</option>
                        <option value="0" ${tratamiento.estado == 0 ? 'selected' : ''}>Inactivo</option>
                    </select>
                </div>

                <div class="text-end">
                    <a href="tratamiento?action=listar" class="btn btn-secondary me-2">Cancelar</a>
                    <button type="submit" class="btn btn-success">Guardar</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>