package dao;

import models.Factura;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    private Connection getConnection() throws SQLException {
        return ConexionBdd.getConnection();
    }

    // -------------------------------------------------------------------------------------
    // LISTAR TODAS LAS FACTURAS (JOIN con consulta + tratamiento)
    // -------------------------------------------------------------------------------------
    public List<Factura> obtenerTodas() {

        List<Factura> lista = new ArrayList<>();

        String sql =
                "SELECT f.*, c.tratamiento_id " +
                        "FROM factura f " +
                        "LEFT JOIN consulta c ON f.consulta_id = c.consulta_id " +
                        "ORDER BY f.fecha_emision DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Factura f = new Factura();
                f.setFactura_id(rs.getInt("factura_id"));
                f.setPaciente_id(rs.getInt("paciente_id"));
                f.setConsulta_id(rs.getInt("consulta_id"));

                Timestamp ts = rs.getTimestamp("fecha_emision");
                if (ts != null) f.setFecha_emision(ts.toLocalDateTime());

                f.setNumero_factura(rs.getString("numero_factura"));
                f.setSubtotal(rs.getBigDecimal("subtotal"));
                f.setDescuento(rs.getBigDecimal("descuento"));
                f.setIva(rs.getBigDecimal("iva"));
                f.setTotal(rs.getBigDecimal("total"));
                f.setEstado(rs.getString("estado"));
                f.setMetodo_pago(rs.getString("metodo_pago"));
                f.setObservaciones(rs.getString("observaciones"));

                lista.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // -------------------------------------------------------------------------------------
    // OBTENER FACTURA POR ID
    // -------------------------------------------------------------------------------------
    public Factura obtenerPorId(int facturaId) {

        Factura f = null;

        String sql =
                "SELECT f.*, c.tratamiento_id " +
                        "FROM factura f " +
                        "LEFT JOIN consulta c ON f.consulta_id = c.consulta_id " +
                        "WHERE f.factura_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facturaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    f = new Factura();

                    f.setFactura_id(rs.getInt("factura_id"));
                    f.setPaciente_id(rs.getInt("paciente_id"));
                    f.setConsulta_id(rs.getInt("consulta_id"));

                    Timestamp ts = rs.getTimestamp("fecha_emision");
                    if (ts != null) f.setFecha_emision(ts.toLocalDateTime());

                    f.setNumero_factura(rs.getString("numero_factura"));
                    f.setSubtotal(rs.getBigDecimal("subtotal"));
                    f.setDescuento(rs.getBigDecimal("descuento"));
                    f.setIva(rs.getBigDecimal("iva"));
                    f.setTotal(rs.getBigDecimal("total"));
                    f.setEstado(rs.getString("estado"));
                    f.setMetodo_pago(rs.getString("metodo_pago"));
                    f.setObservaciones(rs.getString("observaciones"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return f;
    }

    // -------------------------------------------------------------------------------------
    // INSERTAR FACTURA
    // -------------------------------------------------------------------------------------
    public boolean insertar(Factura f) {

        String sql =
                "INSERT INTO factura (paciente_id, consulta_id, fecha_emision, numero_factura, subtotal, descuento, iva, total, estado, metodo_pago, observaciones) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getPaciente_id());
            ps.setInt(2, f.getConsulta_id());

            ps.setTimestamp(3, Timestamp.valueOf(f.getFecha_emision()));

            ps.setString(4, f.getNumero_factura());

            ps.setBigDecimal(5, f.getSubtotal());
            ps.setBigDecimal(6, f.getDescuento());
            ps.setBigDecimal(7, f.getIva());
            ps.setBigDecimal(8, f.getTotal());

            ps.setString(9, f.getEstado());
            ps.setString(10, f.getMetodo_pago());
            ps.setString(11, f.getObservaciones());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------------------------------------
    // ACTUALIZAR
    // -------------------------------------------------------------------------------------
    public boolean actualizar(Factura f) {

        String sql =
                "UPDATE factura SET paciente_id=?, consulta_id=?, fecha_emision=?, numero_factura=?, subtotal=?, descuento=?, iva=?, total=?, estado=?, metodo_pago=?, observaciones=? " +
                        "WHERE factura_id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getPaciente_id());
            ps.setInt(2, f.getConsulta_id());
            ps.setTimestamp(3, Timestamp.valueOf(f.getFecha_emision()));
            ps.setString(4, f.getNumero_factura());
            ps.setBigDecimal(5, f.getSubtotal());
            ps.setBigDecimal(6, f.getDescuento());
            ps.setBigDecimal(7, f.getIva());
            ps.setBigDecimal(8, f.getTotal());
            ps.setString(9, f.getEstado());
            ps.setString(10, f.getMetodo_pago());
            ps.setString(11, f.getObservaciones());
            ps.setInt(12, f.getFactura_id());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------------------------------------
    // ELIMINAR
    // -------------------------------------------------------------------------------------
    public boolean eliminar(int facturaId) {

        String sql = "DELETE FROM factura WHERE factura_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, facturaId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
