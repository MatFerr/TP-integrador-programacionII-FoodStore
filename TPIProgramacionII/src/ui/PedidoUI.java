package ui;

import entities.Categoria;
import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import services.CategoriaServiceImpl;
import services.PedidoServiceImpl;
import services.ProductoServiceImpl;

public class PedidoUI {
   ProductoServiceImpl servicePr = new ProductoServiceImpl();
   PedidoServiceImpl servicePe = new PedidoServiceImpl();
   CategoriaServiceImpl serviceC = new CategoriaServiceImpl();
   Scanner scanner = new Scanner(System.in);
   
   public void mostrarMenuProducto() throws Exception{
        
        int opcion = 0;
        
        do{
            System.out.println("\n--- MENÚ PEDIDOS ---");
            System.out.println("\n1) Listar Pedidos");
            System.out.println("\n2) Crear Pedido( NECESITA CONFIRMACION )");
            System.out.println("\n3) Confirmar Pedido");
            System.out.println("\n4) Eliminar Pedido");
            System.out.println("\n5) Mostrar detalles de Pedido");
            System.out.println("\n6) Añadir pedido de producto");
            System.out.println("\n7) Eliminar pedido de producto");
            System.out.println("\n0) Volver al menu principal");
            System.out.println("\nQue desea realizar?: ");        

            try{
                opcion = Integer.parseInt(scanner.nextLine());
            }catch(IllegalArgumentException e){
                System.out.println("| Dato inválido " + e.getMessage());
            }
            
            switch(opcion){
                case 1 -> listarPedidos();
                case 2 -> flujoCrearPedidoMemoria();
                case 3 -> confirmarPedido();
                case 4 -> eliminarPedido();
                case 5 -> mostrarDetallePedido();
                case 6 -> añadirDetallePedido();
                case 7 -> eliminarDetallePedido();
                case 0 -> System.out.println("\n| REGRESANDO AL MENU PRINCIPAL |\n" );
                default -> System.out.println("| Opcion inválida |");
            }
            
        }while( opcion != 0);
    } 
   
