package com.mza.Libreria.servicios;

import com.mza.Libreria.entidades.Autor;
import com.mza.Libreria.entidades.Editorial;
import com.mza.Libreria.entidades.Libro;
import com.mza.Libreria.entidades.Portada;
import com.mza.Libreria.excepciones.MiExcepcion;
import com.mza.Libreria.repositorios.AutorRepository;
import com.mza.Libreria.repositorios.EditorialRepository;
import com.mza.Libreria.repositorios.LibroRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lautaro Pavez
 */
@Service
public class ServiceLibro {

    @Autowired
    private LibroRepository libroRepo;

    @Autowired
    private AutorRepository autorRepo;

    @Autowired
    private EditorialRepository editorialRepo;
    
    @Autowired
    private ServiceAutor sAutor;

    @Autowired
    private ServiceEditorial sEditorial;
    
    @Autowired
    private ServicePortada sPortada;

    @Transactional
    public void crearLibro(MultipartFile archivo, String titulo, Integer anio, String idAutor, String idEditorial) throws MiExcepcion {

        validacion(titulo, anio, idAutor, idEditorial);
        
        Libro lib = new Libro();
        //Magia para obtener los ejemplares
        int ejemplares = (int) (Math.random() * 999 + 1);
        int prestados = ejemplares - (int) (Math.random() * 88 + 1);

        lib.setTitulo(titulo.trim());//Remuevo espacios al principio y al final del título
        lib.setAnio(anio);
        lib.setAlta(Boolean.TRUE);
        lib.setNroejemplares(ejemplares);
        lib.setEjemplaresPrestados(prestados);
        lib.setEjemplaresRestantes(ejemplares - prestados);
        lib.setIsbn((long) (int) (Math.random() * 999999 + 1));
        
        Autor autor = sAutor.buscarporId(idAutor);
        lib.setAutor(autor);
        
        Editorial editorial = sEditorial.buscarporId(idEditorial);
        lib.setEditorial(editorial);
        
        //Lo busca, si existe, lo setea y sino existe, lo crea y lo setea, por lo que en los dos casos lo setea, asi que le sacamos el "else".(Hice lo mismo en modificar autor)
//        Autor autor = sAutor.buscarPorNombre(nombreAut.toUpperCase());
//        if (autor == null) { 
//            sAutor.crearAutor(nombreAut);
//            Autor newautor = sAutor.buscarPorNombre(nombreAut.toUpperCase());
//            lib.setAutor(newautor);
//            //Autor autornuevo = sAutor.buscarporNombre(nombreAut.toUpperCase());
//            //Aib.setAutor(lib.getAutor());
//            //Aib.setAutor(sAutor.buscarporId(lib.getAutor().getId()));
//        }else{
//            lib.setAutor(autor);
//        }
//        Editorial editorial = sEditorial.buscarporNombre(nombreEdit.toUpperCase());
//        if (editorial == null) {
//            sEditorial.crearEditorial(nombreEdit);
//            Editorial neweditorial = sEditorial.buscarporNombre(nombreEdit.toUpperCase());
//            lib.setEditorial(neweditorial);
//        }else{
//            lib.setEditorial(editorial);
//        }    
        Portada portada = sPortada.guardar(archivo); //En el caso de que yo no mande un archivo adjunto, no hay problema pq está contemplado y el método guardar me va a devolver null entonces el usuario va a quedar sin foto.
        lib.setPortada(portada);
        
        libroRepo.save(lib);        // Esto es igual al --> daolibro.guardarLibro(lib);
    }

    //Pasar los throws al controllator y ver si solo se puede editar todo desde el libro o hace falta llamar a los métodos de los otros servicios
    @Transactional 
    public void modificarLibro(MultipartFile archivo, String id, String titulo, Integer anio, String newNombreAutor, String newNombreEditorial) throws MiExcepcion {

        validacion(titulo, anio, newNombreAutor, newNombreEditorial);

        Libro libro = libroRepo.buscarPorId(id);
        if (libro != null) {
            if(archivo != null){
                String idPortada = null;
                //if (libro.getPortada().getId() != null || (!libro.getPortada().getId().isEmpty())) {
                if (libro.getPortada().getId() != null){
                    idPortada = libro.getPortada().getId();
                }
                Portada portada = sPortada.actualizar(idPortada, archivo);
                libro.setPortada(portada);
            }
            //Verifico que exista algun cambio entre los dos objetos
            if (libro.getTitulo().equalsIgnoreCase(titulo)
                    && libro.getAnio().equals(anio)
                    && libro.getAutor().getNombre().equalsIgnoreCase(newNombreAutor)
                    && libro.getEditorial().getNombre().equalsIgnoreCase(newNombreEditorial)) {

                throw new MiExcepcion("No existen cambios para editar"); //Ver si puedo sacar el throw y poner un sout 
            }
            if (!libro.getTitulo().equalsIgnoreCase(titulo)) {
                libro.setTitulo(titulo.toUpperCase());
            }
            if (!libro.getAnio().equals(anio)) {
                libro.setAnio(anio);
            }
            if (!libro.getAutor().getNombre().equalsIgnoreCase(newNombreAutor)) {
                //libro.setAutor(sAutor.modificarAutor(newNombreAutor));
                Autor autor = sAutor.buscarPorNombre(newNombreAutor.toUpperCase());
                if (autor == null) { 
                   sAutor.crearAutor(newNombreAutor);
                   Autor newautor = sAutor.buscarPorNombre(newNombreAutor.toUpperCase());
                   libro.setAutor(newautor);
                }else{
                    libro.setAutor(autor);
                }
            }
            if (!libro.getEditorial().getNombre().equalsIgnoreCase(newNombreEditorial)) {
                //libro.setEditorial(sEditorial.modificarEditorial(newNombreEditorial));
                Editorial editorial = sEditorial.buscarporNombre(newNombreEditorial.toUpperCase());
                if (editorial == null) {
                    sEditorial.crearEditorial(newNombreEditorial);
                    Editorial neweditorial = sEditorial.buscarporNombre(newNombreEditorial.toUpperCase());
                    libro.setEditorial(neweditorial);
                }
                libro.setEditorial(editorial);
            }
            
            libroRepo.save(libro);
        } else {
            throw new MiExcepcion("No se encontró a este libro en la base de datos"); //Ver si lo dejo o lo pongo en el controlador al throw
        }
    }
    
