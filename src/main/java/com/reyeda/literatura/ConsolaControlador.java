package com.reyeda.literatura;

import com.reyeda.literatura.model.Libro;
import com.reyeda.literatura.repository.LibroRepositorio;
import com.reyeda.literatura.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsolaControlador implements CommandLineRunner {

    @Autowired
    private GutendexService gutendexService;

    @Autowired
    private LibroRepositorio libroRepository;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = Integer.parseInt(scanner.nextLine());
            switch (opcion) {
                case 1 -> buscarYRegistrarLibro();
                case 2 -> listarLibros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLibrosPorIdioma();
                case 0 -> salir = true;
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("Menú:");
        System.out.println("1. Buscar y registrar libro");
        System.out.println("2. Listar libros");
        System.out.println("3. Listar autores");
        System.out.println("4. Listar autores vivos en un año determinado");
        System.out.println("5. Listar libros por idioma");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void buscarYRegistrarLibro() {
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        try {
            Libro libro = gutendexService.buscarYRegistrarLibro(titulo);
            System.out.println("Libro registrado: " + libro.getTitulo());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void listarLibros() {
        List<Libro> libros = libroRepository.findAll();
        libros.forEach(libro -> System.out.println(libro.getTitulo()));
    }

    private void listarAutores() {
        libroRepository.findAll().stream()
                .map(libro -> libro.getAutorApellido() + ", " + libro.getAutorNombre())
                .distinct()
                .forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        // Implementar lógica para listar autores vivos en un año determinado
    }

    private void listarLibrosPorIdioma() {
        System.out.print("Ingrese el idioma (ES, EN, FR, PT): ");
        String idioma = scanner.nextLine();
        libroRepository.findAll().stream()
                .filter(libro -> libro.getIdioma().equalsIgnoreCase(idioma))
                .forEach(libro -> System.out.println(libro.getTitulo()));
    }
}
