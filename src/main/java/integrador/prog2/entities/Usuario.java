package integrador.prog2.entities;

import integrador.prog2.enums.Rol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Usuario extends Base {

    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contraseña;
    private Rol rol;
    private List<Pedido> pedidos = new ArrayList<>();

    // Constructor completo
    public Usuario(String nombre, String apellido, String mail, String celular, String contraseña, Rol rol) {
        super();
        setNombre(nombre);
        setApellido(apellido);
        setMail(mail);
        setCelular(celular);
        setContraseña(contraseña);
        setRol(rol);
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        if (mail == null || mail.trim().isEmpty()) {
            throw new IllegalArgumentException("El mail no puede estar vacío");
        }
        this.mail = mail;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        this.rol = rol;
    }

    public List<Pedido> getPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    public void agregarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        pedidos.add(pedido);
    }

    public void eliminarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        pedidos.remove(pedido);
    }

    @Override
    public String toString() {
        return "Usuario [ID: " + getId() +
                " | Nombre: " + nombre + " " + apellido +
                " | Mail: " + mail +
                " | Celular: " + celular +
                " | Rol: " + rol +
                " | Creado: " + getCreatedAt() + "]";
    }
}

