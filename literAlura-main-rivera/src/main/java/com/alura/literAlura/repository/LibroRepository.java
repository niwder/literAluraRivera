package com.alura.literAlura.repository;

import com.alura.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByTitulo(String titulo);
    Libro findByTituloContainsIgnoreCase(String titulo);

    public Optional<Libro> findById(Long id);

    @Query("SELECT l FROM Libro l ORDER BY l.descargas DESC LIMIT 10")
    List<Libro> findTop10ByTituloByDescargas();
//    @Query("select distinct l.languages from Libro l")
//    List<String> findAllLanguages();

//    @Query("select l from Libro l where l.languages=:languages")
//    List<Libro> findByLanguagesWhereOpcion(String languages);
}
