package dao;

import models.Paciente;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public List<Paciente> listar() {
        // CORREGIDO: Selecciona solo las 5 columnas existentes en la tabla paciente
        String sql = "SELECT paciente_id, nombres, apellidos, telefono, email FROM paciente";
        List<Paciente> lista = new ArrayList<>();

        try (Connection con = ConexionBdd.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Paciente(
                        rs.getInt("paciente_id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("email")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void guardar(Paciente p) {
        // CORREGIDO: Inserta solo las 4 columnas de datos de la tabla paciente
        String sql = "INSERT INTO paciente(nombres, apellidos, telefono, email) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBdd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombres());
            ps.setString(2, p.getApellidos());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getEmail());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}