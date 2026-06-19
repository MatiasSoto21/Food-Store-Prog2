package integrador.prog2.service;

import integrador.prog2.dao.PedidoDAO;
import integrador.prog2.entities.DetallePedido;
import integrador.prog2.entities.Pedido;
import integrador.prog2.entities.Producto;
import integrador.prog2.entities.Usuario;

import java.sql.SQLException;
import java.util.List;

public class PedidoService implements GenericService<Pedido> {

    private final PedidoDAO pedidoDAO;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
        this.productoService = new ProductoService();
        this.usuarioService = new UsuarioService();
    }

    @Override
    public Pedido crear(Pedido pedido) throws SQLException {
        throw new UnsupportedOperationException("Para crear un pedido se debe usar crearPedido(Long idUsuario, Pedido pedido)");
    }

    public Pedido crearPedido(Long idUsuario, Pedido pedido) throws SQLException {
        validarId(idUsuario);
        validarPedido(pedido);

        Usuario usuario = usuarioService.leer(idUsuario);

        if (usuario == null) {
            throw new IllegalArgumentException("No existe un usuario activo con id " + idUsuario);
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto productoActual = productoService.leer(detalle.getProducto().getId());

            if (detalle.getCantidad() > productoActual.getStock()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para " + productoActual.getNombre() +
                                ". Solicitado: " + detalle.getCantidad() +
                                ", disponible: " + productoActual.getStock()
                );
            }

            if (!productoActual.isDisponible()) {
                throw new IllegalArgumentException("El producto " + productoActual.getNombre() + " no está disponible");
            }
        }

        pedido.calcularTotal();

        return pedidoDAO.crearPedido(idUsuario, pedido);
    }

    @Override
    public Pedido leer(Long id) throws SQLException {
        validarId(id);

        Pedido pedido = pedidoDAO.leer(id);

        if (pedido == null) {
            throw new IllegalArgumentException("No existe un pedido activo con id " + id);
        }

        return pedido;
    }

    @Override
    public List<Pedido> listar() throws SQLException {
        return pedidoDAO.listar();
    }

    @Override
    public Pedido actualizar(Pedido pedido) throws SQLException {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        validarId(pedido.getId());

        if (pedido.getEstado() == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        if (pedido.getFormaPago() == null) {
            throw new IllegalArgumentException("La forma de pago no puede ser nula");
        }

        Pedido pedidoExistente = pedidoDAO.leer(pedido.getId());

        if (pedidoExistente == null) {
            throw new IllegalArgumentException("No existe un pedido activo con id " + pedido.getId());
        }

        Pedido actualizado = pedidoDAO.actualizar(pedido);

        if (actualizado == null) {
            throw new IllegalArgumentException("No se pudo actualizar el pedido");
        }

        return actualizado;
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        validarId(id);

        Pedido pedido = pedidoDAO.leer(id);

        if (pedido == null) {
            throw new IllegalArgumentException("No existe un pedido activo con id " + id);
        }

        pedidoDAO.eliminar(id);
    }

    private void validarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        if (pedido.getFormaPago() == null) {
            throw new IllegalArgumentException("La forma de pago no puede ser nula");
        }

        if (pedido.getEstado() == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        if (pedido.getFecha() == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un detalle");
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle == null) {
                throw new IllegalArgumentException("El detalle no puede ser nulo");
            }

            if (detalle.getProducto() == null || detalle.getProducto().getId() == null) {
                throw new IllegalArgumentException("Cada detalle debe tener un producto válido");
            }

            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad del detalle debe ser mayor a cero");
            }

            if (detalle.getSubtotal() < 0) {
                throw new IllegalArgumentException("El subtotal no puede ser negativo");
            }
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor a cero");
        }
    }

    public List<String> listarResumenPedidos() throws SQLException {
        return pedidoDAO.listarResumenPedidos();
    }
}
