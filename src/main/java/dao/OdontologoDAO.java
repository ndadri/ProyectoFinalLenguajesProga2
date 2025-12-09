package dao;

import models.Odontologo;
import models.Especialidad;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDAO {

    private Connection getConnection() throws SQLException {
        return ConexionBdd.getConnection();
    }

    // CREATE - Insertar nuevo odontólogo
    public boolean insertar(Odontologo odontologo) throws SQLException {
        // Verificar si ya existe la cédula
        if (existeCedula(odontologo.getCedula())) {
            throw new RuntimeException("Ya existe un odontólogo con la cédula " + odontologo.getCedula());
        }

        String sql = "INSERT INTO odontologo (usuario_id, especialidad_id, nombres, apellidos, cedula, " +
                "num_registro, telefono, celular, email, direccion, fecha_nacimiento, genero) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // usuario_id puede ser NULL o tener un valor
            if (odontologo.getUsuarioId() != null) {
                ps.setInt(1, odontologo.getUsuarioId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setInt(2, odontologo.getEspecialidadId());
            ps.setString(3, odontologo.getNombres());
            ps.setString(4, odontologo.getApellidos());
            ps.setString(5, odontologo.getCedula());
            ps.setString(6, odontologo.getNumRegistro());
            ps.setString(7, odontologo.getTelefono());
            ps.setString(8, odontologo.getCelular());
            ps.setString(9, odontologo.getEmail());
            ps.setString(10, odontologo.getDireccion());
            ps.setDate(11, odontologo.getFechaNacimiento());
            ps.setString(12, odontologo.getGenero());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // READ - Obtener todos los odontólogos
    public List<Odontologo> obtenerTodos() {
        List<Odontologo> odontologos = new ArrayList<>();
        String sql = "SELECT o.*, e.nombre as especialidad_nombre, u.usuario as nombre_usuario " +
                "FROM odontologo o " +
                "INNER JOIN especialidad e ON o.especialidad_id = e.especialidad_id " +
                "LEFT JOIN usuario u ON o.usuario_id = u.usuario_id " +
                "WHERE o.activo = 1 " +
                "ORDER BY o.apellidos, o.nombres";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                odontologos.add(mapearOdontologo(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return odontologos;
    }

    // READ - Obtener odontólogo por ID
    public Odontologo obtenerPorId(int id) {
        String sql = "SELECT o.*, e.nombre as especialidad_nombre, u.usuario as nombre_usuario " +
                "FROM odontologo o " +
                "INNER JOIN especialidad e ON o.especialidad_id = e.especialidad_id " +
                "LEFT JOIN usuario u ON o.usuario_id = u.usuario_id " +
                "WHERE o.odontologo_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearOdontologo(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE - Actualizar odontólogo
    public boolean actualizar(Odontologo odontologo) {
        String sql = "UPDATE odontologo SET especialidad_id = ?, nombres = ?, apellidos = ?, " +
                "cedula = ?, num_registro = ?, telefono = ?, celular = ?, email = ?, " +
                "direccion = ?, fecha_nacimiento = ?, genero = ? " +
                "WHERE odontologo_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, odontologo.getEspecialidadId());
            ps.setString(2, odontologo.getNombres());
            ps.setString(3, odontologo.getApellidos());
            ps.setString(4, odontologo.getCedula());
            ps.setString(5, odontologo.getNumRegistro());
            ps.setString(6, odontologo.getTelefono());
            ps.setString(7, odontologo.getCelular());
            ps.setString(8, odontologo.getEmail());
            ps.setString(9, odontologo.getDireccion());
            ps.setDate(10, odontologo.getFechaNacimiento());
            ps.setString(11, odontologo.getGenero());
            ps.setInt(12, odontologo.getOdontologoId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Eliminación lógica
    public boolean eliminar(int id) {
        String sql = "UPDATE odontologo SET activo = 0 WHERE odontologo_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verificar si existe una cédula
    public boolean existeCedula(String cedula) {
        String sql = "SELECT COUNT(*) FROM odontologo WHERE cedula = ? AND activo = 1";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Obtener todas las especialidades
    public List<Especialidad> obtenerEspecialidades() {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT * FROM especialidad WHERE activo = 1 ORDER BY nombre";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Especialidad e = new Especialidad();
                e.setEspecialidadId(rs.getInt("especialidad_id"));
                e.setNombre(rs.getString("nombre"));
                e.setDescripcion(rs.getString("descripcion"));
                e.setActivo(rs.getBoolean("activo"));
                especialidades.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return especialidades;
    }

    // Contar odontólogos activos
    public int contarOdontologos() {
        String sql = "SELECT COUNT(*) FROM odontologo WHERE activo = 1";

        try (Connection conn = getConnection();
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

    // Mapear ResultSet a Odontologo
    private Odontologo mapearOdontologo(ResultSet rs) throws SQLException {
        Odontologo o = new Odontologo();
        o.setOdontologoId(rs.getInt("odontologo_id"));

        // Manejar usuario_id que puede ser NULL
        int usuario_Id = rs.getInt("usuario_id");
        if (!rs.wasNull()) {
            o.setUsuarioId(usuario_Id);
        }

        o.setEspecialidadId(rs.getInt("especialidad_id"));
        o.setNombres(rs.getString("nombres"));
        o.setApellidos(rs.getString("apellidos"));
        o.setCedula(rs.getString("cedula"));
        o.setNumRegistro(rs.getString("num_registro"));
        o.setTelefono(rs.getString("telefono"));
        o.setCelular(rs.getString("celular"));
        o.setEmail(rs.getString("email"));
        o.setDireccion(rs.getString("direccion"));
        o.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        o.setGenero(rs.getString("genero"));
        o.setActivo(rs.getBoolean("activo"));
        o.setFechaCreacion(rs.getTimestamp("fecha_creacion"));

        // Campos adicionales (JOIN)
        o.setEspecialidadNombre(rs.getString("especialidad_nombre"));
        o.setNombreUsuario(rs.getString("nombre_usuario"));

        return o;
    }
}