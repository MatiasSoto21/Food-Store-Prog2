/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrador.prog2.entities;

import java.time.LocalDateTime;

/**
 *
 * @author GUILLE
 */
public abstract class Base {
    
    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    // Constructor vacío 
    public Base() {
        this.createdAt = LocalDateTime.now(); 
        this.eliminado = false; 
    }

    // Constructor parametrizado
    public Base(Long id) {
        this();
        this.id = id;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Base{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", createdAt=" + createdAt +
                '}';
    }
}

