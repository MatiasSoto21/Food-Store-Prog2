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
        throw new UnsupportedOperationException("Para crear un producto se debe indicar una categoría");
    }

    public Producto crear(Producto producto, Long idCategoria) throws SQLException {
        validarProducto(producto);
        validarCategoriaSiExiste(idCategoria);

        producto.setNombre(producto.getNombre().trim());

        return productoDAO.crear(producto, idCategoria);
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

    public List<String> listarResumenProductos() throws SQLException {
        return productoDAO.listarResumenProductos();
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

        return actualizar(producto, productoDAO.obtenerIdCategoria(producto.getId()));
    }

    public Producto actualizar(Producto producto, Long idCategoria) throws SQLException {
        validarProducto(producto);
        validarId(producto.getId());
        validarCategoriaSiExiste(idCategoria);

        Producto productoExistente = productoDAO.leer(producto.getId());

        if (productoExistente == null) {
            throw new IllegalArgumentException("No existe un producto activo con id " + producto.getId());
        }

        producto.setNombre(producto.getNombre().trim());

        Producto actualizado = productoDAO.actualizar(producto, idCategoria);

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

    public Long obtenerIdCategoria(Long idProducto) throws SQLException {
        validarId(idProducto);
        leer(idProducto);

        return productoDAO.obtenerIdCategoria(idProducto);
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
    }

    private void validarCategoriaSiExiste(Long idCategoria) throws SQLException {
        validarId(idCategoria);

        if (!productoDAO.existeCategoria(idCategoria)) {
            throw new IllegalArgumentException("No existe una categoría activa con id " + idCategoria);
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor a cero");
        }
    }
}