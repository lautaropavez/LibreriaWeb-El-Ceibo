package com.mza.Libreria.controladores;

import com.mza.Libreria.excepciones.MiExcepcion;
import com.mza.Libreria.servicios.ServiceUsuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *
 * @author Lautaro Pavez
 */
@Controller
@RequestMapping("/") //localhost:8080/
public class MainController {

    @Autowired //En video 2 MVC l agregamos
    private ServiceUsuario servUsuario;
    
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo){
        if(error != null){
            modelo.put("error","Nombre de usuario o clave incorrectos");
        }
        return "login.html";
    }
    @GetMapping("/registro")
    public String registro(){
        return "registro.html";
    }
    
    //Esto va a recibir los parámetros, los lleva al service, el service ejecuta su lógica, si hay algún error lo va a imprimir solo y va a volver al registro, y sino hay error va a ir al index
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo,@RequestParam String nombre,@RequestParam String apellido,@RequestParam String mail,@RequestParam String clave1,@RequestParam String clave2){
        try {
            servUsuario.registrar(nombre,apellido,mail,clave1,clave2);
            
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage()); // Este modelo lo vamos a utilizar para enviar en este caso el mensaje de error en la pantalla.
            
            modelo.put("nombre",nombre);
            modelo.put("apellido",apellido);
            modelo.put("mail",mail);
            modelo.put("clave1",clave1);
            modelo.put("clave2",clave2);
            //Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex); //Con esto nos tira el error por consola, lo podemos sacar
            return "registro.html"; //Si ocurre un error retorna el registro 
            
        }
        //Forma profe Clase Thymeleaft 
        modelo.put("exito","¡¡Registro exitoso!!");
        return "registro.html"; //Si está todo ok retorna el registro y ya pusimos un div con los mensajes de exito cuando ingrese un usuario
        //Forma profe Videos MVC 2
        //modelo.put("titulo","¡¡Bienvenido a Libreria El Ceibo!!");
        //modelo.put("descripcion","Tu usuario fue registrado de manera satisfactoria");
        //return "exito.html"; //Si esta todo ok retorna la pag principal, en vez de el registro de nuevo
        //Forma Lauti hacerlo con una popup
    }
    
    
}
