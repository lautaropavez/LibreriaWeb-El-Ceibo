
package com.mza.Libreria.servicios;

import com.mza.Libreria.entidades.Usuario;
import com.mza.Libreria.enumeradores.Rol;
import com.mza.Libreria.excepciones.MiExcepcion;
import com.mza.Libreria.repositorios.UsuarioRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Lautaro Pavez
 */
@Service
//public class ServiceUsuario implements UserDetailsService{
public class ServiceUsuario{     //Sin Spring Security
    @Autowired
    private UsuarioRepository usuarioRepo;
    
    @Autowired
    private ServiceNotificacion sNotific;
    
    @Transactional
    public void registrar(String nombre,String apellido,String mail,String clave, String clave2) throws MiExcepcion{
   
        validacion(nombre,apellido,mail,clave,clave2);
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setRol(Rol.USUARIO);
//        String encriptada = new BCryptPasswordEncoder().encode(clave);
//        usuario.setClave(encriptada);//Al usuario cuando lo persistimos en la BD lo persistimos con la clave encriptada 
        
        usuario.setClave(clave);
        
        usuario.setAlta(new Date());
        usuario.setBaja(null);
        
        usuarioRepo.save(usuario);
        
        //sNotific.enviarEmail("Bienvenidos a Biblioteca Virtual", "Libreria Web", usuario.getMail()); //en video 2 de mvc la comentamos pq no hemos configurado un servidor de correo todavia
    }
    
    @Transactional //Hacer lo mismo que en ServiceLibro de verificar de que hayan cambios antes de setear nuevamente
    public void modificar(String id,String nombre,String apellido,String mail,String clave,String clave2) throws MiExcepcion{
        
        validacion(nombre,apellido,mail,clave,clave2);
        
        Optional<Usuario> respuesta = usuarioRepo.findById(id); //Este método nos devuelve una clase Optional como respuesta 
        if(respuesta.isPresent()){ //Y nos dice si el resultado está presente, osea si encontró un resultado con este id
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
//            String encriptada = new BCryptPasswordEncoder().encode(clave);
//            usuario.setClave(encriptada);
            usuario.setClave(clave);

            
            usuarioRepo.save(usuario);
        }else{
         throw new MiExcepcion("No se encontró el usuario ingresado");   
        }  
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorId(String id){
        Usuario u = usuarioRepo.buscarPorId(id);
        //Manera más corta si manejo la excepcion en controlador
        return usuarioRepo.buscarPorId(id);
//        if (u != null) {
//            return u;
//        } else {
//            return null; //throw new MiExcepcion("Este libro no existe."); esto debo ponerlo en el controlador
//        }
    }
    
    
    @Transactional
    public void deshabilitar(String id)throws MiExcepcion{
        
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if(respuesta.isPresent()){ 
            Usuario usuario = respuesta.get();
            if(usuario.getBaja() == null){ //Lo mejoré igual que habilitar
                usuario.setBaja(new Date());
                usuarioRepo.save(usuario);
            }
        }else{
         throw new MiExcepcion("No se encontró el usuario ingresado");   
        }  
    }
    
    @Transactional
    public void habilitar(String id)throws MiExcepcion{
        
        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if(respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            if(usuario.getBaja() != null){ //Lo mejoré haciendo que si no está de baja no haga nada(si ya está de alta no le ponga el null a baja pq ya es null)
                usuario.setBaja(null);          
                usuarioRepo.save(usuario);
            }
        }else{
         throw new MiExcepcion("No se encontró el usuario ingresado");   
        }
    }
    
    @Transactional
    public void cambiarRol(String id)throws MiExcepcion{
        Optional<Usuario> respuesta = usuarioRepo.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.USUARIO)) {
                usuario.setRol(Rol.ADMIN);
            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USUARIO);
            }
        }
    }
    
    @Transactional
    public void eliminar(String id)throws Exception{
        Usuario u = usuarioRepo.buscarPorId(id);
        if (u != null) {
            usuarioRepo.deleteById(id);
        } else {
            throw new Exception("No se encontró a este usuario en la base de datos");
        }
    }
    
    @Transactional(readOnly = true) //Busca todos los usuarios activos e inactivos (para el administrador)
    public List<Usuario> listarTodos() {
        return usuarioRepo.findAll();
    }
    
    @Transactional(readOnly = true) //Busca todos los usuarios que esten activos, es distinto a listaActivos de libro porque en libro solo pusimos un boolean activo, acá dos fechas de activo e inactivo
    public List<Usuario> listaActivos(Date baja) {
        return usuarioRepo.listaActivos(baja);
    }
    public void validacion(String nombre,String apellido,String mail,String clave1,String clave2) throws MiExcepcion {

        if (nombre == null || nombre.isEmpty()) {
            throw new MiExcepcion("Debe indicar el nombre");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MiExcepcion("Debe indicar el apellido");
        }
        if (mail == null || mail.isEmpty()) {
            throw new MiExcepcion("Debe indicar el correo electrónico");
        }
        if (mail.contains("@") == false) {
                throw new MiExcepcion("El correo electrónico es incorrecto");
        }
        if (clave1 == null || clave1.trim().isEmpty()) {
            throw new MiExcepcion("Debe indicar la clave");
        }
        if (clave1.length() < 8) {
            throw new MiExcepcion("La clave no puede tener menos de 8 caracteres");
        }
        
        if(!clave1.equals(clave2)) {
            throw new MiExcepcion("Las claves deben ser iguales");
        }
    }

    // El método loadUserByUsernamees llamado cuando el usuario quiera autentificarse en la plataforma. Cuando un usuario tiene el formulario para autentificarse(formulario login), 
    // lo que hace Spring Security es llamar a este método de este servicio. El método busca el usuario por el mail y si existe es crearle estos tres permisos y va a pasarle 
    // a Spring security los datos del usuario, la clave y los permisos a los que tiene acceso ese usuario. 
    // Si nosotros trabajamos con más de un rol, por ejemplo un administrador que pudiese crear personas, en ese caso deberiamos a través de algún atributo de usuario, 
    // determinar que tipo de usuario es y dependiendo de eso, que permiso le asignamos. Por el momento lo dejamos así
    // El método recibe un nombre de usuario y lo transforma en un usuario de Spring Security
//    @Override
//    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
//        Usuario usuario = usuarioRepo.buscarPorMail(mail); 
//        if (usuario != null){ //si existe(si es distinto de null) lo que vamos a hacer es convertirlo en un usuario del dominio spring
//            List<GrantedAuthority> permisos = new ArrayList<>(); //Creo una lista de permisos
//            
//            //Creo una lista de permisos! 
//            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());//Creamos permisos para un usuario común
//            permisos.add(p1);
//            
//            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO PORTADAS"); //CHEQUEAR SI DEJAR O NO
//            permisos.add(p2); //Incluimos en la lista de permisos las variables que acabamos de crear
//
//            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
//            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//            HttpSession session = attr.getRequest().getSession(true);
//
//            session.setAttribute("usuariosession", usuario); // llave + valor
//           
//            User user = new User(usuario.getMail(),usuario.getClave(), permisos); //El constructor de usuarios de Spring security nos pide: nombre de usario, clave, listado de permisos
//           
//            return user;
//        }else{
//            return null;
//        } 
//    }
}
