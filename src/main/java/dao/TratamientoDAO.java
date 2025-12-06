package dao;

import models.TratamientoOdontologico;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TratamientoDAO {

    private Connection getConnection() throws SQLException {
        return ConexionBdd.getConnection();
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<TratamientoOdontologico> obtenerTodos() {
        List<TratamientoOdontologico> lista = new ArrayList<>();
        String sql = "SELECT * FROM tratamiento_odontologico ORDER BY codigo ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TratamientoOdontologico t = new TratamientoOdontologico();

                t.setTratamiento_id(rs.getInt("tratamiento_id"));
                t.setCodigo(rs.getString("codigo"));
                t.setNombre(rs.getString("nombre"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setCategoria(rs.getString("categoria"));
                t.setPrecio_base(rs.getBigDecimal("precio_base"));
                t.setDuracion_aproximada(rs.getInt("duracion_aproximada"));
                t.setRequiere_anestesia(rs.getInt("requiere_anestesia"));
                t.setActivo(rs.getInt("activo"));

                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public TratamientoOdontologico obtenerPorId(int id) {
        TratamientoOdontologico t = null;
        String sql = "SELECT * FROM tratamiento_odontologico WHERE tratamiento_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    t = new TratamientoOdontologico();

                    t.setTratamiento_id(rs.getInt("tratamiento_id"));
                    t.setCodigo(rs.getString("codigo"));
                    t.setNombre(rs.getString("nombre"));
                    t.setDescripcion(rs.getString("descripcion"));
                    t.setCategoria(rs.getString("categoria"));
                    t.setPrecio_base(rs.getBigDecimal("precio_base"));
                    t.setDuracion_aproximada(rs.getInt("duracion_aproximada"));
                    t.setRequiere_anestesia(rs.getInt("requiere_anestesia"));
                    t.setActivo(rs.getInt("activo"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    // =========================
    // INSERTAR
    // =========================
    public boolean insertar(TratamientoOdontologico t) {
        String sql = "INSERT INTO tratamiento_odontologico " +
                "(codigo, nombre, descripcion, categoria, precio_base, duracion_aproximada, requiere_anestesia, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getCodigo());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getDescripcion());
            ps.setString(4, t.getCategoria());
            ps.setBigDecimal(5, t.getPrecio_base());
            ps.setInt(6, t.getDuracion_aproximada());
            ps.setInt(7, t.getRequiere_anestesia());
            ps.setInt(8, t.getActivo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public boolean actualizar(TratamientoOdontologico t) {
        String sql = "UPDATE tratamiento_odontologico SET " +
                "codigo=?, nombre=?, descripcion=?, categoria=?, precio_base=?, duracion_aproximada=?, requiere_anestesia=?, activo=? " +
                "WHERE tratamiento_id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getCodigo());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getDescripcion());
            ps.setString(4, t.getCategoria());
            ps.setBigDecimal(5, t.getPrecio_base());
            ps.setInt(6, t.getDuracion_aproximada());
            ps.setInt(7, t.getRequiere_anestesia());
            ps.setInt(8, t.getActivo());
            ps.setInt(9, t.getTratamiento_id());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean eliminar(int id) {
        String sql = "DELETE FROM tratamiento_odontologico WHERE tratamiento_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
