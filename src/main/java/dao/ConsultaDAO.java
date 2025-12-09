package dao;

import models.Consulta;
import models.Paciente;
import models.Odontologo;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * ConsultaDAO - Data Access Object para la gestión de consultas médicas
 *
 * Descripción: Maneja todas las operaciones de base de datos relacionadas con
 * consultas odontológicas. Permite registrar y consultar el historial médico
 * completo de cada paciente incluyendo diagnósticos, tratamientos y seguimientos.
 */
public class ConsultaDAO {

    // CREATE - Insertar nueva consulta
    public boolean insertar(Consulta consulta) {
        String sql = "INSERT INTO consulta (cita_id, paciente_id, odontologo_id, motivo_consulta, " +
                "sintomas, diagnostico, tratamiento, dientes_tratados, procedimientos, " +
                "observaciones, pronostico, proxima_cita, requiere_seguimiento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (consulta.getCitaId() != null) {
                ps.setInt(1, consulta.getCitaId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setInt(2, consulta.getPacienteId());
            ps.setInt(3, consulta.getOdontologoId());
            ps.setString(4, consulta.getMotivoConsulta());
            ps.setString(5, consulta.getSintomas());
            ps.setString(6, consulta.getDiagnostico());
            ps.setString(7, consulta.getTratamiento());
            ps.setString(8, consulta.getDientesTratados());
            ps.setString(9, consulta.getProcedimientos());
            ps.setString(10, consulta.getObservaciones());
            ps.setString(11, consulta.getPronostico());
            ps.setDate(12, consulta.getProximaCita());
            ps.setBoolean(13, consulta.isRequiereSeguimiento());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar consulta: " + e.getMessage(), e);
        }
    }

    // READ - Obtener todas las consultas
    public List<Consulta> obtenerTodos() {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.cedula as paciente_cedula, " +
                "CONCAT(o.nombres, ' ', o.apellidos) as odontologo_nombre " +
                "FROM consulta c " +
                "INNER JOIN paciente p ON c.paciente_id = p.paciente_id " +
                "INNER JOIN odontologo o ON c.odontologo_id = o.odontologo_id " +
                "ORDER BY c.fecha_consulta DESC";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBdd.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                consultas.add(mapearConsulta(rs));
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

        return consultas;
    }

    // READ - Obtener consulta por ID
    public Consulta obtenerPorId(int id) {
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.cedula as paciente_cedula, " +
                "CONCAT(o.nombres, ' ', o.apellidos) as odontologo_nombre " +
                "FROM consulta c " +
                "INNER JOIN paciente p ON c.paciente_id = p.paciente_id " +
                "INNER JOIN odontologo o ON c.odontologo_id = o.odontologo_id " +
                "WHERE c.consulta_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearConsulta(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ - Obtener historial de consultas de un paciente
    public List<Consulta> obtenerPorPaciente(int pacienteId) {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.cedula as paciente_cedula, " +
                "CONCAT(o.nombres, ' ', o.apellidos) as odontologo_nombre " +
                "FROM consulta c " +
                "INNER JOIN paciente p ON c.paciente_id = p.paciente_id " +
                "INNER JOIN odontologo o ON c.odontologo_id = o.odontologo_id " +
                "WHERE c.paciente_id = ? " +
                "ORDER BY c.fecha_consulta DESC";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacienteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                consultas.add(mapearConsulta(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consultas;
    }

    // UPDATE - Actualizar consulta
    public boolean actualizar(Consulta consulta) {
        String sql = "UPDATE consulta SET motivo_consulta = ?, sintomas = ?, diagnostico = ?, " +
                "tratamiento = ?, dientes_tratados = ?, procedimientos = ?, observaciones = ?, " +
                "pronostico = ?, proxima_cita = ?, requiere_seguimiento = ? " +
                "WHERE consulta_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, consulta.getMotivoConsulta());
            ps.setString(2, consulta.getSintomas());
            ps.setString(3, consulta.getDiagnostico());
            ps.setString(4, consulta.getTratamiento());
            ps.setString(5, consulta.getDientesTratados());
            ps.setString(6, consulta.getProcedimientos());
            ps.setString(7, consulta.getObservaciones());
            ps.setString(8, consulta.getPronostico());
            ps.setDate(9, consulta.getProximaCita());
            ps.setBoolean(10, consulta.isRequiereSeguimiento());
            ps.setInt(11, consulta.getConsultaId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Eliminar consulta
    public boolean eliminar(int id) {
        String sql = "DELETE FROM consulta WHERE consulta_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener consultas recientes
    public List<Consulta> obtenerRecientes(int limite) {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
                "p.cedula as paciente_cedula, " +
                "CONCAT(o.nombres, ' ', o.apellidos) as odontologo_nombre " +
                "FROM consulta c " +
                "INNER JOIN paciente p ON c.paciente_id = p.paciente_id " +
                "INNER JOIN odontologo o ON c.odontologo_id = o.odontologo_id " +
                "ORDER BY c.fecha_consulta DESC LIMIT ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                consultas.add(mapearConsulta(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consultas;
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

    // Contar consultas
    public int contarConsultas() {
        String sql = "SELECT COUNT(*) FROM consulta";

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

    // Mapear ResultSet a Consulta
    private Consulta mapearConsulta(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setConsultaId(rs.getInt("consulta_id"));

        int citaId = rs.getInt("cita_id");
        if (!rs.wasNull()) {
            c.setCitaId(citaId);
        }

        c.setPacienteId(rs.getInt("paciente_id"));
        c.setOdontologoId(rs.getInt("odontologo_id"));
        c.setFechaConsulta(rs.getTimestamp("fecha_consulta"));
        c.setMotivoConsulta(rs.getString("motivo_consulta"));
        c.setSintomas(rs.getString("sintomas"));
        c.setDiagnostico(rs.getString("diagnostico"));
        c.setTratamiento(rs.getString("tratamiento"));
        c.setDientesTratados(rs.getString("dientes_tratados"));
        c.setProcedimientos(rs.getString("procedimientos"));
        c.setObservaciones(rs.getString("observaciones"));
        c.setPronostico(rs.getString("pronostico"));
        c.setProximaCita(rs.getDate("proxima_cita"));
        c.setRequiereSeguimiento(rs.getBoolean("requiere_seguimiento"));
        c.setFechaCreacion(rs.getTimestamp("fecha_creacion"));

        // Campos adicionales
        c.setPacienteNombre(rs.getString("paciente_nombre"));
        c.setPacienteCedula(rs.getString("paciente_cedula"));
        c.setOdontologoNombre(rs.getString("odontologo_nombre"));

        return c;
    }
}