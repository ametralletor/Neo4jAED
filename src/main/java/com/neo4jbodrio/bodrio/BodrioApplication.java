package com.neo4jbodrio.bodrio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class BodrioApplication {

	public static void main(String[] args) {
        SpringApplication.run(BodrioApplication.class, args);
    }

    @Bean
    CommandLineRunner cargarDatos(CategoriaRepository categoriaRepo, ProductoRepository productoRepo) {
        return args -> {


            Scanner scanner = new Scanner(System.in);
            int opcion;

            do{
                System.out.println("\n------MENU------");
                System.out.println("1. AÑADIR PRODUCTO");
                System.out.println("2. AÑADIR CATEGORIA");
                System.out.println("3. MODIFICAR PRODUCTO");
                System.out.println("4. CREAR RELACION");
                System.out.println("5. MOSTRAR PRODUCTOS");
                System.out.println("6. MOSTRAR CATEGORIAS");
                System.out.println("7. ELIMINAR PRODUCTOS");
                System.out.println("8. ELIMINAR CATEGORIA");
                System.out.println("9. RECOMENDADOS DE OTRA CATEGORIA");
                System.out.println("10. SALIR");
                System.out.println("\n--- PULSE EL NUMERO INICADO PARA CADA ACCION --- ");
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch(opcion){
                    case 1 -> crearProducto(scanner, productoRepo, categoriaRepo);
                    case 2 -> crearCategoria(scanner, categoriaRepo);
                    case 3 -> actualizarProducto(scanner, productoRepo, categoriaRepo);
                    case 4 -> crearRelacion(scanner, productoRepo);
                    case 5 -> listarProductos(productoRepo);
                    case 6 -> mostrarCategorias(categoriaRepo);
                    case 7 -> borrarProducto(scanner, productoRepo);
                    case 8 -> borrarCategoria(scanner, categoriaRepo);
                    case 9 -> recomendadosDeOtraCategoria(scanner, productoRepo);

                }

            } while (opcion !=10);

            // Aday Ceratosaurio
            System.out.println("CERRANDO PROGRAMA...");
        };
    }



    public void crearProducto(Scanner scanner, ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {

        System.out.println("--- INSERTE EL NOMBRE DEL PRODUCTO ---");
        String nombre = scanner.nextLine();

        System.out.println("--- INSERTE EL PRECIO DEL PRODUCTO ---");
        double precio = scanner.nextDouble();
        scanner.nextLine();

        Producto producto = new Producto(nombre, precio);

        System.out.println("CATEGORIAS EXISTENTES:");
        List<Categoria> categorias = new ArrayList<>();
        categoriaRepo.findAll().forEach(categorias::add);

        for (int i = 0; i < categorias.size(); i++) {
            System.out.println(categorias.get(i).getId() + ". " + categorias.get(i).getNombre());
        }

        System.out.println("--- ¿QUIERE CREAR UNA CATEGORIA NUEVA PARA ESTE PRODUCTO? ESCRIBA SI ---");
        String respuesta = scanner.nextLine();

        if (respuesta.equalsIgnoreCase("SI")) {
            crearCategoria(scanner, categoriaRepo);

            categorias.clear();
            categoriaRepo.findAll().forEach(categorias::add);
        }

        System.out.print("ESCRIBA LA CATEGORIA DESEADA: ");
        String nombreCategoria = scanner.nextLine();

        Categoria seleccionada = null;

        for (Categoria categoria : categorias) {
            if (categoria.getNombre().equalsIgnoreCase(nombreCategoria)) {
                seleccionada = categoria;
                break;
            }
        }

        if (seleccionada == null) {
            System.out.println("NO EXISTE ESA CATEGORIA");
            return;
        }

        seleccionada.addProducto(producto);
        categoriaRepo.save(seleccionada);

        System.out.println("PRODUCTO CREADO.");
    }

    public void crearCategoria(Scanner scanner, CategoriaRepository categoriaRepo) {

        System.out.println("--- INSERTE EL NOMBRE DE LA CATEGORIA ---");
        String nombre = scanner.nextLine();

        Categoria nueva = new Categoria(nombre);

        categoriaRepo.save(nueva);

        System.out.println("CATEGORIA CREADA");

    }

    public void mostrarCategorias(CategoriaRepository categoriaRepo){
        System.out.println("--- CATEGORIAS EXISTENTES ---");
        List<Categoria> categorias = new ArrayList<>();
        categoriaRepo.findAll().forEach(categorias::add);

        for (int i = 0; i < categorias.size(); i++) {
            System.out.println(categorias.get(i).getId() + ". " + categorias.get(i).getNombre());
        }
    }

    public void crearRelacion(Scanner scanner, ProductoRepository productoRepo) {

        System.out.println("--- LISTA DE PRODUCTOS ---");
        listarProductos(productoRepo);

        try {
            System.out.print("ID DEL PRODUCTO: ");
            Long id1 = scanner.nextLong();

            System.out.print("ID DEL PRODUCTO RECOMENDADO: ");
            Long id2 = scanner.nextLong();
            scanner.nextLine();

            var opt1 = productoRepo.findById(id1);
            var opt2 = productoRepo.findById(id2);

            if (opt1.isPresent() && opt2.isPresent()) {
                Producto p1 = opt1.get();
                Producto p2 = opt2.get();

                p1.addRecomendado(p2);
                productoRepo.save(p1);

                System.out.println("RELACION CREADA");
            } else {
                System.out.println("ALGUN PRODUCTO NO EXISTE");
            }

        } catch (Exception e) {
            System.out.println("DEBES INTRODUCIR IDS VALIDOS");
            scanner.nextLine();
        }
    }

    public void listarProductos(ProductoRepository productoRepo) {
        System.out.println("\n--- LISTA DE PRODUCTOS ---");

        Iterable<Producto> todos = productoRepo.findAll();
        for (Producto p : todos) {
            Producto completo = productoRepo.findById(p.getId()).get();
            System.out.println(" - [" + completo.getId() + "] " + completo.getNombre() + " | Precio: " + completo.getPrecio() + "€");

            List<Producto> recomendados = completo.getRecomendados();
            if (recomendados != null && !recomendados.isEmpty()) {
                System.out.println("   └ RECOMENDADOS:");
                for (Producto r : recomendados) {
                    System.out.println("       - [" + r.getId() + "] " + r.getNombre() + " | " + r.getPrecio() + "€");
                }
            }
        }
    }

    public void recomendadosDeOtraCategoria(Scanner scanner, ProductoRepository productoRepo) {
        listarProductos(productoRepo);
        System.out.println("--- INSERTE EL ID DEL PRODUCTO ---");

        Long id = scanner.nextLong();
        scanner.nextLine();

        List<Producto> resultado = productoRepo.findRecomendadosDeOtraCategoria(id);

        if (resultado.isEmpty()) {
            System.out.println("NO HAY RECOMENDADOS DE OTRAS CATEGORIAS");
        } else {
            System.out.println("PRODUCTOS RECOMENDADOS DE OTRAS CATEGORIAS:");
            for (Producto p : resultado) {
                System.out.println(" → [" + p.getId() + "] " + p.getNombre() + " | " + p.getPrecio() + "€");
            }
        }
    }


    public void actualizarProducto(Scanner scanner, ProductoRepository productoRepo, CategoriaRepository categoriaRepo){
        System.out.println("\n--- ACTUALIZAR PRODUCTO ---");
        listarProductos(productoRepo);
        System.out.println("\nEscribe el ID del producto a actualizar:");

        Long id = scanner.nextLong();
        scanner.nextLine();

        var opt = productoRepo.findById(id);

        if (opt.isPresent()) {

            Producto p = opt.get();

            System.out.print("Nuevo nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Nuevo precio: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("¿Quieres cambiar la categoría del producto? (SI o NO)");
            String opcion = scanner.nextLine();
            if(opcion.equalsIgnoreCase("SI")){
                System.out.println("CATEGORIAS EXISTENTES:");
                List<Categoria> categorias = new ArrayList<>();
                categoriaRepo.findAll().forEach(categorias::add);
                for (int i = 0; i < categorias.size(); i++) {
                    System.out.println((i + 1) + ". " + categorias.get(i).getNombre());
                }
                System.out.print("Elige nueva categoría: ");
                int seleccion = scanner.nextInt();
                scanner.nextLine();
                Categoria nuevaCategoria = categorias.get(seleccion - 1);

                for (Categoria c : categorias) {
                        c.getProductos().remove(p);
                        categoriaRepo.save(c);
                }

                nuevaCategoria.addProducto(p);
                categoriaRepo.save(nuevaCategoria);
            }

            p.setNombre(nombre);
            p.setPrecio(precio);

            productoRepo.save(p);

            System.out.println("Producto actualizado");

        } else {
            System.out.println("Producto no encontrado");
        }

    }

    public void borrarProducto(Scanner scanner, ProductoRepository productoRepo){

        listarProductos(productoRepo);

        System.out.println("--- INSERTE EL ID DEL PRODUCTO A BORRAR ---");
        long id = scanner.nextInt();
        if (productoRepo.existsById(id)) {
            productoRepo.deleteById(id);
            System.out.println("PRODUCTO ELIMINADO.");
        } else {
            System.out.println("EL PRODUCTO NO EXISTE O EL ID NO ES CORRECTO");
        }
    }

    public void borrarCategoria(Scanner scanner, CategoriaRepository categoriaRepo){

        mostrarCategorias(categoriaRepo);

        System.out.println("--- INSERTE EL ID DE LA CATEGORIA A BORRAR ---");
        long id = scanner.nextInt();
        if (categoriaRepo.existsById(id)) {
            categoriaRepo.deleteById(id);
            System.out.println("CATEGORIA ELIMINADA.");
        } else {
            System.out.println("LA CATEGORIA NO EXISTE O EL ID NO ES CORRECTO");
        }
    }
}

