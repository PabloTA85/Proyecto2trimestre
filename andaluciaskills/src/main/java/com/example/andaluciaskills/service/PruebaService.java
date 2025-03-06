package com.example.andaluciaskills.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.andaluciaskills.model.Item;
import com.example.andaluciaskills.model.Prueba;
import com.example.andaluciaskills.repository.PruebaRepository;






@Service
public class PruebaService {

    @Autowired
    private PruebaRepository pruebaRepository;

    private static final String UPLOAD_DIR = "uploads/pdfs/";

    public List<Prueba> findAll() {
        return pruebaRepository.findAll();
    }

    public Optional<Prueba> findById(Long id) {
        return pruebaRepository.findById(id);
    }

    public Prueba save(Prueba prueba) {
        return pruebaRepository.save(prueba);
    }

    public Optional<Prueba> findByEnunciado(String enunciado) {
        return pruebaRepository.findByEnunciado(enunciado);
    }

    public void delete(Long id) {
        pruebaRepository.deleteById(id);
    }

    // Método para obtener los items de una prueba
    public List<Item> findItemsByPruebaId(Long idPrueba) {
        Optional<Prueba> pruebaOpt = pruebaRepository.findById(idPrueba);
        if (pruebaOpt.isPresent()) {
            return pruebaOpt.get().getItems();
        }
        return List.of();
    }


    public Prueba guardarPdf(Long idPrueba, MultipartFile archivoPdf) throws IOException {
        // Verificar si la prueba existe
        Optional<Prueba> pruebaOptional = pruebaRepository.findById(idPrueba);
        if (pruebaOptional.isEmpty()) {
            throw new RuntimeException("Prueba no encontrada");
        }

        Prueba prueba = pruebaOptional.get();

        // Verifica si el archivo está vacío
        if (archivoPdf.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        // Almacenar el archivo PDF en formato de bytes
        prueba.setPdfData(archivoPdf.getBytes());

        // Guardar la prueba con el archivo PDF en la base de datos
        return pruebaRepository.save(prueba);
    }

    public List<Prueba> findByEspecialidad(Long idEspecialidad) {
        return pruebaRepository.findByEspecialidad_IdEspecialidad(idEspecialidad); // Ajusta el nombre aquí
    }
    

    // Método que recibe la lista de puntuaciones y devuelve la suma total
    public static double calcularNota(List<Integer> puntuaciones) {
        double notaFinal = 0.0;

        // Mapeo de la puntuación según la respuesta seleccionada (0-4)
        for (int puntuacion : puntuaciones) {
            switch (puntuacion) {
                case 0:
                    notaFinal += 0.0;
                    break;
                case 1:
                    notaFinal += 0.25;
                    break;
                case 2:
                    notaFinal += 0.50;
                    break;
                case 3:
                    notaFinal += 0.75;
                    break;
                case 4:
                    notaFinal += 1.0;
                    break;
                default:
                    System.out.println("Valor de puntuación no válido: " + puntuacion);
            }
        }

        return notaFinal;
    }
    
    // Método que recibe la lista de puntuaciones y devuelve la suma total
    public static double evaluador(List<Integer> puntuaciones) {
        return puntuaciones.stream()
                           .mapToDouble(p -> switch (p) {
                               case 0 -> 0.0;
                               case 1 -> 0.25;
                               case 2 -> 0.50;
                               case 3 -> 0.75;
                               case 4 -> 1.0;
                               default -> throw new IllegalArgumentException("Valor de puntuación no válido: " + p);
                           })
                           .sum();
    }

}