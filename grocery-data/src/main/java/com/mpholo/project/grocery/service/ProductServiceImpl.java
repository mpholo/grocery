package com.mpholo.project.grocery.service;

import com.mpholo.project.grocery.domain.Product;
import com.mpholo.project.grocery.mapper.ProductMapper;
import com.mpholo.project.grocery.model.GroceryItemDTO;
import com.mpholo.project.grocery.model.ProductDTO;
import com.mpholo.project.grocery.repositories.ProductRepository;
import com.mpholo.project.grocery.util.ProductMappings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl  implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Optional<ProductDTO> findById(Integer id) {

        return Optional.of(productMapper.ProductToProductDTO(
                productRepository.findById(id)
                        .orElseThrow(ResourceNotFoundException::new)));
    }

    @Override
    public List<ProductDTO> findAll() {

        return productRepository.findAll()
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
      productRepository.deleteById(id);
    }

    @Override
    public void delete(ProductDTO productDTO) {
       productRepository.delete(productMapper.ProductDTOToProduct(productDTO));
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product product = productMapper.ProductDTOToProduct(productDTO);
        return  saveAndReturnDTO(product);
    }


    @Override
    public ProductDTO findByProductName(String name) {

        return  productRepository.findByProductName(name)
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
        Product product = productMapper.ProductDTOToProduct(productDTO);
        product.setProductId(id);

        return  saveAndReturnDTO(product);


    }

    @Override
    public ProductDTO patchProduct(Integer productId, ProductDTO productDTO) {
        Product product = productMapper.ProductDTOToProduct(productDTO);
        product.setProductId(productId);

        Optional<Product> foundProduct = productRepository.findById(productId);

        if(foundProduct.isPresent()) {
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

        List<Product> productList= productRepository.findAll();
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
