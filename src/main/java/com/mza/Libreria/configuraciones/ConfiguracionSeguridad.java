//package com.mza.Libreria.configuraciones;
//
//import com.mza.Libreria.servicios.ServiceUsuario;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class ConfiguracionSeguridad extends WebSecurityConfigurerAdapter {
//    
//    // Acá tenemos una instancia del servicio de Usuario, el cual vamos a utilizar para buscar en la BBDD algún usuario por nombre de usuario
//    @Autowired
//    public ServiceUsuario servUsuario;
//    
//    /**
//    * Acá tenemos una configuración del manejador de seguridad Spring Security al cuál le decimos cual es el servicio que debe utilizar para autentificar un usuario
//    * Luego una vez que utiliza nuestro servicio para buscar un usuario en la base de datos, cual es el encoder que va a utilizar para comparar las contraseñas
//    * Nosotros cuando registramos un usuario a través de nuestro servicio, la contraseña que el usuario ingreso en el registro, nosotros la encriptamos a traves 
//    * de un encoder.
//    * Cuando un usuario va a loguearse para ingresar a la plataforma, al llenar su usuario y su clave en texto plano, Spring Security va a tomar ese texto plano y 
//    * la va a encriptar a traves del encoder que le pasamos acá, obviamente los dos encoder deben ser los mismos para que cuando se transformen las contraseñas sean
//    * iguales
//    */ 
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth)throws Exception{
//        auth
//            .userDetailsService(servUsuario).
//            passwordEncoder(new BCryptPasswordEncoder());
//    }
//    
//    /**
//    * Método para Configurar la Seguridad Web, en donde indico las rutas y los
//    * parametros principales
//    *
//    * @param http HttpSecurity
//    * @throws Exception
//    */   
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeRequests()
//                .antMatchers("/css/*", "/js/*", "/img/*", "/**") //Acá le digo que los recursos que estén dentro de estas carpetas, pueda acceder cualquier usuario sin estar logueado o necesidad de algún permiso
//                .permitAll() 
//                .and().formLogin()
//                .loginPage("/login") // En que URL está mi formulario de login
//                .loginProcessingUrl("/logincheck") //Acá va cuál va a ser la URL que va a utilizar Spring Security, esta URL es la que hay que usar en el formulario login de la pagina login.html
//                .usernameParameter("username") // Como viajan los datos del logueo (usamos username pero podemos usar cualquiera)
//                .passwordParameter("password")// Como viajan los datos del logueo (usamos password pero podemos usar cualquiera)
//                .defaultSuccessUrl("/inicio") // A que URL viaja si el usuario se autentico con éxito
//                .permitAll()
//                .and().logout() // Aca configuro la salida
//                .logoutUrl("/logout") // Cuando el usuario ingrese a "/logout", Spring security deberia desloguear al usuario del sistema
//                .logoutSuccessUrl("/") // A que URL nos va a dirigir
//                .permitAll().and().csrf().disable();
//    } 
//}