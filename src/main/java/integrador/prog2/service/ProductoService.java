package integrador.prog2.service;

import integrador.prog2.dao.ProductoDAO;
import integrador.prog2.entities.Producto;

import java.sql.SQLException;
import java.util.List;

public class ProductoService implements GenericService<Producto> {

    private final ProductoDAO productoDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDAO();
    }

    @Override
    public Producto crear(Producto producto) throws SQLException {
        validarProducto(producto);

        if (!productoDAO.existeCategoria(producto.getCategoria().getId())) {
            throw new IllegalArgumentException("No existe una categoría activa con id " + producto.getCategoria().getId());
        }

        producto.setNombre(producto.getNombre().trim());

        return productoDAO.crear(producto);
    }

    @Override
    public Producto leer(Long id) throws SQLException {
        validarId(id);

        Producto producto = productoDAO.leer(id);

        if (producto == null) {
            throw new IllegalArgumentException("No existe un producto activo con id " + id);
        }

        return producto;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        return productoDAO.listar();
    }

    public List<Producto> listarPorCategoria(Long idCategoria) throws SQLException {
        validarId(idCategoria);

        if (!productoDAO.existeCategoria(idCategoria)) {
            throw new IllegalArgumentException("No existe una categoría activa con id " + idCategoria);
        }

        return productoDAO.listarPorCategoria(idCategoria);
    }

    @Override
    public Producto actualizar(Producto producto) throws SQLException {
        validarProducto(producto);
        validarId(producto.getId());

        Producto productoExistente = productoDAO.leer(producto.getId());

        if (productoExistente == null) {
            throw new IllegalArgumentException("No existe un producto activo con id " + producto.getId());
        }

        if (!productoDAO.existeCategoria(producto.getCategoria().getId())) {
            throw new IllegalArgumentException("No existe una categoría activa con id " + producto.getCategoria().getId());
        }

        producto.setNombre(producto.getNombre().trim());

        Producto actualizado = productoDAO.actualizar(producto);

        if (actualizado == null) {
            throw new IllegalArgumentException("No se pudo actualizar el producto");
        }

        return actualizado;
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        validarId(id);

        Producto producto = productoDAO.leer(id);

        if (producto == null) {
            throw new IllegalArgumentException("No existe un producto activo con id " + id);
        }

        productoDAO.eliminar(id);
    }

    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }

        if (producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("El producto debe tener una categoría válida");
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor a cero");
        }
    }
}