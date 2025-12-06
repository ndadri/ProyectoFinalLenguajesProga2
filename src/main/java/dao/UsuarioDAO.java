package dao;

import models.Usuario;
import utils.ConexionBdd;

import java.sql.*;

public class UsuarioDAO {

    // Validar credenciales de login
    public Usuario validarCredenciales(String usuario, String password) {
        String sql = "SELECT u.*, t.nombre as tipo_usuario_nombre " +
                "FROM usuario u " +
                "INNER JOIN tipo_usuario t ON u.tipo_id = t.tipo_id " +
                "WHERE u.usuario = ? AND u.password = ? AND u.activo = 1";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password); // En producción, usar hash (BCrypt)

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario user = new Usuario();
                user.setUsuarioId(rs.getInt("usuario_id"));
                user.setUsuario(rs.getString("usuario"));
                user.setEmail(rs.getString("email"));
                user.setTipoId(rs.getInt("tipo_id"));
                user.setActivo(rs.getBoolean("activo"));
                user.setTipoUsuarioNombre(rs.getString("tipo_usuario_nombre"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Actualizar último acceso
    public void actualizarUltimoAcceso(int usuarioId) {
        String sql = "UPDATE usuario SET ultimo_acceso = NOW() WHERE usuario_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Crear nuevo usuario
    public boolean crear(Usuario usuario) {
        String sql = "INSERT INTO usuario (tipo_id, usuario, password, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuario.getTipoId());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getPassword()); // En producción, usar hash
            ps.setString(4, usuario.getEmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verificar si un usuario existe
    public boolean existeUsuario(String usuario) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE usuario = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}