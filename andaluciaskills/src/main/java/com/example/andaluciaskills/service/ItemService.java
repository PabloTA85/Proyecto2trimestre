package com.example.andaluciaskills.service;

import com.example.andaluciaskills.model.Item;
import com.example.andaluciaskills.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findByEspecialidadId(Long id) {
        return itemRepository.findByEspecialidadId(id);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    // Método para guardar múltiples items
    public void saveAll(List<Item> items) {
        itemRepository.saveAll(items); // Esto guarda todos los ítems a la vez
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
