
package com.tienda.repository;

import com.tienda.domain.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Se utiliza JpaRepository para realizar las operaciones de acceso
 * a datos de la entidad Categoria sin implementar manualmente
 * las operaciones basicas de persistencia.
 *
 * Tambien se utiliza la consulta derivada findByActivoTrue(),
 * que permite obtener solamente las categorias que se encuentran activas.
 *
 * Comentario personal:
 * Esta practica me permitio comprender como Spring Data JPA genera
 * consultas a partir del nombre de los metodos del repositorio.
 */

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    // Spring Data JPA generará la implementación automáticamente
    List<Categoria> findByActivoTrue();
}