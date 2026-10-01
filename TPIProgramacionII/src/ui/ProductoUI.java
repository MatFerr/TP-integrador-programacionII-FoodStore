package ui;

import entities.Categoria;
import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import enums.Estado;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import services.CategoriaServiceImpl;
import services.PedidoServiceImpl;
import services.ProductoServiceImpl;

public class ProductoUI {
    
    CategoriaServiceImpl serviceC = new CategoriaServiceImpl();
    ProductoServiceImpl serviceP = new ProductoServiceImpl();
    PedidoServiceImpl servicePe = new PedidoServiceImpl();
    Scanner scanner = new Scanner(System.in);
    List<Categoria> categoriasActivas;
    
    
    public void mostrarMenuProducto() throws Exception{
        
        int opcion = 0;
        
        do{
            System.out.println("\n--- MENÚ PRODUCTOS ---");
            System.out.println("\n1) Listar");
            System.out.println("\n2) Crear");
            System.out.println("\n3) Editar");
            System.out.println("\n4) Eliminar (baja lógica)");
            System.out.println("\n5) Listar por categoria");
            System.out.println("\n0) Volver al menu principal");
            System.out.println("\nQue desea realizar?: ");        

            try{
                opcion = Integer.parseInt(scanner.nextLine());
            }catch(IllegalArgumentException e){
                System.out.println("Dato inválido " + e.getMessage());
            }
            
            switch(opcion){
                case 1 -> listarProductos();
                case 2 -> crearProductos();
                case 3 -> editarProductos();
                case 4 -> eliminarProductos();
                case 5 -> listarPorCategoria();
                case 0 -> System.out.println("\n| REGRESANDO AL MENU PRINCIPAL |\n" );
                default -> System.out.println("Opcion inválida");
            }
            
        }while( opcion != 0);
    } 
    
