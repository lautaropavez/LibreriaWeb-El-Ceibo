package com.mza.Libreria;

import com.mza.Libreria.servicios.ServiceNotificacion;
import com.mza.Libreria.servicios.ServiceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.servlet.ViewResolver;

@SpringBootApplication
//public class LibreriaWebApplication extends WebSecurityConfigurerAdapter {
public class LibreriaWebApplication{    
    
    @Autowired
    private ServiceNotificacion servNotif;
    
//    @Autowired
//    private ServiceUsuario servUsuario;

    public static void main(String[] args) {
       SpringApplication.run(LibreriaWebApplication.class, args);
    }
    
    // Este método le dice a la configuración de Spring Security cuál es el servicio que vamos a utilizar para autentificar el usuario y le va a setear 
    // un encriptador de contraseñas a ese servicio, osea que cada vez que se chequee una clave va a usarse este encriptador
//    @Autowired
//    public void ConfigureGlobal(AuthenticationManagerBuilder auth)throws Exception{
//         auth
//                .userDetailsService(servUsuario)
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }
 
    
    //MAIL
//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail(){
//        servNotif.enviarEmail("Hola Gaspar...","Mensaje para Gaspar","lautapavez@gmail.com");
//    } 
}
