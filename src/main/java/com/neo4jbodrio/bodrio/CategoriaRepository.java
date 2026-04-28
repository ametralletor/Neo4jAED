package com.neo4jbodrio.bodrio;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface CategoriaRepository extends Neo4jRepository<Categoria, Long> {
}
