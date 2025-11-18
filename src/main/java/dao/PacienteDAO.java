package dao;

import models.Paciente;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public List<Paciente> listar() {
        List<Paciente> lista = new ArrayList<>();

        try (Connection con = ConexionBdd.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM pacientes")) {

            while (rs.next()) {
                lista.add(new Paciente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
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
        String sql = "INSERT INTO pacientes(nombre, apellido, telefono, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBdd.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getEmail());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
