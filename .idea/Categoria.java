import java.util.ArrayList;

public class Categoria extends Base {

    private String nombre;
    private String descripcion;
    private List<Producto> productos;

    // constructor de mas
    public Categoria() {
        super();
        this.productos = new ArrayList<>();
    }

    public Categoria(String nombre, String descripcion) {
        super();
        setNombre(nombre);
        setDescripcion(descripcion);
        this.productos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }

        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void agregarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        productos.add(producto);
    }

    public void mostrarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos en la categoría '" + nombre + "'.");
        } else {
            System.out.println("Productos en la categoría '" + nombre + "':");

            for (Producto producto : productos) {
                System.out.println(
                        "- " + producto.getNombre() +
                        " (Precio: " + producto.getPrecio() +
                        ", Stock: " + producto.getStock() + ")"
                );
            }
        }
    }

    @Override
    // podria mostrar los productos o la cnatidad de productos
    public String toString() {
        return "Categoria{" +
                "id=" + getId() +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}