package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.mapper.GroceryBasketMapper;
import com.mpholo.project.grocery.mapper.MonthlyGroceryMapper;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.repositories.GroceryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroceryItemServiceImpl implements GroceryItemService {

    private final GroceryItemRepository groceryBasketRepository;
    private GroceryBasketMapper groceryBasketMapper;
    private MonthlyGroceryMapper monthlyGroceryMapper;


    public GroceryItemServiceImpl(GroceryItemRepository groceryBasketRepository, GroceryBasketMapper groceryBasketMapper, MonthlyGroceryMapper monthlyGroceryMapper) {
        this.groceryBasketRepository = groceryBasketRepository;
        this.groceryBasketMapper = groceryBasketMapper;
        this.monthlyGroceryMapper = monthlyGroceryMapper;
    }

    @Override
    public Optional<GroceryItemDTO> findById(Integer id) {
        Optional<GroceryItem> groceryBasket=groceryBasketRepository.findById(id);

        if(groceryBasket.isPresent()) {
            return Optional.of(groceryBasketMapper.groceryBasketToGroceryBasketDTO(groceryBasket.get()));
        }

        return  Optional.empty();
    }

    @Override
    public List<GroceryItemDTO> findAll() {
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
    public void delete(GroceryItemDTO groceryBasketDTO) {

        groceryBasketRepository.delete(groceryBasketMapper.groceryBasketDTOToGroceryBasket(groceryBasketDTO));
    }

    @Override
    public GroceryItemDTO save(GroceryItemDTO groceryBasketDTO) {

        GroceryItem groceryBasket=groceryBasketMapper.groceryBasketDTOToGroceryBasket(groceryBasketDTO) ;
        return saveAndReturnDTO(groceryBasket);
    }


    @Override
    public GroceryItemDTO edit(Integer id, GroceryItemDTO groceryBasketDTO) {
        GroceryItem groceryBasket = groceryBasketMapper.groceryBasketDTOToGroceryBasket(groceryBasketDTO);
        groceryBasket.setGroceryItemId(id);
        return saveAndReturnDTO(groceryBasket);
    }

    @Override
    public List<GroceryItemDTO> findByMonthlyGrocery(MonthlyGroceryDTO monthlyGroceryDTO) {
        return groceryBasketRepository.findByMonthlyGrocery(
                monthlyGroceryMapper.monthlyGroceryDTOToMonthlyGrocery(monthlyGroceryDTO))
                .stream()
                .map(groceryBasketMapper::groceryBasketToGroceryBasketDTO)
                .collect(Collectors.toList());
    }

    private GroceryItemDTO saveAndReturnDTO(GroceryItem groceryBasket) {
        GroceryItem savedGroceryBasket= groceryBasketRepository.save(groceryBasket);
        return  groceryBasketMapper.groceryBasketToGroceryBasketDTO(savedGroceryBasket);
    }
}
