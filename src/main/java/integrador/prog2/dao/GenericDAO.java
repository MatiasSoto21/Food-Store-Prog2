package integrador.prog2.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {

    T crear(T entidad) throws SQLException;

    T leer(Long id) throws SQLException;

    List<T> listar() throws SQLException;

    T actualizar(T entidad) throws SQLException;

    void eliminar(Long id) throws SQLException;
}