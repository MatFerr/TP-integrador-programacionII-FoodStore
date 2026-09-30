package entities;

import enums.Estado;
import enums.Rol;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends Base{
    private String nombre;
    private String apellido;
    private String email;
    private String celular;
    private String contraseña;
    private List<Pedido> pedidos = new ArrayList<>();
    private Rol rol;

    public Usuario(String nombre, String apellido, String email, String celular, String contraseña, Rol rol, Long id, boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.celular = celular;
        this.contraseña = contraseña;
        this.rol = rol;
    }
    public Usuario(){}

    public Usuario(String nombre, String apellido, String email, String celular, String contraseña, Rol rol) {
        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.celular = celular;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pedido> getPedidos() {   
        return this.pedidos;
    }
    public void addPedido(Pedido pedido) {
    if (this.pedidos == null) {
        this.pedidos = new ArrayList<>();
    }
    this.pedidos.add(pedido);
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + super.getId() + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + ", celular=" + celular + ", contrase\u00f1a=" + contraseña + ", rol=" + rol + ", eliminado=" + super.isEliminado() + ", Fecha Creacion=" + super.getCreatedAt() + '}';
    }
    
    
    
    
    
}
