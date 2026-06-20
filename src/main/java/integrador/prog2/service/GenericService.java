package integrador.prog2.service;

import java.sql.SQLException;
import java.util.List;

public interface GenericService<T> {

    T crear(T entidad) throws SQLException;

    T leer(Long id) throws SQLException;

    List<T> listar() throws SQLException;

    T actualizar(T entidad) throws SQLException;

    void eliminar(Long id) throws SQLException;
}