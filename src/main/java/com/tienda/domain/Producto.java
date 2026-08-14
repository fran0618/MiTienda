package com.tienda.domain;

import lombok.Data;
import jakarta.persistence.*;
import java.io.Serializable;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
        
@Data
@Entity
@Table(name="producto")
public class Producto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;
    //private Integer idCategoria; ya no se usa porque esta definido en @ManyToOne
    
    @Column(unique = true, nullable = false, length = 50)
    @NotNull
    @Size(max=50)
    private String descripcion;
    
    
    @Column(columnDefinition="TEXT")
    private String detalle;
    
    @Column(precision=12, scale=2)
    @DecimalMin(value="0.00", inclusive=true)
    private BigDecimal precio;
    
    @Min(value=0, message="Las existencias no pueden ser negativas....")
    private Integer existencias;
    
    @Column(length = 1024)

    @Size(max=1024)
    private String rutaImagen;
    
    private boolean activo;
    
    @ManyToOne
    @JoinColumn(name="id_categoria")
    private Categoria categoria;
}
