package integrador.prog2.entities;

public class Validador {
    public static void cadenaVacia(String valor, String nombreAtributo){
    if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + nombreAtributo + "' no puede estar vacio.");
        }
    }
    
    public static void mayorQueCero(double valor, String nombreAtributo) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El campo '" + nombreAtributo + "' debe ser mayor a cero.");
        }
    }
    
    public static void mayorOIgualQueCero(double valor, String nombreAtributo) {
        if (valor < 0) {
            throw new IllegalArgumentException("El campo '" + nombreAtributo + "' debe ser mayor o igual a cero.");
        }
    }
    
    public static void objetoNulo(Object objeto,String nombreObjeto){
    if (objeto == null) {
            throw new IllegalArgumentException("El objeto '" + nombreObjeto + "' no puede ser nulo.");
        }
    }
    
    public static void mayorQueCeroEntero(int valor, String nombreAtributo) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El campo '" + nombreAtributo + "' debe ser mayor a cero.");
        }
    }
}
