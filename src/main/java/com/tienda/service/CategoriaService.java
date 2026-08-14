

package com.tienda.service;

import com.tienda.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.tienda.domain.Categoria;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.dao.DataIntegrityViolationException;

/*
 * Esta clase contiene la logica de negocio relacionada con las categorias.
 * Se implementan operaciones para listar, consultar, guardar y eliminar
 * categorias utilizando CategoriaRepository.
 *
 * Tambien se trabaja con transacciones mediante @Transactional y con
 * FirebaseStorageService para almacenar imagenes asociadas a las categorias.
 *
 * En el metodo delete se valida la existencia de la categoria y se controla
 * el caso en que no pueda eliminarse debido a productos asociados.
 *
 * Comentario personal:
 * Esta practica me permitio comprender mejor la funcion de la capa Service,
 * separando la logica de negocio del acceso directo a los datos.
 */


@Service
public class CategoriaService { 
    
    private final CategoriaRepository categoriaRepository;
    
    private final FirebaseStorageService firebaseStorageService;
    
    
    public CategoriaService(CategoriaRepository categoriaRepository, FirebaseStorageService firebaseStorageService){
        this.categoriaRepository = categoriaRepository;
        this.firebaseStorageService = firebaseStorageService;
    }
    
    
    @Transactional(readOnly=true)
    public List<Categoria> getCategorias(boolean activo){
        if (activo){
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }
    
    //Recupera en un registro de categoria -si existe-
    @Transactional(readOnly=true)
    public Optional<Categoria> getCategoria(Integer idCategoria){
        return categoriaRepository.findById(idCategoria);
    }
    
    //Si categoria, trae un idCategoria... se actualiza el registro, sino se crea
    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile){
        // se "salva" la categoria
        categoriaRepository.save(categoria);
        if (!imagenFile.isEmpty()){ // Nos pasan una imagen
            try {
                String ruta = firebaseStorageService.uploadImage(imagenFile,
                        "categoria", categoria.getIdCategoria());
                categoria.setRutaImagen(ruta);
                categoriaRepository.save(categoria);
            } catch(IOException e){
                
            }
        }
    
    }

    
    //Si idCategoria existe, se elimina... si no tiene productos asociados
    @Transactional
    public void delete(Integer idCategoria){
        // se valida que la categoria exista...
        if (!categoriaRepository.existsById(idCategoria)){
            // se lanza una excepcion para indicarle al usuario que no se elimino
            throw new IllegalArgumentException ("La categoria con ID " + idCategoria +" no existe!");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
        }catch(DataIntegrityViolationException e){
            throw new IllegalStateException("No se puede eliminar la categoria, tiene productos asociados");
        }
    
    }
            
}



    


