package integrador.prog2.dao;

import integrador.prog2.config.ConexionDB;
import integrador.prog2.entities.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements GenericDAO<Producto> {

    @Override
    public Producto crear(Producto producto) throws SQLException {
        throw new UnsupportedOperationException("Para crear un producto se debe usar crear(Producto producto, Long idCategoria)");
    }

    public Producto crear(Producto producto, Long idCategoria) throws SQLException {
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
            ps.setLong(7, idCategoria);

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
                    p.created_at AS producto_created_at
                FROM producto p
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
                    p.created_at AS producto_created_at
                FROM producto p
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

    public List<String> listarResumenProductos() throws SQLException {
        String sql = """
            SELECT 
                p.id,
                p.nombre,
                p.precio,
                p.descripcion,
                p.stock,
                p.imagen,
                p.disponible,
                c.nombre AS categoria
            FROM producto p
            LEFT JOIN categoria c ON p.id_categoria = c.id
            WHERE p.eliminado = false
            ORDER BY p.id
            """;

        List<String> productos = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String categoria = rs.getString("categoria");

                if (categoria == null) {
                    categoria = "Sin categoría";
                }

                String producto = "Producto{" +
                        "id=" + rs.getLong("id") +
                        ", nombre='" + rs.getString("nombre") + '\'' +
                        ", precio=" + rs.getDouble("precio") +
                        ", descripcion='" + rs.getString("descripcion") + '\'' +
                        ", stock=" + rs.getInt("stock") +
                        ", imagen='" + rs.getString("imagen") + '\'' +
                        ", disponible=" + rs.getBoolean("disponible") +
                        ", categoria='" + categoria + '\'' +
                        '}';

                productos.add(producto);
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
                    p.created_at AS producto_created_at
                FROM producto p
                WHERE p.eliminado = false
                  AND p.id_categoria = ?
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
        return actualizar(producto, obtenerIdCategoria(producto.getId()));
    }

    public Producto actualizar(Producto producto, Long idCategoria) throws SQLException {
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
            ps.setLong(7, idCategoria);
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
        Producto producto = new Producto(
                rs.getLong("producto_id"),
                rs.getString("producto_nombre"),
                rs.getDouble("precio"),
                rs.getString("producto_descripcion"),
                rs.getInt("stock"),
                rs.getString("imagen"),
                rs.getBoolean("disponible")
        );

        producto.setEliminado(rs.getBoolean("producto_eliminado"));

        Timestamp productoCreatedAt = rs.getTimestamp("producto_created_at");
        if (productoCreatedAt != null) {
            producto.setCreatedAt(productoCreatedAt.toLocalDateTime());
        }

        return producto;
    }

    public Long obtenerIdCategoria(Long idProducto) throws SQLException {
        String sql = """
                SELECT id_categoria
                FROM producto
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long idCategoria = rs.getLong("id_categoria");
                    return rs.wasNull() ? null : idCategoria;
                }
            }
        }

        return null;
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