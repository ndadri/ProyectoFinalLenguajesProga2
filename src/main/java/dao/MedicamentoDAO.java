package dao;

import models.Medicamento;
import utils.ConexionBdd;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {

    public List<Medicamento> obtenerTodos() {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicamento WHERE activo = 1 ORDER BY nombre";

        try (Connection conn = ConexionBdd.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medicamento m = new Medicamento();
                m.setMedicamentoId(rs.getInt("medicamento_id"));
                m.setNombre(rs.getString("nombre"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setPresentacion(rs.getString("presentacion"));
                m.setConcentracion(rs.getString("concentracion"));
                m.setPrecio(rs.getBigDecimal("precio")); // Asegúrate de que tu BD tenga decimal
                m.setStock(rs.getInt("stock"));
                m.setActivo(rs.getBoolean("activo"));
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}