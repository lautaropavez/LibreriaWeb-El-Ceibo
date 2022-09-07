
package com.mza.Libreria.servicios;

import com.mza.Libreria.entidades.Autor;
import com.mza.Libreria.excepciones.MiExcepcion;
import com.mza.Libreria.repositorios.AutorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Lautaro Pavez
 */
//No tengo metodo modificar porque se modifica solo en el libro
@Service
public class ServiceAutor {

    @Autowired
    private AutorRepository autorRepo;
        
    @Transactional
    public void crearAutor(String nombre) throws MiExcepcion {
        validacion(nombre);
        Autor autor = new Autor();
        autor.setNombre(nombre.toUpperCase());
        autor.setAlta(Boolean.TRUE);

        autorRepo.save(autor); 
    }
       
    @Transactional(readOnly = true)
    public Autor buscarporId(String id){
        return autorRepo.buscarPorId(id); 
    }
   
    @Transactional(readOnly = true)
    public Autor buscarPorNombre(String nombre) {
        return autorRepo.buscarPorNombre(nombre); 
    }
    
    //Busca todos los autores(activos e inactivos)
    @Transactional(readOnly = true)
    public List<Autor> buscaTodos() {
        return autorRepo.findAll(); 
    }
    
    //Busca todos los autores activos
    @Transactional(readOnly = true)
    public List<Autor> buscaActivos() {
        return autorRepo.buscaActivos(); 
    }
    
    public void validacion(String nombreAut) throws MiExcepcion {
        if (nombreAut == null || nombreAut.trim().isEmpty()) {
            throw new MiExcepcion("Debe indicar el nombre del Autor");
        }
        
        Autor autor = autorRepo.buscarPorNombre(nombreAut.toUpperCase());
        if(autor != null){
            throw new MiExcepcion("El autor ya existe");
        }
    }
    
    //------------------------------- NO USADOS --------------------------------
    
//    @Transactional
//    public Autor modificarAutor(String nombre) throws MiExcepcion { 
//        Autor autoreditado = autorRepo.buscarPorNombre(nombre);
//        if (autoreditado != null) {                                //Explicación lógica del método: Si autoredit es distinto a null, existe por lo tanto existe algún libro con ese autor
//            autoreditado.setNombre(nombre);                        //Posible linea innecesaria
//            return autorRepo.save(autoreditado);
//        } else {                                                   //Si no lo encuentra quiere decir que no existe ningún libro con ese autor por lo tanto sería un error
//            throw new MiExcepcion("No se encontró a este Autor en la base de datos.");
//        }
//    }
    
    //Nose si lo usaré, en buscar por nombre busco solo un objeto pq lo necesito asi para las validaciones si existe un autor o no
    @Transactional(readOnly = true)
    public List<Autor> listarPorAutor(String nombre) {
        return autorRepo.listarPorNombre(nombre);
    }
}
