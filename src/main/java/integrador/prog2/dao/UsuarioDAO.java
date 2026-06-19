package integrador.prog2.dao;

import integrador.prog2.config.ConexionDB;
import integrador.prog2.entities.Usuario;
import integrador.prog2.enums.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements GenericDAO<Usuario> {

    @Override
    public Usuario crear(Usuario usuario) throws SQLException {
        String sql = """
                INSERT INTO usuario (nombre, apellido, mail, celular, contrasena, rol)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContraseña());
            ps.setString(6, usuario.getRol().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }

            return usuario;
        }
    }

    @Override
    public Usuario leer(Long id) throws SQLException {
        String sql = """
                SELECT id, nombre, apellido, mail, celular, contrasena, rol, eliminado, created_at
                FROM usuario
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Usuario> listar() throws SQLException {
        String sql = """
                SELECT id, nombre, apellido, mail, celular, contrasena, rol, eliminado, created_at
                FROM usuario
                WHERE eliminado = false
                ORDER BY id
                """;

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }

        return usuarios;
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws SQLException {
        String sql = """
                UPDATE usuario
                SET nombre = ?, apellido = ?, mail = ?, celular = ?, contrasena = ?, rol = ?
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContraseña());
            ps.setString(6, usuario.getRol().name());
            ps.setLong(7, usuario.getId());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return null;
            }

            return usuario;
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = """
                UPDATE usuario
                SET eliminado = true
                WHERE id = ?
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existeMail(String mail) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM usuario
                WHERE mail = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, mail);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("mail"),
                rs.getString("celular"),
                rs.getString("contrasena"),
                Rol.valueOf(rs.getString("rol"))
        );

        usuario.setEliminado(rs.getBoolean("eliminado"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            usuario.setCreatedAt(createdAt.toLocalDateTime());
        }

        return usuario;
    }
}