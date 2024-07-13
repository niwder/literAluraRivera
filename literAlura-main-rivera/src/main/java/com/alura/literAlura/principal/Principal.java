package com.alura.literAlura.principal;

import com.alura.literAlura.model.Autor;
import com.alura.literAlura.model.DatoLibros;
import com.alura.literAlura.model.Datos;
import com.alura.literAlura.model.Libro;
import com.alura.literAlura.repository.AutorRepository;
import com.alura.literAlura.services.ConsumoAPI;
import com.alura.literAlura.services.ConvierteDatos;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

       private Scanner teclado = new Scanner(System.in);
        private ConsumoAPI consumoAPI = new ConsumoAPI();
        private ConvierteDatos conversor = new ConvierteDatos();
        private String URL_BASE = "https://gutendex.com/books/";
        private final AutorRepository repository;

        public Principal(AutorRepository repository) {
            this.repository = repository;
        }

        public void mostrarMenu() {
            var opciones = -1;
            var menu = """
            -------------------------
                  Menu Principal
            -------------------------
            1-Buscar Libros por Titulo
            2-Listar libros 
            3-Listar Autores
            4-Listar Autores Vivos
            5-Listar autores por Año
            6-Top 10 libros mas descargados
            7-generar estadisticas
            -------------------------
            0-Salir
            -------------------------
            Elija una Opcion:
            """;

            while (opciones != 0) {
                System.out.println(menu);
                try {
                    opciones = Integer.parseInt(teclado.nextLine());
                    switch (opciones) {
                        case 1:
                            buscarLibroTitulo();
                            break;

                        case 2:
                            listarLibrosRegistrados();
                            break;
                        case 3:
                            listarAutoresRegistrados();
                            break;
                        case 4:
                            listarAutoresVivos();
                            break;

                        case 5:
                            listarAutoresPoraño();
                            break;
                        case 6:
                            top10();
                            break;
                        case 7:
                            estadisticas();
                            break;
                        case 0:
                            System.out.println("Saliendo...");
                            break;
                        default:
                            System.out.println("Opcion no valida");
                            break;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Opcion no valida" + e.getMessage());
                }
            }
        }

        public void buscarLibroTitulo() {
            System.out.println("Ingresa el Titulo del libro que deseas buscar: ");
            var nombre = teclado.nextLine();
            var json = consumoAPI.obtenerJson(URL_BASE + "?search=" + nombre.replace(" ", "+").toLowerCase());
            System.out.println(json);

            if (!json.isEmpty() || !json.contains("\"count\":0,\"next\":null,\"previous\":null,\"results\":[]")) {
                {
                    var datos = conversor.obtenerDatos(json, Datos.class);
                    Optional<DatoLibros> libro = datos.libros().stream().findFirst();

                    if (libro.isPresent()) {
                        System.out.println(libro.get().toString());
                        try {
                            List<Libro> libroEncontrado = libro.stream().map(l -> new Libro(l)).collect(Collectors.toList());
                            Autor autorApi = libro.stream()
                                    .flatMap(l -> l.autores().stream()
                                            .map(a -> new Autor(a)))
                                    .collect(Collectors.toList()).stream().findFirst().get();

                            Optional<Autor> autorDataBase = repository.buscarAutorNombre(libro.get().autores()
                                    .stream().map(a -> a.nombre()).collect(Collectors.joining()));

                            Optional<Libro> librOptional = repository.buscarLibroNombre(nombre);
                            if (librOptional.isPresent()) {
                                System.out.println("Libro ya existente en la base de datos");
                            } else {
                                Autor autor;
                                if (autorDataBase.isPresent()) {
                                    autor = autorDataBase.get();
                                    System.out.println("Autor ya existente en la base de datos");
                                } else {
                                    autor = autorApi;
                                    repository.save(autor);
                                }
                                autor.setLibros(libroEncontrado);
                                repository.save(autor);
                            }
                        } catch (Exception e) {
                            System.out.println("Error en la base de datos: " + e.getMessage());
                        }
                    }
                }

            } else {
                System.out.println("Libro no encontrado o no existente");
            }
        }

        public void listarLibrosRegistrados() {
            System.out.println("Libros registrados: " + repository.buscarTodosLosLibros().size());
            List<Libro> libros = repository.buscarTodosLosLibros();
            libros.forEach(libro -> System.out.println(libro.toString()));
        }




        public void listarAutoresRegistrados() {
            System.out.println("Autores registrados: " + repository.findAll().size());
            List<Autor> autores = repository.findAll();
            autores.forEach(autor -> System.out.println(autor.toString()));
        }

        public void listarAutoresPoraño() {
            System.out.println("""
              -------------------------
              Seleccione la opcion
              -------------------------
              1 - Lista de autores por nacimiento
              2 - Lista de autores por fallecimiento
              """);

            try {
                var opcion = Integer.parseInt(teclado.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.println("Autores por nacimiento");
                        listarAutoresNacimiento();
                        break;
                    case 2:
                        System.out.println("Autores por fallecimiento");
                        listarAutoresFallecimiento();
                        break;
                    default:
                        System.out.println("No se reconoce la opcion");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error en el formato de fecha");
            }
        }

        public void listarAutoresFallecimiento() {
            System.out.println("Ingresa el año de fallecimiento del autor que deseas buscar: ");
            try {
                var fecha = Integer.valueOf(teclado.nextLine());
                List<Autor> autores = repository.listarAutoresFallecimiento(fecha);
                if (!autores.isEmpty()) {
                    autores.forEach(System.out::println);
                } else {
                    System.out.println("No se encontraron autores");
                }
            } catch (Exception e) {
                System.out.println("Error en el formato de fecha");
            }
        }

        public void listarAutoresNacimiento() {
            System.out.println("Ingresa el año de nacimiento del autor que deseas buscar: ");
            try {
                var fecha = Integer.valueOf(teclado.nextLine());
                List<Autor> autores = repository.listarAutoresNacimiento(fecha);
                if (!autores.isEmpty()) {
                    autores.forEach(System.out::println);
                } else {
                    System.out.println("No se encontraron autores");
                }
            } catch (Exception e) {
                System.out.println("Error en el formato de fecha");
            }
        }

        public void listarAutoresVivos() {
            System.out.println("Ingresa el año para ver los autores vivos en esa fecha: ");
            try {
                var fecha = Integer.valueOf(teclado.nextLine());
                List<Autor> autores = repository.buscarAutoresVivos(fecha);
                if (!autores.isEmpty()) {
                    autores.forEach(System.out::println);
                } else {
                    System.out.println("No se encontraron autores");
                }
            } catch (Exception e) {
                System.out.println("Error en el formato de fecha");
            }
        }

        public void top10() {
            List<Libro> libros = repository.top10Libros();
            libros.forEach(System.out::println);
        }

        public void estadisticas() {
            System.out.println("Estadisticas obtenidas");
            var json = consumoAPI.obtenerJson(URL_BASE);
            var datos = conversor.obtenerDatos(json, Datos.class);
            IntSummaryStatistics est = datos.libros().stream()
                    .filter(l -> l.descargas() > 0)
                    .collect(Collectors.summarizingInt(DatoLibros::descargas));
            Integer media = (int) est.getAverage();
            System.out.println("""
                -------------------------
                Estadisticas de libros
                -------------------------
                """);
            System.out.println("Libros con descargas: " + est.getCount());
            System.out.println("Promedio de descargas: " + media);
            System.out.println("Maximo de descargas: " + est.getMax());
            System.out.println("Minimo de descargas: " + est.getMin());
        }

}
