package ui;

import entities.Categoria;
import entities.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import services.CategoriaServiceImpl;
import services.ProductoServiceImpl;

public class CategoriaUI {
    CategoriaServiceImpl serviceC = new CategoriaServiceImpl();
    ProductoServiceImpl serviceP = new ProductoServiceImpl();
    Scanner scanner = new Scanner(System.in);
    List<Categoria> categoriasActivas;
    
    public void mostrarMenuProducto() throws Exception{
        
        int opcion = 0;
        
        do{
            System.out.println("\n--- MENÚ CATEGORIAS ---");
            System.out.println("\n1) Listar");
            System.out.println("\n2) Crear");
            System.out.println("\n3) Editar");
            System.out.println("\n4) Eliminar (baja lógica)");
            System.out.println("\n0) Volver al menu principal");
            System.out.println("\nQue desea realizar?: ");

            try{
                opcion = Integer.parseInt(scanner.nextLine());
            }catch(IllegalArgumentException e){
                System.out.println("| Dato inválido " + e.getMessage());
            }
            
            switch(opcion){
                case 1 -> listarCategorias();
                case 2 -> crearCategorias();
                case 3 -> editarCategorias();
                case 4 -> eliminarCategorias();
                case 0 -> System.out.println("\n| REGRESANDO AL MENU PRINCIPAL |\n" );
                default -> System.out.println("| Opcion inválida |");
            }
            
        }while( opcion != 0);
    }
    
    public void listarCategorias() throws Exception{
        System.out.println("\n| LISTAR CATEGORIAS |\n" );
        categoriasActivas = null;
        try{
            categoriasActivas = serviceC.findAll();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        if(categoriasActivas == null || categoriasActivas.isEmpty()){
            System.out.println("\n| No hay categorias creadas aun |\n" );
        }else{
            for(Categoria c : categoriasActivas){
                serviceC.mostrarCategoria(c);
            }
        }       
    }
    public void crearCategorias() throws Exception{
        listarCategorias();
        System.out.println("\n| CREAR CATEGORIAS |\n" );
        
        System.out.println("Nombre de la nueva categoria: ");
        String nombre = scanner.nextLine();
            
        System.out.println("Descripcion de la nueva categoria: ");
        String descripcion = scanner.nextLine();
        
        try{
            serviceC.create(new Categoria(nombre, descripcion)); 
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
    }
    public void editarCategorias() throws Exception{
        listarCategorias();
        Categoria categoriaEncontrada = null;
        
        System.out.println("\n| EDITAR CATEGORIAS |\n" );
        System.out.println("\n| Dime el nombre o id de la categoria a editar(Indiferentemente de las mayusculas o minusculas): " );
        
        String ans = scanner.nextLine();
        try{
            categoriaEncontrada = serviceC.obtenerCategoriaPorNombreId(ans, categoriasActivas);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        if(categoriaEncontrada != null){
            List<Producto> productosAsociados = new ArrayList<>();
            try{
                productosAsociados = serviceP.listarProductosPorCategoria(categoriaEncontrada.getId());
            }catch(Exception e){
               System.out.println(e.getMessage()); 
            }
            if(productosAsociados.isEmpty()){
                System.out.println("\n| RESULTADOS DE LA CATEGORIA BUSCADA |\n" );
            
            serviceC.mostrarCategoria(categoriaEncontrada);
            
            System.out.println("\n| EDITAR CATEGORIA |\n" );
            
            System.out.print("Nombre de la categoria: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Descripcion de la categoria: ");
            String descripcion = scanner.nextLine();
            
            try{
                serviceC.update(new Categoria(categoriaEncontrada.getId(), categoriaEncontrada.isEliminado(), categoriaEncontrada.getCreatedAt(), nombre, descripcion));
            }catch(Exception e){
                System.out.print(e.getMessage());
            }
            }else{
                System.out.print("| Hay productos asignados aun a la categoria |");
            }
            
            
            
        }else{
            System.out.print("| No hay una categoria existente para el valor ingresado |");
        }
    }
    public void eliminarCategorias() throws Exception{
        listarCategorias();
        Categoria categoriaEncontrada = null;
        
        System.out.println("\n| ELIMINAR CATEGORIAS |\n" );
        System.out.println("| Dime el id o nombre de la categoria a eliminar: " );
        
        String ans = scanner.nextLine();

        try{
            categoriaEncontrada = serviceC.obtenerCategoriaPorNombreId(ans, categoriasActivas);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
          if(categoriaEncontrada != null){
              List<Producto> productosAsociados = new ArrayList<>();
            try{
                productosAsociados = serviceP.listarProductosPorCategoria(categoriaEncontrada.getId());
            }catch(Exception e){
               System.out.println(e.getMessage()); 
            }
            if(productosAsociados.isEmpty()){
                System.out.println("\n| RESULTADOS DE LA CATEGORIA BUSCADA |\n" );
            
            serviceC.mostrarCategoria(categoriaEncontrada);
            
            System.out.println("\n| ELIMINAR CATEGORIA |\n" );
            System.out.println("\n| Desea eliminar la categoria '" + categoriaEncontrada.getNombre() + "'?(si/no): " );
            String opcion = scanner.nextLine();
            switch(opcion.toLowerCase()){
                case "si":
                    try{
                        serviceC.deleteLogico(categoriaEncontrada.getId());
                    }catch(Exception e){
                       System.out.println(e.getMessage()); 
                    }
                    break;
                case "no" :
                    System.out.println("| La categoria no se ha eliminado |" );
                    break;
                default :
                    System.out.println("| Opcion inválida |");
            }  
               
            }else{
                 System.out.println("| Aun hay productos asociados a la categoria |");
            }
                      
        }else{
            System.out.println("| No hay una categoria existente para el valor ingresado |");
          }
    }
    
    
}