    //2 Formas de buscar por Id
    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) {
        Optional<Libro> l = libroRepo.findById(id);
        if (l.isPresent()) {
            Libro lib = l.get();
            return lib;
        } else {
            return null; //throw new MiExcepcion("Este libro no existe."); esto debo ponerlo en el controlador
        }
    }

    @Transactional(readOnly = true)
    public Libro findbyId(String id) throws MiExcepcion{
        Libro l = libroRepo.buscarPorId(id);
        //Manera más corta si manejo la excepcion en controlador
        //return libroRepo.buscarPorId(id);
        if (l != null) {
            return l;
        } else {
            throw new MiExcepcion("Este libro no existe."); //esto debo ponerlo en el controlador
            //return null; 
        }
    }

    @Transactional(readOnly = true) //Busca todos los libros activos e inactivos (para el administrador)
    public List<Libro> listarTodos() {
        return libroRepo.findAll();
    }
    
    @Transactional(readOnly = true) //Busca todos los libros que esten activos y tengan ejemplares > 0
    public List<Libro> listaActivos() {
        return libroRepo.listaActivos();
    }
    
    @Transactional(readOnly = true) //Busca todo, la variable buscar nos va a buscar ya sea libros editoriales o autores
    public List<Libro> listaBuscada(String buscar) {
        return libroRepo.buscaTodo(buscar);
    }

    @Transactional(readOnly = true) //Busca todo, la variable buscar nos va a buscar ya sea libros editoriales o autores que esten activos
    public List<Libro> listaBuscadaActivos(String buscar) {
        return libroRepo.buscaTodoActivos(buscar);
    }

    @Transactional(readOnly = true) 
    public List<Libro> listaporEditorial(String nombre) {
        return libroRepo.listarPorEditorial(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<Libro> listaporAutor(String nombre) {
        return libroRepo.listarPorAutor(nombre);
    }
    
    @Transactional
    public Libro alta(String id) throws MiExcepcion {
        //Libro entidad = libroRepo.getOne(id);
        Libro entidad = findbyId(id);
        entidad.setAlta(true);
        return libroRepo.save(entidad);
    }
    
    @Transactional // En Pograma v2 de profe fiorde les pone WebException.class en el rollback, investigar
    public Libro baja(String id) throws MiExcepcion {
        //Libro entidad = libroRepo.getOne(id); //Hacemos uso de getOne para traerlo por id
        Libro entidad = findbyId(id);
        entidad.setAlta(false);
        return libroRepo.save(entidad);
    }
    
    @Transactional //Damos de alta al autor y todos sus libros
    public Autor altaxAutor(String idAutor) throws MiExcepcion {
        Autor autor = sAutor.buscarporId(idAutor);
        List<Libro> librosxAutor = listaporAutor(autor.getNombre());
        for (Libro libro : librosxAutor) {
            alta(libro.getId());
        }
        autor.setAlta(true);
        return autorRepo.save(autor);
    }
    
    @Transactional //Damos de baja al autor y todos sus libros
    public Autor bajaxAutor(String idAutor) throws MiExcepcion {
        Autor autor = sAutor.buscarporId(idAutor);
        List<Libro> librosxAutor = listaporAutor(autor.getNombre());
        for (Libro libro : librosxAutor) {
            baja(libro.getId());
        }
        autor.setAlta(false);
        return autorRepo.save(autor);
    }
    
    @Transactional //Damos de alta al autor todos sus libros
    public Editorial altaxEditorial(String idAutor) throws MiExcepcion {
        Editorial editorial = sEditorial.buscarporId(idAutor);
        List<Libro> librosxEdit = listaporAutor(editorial.getNombre());
        for (Libro libro : librosxEdit) {
            alta(libro.getId());
        }
        editorial.setAlta(true);
        return editorialRepo.save(editorial);
    }
    
    @Transactional //Damos de baja todos los libros con dicho autor
    public Editorial bajaxEditorial(String idAutor) throws MiExcepcion {
        Editorial editorial = sEditorial.buscarporId(idAutor);
        List<Libro> librosxEdit = listaporEditorial(editorial.getNombre());
        for (Libro libro : librosxEdit) {
            baja(libro.getId());
        }
        editorial.setAlta(false);
        return editorialRepo.save(editorial);
    }
    
    @Transactional // Clase servicio tarde min 37
    public void eliminarLibro(String id) throws Exception {
        Libro l = libroRepo.buscarPorId(id);
        if (l != null) {
            l.setAutor(null);
            l.setEditorial(null);
            libroRepo.deleteById(id); 
        } else {
            throw new Exception("No se encontró a este libro en la base de datos");
        }
    }
    
    @Transactional 
    public void eliminarAutor(String id) throws Exception {
        Autor a = autorRepo.buscarPorId(id);
        if (a != null) {
            autorRepo.deleteById(id);
        } else {
            throw new Exception("No se encontró a este Autor en la base de datos");
        }
    } 
    @Transactional 
    public void eliminarEditorial(String id) throws Exception {
        Editorial e = editorialRepo.buscarPorId(id);
        List<Libro> librosxEdit = listaporEditorial(e.getNombre());
        if (librosxEdit != null) {
            throw new Exception("No se puede eliminar esta Editorial porque tiene libros a su nombre, primero deberas eliminar los libros");
        }
        if (e != null) {
            editorialRepo.deleteById(id);
        } else {
            throw new Exception("No se encontró a esta Editorial en la base de datos");
        }
    }    
    
    //Servicios Spring - Tarde: Min 11:30 muestra método de validación que tiene ella en su clase
    public void validacion(String titulo, Integer anio, String idAutor, String idEditorial) throws MiExcepcion {
        
        if (titulo == null || titulo.isEmpty()) {
            throw new MiExcepcion("Debe indicar el título");
        }
        if (anio == null || anio < 0 || anio > 2080) {
            throw new MiExcepcion("Debe indicar el año");
        }
        if (idAutor == null || idAutor.trim().isEmpty()) {
            throw new MiExcepcion("Debe indicar el Autor");
        }
        if (idEditorial == null || idAutor.trim().isEmpty()) {
            throw new MiExcepcion("Debe indicar la Editorial");
        }
        Libro l = libroRepo.buscarPorTitulo(titulo);
        if(l != null && l.getAutor().getId().equals(idAutor)){
            throw new MiExcepcion(" Este Libro ya existe");
        }    
    }

    //No utilizado
    @Transactional
    public void eliminarLibro(Libro libro) throws Exception {
        Libro l = libroRepo.buscarPorId(libro.getId());
        if (l != null) {
            libroRepo.deleteById(libro.getId());
        } else {
            throw new Exception("No se encontró a este libro en la base de datos");
        }
    }
//    //Otra manera mandando el objeto y retornandolo
//    @Transactional
//    public Libro modificarLibro(Libro editado) throws MiExcepcion{
//
//        validacion(editado.getTitulo(), editado.getAnio(), editado.getAutor().getNombre(), editado.getEditorial().getNombre());
//
//        Libro libro = libroRepo.buscarPorId(editado.getId());
//        if (libro != null) {
//            if (!editado.getTitulo().equalsIgnoreCase(libro.getTitulo())){
//                libro.setTitulo(editado.getTitulo().toUpperCase());
//            }
//            if (!editado.getAnio().equals(libro.getAnio())){
//                libro.setAnio(editado.getAnio());
//            }
//            if (!editado.getAutor().getNombre().equalsIgnoreCase(libro.getAutor().getNombre())){
//                libro.setAutor(sAutor.modificarAutor(editado.getAutor().getNombre()));
//            }
//            if (!editado.getEditorial().getNombre().equalsIgnoreCase(libro.getEditorial().getNombre())){
//                libro.setEditorial(sEditorial.modificarEditorial(editado.getEditorial().getNombre()));
//            }
//            //Verifico que exista algun cambio entre los dos objetos
//            if (editado.getTitulo().equalsIgnoreCase(libro.getTitulo())
//                    && editado.getAnio().equals(libro.getAnio())
//                    && editado.getAutor().getNombre().equalsIgnoreCase(libro.getAutor().getNombre())
//                    && editado.getEditorial().getNombre().equalsIgnoreCase(libro.getEditorial().getNombre())) {
//                throw new MiExcepcion("No existen cambios para editar");
//            }
//            return libroRepo.save(libro);
//        } else {
//            throw new MiExcepcion("No se encontró a este libro en la base de datos");
//        }
//    }
    //Así seria si lo hiciera con el String nombre(método no arreglado)
//    public void validacionAutor(String nombre) throws Exception {
//
//        Autor a = sAutor.buscarporNombre(nombre);
//        if (a == null) {
//            sAutor.crearAutor(nombre);
//        } else {
//            libro.setAutor(a);
//        }
//    }
}
