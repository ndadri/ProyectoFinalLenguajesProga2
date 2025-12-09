package dao;

import models.Diente;
import models.Odontograma;
import models.Paciente;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OdontogramaDAO {

    private Connection getConnection() throws SQLException {
        return ConexionBdd.getConnection();
    }

    // ==================================================================================
    // MÉTODOS CRUD - ODONTOGRAMA
    // ==================================================================================

    public List<Odontograma> obtenerTodos() throws SQLException {
        List<Odontograma> lista = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, p.cedula as paciente_cedula " +
                "FROM odontograma o " +
                "INNER JOIN paciente p ON o.paciente_id = p.paciente_id " +
                "ORDER BY o.fecha_creacion DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Odontograma o = mapearOdontograma(rs);
                lista.add(o);
            }
        }
        return lista;
    }

    public List<Odontograma> obtenerPorPaciente(int pacienteId) throws SQLException {
        List<Odontograma> lista = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, p.cedula as paciente_cedula " +
                "FROM odontograma o " +
                "INNER JOIN paciente p ON o.paciente_id = p.paciente_id " +
                "WHERE o.paciente_id = ? " +
                "ORDER BY o.fecha_creacion DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacienteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Odontograma o = mapearOdontograma(rs);
                    lista.add(o);
                }
            }
        }
        return lista;
    }

    public Odontograma obtenerPorId(int odontogramaId) throws SQLException {
        Odontograma o = null;
        String sql = "SELECT o.*, CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, p.cedula as paciente_cedula " +
                "FROM odontograma o " +
                "INNER JOIN paciente p ON o.paciente_id = p.paciente_id " +
                "WHERE o.odontograma_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, odontogramaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    o = mapearOdontograma(rs);
                }
            }
        }
        return o;
    }

    public int insertar(Odontograma odontograma) throws SQLException {
        String sql = "INSERT INTO odontograma (paciente_id, fecha_creacion, observaciones) " +
                "VALUES (?, CURDATE(), ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, odontograma.getPacienteId());
            ps.setString(2, odontograma.getObservaciones());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean actualizar(Odontograma odontograma) throws SQLException {
        String sql = "UPDATE odontograma SET observaciones = ? WHERE odontograma_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, odontograma.getObservaciones());
            ps.setInt(2, odontograma.getOdontogramaId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int odontogramaId) throws SQLException {
        // Primero eliminar todos los dientes asociados
        eliminarDientes(odontogramaId);

        String sql = "DELETE FROM odontograma WHERE odontograma_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, odontogramaId);
            return ps.executeUpdate() > 0;
        }
    }

    // ==================================================================================
    // MÉTODOS CRUD - DIENTES
    // ==================================================================================

    public List<Diente> obtenerDientes(int odontogramaId) throws SQLException {
        List<Diente> lista = new ArrayList<>();
        String sql = "SELECT * FROM diente WHERE odontograma_id = ? ORDER BY numero_diente";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, odontogramaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Diente d = new Diente();
                    d.setDienteId(rs.getInt("diente_id"));
                    d.setOdontogramaId(rs.getInt("odontograma_id"));
                    d.setNumeroDiente(rs.getString("numero_diente"));
                    d.setEstado(rs.getString("estado"));
                    d.setObservaciones(rs.getString("observaciones"));
                    lista.add(d);
                }
            }
        }
        return lista;
    }

    public Diente obtenerDiente(int odontogramaId, String numeroDiente) throws SQLException {
        Diente d = null;
        String sql = "SELECT * FROM diente WHERE odontograma_id = ? AND numero_diente = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, odontogramaId);
            ps.setString(2, numeroDiente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = new Diente();
                    d.setDienteId(rs.getInt("diente_id"));
                    d.setOdontogramaId(rs.getInt("odontograma_id"));
                    d.setNumeroDiente(rs.getString("numero_diente"));
                    d.setEstado(rs.getString("estado"));
                    d.setObservaciones(rs.getString("observaciones"));
                }
            }
        }
        return d;
    }

    public boolean insertarDiente(Diente diente) throws SQLException {
        String sql = "INSERT INTO diente (odontograma_id, numero_diente, estado, observaciones) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE estado = VALUES(estado), observaciones = VALUES(observaciones)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, diente.getOdontogramaId());
            ps.setString(2, diente.getNumeroDiente());
            ps.setString(3, diente.getEstado());
            ps.setString(4, diente.getObservaciones());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarDiente(Diente diente) throws SQLException {
        String sql = "UPDATE diente SET estado = ?, observaciones = ? " +
                "WHERE odontograma_id = ? AND numero_diente = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, diente.getEstado());
            ps.setString(2, diente.getObservaciones());
            ps.setInt(3, diente.getOdontogramaId());
            ps.setString(4, diente.getNumeroDiente());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarDientes(int odontogramaId) throws SQLException {
        String sql = "DELETE FROM diente WHERE odontograma_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, odontogramaId);
            ps.executeUpdate();
            return true;
        }
    }

    // ==================================================================================
    // MÉTODOS AUXILIARES
    // ==================================================================================

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

    public boolean inicializarDientes(int odontogramaId) throws SQLException {
        // Crear los 32 dientes con estado "Sano" por defecto
        String[] dientes = {
                // Cuadrante 1 (superior derecho)
                "18", "17", "16", "15", "14", "13", "12", "11",
                // Cuadrante 2 (superior izquierdo)
                "21", "22", "23", "24", "25", "26", "27", "28",
                // Cuadrante 3 (inferior izquierdo)
                "38", "37", "36", "35", "34", "33", "32", "31",
                // Cuadrante 4 (inferior derecho)
                "41", "42", "43", "44", "45", "46", "47", "48"
        };

        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO diente (odontograma_id, numero_diente, estado, observaciones) VALUES (?, ?, 'Sano', '')";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (String numeroDiente : dientes) {
                    ps.setInt(1, odontogramaId);
                    ps.setString(2, numeroDiente);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return true;
        }
    }

    // ==================================================================================
    // MAPEO
    // ==================================================================================

    private Odontograma mapearOdontograma(ResultSet rs) throws SQLException {
        Odontograma o = new Odontograma();
        o.setOdontogramaId(rs.getInt("odontograma_id"));
        o.setPacienteId(rs.getInt("paciente_id"));
        o.setFechaCreacion(rs.getDate("fecha_creacion"));
        o.setObservaciones(rs.getString("observaciones"));
        o.setPacienteNombre(rs.getString("paciente_nombre"));
        o.setPacienteCedula(rs.getString("paciente_cedula"));
        return o;
    }
    // AGREGAR AL FINAL DE LA CLASE, ANTES DEL ÚLTIMO }

    public List<Odontograma> obtenerPorOdontologo(int odontologoId) throws SQLException {
        List<Odontograma> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT o.*, CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, p.cedula as paciente_cedula " +
                "FROM odontograma o " +
                "INNER JOIN paciente p ON o.paciente_id = p.paciente_id " +
                "INNER JOIN cita c ON c.paciente_id = p.paciente_id " +
                "WHERE c.odontologo_id = ? " +
                "ORDER BY o.fecha_creacion DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, odontologoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Odontograma o = mapearOdontograma(rs);
                    lista.add(o);
                }
            }
        }
        return lista;
    }
}