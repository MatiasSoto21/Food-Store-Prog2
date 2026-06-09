package integrador.prog2.entities;
import integrador.prog2.exception.InsufficientStockException;

public class Producto extends Base {

    private String nombre;
    private double precio;
    private int stock;
    private String imagen;

    public Producto(String nombre, double precio, int stock, String imagen) {
        super();
        setNombre(nombre);
        setPrecio(precio);
        setStock(stock);
        setImagen(imagen);
    }




    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isDisponible() {
        return stock > 0;
    }

    public void validarVenta(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }

        if (cantidad > stock) {
            throw new InsufficientStockException("No hay stock suficiente para " + nombre + ". Solicitado: " + cantidad + ", disponible: " + stock);
        }

        reducirStock(cantidad);
    }

    private void reducirStock(int cantidad) {
        this.stock -= cantidad;

        if (this.stock == 0) {
            System.out.println("ALERTA: El producto '" + nombre + "' se agotó.");
        }
    }

    @Override
    public String toString() {
        return "Producto{" + "id=" + getId() + ", nombre='" + nombre + '\'' + ", precio=" + precio + ", stock=" + stock + ", disponible=" + isDisponible() + ", imagen=" + (imagen != null ? imagen : "-") + '}';
    }
}