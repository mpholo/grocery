package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.GroceryItem;
import com.mpholo.project.grocery.domain.MonthlyGrocery;
import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.mapper.GroceryBasketMapper;
import com.mpholo.project.grocery.mapper.MonthlyGroceryMapper;
import com.mpholo.project.grocery.mapper.ProductMapper;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.GroceryItemUpdateDTO;
import com.mpholo.project.grocery.model.MonthlyGroceryDTO;
import com.mpholo.project.grocery.model.ProductDTO;
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
    private final ProductService productService;
    private ProductMapper productMapper;
    private MonthlyGroceryService monthlyGroceryService;


    public GroceryItemServiceImpl(GroceryItemRepository groceryBasketRepository,
                                  GroceryBasketMapper groceryBasketMapper,
                                  MonthlyGroceryMapper monthlyGroceryMapper, ProductService productService, ProductMapper productMapper, MonthlyGroceryService monthlyGroceryService) {

        this.groceryBasketRepository = groceryBasketRepository;
        this.groceryBasketMapper = groceryBasketMapper;
        this.monthlyGroceryMapper = monthlyGroceryMapper;
        this.productService = productService;
        this.productMapper = productMapper;
        this.monthlyGroceryService = monthlyGroceryService;
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

    public GroceryItemDTO save(GroceryItemUpdateDTO groceryItemUpdateDTO) {

        Optional<ProductDTO> productDTO = productService.findById(groceryItemUpdateDTO.getProductId());
        Optional<MonthlyGroceryDTO> monthlyGroceryDTO=monthlyGroceryService.findById(groceryItemUpdateDTO.getMonthlyGroceryId());

        if (!productDTO.isPresent() && !monthlyGroceryDTO.isPresent()) {

           throw  new RuntimeException("Product or monthly grocery not found");
        }

        Product product = productMapper.ProductDTOToProduct(productDTO.get());
        MonthlyGrocery monthlyGrocery=monthlyGroceryMapper.monthlyGroceryDTOToMonthlyGrocery(monthlyGroceryDTO.get());

        GroceryItem groceryItem = new GroceryItem();
        //when updating grocery item
        if(groceryItemUpdateDTO.getGroceryItemId()!=null)
        {
            groceryItem.setGroceryItemId(groceryItemUpdateDTO.getGroceryItemId());
        }

        groceryItem.setActualPrice(groceryItemUpdateDTO.getActualPrice());
        groceryItem.setMonthlyGrocery(monthlyGrocery);
        groceryItem.setProduct(product);
        groceryItem.setQuantity(groceryItemUpdateDTO.getQuantity());

        return saveAndReturnDTO(groceryItem);

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
