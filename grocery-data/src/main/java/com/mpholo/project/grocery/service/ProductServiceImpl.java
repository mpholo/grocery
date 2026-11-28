package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.mapper.ProductMapper;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.repositories.ProductRepository;
import com.mpholo.project.grocery.security.domain.User;
import com.mpholo.project.grocery.security.service.CurrentUserService;
import com.mpholo.project.grocery.util.ProductMappings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl  implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CurrentUserService currentUserService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
                              CurrentUserService currentUserService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.currentUserService = currentUserService;
    }

    // A product the current user owns, or 404.
    private Product ownedProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        if (!currentUserService.ownedByCurrent(product.getOwner())) {
            throw new ResourceNotFoundException();
        }
        return product;
    }

    @Override
    public Optional<ProductDTO> findById(Integer id) {
        return Optional.of(productMapper.ProductToProductDTO(ownedProduct(id)));
    }

    @Override
    public List<ProductDTO> findAll() {

        return productRepository.findByOwner(currentUserService.requireCurrentUser())
                .stream()
                .map(p ->{
                    ProductDTO productDto = productMapper.ProductToProductDTO(p);
                    productDto.setProductUrl(getProductUrl(p.getProductId()));
                    return  productDto;
                   }

                )
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
      ownedProduct(id); // authorize
      productRepository.deleteById(id);
    }

    @Override
    public void delete(ProductDTO productDTO) {
       productRepository.delete(productMapper.ProductDTOToProduct(productDTO));
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product product = productMapper.ProductDTOToProduct(productDTO);
        product.setOwner(currentUserService.requireCurrentUser());
        return  saveAndReturnDTO(product);
    }


    @Override
    public ProductDTO findByProductName(String name) {

        return  productRepository.findByOwnerAndProductName(currentUserService.requireCurrentUser(), name)
                .map(productMapper::ProductToProductDTO)
                .map(productDTO-> {
                    productDTO.setProductUrl(getProductUrl(productDTO.getProductId()));
                    return productDTO;
                })
                .orElseThrow(ResourceNotFoundException::new);

    }

    private ProductDTO saveAndReturnDTO(Product product) {
        Product saveProduct = productRepository.save(product);
        ProductDTO returnDTo=  productMapper.ProductToProductDTO(saveProduct);
        returnDTo.setProductUrl(getProductUrl(product.getProductId()));

        return returnDTo;
    }

    @Override
    public ProductDTO edit(Integer id, ProductDTO productDTO) {
        ownedProduct(id); // authorize
        Product product = productMapper.ProductDTOToProduct(productDTO);
        product.setProductId(id);
        product.setOwner(currentUserService.requireCurrentUser());

        return  saveAndReturnDTO(product);


    }

    @Override
    public ProductDTO patchProduct(Integer productId, ProductDTO productDTO) {
        Product product = productMapper.ProductDTOToProduct(productDTO);
        product.setProductId(productId);

        Optional<Product> foundProduct = productRepository.findById(productId);

        if(foundProduct.isPresent() && currentUserService.ownedByCurrent(foundProduct.get().getOwner())) {
            if (product.getProductName() != null && !product.getProductName().equals(foundProduct.get().getProductName())) {
                        foundProduct.get().setProductName(product.getProductName());
            }
            if (product.getProductDescription() != null && !product.getProductDescription().equals(foundProduct.get().getProductDescription())) {
                foundProduct.get().setProductDescription(product.getProductDescription());
            }
             return  saveAndReturnDTO(foundProduct.get());

        }

        return  productDTO;

    }

    @Override
    public List<ProductDTO> availableProducts(List<GroceryItemDTO> GroceryItemListDTO) {

        List<Product> productList= productRepository.findByOwner(currentUserService.requireCurrentUser());
        for(GroceryItemDTO item:GroceryItemListDTO) {
              productList.removeIf( p->p.equals(item.getProduct()) );
        }
        return productList.stream()
                .map(productMapper::ProductToProductDTO)
                .collect(Collectors.toList());
    }

    private String getProductUrl(Integer productID) {
        return ProductMappings.PRODUCTURL+"/"+productID;
    }
}
