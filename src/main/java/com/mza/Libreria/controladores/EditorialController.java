package com.mza.Libreria.controladores;

import com.mza.Libreria.entidades.Editorial;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mza.Libreria.excepciones.MiExcepcion;
import com.mza.Libreria.servicios.ServiceEditorial;
import com.mza.Libreria.servicios.ServiceLibro;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *
 * @author Lautaro Pavez
 */
@Controller 
@RequestMapping("/editorial") //localhost:8080/editorial   //Clase 2 Mañana
public class EditorialController {

    @Autowired
    private ServiceEditorial servEditorial; 
    
    @Autowired
    private ServiceLibro servLibro; 
    
    @GetMapping("/registroEditorial")
    public String formulario() {
        return "nuevaEditorial";
    }

    @PostMapping("/registrarEditorial") 
    public String crear(ModelMap modelo,@RequestParam String nombre){ 
        try{
            servEditorial.crearEditorial(nombre);
            modelo.put("exito","¡Has creado una nueva Editorial!"); 
            //return "redirect:/libro/registro";
            return "nuevaEditorial";
        }catch(MiExcepcion ex){
            modelo.put("error", ex.getMessage());
            modelo.put("nombre",nombre);
            return "nuevaEditorial"; 
        }  
    }
     
    @GetMapping("/lista")
    public String lista(ModelMap modelo){
        List<Editorial> editorialesLista = servEditorial.buscaTodas();
        modelo.addAttribute("editoriales",editorialesLista); //Utilizo una llave("libros") y lo que viaja como valor es la lista librosLista
         return "list-editorial"; // 
    }
    
    @GetMapping("/alta/{id}") 
    public String alta(@PathVariable String id,ModelMap modelo){
        try {
            servLibro.altaxEditorial(id);
            return "redirect:/editorial/lista";  
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage()); 
            return "redirect:/editorial/lista"; 
        }
    }
    
    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id,ModelMap modelo){
        try {
            servLibro.bajaxEditorial(id);
            return "redirect:/editorial/lista";  
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:/editorial/lista"; 
        }
    }
    
    @GetMapping("/eliminar/{id}") //PATHVARIABLE
    public String eliminar(@PathVariable String id,ModelMap modelo) throws Exception{
        try {
            servLibro.eliminarEditorial(id);
            return "redirect:/editorial/lista";  
        }catch(MiExcepcion ex) {
            modelo.put("error", ex.getMessage()); 
            return "redirect:/editorial/lista"; 
        }
    }   
    
}
