package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.GroceryBasket;
import com.mpholo.project.grocery.mapper.GroceryBasketMapper;
import com.mpholo.project.grocery.model.GroceryBasketDTO;
import com.mpholo.project.grocery.repositories.GroceryBasketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroceryBasketServiceImpl implements  GroceryBasketService   {

    private final GroceryBasketRepository groceryBasketRepository;
    private GroceryBasketMapper groceryBasketMapper;


    public GroceryBasketServiceImpl(GroceryBasketRepository groceryBasketRepository, GroceryBasketMapper groceryBasketMapper) {
        this.groceryBasketRepository = groceryBasketRepository;
        this.groceryBasketMapper = groceryBasketMapper;
    }

    @Override
    public Optional<GroceryBasketDTO> findById(Integer id) {
        Optional<GroceryBasket> groceryBasket=groceryBasketRepository.findById(id);

        if(groceryBasket.isPresent()) {
            return Optional.of(groceryBasketMapper.groceryBasketToGroceryBasketDTO(groceryBasket.get()));
        }

        return  Optional.empty();
    }

    @Override
    public List<GroceryBasketDTO> findAll() {
        return groceryBasketRepository.findAll()
                .stream()
                .map(groceryBasketMapper::groceryBasketToGroceryBasketDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {

        groceryBasketRepository.deleteById(id);
    }

    @Override
    public void delete(GroceryBasketDTO groceryBasketDTO) {

        groceryBasketRepository.delete(groceryBasketMapper.groceryBasketDTOToGroceryBasket(groceryBasketDTO));
    }

    @Override
    public void save(GroceryBasketDTO groceryBasketDTO) {
        groceryBasketRepository.save(groceryBasketMapper.groceryBasketDTOToGroceryBasket(groceryBasketDTO));
    }
}
