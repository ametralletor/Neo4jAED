package com.neo4jbodrio.bodrio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BodrioApplication {

	public static void main(String[] args) {
		SpringApplication.run(BodrioApplication.class, args);
	}
    @Bean
    CommandLineRunner cargarDatos(CategoriaRepository categoriaRepo) {
        return args -> {

            Producto hamburguesa = new Producto("Hamburguesa", 5);
            Producto pan = new Producto("Pan", 1.5);
            Producto salsa = new Producto("Salsa", 2);

            // recomendaciones
            hamburguesa.addRecomendado(pan);
            pan.addRecomendado(salsa);

            Categoria comidaRapida = new Categoria("Comida rápida");
            comidaRapida.addProducto(hamburguesa);
            comidaRapida.addProducto(pan);
            comidaRapida.addProducto(salsa);

            categoriaRepo.save(comidaRapida);

            System.out.println("Datos insertados");
        };
    }
}

