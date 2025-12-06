package dao;

import models.Cita;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    // Obtener citas recientes
    public List<Cita> obtenerCitasRecientes(int limite) {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, " +
                "CONCAT(p.nombres, ' ', p.apellidos) as paciente_nombre, " +
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
        return c;
    }
}