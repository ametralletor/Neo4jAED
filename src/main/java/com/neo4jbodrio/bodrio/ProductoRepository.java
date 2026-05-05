package com.neo4jbodrio.bodrio;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface ProductoRepository extends Neo4jRepository<Producto, Long> {
    @Query("MATCH (c1:Categoria)-[:TIENE]->(p:Producto)-[:RECOMENDADO]->(r:Producto)<-[:TIENE]-(c2:Categoria) WHERE id(p) = $id AND c1 <> c2 RETURN r")
    List<Producto> findRecomendadosDeOtraCategoria(Long id);
}