package integrador.prog2.entities;
import java.util.Objects;
import static integrador.prog2.entities.Validador.*;
import integrador.prog2.exception.StockInsuficienteException;
public class Producto extends Base {

    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Categoria categoria;

    public Producto(String nombre, Double precio, String descripcion, int stock, String imagen) {
        super();
        setNombre(nombre);
        setPrecio(precio);
        setDescripcion(descripcion);
        setStock(stock);
        setImagen(imagen);

    }

    public void validarDisponibilidad() {
        if (stock > 0) {
            System.out.println("El producto " + nombre + " esta disponible");
        } else {
            throw new StockInsuficienteException("Stock insuficiente para el producto '" + this.nombre);
        }
    }

    public void reducirStock(int numero) {
        mayorQueCero(numero, "numero");
        int stockActual = stock;
        int nuevoStock = stock - numero;
        if (stockActual == 0) {
            System.out.println("El stock actual es 0. No puede reducir");
        } else {
            if (nuevoStock < 0) {
                System.out.println("No se puede reducir el stock en " + numero + " unidades. Ingresar un numero menor o igual a " + stock);
            } else if (nuevoStock == 0) {
                System.out.println("El producto actual quedo con stock 0");
                setStock(nuevoStock);
            } else {
                System.out.println("El stock actual del prodcuto es: " + nuevoStock);
                setStock(nuevoStock);
            }
        }

    }

    public void aumentarStock(int numero) {
        mayorQueCero(numero, "numero");
        int nuevoStock = stock + numero;
        System.out.println("El producto tiene un nuevo stock de: " + nuevoStock);
        setStock(nuevoStock);

    }

    public boolean validarVenta(int cantidad) {
        if(cantidad<=0){
            throw new StockInsuficienteException("Stock insuficiente para el producto '" + this.nombre + "'. Solicitado: " + cantidad + ", Disponible: " + this.stock);
        }

        int nuevoStock = stock - cantidad;
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("Stock insuficiente para validar venta. Stock actual: " + stock + " cantidad a retirar: " + cantidad);
        }
        System.out.println("La venta fue validada");
        reducirStock(cantidad);
        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getStock() {
        return stock;
    }

    public String getImagen() {
        return imagen;
    }

    public boolean getDisponible() {
        return disponible;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setNombre(String nombre) {
        cadenaVacia(nombre, "nombre producto");
        this.nombre = nombre;
    }

    public void setPrecio(Double precio) {
        if (precio == null || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        this.precio = precio;
    }

    public void setDescripcion(String descripcion) {
        cadenaVacia(descripcion, "descripcion producto");
        this.descripcion = descripcion;
    }

    public void setStock(int stock) {
        // metodo comentado para que se pueda probar: StockInsuficienteException
        //mayorQueCero(stock, "stock");

        if (stock == 0) {
            setDisponible(false);
        } else {
            setDisponible(true);
        }
        this.stock = stock;
    }

    public void setImagen(String imagen) {
        cadenaVacia(imagen, "imagen");
        this.imagen = imagen;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Producto{" + "nombre=" + nombre + ", precio=" + precio + ", descripcion=" + descripcion + ", stock=" + stock + ", imagen=" + imagen + ", disponible=" + disponible + ", categoria=" + (categoria != null ? categoria.getNombre() : "Sin categoria") + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Producto other = (Producto) obj;
        return Objects.equals(this.nombre, other.nombre);
    }

}
