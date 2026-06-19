package integrador.prog2.service;

import integrador.prog2.dao.UsuarioDAO;
import integrador.prog2.entities.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService implements GenericService<Usuario> {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    public Usuario crear(Usuario usuario) throws SQLException {
        validarUsuario(usuario);

        usuario.setMail(usuario.getMail().trim().toLowerCase());

        if (usuarioDAO.existeMail(usuario.getMail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese mail");
        }

        return usuarioDAO.crear(usuario);
    }

    @Override
    public Usuario leer(Long id) throws SQLException {
        validarId(id);

        Usuario usuario = usuarioDAO.leer(id);

        if (usuario == null) {
            throw new IllegalArgumentException("No existe un usuario activo con id " + id);
        }

        return usuario;
    }

    @Override
    public List<Usuario> listar() throws SQLException {
        return usuarioDAO.listar();
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws SQLException {
        validarUsuario(usuario);
        validarId(usuario.getId());

        Usuario usuarioExistente = usuarioDAO.leer(usuario.getId());

        if (usuarioExistente == null) {
            throw new IllegalArgumentException("No existe un usuario activo con id " + usuario.getId());
        }

        usuario.setMail(usuario.getMail().trim().toLowerCase());

        if (!usuarioExistente.getMail().equalsIgnoreCase(usuario.getMail())
                && usuarioDAO.existeMail(usuario.getMail())) {
            throw new IllegalArgumentException("Ya existe otro usuario con ese mail");
        }

        Usuario actualizado = usuarioDAO.actualizar(usuario);

        if (actualizado == null) {
            throw new IllegalArgumentException("No se pudo actualizar el usuario");
        }

        return actualizado;
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        validarId(id);

        Usuario usuario = usuarioDAO.leer(id);

        if (usuario == null) {
            throw new IllegalArgumentException("No existe un usuario activo con id " + id);
        }

        usuarioDAO.eliminar(id);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }

        if (usuario.getMail() == null || usuario.getMail().trim().isEmpty()) {
            throw new IllegalArgumentException("El mail no puede estar vacío");
        }

        if (!usuario.getMail().contains("@")) {
            throw new IllegalArgumentException("El mail debe tener un formato válido");
        }

        if (usuario.getRol() == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor a cero");
        }
    }
}