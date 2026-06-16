package integrador.prog2.entities;

import java.time.LocalDateTime;

public abstract class Base {
    
    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;
    private static long contador = 0;


    public Base() {
        this.id = generadorID();
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    private long generadorID(){
       return contador++;
   }
    
    public long getId() {
        return id;
    }
   

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    @Override
    public String toString() {
        return "Base{" + "id=" + id + ", eliminado=" + eliminado + ", createdAt=" + createdAt + '}';
    }
}

