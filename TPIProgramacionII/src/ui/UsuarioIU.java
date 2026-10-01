package ui;

import entities.Pedido;
import entities.Usuario;
import enums.Rol;
import exception.OperacionValidaException;
import java.util.List;
import java.util.Scanner;
import services.PedidoServiceImpl;

public class UsuarioIU {
    PedidoServiceImpl usuarioService = new PedidoServiceImpl();
    Scanner scanner = new Scanner(System.in);
    
    public void mostrarMenuProducto() throws Exception{
        
        int opcion = 0;
        
        do{
            System.out.println("\n--- MENÚ USUARIOS ---");
            System.out.println("\n1) Listar");
            System.out.println("\n2) Crear");
            System.out.println("\n3) Editar");
            System.out.println("\n4) Eliminar (baja lógica)");
            System.out.println("\n5) Ver pedidos por usuario");
            System.out.println("\n0) Volver al menu principal");
            System.out.println("\nQue desea realizar?: ");

            try{
                opcion = Integer.parseInt(scanner.nextLine());
            }catch(IllegalArgumentException e){
                System.out.println("Dato inválido " + e.getMessage());
            }
            
            switch(opcion){
                case 1 -> listarUsuarios();
                case 2 -> crearUsuarios();
                case 3 -> editarUsuario();
                case 4 -> eliminarUsuario();
                case 5 -> obtenerPedidosUsuario();
                case 0 -> System.out.println("\n| REGRESANDO AL MENU PRINCIPAL |\n" );
                default -> System.out.println("| Opcion inválida |");
            }
            
        }while( opcion != 0);
    }
    public void listarUsuarios(){
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        if(usuarios.isEmpty()){
            System.out.println("\n| No hay usuarios creados hasta el momento |\n" );
        }else{
            System.out.println("\n| LISTAR USUARIOS |\n" );
            for(Usuario u : usuarios){
                usuarioService.mostrarUsuario(u);
            }
        }     
    }
    public void crearUsuarios() throws Exception{
        listarUsuarios();
        System.out.println("\n| CREAR USUARIOS |\n" );
        
        System.out.print("Nombre del usuario: ");
        String nombre = scanner.nextLine();          
        System.out.print("Apellido del usuario: ");
        String apellido = scanner.nextLine();
        System.out.print("Email del usuario: ");
        String email = scanner.nextLine();
        System.out.print("Telefono del usuario: ");
        String celular = scanner.nextLine();
        System.out.print("Contraseña del usuario: ");
        String contraseña = scanner.nextLine();
        System.out.print("Eres admin?(si/no): ");
        Rol rol = (scanner.nextLine().toLowerCase().equals("si") ? Rol.ADMIN : Rol.USUARIO);
        
        Usuario usuarioNuevo = new Usuario(nombre, apellido, email, celular, contraseña, rol );
        System.out.print("\n| Desea guardar el usuario?(si/no): ");
        String ans = scanner.nextLine();
        switch(ans){
                case "si": 
                    try{
                        usuarioService.saveUsuario(usuarioNuevo, false);
                    }catch(Exception e){
                        System.out.print(e.getMessage());
                    }
                    break;
                case "no":
                    System.out.print("\n| No se ha guardado el usuario |");
                    break;
                default :
                    System.out.println("\n| Opcion inválida | Usuario no guardado |");
                   
            }
        
        
    }
    public void editarUsuario() throws Exception{
        listarUsuarios();
        Usuario usuarioEncontrado = null;
        if(!usuarioService.listarUsuarios().isEmpty()){
            System.out.println("\n| EDITAR USUARIOS |\n" );
        System.out.print("| Dime el id del usuario a actualizar: ");
        long id = 0l;
        String ans = scanner.nextLine();
        try{ 
             id = Long.parseLong(ans.trim());
        }catch(NumberFormatException e){
            System.out.print("| Valor ingresado no válido | " + e.getMessage());
        }
        
        try{
          usuarioEncontrado = usuarioService.findUsuario(id);
        }catch(Exception e){
            System.out.print(e.getMessage());
        }
        
        if(usuarioEncontrado != null){
            usuarioService.mostrarUsuario(usuarioEncontrado);
            System.out.println("\n| EDITAR USUARIO '" + usuarioEncontrado.getNombre() + "' |\n" );
            System.out.print("Nombre del usuario: ");
            String nombre = scanner.nextLine();          
            System.out.print("Apellido del usuario: ");
            String apellido = scanner.nextLine();
            System.out.print("Email del usuario: ");
            String email = scanner.nextLine();
            System.out.print("Telefono del usuario: ");
            String celular = scanner.nextLine();
            System.out.print("Contraseña del usuario: ");
            String contraseña = scanner.nextLine();
            System.out.print("Eres admin?(si/no): ");
            Rol rol = (scanner.nextLine().toLowerCase().equals("si") ? Rol.ADMIN : Rol.USUARIO);
            
            Usuario nuevoUsuario = new Usuario(nombre, apellido, email, celular, contraseña, rol, usuarioEncontrado.getId(), usuarioEncontrado.isEliminado(), usuarioEncontrado.getCreatedAt() );
        
            System.out.print("\n| Desea guardar el usuario?(si/no): ");
            ans = scanner.nextLine();
            switch(ans){
                case "si":
                    try{
                        boolean valido = usuarioService.validarUsuario(nuevoUsuario, true);
                        if(valido){
                        usuarioService.eliminarUsuario(usuarioEncontrado.getId());
                        }
                    }catch(OperacionValidaException e){
                        System.out.print(e.getMessage());
                    }catch(Exception e){
                        //Nada para no repetir validaciones
                    } 
                    try{
                        usuarioService.saveUsuario(nuevoUsuario, true);
                    }catch(Exception e){
                        System.out.print(e.getMessage());
                    } 
                    break;
                case "no":
                    System.out.print("\n| No se ha actualizado el usuario |");
                    break;
                default :
                    System.out.print("\n| Opcion inválida | Usuario no actualizado |");
                   
            }
        
        }else{
            System.out.print("| Usuario no encontrado o no existente |");
        }
        }
        
    }
    public void eliminarUsuario() throws Exception{
        listarUsuarios();
        Usuario usuarioEncontrado = null;
        if(!usuarioService.listarUsuarios().isEmpty()){
           System.out.println("\n| ELIMINAR USUARIOS |\n" );
           System.out.print("| Dime el id del usuario a eliminar: ");
        long id = 0l;
        String ans = scanner.nextLine();
        try{
             id = Long.parseLong(ans.trim());
        }catch(NumberFormatException e){
            System.out.print("| Valor ingresado no válido | + " + e.getMessage());
        }
        try{
             usuarioEncontrado = usuarioService.findUsuario(id);
        }catch(Exception e){
             System.out.print(e.getMessage());
        }
       
        if(usuarioEncontrado != null){
            usuarioService.mostrarUsuario(usuarioEncontrado);
            System.out.print("\n| Desea eliminar el usuario?(si/no): ");
            ans = scanner.nextLine();
            switch(ans){
                case "si": 
                    try{
                        usuarioService.eliminarUsuario(usuarioEncontrado.getId());
                    }catch(Exception e){
                        System.out.print(e.getMessage());
                    }
                    
                    break;
                case "no":
                    System.out.print("\n| No se ha eliminado el usuario ");
                    break;
                default :
                    System.out.print("\n| Opcion inválida | Usuario no eliminado |");
                   
            }
        }else{
            System.out.print("| Usuario no encontrado o no existente |");
        } 
        }
    }
    public void obtenerPedidosUsuario(){
        listarUsuarios();
        Usuario usuarioEncontrado = null;
        List<Pedido> pedidos = null;
        if(!usuarioService.listarUsuarios().isEmpty()){
            System.out.println("\n| VER PEDIDOS |\n" );
        System.out.print("| Dime el id del usuario a buscar sus pedidos: ");
        long id = 0l;
        String ans = scanner.nextLine();
        try{
             id = Long.parseLong(ans.trim());
        }catch(NumberFormatException e){
            System.out.print("| Valor ingresado no válido |" + e.getMessage());
        }
        
        try{
           usuarioEncontrado = usuarioService.findUsuario(id); 
        }catch(Exception e){
            System.out.print(e.getMessage());
        }
        
        if(usuarioEncontrado != null){
            try{
                pedidos = usuarioService.listarPedidosPorUsuario(usuarioEncontrado.getId());
            }catch(Exception e){
                System.out.print(e.getMessage());
            }
            
            if(pedidos != null && !pedidos.isEmpty()){
                for(Pedido p : pedidos){
                usuarioService.mostrarPedido(p);
                }
            }else{
                System.out.print("\n| Aun no se han cargado pedidos en el usuario |");
            }
            
                
        }else{
            System.out.print("| Usuario no encontrado o no existente |");
        }
        
    }
            
    }        
            
            
}
