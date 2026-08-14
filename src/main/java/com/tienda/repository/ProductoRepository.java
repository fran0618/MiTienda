

package com.tienda.repository;

import com.tienda.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

/*
 *
 * En este repositorio se utiliza Spring Data JPA para acceder a los
 * datos de Producto. Se implementaron diferentes formas de realizar
 * consultas: consultas derivadas, JPQL y SQL nativo.
 *
 * Las consultas permiten buscar productos dentro de un rango de
 * precios y ordenar los resultados de forma ascendente.
 *
 * Comentario personal:
 * Esta practica me permitio comparar las consultas derivadas de
 * Spring Data JPA con JPQL y SQL nativo, comprendiendo las diferentes
 * formas disponibles para consultar la base de datos.
 */



@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    // Se crea una consulta derivada para recuperar los registros de los productos activas
    List<Producto> findByActivoTrue();
    
    //Consulta derivada para obtener los productos que estan en un rango de precios,ordenados por precio asc.
    public List<Producto> findByPrecioBetweenOrderByPrecioAsc(double precioInf, double precioSup);
    
    //Consulta JPQL para obtener los productos que estan en un rango de precios,ordenados por precio asc.
    @Query(value="SELECT p FROM Producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaJPQL(double precioInf, double precioSup);
    
    //Consulta SQL para obtener los productos que estan en un rango de precios,ordenados por precio asc.
    @Query(nativeQuery=true,
            value="SELECT * FROM producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaSQL(double precioInf, double precioSup);
    
}