    private void listarProductos() {
        List<Producto> productosCategoria = null;
        categoriasActivas = null;
        
        System.out.println("\n| LISTAR PRODUCTOS POR CATEGORIA |\n" );
        try{
            categoriasActivas = serviceC.findAll();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        if(categoriasActivas == null || categoriasActivas.isEmpty()){
            System.out.println("| No hay categorias creadas aun |" );
        }else{
            for(Categoria c : categoriasActivas){
            System.out.println("\n--- " + c.getNombre().toUpperCase() + " ---\n");
            try{
                productosCategoria = serviceP.listarProductosPorCategoria(c.getId());
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            
            if(productosCategoria == null || productosCategoria.isEmpty()){
                System.out.println("| No hay productos asignados a esta categoria |" );
            }else{
                for(Producto p : productosCategoria){
                    serviceP.mostrarProducto(p);
                }
            }
                c.setProductos(productosCategoria);
            }
        }
        
    }
    public void listarPorCategoria(){
        List<Producto> productosCategoria = new ArrayList<>();
        categoriasActivas = null;
        Categoria categoriaEncontrada = null;
        
        System.out.println("\n| LISTAR PRODUCTOS POR CATEGORIA |\n" );
        try{
            categoriasActivas = serviceC.findAll();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        if(categoriasActivas == null || categoriasActivas.isEmpty()){
            System.out.println("| No hay categorias creadas aun |" );
        }else{
            for(Categoria c : categoriasActivas){
               System.out.println("\n--- " + c.getNombre().toUpperCase() + " | ID: " + c.getId() +" ---\n"); 
            }
            
            System.out.println("| Dime el nombre o el id de la categoria a buscar: |" );
        String ans = scanner.nextLine();
        try{
            categoriaEncontrada = serviceC.obtenerCategoriaPorNombreId(ans, categoriasActivas);
        }catch(Exception e){
            System.out.print(e.getMessage());
        }
        
        
        if(categoriaEncontrada != null){
            System.out.println("\n--- " + categoriaEncontrada.getNombre().toUpperCase() + " ---\n");
            try{
                productosCategoria = serviceP.listarProductosPorCategoria(categoriaEncontrada.getId());
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            if(!productosCategoria.isEmpty()){
                
                for(Producto p : productosCategoria){
                    serviceP.mostrarProducto(p);
                }
            }else{
                System.out.println("| La categoria solicitada no tiene productos cargados aun |" );
            }
        }else{
            System.out.println("| El id solicitado no es válido |" );
        }
        }
    }
    public void crearProductos() {
        listarProductos();
        Double precio = -1.0;
        Categoria categoriaEncontrada = null;
        int stock = -1;
        System.out.println("\n| CREAR PRODUCTOS |\n" );
        
        System.out.println("| Dime el nombre o el id de la categoria la cual pertenecera el producto: |" );
        String ans = scanner.nextLine();
        try{
            categoriaEncontrada = serviceC.obtenerCategoriaPorNombreId(ans, categoriasActivas);
        }catch(Exception e){
            System.out.print(e.getMessage());
        }
        
        
        if(categoriaEncontrada != null){
            serviceC.mostrarCategoria(categoriaEncontrada);
            System.out.print("\nNombre del producto: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Descripcion del producto: ");
            String descripcion = scanner.nextLine();
            
            System.out.print("Url de la imagen del producto: ");
            String imagen = scanner.nextLine();
            
            System.out.print("Precio: ");
            try{
                precio = Double.parseDouble(scanner.nextLine().trim());
            }catch(NumberFormatException e){
                System.out.print("| Precio ingresado inválido | " + e.getMessage() + " |\n");
            }
             
        
            System.out.print("Stock inicial: ");
            try{
                
                stock = Integer.parseInt(scanner.nextLine().trim());
            }catch(NumberFormatException e){
                System.out.print("| Stock ingresado inválido | " + e.getMessage() + " |\n");
            }
            
            Producto producto = new Producto(nombre, descripcion, precio, stock, imagen, true);
            try{
                serviceP.create(producto, categoriaEncontrada.getId());
            }catch(Exception e){
                 System.out.print(e.getMessage());
            }
            
           
        }else{
             System.out.print("No hay una categoria existente para el valor ingresado");
        }
        
        
        
    }
    private void editarProductos() throws Exception{
        listarProductos();
        Double precio = -1.0;
        int stock = -1;
        Producto productoEncontrado = null;
        Long categoriaId = null;
        
        System.out.println("\n| EDITAR PRODUCTOS |\n" );
        System.out.println("| Dime el nombre o id del producto a editar(Indiferentemente de las mayusculas o minusculas): " );
        
        String ans = scanner.nextLine();
        try{
            productoEncontrado = serviceP.obtenerProductoporIdNombre(ans, categoriasActivas);
            categoriaId = serviceP.obtenerCategoriaId(categoriasActivas, productoEncontrado);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
        
        if(productoEncontrado != null){
            List<Pedido> pedidos = servicePe.listarPedidos();
            DetallePedido detalleEncontrado = null;
            for (Pedido p : pedidos) {
             if (p.getEstado() == Estado.CANCELADO) {
                 continue;
             }
            DetallePedido detalle = p.findDetallePedidoByProducto(productoEncontrado);
             if (detalle != null) {
                detalleEncontrado = detalle;
                break;
            }
            }
            if(detalleEncontrado == null){
            System.out.println("\n| RESULTADOS DEL PRODUCTO BUSCADO |\n" );
            
            serviceP.mostrarProducto(productoEncontrado); 
            
            System.out.println("\n| EDITAR PRODUCTO |\n" );
            
            System.out.print("Nombre del producto: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Descripcion del producto: ");
            String descripcion = scanner.nextLine();
            
            System.out.print("Url de la imagen del producto: ");
            String imagen = scanner.nextLine();
            
            System.out.print("Precio: ");
            try{
                precio = Double.parseDouble(scanner.nextLine().trim());
            }catch(NumberFormatException e){
                System.out.print("| Precio ingresado inválido | " + e.getMessage() + " |\n");
            }
            System.out.print("Stock inicial: ");
            try{
                stock = Integer.parseInt(scanner.nextLine().trim());
            }catch(NumberFormatException e){
                System.out.print("| Stock ingresado inválido | " + e.getMessage() + " |\n");
            }
            Producto productoEditado = new Producto(nombre, descripcion, precio, stock, imagen, productoEncontrado.isDisponible(), productoEncontrado.getId(), productoEncontrado.isEliminado(), productoEncontrado.getCreatedAt());
            
            try{
                serviceP.update(productoEditado, categoriaId);
            }catch(Exception e){
               System.out.print(e.getMessage()); 
            }
            }else{
                System.out.println("| Hay pedidos asignados a ese producto aun |");
            }
            
        }else{
            System.out.print("No hay un producto existente para el valor ingresado");
        }
    }
    
    
    public void eliminarProductos() throws Exception{
        listarProductos();
        Producto productoEncontrado = null;
        Long categoriaId = null;
        System.out.println("\n| ELIMINAR PRODUCTOS |\n" );
        System.out.println("| Dime el id o nombre del producto a eliminar: " );
        
        String ans = scanner.nextLine();
        try{
            productoEncontrado = serviceP.obtenerProductoporIdNombre(ans, categoriasActivas);
            categoriaId = serviceP.obtenerCategoriaId(categoriasActivas, productoEncontrado);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
        if(productoEncontrado != null){
            List<Pedido> pedidos = servicePe.listarPedidos();
            DetallePedido detalleEncontrado = null;
            for(Pedido p : pedidos){
                if(p.findDetallePedidoByProducto(productoEncontrado) != null){
                    detalleEncontrado = p.findDetallePedidoByProducto(productoEncontrado);
                }
            }
            if(detalleEncontrado == null){
                System.out.println("\n| RESULTADOS DEL PRODUCTO BUSCADO |\n" );
            
            serviceP.mostrarProducto(productoEncontrado); 

            System.out.println("\n| ELIMINAR PRODUCTO |\n" );
            System.out.println("| Desea eliminar el producto '" + productoEncontrado.getNombre() + "'?(si/no): " );
            String opcion = scanner.nextLine();
            
            switch(opcion.toLowerCase()){
                case "si" :
                    try{
                       serviceP.deleteLogico(productoEncontrado.getId(), categoriasActivas); 
                    }catch(Exception e){
                       System.out.println(e.getMessage()); 
                    }
                    
                    break;
                case "no" :
                    System.out.println("| El producto no se ha eliminado |" );
                    break;
                default :
                    System.out.println("| Opcion inválida |");
            }
            }else{
                System.out.println("| Hay pedidos asignados a ese producto aun |");
            }
            
            
        }else{
            System.out.println("| No hay un producto existente para el valor ingresado |");
        }
        
        
    }
    
    
}
