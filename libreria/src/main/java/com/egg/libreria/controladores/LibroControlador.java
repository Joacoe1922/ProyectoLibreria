package com.egg.libreria.controladores;

import java.util.List;

import com.egg.libreria.entidades.Autor;
import com.egg.libreria.entidades.Editorial;
import com.egg.libreria.entidades.Libro;
import com.egg.libreria.entidades.Prestamo;
import com.egg.libreria.servicios.AutorServicio;
import com.egg.libreria.servicios.EditorialServicio;
import com.egg.libreria.servicios.LibroServicio;
import com.egg.libreria.servicios.PrestamoServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @Autowired
    private PrestamoServicio prestamoServicio;

    @GetMapping("/registrolibro")
    public String formulario(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarTodos();
        modelo.addAttribute("autores", autores);

        List<Editorial> editoriales = editorialServicio.listarTodos();
        modelo.addAttribute("editoriales", editoriales);

        return "registrolibro";
    }

    @PostMapping("/registrolibro")
    public String guardar(ModelMap modelo, @RequestParam String isbn, @RequestParam String titulo,
            @RequestParam int anio, @RequestParam int ejemplares, @RequestParam String autor,
            @RequestParam String editorial) {

        try {
            libroServicio.crear(isbn, titulo, anio, ejemplares, autor, editorial);

            modelo.put("exito", "Registro realizado con éxito!");

            return "registrolibro";
        } catch (Exception e) {
            modelo.put("error", "Faltó algún dato!");

            return "registrolibro";
        }
    }

    @GetMapping("/lista-libros")
    public String lista(ModelMap modelo) {

        List<Libro> libros = libroServicio.listarTodos();

        modelo.addAttribute("libros", libros);

        return "lista-libros";
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {
        try {
            libroServicio.habilitar(id);

            return "redirect:/libro/lista-libros";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {
        try {
            libroServicio.deshabilitar(id);

            return "redirect:/libro/lista-libros";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/registrolibromodif/{id}")
    public String formularioModif(@PathVariable String id, ModelMap modelo) {

        modelo.put("libro", libroServicio.getOne(id));

        List<Autor> autores = autorServicio.listarTodos();
        modelo.addAttribute("autores", autores);

        List<Editorial> editoriales = editorialServicio.listarTodos();
        modelo.addAttribute("editoriales", editoriales);

        return "registrolibromodif";
    }

    @PostMapping("/registrolibromodif/{id}")
    public String formularioModif(ModelMap modelo, @PathVariable String id, @RequestParam String isbn,
            @RequestParam String titulo, @RequestParam int anio, @RequestParam int ejemplares,
            @RequestParam String autor, @RequestParam String editorial) {

        try {
            libroServicio.modificar(id, isbn, titulo, anio, ejemplares, autor, editorial);

            modelo.put("exito", "Modificación exitosa");

            return "redirect:/libro/lista-libros";
        } catch (Exception e) {
            modelo.put("error", "Falto algun dato");
            return "redirect:/libro/registrolibromodif";
        }
    }

    @PostMapping("/librobuscado")
    public String libroBuscado(ModelMap modelo, @RequestParam String titulo) {

        List<Libro> libroBuscado = libroServicio.buscarPorNombre(titulo);

        modelo.put("libros", libroBuscado);

        return "librobuscado";
    }

    @GetMapping("/prestamo")
    public String prestamo(ModelMap modelo, @RequestParam String id) {

        try {
            List<Libro> libros = libroServicio.listarTodos();
            modelo.addAttribute("libros", libros);

            modelo.addAttribute("id", id);

            return "prestamo";
        } catch (Exception e) {
            modelo.put("error", "Error al prestar un libro");

            return "prestamo";
        }

    }

    @PostMapping("/prestamo/{id}")
    public String prestar(ModelMap modelo, @PathVariable String id, @RequestParam String titulo) {

        try {
            prestamoServicio.prestar(id, titulo);

            modelo.put("exito", "Préstamo realizado con éxito!");

            List<Libro> libros = libroServicio.listarTodos();
            modelo.addAttribute("libros", libros);

            modelo.addAttribute("id", id);

            return "prestamo";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());

            return "prestamo";
        }
    }

    @GetMapping("/lista-prestamos")
    public String listarPrestamos(ModelMap modelo) {

        List<Prestamo> prestamos = prestamoServicio.listarTodosOrdenadosPorFechaDePrestamo();

        modelo.addAttribute("prestamos", prestamos);

        return "lista-prestamos";
    }

    @GetMapping("/devolver/{id}")
    public String devolver(ModelMap modelo, @PathVariable String id) {

        try {
            prestamoServicio.devolver(id);

            return "redirect:/libro/lista-prestamos";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());

            List<Prestamo> prestamos = prestamoServicio.listarTodos();

            modelo.addAttribute("prestamos", prestamos);
            return "lista-prestamos";
        }

    }
}
