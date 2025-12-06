package dao;

import models.Cita;
import models.Paciente;
import models.Odontologo;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    // CREATE - Insertar nueva cita
    public boolean insertar(Cita cita) {
        String sql = "INSERT INTO cita (paciente_id, odontologo_id, fecha_hora, duracion_minutos, " +
                "motivo, tipo_cita, estado, consultorio, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cita.getPacienteId());
            ps.setInt(2, cita.getOdontologoId());
            ps.setTimestamp(3, cita.getFechaHora());
            ps.setInt(4, cita.getDuracionMinutos());
            ps.setString(5, cita.getMotivo());
            ps.setString(6, cita.getTipoCita());
            ps.setString(7, cita.getEstado());
            ps.setString(8, cita.getConsultorio());
            ps.setString(9, cita.getObservaciones());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar cita: " + e.getMessage(), e);
        }
    }

    // READ - Obtener todas las citas
    public List<Cita> obtenerTodos() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.telefono as paciente_telefono, " +
                "p.cedula as paciente_cedula, " +
                "CONCAT(o.nombres, ' ', o.apellidos) as odontologo_nombre " +
                "FROM cita c " +
                "INNER JOIN paciente p ON c.paciente_id = p.paciente_id " +
                "INNER JOIN odontologo o ON c.odontologo_id = o.odontologo_id " +
                "ORDER BY c.fecha_hora DESC";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBdd.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                citas.add(mapearCita(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return citas;
    }

    // READ - Obtener cita por ID
    public Cita obtenerPorId(int id) {
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.telefono as paciente_telefono, " +
                "p.cedula as paciente_cedula, " +
                "CONCAT(o.nombres, ' ', o.apellidos) as odontologo_nombre " +
                "FROM cita c " +
                "INNER JOIN paciente p ON c.paciente_id = p.paciente_id " +
                "INNER JOIN odontologo o ON c.odontologo_id = o.odontologo_id " +
                "WHERE c.cita_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearCita(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ - Obtener citas de hoy
    public List<Cita> obtenerCitasHoy() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.telefono as paciente_telefono, " +
                "p.cedula as paciente_cedula, " +
                "CONCAT(o.nombres, ' ', o.apellidos) as odontologo_nombre " +
                "FROM cita c " +
                "INNER JOIN paciente p ON c.paciente_id = p.paciente_id " +
                "INNER JOIN odontologo o ON c.odontologo_id = o.odontologo_id " +
                "WHERE DATE(c.fecha_hora) = CURDATE() " +
                "ORDER BY c.fecha_hora";

        try (Connection conn = ConexionBdd.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                citas.add(mapearCita(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return citas;
    }

    // READ - Obtener citas por estado
    public List<Cita> obtenerPorEstado(String estado) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.telefono as paciente_telefono, " +
                "p.cedula as paciente_cedula, " +
                "CONCAT(o.nombres, ' ', o.apellidos) as odontologo_nombre " +
                "FROM cita c " +
                "INNER JOIN paciente p ON c.paciente_id = p.paciente_id " +
                "INNER JOIN odontologo o ON c.odontologo_id = o.odontologo_id " +
                "WHERE c.estado = ? " +
                "ORDER BY c.fecha_hora DESC";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                citas.add(mapearCita(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return citas;
    }

    // UPDATE - Actualizar cita
    public boolean actualizar(Cita cita) {
        String sql = "UPDATE cita SET paciente_id = ?, odontologo_id = ?, fecha_hora = ?, " +
                "duracion_minutos = ?, motivo = ?, tipo_cita = ?, estado = ?, " +
                "consultorio = ?, observaciones = ? WHERE cita_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cita.getPacienteId());
            ps.setInt(2, cita.getOdontologoId());
            ps.setTimestamp(3, cita.getFechaHora());
            ps.setInt(4, cita.getDuracionMinutos());
            ps.setString(5, cita.getMotivo());
            ps.setString(6, cita.getTipoCita());
            ps.setString(7, cita.getEstado());
            ps.setString(8, cita.getConsultorio());
            ps.setString(9, cita.getObservaciones());
            ps.setInt(10, cita.getCitaId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE - Cambiar estado de cita
    public boolean cambiarEstado(int citaId, String nuevoEstado) {
        String sql = "UPDATE cita SET estado = ? WHERE cita_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, citaId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Eliminar cita
    public boolean eliminar(int id) {
        String sql = "DELETE FROM cita WHERE cita_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener citas recientes
    public List<Cita> obtenerCitasRecientes(int limite) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.telefono as paciente_telefono, " +
                "p.cedula as paciente_cedula, " +
                "CONCAT(o.nombres, ' ', o.apellidos) as odontologo_nombre " +
                "FROM cita c " +
                "INNER JOIN paciente p ON c.paciente_id = p.paciente_id " +
                "INNER JOIN odontologo o ON c.odontologo_id = o.odontologo_id " +
                "ORDER BY c.fecha_hora DESC LIMIT ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                citas.add(mapearCita(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return citas;
    }

    // Contar citas de hoy
    public int contarCitasHoy() {
        String sql = "SELECT COUNT(*) FROM cita WHERE DATE(fecha_hora) = CURDATE()";

        try (Connection conn = ConexionBdd.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Contar citas pendientes
    public int contarCitasPendientes() {
        String sql = "SELECT COUNT(*) FROM cita WHERE estado IN ('Programada', 'Confirmada')";

        try (Connection conn = ConexionBdd.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Obtener todos los pacientes (para dropdown)
    public List<Paciente> obtenerPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT paciente_id, nombres, apellidos, cedula FROM paciente WHERE activo = 1 ORDER BY apellidos, nombres";

        try (Connection conn = ConexionBdd.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Paciente p = new Paciente();
                p.setPacienteId(rs.getInt("paciente_id"));
                p.setNombres(rs.getString("nombres"));
                p.setApellidos(rs.getString("apellidos"));
                p.setCedula(rs.getString("cedula"));
                pacientes.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientes;
    }

    // Obtener todos los odontólogos (para dropdown)
    public List<Odontologo> obtenerOdontologos() {
        List<Odontologo> odontologos = new ArrayList<>();
        String sql = "SELECT o.odontologo_id, o.nombres, o.apellidos, e.nombre as especialidad " +
                "FROM odontologo o " +
                "INNER JOIN especialidad e ON o.especialidad_id = e.especialidad_id " +
                "WHERE o.activo = 1 ORDER BY o.apellidos, o.nombres";

        try (Connection conn = ConexionBdd.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Odontologo o = new Odontologo();
                o.setOdontologoId(rs.getInt("odontologo_id"));
                o.setNombres(rs.getString("nombres"));
                o.setApellidos(rs.getString("apellidos"));
                o.setEspecialidadNombre(rs.getString("especialidad"));
                odontologos.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return odontologos;
    }

    // Mapear ResultSet a Cita
    private Cita mapearCita(ResultSet rs) throws SQLException {
        Cita c = new Cita();
        c.setCitaId(rs.getInt("cita_id"));
        c.setPacienteId(rs.getInt("paciente_id"));
        c.setOdontologoId(rs.getInt("odontologo_id"));
        c.setFechaHora(rs.getTimestamp("fecha_hora"));
        c.setDuracionMinutos(rs.getInt("duracion_minutos"));
        c.setMotivo(rs.getString("motivo"));
        c.setTipoCita(rs.getString("tipo_cita"));
        c.setEstado(rs.getString("estado"));
        c.setConsultorio(rs.getString("consultorio"));
        c.setObservaciones(rs.getString("observaciones"));
        c.setPacienteNombre(rs.getString("paciente_nombre"));
        c.setOdontologoNombre(rs.getString("odontologo_nombre"));
        c.setPacienteTelefono(rs.getString("paciente_telefono"));
        c.setPacienteCedula(rs.getString("paciente_cedula"));
        return c;
    }
}