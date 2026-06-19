package integrador.prog2.dao;

import integrador.prog2.config.ConexionDB;
import integrador.prog2.entities.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements GenericDAO<Categoria> {

    @Override
    public Categoria crear(Categoria categoria) throws SQLException {
        String sql = """
                INSERT INTO categoria (nombre, descripcion)
                VALUES (?, ?)
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getLong(1));
                }
            }

            return categoria;
        }
    }

    @Override
    public Categoria leer(Long id) throws SQLException {
        String sql = """
                SELECT id, nombre, descripcion, eliminado, created_at
                FROM categoria
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        String sql = """
                SELECT id, nombre, descripcion, eliminado, created_at
                FROM categoria
                WHERE eliminado = false
                ORDER BY id
                """;

        List<Categoria> categorias = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
        }

        return categorias;
    }

    @Override
    public Categoria actualizar(Categoria categoria) throws SQLException {
        String sql = """
                UPDATE categoria
                SET nombre = ?, descripcion = ?
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setLong(3, categoria.getId());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return null;
            }

            return categoria;
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = """
                UPDATE categoria
                SET eliminado = true
                WHERE id = ?
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existeNombre(String nombre) throws SQLException {
        String sql = """
                SELECT COUNT(*) 
                FROM categoria
                WHERE nombre = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("descripcion")
        );

        categoria.setEliminado(rs.getBoolean("eliminado"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            categoria.setCreatedAt(createdAt.toLocalDateTime());
        }

        return categoria;
    }
}