package com.mza.Libreria.servicios;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
/**
 *
 * @author Lautaro Pavez
 */
@Service
public class ServiceNotificacion {
    
//    @Autowired//(required=true)
//    private JavaMailSender mailSender;
    
    
    
    // En inglés establecemos los atributos como:
    // cuerpo = body, mail = toEmail, titulo = subject
//    @Async
//    public void enviarEmail(String cuerpo, String titulo, String mail){
//        SimpleMailMessage mensaje = new SimpleMailMessage();
//        mensaje.setTo(mail);
//        mensaje.setFrom("lautapavez@gmail.com"); //Quien lo manda
//        mensaje.setSubject(titulo); //Asunto
//        mensaje.setText(cuerpo); //Cuerpo del mensaje
//        
//        mailSender.send(mensaje); //Enviamos el mensaje
//        System.out.println("Holaaa, Mail enviado...");
//    }
}
