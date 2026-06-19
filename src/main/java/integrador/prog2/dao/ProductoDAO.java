package integrador.prog2.dao;

import integrador.prog2.config.ConexionDB;
import integrador.prog2.entities.Categoria;
import integrador.prog2.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements GenericDAO<Producto> {

    @Override
    public Producto crear(Producto producto) throws SQLException {
        String sql = """
                INSERT INTO producto (nombre, precio, descripcion, stock, imagen, disponible, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getDescripcion());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagen());
            ps.setBoolean(6, producto.isDisponible());
            ps.setLong(7, producto.getCategoria().getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setId(rs.getLong(1));
                }
            }

            return producto;
        }
    }

    @Override
    public Producto leer(Long id) throws SQLException {
        String sql = """
                SELECT 
                    p.id AS producto_id,
                    p.nombre AS producto_nombre,
                    p.precio,
                    p.descripcion AS producto_descripcion,
                    p.stock,
                    p.imagen,
                    p.disponible,
                    p.eliminado AS producto_eliminado,
                    p.created_at AS producto_created_at,
                    c.id AS categoria_id,
                    c.nombre AS categoria_nombre,
                    c.descripcion AS categoria_descripcion,
                    c.eliminado AS categoria_eliminado,
                    c.created_at AS categoria_created_at
                FROM producto p
                INNER JOIN categoria c ON p.id_categoria = c.id
                WHERE p.id = ? AND p.eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        String sql = """
                SELECT 
                    p.id AS producto_id,
                    p.nombre AS producto_nombre,
                    p.precio,
                    p.descripcion AS producto_descripcion,
                    p.stock,
                    p.imagen,
                    p.disponible,
                    p.eliminado AS producto_eliminado,
                    p.created_at AS producto_created_at,
                    c.id AS categoria_id,
                    c.nombre AS categoria_nombre,
                    c.descripcion AS categoria_descripcion,
                    c.eliminado AS categoria_eliminado,
                    c.created_at AS categoria_created_at
                FROM producto p
                INNER JOIN categoria c ON p.id_categoria = c.id
                WHERE p.eliminado = false
                ORDER BY p.id
                """;

        List<Producto> productos = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        }

        return productos;
    }

    public List<Producto> listarPorCategoria(Long idCategoria) throws SQLException {
        String sql = """
                SELECT 
                    p.id AS producto_id,
                    p.nombre AS producto_nombre,
                    p.precio,
                    p.descripcion AS producto_descripcion,
                    p.stock,
                    p.imagen,
                    p.disponible,
                    p.eliminado AS producto_eliminado,
                    p.created_at AS producto_created_at,
                    c.id AS categoria_id,
                    c.nombre AS categoria_nombre,
                    c.descripcion AS categoria_descripcion,
                    c.eliminado AS categoria_eliminado,
                    c.created_at AS categoria_created_at
                FROM producto p
                INNER JOIN categoria c ON p.id_categoria = c.id
                WHERE p.eliminado = false
                  AND c.eliminado = false
                  AND c.id = ?
                ORDER BY p.id
                """;

        List<Producto> productos = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
        }

        return productos;
    }

    @Override
    public Producto actualizar(Producto producto) throws SQLException {
        String sql = """
                UPDATE producto
                SET nombre = ?, precio = ?, descripcion = ?, stock = ?, imagen = ?, disponible = ?, id_categoria = ?
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getDescripcion());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagen());
            ps.setBoolean(6, producto.isDisponible());
            ps.setLong(7, producto.getCategoria().getId());
            ps.setLong(8, producto.getId());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return null;
            }

            return producto;
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = """
                UPDATE producto
                SET eliminado = true
                WHERE id = ?
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existe(Long id) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM producto
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public boolean existeCategoria(Long idCategoria) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM categoria
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria(
                rs.getLong("categoria_id"),
                rs.getString("categoria_nombre"),
                rs.getString("categoria_descripcion")
        );

        categoria.setEliminado(rs.getBoolean("categoria_eliminado"));

        Timestamp categoriaCreatedAt = rs.getTimestamp("categoria_created_at");
        if (categoriaCreatedAt != null) {
            categoria.setCreatedAt(categoriaCreatedAt.toLocalDateTime());
        }

        Producto producto = new Producto(
                rs.getLong("producto_id"),
                rs.getString("producto_nombre"),
                rs.getDouble("precio"),
                rs.getString("producto_descripcion"),
                rs.getInt("stock"),
                rs.getString("imagen"),
                rs.getBoolean("disponible"),
                categoria
        );

        producto.setEliminado(rs.getBoolean("producto_eliminado"));

        Timestamp productoCreatedAt = rs.getTimestamp("producto_created_at");
        if (productoCreatedAt != null) {
            producto.setCreatedAt(productoCreatedAt.toLocalDateTime());
        }

        return producto;
    }

    public boolean existenProductosActivosPorCategoria(Long idCategoria) throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM producto
            WHERE id_categoria = ?
              AND eliminado = false
            """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }
}