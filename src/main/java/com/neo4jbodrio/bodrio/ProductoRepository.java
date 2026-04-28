package com.neo4jbodrio.bodrio;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface ProductoRepository extends Neo4jRepository<Producto, Long> {
}