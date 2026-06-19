package integrador.prog2.entities;
import integrador.prog2.exception.InsufficientStockException;

public class Producto extends Base {

    private String nombre;
    private double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Categoria categoria;

    public Producto() {
        super();
    }

    public Producto(String nombre, double precio, String descripcion, int stock, String imagen, boolean disponible, Categoria categoria) {
        super();
        setNombre(nombre);
        setPrecio(precio);
        setDescripcion(descripcion);
        setStock(stock);
        setImagen(imagen);
        setDisponible(disponible);
        setCategoria(categoria);
    }

    public Producto(Long id, String nombre, double precio, String descripcion, int stock, String imagen, boolean disponible, Categoria categoria) {
        super(id);
        setNombre(nombre);
        setPrecio(precio);
        setDescripcion(descripcion);
        setStock(stock);
        setImagen(imagen);
        setDisponible(disponible);
        setCategoria(categoria);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
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
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void validarVenta(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }

        if (!disponible) {
            throw new IllegalArgumentException("El producto no está disponible");
        }

        if (cantidad > stock) {
            throw new InsufficientStockException("No hay stock suficiente para " + nombre + ". Solicitado: " + cantidad + ", disponible: " + stock);
        }

        reducirStock(cantidad);
    }

    private void reducirStock(int cantidad) {
        this.stock -= cantidad;

        if (this.stock == 0) {
            this.disponible = false;
            System.out.println("ALERTA: El producto '" + nombre + "' se agotó.");
        }
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + getId() +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", descripcion='" + descripcion + '\'' +
                ", stock=" + stock +
                ", imagen='" + imagen + '\'' +
                ", disponible=" + disponible +
                ", categoria=" + (categoria != null ? categoria.getNombre() : "Sin categoría") +
                '}';
    }
}