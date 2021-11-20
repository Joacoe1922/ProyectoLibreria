package com.egg.libreria.servicios;

import java.util.ArrayList;
import java.util.List;

import com.egg.libreria.entidades.Usuario;
import com.egg.libreria.errores.ErrorServicio;
import com.egg.libreria.repositorios.UsuarioRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrar(long documento, String nombre, String apellido, String mail, String telefono, String clave,
            String clave2) throws ErrorServicio {
        
        validar(documento, nombre, apellido, mail, telefono, clave, clave2);

        Usuario usuario = new Usuario();
        usuario.setDocumento(documento);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setTelefono(telefono);

        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setAlta(true);

        usuarioRepositorio.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);

            // GrantedAuthority p2 = new
            // SimpleGrantedAuthority("ROL_USUARIO_ADMINISTRADOR");
            // permisos.add(p2);

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

    public void validar(long documento, String nombre, String apellido, String mail, String telefono, String clave,
            String clave2) throws ErrorServicio {

        if (documento == 0) {
            throw new ErrorServicio("El documento no puede ser cero");
        }

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo o estar vacío");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo o estar vacío");
        }

        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo o estar vacío");
        }

        if (telefono == null || telefono.isEmpty()) {
            throw new ErrorServicio("El telefono no puede ser nulo o estar vacío");
        }

        if (clave == null || clave.isEmpty()) {
            throw new ErrorServicio("La clave no puede ser nula o estar vacía");
        }

        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales");
        }
    }
}
