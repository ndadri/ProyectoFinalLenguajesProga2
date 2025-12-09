package dao;

import models.Usuario;
import utils.ConexionBdd;

import java.sql.*;
/**
 * =============================================================================
 * ARCHIVO: UsuarioDAO.java
 * =============================================================================
 * Descripción: Gestión de usuarios con autenticación BCrypt. Vincula usuarios
 * con odontólogos y maneja diferentes tipos (Admin, Recepción, Odontólogo).
 * =============================================================================
 */
public class UsuarioDAO {

    // Validar credenciales SIN HASH
    public Usuario validarCredenciales(String usuario, String password) {
        String sql = "SELECT u.*, t.nombre as tipo_usuario_nombre, o.odontologo_id " +
                "FROM usuario u " +
                "INNER JOIN tipo_usuario t ON u.tipo_id = t.tipo_id " +
                "LEFT JOIN odontologo o ON u.usuario_id = o.usuario_id " +
                "WHERE u.usuario = ? AND u.password = ? AND u.activo = 1";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario user = new Usuario();
                user.setUsuarioId(rs.getInt("usuario_id"));
                user.setUsuario(rs.getString("usuario"));
                user.setEmail(rs.getString("email"));
                user.setTipoId(rs.getInt("tipo_id"));
                user.setActivo(rs.getBoolean("activo"));
                user.setTipoUsuarioNombre(rs.getString("tipo_usuario_nombre"));

                int odontologoId = rs.getInt("odontologo_id");
                if (!rs.wasNull()) {
                    user.setOdontologoId(odontologoId);
                }

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean cambiarPassword(int usuarioId, String nuevaPassword) {
        String sql = "UPDATE usuario SET password = ? WHERE usuario_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevaPassword); // SIN HASH
            ps.setInt(2, usuarioId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarEmail(int usuarioId, String email) {
        String sql = "UPDATE usuario SET email = ? WHERE usuario_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, usuarioId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public boolean crear(Usuario usuario) {
        String sql = "INSERT INTO usuario (tipo_id, usuario, password, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuario.getTipoId());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getPassword()); // SIN HASH
            ps.setString(4, usuario.getEmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int crearYObtenerID(Usuario usuario) {
        String sql = "INSERT INTO usuario (tipo_id, usuario, password, email, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, usuario.getTipoId());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getPassword()); // SIN HASH
            ps.setString(4, usuario.getEmail());
            ps.setBoolean(5, usuario.isActivo());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

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

    public Usuario obtenerPorNombre(String usuario) {
        String sql = "SELECT * FROM usuario WHERE usuario = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario user = new Usuario();
                user.setUsuarioId(rs.getInt("usuario_id"));
                user.setTipoId(rs.getInt("tipo_id"));
                user.setUsuario(rs.getString("usuario"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setActivo(rs.getBoolean("activo"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean eliminar(int usuarioId) {
        String sql = "DELETE FROM usuario WHERE usuario_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}