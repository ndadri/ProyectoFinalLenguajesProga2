package dao;

import models.Usuario;
import utils.ConexionBdd;
import utils.PasswordUtil;

import java.sql.*;

public class UsuarioDAO {

    // Validar credenciales de login CON BCRYPT
    public Usuario validarCredenciales(String usuario, String password) {
        String sql = "SELECT u.*, t.nombre as tipo_usuario_nombre, o.odontologo_id " +
                "FROM usuario u " +
                "INNER JOIN tipo_usuario t ON u.tipo_id = t.tipo_id " +
                "LEFT JOIN odontologo o ON u.usuario_id = o.usuario_id " +
                "WHERE u.usuario = ? AND u.activo = 1";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                // Verificar contraseña con BCrypt
                if (PasswordUtil.checkPassword(password, hashedPassword)) {
                    Usuario user = new Usuario();
                    user.setUsuarioId(rs.getInt("usuario_id"));
                    user.setUsuario(rs.getString("usuario"));
                    user.setEmail(rs.getString("email"));
                    user.setTipoId(rs.getInt("tipo_id"));
                    user.setActivo(rs.getBoolean("activo"));
                    user.setTipoUsuarioNombre(rs.getString("tipo_usuario_nombre"));

                    // Si es odontólogo, obtener su ID
                    int odontologoId = rs.getInt("odontologo_id");
                    if (!rs.wasNull()) {
                        user.setOdontologoId(odontologoId);
                    }

                    return user;
                }
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

    // Crear nuevo usuario CON HASH
    public boolean crear(Usuario usuario) {
        String sql = "INSERT INTO usuario (tipo_id, usuario, password, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuario.getTipoId());
            ps.setString(2, usuario.getUsuario());
            // HASHEAR la contraseña antes de guardar
            ps.setString(3, PasswordUtil.hashPassword(usuario.getPassword()));
            ps.setString(4, usuario.getEmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Crear y obtener ID CON HASH
    public int crearYObtenerID(Usuario usuario) {
        String sql = "INSERT INTO usuario (tipo_id, usuario, password, email, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, usuario.getTipoId());
            ps.setString(2, usuario.getUsuario());
            // HASHEAR la contraseña antes de guardar
            ps.setString(3, PasswordUtil.hashPassword(usuario.getPassword()));
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

    // Obtener usuario por nombre
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

    // Obtener usuario por ID
    public Usuario obtenerPorId(int usuarioId) {
        String sql = "SELECT u.*, t.nombre as tipo_usuario_nombre " +
                "FROM usuario u " +
                "INNER JOIN tipo_usuario t ON u.tipo_id = t.tipo_id " +
                "WHERE u.usuario_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario user = new Usuario();
                user.setUsuarioId(rs.getInt("usuario_id"));
                user.setTipoId(rs.getInt("tipo_id"));
                user.setUsuario(rs.getString("usuario"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setActivo(rs.getBoolean("activo"));
                user.setTipoUsuarioNombre(rs.getString("tipo_usuario_nombre"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Cambiar contraseña CON HASH
    public boolean cambiarPassword(int usuarioId, String nuevaPassword) {
        String sql = "UPDATE usuario SET password = ? WHERE usuario_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // HASHEAR la nueva contraseña
            ps.setString(1, PasswordUtil.hashPassword(nuevaPassword));
            ps.setInt(2, usuarioId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verificar contraseña actual
    public boolean verificarPassword(int usuarioId, String password) {
        String sql = "SELECT password FROM usuario WHERE usuario_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return PasswordUtil.checkPassword(password, hashedPassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Eliminar usuario
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