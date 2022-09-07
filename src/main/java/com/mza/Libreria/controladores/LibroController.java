package com.mza.Libreria.controladores;

import com.mza.Libreria.entidades.Autor;
import com.mza.Libreria.entidades.Editorial;
import com.mza.Libreria.entidades.Libro;
import com.mza.Libreria.excepciones.MiExcepcion;
import com.mza.Libreria.servicios.ServiceAutor;
import com.mza.Libreria.servicios.ServiceEditorial;
import com.mza.Libreria.servicios.ServiceLibro;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lautaro Pavez
 */

// Notas: Clase Servicio Mañana muestra CONTROLADOR DE LIBRO y en (Clase 2 Mañana)
@Controller 
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private ServiceLibro servLibro; 
    
    @Autowired
    private ServiceAutor servAutor; 
    
    @Autowired
    private ServiceEditorial servEditorial; 
    
    @GetMapping("/registro") 
    public String formulario(ModelMap modelo){
        
        List<Autor> autores = servAutor.buscaActivos();
        modelo.addAttribute("autores", autores);
        
        List<Editorial> editoriales = servEditorial.buscaActivas();
        modelo.addAttribute("editoriales", editoriales);
        
        return "nuevoLibro";
    }	
      
    @PostMapping("/registrar") 
    public String crear(ModelMap modelo,@RequestParam MultipartFile archivo,@RequestParam String titulo,@RequestParam Integer anio, @RequestParam String idAutor,@RequestParam String idEditorial){ 
        try{
           servLibro.crearLibro(archivo,titulo,anio,idAutor,idEditorial);
           modelo.put("exito","¡Registro exitoso!");    
           //return "redirect:/libro/lista";
           List<Autor> autores = servAutor.buscaActivos();
           modelo.addAttribute("autores", autores);
        
           List<Editorial> editoriales = servEditorial.buscaActivas();
           modelo.addAttribute("editoriales", editoriales);
           return "nuevoLibro"; 
          //Forma profe Videos MVC 2
          //modelo.put("titulo","¡¡Bienvenido a Libreria El Ceibo!!");
          //modelo.put("descripcion","Tu usuario fue registrado de manera satisfactoria");
          //return "exito.html"; //Si esta todo ok retorna la pag principal, en vez de el registro de nuevo
        }catch(MiExcepcion ex){
            modelo.put("error", ex.getMessage());
            //Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex); //Con esto nos tira el error por consola, lo podemos sacar
            modelo.put("titulo",titulo);
            modelo.put("anio",anio);
            modelo.put("autor",idAutor);
            modelo.put("editorial",idEditorial);
            return "nuevoLibro"; 
        }  
    }

    
    //Clase THYMELEAF min 01:03:00
    @GetMapping("/lista") //localhost:8080/libro/lista
    public String lista(ModelMap modelo){
        List<Libro> librosLista = servLibro.listarTodos();
        modelo.addAttribute("libros",librosLista); //Utilizo una llave("libros") y lo que viaja como valor es la lista librosLista
         return "list-libro"; // 
    }
        
    //IN PROCESS
    @GetMapping("/biblioteca") 
    public String biblioteca(ModelMap modelo){
       // List<Libro> librosLista = servLibro.listarTodos();
      //  modelo.addAttribute("libros",librosLista); 
       return "/fragments/nada"; // 
    }
    
    //CREAR TRY AND CATCH
    //Clase THYMELEAF min 01:02:00
    @GetMapping("/modificar/{id}") //localhost:8080/libro/modificar
    public String modificar(@PathVariable String id,ModelMap modelo){
        modelo.put("libro",servLibro.buscarPorId(id));
        return "modif-Libro"; // ya esta creado el form en el archivo form-perro.html (de la clase)
    }
    
    @PostMapping("/modificar/{id}") //,@RequestParam MultipartFile archivo
    public String modificar(ModelMap modelo,@PathVariable String id, @RequestParam String titulo,@RequestParam Integer anio, @RequestParam String nombreAut,@RequestParam String nombreEdit)throws Exception{
        try{
            servLibro.modificarLibro(null,id,titulo,anio,nombreAut,nombreEdit);
            //modelo.put("exito","Modificación exitosa"); 
            //return "list-libro"; Profe en clase thy pone este return pero se lo devuelve vacío min 1:57
            return "list-libro";
        }catch(MiExcepcion ex){
            modelo.put("error",ex.getMessage());
            modelo.put("id",id);
           // modelo.put("archivo",archivo);
            modelo.put("titulo",titulo);
            modelo.put("anio",anio);
            modelo.put("nombreAut",nombreAut);
            modelo.put("nombreEdit",nombreEdit);
            //return "redirect:/libro/lista"; 
            return "modif-Libro";
            //return "redirect:/libro/modificar/{id}";  
        }
    }
    
    //Alta y Baja Clase THYMELEAF Tarde min 01:28:00
    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id,ModelMap modelo){
        try {
            servLibro.baja(id);
            return "redirect:/libro/lista";  
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/libro/lista"; 
        }
    }
    
    @GetMapping("/alta/{id}") 
    public String alta(@PathVariable String id,ModelMap modelo){
        try {
            servLibro.alta(id);
            return "redirect:/libro/lista";  
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage()); 
            return "redirect:/libro/lista"; 
        }
    }
    
    @GetMapping("/eliminar/{id}") //PATHVARIABLE
    public String eliminar(@PathVariable String id,ModelMap modelo) throws Exception{
        try {
            servLibro.eliminarLibro(id);
            return "redirect:/libro/lista";  
        }catch(MiExcepcion ex) {
            modelo.put("error", ex.getMessage()); //La profe no lo puso pero fijarme si anda
            return "redirect:/libro/lista"; 
        }
    }   
}
