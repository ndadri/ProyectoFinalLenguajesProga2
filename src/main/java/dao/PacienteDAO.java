package dao;

import models.Paciente;
import utils.ConexionBdd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * =============================================================================
 * ARCHIVO: PacienteDAO.java
 * =============================================================================
 * Descripción: Maneja operaciones de BD para pacientes incluyendo historial
 * médico, alergias, contactos de emergencia y cálculo automático de edad.
 * =============================================================================
 */
public class PacienteDAO {

    // CREATE - Insertar nuevo paciente
    public boolean insertar(Paciente paciente) {
        // Primero verificar si ya existe la cédula
        if (existeCedula(paciente.getCedula())) {
            throw new RuntimeException("Ya existe un paciente con la cédula " + paciente.getCedula());
        }

        String sql = "INSERT INTO paciente (nombres, apellidos, cedula, fecha_nacimiento, edad, genero, " +
                "telefono, email, direccion, ocupacion, contacto_emergencia, telefono_emergencia, " +
                "alergias, enfermedades_sistemicas, medicamentos_actuales, embarazada, notas) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getNombres());
            ps.setString(2, paciente.getApellidos());
            ps.setString(3, paciente.getCedula());
            ps.setDate(4, paciente.getFechaNacimiento());
            ps.setInt(5, paciente.getEdad());
            ps.setString(6, paciente.getGenero());
            ps.setString(7, paciente.getTelefono());
            ps.setString(8, paciente.getEmail());
            ps.setString(9, paciente.getDireccion());
            ps.setString(10, paciente.getOcupacion());
            ps.setString(11, paciente.getContactoEmergencia());
            ps.setString(12, paciente.getTelefonoEmergencia());
            ps.setString(13, paciente.getAlergias());
            ps.setString(14, paciente.getEnfermedadesSistemicas());
            ps.setString(15, paciente.getMedicamentosActuales());

            if (paciente.getEmbarazada() != null) {
                ps.setBoolean(16, paciente.getEmbarazada());
            } else {
                ps.setNull(16, Types.BOOLEAN);
            }

            ps.setString(17, paciente.getNotas());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar paciente: " + e.getMessage(), e);
        }
    }

    // Verificar si existe una cédula
    public boolean existeCedula(String cedula) {
        String sql = "SELECT COUNT(*) FROM paciente WHERE cedula = ?";

        try (Connection conn = ConexionBdd.getConnection();
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

    // READ - Obtener todos los pacientes
    public List<Paciente> obtenerTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente WHERE activo = 1 ORDER BY apellidos, nombres";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBdd.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            System.out.println("=== DEBUG obtenerTodos() ===");
            System.out.println("Query ejecutado: " + sql);

            int contador = 0;
            while (rs.next()) {
                contador++;
                Paciente p = mapearPaciente(rs);

                if (p != null) {
                    pacientes.add(p);
                    System.out.println("Paciente #" + contador + " agregado correctamente: " + p.getNombres());
                } else {
                    System.err.println("ERROR: mapearPaciente retornó null para registro #" + contador);
                }
            }

            System.out.println("Total de pacientes agregados a la lista: " + pacientes.size());

        } catch (SQLException e) {
            System.err.println("ERROR en obtenerTodos(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar recursos en orden inverso
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return pacientes;
    }

    // READ - Obtener paciente por ID
    public Paciente obtenerPorId(int id) {
        String sql = "SELECT * FROM paciente WHERE paciente_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearPaciente(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ - Buscar pacientes por nombre o cédula
    public List<Paciente> buscar(String termino) {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente WHERE activo = 1 AND " +
                "(nombres LIKE ? OR apellidos LIKE ? OR cedula LIKE ?) " +
                "ORDER BY apellidos, nombres";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String busqueda = "%" + termino + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            ps.setString(3, busqueda);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pacientes.add(mapearPaciente(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pacientes;
    }

    // UPDATE - Actualizar paciente
    public boolean actualizar(Paciente paciente) {
        String sql = "UPDATE paciente SET nombres = ?, apellidos = ?, cedula = ?, " +
                "fecha_nacimiento = ?, edad = ?, genero = ?, telefono = ?, email = ?, " +
                "direccion = ?, ocupacion = ?, contacto_emergencia = ?, telefono_emergencia = ?, " +
                "alergias = ?, enfermedades_sistemicas = ?, medicamentos_actuales = ?, " +
                "embarazada = ?, notas = ? WHERE paciente_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getNombres());
            ps.setString(2, paciente.getApellidos());
            ps.setString(3, paciente.getCedula());
            ps.setDate(4, paciente.getFechaNacimiento());
            ps.setInt(5, paciente.getEdad());
            ps.setString(6, paciente.getGenero());
            ps.setString(7, paciente.getTelefono());
            ps.setString(8, paciente.getEmail());
            ps.setString(9, paciente.getDireccion());
            ps.setString(10, paciente.getOcupacion());
            ps.setString(11, paciente.getContactoEmergencia());
            ps.setString(12, paciente.getTelefonoEmergencia());
            ps.setString(13, paciente.getAlergias());
            ps.setString(14, paciente.getEnfermedadesSistemicas());
            ps.setString(15, paciente.getMedicamentosActuales());

            if (paciente.getEmbarazada() != null) {
                ps.setBoolean(16, paciente.getEmbarazada());
            } else {
                ps.setNull(16, Types.BOOLEAN);
            }

            ps.setString(17, paciente.getNotas());
            ps.setInt(18, paciente.getPacienteId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Eliminación lógica (desactivar)
    public boolean eliminar(int id) {
        String sql = "UPDATE paciente SET activo = 0 WHERE paciente_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Eliminación física (usar con precaución)
    public boolean eliminarFisico(int id) {
        String sql = "DELETE FROM paciente WHERE paciente_id = ?";

        try (Connection conn = ConexionBdd.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método auxiliar para mapear ResultSet a Paciente
    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();

        try {
            p.setPacienteId(rs.getInt("paciente_id"));
            p.setNombres(rs.getString("nombres"));
            p.setApellidos(rs.getString("apellidos"));
            p.setCedula(rs.getString("cedula"));
            p.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
            p.setEdad(rs.getInt("edad"));
            p.setGenero(rs.getString("genero"));
            p.setTelefono(rs.getString("telefono"));
            p.setEmail(rs.getString("email"));
            p.setDireccion(rs.getString("direccion"));
            p.setOcupacion(rs.getString("ocupacion"));
            p.setContactoEmergencia(rs.getString("contacto_emergencia"));
            p.setTelefonoEmergencia(rs.getString("telefono_emergencia"));
            p.setAlergias(rs.getString("alergias"));
            p.setEnfermedadesSistemicas(rs.getString("enfermedades_sistemicas"));
            p.setMedicamentosActuales(rs.getString("medicamentos_actuales"));

            // Embarazada puede ser null
            Boolean embarazada = rs.getBoolean("embarazada");
            if (!rs.wasNull()) {
                p.setEmbarazada(embarazada);
            }

            p.setActivo(rs.getBoolean("activo"));
            p.setNotas(rs.getString("notas"));
            p.setFechaRegistro(rs.getTimestamp("fecha_registro"));
            p.setUltimaVisita(rs.getDate("ultima_visita"));

            // Debug - Imprimir lo que se está mapeando
            System.out.println("Mapeando paciente: ID=" + p.getPacienteId() +
                    ", Nombres=" + p.getNombres() +
                    ", Apellidos=" + p.getApellidos() +
                    ", Cédula=" + p.getCedula());

        } catch (SQLException e) {
            System.err.println("Error al mapear paciente: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return p;
    }

    // Método para obtener estadísticas
    public int contarPacientes() {
        String sql = "SELECT COUNT(*) FROM paciente WHERE activo = 1";

        try (Connection conn = ConexionBdd.getConnection();
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
}