   public void listarPedidos(){
       List<Usuario> usuarios = null;
       List<Pedido> pedidos = null;
       try{
           usuarios = servicePe.listarUsuarios();
       }catch(Exception e){
           System.out.println(e.getMessage());
       }
       System.out.println("\n| LISTAR PEDIDOS |"); 
       if(usuarios == null ||  usuarios.isEmpty()){
           System.out.println("\n| No hay usuarios creados aun |"); 
       }else{
           for(Usuario u : usuarios){
               try{
                   pedidos = servicePe.listarPedidosPorUsuario(u.getId());
               }catch(Exception e){
                   System.out.println(e.getMessage());
               }
               
           System.out.println("\n| USUARIO '" + u.getNombre() + "' | ID: " + u.getId() + " |");   
           if(pedidos != null && !pedidos.isEmpty()){
               System.out.println("\n| PEDIDOS VIGENTES |");
               for(Pedido p : pedidos){
                   servicePe.mostrarPedido(p);
               }
           }else{
               System.out.println("\n| No hay pedidos guardados en el usuario |"); 
           }
       }
       }
       
   }
   private void flujoCrearPedidoMemoria() throws Exception {
       List<DetallePedido> detalles = new ArrayList<>();
       List<Usuario> usuarios = null;
       Usuario usuario = null;
       List<Pedido> pedidos = null;
       List<Categoria> categorias = null;
       List<Producto> productosCategoria = null;
       Categoria categoria = null;
       FormaPago pagoCliente = null;
       
       
       try{
           usuarios = servicePe.listarUsuarios();
       }catch(Exception e){
           System.out.println(e.getMessage());
       }
       if(usuarios != null && !usuarios.isEmpty()){
           for(Usuario u : usuarios){
                System.out.println("| USUARIO '" + u.getNombre() + "' | ID: " + u.getId() + " |");
            }
            System.out.println("\n| Ingrese ID del usuario: ");
            Long usuarioId = 0l;
            try{
               usuarioId = Long.parseLong(scanner.nextLine());
            }catch(NumberFormatException e){
                System.out.println("| Formato ingresado no válido |");
            }

            try{
                usuario = servicePe.findUsuario(usuarioId);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
             
            if(usuario != null){
                try{
                   pedidos = servicePe.listarPedidosPorUsuario(usuarioId); 
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                System.out.println("\n| PEDIDOS DEL USUARIO '" + usuario.getNombre() + "' |");  
               if(pedidos == null || pedidos.isEmpty()){
                   System.out.println("\n| No hay pedidos guardados en el usuario |");
                }else{
                  for(Pedido p : pedidos){
                   servicePe.mostrarPedido(p);
                   }       
                }
               System.out.println("\n| CATEGORIAS |");
               try{
                  categorias = serviceC.findAll(); 
               }catch(Exception e){
                   System.out.println(e.getMessage());
               }
               if(categorias != null && !categorias.isEmpty()){
                   for(Categoria c : categorias){
                   serviceC.mostrarCategoria(c);
               }
               System.out.println("\n| Ingrese id o nombre de la categoria a filtrar: ");
               
                String ans;
                ans = scanner.nextLine();
                try{
                    categoria = serviceC.obtenerCategoriaPorNombreId(ans, categorias);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                if(categoria != null){
                    System.out.println("| PRODUCTOS DE LA CATEGORIA '" + categoria.getNombre() + "' "); 
                    try{
                        productosCategoria = servicePr.listarProductosPorCategoria(categoria.getId());
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    //Validacion sin sobreescribir equals();
                    if(productosCategoria != null && !productosCategoria.isEmpty()){

                    for(Producto p : productosCategoria){
                        servicePr.mostrarProducto(p);
                    }
                    
                    System.out.println("\n| CREAR DETALLE PEDIDO |");

                    String respuesta = "";

                    
                    while(!respuesta.equalsIgnoreCase("salir")){
                      Producto productoEncontrado = null;
                      Long productoId = -1l;
                      System.out.println("\n| Dime el id del producto a agregar('salir' para terminar de agregar): ");
                     respuesta = scanner.nextLine().trim();
                     
                    if (!respuesta.equalsIgnoreCase("salir")) {
                     try {
                         productoId = Long.parseLong(respuesta);
                     } catch (NumberFormatException e) {
                        System.out.println("Formato ingresado no válido");
                     }
                     try{
                         productoEncontrado = servicePr.findById(productoId);
                        
                     }catch(Exception e){
                         System.out.println(e.getMessage());
                     }
                     
                     
                     boolean valido = false;
                     for(Producto p : productosCategoria){
                         if(productoEncontrado != null && p.getId().equals(productoEncontrado.getId())){
                            valido = true;
                        }
                     }
                     
                    
                    if(valido){
                        DetallePedido detalleExistente = null;
                        for(DetallePedido dt : detalles){
                            if(dt.getProducto().getId().equals(productoEncontrado.getId())){
                                detalleExistente = dt;
                            }
                        }
                         
                        
                        if(detalleExistente != null){
                             System.out.println("| Ya existe un pedido del producto solicitado |");
                        }else{
                            int cantidad = 0;
                            System.out.println("| PRODUCTO '" + productoEncontrado.getNombre() + "' |");
                             servicePr.mostrarProducto(productoEncontrado);
                            System.out.println("\nDime la cantidad a agregar: ");
                            try{
                                cantidad = Integer.parseInt(scanner.nextLine());
                             }catch(NumberFormatException e){
                                 System.out.println("| Formato inválido | " + e.getMessage());
                             }
                     
                            if(cantidad > 0){
                        
                                //ACLARACION: No se valida si la cantidad es mayor al stock en memoria, esto no se hace ya que
                                //esta validacion y posterior mensaje de error, lo hace el DAO en el metodo que descuenta
                                 //el stock. Se dejjo asi para que se pueda ver la funcionalidad del metodo, con el rollback
                        
                                double subtotal = productoEncontrado.getPrecio() * cantidad;
                                detalles.add(new DetallePedido(cantidad, subtotal, productoEncontrado));
                    }else{
                        System.out.println("| Cantidad no válida | ");
                    }
                        }
                    }else{
                        System.out.println("| Producto no válido |");
                    }
                  }
                 }
                     //Si no quiere agregar mas detalles
                     System.out.println("\n| FORMAS DE PAGO |");
                     for(FormaPago fp : FormaPago.values()){
                         System.out.println("\n| '" + fp.name() + "' |");
                     }
                     System.out.print("\n| Como deseas pagar: ");
                     ans = scanner.nextLine();
                     if(servicePe.metodoPagoVálido(ans)){
                          pagoCliente = FormaPago.valueOf(ans.toUpperCase().trim());
                          Pedido pedidoProvisional = new Pedido();
                            pedidoProvisional.setDetalles(detalles);
                            pedidoProvisional.setFormaPago(pagoCliente);
                            pedidoProvisional.calcularTotal();
                     
                            System.out.println("\n==INFORMACION DEL PEDIDO ==");
                            for(DetallePedido dt : pedidoProvisional.getDetalles()){
                                System.out.println("|Producto solicitado: " + dt.getProducto().getNombre());
                                System.out.println("|Subtotal del Producto solicitado: " + dt.getSubtotal());
                                System.out.println("|Cantidad del Producto solicitado: " + dt.getCantidad());
                            }
                            System.out.println("\n==PEDIDO ==");
                            System.out.println("Total a pagar: " + pedidoProvisional.getTotal());
                            System.out.println("Forma de pago: " + pedidoProvisional.getFormaPago().name());
                     
                            System.out.println("\n| Desea crear el pedido?(si/no): ");
                            ans = scanner.nextLine();
                        switch(ans.toLowerCase()){
                            case "si":
                                try{
                                    servicePe.crearPedido(usuario, detalles, pagoCliente);
                                }catch(Exception e){
                                     System.out.println(e.getMessage());
                                }     
                            break;
                            case "no":
                                System.out.println("\n| El pedido no se ha creado |");
                                break;
                            default:
                                System.out.println("\n| Opcion no válida |");
                                System.out.println("\n| El pedido no se ha creado |");
                        }
                     }else{
                         System.out.println("Valor ingresado no válido");
                     }
                     
                     
                    }else{
                        System.out.println("| No hay productos asociados a esta categoria |");
                    }
                }else{
                    System.out.print("| Categoria no válida |");
                }
               }else{
                   
               }
               
               
            }else{
                System.out.println("\n| Usuario no válido |");        
            }
       }else{
           System.out.println("\n| No hay usuarios creados aun |");
       }      
           
 
   }
   
   public void confirmarPedido(){
       List<Pedido> pedidosPendientes = new ArrayList<>();
       Long pedidoId = -1l;
       List<Usuario> usuarios = servicePe.listarUsuarios();
       Usuario usuario = null;
       if(!usuarios.isEmpty()){
           for(Usuario u : usuarios){
             System.out.println("| USUARIO '" + u.getNombre() + "' | ID: " + u.getId() + " |");
            }
       System.out.print("Ingrese ID del usuario: ");
            Long usuarioId = 0l;
            try{
               usuarioId = Long.valueOf(scanner.nextLine());
            }catch(NumberFormatException e){
                System.out.println("Formato ingresado no válido");
            }
            try{
                usuario = servicePe.findUsuario(usuarioId);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            
            if(usuario != null){
                for(Pedido p : servicePe.listarPedidosPorUsuario(usuario.getId())){
                    if(p.getEstado() == Estado.PENDIENTE){
                        pedidosPendientes.add(p);
                    }
                }
                if(!pedidosPendientes.isEmpty()){
                    System.out.println("\n| PEDIDOS PENDIENTES DEL USUARIO '" + usuario.getNombre() + "' |");
                    for(Pedido p : pedidosPendientes){
                        servicePe.mostrarPedido(p);
                    }
                    
                    System.out.println("\n| Ingrese el id del pedido a confirmar: |");
                    try{
                        pedidoId = Long.parseLong(scanner.nextLine().trim());
                    }catch(NumberFormatException e){
                        System.out.println("| Formato ingresado no válido | " + e.getMessage());
                    }
                    Pedido pedidoEncontrado = servicePe.buscarPedidoMemoria(pedidoId);
                    if(pedidoEncontrado != null && pedidoEncontrado.getEstado() == Estado.PENDIENTE){
                        servicePe.mostrarPedido(pedidoEncontrado);
                        System.out.println("\n| Desea cambiar la forma de pago del pedido?(si/no): |");
                        
                        String ans = scanner.nextLine();
                        
                        if(!ans.toLowerCase().equals("si") && !ans.toLowerCase().equals("no")){
                                System.out.println("| Opcion no válida |");
                            }else{
                                if(ans.toLowerCase().equals("si")){
                                  System.out.println("\n| FORMAS DE PAGO |");
                                    for(FormaPago fp : FormaPago.values()){
                                        System.out.println("\n| '" + fp.name() + "' |");
                                    }
                                    System.out.print("\n| Como deseas pagar: ");
                                    ans = scanner.nextLine();
                                    if(servicePe.metodoPagoVálido(ans)){
                                        pedidoEncontrado.setFormaPago(FormaPago.valueOf(ans.toUpperCase().trim())); 
                                        System.out.print("\n| Metodo de pago cambiado correctamente | ");
                                    }else{
                                         System.out.print("\n| Metodo de pago no valido. Se conserva el metodo anteriormente asignado | ");
                                    }
                                }
                                if(pedidoEncontrado.getFormaPago() != null ){
                                    System.out.println("\n| Desea confirmar el pedido?(si/no): |");
                                    ans = scanner.nextLine();
                                    switch(ans.toLowerCase()){
                                    case "si":
                                        try{
                                            servicePe.confirmarPedido(pedidoEncontrado);
                                            servicePe.crearPedidoTransaccional(pedidoEncontrado);
                                        }catch(Exception e){
                                            System.out.println(e.getMessage()); 
                                        }
                                         if(pedidoEncontrado.getEstado() == Estado.CONFIRMADO){
                                            pedidoEncontrado.setEstado(Estado.CANCELADO); 
                                         }
                                    break;
                                    case "no":
                                        System.out.println("\n| El pedido no se ha confirmado |");
                                        break;
                                    default:
                                        System.out.println("\n| Opcion no válida |");
                                        System.out.println("\n| El pedido no se ha confirmado |");
                                    }
                                }else{
                                    System.out.println("\n| Forma de pago no válida |");
                                }
                                 
                        }
                                
                        
                        
                    }else{
                        System.out.println("| El pedido buscado no existe o no esta en estado pendiente |");
                    }
                    
                }else{
                    System.out.println("| El usuario no tiene pedidos pendientes |");
                }
            }else{
                System.out.println("| Usuario no Encontrado |");
            }
       }else{
          System.out.println("\n| No hay usuarios creados aun |"); 
       }
       
   }
   
   public void eliminarPedido() throws Exception{
       Long pedidoId = -1l;
       Usuario usuario = null;
       List<Usuario> usuarios = servicePe.listarUsuarios();
       if(!usuarios.isEmpty()){
           for(Usuario u : servicePe.listarUsuarios()){
           System.out.println("| USUARIO '" + u.getNombre() + "' | ID: " + u.getId() + " |");
       }
       System.out.print("Ingrese ID del usuario: ");
            Long usuarioId = 0l;
            try{
               usuarioId = Long.valueOf(scanner.nextLine());
            }catch(NumberFormatException e){
                System.out.println("Formato ingresado no válido");
            }

            try{
                usuario = servicePe.findUsuario(usuarioId);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            if(usuario != null){
                if(!servicePe.listarPedidos().isEmpty()){
                    System.out.println("\n| PEDIDOS DEL USUARIO '" + usuario.getNombre() + "' |");
                    for(Pedido p : servicePe.listarPedidosPorUsuario(usuarioId)){
                        if(p.getEstado() != Estado.CANCELADO){
                            servicePe.mostrarPedido(p); 
                        }  
                    }
                    
                    System.out.println("\n| Ingrese el id del pedido a eliminar: |");
                    try{
                        pedidoId = Long.valueOf(scanner.nextLine());
                    }catch(NumberFormatException e){
                        System.out.println("Formato ingresado no válido");
                    }
                    Pedido pedidoEncontrado = servicePe.buscarPedidoMemoria(pedidoId);
                    if(pedidoEncontrado != null && pedidoEncontrado.getEstado() != Estado.CANCELADO){
                        servicePe.mostrarPedido(pedidoEncontrado);
                        System.out.println("\n| Desea eliminar el pedido?(si/no): |");
                        String ans = scanner.nextLine();
                        switch(ans.toLowerCase()){
                            case "si":
                                try{
                                    servicePe.eliminarPedidoLogico(pedidoEncontrado.getId());
                                }catch(Exception e){
                                    System.out.println(e.getMessage());
                                }
                                
                            break;
                            case "no":
                                System.out.println("\n| El pedido no se ha eliminado |");
                                break;
                            default:
                                System.out.println("\n| Opcion no válida |");
                                System.out.println("\n| El pedido no se ha eliminado |");
                        }
                    }else{
                        System.out.println("| El pedido buscado no existe o esta cancelado |");
                    }
                    
                }else{
                    System.out.println("| El usuario no tiene pedidos |");
                }
            }else{
               System.out.println("| Usuario no Encontrado |"); 
            }
       }else{
           System.out.println("| No hay usuarios creados aun |");
        }
       
   }
   
   public void mostrarDetallePedido() throws Exception{
       Long pedidoId = -1l;
       List<Usuario> usuarios = servicePe.listarUsuarios();
       List<Pedido> pedidos = new ArrayList<>();
       Pedido pedidoEncontrado = null;
       Usuario usuario = null;
       if(!usuarios.isEmpty()){
           for(Usuario u : servicePe.listarUsuarios()){
           System.out.println("| USUARIO '" + u.getNombre() + "' | ID: " + u.getId() + " |");
            }
       System.out.print("Ingrese ID del usuario: ");
            Long usuarioId = 0l;
            try{
               usuarioId = Long.valueOf(scanner.nextLine());
            }catch(NumberFormatException e){
                 System.out.println("Formato ingresado no válido");
            }
            try{
                usuario = servicePe.findUsuario(usuarioId);
            }catch(Exception e){
                 System.out.println(e.getMessage());
            }
            
            if(usuario != null){
                pedidos = servicePe.listarPedidosPorUsuario(usuarioId);
                if(!pedidos.isEmpty()){
                    System.out.println("\n| PEDIDOS DEL USUARIO '" + usuario.getNombre() + "' |");
                    List<Pedido> pedidosPendientes = new ArrayList<>();
                    for(Pedido p : pedidos){
                        if(p.getEstado() == Estado.PENDIENTE){
                              servicePe.mostrarPedido(p);   
                              pedidosPendientes.add(p);
                        }
                   
                    }
                    if(!pedidosPendientes.isEmpty()){
                         System.out.println("\n| Ingrese el id del pedido a buscar: ");
                    try{
                        pedidoId = Long.valueOf(scanner.nextLine());
                    }catch(NumberFormatException e){
                         System.out.println("| Formato ingresado no válido |");
                    }
                    try{
                        pedidoEncontrado = servicePe.buscarPedidoMemoria(pedidoId);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    
                    if(pedidoEncontrado != null && pedidoEncontrado.getEstado() != Estado.CANCELADO){
                        System.out.println("\n| DETALLES DEL PEDIDO '" + pedidoEncontrado.getId() + "' |");
                        for(DetallePedido dt : pedidoEncontrado.getDetalles()){
                            servicePe.mostrarDetallePedido(dt);
                        }
                    }else{
                         System.out.println("| El pedido buscado no existe o esta cancelado |");
                    }
                    }else{
                            System.out.println("| El usuario no tiene pedidos pendientes|");
                            }
                }else{
                    System.out.println("| El usuario no tiene pedidos |");
                }
            }else{
               System.out.println("| Usuario no Encontrado |"); 
            }
       }else{
           System.out.println("| No hay usuarios creados aun |");
       }
       
            
   }
   
   public void añadirDetallePedido() throws Exception{
       Long pedidoId = -1l;
       int cantidad = -1;
       List<Usuario> usuarios = servicePe.listarUsuarios();
       Usuario usuario = null;
       Pedido pedidoEncontrado = null;
       List<Categoria> categorias = null;
       Categoria categoria = null;
       List<Pedido> pedidos = new ArrayList<>();
       Producto productoEncontrado = null;
       DetallePedido detalleExistente = null;
       
       if(!usuarios.isEmpty()){
           for(Usuario u : servicePe.listarUsuarios()){
           System.out.println("| USUARIO '" + u.getNombre() + "' | ID: " + u.getId() + " |");
       }
       System.out.print("\n| Ingrese ID del usuario: ");
            Long usuarioId = 0l;
            List<Producto> productosCategoria = new ArrayList<>();
            try{
               usuarioId = Long.valueOf(scanner.nextLine());
            }catch(NumberFormatException e){
                System.out.println("| Formato ingresado no válido |");
            }
            try{
                usuario = servicePe.findUsuario(usuarioId);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            
            if(usuario != null){
                pedidos = servicePe.listarPedidosPorUsuario(usuarioId);
            if(!pedidos.isEmpty()){
                    System.out.println("\n| PEDIDOS PENDIENTES DEL USUARIO '" + usuario.getNombre() + "' |");
                    List<Pedido> pedidosPendientes = new ArrayList<>();
                    for(Pedido p : pedidos){
                        if(p.getEstado() == Estado.PENDIENTE){
                              servicePe.mostrarPedido(p);   
                              pedidosPendientes.add(p);
                        }
                   
                    }
                    if(!pedidosPendientes.isEmpty()){
                        System.out.println("\n| Ingrese el id del pedido a añadir detalles: |");
                    try{
                        pedidoId = Long.valueOf(scanner.nextLine());
                    }catch(NumberFormatException e){
                        System.out.println("Formato ingresado no válido");
                    }
                    try{
                        pedidoEncontrado = servicePe.buscarPedidoMemoria(pedidoId);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    
                    if(pedidoEncontrado != null && pedidoEncontrado.getEstado() == Estado.PENDIENTE){
                        System.out.println("\n| DETALLES DEL PEDIDO '" + pedidoEncontrado.getId() + "' |");
                        for(DetallePedido dt : pedidoEncontrado.getDetalles()){
                            servicePe.mostrarDetallePedido(dt);
                        }
                 System.out.println("\n| CREAR DETALLE PEDIDO |");

                 System.out.println("\n| CATEGORIAS |");
                 try{
                     categorias = serviceC.findAll();
                 }catch(Exception e){
                     System.out.println(e.getMessage());
                 }
                 if(categorias != null && !categorias.isEmpty()){
                     for(Categoria c : categorias){
                   serviceC.mostrarCategoria(c);
               }
               System.out.print("\n| Ingrese id o nombre de la categoria a filtrar: ");
               
                String ans;
                ans = scanner.nextLine();
                 try{
                     categoria = serviceC.obtenerCategoriaPorNombreId(ans, categorias);
                 } catch(Exception e){
                     System.out.print(e.getMessage());
                 }          
                
                if(categoria != null){
                    System.out.print("| PRODUCTOS DE LA CATEGORIA '" + categoria.getNombre() + "' ");  
                    try{
                        productosCategoria = servicePr.listarProductosPorCategoria(categoria.getId());
                    }catch(Exception e){
                        System.out.print(e.getMessage());
                    }
                    if(productosCategoria.isEmpty()){
                        System.out.print("\n| La categoria solicitada no tiene productos |");
                    }else{
                        for(Producto p : productosCategoria){
                        servicePr.mostrarProducto(p);
                    }
                    System.out.print("\n| Dime el id del producto a agregar: |");
                    Long productoId = -1l;
                    String respuesta = scanner.nextLine();
                        try{
                        productoId = Long.valueOf(respuesta);
                    }catch(NumberFormatException e){
                        System.out.print("| Formato ingresado no válido |");
                    }  
                        try{
                            productoEncontrado = servicePr.findById(productoId);
                        }catch(Exception e){
                           System.out.print(e.getMessage()); 
                        }
                        boolean valido = false;
                        for(Producto p : productosCategoria){
                         if(productoEncontrado != null && p.getId().equals(productoEncontrado.getId())){
                            valido = true;
                        }
                     }
                    
                    if(valido){
                        try{
                            detalleExistente = servicePe.findDetallePedidoByProductoId(pedidoId, productoId);
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                        
                        if(detalleExistente != null){
                             System.out.println("| Ya existe un pedido del producto solicitado |");
                        }else{
                            System.out.print("| PRODUCTO '" + productoEncontrado.getNombre() + "' |");
                        servicePr.mostrarProducto(productoEncontrado);
                        System.out.println("\n| Dime La cantidad a agregar: ");
                        try{
                             cantidad = Integer.parseInt(scanner.nextLine());
                        }catch(NumberFormatException e){
                             System.out.println("| Formato ingresado no válido |" + e.getMessage());
                        }
                    
                        try{
                            servicePe.añadirDetallePedido(pedidoEncontrado.getId(), cantidad, productoEncontrado.getId());
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                        
                        System.out.println("\n| Detalle del pedido agregado correctamente |");

                        }
                        
                        }else{
                         System.out.println("| Producto no válido |");
                        }
                    }
                    
                        
                }else{
                    System.out.println("| Categoria no válida |");
                }
                 }else{
                     System.out.println("| No hay categorias creadas aun |");
                 }
               
                    }else{
                        System.out.println("| El pedido buscado no existe o no esta en estado pendiente |");
                    }
                    }else{
                        System.out.println("| El usuario no tiene pedidos pendientes|");
                    }
                    
                    
                }else{
                    System.out.println("| El usuario no tiene pedidos |");
                }
            }else{
               System.out.println("| Usuario no Encontrado |"); 
            }
       }else{
           System.out.println("| No hay usuarios creados aun |");
       }
       
}
   
   public void eliminarDetallePedido() throws Exception{
       List<Usuario> usuarios = servicePe.listarUsuarios();
       Pedido pedidoEncontrado = null;
       List<Producto> productos = null;
       List<Pedido> pedidos = new ArrayList<>();
       DetallePedido detallePedido = null;
       if(!usuarios.isEmpty()){
           for(Usuario u : usuarios){
           System.out.println("| USUARIO '" + u.getNombre() + "' | ID: " + u.getId() + " |");
       }
    System.out.print("Ingrese ID del usuario: ");
            Long usuarioId = -1l;
            Long pedidoId = -1l;
            Long productoId = -1l;
            try{
               usuarioId = Long.valueOf(scanner.nextLine());
            }catch(NumberFormatException e){
                System.out.println("| Formato ingresado no válido | " + e.getMessage());
            }

            Usuario usuario = servicePe.findUsuario(usuarioId);
            if(usuario != null){
               pedidos = servicePe.listarPedidosPorUsuario(usuarioId);
            if(!pedidos.isEmpty()){
                    System.out.println("\n| PEDIDOS PENDIENTES DEL USUARIO '" + usuario.getNombre() + "' |");
                     List<Pedido> pedidosPendientes = new ArrayList<>();
                    for(Pedido p : pedidos){
                        if(p.getEstado() == Estado.PENDIENTE){
                              servicePe.mostrarPedido(p);   
                              pedidosPendientes.add(p);
                        }
                   
                    }
                    if(!pedidosPendientes.isEmpty()){
                    
                    System.out.println("\n| Ingrese el id del pedido a eliminar detalles: |");
                    try{
                        pedidoId = Long.parseLong(scanner.nextLine().trim());
                    }catch(NumberFormatException e){
                        System.out.println("| Formato ingresado no válido |");
                    }
                    try{
                         pedidoEncontrado = servicePe.buscarPedidoMemoria(pedidoId);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    
                    if(pedidoEncontrado != null && pedidoEncontrado.getEstado() == Estado.PENDIENTE){
                        try{
                            productos = servicePr.findAll();
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                        if(productos != null && !productos.isEmpty()){
                           System.out.println("\n| DETALLES DEL PEDIDO '" + pedidoEncontrado.getId() + "' |");
                        for(DetallePedido dt : pedidoEncontrado.getDetalles()){
                            servicePe.mostrarDetallePedido(dt);
                            System.out.println("\n| PRODUCTOS ASOCIADOS AL DETALLE N° " + dt.getId() + " |");
                            for(Producto p : productos){
                                if(p.getId().equals(dt.getProducto().getId())){
                                    servicePr.mostrarProducto(p);
                                }
                            }
                        }
                        System.out.println("\n| Ingrese el id del producto a eliminar: |");
                    try{
                        productoId = Long.parseLong(scanner.nextLine().trim());
                    }catch(NumberFormatException e){
                        System.out.println("| Formato ingresado no válido | " + e.getMessage());
                    }
                        try{
                            detallePedido = servicePe.findDetallePedidoByProductoId(pedidoId, productoId);
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                        
                        if(detallePedido != null){
                            try{
                                servicePe.eliminarDetallePedido(pedidoId, productoId);
                            }catch(Exception e){
                                System.out.println(e.getMessage());
                            }
                            
                            
                        }else{
                            System.out.println("| El detalle del producto solicitado no existe |");
                        } 
                        }else{
                            System.out.println("| No hay productos creados en la categoria aun |");
                        }
                        
                        }else{
                        System.out.println("| El pedido buscado no existe o no esta en estado pendiente |");
                    }
                    }else{
                        System.out.println("| El usuario no tiene pedidos pendientes |");
                    }
                }else{
                    System.out.println("| El usuario no tiene pedidos |");
                }
            }else{
               System.out.println("| Usuario no Encontrado |"); 
            }
       }else{
          System.out.println("| No hay usuarios creados aun |");
       }
       
    }
   
}
