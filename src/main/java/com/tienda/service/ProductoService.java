
package com.tienda.service;

import com.tienda.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.tienda.domain.Producto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.dao.DataIntegrityViolationException;

/*
 *
 * Esta clase contiene la logica de negocio relacionada con los productos.
 * Se implementan operaciones para listar, consultar, guardar y eliminar
 * productos utilizando ProductoRepository.
 *
 * Tambien se integra FirebaseStorageService para almacenar imagenes
 * asociadas a los productos y se utilizan transacciones con @Transactional.
 *
 * Ademas, se incluyen metodos para ejecutar consultas derivadas,
 * consultas JPQL y consultas SQL nativas utilizando rangos de precios.
 *
 * Comentario personal:
 * Esta practica me ayudo a comprender como la capa Service conecta
 * la logica de negocio con el repositorio y como reutilizar diferentes
 * tipos de consultas desde una misma clase de servicio.
 */

@Service
public class ProductoService { 
    
    private final ProductoRepository productoRepository;
    
    private final FirebaseStorageService firebaseStorageService;
    
    
    public ProductoService(ProductoRepository productoRepository, FirebaseStorageService firebaseStorageService){
        this.productoRepository = productoRepository;
        this.firebaseStorageService = firebaseStorageService;
    }
    
    
    @Transactional(readOnly=true)
    public List<Producto> getProductos(boolean activo){
        if (activo){
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }
    
    //Recupera en un registro de producto -si existe-
    @Transactional(readOnly=true)
    public Optional<Producto> getProducto(Integer idProducto){
        return productoRepository.findById(idProducto);
    }
    
    //Si producto, trae un idProducto... se actualiza el registro, sino se crea
    @Transactional
    public void save(Producto producto, MultipartFile imagenFile){
        // se "salva" la producto
        productoRepository.save(producto);
        if (!imagenFile.isEmpty()){ // Nos pasan una imagen
            try {
                String ruta = firebaseStorageService.uploadImage(imagenFile,
                        "producto", producto.getIdProducto());
                producto.setRutaImagen(ruta);
                productoRepository.save(producto);
            } catch(IOException e){
                
            }
        }
    
    }
    
    //Si idProducto existe, se elimina... si no tiene productos asociados
    @Transactional
    public void delete(Integer idProducto){
        // se valida que la producto exista...
        if (!productoRepository.existsById(idProducto)){
            // se lanza una excepcion para indicarle al usuario que no se elimino
            throw new IllegalArgumentException ("La producto con ID " + idProducto +" no existe!");
        }
        try {
            productoRepository.deleteById(idProducto);
        }catch(DataIntegrityViolationException e){
            throw new IllegalStateException("No se puede eliminar la producto, tiene productos asociados");
        }
    
    }
    
    //Método de servicio para la consulta derivada....
    @Transactional(readOnly=true)
    public List<Producto> consultaDerivada(double precioInf, double precioSup){
        return productoRepository.findByPrecioBetweenOrderByPrecioAsc(precioInf, precioSup);
    }
    
    //Método de servicio para la consulta JPQL....
    @Transactional(readOnly=true)
    public List<Producto> consultaJPQL(double precioInf, double precioSup){
        return productoRepository.consultaJPQL(precioInf, precioSup);
    }
    
    //Método de servicio para la consulta SQL....
    @Transactional(readOnly=true)
    public List<Producto> consultaSQL(double precioInf, double precioSup){
        return productoRepository.consultaSQL(precioInf, precioSup);
    }           
}