package com.reyeda.literatura.service;

import java.util.List;

import com.reyeda.literatura.model.Libro;
import com.reyeda.literatura.repository.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

public class GutendexService {

    @Autowired
    private LibroRepositorio libroRepository;

    private final String API_URL = "https://gutendex.com/books?search=";

    public Libro buscarYRegistrarLibro(String titulo) {
        if (libroRepository.existsByTitulo(titulo)) {
            throw new RuntimeException("El libro ya está registrado.");
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + titulo;
        GutendexResponse response = restTemplate.getForObject(url, GutendexResponse.class);

        if (response == null || response.getResults().isEmpty()) {
            throw new RuntimeException("Libro no encontrado.");
        }

        GutendexResponse.Book book = response.getResults().get(0);
        Libro libro = new Libro();
        libro.setTitulo(book.getTitle());
        libro.setAutorApellido(book.getAuthors().get(0).getLastName());
        libro.setAutorNombre(book.getAuthors().get(0).getFirstName());
        libro.setIdioma(book.getLanguage());
        libro.setNumeroDescargas(book.getDownloadCount());

        return libroRepository.save(libro);
    }

    static class GutendexResponse {
        private List<Book> results;

        public List<Book> getResults() {
            return results;
        }

        public void setResults(List<Book> results) {
            this.results = results;
        }

        static class Book {
            private String title;
            private List<Author> authors;
            private String language;
            private int downloadCount;

            // Getters y setters

            static class Author {
                private String firstName;
                private String lastName;

                // Getters y setters
            }
        }
    }
}
