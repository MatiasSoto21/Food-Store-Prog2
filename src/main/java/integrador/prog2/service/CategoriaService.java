package integrador.prog2.service;

import integrador.prog2.dao.*;
import integrador.prog2.entities.Categoria;

import java.sql.SQLException;
import java.util.List;

public class CategoriaService implements GenericService<Categoria> {

    private final CategoriaDAO categoriaDAO;
    private final ProductoDAO productoDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
        this.productoDAO = new ProductoDAO();
    }

    @Override
    public Categoria crear(Categoria categoria) throws SQLException {
        validarCategoria(categoria);

        String nombreNormalizado = categoria.getNombre().trim();
        categoria.setNombre(nombreNormalizado);

        if (categoriaDAO.existeNombre(nombreNormalizado)) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        return categoriaDAO.crear(categoria);
    }

    @Override
    public Categoria leer(Long id) throws SQLException {
        validarId(id);

        Categoria categoria = categoriaDAO.leer(id);

        if (categoria == null) {
            throw new IllegalArgumentException("No existe una categoría activa con id " + id);
        }

        return categoria;
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        return categoriaDAO.listar();
    }

    @Override
    public Categoria actualizar(Categoria categoria) throws SQLException {
        validarCategoria(categoria);
        validarId(categoria.getId());

        Categoria categoriaExistente = categoriaDAO.leer(categoria.getId());

        if (categoriaExistente == null) {
            throw new IllegalArgumentException("No existe una categoría activa con id " + categoria.getId());
        }

        String nombreNormalizado = categoria.getNombre().trim();
        categoria.setNombre(nombreNormalizado);

        if (!categoriaExistente.getNombre().equalsIgnoreCase(nombreNormalizado)
                && categoriaDAO.existeNombre(nombreNormalizado)) {
            throw new IllegalArgumentException("Ya existe otra categoría con ese nombre");
        }

        Categoria actualizada = categoriaDAO.actualizar(categoria);

        if (actualizada == null) {
            throw new IllegalArgumentException("No se pudo actualizar la categoría");
        }

        return actualizada;
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        validarId(id);

        Categoria categoria = categoriaDAO.leer(id);

        if (categoria == null) {
            throw new IllegalArgumentException("No existe una categoría activa con id " + id);
        }

        if (productoDAO.existenProductosActivosPorCategoria(id)) {
            throw new IllegalArgumentException("No se puede eliminar la categoría porque tiene productos activos asociados");
        }

        categoriaDAO.eliminar(id);
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor a cero");
        }
    }
}