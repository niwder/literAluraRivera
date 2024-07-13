package com.alura.literAlura.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DatoLibros(
        @JsonAlias("id") Long id,
        @JsonAlias("id") Long libroId,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatoAutor> autores,
        //@JsonAlias("languages")List<String> idiomas,
        @JsonAlias("copyright") String copyright,
        @JsonAlias("download_count") Integer descargas) {

}