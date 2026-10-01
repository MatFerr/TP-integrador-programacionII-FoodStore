package repository;

import entities.Usuario;
import exception.OperacionValidaException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    
    private final static List<Usuario> usuarios = new ArrayList<>();
    private static Long idGenerador = 1L; 
    
    public void save(Usuario usuario, boolean update){
       if(!update){
          usuario.setId(idGenerador);
          idGenerador ++; 
       }  
        usuarios.add(usuario);
        throw new OperacionValidaException("\n| Usuario guardado correctamente |");
    }
    public List<Usuario> listarActivos(){
        List<Usuario> usuariosActivos = new ArrayList<>();
        for(Usuario u : usuarios){
            if(!u.isEliminado()){
                usuariosActivos.add(u);
            }
        }
        return usuariosActivos;
    }
    public Usuario findById(Long id){
        for(Usuario u : usuarios){
            if(u.getId().equals(id) && !u.isEliminado()){
                return u;
            }
        }
        return null;
    }
    public void eliminarLogico(Long id){
        for(Usuario u : usuarios){
            if(u.getId().equals(id) && !u.isEliminado()){
                u.setEliminado(true);
                throw new OperacionValidaException("\n| Usuario eliminado correctamente |");
            }
        }
    }
    public boolean existeEmail(String email, Usuario usuarioExcluido){
        for(Usuario u : usuarios){
            if(usuarioExcluido != null && usuarioExcluido.getId() != null){
                if(u.getEmail().toLowerCase().equals(email.toLowerCase()) && !u.isEliminado() && !u.getId().equals(usuarioExcluido.getId())){
                return true;
                }
            }else{
                   if(u.getEmail().toLowerCase().equals(email.toLowerCase()) && !u.isEliminado()){
                return true;
            } 
               
            }
            
        }
        return false;
    }
    

}
