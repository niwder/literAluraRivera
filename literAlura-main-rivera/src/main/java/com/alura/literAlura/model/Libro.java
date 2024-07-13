package com.alura.literAlura.model;

import java.util.stream.Collectors;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long libroId;
    @Column(unique = true)
    private String titulo;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // Asegura que el autor se guarde automáticamente
    @JoinColumn(name = "autor_id")
    private Autor autor;
   // @Enumerated(EnumType.STRING)

    private String copyright;
    @Column (name = "descargas")
    private Integer descargas;
    // @ManyToOne

    public Libro() {
    }

    public Libro(DatoLibros libros) {
        //this.id = libros.id();

        this.libroId = libros.id();

        this.titulo = libros.titulo();
        if (libros.autores() != null && !libros.autores().isEmpty()) {
            this.autor = new Autor(libros.autores().get(0)); // Toma el primer autor de la lista
        } else {
            this.autor = null; // o maneja el caso de que no haya autor
        }
        this.copyright = libros.copyright();
        this.descargas = libros.descargas();

    }
    public Libro(Libro libro){

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return """
               ------------ Libro ------------
               id=""" + id
                + "  Libro id=" + libroId +
                ", titulo='" + titulo + '\''
                //+ ", idioma=" + idioma
                + ", copyright='" + copyright + '\''
                + ", descargas=" + descargas
                + ", autor=" + autor
                + "\n-----------------------------------\n";
    }

}