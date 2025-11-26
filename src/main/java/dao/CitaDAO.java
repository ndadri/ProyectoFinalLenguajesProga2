package dao;

import models.Cita;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    public List<Cita> listar() {
        // CORRECCIÓN: Tabla 'cita' y columnas correctas
        String sql = "SELECT cita_id, paciente_id, medico_id, fecha_hora, motivo, estado FROM cita";
        List<Cita> lista = new ArrayList<>();

        try (Connection con = ConexionBdd.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Cita(
                        rs.getInt("cita_id"),
                        rs.getInt("paciente_id"),
                        rs.getInt("medico_id"),
                        rs.getString("fecha_hora"),
                        rs.getString("motivo"),
                        rs.getString("estado")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void guardar(Cita c) {
        // CORRECCIÓN: Tabla 'cita', columnas correctas y 5 placeholders
        String sql = "INSERT INTO cita(paciente_id, medico_id, fecha_hora, motivo, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBdd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getPacienteId());
            ps.setInt(2, c.getMedicoId());
            ps.setString(3, c.getFechaHora());
            ps.setString(4, c.getMotivo());
            ps.setString(5, c.getEstado());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}