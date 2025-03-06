package com.example.andaluciaskills.service;

import com.example.andaluciaskills.dto.EvaluacionItemDTO;
import com.example.andaluciaskills.dto.EvaluacionResponse;
import com.example.andaluciaskills.dto.ItemDTO;
import com.example.andaluciaskills.model.Evaluacion;
import com.example.andaluciaskills.model.EvaluacionItem;
import com.example.andaluciaskills.model.Item;
import com.example.andaluciaskills.model.Participante;
import com.example.andaluciaskills.model.Prueba;
import com.example.andaluciaskills.repository.EvaluacionItemRepository;
import com.example.andaluciaskills.repository.EvaluacionRepository;
import com.example.andaluciaskills.repository.ItemRepository;
import com.example.andaluciaskills.repository.ParticipanteRepository;
import com.example.andaluciaskills.repository.PruebaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionItemService {

    @Autowired
    private EvaluacionItemRepository evaluacionItemRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private PruebaRepository pruebaRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<EvaluacionItem> findAll() {
        return evaluacionItemRepository.findAll();
    }

    public Optional<EvaluacionItem> findById(Long id) {
        return evaluacionItemRepository.findById(id);
    }

    public EvaluacionItem save(EvaluacionItem evaluacionItem) {
        return evaluacionItemRepository.save(evaluacionItem);
    }

    public void delete(Long id) {
        evaluacionItemRepository.deleteById(id);
    }
    
    public EvaluacionResponse evaluar(EvaluacionItemDTO evaluacionItemDTO) {

        var evaluacionId = evaluacionItemDTO.getEvaluacionId();
        var participanteId = evaluacionItemDTO.getParticipanteId();
        var pruebaId = evaluacionItemDTO.getPruebaId();
        
        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId).get();
        Participante participante = participanteRepository.findById(participanteId).get();
        Prueba prueba = pruebaRepository.findById(pruebaId).get();

        // Inicializar una variable para almacenar la nota final
        Float notaFinal = 0.0f;

        for (ItemDTO item : evaluacionItemDTO.getItems()) {
            
            Optional<Item> itemOpt = itemRepository.findById(item.getItemId());

            if (itemOpt.isPresent()) {
                EvaluacionItem evaluacionItem = new EvaluacionItem();
                evaluacionItem.setEvaluacion(evaluacion);
                evaluacionItem.setItem(itemOpt.get());
                evaluacionItem.setValoracion(item.getValoracion());
                evaluacionItem.setComentario(item.getComentario());
    
                evaluacionItemRepository.save(evaluacionItem);
    
                // Sumar la puntuación del ítem a la nota final
                notaFinal += evaluador(List.of(item));
            }
        }
        
        evaluacion.setNotaFinal(notaFinal);
        evaluacionRepository.save(evaluacion);
        return EvaluacionResponse.builder()
        .total(notaFinal)
        .mensaje( "Evaluación exitosa de " + participante.getNombre() + " en " + prueba.getEnunciado())
        .build();

    }

    // Método que calcula la suma total
    private static Float evaluador(List<ItemDTO> itemsValoraciones) {

        var puntuaciones = itemsValoraciones.stream()
                .map(item -> item.getValoracion())
                .toList();
    
        return puntuaciones.stream()
                           .map(p -> switch (p) {
                               case 0 -> 0.0f;
                               case 1 -> 0.25f;
                               case 2 -> 0.50f;
                               case 3 -> 0.75f;
                               case 4 -> 1.0f;
                               default -> throw new IllegalArgumentException("Valor de puntuación no válido: " + p);
                           })
                           .reduce(0.0f, Float::sum);  // Cambié la suma para devolver un Float
    }
}
