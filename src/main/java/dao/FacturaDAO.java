package dao;

import models.*;
import utils.ConexionBdd;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    private Connection getConnection() throws SQLException {
        return ConexionBdd.getConnection();
    }

    // ==================================================================================
    // MÉTODOS CRUD BÁSICOS - FACTURA
    // ==================================================================================

    public List<Factura> obtenerTodos() throws SQLException {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT f.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.cedula as paciente_cedula, " +
                "p.telefono as paciente_telefono, " +
                "COALESCE((SELECT SUM(hp.monto) FROM historial_pagos hp WHERE hp.factura_id = f.factura_id), 0) as total_pagado " +
                "FROM factura f " +
                "INNER JOIN paciente p ON f.paciente_id = p.paciente_id " +
                "ORDER BY f.fecha_emision DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Factura f = mapearFactura(rs);
                lista.add(f);
            }
        }
        return lista;
    }

    public List<Factura> obtenerPorEstado(String estado) throws SQLException {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT f.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.cedula as paciente_cedula, " +
                "p.telefono as paciente_telefono, " +
                "COALESCE((SELECT SUM(hp.monto) FROM historial_pagos hp WHERE hp.factura_id = f.factura_id), 0) as total_pagado " +
                "FROM factura f " +
                "INNER JOIN paciente p ON f.paciente_id = p.paciente_id " +
                "WHERE f.estado = ? " +
                "ORDER BY f.fecha_emision DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Factura f = mapearFactura(rs);
                    lista.add(f);
                }
            }
        }
        return lista;
    }

    public Factura obtenerPorId(int facturaId) throws SQLException {
        Factura f = null;
        String sql = "SELECT f.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.cedula as paciente_cedula, " +
                "p.telefono as paciente_telefono, " +
                "COALESCE((SELECT SUM(hp.monto) FROM historial_pagos hp WHERE hp.factura_id = f.factura_id), 0) as total_pagado " +
                "FROM factura f " +
                "INNER JOIN paciente p ON f.paciente_id = p.paciente_id " +
                "WHERE f.factura_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facturaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    f = mapearFactura(rs);
                }
            }
        }
        return f;
    }

    public int insertar(Factura f) throws SQLException {
        String sql = "INSERT INTO factura (paciente_id, consulta_id, numero_factura, fecha_emision, " +
                "subtotal, descuento, iva, total, estado, metodo_pago, observaciones) " +
                "VALUES (?, ?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, f.getPaciente_id());

            if (f.getConsulta_id() != null && f.getConsulta_id() > 0) {
                ps.setInt(2, f.getConsulta_id());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setString(3, f.getNumero_factura());
            ps.setBigDecimal(4, f.getSubtotal());
            ps.setBigDecimal(5, f.getDescuento());
            ps.setBigDecimal(6, f.getIva());
            ps.setBigDecimal(7, f.getTotal());
            ps.setString(8, f.getEstado());
            ps.setString(9, f.getMetodo_pago());
            ps.setString(10, f.getObservaciones());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean actualizar(Factura f) throws SQLException {
        String sql = "UPDATE factura SET " +
                "subtotal = ?, descuento = ?, iva = ?, total = ?, " +
                "estado = ?, metodo_pago = ?, observaciones = ? " +
                "WHERE factura_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, f.getSubtotal());
            ps.setBigDecimal(2, f.getDescuento());
            ps.setBigDecimal(3, f.getIva());
            ps.setBigDecimal(4, f.getTotal());
            ps.setString(5, f.getEstado());
            ps.setString(6, f.getMetodo_pago());
            ps.setString(7, f.getObservaciones());
            ps.setInt(8, f.getFactura_id());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int facturaId) throws SQLException {
        // Primero eliminar detalles
        eliminarDetalles(facturaId);

        String sql = "DELETE FROM factura WHERE factura_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facturaId);
            return ps.executeUpdate() > 0;
        }
    }

    // ==================================================================================
    // MÉTODOS PARA DETALLES DE FACTURA
    // ==================================================================================

    public List<DetalleFactura> obtenerDetalles(int facturaId) throws SQLException {
        List<DetalleFactura> lista = new ArrayList<>();
        String sql = "SELECT df.*, t.nombre as tratamiento_nombre, t.categoria as tratamiento_categoria " +
                "FROM detalle_factura df " +
                "LEFT JOIN tratamiento_odontologico t ON df.tratamiento_id = t.tratamiento_id " +
                "WHERE df.factura_id = ? " +
                "ORDER BY df.detalle_id";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facturaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleFactura d = new DetalleFactura();
                    d.setDetalle_id(rs.getInt("detalle_id"));
                    d.setFactura_id(rs.getInt("factura_id"));

                    int tratamientoId = rs.getInt("tratamiento_id");
                    if (!rs.wasNull()) {
                        d.setTratamiento_id(tratamientoId);
                    }

                    d.setDescripcion(rs.getString("descripcion"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecio_unitario(rs.getBigDecimal("precio_unitario"));
                    d.setSubtotal(rs.getBigDecimal("subtotal"));
                    d.setTratamiento_nombre(rs.getString("tratamiento_nombre"));
                    d.setTratamiento_categoria(rs.getString("tratamiento_categoria"));
                    lista.add(d);
                }
            }
        }
        return lista;
    }

    public boolean insertarDetalle(DetalleFactura detalle) throws SQLException {
        String sql = "INSERT INTO detalle_factura (factura_id, tratamiento_id, descripcion, cantidad, precio_unitario, subtotal) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detalle.getFactura_id());

            if (detalle.getTratamiento_id() != null && detalle.getTratamiento_id() > 0) {
                ps.setInt(2, detalle.getTratamiento_id());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setString(3, detalle.getDescripcion());
            ps.setInt(4, detalle.getCantidad());
            ps.setBigDecimal(5, detalle.getPrecio_unitario());
            ps.setBigDecimal(6, detalle.getSubtotal());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarDetalles(int facturaId) throws SQLException {
        String sql = "DELETE FROM detalle_factura WHERE factura_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facturaId);
            ps.executeUpdate();
            return true;
        }
    }

    // ==================================================================================
    // MÉTODOS PARA HISTORIAL DE PAGOS
    // ==================================================================================

    public List<HistorialPago> obtenerPagos(int facturaId) throws SQLException {
        List<HistorialPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_pagos " +
                "WHERE factura_id = ? " +
                "ORDER BY fecha_pago DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facturaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HistorialPago p = new HistorialPago();
                    p.setPago_id(rs.getInt("pago_id"));
                    p.setFactura_id(rs.getInt("factura_id"));

                    Timestamp ts = rs.getTimestamp("fecha_pago");
                    if (ts != null) {
                        p.setFecha_pago(ts.toLocalDateTime());
                    }

                    p.setMonto(rs.getBigDecimal("monto"));
                    p.setMetodo_pago(rs.getString("metodo_pago"));
                    p.setReferencia(rs.getString("referencia"));
                    p.setRecibido_por(rs.getString("recibido_por"));
                    p.setObservaciones(rs.getString("observaciones"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    public boolean insertarPago(HistorialPago pago) throws SQLException {
        String sql = "INSERT INTO historial_pagos (factura_id, fecha_pago, monto, metodo_pago, referencia, recibido_por, observaciones) " +
                "VALUES (?, NOW(), ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pago.getFactura_id());
            ps.setBigDecimal(2, pago.getMonto());
            ps.setString(3, pago.getMetodo_pago());
            ps.setString(4, pago.getReferencia());
            ps.setString(5, pago.getRecibido_por());
            ps.setString(6, pago.getObservaciones());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstadoFacturaPorPagos(int facturaId) throws SQLException {
        String sql = "UPDATE factura f " +
                "SET f.estado = CASE " +
                "  WHEN COALESCE((SELECT SUM(hp.monto) FROM historial_pagos hp WHERE hp.factura_id = f.factura_id), 0) = 0 THEN 'Pendiente' " +
                "  WHEN COALESCE((SELECT SUM(hp.monto) FROM historial_pagos hp WHERE hp.factura_id = f.factura_id), 0) >= f.total THEN 'Pagada' " +
                "  ELSE 'Parcial' " +
                "END " +
                "WHERE f.factura_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facturaId);
            return ps.executeUpdate() > 0;
        }
    }

    // ==================================================================================
    // MÉTODOS AUXILIARES
    // ==================================================================================

    public String generarNumeroFactura() throws SQLException {
        String sql = "SELECT CONCAT('FAC-', DATE_FORMAT(NOW(), '%Y%m%d'), '-', " +
                "LPAD(COALESCE(MAX(CAST(SUBSTRING(numero_factura, 14) AS UNSIGNED)), 0) + 1, 4, '0')) as numero " +
                "FROM factura " +
                "WHERE numero_factura LIKE CONCAT('FAC-', DATE_FORMAT(NOW(), '%Y%m%d'), '%')";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String numero = rs.getString("numero");
                if (numero != null && !numero.isEmpty()) {
                    return numero;
                }
            }
        }

        // Si no hay facturas del día, generar el primero
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fecha = LocalDateTime.now().format(formatter);
        return "FAC-" + fecha + "-0001";
    }

    public List<Paciente> obtenerPacientes() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM paciente WHERE activo = 1 ORDER BY apellidos, nombres";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente p = new Paciente();
                p.setPacienteId(rs.getInt("paciente_id"));
                p.setNombres(rs.getString("nombres"));
                p.setApellidos(rs.getString("apellidos"));
                p.setCedula(rs.getString("cedula"));
                p.setTelefono(rs.getString("telefono"));
                lista.add(p);
            }
        }
        return lista;
    }

    public List<TratamientoOdontologico> obtenerTratamientos() throws SQLException {
        List<TratamientoOdontologico> lista = new ArrayList<>();
        String sql = "SELECT * FROM tratamiento_odontologico WHERE activo = 1 ORDER BY nombre";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TratamientoOdontologico t = new TratamientoOdontologico();
                t.setTratamiento_id(rs.getInt("tratamiento_id"));
                t.setCodigo(rs.getString("codigo"));
                t.setNombre(rs.getString("nombre"));
                t.setCategoria(rs.getString("categoria"));
                t.setPrecio_base(rs.getBigDecimal("precio_base"));
                lista.add(t);
            }
        }
        return lista;
    }

    // ==================================================================================
    // MÉTODOS DE MAPEO
    // ==================================================================================

    private Factura mapearFactura(ResultSet rs) throws SQLException {
        Factura f = new Factura();
        f.setFactura_id(rs.getInt("factura_id"));
        f.setPaciente_id(rs.getInt("paciente_id"));

        int consultaId = rs.getInt("consulta_id");
        if (!rs.wasNull()) {
            f.setConsulta_id(consultaId);
        }

        Timestamp ts = rs.getTimestamp("fecha_emision");
        if (ts != null) {
            f.setFecha_emision(ts.toLocalDateTime());
        }

        f.setNumero_factura(rs.getString("numero_factura"));
        f.setSubtotal(rs.getBigDecimal("subtotal"));
        f.setDescuento(rs.getBigDecimal("descuento"));
        f.setIva(rs.getBigDecimal("iva"));
        f.setTotal(rs.getBigDecimal("total"));
        f.setEstado(rs.getString("estado"));
        f.setMetodo_pago(rs.getString("metodo_pago"));
        f.setObservaciones(rs.getString("observaciones"));

        f.setPaciente_nombre(rs.getString("paciente_nombre"));
        f.setPaciente_cedula(rs.getString("paciente_cedula"));
        f.setPaciente_telefono(rs.getString("paciente_telefono"));
        f.setTotal_pagado(rs.getBigDecimal("total_pagado"));

        return f;
    }
}