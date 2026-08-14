package com.tienda.domain;

import lombok.Data;
import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/*
 * Entidad Categoria desarrollada durante las practicas del curso.
 * En esta clase se aplican conceptos de JPA para relacionar la clase
 * con la tabla categoria de la base de datos.
 *
 * Se utilizaron validaciones para controlar la descripcion y la ruta
 * de la imagen, además de una relacion OneToMany con Producto.
 *
 * Comentario personal:
 * Esta practica me permitio comprender como una entidad de Java se
 * relaciona con una tabla de la base de datos utilizando JPA.
 */


@Data
@Entity
@Table(name="categoria")
public class Categoria implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoria;
    
    @Column(unique = true, nullable = false, length = 50)
    @NotNull
    @Size(max=50)
    private String descripcion;
    
    @Column(length = 1024)

    @Size(max=1024)
    private String rutaImagen;
    
    private boolean activo;
    
    @OneToMany(mappedBy="categoria")
    private List <Producto> productos;        
}
