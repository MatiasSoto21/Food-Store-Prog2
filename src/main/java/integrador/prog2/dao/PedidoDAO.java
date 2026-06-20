package integrador.prog2.dao;

import integrador.prog2.config.ConexionDB;
import integrador.prog2.entities.*;

import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements GenericDAO<Pedido> {

    @Override
    public Pedido crear(Pedido pedido) throws SQLException {
        throw new UnsupportedOperationException("Para crear un pedido se debe usar crearPedido(Long idUsuario, Pedido pedido)");
    }

    public Pedido crearPedido(Long idUsuario, Pedido pedido) throws SQLException {
        String sqlPedido = """
                INSERT INTO pedido (fecha, estado, total, forma_pago, id_usuario)
                VALUES (?, ?, ?, ?, ?)
                """;

        String sqlDetalle = """
                INSERT INTO detalle_pedido (cantidad, subtotal, id_pedido, id_producto)
                VALUES (?, ?, ?, ?)
                """;

        String sqlDescontarStock = """
                UPDATE producto
                SET stock = stock - ?,
                    disponible = CASE WHEN stock - ? <= 0 THEN false ELSE disponible END
                WHERE id = ?
                  AND eliminado = false
                  AND disponible = true
                  AND stock >= ?
                """;

        Connection conexion = null;

        try {
            conexion = ConexionDB.getConexion();
            conexion.setAutoCommit(false);

            pedido.calcularTotal();

            try (PreparedStatement psPedido = conexion.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psPedido.setDate(1, Date.valueOf(pedido.getFecha()));
                psPedido.setString(2, pedido.getEstado().name());
                psPedido.setDouble(3, pedido.getTotal());
                psPedido.setString(4, pedido.getFormaPago().name());
                psPedido.setLong(5, idUsuario);

                psPedido.executeUpdate();

                try (ResultSet rs = psPedido.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getLong(1));
                    }
                }
            }

            for (DetallePedido detalle : pedido.getDetalles()) {
                Producto producto = detalle.getProducto();

                try (PreparedStatement psStock = conexion.prepareStatement(sqlDescontarStock)) {
                    psStock.setInt(1, detalle.getCantidad());
                    psStock.setInt(2, detalle.getCantidad());
                    psStock.setLong(3, producto.getId());
                    psStock.setInt(4, detalle.getCantidad());

                    int filasStock = psStock.executeUpdate();

                    if (filasStock == 0) {
                        throw new SQLException("No hay stock suficiente para el producto con id " + producto.getId());
                    }
                }

                try (PreparedStatement psDetalle = conexion.prepareStatement(sqlDetalle, Statement.RETURN_GENERATED_KEYS)) {
                    psDetalle.setInt(1, detalle.getCantidad());
                    psDetalle.setDouble(2, detalle.getSubtotal());
                    psDetalle.setLong(3, pedido.getId());
                    psDetalle.setLong(4, producto.getId());

                    psDetalle.executeUpdate();

                    try (ResultSet rs = psDetalle.getGeneratedKeys()) {
                        if (rs.next()) {
                            detalle.setId(rs.getLong(1));
                        }
                    }
                }
            }

            conexion.commit();
            return pedido;

        } catch (SQLException e) {
            if (conexion != null) {
                conexion.rollback();
            }

            throw e;

        } finally {
            if (conexion != null) {
                conexion.setAutoCommit(true);
                conexion.close();
            }
        }
    }

    @Override
    public Pedido leer(Long id) throws SQLException {
        String sql = """
                SELECT id, fecha, estado, total, forma_pago, eliminado, created_at
                FROM pedido
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pedido pedido = mapearPedido(rs);
                    cargarDetalles(pedido, conexion);
                    return pedido;
                }
            }
        }

        return null;
    }

    @Override
    public List<Pedido> listar() throws SQLException {
        String sql = """
                SELECT id, fecha, estado, total, forma_pago, eliminado, created_at
                FROM pedido
                WHERE eliminado = false
                ORDER BY id
                """;

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        }

        return pedidos;
    }

    @Override
    public Pedido actualizar(Pedido pedido) throws SQLException {
        String sql = """
                UPDATE pedido
                SET estado = ?, forma_pago = ?
                WHERE id = ? AND eliminado = false
                """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.getEstado().name());
            ps.setString(2, pedido.getFormaPago().name());
            ps.setLong(3, pedido.getId());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return null;
            }

            return pedido;
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sqlRestaurarStock = """
            UPDATE producto p
            INNER JOIN detalle_pedido dp ON p.id = dp.id_producto
            SET p.stock = p.stock + dp.cantidad,
                p.disponible = true
            WHERE dp.id_pedido = ?
              AND dp.eliminado = false
            """;

        String sqlDetalles = """
            UPDATE detalle_pedido
            SET eliminado = true
            WHERE id_pedido = ?
            """;

        String sqlPedido = """
            UPDATE pedido
            SET eliminado = true
            WHERE id = ?
            """;

        Connection conexion = null;

        try {
            conexion = ConexionDB.getConexion();
            conexion.setAutoCommit(false);

            try (PreparedStatement psRestaurarStock = conexion.prepareStatement(sqlRestaurarStock)) {
                psRestaurarStock.setLong(1, id);
                psRestaurarStock.executeUpdate();
            }

            try (PreparedStatement psDetalles = conexion.prepareStatement(sqlDetalles)) {
                psDetalles.setLong(1, id);
                psDetalles.executeUpdate();
            }

            try (PreparedStatement psPedido = conexion.prepareStatement(sqlPedido)) {
                psPedido.setLong(1, id);
                psPedido.executeUpdate();
            }

            conexion.commit();

        } catch (SQLException e) {
            if (conexion != null) {
                conexion.rollback();
            }

            throw e;

        } finally {
            if (conexion != null) {
                conexion.setAutoCommit(true);
                conexion.close();
            }
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido(
                rs.getLong("id"),
                rs.getDate("fecha").toLocalDate(),
                Estado.valueOf(rs.getString("estado")),
                rs.getDouble("total"),
                FormaPago.valueOf(rs.getString("forma_pago"))
        );

        pedido.setEliminado(rs.getBoolean("eliminado"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            pedido.setCreatedAt(createdAt.toLocalDateTime());
        }

        return pedido;
    }

    private void cargarDetalles(Pedido pedido, Connection conexion) throws SQLException {
        String sql = """
                SELECT 
                    dp.id AS detalle_id,
                    dp.cantidad,
                    dp.subtotal,
                    p.id AS producto_id,
                    p.nombre AS producto_nombre,
                    p.precio,
                    p.descripcion AS producto_descripcion,
                    p.stock,
                    p.imagen,
                    p.disponible
                FROM detalle_pedido dp
                INNER JOIN producto p ON dp.id_producto = p.id
                WHERE dp.id_pedido = ?
                  AND dp.eliminado = false
                ORDER BY dp.id
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, pedido.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Producto producto = new Producto(
                            rs.getLong("producto_id"),
                            rs.getString("producto_nombre"),
                            rs.getDouble("precio"),
                            rs.getString("producto_descripcion"),
                            rs.getInt("stock"),
                            rs.getString("imagen"),
                            rs.getBoolean("disponible")
                    );

                    DetallePedido detalle = new DetallePedido(
                            rs.getLong("detalle_id"),
                            rs.getInt("cantidad"),
                            rs.getDouble("subtotal"),
                            producto
                    );

                    pedido.agregarDetalleExistente(detalle);
                }
            }
        }
    }

    public List<String> listarResumenPedidos() throws SQLException {
        String sql = """
            SELECT 
                p.id,
                p.fecha,
                p.estado,
                p.forma_pago,
                p.total,
                u.nombre,
                u.apellido,
                u.mail
            FROM pedido p
            INNER JOIN usuario u ON p.id_usuario = u.id
            WHERE p.eliminado = false
            ORDER BY p.id
            """;

        List<String> resumenes = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String resumen = "Pedido{" +
                        "id=" + rs.getLong("id") +
                        ", fecha=" + rs.getDate("fecha") +
                        ", estado=" + rs.getString("estado") +
                        ", formaPago=" + rs.getString("forma_pago") +
                        ", total=" + rs.getDouble("total") +
                        ", usuario=" + rs.getString("nombre") + " " + rs.getString("apellido") +
                        ", mail=" + rs.getString("mail") +
                        '}';

                resumenes.add(resumen);
            }
        }

        return resumenes;
    }
}