package dao;

import models.Cita;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    public List<Cita> listar() {
        List<Cita> lista = new ArrayList<>();

        try (Connection con = ConexionBdd.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM citas")) {

            while (rs.next()) {
                lista.add(new Cita(
                        rs.getInt("id"),
                        rs.getInt("paciente_id"),
                        rs.getString("fecha"),
                        rs.getString("motivo")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void guardar(Cita c) {
        String sql = "INSERT INTO citas(paciente_id, fecha, motivo) VALUES (?, ?, ?)";

        try (Connection con = ConexionBdd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getPacienteId());
            ps.setString(2, c.getFecha());
            ps.setString(3, c.getMotivo());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
