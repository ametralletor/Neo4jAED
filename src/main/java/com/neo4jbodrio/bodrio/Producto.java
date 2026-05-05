package com.neo4jbodrio.bodrio;


import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node ("Producto")
    public class Producto {

        @Id
        @GeneratedValue
        private Long id;

        private String nombre;
        private double precio;

    @Relationship(type = "RECOMENDADO", direction = Relationship.Direction.OUTGOING)
    private List<Producto> recomendados = new ArrayList<>();


    public Producto() {
    }

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public List<Producto> getRecomendados() {
        return recomendados;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setRecomendados(List<Producto> recomendados) {
        this.recomendados = recomendados;
    }

    public void addRecomendado(Producto producto) {
        this.recomendados.add(producto);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}


