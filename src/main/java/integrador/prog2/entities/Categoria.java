package integrador.prog2.entities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import static integrador.prog2.entities.Validador.*;
public class Categoria extends Base {

    private String nombre;
    private String descripcion;
    private List<Producto> productos = new ArrayList<>();

    public Categoria(String nombre, String descripcion) {
        super();
        setNombre(nombre);
        setDescripcion(descripcion);
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setNombre(String nombre) {
        cadenaVacia(nombre,"nombre");
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        cadenaVacia(descripcion,"descripcion");
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        String productosTexto = "";
        for (Producto p : productos) {
            productosTexto += p.getNombre() + " ";
        }
        return "Categoria{" + "nombre=" + nombre + ", descripcion=" + descripcion + ", productos=" + productosTexto + '}';
    }
    
    // Producto
    //Mostrar
    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    //Agregar
    public void agregarProducto(Producto producto) {
        objetoNulo(producto,"prodcuto");
         //verifico que no esten en la lista
        if (!productos.contains(producto)) {
            productos.add(producto);
            producto.setCategoria(this);
        }else{
            System.out.println("El producto ya se encuentra en la lista, no se volvera a agregar");
        }
        
    }

    //Eliminar
    public void eliminarProducto(Producto producto) {
        objetoNulo(producto,"prodcuto");
        productos.remove(producto);
        producto.setCategoria(null);
        
    }

    // equals y hash

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.nombre);
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
        final Categoria other = (Categoria) obj;
        return Objects.equals(this.nombre, other.nombre);
    }
}